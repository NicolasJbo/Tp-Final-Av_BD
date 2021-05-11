package com.utn.tpFinal.util;


import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class EntityURLBuilder {

    /**
     * Method Use whit Integer as PK
     * @param entity
     * @param id
     * @return
     */
    public static String buildUrl(final String entity,final Integer id){

        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/{entity}/{id}")
                .buildAndExpand(entity, id)
                .toUriString();
    }

    /**
     * Method Use whit String as PK
     * @param entity
     * @param string
     * @return
     */
    public static String buildUrl(final String entity,final String string){

        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/{entity}/{string}")
                .buildAndExpand(entity, string)
                .toUriString();
    }
    public static String buildUrl(final String entity){

        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/{entity}")
                .buildAndExpand(entity)
                .toUriString();
    }


}
