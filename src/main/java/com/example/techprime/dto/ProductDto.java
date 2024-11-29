package com.example.techprime.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private long id;
    private String name;
    private String type;
    private double price;
    private int quantity;
    private Boolean status;
    private int rating;
    private String description;
    private String url_image;
}
