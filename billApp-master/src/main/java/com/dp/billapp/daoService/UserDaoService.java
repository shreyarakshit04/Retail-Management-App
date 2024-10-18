package com.dp.billapp.daoService;

import com.dp.billapp.model.User;
import io.vavr.control.Option;

import java.util.List;
import java.util.Optional;

public interface UserDaoService {

    User register(User user);

    User getUser(String contact);

    Option<User> findByContact(String phone);

    Option<User> findByEmail(String email);

    List<User> getAll(String role);

    List<User> getUserByRole(String role);

   Optional<User>  findById(long id);
   List<User> getUsers();
   String deleteById(long id);
  User updateUser(User user);


}
