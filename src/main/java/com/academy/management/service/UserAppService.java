package com.academy.management.service;

import com.academy.management.dao.UserAppDao;
import com.academy.management.model.UserApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAppService {

    @Autowired
    private UserAppDao userAppDao;

    public List<UserApp> getAllUsers() {
        return userAppDao.getAllUsers();
    }

    public UserApp getUserById(Long id) {
        return userAppDao.getUserById(id);
    }

    public UserApp createUser(UserApp user) {
        if (user.getRole() == null) user.setRole("STUDENT");
        return userAppDao.save(user);
    }

    public void updateUser(UserApp user) {
        userAppDao.update(user);
    }

    public void updateUserPartial(UserApp user) {
        userAppDao.updatePartial(user);
    }

    public boolean deleteUser(Long id) {
        return userAppDao.delete(id);
    }
}
