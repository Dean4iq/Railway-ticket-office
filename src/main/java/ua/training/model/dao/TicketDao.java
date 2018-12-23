package ua.training.model.dao;

import ua.training.model.entity.Ticket;

import java.util.List;

public interface TicketDao extends GenericDao<Integer, Ticket> {
    List<Ticket> findByTrainId(Integer id);
}
