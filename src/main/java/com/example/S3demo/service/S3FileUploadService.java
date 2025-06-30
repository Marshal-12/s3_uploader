package com.example.S3demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import java.io.IOException;

@Service
public class S3FileUploadService {

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    private final S3Client s3Client;

    public S3FileUploadService() {
        this.s3Client = S3Client.builder()
                .region(Region.AP_SOUTH_1) // Specify your desired region
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    public String uploadFile(MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            s3Client.putObject(request, RequestBody.fromBytes(multipartFile.getBytes()));

            return "File uploaded successfully to S3.";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error uploading file to S3: " + e.getMessage();
        }
    }
}
