package us.pinette.fileupload.api.repositories;

import java.util.List;

/**
 * Defines functionality for all database repositories.
 */
public interface BaseRepository<T> {
    /**
     * Inserts / updates the specified entity.
     * @param entity The entity to insert / update.
     */
    void save(T entity);

    /**
     * Deletes the specified entity.
     * @param entity The entity to delete.
     */
    void delete(T entity);

    /**
     * Gets the entity from the database by id.
     * @param id The id of the entity.
     * @return The entity if it exists, null if it doesn't.
     */
    T get(long id);

    /**
     * Gets all entities of type T.
     * @return All persisted entities of type T.
     */
    List<T> getAll();
}
