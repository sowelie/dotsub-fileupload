package us.pinette.fileupload.api.models;

import java.time.Instant;

/**
 * Holds information about an uploaded file.
 */
public class FileMetaDataModel {
    private String title = "";
    private String description = "";
    private String contentType = "";
    private Instant createdDate = Instant.now();

    /**
     * Creates a new instance of FileMetaDataModel.
     * @param title The file's title.
     * @param description The file's description.
     */
    public FileMetaDataModel(final String title, final String description, final String contentType) {
        this.title = title;
        this.description = description;
        this.contentType = contentType;
    }

    /**
     * Creates a new instance of FileMetaDataModel.
     */
    public FileMetaDataModel() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
