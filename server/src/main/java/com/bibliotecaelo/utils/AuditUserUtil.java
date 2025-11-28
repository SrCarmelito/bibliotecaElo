package com.bibliotecaelo.utils;

import java.util.Objects;
import java.util.UUID;

import com.bibliotecaelo.domain.Usuario;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuditUserUtil {

    public Usuario getAuditUser() {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(Objects.nonNull(authentication)) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof Usuario usuario) {
                return usuario;
            }
        }

        final Usuario anonymousUser = new Usuario();
        anonymousUser.setId(UUID.fromString("e25e251b-f4ba-42b9-9861-2bde06584daf"));
        anonymousUser.setNome("anonymousUser");
        anonymousUser.setLogin("anonymousUser");

        return anonymousUser;
    }
}
