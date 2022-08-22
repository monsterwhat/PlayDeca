package com.playdeca.dao;

import com.playdeca.models.UserData;
import java.util.List;

/**
 *
 * @author alvaro@playdeca.com
 */
public class UserDataDAOImpl implements UserDataDAO{

    @Override
    public List<UserData> listAll() {
        return null;
    }

    @Override
    public UserData findByID(int id) {
        return null;
    }

    @Override
    public void register(UserData t) {
    }

    @Override
    public void update(UserData t) {
    }

    @Override
    public void delete(int id) {
    }

    public boolean signIn(UserData user) {
        return false;
    }

    public boolean findByEmail(String email) {
        return false;
    }

    public boolean isConfirmed(String email) {
        return false;
    }

    public boolean isBanned(String extra_info) {
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

    public Object findAll() {
        return null;
    }

    public Object findAllPublic() {
        return null;
    }


    
}
