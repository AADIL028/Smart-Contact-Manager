package com.scm.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.scm.entities.Contact;
import com.scm.entities.User;

public interface ContactService {
    Contact save(Contact contact);
    Contact update(Contact contact);
    List<Contact> getAll();
    Contact getById(String id);
    void delete(String id);
    Page<Contact> searchByName(String name,int page,int size,String sortField,String sortDirection,User user);
    Page<Contact> searchByEmail(String email,int page,int size,String sortField,String sortDirection,User user);
    Page<Contact> searchByPhone(String phoneNumber,int page,int size,String sortField,String sortDirection,User user);
    List<Contact> getByUserId(String userId);
    Page<Contact> getByUser(User user,int page,int size,String sortField,String sortDirection);
}
