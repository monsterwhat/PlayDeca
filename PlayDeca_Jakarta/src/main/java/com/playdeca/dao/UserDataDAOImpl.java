package com.playdeca.dao;

import com.playdeca.models.UserData;
import jakarta.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author alvaro@playdeca.com
 */
@Named
public class UserDataDAOImpl implements UserDataDAO {

    private static List<UserData> people = new ArrayList<>();

    @Override
    public List<UserData> listAll() {
        return people;
    }

    @Override
    public UserData findByID(int id) {
        return people.stream().filter(e -> e.getEmail().equals(id)).findAny().orElse(null);
    }

    @Override
    public UserData register(UserData t) {
        people.add(t);
        return t;
    }

    @Override
    public boolean update(UserData t) {
        people = people.stream().map(p -> p.getEmail().equals(t.getEmail()) ? t : p).collect(Collectors.toList());
        return false;
    }

    @Override
    public void delete(int id) {
        people.removeIf(p -> p.getEmail().equals(id));
    }

    public boolean signIn(UserData user) {
        return false;
    }

    public UserData findByEmail(String email) {
        return null;
    }

    public boolean isConfirmed(UserData user) {
        return false;
    }

    public boolean findByID(UserData User) {
        return false;
    }

    public boolean seeInfo(UserData User) {
        return false;
    }

    public boolean exists(String email) {
        return false;
    }

    public UserData getInfo(UserData User) {
        return null;
    }

    public boolean confirmEmail(UserData user) {
        return false;
    }

    public boolean isBanned() {
        return false;
    }

    public boolean isBanned(UserData foundUser) {
        return false;
    }

    public boolean updatePassword(String password) {
        
        return false;
    }

    public UserData signIn(String Email, String password) {
        return null;
    }



}
