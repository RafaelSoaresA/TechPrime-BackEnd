package com.example.techprime.domain.service;

import com.example.techprime.domain.entities.*;
import com.example.techprime.domain.repository.*;
import com.example.techprime.dto.TaxInvoiceDto;
import com.example.techprime.dto.UpdateOrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.constraintvalidators.bv.time.futureorpresent.FutureOrPresentValidatorForReadableInstant;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Slf4j
@Service
public class TaxInvoiceService {

    private final TaxInvoiceRepository taxInvoiceRepository;
    private final TaxInvoiceItemRepository taxInvoiceItemRepository;
    private final UsersService usersService;
    private final CustomersRepository customersRepository;
    private final CreditCardRepository creditCardRepository;
    private final InvoiceAddressRepository invoiceAddressRepository;
    private final HistoryInvoiceRepository historyInvoiceRepository;
    private final ProductImageRepository productImageRepository;

    @Transactional
    public TaxInvoice createTaxInvoice(TaxInvoiceDto taxInvoiceDto) {
        double totalCost = 0;

        Optional<User> usersOptional = usersService.findByUserAuthenticated();
        if(usersOptional.isEmpty()){
            throw new RuntimeException("USUÁRIO NÃO ENCONTRADO");
        }
        Optional<Customer> customerOptional = customersRepository.findByUser(usersOptional.get());

        if(customerOptional.isEmpty()){
            throw new RuntimeException("CUSTOMER NÃO ENCONTRADO");
        }

        InvoiceAddress billingInvoiceAddressBuilder = InvoiceAddress.builder()
                .customerAddressType("B")
                .nickname(taxInvoiceDto.getBillingAddressDto().getNickname())
                .zipCode(taxInvoiceDto.getBillingAddressDto().getZipCode())
                .address(taxInvoiceDto.getBillingAddressDto().getAddress())
                .addressType(taxInvoiceDto.getBillingAddressDto().getAddressType())
                .number(taxInvoiceDto.getBillingAddressDto().getNumber())
                .complement(taxInvoiceDto.getBillingAddressDto().getComplement())
                .neighborhood(taxInvoiceDto.getBillingAddressDto().getNeighborhood())
                .city(taxInvoiceDto.getBillingAddressDto().getCity())
                .state(taxInvoiceDto.getBillingAddressDto().getState())
                .build();
        InvoiceAddress billingInvoiceAddress = invoiceAddressRepository.save(billingInvoiceAddressBuilder);

        InvoiceAddress deliveryInvoiceAddressBuilder = InvoiceAddress.builder()
                .customerAddressType("D")
                .nickname(taxInvoiceDto.getDeliveryAddressDto().getNickname())
                .zipCode(taxInvoiceDto.getDeliveryAddressDto().getZipCode())
                .address(taxInvoiceDto.getDeliveryAddressDto().getAddress())
                .addressType(taxInvoiceDto.getDeliveryAddressDto().getAddressType())
                .number(taxInvoiceDto.getDeliveryAddressDto().getNumber())
                .complement(taxInvoiceDto.getDeliveryAddressDto().getComplement())
                .neighborhood(taxInvoiceDto.getDeliveryAddressDto().getNeighborhood())
                .city(taxInvoiceDto.getDeliveryAddressDto().getCity())
                .state(taxInvoiceDto.getDeliveryAddressDto().getState())
                .build();
        InvoiceAddress deliveyInvoiceAddress= invoiceAddressRepository.save(deliveryInvoiceAddressBuilder);


        for (Product product : taxInvoiceDto.getProducts()) {
            totalCost += product.getPrice() * product.getQuantity();
        }

        TaxInvoice taxInvoiceBuilder = TaxInvoice.builder()
                .customer(customerOptional.get())
                .document(customerOptional.get().getDocument())
                .customerType(customerOptional.get().getCustomerType())
                .installmentsNumber(taxInvoiceDto.getInstallmentsNumber())
                .fullname(customerOptional.get().getFullname())
                .totalCost(totalCost)
                .shippingCost(taxInvoiceDto.getShippingCost())
                .invoiceStatus("AP")
                .createdAt(LocalDateTime.now())
                .issueDate(LocalDateTime.now())
                .paymentType(taxInvoiceDto.getPaymentDto().getPaymentType())
                .billingAddress(billingInvoiceAddress)
                .deliveryAddress(deliveyInvoiceAddress)
                .shippingType(taxInvoiceDto.getShippingType())
                .build();

        if(Objects.equals(taxInvoiceDto.getPaymentDto().getPaymentType(), "CC")){
            Optional<CreditCard> creditCardOptional = Optional.empty();
            creditCardOptional = creditCardRepository.findByToken(taxInvoiceDto.getPaymentDto().getCreditCardToken());
            creditCardOptional.ifPresent(taxInvoiceBuilder::setCreditCard);
        }


        TaxInvoice taxInvoice = taxInvoiceRepository.save(taxInvoiceBuilder);

        for (Product product : taxInvoiceDto.getProducts()) {
            TaxInvoiceItem taxInvoiceItem = TaxInvoiceItem.builder()
                            .taxInvoice(taxInvoice)
                    .product(product)
                    .productName(product.getName())
                    .productPrice(product.getPrice())
                    .quantity(product.getQuantity())
                    .build();
            taxInvoiceItemRepository.save(taxInvoiceItem);
        }

        return taxInvoice;
    }

    public List<TaxInvoice> getCustomerOrders (){
        Optional<User> usersOptional = usersService.findByUserAuthenticated();
        if(usersOptional.isEmpty()){
            throw new RuntimeException("USUÁRIO NÃO ENCONTRADO");
        }
        Optional<Customer> customerOptional = customersRepository.findByUser(usersOptional.get());

        if(customerOptional.isEmpty()){
            throw new RuntimeException("CUSTOMER NÃO ENCONTRADO");
        }

        Customer customer =  customerOptional.get();

        return taxInvoiceRepository.findByCustomer(customer);
    }

    public List<TaxInvoice> getAllOrders(){
        Optional<User> usersOptional = usersService.findByUserAuthenticated();

        if(usersOptional.isEmpty()){
            throw new RuntimeException("USUÁRIO NÃO ENCONTRADO");
        }

        if(usersOptional.get().getRole().equals("admin") || usersOptional.get().getRole().equals("stocker")){
           return taxInvoiceRepository.findAll();
        }else{
            throw new RuntimeException("Acesso não permitido");
        }
    }

    public TaxInvoice getOrder(int id){
        Optional<User> usersOptional = usersService.findByUserAuthenticated();

        if(usersOptional.get().getRole().equals("admin") || usersOptional.get().getRole().equals("stocker")){
            log.info("Acesso autorizado");
            Optional<TaxInvoice> taxInvoice = taxInvoiceRepository.findByid(id);
            return taxInvoice.get();
        }else{
            Optional<Customer> customerOptional = customersRepository.findByUser(usersOptional.get());
            Optional<TaxInvoice> taxInvoice = taxInvoiceRepository.findByid(id);
            if(customerOptional.isEmpty() || taxInvoice.get().getCustomer().getId() != customerOptional.get().getId()){
                throw new RuntimeException("Acesso não autorizado");
            }
            return taxInvoice.get();
        }
    }

    public List<TaxInvoiceItem> getOrderDetails(int orderId){
        Optional<User> usersOptional = usersService.findByUserAuthenticated();

        if(usersOptional.get().getRole().equals("admin") || usersOptional.get().getRole().equals("stocker")){
            log.info("Acesso autorizado");
            Optional<TaxInvoice> taxInvoice = taxInvoiceRepository.findByid(orderId);

            List<TaxInvoiceItem> lstTaxInvoice = taxInvoiceItemRepository.findByTaxInvoice(taxInvoice.get());
            for (TaxInvoiceItem item : lstTaxInvoice) {
                //buscar URLimagem da ProductImage a atribuir o primeiro
                item.getProduct().setUrl_image(productImageRepository.findByProduct(item.getProduct()).getFirst().getUrlImage());
            }

            return lstTaxInvoice;
            //return taxInvoiceItemRepository.findByTaxInvoice(taxInvoice.get());
        }else{
            Optional<Customer> customerOptional = customersRepository.findByUser(usersOptional.get());
            Optional<TaxInvoice> taxInvoice = taxInvoiceRepository.findByid(orderId);
            if(customerOptional.isEmpty() || taxInvoice.get().getCustomer().getId() != customerOptional.get().getId()){
                throw new RuntimeException("Acesso não autorizado");
            }

            List<TaxInvoiceItem> lstTaxInvoice = taxInvoiceItemRepository.findByTaxInvoice(taxInvoice.get());
            for (TaxInvoiceItem item : lstTaxInvoice) {
                //buscar URLimagem da ProductImage a atribuir o primeiro
                item.getProduct().setUrl_image(productImageRepository.findByProduct(item.getProduct()).getFirst().getUrlImage());
            }

            return lstTaxInvoice;
            //return taxInvoiceItemRepository.findByTaxInvoice(taxInvoice.get());
        }
    }

    public TaxInvoice orderUpdate(UpdateOrderDto updateOrderDto){
        Optional<User> usersOptional = usersService.findByUserAuthenticated();


        if(usersOptional.isEmpty()){
            throw new RuntimeException("USUÁRIO NÃO ENCONTRADO");
        }
        if(usersOptional.get().getRole().equals("admin") || usersOptional.get().getRole().equals("stocker")){
            Optional<TaxInvoice> taxInvoice = taxInvoiceRepository.findByid(updateOrderDto.getInvoiceId());

            HistoryInvoice historyInvoice = HistoryInvoice.builder()
                    .taxInvoice(taxInvoice.get())
                    .createdAt(LocalDateTime.now())
                    .user(usersOptional.get())
                    .beforeInvoiceStatus(taxInvoice.get().getInvoiceStatus())
                    .actualInvoiceStatus(updateOrderDto.getNewStatus())
                    .description(updateOrderDto.getDescription())
                    .build();

            historyInvoiceRepository.save(historyInvoice);

            taxInvoice.get().setInvoiceStatus(updateOrderDto.getNewStatus());
            taxInvoice.get().setInvoiceNumber(updateOrderDto.getInvoiceNumber());
            taxInvoice.get().setIssueDate(LocalDateTime.now());

           return taxInvoiceRepository.save(taxInvoice.get());
        }else{
            throw new RuntimeException("Acesso não permitido");
        }
    }

    public List<HistoryInvoice> getOrderHistory(int id){
        Optional<User> usersOptional = usersService.findByUserAuthenticated();
        Optional<TaxInvoice> taxInvoice;
        if(usersOptional.get().getRole().equals("admin") || usersOptional.get().getRole().equals("stocker")){
            log.info("Acesso autorizado");
            taxInvoice = taxInvoiceRepository.findByid(id);
        }else{
            Optional<Customer> customerOptional = customersRepository.findByUser(usersOptional.get());
            taxInvoice = taxInvoiceRepository.findByid(id);
            if(customerOptional.isEmpty() || taxInvoice.get().getCustomer().getId() != customerOptional.get().getId()){
                throw new RuntimeException("Acesso não autorizado");
            }
        }

        return historyInvoiceRepository.findByTaxInvoice(taxInvoice.get());
    }
}



