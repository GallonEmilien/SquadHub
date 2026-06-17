package com.squadhub.infrastructure.auth;


import com.squadhub.domain.models.User;
import com.squadhub.domain.usecase.AuthenticateUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthRestController {

    private final AuthenticateUseCase authenticateUseCase;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthRestController(AuthenticateUseCase authenticateUseCase, JwtTokenProvider jwtTokenProvider) {
        this.authenticateUseCase = authenticateUseCase;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/google")
    public ResponseEntity<AuthResponse> loginWithGoogle(@RequestBody AuthRequest request) {
        User user = authenticateUseCase.authenticateWithGoogle(request.idToken());
        String token = jwtTokenProvider.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(token, user.name(), user.avatar()));
    }

    public record AuthRequest(String idToken) {}
    public record AuthResponse(String token, String name, String avatar) {}
}