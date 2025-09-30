package com.myProject.SpringSalesApp.controllers;

import com.myProject.SpringSalesApp.DTO.security.AccountCredentialsDTO;
import com.myProject.SpringSalesApp.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication", description = "Authentication API")
@RestController
@RequestMapping(value = "/auth")
public class AuthController implements com.myProject.SpringSalesApp.controllers.docs.AuthControllerDocs {

    @Autowired
    private AuthService service;

    @PostMapping("/signin")
    @Override
    public ResponseEntity<?> signin(@RequestBody AccountCredentialsDTO credentials) {
        if (credentialsIsInvalid(credentials)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
        }
        var token = service.signin(credentials);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
        }
        return ResponseEntity.ok().body(token);
    }

    @PutMapping("/refresh/{username}")
    @Override
    public ResponseEntity<?> refreshToken(
            @PathVariable("username") String username,
            @RequestHeader("Authorization") String refreshToken) {

        if (parametersAreInvalid(username, refreshToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
        }
        var token = service.refreshToken(username, refreshToken);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
        }
        return ResponseEntity.ok().body(token);
    }

    @PostMapping("/createUser")
    @Override
    public ResponseEntity<AccountCredentialsDTO> create(@RequestBody AccountCredentialsDTO credentials) {
        var createdUser = service.createUser(credentials);
        return ResponseEntity.ok().body(createdUser);
    }

    private boolean parametersAreInvalid(String username, String refreshToken) {
        return StringUtils.isEmpty(username) || StringUtils.isEmpty(refreshToken);
    }

    private static boolean credentialsIsInvalid(AccountCredentialsDTO credentials) {
        return credentials == null ||
                StringUtils.isEmpty(credentials.getUsername()) ||
                StringUtils.isEmpty(credentials.getPassword());
    }
}
