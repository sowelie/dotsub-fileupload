package us.pinette.fileupload.api.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import us.pinette.fileupload.api.entities.FileMetaData;
import us.pinette.fileupload.api.exceptions.FileStorageException;
import us.pinette.fileupload.api.models.FileMetaDataModel;
import us.pinette.fileupload.api.repositories.FileMetaDataRepository;
import us.pinette.fileupload.api.services.FileStorageService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Default implementation of FileStorageService which writes file data to the local file system and saves any meta
 * data using the FileMetaDataRepository.
 */
@Service
public class LocalFileStorageService implements FileStorageService {
    @Autowired
    private FileMetaDataRepository metaDataRepository = null;

    @Value("${fileupload.storageDirectory:}")
    private String storageDirectory = "";

    @Override
    @Transactional
    public FileMetaData addFile(final FileMetaDataModel model, final String extension, final InputStream input) throws FileStorageException {
        // if no directory is provided, use the temp directory
        if (StringUtils.isEmpty(storageDirectory)) {
            storageDirectory = System.getProperty("java.io.tmpdir");
        }

        try {
            // remove any special characters, replace spaces with underscores
            String fileName = cleanFileName(model.getTitle());

            // add the extension, remove any period from the supplied extension, that way
            // api users can pass the extension with or without a period
            final String processedExtension = "." + extension.replace(".", "");

            int index = 0;
            String fileNameSuffix = "";

            // check to see if the file already exists
            while (Files.exists(Paths.get(storageDirectory, fileName + fileNameSuffix + processedExtension))) {
                fileNameSuffix = "_" + (++index);
            }

            // add the suffix and extension
            fileName += fileNameSuffix + processedExtension;

            // write the file to disk
            final long contentLength = Files.copy(input, Paths.get(storageDirectory, fileName));

            // create the file meta data
            FileMetaData result = new FileMetaData();
            result.setTitle(model.getTitle());
            result.setDescription(model.getDescription());
            result.setCreatedDate(model.getCreatedDate());
            result.setContentLength(contentLength);
            result.setContentType(model.getContentType());
            result.setFileName(fileName);

            // persist the meta data
            metaDataRepository.save(result);

            return result;
        } catch (Exception ex) {
            throw new FileStorageException("An exception occurred storing the specified file.", ex);
        }
    }

    @Override
    public List<FileMetaData> getAll() {
        return metaDataRepository.getAll();
    }

    @Override
    public FileMetaData get(long id) {
        return metaDataRepository.get(id);
    }

    @Override
    public void streamFile(FileMetaData metaData, OutputStream output) throws IOException {
        Files.copy(Paths.get(storageDirectory, metaData.getFileName()), output);
    }

    private String cleanFileName(final String input) {
        return input.replace(" ", "_").replaceAll("[^\\w\\d]", "");
    }
}
