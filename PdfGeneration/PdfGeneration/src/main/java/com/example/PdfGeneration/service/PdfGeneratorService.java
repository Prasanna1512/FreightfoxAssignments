package com.example.PdfGeneration.service;

import com.example.PdfGeneration.entity.InvoiceRequest;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class PdfGeneratorService {

    // Inject the directory path from application.properties
    @Value("${pdf.storage.directory}")
    private String pdfStorageDirectory;

    /**
     * Generate a PDF based on the provided InvoiceRequest object and save it to the specified directory.
     * @param invoiceRequest The invoice details to be included in the PDF.
     * @return The byte array of the generated PDF.
     * @throws Exception if there is an error during PDF creation or file operations.
     */
    public byte[] generatePdf(InvoiceRequest invoiceRequest) throws Exception {
        // Ensure the directory exists or create it
        Path storagePath = Paths.get(pdfStorageDirectory);
        if (!Files.exists(storagePath)) {
            Files.createDirectories(storagePath);
        }

        // Define the file name based on buyer's name and save it in the specified directory
        String fileName = invoiceRequest.getBuyer().replaceAll("\\s+", "_") + "_invoice.pdf";
        String filePath = pdfStorageDirectory + "/" + fileName;

        // Check if the file already exists
        File pdfFile = new File(filePath);
        if (pdfFile.exists()) {
            // If it already exists, read and return the existing PDF as byte array
            return Files.readAllBytes(Paths.get(filePath));
        }

        // Create a new PDF document
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        Document document = new Document(new com.itextpdf.kernel.pdf.PdfDocument(writer));

        // Add content to the PDF
        document.add(new Paragraph("Invoice"));
        document.add(new Paragraph("Seller: " + invoiceRequest.getSeller()));
        document.add(new Paragraph("Seller GSTIN: " + invoiceRequest.getSellerGstin()));
        document.add(new Paragraph("Seller Address: " + invoiceRequest.getSellerAddress()));
        document.add(new Paragraph("Buyer: " + invoiceRequest.getBuyer()));
        document.add(new Paragraph("Buyer GSTIN: " + invoiceRequest.getBuyerGstin()));
        document.add(new Paragraph("Buyer Address: " + invoiceRequest.getBuyerAddress()));

        // Add items table
        Table table = new Table(4); // 4 columns: Item Name, Quantity, Rate, Amount
        table.addCell("Item Name");
        table.addCell("Quantity");
        table.addCell("Rate");
        table.addCell("Amount");

        // Iterate through each item and add it to the table
        for (InvoiceRequest.Item item : invoiceRequest.getItems()) {
            table.addCell(item.getName());
            table.addCell(item.getQuantity());
            table.addCell(String.valueOf(item.getRate()));
            table.addCell(String.valueOf(item.getAmount()));
        }
        document.add(table);

        // Close the document to finalize PDF creation
        document.close();

        // Write the PDF to the specified directory
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(out.toByteArray());
        }

        // Return the PDF as byte array
        return out.toByteArray();
    }
}
