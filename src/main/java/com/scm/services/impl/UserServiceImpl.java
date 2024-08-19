package com.scm.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.scm.entities.User;
import com.scm.helpers.AppConstants;
import com.scm.helpers.Helper;
import com.scm.helpers.ResourceNotFoundException;
import com.scm.repositories.UserRepository;
import com.scm.services.EmailService;
import com.scm.services.UserService;


@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;

    @Override
    public User saveUser(User user) {
        //generating user id and set it to user.
        String userId = UUID.randomUUID().toString();
        user.setUserId(userId);
        //encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoleList(List.of(AppConstants.ROLE_USER));

        String emailToken = UUID.randomUUID().toString();
        user.setEmailToken(emailToken);
        User savedUser = userRepository.save(user); 
        String varifyLink = Helper.getLinkForEmailVarification(emailToken);
        emailService.sendEmail(savedUser.getEmail(), "Verify Account : Smart Contact Manager", varifyLink);

        return savedUser;
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> updateUser(User userNew) {
        User userOld = userRepository.findById(userNew.getUserId()).orElseThrow(()-> new ResourceNotFoundException("User not found!"));
        //update user
        userOld.setName(userNew.getName());
        userOld.setEmail(userNew.getEmail());
        userOld.setPassword(userNew.getPassword());
        userOld.setAbout(userNew.getAbout());
        userOld.setPhoneNumber(userNew.getPhoneNumber());
        userOld.setProfilePic(userNew.getProfilePic());
        userOld.setEnabled(userNew.isEnabled());
        userOld.setEmailVarified(userNew.isEmailVarified());
        userOld.setMobileVarified(userNew.isMobileVarified());
        userOld.setProvider(userNew.getProvider());
        userOld.setProviderUserId(userNew.getProviderUserId());

        //save the user in db.
        User save = userRepository.save(userOld);

        return Optional.ofNullable(save);
    }

    @Override
    public void deleteUser(String id) {
        User user = userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("User not found!"));
        userRepository.delete(user);
    }

    @Override
    public boolean isUserExist(String userId) {
        User user = userRepository.findById(userId).orElse(null);
        return user!=null ? true : false;
    }

    @Override
    public boolean isUserExistByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        return user!=null ? true : false;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);    
    }

    @Override
    public User getUserByEmailToken(String emailToken) {
        return userRepository.findByEmailToken(emailToken).orElse(null);
    }

}
