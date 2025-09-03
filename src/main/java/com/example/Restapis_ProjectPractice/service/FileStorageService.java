package com.example.Restapis_ProjectPractice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FileStorageService {
    @Value("${app.upload.dir:/tmp/uploads}")
    private String uploadDir;

    public void printDir() {
        System.out.println("Upload directory: " + uploadDir);
    }
}
