package ua.training.model.dao;

import ua.training.model.entity.Train;

import java.util.List;

public interface TrainDao extends GenericDao<Integer, Train> {
    List<Train> getEveryTrain();
}
