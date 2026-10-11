package ua.garage.lelviv.authorization.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.garage.lelviv.authorization.config.JwtServise;
import ua.garage.lelviv.authorization.entities.Role;
import ua.garage.lelviv.authorization.entities.User;
import ua.garage.lelviv.authorization.entities.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtServise jwtServise;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponce register(RegisterRequest request) {
        var user = User.builder()
                .name(request.getFirstname())
                .surname(request.getLastname())
                .phoneNumber(request.getPhoneNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .dateCreated(LocalDateTime.now())
                .build();
        repository.save(user);
        var jwtToken = jwtServise.generateToken(user);
        return AuthenticationResponce.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponce authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getPhoneNumber(),
                        request.getPassword()
                )
        );
        var user = repository.findByPhoneNumber(request.getPhoneNumber())
                .orElseThrow();

        var jwtToken = jwtServise.generateToken(user);
        return AuthenticationResponce.builder()
                .token(jwtToken)
                .build();
    }
}
