package com.example.techprime.web;

import com.example.techprime.domain.entities.Product;
import com.example.techprime.domain.entities.ProductImage;
import com.example.techprime.domain.entities.User;
import com.example.techprime.domain.service.ProductsService;
import com.example.techprime.dto.ProductDto;
import com.example.techprime.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductsController {

    private final ProductsService productsService;

    @PostMapping("/create")
    public ResponseEntity<Product> createProduct (@RequestBody Product product) {
        return ResponseEntity.ok(productsService.create(product));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Product> updateProduct (@PathVariable Integer id,
                                                  @RequestBody Product product) {
      //product = productsService.findById(id);
      return ResponseEntity.ok().body(productsService.update(product, id));
    }

    @GetMapping("/list")
    public ResponseEntity<List<Product>> listProducts () {
        List<Product> products = productsService.findAll();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/product-info/{id}")
    public ResponseEntity<ProductDto> getUserInfo(@PathVariable int id) {
        Optional<Product> productById = productsService.findById(id);

        if (productById.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var productInfo = ProductDto.builder()
                .id(productById.get().getId())
                .name(productById.get().getName())
                .type(productById.get().getType())
                .price(productById.get().getPrice())
                .quantity(productById.get().getQuantity())
                .rating(productById.get().getRating())
                .description(productById.get().getDescription())
                .url_image(productById.get().getUrl_image())
                .status(productById.get().getDeleted())
                .build();

        return ResponseEntity.ok().body(productInfo);
    }

    @PatchMapping("/alter/{id}/{status}")
    public ResponseEntity<?> updateProductStatus(@PathVariable Integer id, @PathVariable String status) {
        Optional<Product> product = productsService.findById(id);

        if(product.isPresent()) {
            Product existingPrd = product.get();

            if("activated".equalsIgnoreCase(status)) {
                existingPrd.setDeleted(false);
            } else if ("deactivated".equalsIgnoreCase(status)) {
                existingPrd.setDeleted(true);
            }else {
                return ResponseEntity.badRequest().body("Status inválido");
            }

            productsService.updatePrdStatus(existingPrd);
            return ResponseEntity.ok().body("Status do produto atualizado com sucesso.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
    }

    @PostMapping("/insert/image")
    public ResponseEntity<ProductImage> insertProductImage(@RequestBody ProductImage productImage){
        return ResponseEntity.ok().body(productsService.insertProductImage(productImage));
    }

    @DeleteMapping("/delete/image/{id}")
    public ResponseEntity<Boolean> deleteProductImage(@PathVariable int id){
        return ResponseEntity.ok().body(productsService.deleteProductImage(id));
    }

    @GetMapping("/images/{productId}")
    public ResponseEntity<List<ProductImage>> listImages(@PathVariable int productId){
        return ResponseEntity.ok().body(productsService.listImages(productId));
    }
}
