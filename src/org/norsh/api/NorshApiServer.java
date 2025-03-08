package org.norsh.api;

import java.util.Calendar;

import org.norsh.api.config.ApiConfig;
import org.norsh.api.handlers.ApiThrowableHandler;
import org.norsh.api.v1.crypto.AddressApiV1;
import org.norsh.api.v1.elements.ElementV1;
import org.norsh.api.v1.transactions.PaymentV1;
import org.norsh.rest.HttpServer;
import org.norsh.util.Log;

/**
 * Main class for the Norsh Blockchain API.
 * <p>
 * This server is the primary API layer for handling blockchain operations, including transaction processing, ledger
 * synchronization, and cryptographic verification. It is built with a high-performance native HTTP server to ensure
 * minimal overhead and optimized data exchange.
 * </p>
 *
 * <h2>Key Features:</h2>
 * <ul>
 * <li>Lightweight HTTP server optimized for Norsh operations.</li>
 * <li>Asynchronous processing of ledger transactions.</li>
 * <li>Dynamic logging and configurable system parameters.</li>
 * <li>Structured exception handling with {@link ApiThrowableHandler}.</li>
 * </ul>
 *
 * <h2>Startup Process:</h2>
 * <ol>
 * <li>Initializes localization and default configurations via {@link ApiConfig}.</li>
 * <li>Bootstraps the native HTTP server on port 9090.</li>
 * <li>Registers blockchain-related API endpoints.</li>
 * <li>Configures centralized exception handling.</li>
 * </ol>
 *
 * @author Danthur Lice
 * @since 1.0.0
 * @version 1.0.0
 * @see <a href="https://docs.norsh.org">Norsh Documentation</a>
 */
public class NorshApiServer {

	/**
	 * Entry point for the Norsh API Server.
	 * <p>
	 * This method initializes the API configuration, sets up logging, registers endpoints, and starts the lightweight HTTP
	 * server.
	 * </p>
	 *
	 * <h3>Startup Configuration:</h3>
	 * <ul>
	 * <li>Localization defaults are set via {@link ApiConfig#initializeDefaultLocalization()}.</li>
	 * <li>Logging is configured dynamically based on application settings.</li>
	 * <li>Exception handling is centralized using {@link ApiThrowableHandler}.</li>
	 * <li>REST endpoints are registered for blockchain-related operations.</li>
	 * </ul>
	 *
	 * @param args Command-line arguments (not required for standard execution).
	 */
	public static void main(String[] args) {
		ApiConfig.initializeDefaultLocalization();

		Log log = new Log(ApiConfig.getInstance().getLogConfig());
		log.system("Norsh API Server ○ ●●");
		log.system("Developed by " + String.join(", ", "Danthur Lice") + " and contributors.");
		log.system(String.format("Copyright © 2024-%s Norsh. All rights reserved", Calendar.getInstance().get(Calendar.YEAR)));
		log.system(String.format("Server address: %s:%s", ApiConfig.getInstance().getSpringProperties().get("server.host"), ApiConfig.getInstance().getSpringProperties().get("server.port")));

		HttpServer httpServer = new HttpServer();
		httpServer.setExceptionHandler(new ApiThrowableHandler());
		httpServer.addEndpoint(AddressApiV1.class);
		httpServer.addEndpoint(PaymentV1.class);
		httpServer.addEndpoint(ElementV1.class);

		httpServer.start(9090, false);

		log.system("Server started.");
		log.breakLine();
	}
}
