package org.norsh.api.handlers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.norsh.exceptions.NorshException;
import org.norsh.exceptions.OperationException;
import org.norsh.rest.RestRequest;
import org.norsh.rest.RestResponse;
import org.norsh.rest.annotations.ThrowableHandler;

public class ApiThrowableHandler {
	@ThrowableHandler(Exception.class)
	public void exception(RestRequest request, RestResponse response, Throwable ex) throws IOException {
		if (response == null)
			return;
		
		response.setBody(500, buildResponse(ex));
		response.writeResponse();
	}

	@ThrowableHandler(NorshException.class)
	public void norshException(RestRequest request, RestResponse response, Throwable ex) throws IOException {
		response.setBody(500, buildResponse(ex));
		response.writeResponse();
	}

	@ThrowableHandler(OperationException.class)
	public void operationException(RestRequest request, RestResponse response, OperationException ex) throws IOException {
		response.setBody(500, buildResponse(ex));
		response.writeResponse();
	}

	private Map<String, Object> buildResponse(Throwable ex) {
		Map<String, Object> response = new HashMap<>();
		response.put("error", true);
		response.put("timestamp", System.currentTimeMillis());

		String message = ex.getMessage();

		if (message != null) {
			if (message.toLowerCase().contains("argument \"content\" is null")) {
				response.put("message", "Required request body is missing.");
			} else if (ex instanceof NorshException) {
				response.put("message", message);
			} else {
				message = null;
			}
		}

		if (message == null) {
			response.put("message", "An unexpected error occurred while processing the request.");
		}

		if (ex instanceof NorshException nex && !nex.getDetails().isEmpty()) {
			response.put("details", nex.getDetails());
		}

		ex.printStackTrace();

		return response;
	}
}
