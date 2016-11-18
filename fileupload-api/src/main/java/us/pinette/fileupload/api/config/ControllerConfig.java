package us.pinette.fileupload.api.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Configures the controllers for the web appplication.
 */
@Configuration
@ComponentScan("us.pinette.fileupload.api.controllers")
public class ControllerConfig {
}
