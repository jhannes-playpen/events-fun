package com.johannesbrodwall.infrastructure.db;

import java.util.List;

public interface Repository<T> {

    List<T> findAll();

    long insert(T category);

    T fetch(long id);

}
