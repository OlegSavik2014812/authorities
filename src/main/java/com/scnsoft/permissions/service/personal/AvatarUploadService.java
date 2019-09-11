package com.scnsoft.permissions.service.personal;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
public class AvatarUploadService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AvatarUploadService.class);
    @Value("${cloudinary.name}")
    private String name;
    @Value("${cloudinary.key}")
    private String key;
    @Value("${cloudinary.secret}")
    private String secret;
    private Map<String, String> cloudCredentials;

    @PostConstruct
    private void init() {
        cloudCredentials = Map.of(
                "cloud_name", name,
                "api_key", key,
                "api_secret", secret);
    }

    public void upload(File file) {
        Cloudinary cloudinary = new Cloudinary();
        Uploader uploader = cloudinary.uploader();
        try {
            uploader.upload(file, cloudCredentials);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }
}