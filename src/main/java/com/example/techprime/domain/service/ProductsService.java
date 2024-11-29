package com.example.techprime.domain.service;

import com.example.techprime.domain.entities.*;
import com.example.techprime.domain.repository.ProductImageRepository;
import com.example.techprime.domain.repository.ProductsRepository;
import com.example.techprime.domain.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service

public class ProductsService {

    private final UsersRepository usersRepository;
    private final ProductsRepository productsRepository;
    private final UsersService usersService;
    private final ProductImageRepository productImageRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    public Product create(Product product) {

        Optional<User> usersOptional = this.usersRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        if(!Objects.equals(usersOptional.get().getRole(), "admin")){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autorizado, é necessário o perfil 'admin'");
        }

        product = product.toBuilder().build();
        log.info("Criando produto... : {}", product);
        try {
            return this.productsRepository.save(product);
        }
        catch (Exception e) {
            log.error("Erro ao criar produto: {}", e);
        }
        return null;
    }

    public Product update(Product product, Integer id) {
        /*Optional<User> usersOptional = this.usersRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        if(!Objects.equals(usersOptional.get().getRole(), "admin")){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autorizado, é necessário o perfil 'admin'");
        }*/

        Product productAPI = new Product();

        try {
            productAPI = productsRepository.findById(id).get();
            log.info("Buscando produto por ID: {}", id);
        } catch (Exception e) {
            log.error("Erro ao buscar produto: {}", e);
        }

        productAPI.setId(id);
        productAPI.setName(product.getName());
        productAPI.setType(product.getType());
        productAPI.setPrice(product.getPrice());
        productAPI.setQuantity(product.getQuantity());
        productAPI.setRating(product.getRating());
        productAPI.setDescription(product.getDescription());
        productAPI.setDeleted(product.getDeleted());


        return productsRepository.save(productAPI);
    }

    public List<Product> findAll() {

        log.info("Listando produtos...");
        Optional<User> usersOptional = usersService.findByUserAuthenticated();


        if(usersOptional.isEmpty()){
           return productsRepository.findAllWhereActiveIsFalseOrNull();
        }
        if(usersOptional.get().getRole().equals("admin") || usersOptional.get().getRole().equals("stocker")) {
            return this.productsRepository.findAll();
        } else if (usersOptional.get().getRole().equals("customer")) {
            return productsRepository.findAllWhereActiveIsFalseOrNull();
        } else{
            throw new RuntimeException("Acesso não permitido");
        }
    }

    public Optional<Product> findById(Integer id) {
        return productsRepository.findById(id);
    }

    public Product updatePrdStatus(Product product){
        return productsRepository.save(product);
    }

    public ProductImage insertProductImage(ProductImage productImage){
        Optional<User> usersOptional = usersService.findByUserAuthenticated();

        if(usersOptional.isEmpty()){
            throw new RuntimeException("USUÁRIO NÃO ENCONTRADO");
        }

        if(usersOptional.get().getRole().equals("admin") || usersOptional.get().getRole().equals("stocker")){
            return productImageRepository.save(productImage);
        }else{
            throw new RuntimeException("Acesso não permitido");
        }
    }

    public Boolean deleteProductImage(int id){
        Optional<User> usersOptional = usersService.findByUserAuthenticated();

        if(usersOptional.isEmpty()){
            throw new RuntimeException("USUÁRIO NÃO ENCONTRADO");
        }

        if(usersOptional.get().getRole().equals("admin") || usersOptional.get().getRole().equals("stocker")){
            productImageRepository.delete(productImageRepository.findById(id).get());
            return true;
        }else{
            throw new RuntimeException("Acesso não permitido");
        }
    }

    public List<ProductImage> listImages(int productId){
        Optional<Product> productOptional = productsRepository.findById(productId);

        if(productOptional.isEmpty()){
            throw new RuntimeException("PRODUTO NÃO ENCONTRADO");
        }

        return productImageRepository.findByProduct(productOptional.get());
    }
}
