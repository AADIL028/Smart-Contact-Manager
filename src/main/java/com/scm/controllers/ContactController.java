package com.scm.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.forms.ContactForm;
import com.scm.forms.ContactSearchForm;
import com.scm.helpers.AppConstants;
import com.scm.helpers.Helper;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.services.ContactService;
import com.scm.services.ImageService;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/user/contacts")
public class ContactController {

    @Autowired
    private ContactService contactService;
    @Autowired
    private UserService userService;
    @Autowired
    private ImageService imageService;

    @GetMapping("/add")
    public String addContactView(Model model) {
        ContactForm contactForm = new ContactForm();
        model.addAttribute("contactForm", contactForm);
        model.addAttribute("isActive", "addContact");
        return "/user/add_contact";
    }

    @PostMapping("/add")
    public String saveContact(@Valid @ModelAttribute ContactForm contactForm, BindingResult bindingResult,
            Authentication authentication, HttpSession session) {

        // check for validation errors
        if (bindingResult.hasErrors()) {
            session.setAttribute("message",
                    Message.builder().content("Please correct following errors.").messageType(MessageType.red).build());
            return "user/add_contact";
        }

        String username = Helper.getEmailOfLoggedinUser(authentication);
        User user = userService.getUserByEmail(username);

        Contact contact = new Contact();
        contact.setName(contactForm.getName());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setEmail(contactForm.getEmail());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setLinkedinLink(contactForm.getLinkedinLink());
        contact.setWebsiteLink(contactForm.getWebsiteLink());
        contact.setFavourite(contactForm.isFavourite());
        contact.setUser(user);

        // process image
        if(contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()){
            String filename = UUID.randomUUID().toString();
            String imgUrl = imageService.uploadImage(contactForm.getContactImage(), filename);
            contact.setPicture(imgUrl);
            contact.setCloudinaryPublicId(filename);
        }
        // save into db.
        contactService.save(contact);

        // success message.
        session.setAttribute("message",
                Message.builder().content("Contact saved successfully!").messageType(MessageType.green).build());
        return "redirect:/user/contacts/add";
    }

    @GetMapping
    public String viewContacts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE + "") int size,
            @RequestParam(value = "sortBy", defaultValue = "favourite") String sortBy,
            @RequestParam(value = "direction", defaultValue = "desc") String direction,
            Model model, Authentication authentication) {
        String username = Helper.getEmailOfLoggedinUser(authentication);
        User user = userService.getUserByEmail(username);
        Page<Contact> pageContacts = contactService.getByUser(user, page, size, sortBy, direction);
        model.addAttribute("pageContacts", pageContacts);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
        ContactSearchForm contactSearchForm = new ContactSearchForm();
        model.addAttribute("contactSearchForm", contactSearchForm);
        model.addAttribute("isActive", "contacts");
        return "user/view_contact";
    }

    @GetMapping("/search")
    public String searchContact(
            @ModelAttribute ContactSearchForm contactSearchForm,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE + "") int size,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            Model model, Authentication authentication) {

        String username = Helper.getEmailOfLoggedinUser(authentication);
        User user = userService.getUserByEmail(username);

        Page<Contact> pageContacts = null;
        if (contactSearchForm.getField().equals("name")) {
            pageContacts = contactService.searchByName(contactSearchForm.getValue(), page, size, sortBy, direction,
                    user);
        } else if (contactSearchForm.getField().equals("email")) {
            pageContacts = contactService.searchByEmail(contactSearchForm.getValue(), page, size, sortBy, direction,
                    user);
        } else if (contactSearchForm.getField().equals("phoneNumber")) {
            pageContacts = contactService.searchByPhone(contactSearchForm.getValue(), page, size, sortBy, direction,
                    user);
        }
        model.addAttribute("pageContacts", pageContacts);
        model.addAttribute("contactSearchForm", contactSearchForm);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
        return "user/search";
    }

    @GetMapping("/delete/{id}")
    public String deleteContact(@PathVariable String id, HttpSession session) {
        contactService.delete(id);
        session.setAttribute("message",
                Message.builder().content("Contact deleted successfully!!").messageType(MessageType.green).build());
        return "redirect:/user/contacts";
    }

    @GetMapping("/view/{id}")
    public String updateContactView(@PathVariable String id, Model model) {
        Contact contact = contactService.getById(id);
        ContactForm contactForm = new ContactForm();
        contactForm.setName(contact.getName());
        contactForm.setEmail(contact.getEmail());
        contactForm.setPhoneNumber(contact.getPhoneNumber());
        contactForm.setAddress(contact.getAddress());
        contactForm.setDescription(contact.getDescription());
        contactForm.setFavourite(contact.isFavourite());
        contactForm.setWebsiteLink(contact.getWebsiteLink());
        contactForm.setLinkedinLink(contact.getLinkedinLink());
        contactForm.setPicture(contact.getPicture());
        model.addAttribute("contactForm", contactForm);
        model.addAttribute("contactId", contact.getId());
        return "user/contact_update_view";
    }

    @PostMapping("/update/{id}")
    public String updateContact(@PathVariable String id,@Valid @ModelAttribute ContactForm contactForm,Model model,BindingResult bindingResult){
        
        if(bindingResult.hasErrors()){
            return "user/contact_update_view";
        }

        Contact contact = contactService.getById(id);
        contact.setId(id);
        contact.setName(contactForm.getName());
        contact.setEmail(contactForm.getEmail());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setWebsiteLink(contactForm.getWebsiteLink());
        contact.setLinkedinLink(contactForm.getLinkedinLink());
        contact.setFavourite(contactForm.isFavourite());
        //process image
        if(contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty())
        {
            String imgPublicId = UUID.randomUUID().toString();
            String imgURI = imageService.uploadImage(contactForm.getContactImage(), imgPublicId);
            contact.setCloudinaryPublicId(imgPublicId);
            contact.setPicture(imgURI);
            contactForm.setPicture(imgURI);
        }

        contactService.update(contact);
        
        model.addAttribute("message", Message.builder().content("Contact Updated Successfully!").messageType(MessageType.green).build());
        return "redirect:/user/contacts/view/"+id;
    }
}
