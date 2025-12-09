package com.bibliotecaelo.domain;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.bibliotecaelo.audit.AuditListener;
import com.bibliotecaelo.audit.Auditable;
import com.bibliotecaelo.audit.domain.AuditInfo;
import com.bibliotecaelo.enums.SituacaoUsuarioEnum;
import com.bibliotecaelo.interfaces.Entidade;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "usuario", schema = "biblioteca")
@Data
@Audited
@EntityListeners(AuditListener.class)
public class Usuario
        implements UserDetails, Auditable, Entidade {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "É necessário informar o nome.")
    @Size(min = 6, max = 150, message = "Nome deve ter entre 6 a 150 caracteres.")
    private String nome;

    @Column(unique = true)
    @Email(message = "Não é um e-mail válido.")
    @NotBlank(message = "É necessário informar o e-mail.")
    private String email;

    @NotNull(message = "Não é permitido data de nascimento vazia.")
    @PastOrPresent(message = "Não é permitido data de nascimento no futuro.")
    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @NotBlank(message = "É necessário informar o telefone.")
    @Size(min = 10, max = 11, message = "Deve ser entre 10 e 11 caracteres com DDD.")
    private String telefone;

    @Column(unique = true)
    @NotBlank(message = "É necessário informar o login.")
    @Size(min = 6, max = 15, message = "Login deve ter entre 6 a 15 caracteres.")
    private String login;

    @NotBlank(message = "É necessário informar a senha.")
    @Size(min = 6, max = 100, message = "Senha deve ter entre 6 a 15 caracteres.")
    private String senha;

    @Column(name = "reset_token", length = 1000)
    private String resetToken;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SituacaoUsuarioEnum situacao;

    @Embedded
    @NotAudited
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
