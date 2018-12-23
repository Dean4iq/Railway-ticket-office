package ua.training.model.dao;

import ua.training.model.entity.Seat;

import java.util.List;

public interface SeatDao extends GenericDao<Integer, Seat> {
    List<Seat> findByTrainId(Integer id);
}
