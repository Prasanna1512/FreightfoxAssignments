package com.example.PdfGeneration.entity;
import lombok.Data;
import java.util.List;

@Data
public class InvoiceRequest {
    private String seller;
    private String sellerGstin;
    private String sellerAddress;
    private String buyer;
    private String buyerGstin;
    private String buyerAddress;
    private List<Item> items;

    @Data
    public static class Item {
        private String name;
        private String quantity;
        private double rate;
        private double amount;
    }
}

