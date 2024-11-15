package com.bibliotecaelo.auth.converter;

import com.bibliotecaelo.converter.LivroDTOConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfigurationConverter {

    @Bean
    public UsuarioDTOConverter usuarioDTOConverter() {
        return new UsuarioDTOConverter();
    }

    @Bean
    public UsuarioResponseDTOConverter usuarioResponseDTOConverter() {
        return new UsuarioResponseDTOConverter();
    }

    @Bean
    public LivroDTOConverter livroDTOConverter() {
        return new LivroDTOConverter();
    }
}
