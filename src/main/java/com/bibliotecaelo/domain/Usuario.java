package com.bibliotecaelo.domain;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.bibliotecaelo.audit.AuditInfo;
import com.bibliotecaelo.audit.AuditListener;
import com.bibliotecaelo.audit.Auditable;
import com.bibliotecaelo.enums.SituacaoUsuarioEnum;
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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "usuario", schema = "biblioteca")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@Audited
@EntityListeners(AuditListener.class)
public class Usuario implements UserDetails, Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "É Necessário Informar o Nome")
    @Size(min = 6, max = 150, message = "Nome deve ter entre 6 a 150 caracteres.")
    private String nome;

    @Email
    @NotBlank(message = "É Necessário Informar o E-mail")
    private String email;

    @NotNull(message = "Não é permitido Data de Cadastro Vazia!")
    @PastOrPresent(message = "Não é permitido Data de Cadastro no futuro!")
    @Column(name = "data_cadastro")
    private LocalDate dataCadastro;

    @NotBlank(message = "É Necessário Informar o Telefone!")
    @Size(min = 10, max = 11, message = "Deve ser entre 10 e 11 caracteres com DDD")
    private String telefone;

    @NotBlank(message = "É Necessário Informar o login!")
    @Size(min = 6, max = 150, message = "Login deve ter entre 6 a 150 caracteres.")
    private String login;

    @NotBlank(message = "É Necessário Informar a senha!")
    @Size(min = 6, max = 150, message = "Senha deve ter entre 6 a 150 caracteres.")
    private String senha;

    @Column(name = "reset_token",length = 1000)
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
