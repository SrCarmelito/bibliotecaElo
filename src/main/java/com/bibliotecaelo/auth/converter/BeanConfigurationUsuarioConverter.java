package com.bibliotecaelo.auth.converter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfigurationUsuarioConverter {

    @Bean
    public UsuarioDTOConverter usuarioDTOConverter() {
        return new UsuarioDTOConverter();
    }

    @Bean
    public UsuarioResponseDTOConverter usuarioResponseDTOConverter() {
        return new UsuarioResponseDTOConverter();
    }
}
