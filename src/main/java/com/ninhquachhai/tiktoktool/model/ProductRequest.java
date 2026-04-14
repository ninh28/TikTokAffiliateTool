package com.ninhquachhai.tiktoktool.model;

import java.util.List;
import java.util.ArrayList;

public class ProductRequest {
    private String productName = "";
    private String description = "";
    private int videoCount = 3; // ★ GIÁ TRỊ MẶC ĐỊNH LÀ 3 ★
    private List<String> videoPrompts = new ArrayList<>();

    // Default constructor
    public ProductRequest() {}

    // Constructor with parameters
    public ProductRequest(String productName, String description, int videoCount, List<String> videoPrompts) {
        this.productName = productName != null ? productName : "";
        this.description = description != null ? description : "";
        this.videoCount = videoCount > 0 ? videoCount : 3; // ★ LUÔN ĐẢM BẢO >= 3 ★
        this.videoPrompts = videoPrompts != null ? videoPrompts : new ArrayList<>();
    }

    // ★ SPRING CONVENTION GETTERS ★
    public String getProductName() { return productName; }
    public String getDescription() { return description; }
    public int getVideoCount() { return videoCount; }
    public List<String> getVideoPrompts() { return videoPrompts; }

    // ★ RECORD-STYLE GETTERS (for backward compatibility) ★
    public String productName() { return productName; }
    public String description() { return description; }
    public int videoCount() { return videoCount; }
    public List<String> videoPrompts() { return videoPrompts; }

    // ★ SPRING CONVENTION SETTERS ★
    public void setProductName(String productName) {
        this.productName = productName != null ? productName : "";
    }

    public void setDescription(String description) {
        this.description = description != null ? description : "";
    }

    public void setVideoCount(int videoCount) {
        this.videoCount = videoCount > 0 ? videoCount : 3; // ★ LUÔN ĐẢM BẢO >= 3 ★
    }

    public void setVideoPrompts(List<String> videoPrompts) {
        this.videoPrompts = videoPrompts != null ? videoPrompts : new ArrayList<>();
    }

    @Override
    public String toString() {
        return "ProductRequest{" +
                "productName='" + productName + '\'' +
                ", description='" + description + '\'' +
                ", videoCount=" + videoCount +
                ", videoPrompts=" + videoPrompts +
                '}';
    }
}