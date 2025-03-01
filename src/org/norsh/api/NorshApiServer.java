package org.norsh.api;

import java.util.Calendar;

import org.norsh.api.config.ApiConfig;
import org.norsh.api.v1.crypto.AddressApiV1;
import org.norsh.api.v1.elements.ElementV1;
import org.norsh.api.v1.transactions.PaymentV1;
import org.norsh.rest.HttpServer;
import org.norsh.util.Logger;

/**
 * Main class for the Norsh Distributed Blockchain Server (NDBServer).
 * <p>
 * This server acts as the central API layer for handling blockchain operations, including transaction processing, ledger 
 * synchronization, and cryptographic verification. It is built with Spring Boot for rapid deployment and scalability.
 * </p>
 *
 * <h2>Key Features:</h2>
 * <ul>
 *   <li>Integrates with Spring Boot for streamlined application setup.</li>
 *   <li>Handles ledger transactions asynchronously for improved efficiency.</li>
 *   <li>Configures logging and system parameters dynamically at runtime.</li>
 *   <li>Implements best practices for secure API handling and modular architecture.</li>
 * </ul>
 *
 * <h2>Startup Process:</h2>
 * <ol>
 *   <li>Initializes localization and default configurations.</li>
 *   <li>Bootstraps the Spring Boot application with configured settings.</li>
 *   <li>Logs system metadata and operational parameters.</li>
 * </ol>
 *
 * @author Danthur Lice
 * @license NCL-139
 * @since 1.0.0
 * @version 1.0.0
 * @see <a href="https://docs.norsh.org">Norsh Documentation</a>
 */
//@SpringBootApplication
//@ComponentScan("org.norsh.api")
//@EnableAutoConfiguration(exclude = { WebMvcAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class })
public class NorshApiServer {

	//@Autowired
	private static Logger log;

	/**
	 * Entry point for the Norsh API Server.
	 * <p>
	 * This method initializes the Spring Boot environment, sets up application properties, and launches the server.
	 * </p>
	 *
	 * <h3>Startup Configuration:</h3>
	 * <ul>
	 *   <li>Localization defaults are set via {@link ApiConfig#initializeDefaultLocalization()}.</li>
	 *   <li>Application properties (e.g., server host, port) are loaded dynamically.</li>
	 *   <li>Banner mode is disabled for a cleaner startup output.</li>
	 * </ul>
	 *
	 * @param args Command-line arguments passed to the application (not required for standard execution).
	 * @throws Exception If any initialization error occurs during startup.
	 */
	public static void main(String[] args) {
		ApiConfig.initializeDefaultLocalization();
		log = new Logger(ApiConfig.getInstance().getLogConfig());
		
		
		HttpServer httpServer = new HttpServer();
		httpServer.addEndpoint(AddressApiV1.class);
		httpServer.addEndpoint(PaymentV1.class);
		httpServer.addEndpoint(ElementV1.class);
		
		bootstrap();
		
		httpServer.start(9090);
		//bootstrap();
//		SpringApplication app = new SpringApplication(NorshApiServer.class);
//		app.setBannerMode(Banner.Mode.OFF);
//		app.setDefaultProperties(ApiConfig.getInstance().getSpringProperties());
//		app.run();
	}

	/**
     * Logs system configuration details during startup.
     * <p>
	 * This method runs automatically after the server is initialized and records essential information,
	 * including system metadata, author details, and server connection properties.
     * </p>
     */
	//@PostConstruct
	private static void bootstrap() {
		log.system("Norsh API Server");
		log.system("Developed by " + String.join(", ", "Danthur Lice") + " and contributors.");
		log.system(String.format("Copyright © 2024-%s Norsh. All rights reserved", Calendar.getInstance().get(Calendar.YEAR)));
		log.system(String.format("Server address: %s:%s", ApiConfig.getInstance().getSpringProperties().get("server.host"), ApiConfig.getInstance().getSpringProperties().get("server.port")));
		log.system("Server started.");
		log.breakLine();
	}
}