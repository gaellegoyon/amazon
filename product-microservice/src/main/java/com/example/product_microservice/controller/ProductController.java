package com.example.product_microservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.product_microservice.exceptions.BadRequestException;
import com.example.product_microservice.exceptions.NotFoundException;
import com.example.product_microservice.model.Product;
import com.example.product_microservice.service.ProductService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable UUID id) {
        Product product = productService.findById(id);

        if (product == null) {
            throw new NotFoundException("Produit non trouvé pour l'ID : " + id);
        }

        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        if (product.getName() == null || product.getName().isEmpty()) {
            throw new BadRequestException("Le nom du produit est obligatoire.");
        }
        if (product.getPrice() == null || product.getPrice().doubleValue() <= 0) {
            throw new BadRequestException("Le prix du produit doit être supérieur à zéro.");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(productService.save(product));
    }

    @PutMapping("/{id}/add-order")
    public ResponseEntity<Product> addOrderToProduct(@PathVariable UUID id, @RequestBody UUID orderId) {
        Product product = productService.findById(id);

        if (product == null) {
            throw new NotFoundException("Produit non trouvé pour l'ID : " + id);
        }

        if (orderId == null) {
            throw new BadRequestException("L'ID de la commande est obligatoire.");
        }

        productService.addOrderToProducts(List.of(id), orderId);
        return ResponseEntity.ok(productService.findById(id));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllProducts() {
        productService.deleteAll();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable UUID id) {
        Product product = productService.findById(id);

        if (product == null) {
            throw new NotFoundException("Impossible de supprimer. Produit non trouvé pour l'ID : " + id);
        }

        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
