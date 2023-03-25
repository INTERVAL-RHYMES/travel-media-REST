package com.example.travelmediarest.service;

import com.example.travelmediarest.config.JwtUtil;
import com.example.travelmediarest.dto.RegistrationDto;
import com.example.travelmediarest.dto.UserDto;
import com.example.travelmediarest.model.User;
import com.example.travelmediarest.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;


@Service
@Slf4j
public class UserService {
    @Autowired
    @Qualifier("userRepository")
    private UserRepository userRepository;
    @Autowired
    @Qualifier("jwtUtil")
    private JwtUtil jwtUtil;

    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;

    public UserService(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    public User isAlreadyOrSaveUSer(RegistrationDto registrationDto) {
        Optional<User> userOptional = userRepository.findByMail(registrationDto.getMail());

        if (userOptional.isPresent()) return null;

        User user = registrationDto.toUser(passwordEncoder);
        userRepository.save(user);
        return user;
    }

    public Map<String, Object> authenticateByCredentials(UserDto userDto) {
        try {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDto.getMail(), userDto.getPassword());
            authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            String token = jwtUtil.generateToken(userDto.getMail());
            return Collections.singletonMap("Jwt-token", token);
        } catch (AuthenticationException authenticationException) {
//            authenticationException.printStackTrace();
            throw new RuntimeException("invalid login Credentials");
        }
    }

    public User fetchUserByMail(String mail) {
        Optional<User> userOptional = userRepository.findByMail(mail);
        User user = null;
        if (userOptional.isPresent()) {
            user = userOptional.get();
        }
        return user;
    }
}
