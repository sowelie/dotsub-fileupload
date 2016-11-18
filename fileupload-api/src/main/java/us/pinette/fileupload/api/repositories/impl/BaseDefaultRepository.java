package us.pinette.fileupload.api.repositories.impl;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import us.pinette.fileupload.api.repositories.BaseRepository;

import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * The base implementation for all repositories.
 */
public class BaseDefaultRepository<T> implements BaseRepository<T> {
    @SuppressWarnings("unchecked")
    public BaseDefaultRepository() {
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    protected Criteria createCriteria(Class<?> clazz) {
        return this.createCriteria(clazz, null);
    }

    protected Criteria createCriteria(Class<?> clazz, String alias) {
        return this.sessionFactory.getCurrentSession().createCriteria(clazz, alias);
    }

    public void save(T entity) {
        this.sessionFactory.getCurrentSession().saveOrUpdate(entity);
    }

    public void delete(T entity)
    {
        getSession().delete(entity);
    }

    public Session getSession() {
        return this.sessionFactory.getCurrentSession();
    }

    @SuppressWarnings("unchecked")
    public List<T> getAll() {
        return this.sessionFactory.getCurrentSession().createCriteria(this.persistentClass).list();
    }

    @SuppressWarnings("unchecked")
    public T get(long id) {
        return (T)this.sessionFactory.getCurrentSession().createCriteria(this.persistentClass).add(Restrictions.eq("id", id)).uniqueResult();
    }

    @Autowired
    protected SessionFactory sessionFactory = null;

    protected Class<T> persistentClass = null;
}
