package com.dp.billapp.service;


import com.dp.billapp.model.User;
import com.dp.billapp.repository.UserRepository;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@RequiredArgsConstructor
@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;



    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        log.info("#  Finding user with mobile - {}", userName);

        Option<User> user =userRepository.findByContact(userName);

        if(!user.isEmpty()){
            log.info("user - {}", user);
           return new org.springframework.security.core.userdetails.User(user.get().getContact(), user.get().getPassword(),new ArrayList<>());
        }
        else{
            throw  new UsernameNotFoundException("User not found!");
        }

    }

}
