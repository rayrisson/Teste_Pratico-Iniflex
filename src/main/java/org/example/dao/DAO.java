package org.example.dao;

import java.util.List;

public interface DAO<T> {
    void create (T t);
    List<T> getAll ();
}
