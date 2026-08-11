package com.bibliotecaelo.audit;

import com.bibliotecaelo.domain.Revision;
import com.bibliotecaelo.domain.Usuario;
import com.bibliotecaelo.utils.AuditUserUtil;
import org.hibernate.envers.RevisionListener;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

import static java.util.Objects.nonNull;

public class RevisionListenerImpl implements RevisionListener {

    AuditUserUtil auditUserUtil = new AuditUserUtil();

    @Override
    public void newRevision(Object revisionEntity) {
        Revision revision = (Revision) revisionEntity;

        Usuario user = auditUserUtil.getAuditUser();

        revision.setUserName(user.getNome());
        revision.setLogin(user.getLogin());
        revision.setRemoteIpAddress(getIpFromRequest());
        revision.setUserId(user.getId());
        revision.setRevisionDate(LocalDateTime.now());
    }

    private static String getIpFromRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (nonNull(requestAttributes) && requestAttributes instanceof ServletRequestAttributes) {
            return ((ServletRequestAttributes) requestAttributes).getRequest().getRemoteAddr();
        }
        return "IP não obtido!";
    }

}
