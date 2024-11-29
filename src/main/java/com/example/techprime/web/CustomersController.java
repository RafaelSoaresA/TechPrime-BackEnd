package com.example.techprime.web;

import com.example.techprime.domain.entities.Customer;
import com.example.techprime.domain.entities.CustomerAddress;
import com.example.techprime.domain.entities.User;
import com.example.techprime.domain.service.CustomersService;
import com.example.techprime.dto.CustomerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/customer")
public class CustomersController {

    private final CustomersService customersService;
    @PostMapping("/create")
    public ResponseEntity<Customer> saveCustomer(@RequestBody CustomerDto customerDto) {
        return ResponseEntity.ok(customersService.create(customerDto));
    }

    @GetMapping("/customerAddress")
    public ResponseEntity<List<CustomerAddress>> getCustomerAddress(){
        return ResponseEntity.ok(customersService.getCustomerAddress());
    }

    @PostMapping("/deliveryAddress/create")
    public ResponseEntity<CustomerAddress> saveDeliveryAddress(@RequestBody CustomerAddress customerAddress){
        return ResponseEntity.ok(customersService.saveDeliveryAddress(customerAddress));
    }

    @PutMapping("/billingAddress/update")
    public ResponseEntity<CustomerAddress> updateBillingAddress(@RequestBody CustomerAddress customerAddress){
        return ResponseEntity.ok(customersService.updateBillingAddress(customerAddress));
    }

    @GetMapping("/user")
    public ResponseEntity<Customer> getCustomerByUserId(){
        return ResponseEntity.ok(customersService.getCustomerByUserId());
    }
}
