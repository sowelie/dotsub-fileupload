package us.pinette.fileupload.api.services;

import us.pinette.fileupload.api.entities.FileMetaData;
import us.pinette.fileupload.api.exceptions.FileStorageException;
import us.pinette.fileupload.api.models.FileMetaDataModel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

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

    /**
     * Gets all files.
     * @return All files that have been persisted.
     */
    List<FileMetaData> getAll();

    /**
     * Gets the FileMetaData by id.
     * @param id The id of the FileMetaData.
     * @return The FileMetaData if it exists, null if it does not.
     */
    FileMetaData get(long id);

    /**
     * Streams the file content to the specified output stream.
     * @param metaData The file to stream.
     * @param output The stream to write to.
     */
    void streamFile(FileMetaData metaData, OutputStream output) throws IOException;
}
