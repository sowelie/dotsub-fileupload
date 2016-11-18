package us.pinette.fileupload.tests.controllers;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import us.pinette.fileupload.api.FileuploadApiApplication;
import us.pinette.fileupload.api.controllers.FileController;
import us.pinette.fileupload.api.entities.FileMetaData;
import us.pinette.fileupload.api.exceptions.FileStorageException;
import us.pinette.fileupload.api.models.FileMetaDataModel;
import us.pinette.fileupload.api.services.FileStorageService;

import java.io.InputStream;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests the functionality of the FileController.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { FileuploadApiApplication.class, FileControllerTest.Config.class })
@AutoConfigureMockMvc
public class FileControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FileStorageService fileStorageService = null;

    private byte[] tempData = null;

    @Test
    public void canUploadFile() throws Exception {
        final byte[] testFileData = new byte[] { 123 };
        final MockMultipartFile mockFile = new MockMultipartFile("file", "test file.txt", "text/plain", testFileData);

        // perform the upload
        mockMvc.perform(
                fileUpload("/file")
                        .file(mockFile)
                        .param("title", "test file")
                        .param("description", "test file description"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{title: 'test file', description: 'test file description', id: 1, " +
                    "contentType: 'text/plain', contentLength: 1 }"));

        // set up a captor for the model
        final ArgumentCaptor<FileMetaDataModel> modelCaptor = ArgumentCaptor.forClass(FileMetaDataModel.class);

        // verify the storage service was called
        verify(fileStorageService).addFile(modelCaptor.capture(), eq("txt"), any(InputStream.class));

        assertEquals("test file", modelCaptor.getValue().getTitle());
        assertEquals("test file description", modelCaptor.getValue().getDescription());
        assertEquals("text/plain", modelCaptor.getValue().getContentType());
        assertArrayEquals(testFileData, tempData);
    }

    @Test
    public void canListFiles() throws Exception {
        mockMvc.perform(get("/file"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[{title: 'test file', description: 'test file description', id: 1, " +
                        "contentType: 'text/plain', contentLength: 1 }]"));
    }

    @Before
    public void before() throws FileStorageException {
        reset(fileStorageService);

        final FileMetaData fileMetaData = new FileMetaData();
        fileMetaData.setTitle("test file");
        fileMetaData.setDescription("test file description");
        fileMetaData.setId(1L);
        fileMetaData.setContentType("text/plain");
        fileMetaData.setContentLength(1);

        when(fileStorageService.getAll()).thenReturn(Collections.singletonList(fileMetaData));

        // mock the file storage service
        when(fileStorageService.addFile(any(FileMetaDataModel.class), anyString(), any(InputStream.class)))
                .thenAnswer(invocation -> {
                    final FileMetaDataModel model = invocation.getArgumentAt(0, FileMetaDataModel.class);
                    final FileMetaData result = new FileMetaData();
                    result.setId(1L);
                    result.setTitle(model.getTitle());
                    result.setContentType(model.getContentType());
                    result.setContentLength(1);
                    result.setCreatedDate(model.getCreatedDate());
                    result.setDescription(model.getDescription());

                    // read the input data
                    tempData = IOUtils.toByteArray(invocation.getArgumentAt(2, InputStream.class));

                    return result;
                });
    }

    @Configuration
    static class Config {
        @Bean
        public FileStorageService fileStorageService() {
            return mock(FileStorageService.class);
        }

        @Bean
        public FileController fileController() {
            return new FileController();
        }
    }
}
