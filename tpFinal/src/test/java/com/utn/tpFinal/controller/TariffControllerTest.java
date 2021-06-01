package com.utn.tpFinal.controller;

import com.utn.tpFinal.AbstractController;
import com.utn.tpFinal.model.Tariff;
import com.utn.tpFinal.repository.TariffRepository;
import com.utn.tpFinal.service.TariffService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = TariffController.class)  //especificamos sobre que clase va a trabajar asi se ejecuta cuando corro los test
public class TariffControllerTest extends AbstractController {

    @MockBean
    TariffService tariffService;

    @MockBean
    TariffRepository tariffRepository;
    @MockBean
    private FormattingConversionService formattingConversionService;

    @Mock
    Root<Tariff> root;
    @Mock
    CriteriaQuery <?> query;
    @Mock
    CriteriaBuilder builder;
    @Mock
    Predicate predicate;
    @Mock
    Specification<Tariff> tariffSpecification;


    //Result Action -> obj que permite el resulta de la interaccion entre el contexto
    // generado y la accion que estamos haciendo

    @Test
    public void getAll() throws Exception {  //given controller -> levanta el contexto del metodo

        Mockito.when(tariffSpecification.toPredicate(root,query,builder)).thenReturn(predicate);

        final ResultActions resultActions = givenController().perform(MockMvcRequestBuilders //inicializar mock
                .get("/tariff")
                .param("name")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[?(@.name=='d1')]").exists())
                .andExpect(jsonPath("$[2]").doesNotExist());

         // assertEquals(tariffSpecification,is(notNullValue()));
        //assertEquals(tariffSpecification.toPredicate(root,query,builder), is(nullValue()));

        assertEquals(HttpStatus.OK.value(), resultActions.andReturn().getResponse().getStatus());
    }
}
