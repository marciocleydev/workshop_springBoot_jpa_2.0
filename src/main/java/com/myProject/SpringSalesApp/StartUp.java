package com.myProject.SpringSalesApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class StartUp {

	public static void main(String[] args) {
		SpringApplication.run(StartUp.class, args);
       // generateHashedPassword();

	}
    private static void generateHashedPassword() {
            PasswordEncoder pbkdf2Encoder = new Pbkdf2PasswordEncoder(
                    "", 8, 185000,
                    Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);
                Map<String, PasswordEncoder> encoders = new HashMap<>();
                encoders.put("pbkdf2", pbkdf2Encoder);
                DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);

                passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2Encoder);
                //gera o hash da senha, ideal para salvar no banco de dados
                var pass1 = passwordEncoder.encode("admin123");
                var pass2 = passwordEncoder.encode("M@rcio81050050");

        System.out.println(pass1);
        System.out.println(pass2);
    }
}
