package com.example.similar_product_microservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.similar_product_microservice.entity.SimilarProduct;
import com.example.similar_product_microservice.exceptions.NotFoundException;
import com.example.similar_product_microservice.service.SimilarProductService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/similars")
public class SimilarProductController {

    @Autowired
    private SimilarProductService similarProductService;

    @GetMapping("/{productId}")
    public ResponseEntity<List<SimilarProduct>> getSimilarProducts(@PathVariable UUID productId) {
        List<SimilarProduct> similarProducts = similarProductService.getSimilarProducts(productId);

        if (similarProducts == null || similarProducts.isEmpty()) {
            throw new NotFoundException("Aucun produit similaire trouvé pour l'ID produit : " + productId);
        }

        return ResponseEntity.ok(similarProducts);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAll() {
        similarProductService.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
