package us.pinette.fileupload.api.entities;

import javax.persistence.Entity;
import java.time.Instant;

/**
 * An entity which can be used to store information about an uploaded file in a database.
 */
@Entity
public class FileMetaData extends BaseEntity {
    private String title = "";
    private String description = "";
    private String contentType = "";
    private String fileName = "";
    private long contentLength = 0;
    private Instant createdDate = null;

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

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
