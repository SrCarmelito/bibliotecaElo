package com.bibliotecaelo.audit;

import java.time.LocalDateTime;
import java.util.UUID;

import com.bibliotecaelo.audit.domain.Revision;
import com.bibliotecaelo.domain.Usuario;
import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static java.util.Objects.nonNull;

public class EnversListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        Revision revision = (Revision) revisionEntity;
        Usuario user = new Usuario();

        String ANONYMOUS_USER = "anonymousUser";

        if(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals(ANONYMOUS_USER)) {
            user.setLogin(ANONYMOUS_USER);
            user.setNome(ANONYMOUS_USER);
            user.setId(UUID.fromString("e25e251b-f4ba-42b9-9861-2bde06584daf"));
        } else {
            user = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }

        revision.setUserName(user.getNome());
        revision.setLogin(user.getLogin());
        revision.setRemoteIpAddress(getIpFromRequest());
        revision.setUserId(user.getId());
        revision.setRevisionDate(LocalDateTime.now());
    }

    private static String getIpFromRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (nonNull(requestAttributes) && requestAttributes instanceof ServletRequestAttributes) {
            return ((ServletRequestAttributes)requestAttributes).getRequest().getRemoteAddr();
        }
        return "Não Obtido!!!";
    }

}
