package com.example.PdfGeneration;

import com.example.PdfGeneration.entity.InvoiceRequest;
import com.example.PdfGeneration.service.PdfGeneratorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class PdfGenerationTests {

    @Autowired
    private PdfGeneratorService pdfService;

    @Test
    void testGeneratePdf() throws Exception {
        // Create an instance of InvoiceRequest
        InvoiceRequest request = new InvoiceRequest();

        // Set seller information
        request.setSeller("XYZ Pvt. Ltd.");
        request.setSellerGstin("29AABBCCDD121ZD");
        request.setSellerAddress("New Delhi, India");

        // Set buyer information
        request.setBuyer("Vedant Computers");
        request.setBuyerGstin("29AABBCCDD131ZD");
        request.setBuyerAddress("New Delhi, India");

        // Set items
        InvoiceRequest.Item item1 = new InvoiceRequest.Item();
        item1.setName("Product 1");
        item1.setQuantity("12 Nos");
        item1.setRate(123.00);
        item1.setAmount(1476.00);

        InvoiceRequest.Item item2 = new InvoiceRequest.Item();
        item2.setName("Product 2");
        item2.setQuantity("5 Nos");
        item2.setRate(200.00);
        item2.setAmount(1000.00);

        // Add items to the request
        request.setItems(List.of(item1, item2));

        // Call the service to generate the PDF and validate the result
        byte[] pdfData = pdfService.generatePdf(request);

        // Validate that PDF data is not null and contains content
        assertNotNull(pdfData);
        assertTrue(pdfData.length > 0);
    }
}

