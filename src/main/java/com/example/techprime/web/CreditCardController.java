package com.example.techprime.web;

import com.example.techprime.domain.entities.CreditCard;
import com.example.techprime.domain.entities.Customer;
import com.example.techprime.domain.service.CreditCardService;
import com.example.techprime.dto.CustomerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/creditCard")
public class CreditCardController {

    private final CreditCardService creditCardService;
    @PostMapping("/create")
    public ResponseEntity<CreditCard> saveCreditCard(@RequestBody CreditCard creditCard) {
        return ResponseEntity.ok(creditCardService.create(creditCard));
    }
}
