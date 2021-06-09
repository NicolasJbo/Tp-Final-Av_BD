package com.utn.tpFinal.service;

import com.utn.tpFinal.UTILS_TESTCONSTANTS;
import com.utn.tpFinal.controller.ClientController;
import com.utn.tpFinal.model.User;
import com.utn.tpFinal.model.dto.RegisterDto;
import com.utn.tpFinal.model.dto.UserDto;
import com.utn.tpFinal.repository.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceTest {
    UserRepository userRepository;
    UserService userService;

    @Before
    public void setUp() {

        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    public void findByMailAndPassword_TestOK() {
        User u = new User();

        try {
            when(userRepository.findByMailAndPassword("mail@gmail.com", "123")).thenReturn(UTILS_TESTCONSTANTS.getUser());
            u = userService.findByMailAndPassword("mail@gmail.com", "123");
            assertEquals("mail1@gmail.com", u.getMail());
        } catch (ParseException e) {
            Assert.fail("Fallo las fechas");
        }
    }

    @Test
    public void addEmploye_TestOK() {
        try {
            User u = UTILS_TESTCONSTANTS.getUser();
            when(userRepository.save(u)).thenReturn(u);
            UserDto userDto = userService.addEmploye(u);
            assertEquals("mail1@gmail.com", userDto.getMail());
        } catch (ParseException e) {
            Assert.fail("Fallo las fechas");
        }
    }
   //todo revisar
    @Test
    public void addClient_TestOK() {
        try {
            RegisterDto registerDto = UTILS_TESTCONSTANTS.getRegisterDTO();

            User u = UTILS_TESTCONSTANTS.getUser();
            when(userRepository.save(u)).thenReturn(u);
            User user = userService.addClient(registerDto);
            assertEquals("mail1@gmail.com", user.getMail());

        } catch (ParseException e) {
            Assert.fail("Fallo las fechas");
        }


    }
}
