package org.norsh.api.v1;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;

import org.norsh.api.config.ApiConfig;
import org.norsh.model.transport.DataTransfer;
import org.norsh.rest.RestRequest;
import org.norsh.rest.RestResponse;
import org.norsh.util.Converter;

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
	// @Autowired
	// protected HttpServletRequest servletRequest;

	/**
	 * Processes a Smart Element request, forwarding it to the queue and caching its status.
	 *
	 * @param restRequest The unique request identifier.
	 * @param data    The payload to be sent to the processing queue.
	 * @return a response confirming that the request has been accepted for processing.
	 * @throws URISyntaxException 
	 */
	protected void processRequest(RestRequest restRequest, RestResponse restResponse, String requestId, Object o) throws IOException, InterruptedException, URISyntaxException {
		DataTransfer requestTransfer = new DataTransfer(requestId, restRequest.getRestMethod(), o);
		String json = Converter.toJson(requestTransfer);
		
		System.out.println("\nRequest: " + json);
		
		HttpRequest httpRequest = HttpRequest.newBuilder()
				.uri(new URI(ApiConfig.getInstance().get("transfer.url", "")))
				.timeout(Duration.ofSeconds(30))
				.header("Content-Type", "application/json")
				.expectContinue(true)
				.POST(BodyPublishers.ofString(json))
				.build();

		HttpClient client = HttpClient.newBuilder().build();
		HttpResponse<String> httpResponse = client.send(httpRequest, BodyHandlers.ofString());

		System.out.println("\n\nResponse: " + httpResponse.body());
		
		DataTransfer responseTransfer = Converter.fromJson(httpResponse.body(), DataTransfer.class);

		switch (responseTransfer.getStatus()) {
			case EXISTS -> restResponse.setBody(209, responseTransfer.toResponse());
			case TIMEOUT -> restResponse.setBody(408, responseTransfer.toResponse());
			case NOT_FOUND -> restResponse.setBody(404, responseTransfer.toResponse());
			case ERROR -> restResponse.setBody(500, responseTransfer.toResponse());
			case INSUFFICIENT_BALANCE -> restResponse.setBody(402, responseTransfer.toResponse());
			case FORBIDDEN -> restResponse.setBody(403, responseTransfer.toResponse());
			default -> restResponse.setBody(responseTransfer.getResponseData());
		};
	}
}
