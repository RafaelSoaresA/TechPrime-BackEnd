package com.example.techprime.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {

    private String paymentType;
    private String creditCardToken;
}
