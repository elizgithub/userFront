package com.userfront.service;

import java.security.PublicKey;
import java.util.List;
import java.util.Set;

import com.userfront.dao.UserDao;
import com.userfront.model.User;
import com.userfront.model.security.UserRole;
import org.springframework.stereotype.Service;

public interface UserService {
	User findByUsername(String username);

    User findByEmail(String email);

    boolean checkUserExists(String username, String email);

    boolean checkUsernameExists(String username);

    boolean checkEmailExists(String email);
    
    void save (User user);
    
    User createUser(User user, Set<UserRole> userRoles);
    
    User saveUser(User user);
}
