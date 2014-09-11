package com.johannesbrodwall.infrastructure.db;

import java.util.List;

public interface Repository<T> {

    List<T> findAll();

    void insert(T entity);

    T fetch(Integer id);

}
