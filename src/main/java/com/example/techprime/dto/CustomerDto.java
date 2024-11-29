package com.example.techprime.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {

    //Campos de customer
    private String document;
    private String fullname;
    private String customerType;
    private String gender;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthDate;
    private String phone1;
    private String phone2;

    //Campos de user
    CustomerUserDto userDto;

    //Campos de billing_invoice_address
    BillingAddressDto billingAddressDto;

    //Campos de delivery_invoice_address
    DeliveryAddressDto deliveryAddressDto;

}
