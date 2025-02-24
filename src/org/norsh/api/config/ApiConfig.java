package org.norsh.api.config;

import org.norsh.config.Config;
import org.norsh.config.LogConfig;

/**
 * API configuration loader and manager.
 * <p>
 * This class loads and provides access to API-specific configuration settings, including logging configurations and
 * server properties.
 * </p>
 *
 * <h2>Features:</h2>
 * <ul>
 * <li>Automatically loads API configurations from predefined locations.</li>
 * <li>Provides access to logging configurations.</li>
 * <li>Retrieves server properties for application setup.</li>
 * </ul>
 *
 * <h2>Configuration File Locations:</h2>
 * <ul>
 * <li>Default: {@code /etc/norsh/api.json}</li>
 * <li>Environment Variable: {@code NORSH_API_CONFIG}</li>
 * </ul>
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Danthur Lice
 * @see LogConfig
 * @see <a href="https://docs.norsh.org">Norsh Documentation</a>
 */

public class ApiConfig extends Config {
	private static final ApiConfig instance = new ApiConfig();

	static {
		instance.load("NORSH_API_CONFIG", "/etc/norsh/api.json");
	}

	public static ApiConfig getInstance() {
		return instance;
	}
}
