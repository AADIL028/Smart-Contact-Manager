package com.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.entities.User;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired 
    private UserService userService;

    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam("token") String token,HttpSession session){
        User user = userService.getUserByEmailToken(token);
        if(user!=null){
            if(user.getEmailToken().equals(token)){
                user.setEmailVarified(true);
                user.setEnabled(true);
                userService.updateUser(user);
                Message message = Message.builder().content("Your Account is varified! You can Login now!").messageType(MessageType.green).build();
                session.setAttribute("message", message);
                return "auth_success_page";
            }
            Message message = Message.builder().content("Something went wrong!This token is not associated with your account!").messageType(MessageType.red).build();
            session.setAttribute("message", message);
            return "auth_error_page";
        }
        Message message = Message.builder().content("Something went wrong!This token is not associated with your account!").messageType(MessageType.red).build();
        session.setAttribute("message", message);
        return "auth_error_page";
    }
}
