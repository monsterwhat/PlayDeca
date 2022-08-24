package com.playdeca.dao;

import jakarta.inject.Named;
import java.util.List;

/**
 *
 * @author alvaro@playdeca.com
 * @param <T>
 */
@Named
public interface CRUD<T> {
    
    List<T> listAll();
    T findByID(int id);
    T register(T t);
    boolean update(T t);
    void delete(int id);
    
}
