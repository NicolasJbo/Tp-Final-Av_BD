package com.utn.tpFinal.service;

import com.utn.tpFinal.model.User;
import com.utn.tpFinal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;


    public User findByMailAndPassword(String mail, String password) {
        return userRepository.findByMailAndPassword(mail,password);
    }
}
