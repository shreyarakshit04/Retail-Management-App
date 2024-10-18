package com.dp.billapp.service;

import com.dp.billapp.model.User;
import io.vavr.control.Option;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

public interface UserService {

    User saveUser(User user);

    List<User> getAll(String role);

    User getUser(String contactNumber);

    Option<User> findByContact(String contact);

    Option<User> findByEmail(String email);

    boolean isEmailValid(String email);

    Optional<User> findById(long id);

    List<String> getAllContacts(String role);

    String deleteById(long id);

    User updateUser(User user);

   String getContact(HttpServletRequest request);


//    List<ContactResponse> searchUser(String userId,SearchRequest searchRequest);
}
