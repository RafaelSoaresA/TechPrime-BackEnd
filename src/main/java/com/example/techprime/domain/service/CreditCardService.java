package com.example.techprime.domain.service;

import com.example.techprime.domain.entities.CreditCard;
import com.example.techprime.domain.entities.User;
import com.example.techprime.domain.repository.CreditCardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class CreditCardService {

    private final CreditCardRepository creditCardRepository;
    private final UsersService usersService;

    public CreditCard create(CreditCard creditCard){
        Optional<User> usersOptional = usersService.findByUserAuthenticated();

        if(!Objects.equals(usersOptional.get().getRole(), "customer")){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autorizado, é necessário o perfil 'customer'");
        }

        creditCard.setToken(generateGuid());

        creditCard.setUser(usersOptional.get());

        log.info("Cadastrando cartão... : {}", creditCard);
        try {
            return this.creditCardRepository.save(creditCard);
        }
        catch (Exception e) {
            log.error("Erro ao cadastrar cartão de crédito: {}", e);
        }
        return null;
    }

    public String generateGuid() {
        UUID uuid = UUID.randomUUID(); return uuid.toString();
    }
}
