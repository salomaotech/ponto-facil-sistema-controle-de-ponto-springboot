package com.ana.coutinho.ponto.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileUploadService {

    @Value("${app.uploads.directory}")
    private String uploadDirectory;

    public String getUploadDirectory() {
        return uploadDirectory;
    }

}
