package com.playdeca.dao;

import java.util.List;

/**
 *
 * @author alvaro@playdeca.com
 * @param <T>
 */
public interface CRUD<T> {
    
    List<T> listAll();
    T findByID(int id);
    void register(T t);
    void update(T t);
    void delete(int id);
    
}
