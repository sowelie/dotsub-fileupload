package us.pinette.fileupload.tests.servicecs;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.util.io.IOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.mock.env.MockPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import us.pinette.fileupload.api.entities.FileMetaData;
import us.pinette.fileupload.api.models.FileMetaDataModel;
import us.pinette.fileupload.api.repositories.FileMetaDataRepository;
import us.pinette.fileupload.api.services.impl.LocalFileStorageService;
import us.pinette.fileupload.tests.TestUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Tests the functionality of the local file storage service.
 */
@ContextConfiguration(classes = { LocalFileStorageServiceTest.Config.class })
@RunWith(SpringRunner.class)
public class LocalFileStorageServiceTest {
    private static final String TEST_FILE_PATH = System.getProperty("java.io.tmpdir");

    @Autowired
    private LocalFileStorageService service = null;

    @Autowired
    private FileMetaDataRepository metaDataRepository = null;

    @Test
    public void canSaveFile() throws Exception {
        // throw in some special characters that should be removed
        final FileMetaDataModel model = new FileMetaDataModel("test file$:", "A test file.", "text/plain");
        final byte[] testFileData = new byte[] { 123 };

        ByteArrayInputStream inputStream = null;

        try {
            inputStream = new ByteArrayInputStream(testFileData);

            // store the file
            final FileMetaData result = service.addFile(model, ".txt", inputStream);

            // make sure a file meta data entity was returned
            assertNotNull(result);

            // make sure the properties were copied from the model
            assertEquals(model.getTitle(), result.getTitle());
            assertEquals(model.getDescription(), result.getDescription());
            assertEquals(model.getCreatedDate(), result.getCreatedDate());
            assertEquals("test_file.txt", result.getFileName());

            // make sure content length and content type were stored
            assertEquals(model.getContentType(), result.getContentType());
            assertEquals(testFileData.length, result.getContentLength());

            final File file = new File(TEST_FILE_PATH, result.getFileName());

            // make sure the file was saved
            assertTrue(file.exists());

            // make sure the right file content was saved
            assertArrayEquals(testFileData, Files.readAllBytes(file.toPath()));

            final ArgumentCaptor<FileMetaData> metaDataCaptor = ArgumentCaptor.forClass(FileMetaData.class);

            // make sure the entity was persisted
            verify(metaDataRepository).save(metaDataCaptor.capture());

            // make sure it was the returned entity that was persisted
            assertEquals(result, metaDataCaptor.getValue());
        } finally {
            // delete the created file
            Files.deleteIfExists(Paths.get(TEST_FILE_PATH, "test_file.txt"));
            IOUtil.closeQuietly(inputStream);
        }
    }

    @Test
    public void canSaveFileSameName() throws Exception {
        final FileMetaDataModel model = new FileMetaDataModel("test file", "A test file.", "text/plain");
        final byte[] testFileData = new byte[] { 123 };

        ByteArrayInputStream inputStream = null;

        try {
            inputStream = new ByteArrayInputStream(testFileData);

            // store the file
            final FileMetaData result = service.addFile(model, ".txt", inputStream);
            assertEquals("test_file.txt", result.getFileName());

            final File file = new File(TEST_FILE_PATH, result.getFileName());

            // make sure the file was saved
            assertTrue(file.exists());

            // save another with the same name, make sure a file with a suffix is created
            final FileMetaData result2 = service.addFile(model, ".txt", inputStream);
            assertEquals("test_file_1.txt", result2.getFileName());

            final File file2 = new File(TEST_FILE_PATH, result2.getFileName());

            // make sure the file was saved
            assertTrue(file2.exists());
            assertNotEquals(result.getFileName(), result2.getFileName());
        } finally {
            Files.deleteIfExists(Paths.get(TEST_FILE_PATH, "test_file.txt"));
            Files.deleteIfExists(Paths.get(TEST_FILE_PATH, "test_file_1.txt"));
            IOUtil.closeQuietly(inputStream);
        }
    }

    @Test
    public void canGetAll() {
        final FileMetaData metaData = new FileMetaData();
        when(metaDataRepository.getAll()).thenReturn(Collections.singletonList(metaData));

        List<FileMetaData> result = service.getAll();

        assertEquals(metaData, result.get(0));
        verify(metaDataRepository).getAll();
    }

    @Before
    public void before() {
        reset(metaDataRepository);
    }

    @Configuration
    static class Config {
        @Bean
        public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
            return TestUtils.configureMockProperties(new MockPropertySource().withProperty("fileupload.storageDirectory", TEST_FILE_PATH));
        }

        @Bean
        public LocalFileStorageService localFileStorageService() {
            return new LocalFileStorageService();
        }

        @Bean
        public FileMetaDataRepository metaDataRepository() {
            return mock(FileMetaDataRepository.class);
        }
    }
}
