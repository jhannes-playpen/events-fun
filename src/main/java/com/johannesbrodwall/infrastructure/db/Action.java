package com.johannesbrodwall.infrastructure.db;

public interface Action<T> {

    T execute();

}
