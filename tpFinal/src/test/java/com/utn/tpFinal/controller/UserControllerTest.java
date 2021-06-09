package com.utn.tpFinal.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.utn.tpFinal.UTILS_TESTCONSTANTS;
import com.utn.tpFinal.model.User;
import com.utn.tpFinal.model.dto.LoginRequestDto;
import com.utn.tpFinal.model.dto.LoginResponseDto;
import com.utn.tpFinal.model.dto.UserDto;
import com.utn.tpFinal.service.EnergyMeterService;
import com.utn.tpFinal.service.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    UserController userController;
    UserService userService;
    ObjectMapper objectMapper;
    ModelMapper modelMapper;

    @Before
    public void setUp(){
        userService = mock(UserService.class);
        objectMapper = mock(ObjectMapper.class);
        modelMapper = mock(ModelMapper.class);

        userController= new UserController(userService,objectMapper,modelMapper);
    }
    @Test
    public void addemployee_Test200(){
        try {
            User user = UTILS_TESTCONSTANTS.getUser();
            UserDto userdto = UTILS_TESTCONSTANTS.getUserDto(1);
            when(userService.addEmploye(user)).thenReturn(userdto);

            ResponseEntity<UserDto> response =userController.addemployee(user);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("carlos@gmail.com", response.getBody().getMail());


        } catch (ParseException e) {
            Assert.fail("Fechas mal Ingresada");
        }
    }
    @Test
    public void login_Test200(){
        try {
            LoginRequestDto loginRequestDto = UTILS_TESTCONSTANTS.getLoginRequestDTO();
            User user = UTILS_TESTCONSTANTS.getUserCLient();
            UserDto userDto = UTILS_TESTCONSTANTS.getUserDto(1);

            when(modelMapper.map(user,UserDto.class)).thenReturn(userDto);
            when(userService.findByMailAndPassword(loginRequestDto.getMail(),loginRequestDto.getPassword())).thenReturn(user);
            when(userController.generateToken(userDto)).thenReturn("123abc");
            ResponseEntity<LoginResponseDto> responseEntity = userController.login(loginRequestDto);

            assertEquals(HttpStatus.OK,responseEntity.getStatusCode());

        } catch (ParseException | JsonProcessingException e) {
            e.printStackTrace();
        }

    }
    @Test
    public void login_Test401() throws JsonProcessingException {
        try {
            LoginRequestDto loginRequestDto = UTILS_TESTCONSTANTS.getLoginRequestDTO();
            User user = null;
            UserDto userDto = UTILS_TESTCONSTANTS.getUserDto(1);
            when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);
            when(userService.findByMailAndPassword(loginRequestDto.getMail(), loginRequestDto.getPassword())).thenReturn(null);
            ResponseEntity<LoginResponseDto> responseEntity = userController.login(loginRequestDto);
        }catch (ResponseStatusException e){
            assertEquals(HttpStatus.UNAUTHORIZED,e.getStatus());
        }



    }

}
