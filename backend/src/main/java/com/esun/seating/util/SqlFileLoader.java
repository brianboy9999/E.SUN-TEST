package com.esun.seating.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class SqlFileLoader {

    public String load(String classpathLocation) {
        try {
            ClassPathResource resource = new ClassPathResource(classpathLocation);
            return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load SQL file: " + classpathLocation, e);
        }
    }
}
