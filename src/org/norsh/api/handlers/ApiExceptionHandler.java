package org.norsh.api.handlers;

import java.util.HashMap;
import java.util.Map;

import org.norsh.exceptions.ArgumentException;
import org.norsh.exceptions.InternalException;
import org.norsh.exceptions.NorshException;
import org.norsh.exceptions.OperationException;
import org.norsh.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

/**
 * Global exception handler for API errors in the Norsh ecosystem.
 * <p>
 * This class provides centralized exception handling for all API controllers, ensuring a structured and consistent
 * response format for both known and unexpected errors.
 * </p>
 *
 * <h2>Exception Handling:</h2>
 * <ul>
 * <li>{@link OperationException} and {@link ArgumentException} - Handled as {@code 400 BAD REQUEST}, indicating
 * client-side errors.</li>
 * <li>{@link NorshException} and {@link InternalException} - Handled as {@code 500 INTERNAL SERVER ERROR}, indicating
 * application-level failures.</li>
 * <li>{@link Exception} - Catches all unhandled exceptions, preventing internal stack traces from leaking.</li>
 * </ul>
 *
 * <h2>Response Format:</h2>
 * <p>
 * All error responses follow this structured JSON format:
 * </p>
 * 
 * <pre>
 * {
 *   "error": true,
 *   "timestamp": 1705439484231,
 *   "message": "Detailed error message",
 *   "details": ["Optional list of error details"]
 * }
 * </pre>
 *
 * <h2>HTTP Status Codes:</h2>
 * <ul>
 * <li>{@code 400 BAD REQUEST} - For known validation or logical errors.</li>
 * <li>{@code 500 INTERNAL SERVER ERROR} - For unexpected runtime errors.</li>
 * </ul>
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Danthur Lice
 * @see <a href="https://docs.norsh.org">Norsh Documentation</a>
 */
@RestControllerAdvice
public class ApiExceptionHandler {
	@Autowired
	private Log log;

	/**
	 * Handles validation and argument exceptions.
	 * <p>
	 * Returns a structured response with a {@code 400 BAD REQUEST} status when client-side validation errors occur.
	 * </p>
	 *
	 * @param ex The exception instance.
	 * @return A structured JSON response with an appropriate error message.
	 */
	@ExceptionHandler(value = { OperationException.class, ArgumentException.class })
	public ResponseEntity<Map<String, Object>> handleBadRequest(OperationException ex) {
		Map<String, Object> response = buildResponse(ex);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handles internal application exceptions.
	 * <p>
	 * This method catches {@link NorshException} and {@link InternalException}, returning a structured response with a
	 * {@code 500 INTERNAL SERVER ERROR}.
	 * </p>
	 *
	 * @param ex The exception instance.
	 * @return A structured JSON response with an appropriate error message.
	 */
	@ExceptionHandler(value = { InternalException.class, NorshException.class })
	public ResponseEntity<Map<String, Object>> handleNorshException(NorshException ex) {
		Map<String, Object> response = buildResponse(ex);
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Handles generic unexpected exceptions.
	 * <p>
	 * This method captures all unhandled exceptions, preventing internal stack traces from being exposed in API responses.
	 * </p>
	 *
	 * @param ex The exception instance.
	 * @return A structured JSON response with a generic error message.
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
		Map<String, Object> response = buildResponse(ex);

		if (ex instanceof NoHandlerFoundException) {
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Builds a structured response map for any exception.
	 * <p>
	 * This method ensures that all error responses follow a standard format, including timestamps and optional detailed
	 * messages.
	 * </p>
	 *
	 * @param ex The exception instance.
	 * @return A structured error response map.
	 */
	private Map<String, Object> buildResponse(Exception ex) {
		Map<String, Object> response = new HashMap<>();
		response.put("error", true);
		response.put("timestamp", System.currentTimeMillis());

		String message = ex.getMessage();
		if (message != null) {
			if (message.contains("body is missing")) {
				response.put("message", "Required request body is missing.");
			} else if (ex instanceof HttpMessageNotReadableException) {
				message = "Request body contains invalid or unexpected data.";

				if (ex.getCause() instanceof InvalidFormatException) {
					InvalidFormatException e = (InvalidFormatException) ex.getCause();
					if (e.getPath().size() > 0) {
						message += String.format(" Please check '%s' field.", e.getPath().get(0).getFieldName());
					}
				}
				
				response.put("message", message);
			} else if (ex instanceof NorshException) {
				response.put("message", ex.getMessage());
			} else {
				response.put("message", "An unexpected error occurred while processing the request.");
			}
		}

		if (ex instanceof NorshException nex && !nex.getDetails().isEmpty()) {
			response.put("details", nex.getDetails());
		}

		ex.printStackTrace();
		log.error(ex.getMessage(), ex);

		return response;
	}
}
