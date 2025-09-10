package com.myProject.SpringSalesApp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final Logger log = LoggerFactory.getLogger(WebConfig.class);
    @Value("${cors.originPattern:default}")
    private String corsOriginPatterns = " ";

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        WebMvcConfigurer.super.addCorsMappings(registry);
        var allowedOrigins = corsOriginPatterns.split(",");
        registry.addMapping("/**") //todas as requisições vão ser aplicadas cors
                .allowedOrigins(allowedOrigins) // as origens permitidas
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") //metodos que serão permitios;
                .allowCredentials(true);
    }
}


