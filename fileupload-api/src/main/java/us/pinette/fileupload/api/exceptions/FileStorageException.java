package us.pinette.fileupload.api.exceptions;

/**
 * An exception that can be thrown by the LocalFileStorageService.
 */
public class FileStorageException extends Exception {
    public FileStorageException(final String message) {
        super(message);
    }

    public FileStorageException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
