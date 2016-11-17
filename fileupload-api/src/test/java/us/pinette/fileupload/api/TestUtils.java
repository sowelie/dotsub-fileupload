package us.pinette.fileupload.api;

import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.mock.env.MockPropertySource;

/**
 * Common utilities for tests.
 */
public class TestUtils {
    /**
     * Configures a PropertySourcesPlaceholderConfigurer with mock properties.
     * @param propertySource
     * @return
     */
    public static PropertySourcesPlaceholderConfigurer configureMockProperties(final MockPropertySource propertySource) {
        final PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        final MutablePropertySources propertySources = new MutablePropertySources();
        propertySources.addFirst(propertySource);

        configurer.setPropertySources(propertySources);

        return configurer;
    }
}
