package us.pinette.fileupload.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import us.pinette.fileupload.api.config.ControllerConfig;
import us.pinette.fileupload.api.config.PersistenceConfig;
import us.pinette.fileupload.api.config.ServiceConfig;

@Configuration
@EnableAutoConfiguration(exclude = HibernateJpaAutoConfiguration.class)
public class FileuploadApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(new Class[] {
				FileuploadApiApplication.class,
				PersistenceConfig.class,
				ServiceConfig.class,
				ControllerConfig.class
		}, args);
	}
}
