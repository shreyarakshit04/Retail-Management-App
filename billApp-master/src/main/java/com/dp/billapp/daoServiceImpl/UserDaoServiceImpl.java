package com.dp.billapp.daoServiceImpl;

import com.dp.billapp.daoService.UserDaoService;
import com.dp.billapp.model.User;
import com.dp.billapp.repository.UserRepository;
import io.vavr.control.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserDaoServiceImpl implements UserDaoService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User register(User user) {
        System.out.println("dao service user");
        System.out.println(user);
        Option<User> userOptional = userRepository.findByContact(user.getContact());

        if(userOptional.isEmpty()){
            return userRepository.save(user);
        }
        else{
            throw new BadCredentialsException("User already exists!");
        }
    }

    @Override
    public User getUser(String contact) {
        Option<User> userOption = userRepository.findByContact(contact);
        if(!userOption.isEmpty()) {
            User user = userOption.get();
            return User.builder().name(user.getName()).id(user.getId())
                    .contact(user.getContact())
                    .email(user.getEmail()).build();
        }
        return null;
    }

    @Override
    public Option<User> findByContact(String contact) {
        return userRepository.findByContact(contact);
    }

    @Override
    public Option<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getAll(String role) {
        return userRepository.findByRoleAndIsActive(role,"1");
    }

    @Override
    public List<User> getUserByRole(String role) {
        return userRepository.findByRoleAndIsActive(role,"1");
    }

    @Override
    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public String deleteById(long id) {
        Optional<User> user = userRepository.findById(id);
        user.get().setIsActive("0");
        userRepository.save(user.get());
        return "User has been deleted";
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

}
