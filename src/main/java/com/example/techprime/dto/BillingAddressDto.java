package com.example.techprime.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BillingAddressDto {

    private String nickname;
    private String zipCode;
    private String addressType;
    private String address;
    private String number;
    private String complement;
    private String neighborhood;
    private String city;
    private String state;
}
