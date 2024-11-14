package com.bibliotecaelo.auth.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.bibliotecaelo.auth.domain.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    @Value("${mySecretToken}")
    private String secret;

    public String gerarToken(Usuario usuario, int minutes) {
        return JWT.create()
                .withSubject(usuario.getUsername())
                .withClaim("id", String.valueOf(usuario.getId()))
                .withExpiresAt(LocalDateTime.now()
                        .plusMinutes(minutes)
                        .toInstant(ZoneOffset.of("-03:00"))
                ).sign(Algorithm.HMAC256(secret));
    }

    public Object getSubject(String token) {
        return JWT.require(Algorithm.HMAC256(secret))
                .build().verify(token).getSubject();
    }

}
