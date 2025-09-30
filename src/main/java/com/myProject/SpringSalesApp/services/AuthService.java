package com.myProject.SpringSalesApp.services;

import com.myProject.SpringSalesApp.DTO.security.AccountCredentialsDTO;
import com.myProject.SpringSalesApp.DTO.security.TokenDTO;
import com.myProject.SpringSalesApp.entities.User;
import com.myProject.SpringSalesApp.mapper.AccountMapper;
import com.myProject.SpringSalesApp.repositories.UserRepository;
import com.myProject.SpringSalesApp.security.jwt.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountMapper mapper;

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    public ResponseEntity<TokenDTO> signin(AccountCredentialsDTO credentials){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        credentials.getUsername(),
                        credentials.getPassword()
                )
        );

        var user = userRepository.findByUsername(credentials.getUsername());
        if (user == null){
            throw new UsernameNotFoundException("User: " + credentials.getUsername()+ " not found!");
        }

        var token = tokenProvider.createAccessToken(
                credentials.getUsername(),
                user.getRoles()
        );
        return  ResponseEntity.ok(token);
    }

    public ResponseEntity<TokenDTO> refreshToken(String username, String refreshToken){
        var user = userRepository.findByUsername(username);
        TokenDTO token;
        if (user != null){
            token = tokenProvider.refreshToken(username, refreshToken);
        }else {
            throw new UsernameNotFoundException("User: " + username + " not found!");
        }
        return  ResponseEntity.ok(token);
    }

    public AccountCredentialsDTO createUser(AccountCredentialsDTO user) {
        logger.info("Creating a new User!");
        User newUser = new User();
        newUser.setUserName(user.getUsername());
        newUser.setPassword(generateHashedPassword(user.getPassword()));
        newUser.setFullName(user.getFullName());
        newUser.setEnabled(true);
        newUser.setCredentialsNonExpired(true);
        newUser.setAccountNonExpired(true);
        newUser.setAccountNonLocked(true);

        return mapper.toDTO(userRepository.save(newUser));
    }

    private String generateHashedPassword(String password) {
        PasswordEncoder pbkdf2Encoder = new Pbkdf2PasswordEncoder(
                "", 8, 185000,
                Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("pbkdf2", pbkdf2Encoder);
        DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);

        passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2Encoder);
        return passwordEncoder.encode(password);
    }
}

