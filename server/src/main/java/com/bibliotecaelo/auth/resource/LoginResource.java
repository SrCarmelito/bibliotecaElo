package com.bibliotecaelo.auth.resource;

import com.bibliotecaelo.auth.service.LoginService;
import com.bibliotecaelo.auth.dto.LoginDTO;
import com.bibliotecaelo.auth.dto.NewPasswordDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class LoginResource {

    private final LoginService loginService;

    @PostMapping("/login")
    public String login(@RequestBody LoginDTO login) {
        return loginService.gerarToken(login);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(HttpServletRequest request,
            @RequestBody String email,
            HttpServletResponse response) throws Exception {
        loginService.resetPassword(request, email);
        return ResponseEntity.ok(email);
    }

    @PostMapping("/confirm-reset-password")
    public ResponseEntity<Void> confirmResetPassword(@RequestBody NewPasswordDTO newPasswordDTO) {
        loginService.confirmResetPassword(newPasswordDTO);
        return ResponseEntity.noContent().build();
    }
}
