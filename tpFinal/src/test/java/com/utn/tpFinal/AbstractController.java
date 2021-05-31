package com.utn.tpFinal;

import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@AutoConfigureWebMvc //spring se hace cargo de la configuración
@AutoConfigureMockMvc
@ContextConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS) //genera una instacia nueva por clase / por metodo
public abstract class AbstractController {

    @Autowired
    private WebApplicationContext webApplicationContext; //levantar el contexto de la aplicacion

    public MockMvc mockMvc;

    protected MockMvc givenController (){  //levanta solo una parte del sistem para hacer los test
        return MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }
}