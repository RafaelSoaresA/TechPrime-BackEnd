package com.example.techprime.dto;

import com.example.techprime.domain.entities.Product;
import lombok.*;

import java.util.ArrayList;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaxInvoiceDto {

    private double shippingCost;
    private String shippingType;
    private int installmentsNumber;

    //Forma de pagamento
    PaymentDto paymentDto;

    //Endereço de faturamento
    BillingAddressDto billingAddressDto;

    //Endereço de entrega
    DeliveryAddressDto deliveryAddressDto;

    //Dados do carrinho
    ArrayList<Product> products;

}
