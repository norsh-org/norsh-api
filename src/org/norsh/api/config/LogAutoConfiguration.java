package org.norsh.api.config;

import org.norsh.util.Log;

/**
 * Auto-configuration for system logging.
 * <p>
 * This configuration ensures that logging is initialized before the web dispatcher, allowing early-stage logging during startup.
 * </p>
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Danthur Lice
 * @see <a href="https://docs.norsh.org">Norsh Documentation</a>
 */
//@AutoConfiguration(before = { DispatcherServletAutoConfiguration.class })
//@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
public class LogAutoConfiguration {

    /**
     * Provides the main logging instance.
     * <p>
     * This bean initializes the logging system with the current API configuration settings.
     * </p>
     *
     * @return an instance of {@link Log}.
     */
//    @Bean
//    @Lazy
    public Log log() {
        return new Log(ApiConfig.getInstance().getLogConfig());
    }
}
