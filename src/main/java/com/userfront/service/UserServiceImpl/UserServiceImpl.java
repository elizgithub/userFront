package com.userfront.service.UserServiceImpl;

import java.util.List;
import java.util.Set;

import com.userfront.dao.RoleDao;
import com.userfront.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.userfront.dao.UserDao;
import com.userfront.model.User;
import com.userfront.model.security.UserRole;
import com.userfront.service.UserService;

@Transactional //uses hibernate and spring it creates unit of works like multiple DB
//transactions and all trnsactions have to be successfull like all-or-noting
//if one db transaction fails it will roleback all db operations noting will be persisited
//Good for data integrity or consistancy
@Service
public class UserServiceImpl implements UserService{
	
	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
    private RoleDao roleDao;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AccountService accountService;

    public User createUser (User user, Set <UserRole> userRoles)
    {
        User localUser = userDao.findByUsername(user.getUsername());

        if(localUser!=null)
        {
            LOG.info("User with username {} already exists ",user.getUsername());
        }

        else {
            String encryptedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encryptedPassword);//set encrypted password


            for (UserRole ur : userRoles) {
                    roleDao.save(ur.getRole());
            }
            user.getUserRoles().addAll(userRoles);
            user.setPrimaryAccount(accountService.createPrimaryAccount());
            user.setSavingsAccount(accountService.createSavingsAccount());

            localUser= userDao.save(user);
        }
        return localUser;
    }

	public void save(User user) {
        userDao.save(user);
    }

    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }
    
    
//    public User createUser(User user, Set<UserRole> userRoles) {
//        User localUser = userDao.findByUsername(user.getUsername());
//
//        if (localUser != null) {
//            LOG.info("User with username {} already exist. Nothing will be done. ", user.getUsername());
//        } else {
//            String encryptedPassword = passwordEncoder.encode(user.getPassword());
//            user.setPassword(encryptedPassword);
//
//            for (UserRole ur : userRoles) {
//                roleDao.save(ur.getRole());
//            }
//
//            user.getUserRoles().addAll(userRoles);
//
//            user.setPrimaryAccount(accountService.createPrimaryAccount());
//            user.setSavingsAccount(accountService.createSavingsAccount());
//
//            localUser = userDao.save(user);
//        }
//
//        return localUser;
//    }
    
    public boolean checkUserExists(String username, String email){
        if (checkUsernameExists(username) || checkEmailExists(username)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkUsernameExists(String username) {
        if (null != findByUsername(username)) {
            return true;
        }

        return false;
    }
    
    public boolean checkEmailExists(String email) {
        if (null != findByEmail(email)) {
            return true;
        }

        return false;
    }

    public User saveUser (User user) {
        return userDao.save(user);
    }
    
    public List<User> findUserList() {
        return userDao.findAll();
    }

//    public void enableUser (String username) {
//        User user = findByUsername(username);
//        user.setEnabled(true);
//        userDao.save(user);
//    }

//    public void disableUser (String username) {
//        User user = findByUsername(username);
//        user.setEnabled(false);
//        System.out.println(user.isEnabled());
//        userDao.save(user);
//        System.out.println(username + " is disabled.");
//    }
}
