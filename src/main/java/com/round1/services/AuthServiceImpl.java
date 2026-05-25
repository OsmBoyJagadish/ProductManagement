package com.round1.services;

import com.round1.dto.request.LoginRequest;
import com.round1.dto.request.RegisterRequest;
import com.round1.dto.response.AuthResponse;
import com.round1.entities.Cart;
import com.round1.entities.User;
import com.round1.enums.Role;
import com.round1.exception.DuplicateEmailException;
import com.round1.repositories.CartRepository;
import com.round1.repositories.UserRepository;
import com.round1.security.JwtService;
import com.round1.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        return createUser(request, Role.USER);
    }

    @Override
    @Transactional
    public AuthResponse registerAdmin(RegisterRequest request) {
        return createUser(request, Role.ADMIN);
    }

    private AuthResponse createUser(RegisterRequest request, Role role) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException(request.getEmail());
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();

        user = userRepository.save(user);

        if (role == Role.USER) {
            Cart cart = Cart.builder().user(user).build();
            cartRepository.save(cart);
        }

        String token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().name())
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        String token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().name())
                .build();
    }
}
