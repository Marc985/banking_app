package com.prog3.exam.repository;

import java.util.List;

public interface CrudOperation <T>{
    public List<T> findAll();

    public T save(T tosave);
}
