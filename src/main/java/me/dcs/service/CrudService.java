package me.dcs.service;

import java.util.List;

public interface CrudService<Long, T> {
    List<T> findAllByVehicleId(String vehicleId);

    T findBySessionId(Long sessionId);

    T save(T entity);
}
