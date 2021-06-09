package com.utn.tpFinal.service;

import com.utn.tpFinal.model.User;
import com.utn.tpFinal.model.dto.RegisterDto;
import com.utn.tpFinal.model.dto.UserDto;
import com.utn.tpFinal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }




    public User findByMailAndPassword(String mail, String password) {
        return userRepository.findByMailAndPassword(mail,password);
    }

    public UserDto addEmploye(User user) {
      User u= userRepository.save(user);
      UserDto dto = UserDto.from(u);
      return dto;
    }

    public User addClient(RegisterDto registerDto) {
        User u = User.builder().mail(registerDto.getMail())
                .isClient(true)
                .password(registerDto.getPassword())
                .build();
        return userRepository.save(u);
    }
}
