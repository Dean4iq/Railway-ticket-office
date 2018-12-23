package ua.training.model.dao;

import ua.training.model.entity.Wagon;

import java.util.List;

public interface WagonDao extends GenericDao<Integer, Wagon> {
    List<Wagon> findByTrainId(Integer id);
}
