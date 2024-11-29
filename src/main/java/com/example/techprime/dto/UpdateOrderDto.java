package com.example.techprime.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrderDto {
    private String newStatus;
    private int invoiceId;
    private String description;
    private String invoiceNumber;
}
