package org.norsh.api.v1;

import org.norsh.api.config.ApiConfig;
import org.norsh.api.service.MessagingRequestService;
import org.norsh.model.transport.DataTransfer;
import org.norsh.model.transport.RestMethod;
import org.norsh.util.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Abstract base class for API version 1 controllers.
 * <p>
 * This class serves as the foundation for all API controllers in version 1, ensuring compatibility, future evolution,
 * reusability, and standardization across the Norsh platform.
 * </p>
 *
 * <h3>Purpose:</h3>
 * <ul>
 * <li>Provides a unified base for version 1 API controllers.</li>
 * <li>Facilitates consistent behaviors and patterns for APIs.</li>
 * <li>Allows for seamless upgrades and compatibility with future API versions.</li>
 * </ul>
 *
 * <h3>Key Features:</h3>
 * <ul>
 * <li>Ensures API controllers adhere to a standardized structure.</li>
 * <li>Simplifies future extensibility and integration of cross-cutting concerns.</li>
 * </ul>
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Danthur Lice
 * @see <a href="https://docs.norsh.org">Norsh Documentation</a>
 */
public abstract class ApiV1 {
	@Autowired
	protected MessagingRequestService messagingService;
	
	@Autowired
	protected HttpServletRequest servletRequest;

	/**
	 * Processes a Smart Element request, forwarding it to the queue and caching its status.
	 *
	 * @param request The unique request identifier.
	 * @param data    The payload to be sent to the processing queue.
	 * @return a response confirming that the request has been accepted for processing.
	 */
	protected ResponseEntity<Object> processRequest(String requestId, Object o) {
		RestMethod restMethod = RestMethod.valueOf(servletRequest.getMethod());
		DataTransfer requestTransfer = new DataTransfer(requestId,restMethod, o);
		
		DataTransfer responseTransfer =  new RestTemplate().postForObject(ApiConfig.getInstance().get("transfer.url", ""), requestTransfer, DataTransfer.class);
		return resquestStatusToResponseEntity(responseTransfer);
	}

	private ResponseEntity<Object> resquestStatusToResponseEntity(DataTransfer transport) {
		System.out.println(Converter.toJsonPretty(transport));
		
		return switch (transport.getStatus()) {
		case EXISTS -> new ResponseEntity<Object>(transport.toResponse(), HttpStatus.CONFLICT);
		case TIMEOUT -> new ResponseEntity<Object>(transport.toResponse(), HttpStatus.REQUEST_TIMEOUT);
		case NOT_FOUND -> new ResponseEntity<Object>(transport.toResponse(), HttpStatus.NOT_FOUND);
		case ERROR -> new ResponseEntity<Object>(transport.toResponse(), HttpStatus.INTERNAL_SERVER_ERROR);
		case INSUFFICIENT_BALANCE -> new ResponseEntity<Object>(transport.toResponse(), HttpStatus.PAYMENT_REQUIRED);
		case FORBIDDEN -> new ResponseEntity<Object>(transport.toResponse(), HttpStatus.FORBIDDEN);
		default -> ResponseEntity.status(HttpStatus.OK).body(transport.getResponseData());
		};
	}
}
