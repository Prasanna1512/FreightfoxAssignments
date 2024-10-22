package com.example.DocumentStorage.controller;

import com.example.DocumentStorage.entity.FileResponse;
import com.example.DocumentStorage.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api/v1/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @GetMapping("/search")
    public ResponseEntity<List<FileResponse>> searchFiles(
            @RequestParam String userName,
            @RequestParam String searchTerm) {
        List<FileResponse> files = documentService.searchFiles(userName, searchTerm);
        return ResponseEntity.ok(files);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @RequestParam String userName,
            @RequestParam String fileName,
            @RequestBody InputStream fileStream) {
        try {
            documentService.uploadFile(userName, fileName, fileStream, fileStream.available());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("File uploaded successfully.");
    }
}

