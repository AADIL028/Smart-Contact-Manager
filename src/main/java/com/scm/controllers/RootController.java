package com.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.scm.entities.User;
import com.scm.helpers.Helper;
import com.scm.services.UserService;

@ControllerAdvice
public class RootController {

    @Autowired
    private UserService userService;

    @ModelAttribute
    public void addLoggedInUserInformation(Model model,Authentication authentication){
        if(authentication==null){
            return;
        }
        String username = Helper.getEmailOfLoggedinUser(authentication);
        User loggedInUser = userService.getUserByEmail(username);
        model.addAttribute("loggedInUser", loggedInUser);
    }
}
