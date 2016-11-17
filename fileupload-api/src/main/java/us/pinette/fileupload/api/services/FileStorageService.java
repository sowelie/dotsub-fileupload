package us.pinette.fileupload.api.services;

import us.pinette.fileupload.api.entities.FileMetaData;
import us.pinette.fileupload.api.exceptions.FileStorageException;
import us.pinette.fileupload.api.models.FileMetaDataModel;

import java.io.InputStream;

/**
 * Defines functionality required for storing files uploaded by users.
 */
public interface FileStorageService {
    /**
     * Saves the specified file and its meta data.
     * @param model The meta data for the file.
     * @param input The input stream that can be read to retrieve the file data.
     * @return Returns the persisted FileMetaData entity.
     */
    FileMetaData addFile(FileMetaDataModel model, String extension, InputStream input) throws FileStorageException;
}
