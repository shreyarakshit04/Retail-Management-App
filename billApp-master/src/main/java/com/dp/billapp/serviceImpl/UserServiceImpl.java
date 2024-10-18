package com.dp.billapp.serviceImpl;

import com.dp.billapp.daoService.UserDaoService;
import com.dp.billapp.helper.JwtUtil;
import com.dp.billapp.model.User;
import com.dp.billapp.model.UserConstants;
import com.dp.billapp.repository.UserRepository;
import com.dp.billapp.service.UserService;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}";
    Pattern pattern = Pattern.compile(regex);


    private final UserRepository userRepository;
    private  final JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    UserDaoService userDaoService;

    @Override
    public User saveUser(User user) {
        System.out.println("save user");

        if(user.getRole().equals(UserConstants.CustomerRole)){
            user.setPassword("");
        }
        else{
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userDaoService.register(user);
    }

    @Override
    public List<User> getAll(String role) {
        return userDaoService.getAll(role);
    }

    @Override
    public User getUser(String contactNumber) {
        return null;
    }

    @Override
    public Option<User> findByContact(String contact) {
        return userDaoService.findByContact(contact);
    }

    @Override
    public Option<User> findByEmail(String email) {
        return userDaoService.findByEmail(email);
    }


    @Override
    public boolean isEmailValid(String email) {
        Matcher matcher = pattern.matcher(email);
        System.out.println("user email valid: "+matcher.matches());
        return matcher.matches();
    }

    @Override
    public Optional<User> findById(long id) {
        return userDaoService.findById(id);
    }

    @Override
    public List<String> getAllContacts(String role) {
        ArrayList<String> contactList = new ArrayList<>();
        List<User> userList = new ArrayList<>();
        if(role.equals("customer")){
            userList = userDaoService.getUserByRole(UserConstants.CustomerRole);
        }else {
            userList = userDaoService.getUserByRole(UserConstants.EditorRole);
        }
        for(User u:userList){
            contactList.add(u.getContact());
        }

        return contactList;
    }

    @Override
    public String deleteById(long id) {
        userDaoService.deleteById(id);
        return "User has been deleted";
    }

    @Override
    public User updateUser(User user) {

        if(user.getRole().equals(UserConstants.CustomerRole)){
            user.setPassword("");
        }
        else{
            Optional<User> userOptional=userRepository.findById(user.getId());
            if(user.getPassword().equals(userOptional.get().getPassword())){
                user.setPassword(user.getPassword());
            }
            else{
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }

        }
        return userDaoService.updateUser(user);
    }

    @Override
    public String getContact(HttpServletRequest request) {
        String contact="";
        String requestHeader = request.getHeader("Authorization");
        if(requestHeader!=null && requestHeader.startsWith("Bearer ")) {
            String jwtToken = requestHeader.substring(7);
            if(!jwtUtil.isTokenExpired(jwtToken)){
                Map<String, String> map = jwtUtil.getJwtTokenDetails(request);
                 contact= map.get(UserConstants.contactNo);
            }
        }
        return contact;
    }
}
