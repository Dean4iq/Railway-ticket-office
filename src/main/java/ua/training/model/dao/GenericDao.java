package ua.training.model.dao;

import java.util.List;

public interface GenericDao<T> extends AutoCloseable{
    void create(T entity);
    T findById(Object id);
    List<T> findAll();
    void update(T entity);
    void delete(int id);
}
