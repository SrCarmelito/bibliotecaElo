package com.bibliotecaelo.auth.domain;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.bibliotecaelo.audit.AuditInfo;
import com.bibliotecaelo.audit.AuditListener;
import com.bibliotecaelo.audit.Auditable;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(schema = "biblioteca", name = "usuario")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Audited
@EntityListeners(AuditListener.class)
public class Usuario implements UserDetails, Auditable {

    @Id
    private UUID id;

    @NotBlank(message = "É Necessário Informar o Nome")
    @Size(min = 6, max = 150, message = "Nome deve ter entre 6 a 150 caracteres.")
    private String nome;

    @Email
    @NotBlank(message = "É Necessário Informar o E-mail")
    private String email;

    @NotBlank
    @Size(min = 6, max = 150, message = "Login deve ter entre 6 a 150 caracteres.")
    private String login;

    @Size(min = 6, max = 150, message = "Senha deve ter entre 6 a 150 caracteres.")
    private String senha;

    @Column(name = "reset_token")
    private String resetToken;

    @Embedded
    @Audited
    private AuditInfo audit = AuditInfo.now();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
