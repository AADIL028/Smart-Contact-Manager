package com.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.scm.entities.User;
import com.scm.forms.UserForm;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class PageController {

    @Autowired
    private UserService userService;

    @GetMapping(path = "/")
    public String index() {
        return "redirect:/home";
    }
    
    @GetMapping(path = "/home")
    public ModelAndView home(Model model) {
        model.addAttribute("isActive", "home");
        return new ModelAndView("home");
    }

    @GetMapping("/about")
    public ModelAndView aboutPage(Model model) {
        model.addAttribute("isActive", "about");
        return new ModelAndView("about");
    }

    @GetMapping("/services")
    public ModelAndView servicesPage(Model model) {
        model.addAttribute("isActive", "services");
        return new ModelAndView("services");
    }

    @GetMapping("/contact")
    public ModelAndView contactPage(Model model) {
        model.addAttribute("isActive", "contact");
        return new ModelAndView("contact");
    }

    @GetMapping("/login")
    public ModelAndView loginPage() {
        return new ModelAndView("login");
    }

    @GetMapping("/register")
    public ModelAndView registerPage(Model model) {
        UserForm userForm = new UserForm();
        model.addAttribute("userForm", userForm);
        return new ModelAndView("register");
    }

    // handling and processing signup form
    @PostMapping("/do-register")
    public String processingRegister(@Valid @ModelAttribute UserForm userForm, BindingResult bindingResult, HttpSession session) {
        
        // validate user
        if(bindingResult.hasErrors()){
            return "register";
        }

        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setAbout(userForm.getAbout());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setEnabled(false);
        user.setProfilePic("https://upload.wikimedia.org/wikipedia/commons/a/ac/Default_pfp.jpg");
        
        userService.saveUser(user);

        Message message = Message.builder()
        .content("Registration successfull!")
        .messageType(MessageType.green)
        .build();
        session.setAttribute("message", message);
        
        return "redirect:/register";
    }
}
