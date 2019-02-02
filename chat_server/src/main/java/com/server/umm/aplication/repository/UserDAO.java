package com.server.umm.aplication.repository;


import com.server.umm.aplication.entity.user.User;
import com.server.umm.aplication.exceptions.EntityExistException;

import java.util.List;

public interface UserDAO {

	boolean save(User user) throws EntityExistException;

	boolean updateUser(User user);

	List<User> getUsers();

	User getOneUser(String onlyNick);



}
