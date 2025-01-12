package com.krzywdek19.flashcards.auth;

import com.krzywdek19.flashcards.user.Role;
import com.krzywdek19.flashcards.user.User;
import com.krzywdek19.flashcards.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public User signup(UserRegisterDto dto){
        User user = User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(Role.USER)
                .build();

        return userRepository.save(user);
    }

    public User authenticate(LoginUserDto dto) {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
        return userRepository
                .findByUsername(dto.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(dto.getUsername()));
    }
}
