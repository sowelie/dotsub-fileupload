package us.pinette.fileupload.api.servicecs;

import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.mock.env.MockPropertySource;
import org.springframework.test.context.ContextConfiguration;
import us.pinette.fileupload.api.TestUtils;

import org.mockito.Mockito.*;

/**
 * Tests the functionality of the local file storage service.
 */
@ContextConfiguration(classes = { LocalFileStorageServiceTest.Config.class })
public class LocalFileStorageServiceTest {
    @Test
    public void canSaveFile() {

    }

    @Configuration
    static class Config {
        @Bean
        public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
            return TestUtils.configureMockProperties(new MockPropertySource().withProperty("location", ""));
        }
    }
}
