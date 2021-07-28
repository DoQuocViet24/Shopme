package com.shopme.admin.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Service
public class UserService {
    @Autowired
    private UserRepository repo;
    @Autowired
    private RoleRepository repoRole;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public List<User> listAll(){
    	return (List<User>)repo.findAll();
    }
    
    public List<Role> listRoles(){
    	return (List<Role>)repoRole.findAll();
    }
    
    public void save(User user) {
    	encodePassword(user);
    	repo.save(user);
    }
    
    private void encodePassword(User user) {
    	String encodedPassword = passwordEncoder.encode(user.getPassword());
    	user.setPassword(encodedPassword);
    }
    
    public boolean isEmailUnique(String email) {
    	User userByEmail = repo.getUserByEmail(email);
    	return userByEmail==null;
    }
}
