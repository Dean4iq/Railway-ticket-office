package ua.training.model.dao;

import java.sql.SQLException;
import java.util.List;

public interface GenericDao<K, T> extends AutoCloseable {
    void create(T entity) throws SQLException;

    T findById(K id);

    List<T> findAll();

    void update(T entity);

    void delete(T id);
}
