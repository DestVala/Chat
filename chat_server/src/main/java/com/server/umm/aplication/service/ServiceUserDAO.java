package com.server.umm.aplication.service;

import com.server.umm.aplication.entity.user.User;
import com.server.umm.aplication.exceptions.EntityExistException;
import com.server.umm.aplication.repository.UserCRUD;
import com.server.umm.aplication.repository.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ServiceUserDAO implements UserDAO {

    /**
     * Class to create queries to DB - User Type are inserted and selected.
     * TODO - query to update status of user eg. login/logout and textStatus
     * TODO - query to delete user/users
     */

    UserCRUD userCRUD;

    @Autowired
    public ServiceUserDAO(UserCRUD userCRUD) {
        this.userCRUD = userCRUD;
    }

    //method to registered user -> save into data base
    @Override
    public boolean save(User user) throws EntityExistException{
        // create unique insert -> problem with keep unique with spring
        Optional<User> unique = userCRUD.findByNick(user.getNick());
        if(unique.isPresent()) throw new EntityExistException("This nick name already exist");
        userCRUD.save(user);
        Optional<User> existingTest = userCRUD.findByNick(user.getNick());
        if (existingTest.isPresent()) return true;
        else return false;
    }

    @Override
    public boolean updateUser(User user) {

        userCRUD.updateUserByNick(user.getNick());
        Optional<User> existingTest = userCRUD.findByNick(user.getNick());
        if (existingTest.isPresent()) return true;
        else return false;
    }

    public boolean updateLogStatusUser(String nick) {

        userCRUD.updateUserByNick(nick);
        Optional<User> existingTest = userCRUD.findByNick(nick);
        if (existingTest.isPresent()) return true;
        else return false;
    }

    public boolean updateLogoutStatusUser(String nick) {

        userCRUD.updateLogoutUserStatus(nick);
        Optional<User> existingTest = userCRUD.findByNick(nick);
        if (existingTest.isPresent()) return true;
        else return false;
    }

    @Override
    public List<User> getUsers() {
        List<User> users = userCRUD.findAll();
        return users;
    }

    @Override
    public User getOneUser(String onlyNick) {
      User user = userCRUD.findByNick(onlyNick).get();
        return user;
    }

    public User getUserById(UUID id) {
        Optional<User> user = userCRUD.findByUserId(id);
        return user.get();
    }

    public User getUserByNick(String nick) {
        Optional<User> user = userCRUD.findByNick(nick);
        return user.get();
    }

    public boolean getUserByNickAndUserPassword(String nick, String pass) {
        Optional<User> checkUser = userCRUD.findByNickAndUserPassword(nick,pass);
        if(checkUser.isPresent()) return true;
        else return false;
    }

    public boolean deleteByNick(String nick) {
        userCRUD.deleteByNick(nick);
        Optional<User> existingTest = userCRUD.findByNick(nick);
        if (existingTest.isPresent()) return false;
        else return true;
    }

    public boolean deleteById(UUID id) {
        userCRUD.deleteByUserId(id);
        Optional<User> existingTest = userCRUD.findByUserId(id);
        if (existingTest.isPresent()) return false;
        else return true;
    }

    public boolean deleteAllUser() {
        userCRUD.deleteAll();
        List<User> users = new ArrayList<>();
        userCRUD.findAll().forEach(user -> users.add(user));
        if(users.isEmpty()) return true;
        else return false;
    }

}
