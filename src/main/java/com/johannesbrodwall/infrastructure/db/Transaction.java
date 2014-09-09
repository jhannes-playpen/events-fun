package com.johannesbrodwall.infrastructure.db;

public interface Transaction extends AutoCloseable {

    @Override
    void close();

    void setCommit();

}
