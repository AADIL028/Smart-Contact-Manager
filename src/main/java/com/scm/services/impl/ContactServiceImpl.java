package com.scm.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.helpers.ResourceNotFoundException;
import com.scm.repositories.ContactRepository;
import com.scm.services.ContactService;

import lombok.var;

@Service
public class ContactServiceImpl implements ContactService{

    @Autowired
    private ContactRepository contactRepository;
    
    @Override
    public Contact save(Contact contact) {
        String id = UUID.randomUUID().toString();
        contact.setId(id);
        return contactRepository.save(contact);    
    }

    @Override
    public Contact update(Contact contact) {
        var contactOld = contactRepository.findById(contact.getId()).orElseThrow(()->new ResourceNotFoundException("Contact not found with this id."));
        contactOld.setName(contact.getName());
        contactOld.setEmail(contact.getEmail());
        contactOld.setPhoneNumber(contact.getPhoneNumber());
        contactOld.setAddress(contact.getAddress());
        contactOld.setDescription(contact.getDescription());
        contactOld.setFavourite(contact.isFavourite());
        contactOld.setWebsiteLink(contact.getWebsiteLink());
        contactOld.setLinkedinLink(contact.getLinkedinLink());
        contactOld.setPicture(contact.getPicture());
        contactOld.setCloudinaryPublicId(contact.getCloudinaryPublicId());
        return contactRepository.save(contactOld);
    }

    @Override
    public List<Contact> getAll() {
        return contactRepository.findAll();
    }

    @Override
    public Contact getById(String id) {
        return contactRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Contact not found with id : "+id));    
    }

    @Override
    public void delete(String id) {
        Contact contact = contactRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Contact not found with id : "+id));
        contactRepository.delete(contact);
    }

    @Override
    public List<Contact> getByUserId(String userId) {
        return contactRepository.findByUserId(userId);    
    }

    @Override
    public Page<Contact> getByUser(User user,int page,int size,String sortField,String sortDirection) {

        Sort sort = sortDirection.equals("desc") ? Sort.by(sortField).descending() : Sort.by(sortField).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return contactRepository.findByUser(user,pageable);    
    }

    @Override
    public Page<Contact> searchByName(String name, int page, int size, String sortField, String sortDirection,User user) {
        Sort sort = sortDirection.equals("desc") ? Sort.by(sortField).descending() : Sort.by(sortField).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return contactRepository.findByUserAndNameContaining(user,name, pageable);
    }

    @Override
    public Page<Contact> searchByEmail(String email, int page, int size, String sortField, String sortDirection,User user) {
        Sort sort = sortDirection.equals("desc") ? Sort.by(sortField).descending() : Sort.by(sortField).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return contactRepository.findByUserAndEmailContaining(user,email, pageable);
    }

    @Override
    public Page<Contact> searchByPhone(String phoneNumber, int page, int size, String sortField, String sortDirection,User user) {
        Sort sort = sortDirection.equals("desc") ? Sort.by(sortField).descending() : Sort.by(sortField).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return contactRepository.findByUserAndPhoneNumberContaining(user,phoneNumber, pageable);
    }

    

}
