package com.example.similar_product_microservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.similar_product_microservice.entity.SimilarProduct;
import com.example.similar_product_microservice.service.SimilarProductService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/similars")
public class SimilarProductController {

    @Autowired
    private SimilarProductService similarProductService;

    @GetMapping("/{productId}")
    public List<SimilarProduct> getSimilarProducts(@PathVariable UUID productId) {
        return similarProductService.getSimilarProducts(productId);
    }

    @DeleteMapping
    public void deleteAll() {
        similarProductService.deleteAll();
    }
}
