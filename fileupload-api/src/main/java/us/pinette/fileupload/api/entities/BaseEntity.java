package us.pinette.fileupload.api.entities;

/**
 * The base class for all database entities.
 */
public class BaseEntity {
    private int id = 0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
