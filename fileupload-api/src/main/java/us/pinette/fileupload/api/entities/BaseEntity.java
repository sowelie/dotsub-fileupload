package us.pinette.fileupload.api.entities;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * The base class for all database entities.
 */
@MappedSuperclass
public abstract class BaseEntity
{
    @Id
    @GeneratedValue
    @Column(name = "Id")
    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    private Long id = null;
}
