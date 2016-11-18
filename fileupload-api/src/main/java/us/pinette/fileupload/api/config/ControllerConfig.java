package us.pinette.fileupload.api.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.support.OpenSessionInViewFilter;

/**
 * Configures the controllers for the web appplication.
 */
@Configuration
@ComponentScan("us.pinette.fileupload.api.controllers")
public class ControllerConfig {
    @Bean
    public FilterRegistrationBean registerOpenEntityManagerInViewFilterBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        OpenSessionInViewFilter filter = new OpenSessionInViewFilter();
        registrationBean.setFilter(filter);
        registrationBean.setOrder(5);
        return registrationBean;
    }
}
