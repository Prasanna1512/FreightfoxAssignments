package com.example.PdfGeneration.controller;
import com.example.PdfGeneration.entity.InvoiceRequest;
import com.example.PdfGeneration.service.PdfGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/pdf")
public class PdfController {

    @Autowired
    private PdfGeneratorService pdfService;

    @PostMapping("/generate")
    public ResponseEntity<byte[]> generatePdf(@RequestBody InvoiceRequest request) throws Exception {
        byte[] pdfData = pdfService.generatePdf(request);

        // Save to file
        String filePath = "D:\\PdfGeneratedFiles" + request.getBuyer() + "_invoice.pdf";
        Files.write(Paths.get(filePath), pdfData);

        // Return as ResponseEntity
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfData);
    }

    @GetMapping("/download/{buyer}")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable String buyer) throws Exception {
        String filePath = "D:\\PdfGeneratedFiles" + buyer + "_invoice.pdf";
        Path path = Paths.get(filePath);
        byte[] pdfData = Files.readAllBytes(path);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + buyer + "_invoice.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfData);
    }
}

