package com.example.DocumentStorage.service;

import com.amazonaws.services.s3.AmazonS3;
import com.example.DocumentStorage.entity.FileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentService {

    private final AmazonS3 s3Client;

    @Autowired
    public DocumentService(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    public List<FileResponse> searchFiles(String userName, String searchTerm) {
        String userFolder = userName + "/";
        return s3Client.listObjects(bucketName, userFolder).getObjectSummaries().stream()
                .filter(file -> file.getKey().contains(searchTerm))
                .map(file -> new FileResponse(file.getKey().substring(userFolder.length()), file.getKey()))
                .collect(Collectors.toList());
    }


    public void uploadFile(String userName, String fileName, InputStream inputStream, long fileSize) {
        String userFolder = userName + "/";
        s3Client.putObject(bucketName, userFolder + fileName, inputStream, null);
    }
}
