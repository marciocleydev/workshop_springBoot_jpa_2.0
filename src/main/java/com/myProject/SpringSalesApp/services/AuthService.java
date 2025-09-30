package com.myProject.SpringSalesApp.services;

import com.myProject.SpringSalesApp.DTO.security.AccountCredentialsDTO;
import com.myProject.SpringSalesApp.DTO.security.TokenDTO;
import com.myProject.SpringSalesApp.repositories.UserRepository;
import com.myProject.SpringSalesApp.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

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

        var tokenResponse = tokenProvider.createAccessToken(
                credentials.getUsername(),
                user.getRoles()
        );
        return  ResponseEntity.ok(tokenResponse);
    }

}
