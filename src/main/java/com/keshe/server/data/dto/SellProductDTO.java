package com.keshe.server.data.dto;

import lombok.Data;

import java.util.List;

@Data
public class SellProductDTO {
    private String productName;
    private String productDescription;
    private double productPrice;
    private List<String> productUrl;
    private String productCategory;
}
