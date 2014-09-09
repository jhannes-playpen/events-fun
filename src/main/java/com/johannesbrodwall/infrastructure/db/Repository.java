package com.johannesbrodwall.infrastructure.db;

import java.util.List;

public interface Repository<T> {

    List<T> findAll();

    int insert(T entity);

    T fetch(long id);

}
