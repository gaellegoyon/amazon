package com.example.similar_product_microservice.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.similar_product_microservice.entity.SimilarProduct;

import com.example.similar_product_microservice.repository.SimilarProductRepository;

@Service
public class SimilarProductService {

    @Autowired
    private SimilarProductRepository similarProductRepository;

    public void updateSimilarProducts(List<UUID> productIds) {
        for (UUID productId : productIds) {
            for (UUID similarProductId : productIds) {
                if (!productId.equals(similarProductId)) {
                    addOrUpdateSimilarProduct(productId, similarProductId);
                }
            }
        }
    }

    private void addOrUpdateSimilarProduct(UUID productId, UUID similarProductId) {
        SimilarProduct similarProduct = similarProductRepository.findByProductIdAndSimilarProductId(productId,
                similarProductId);
        if (similarProduct == null) {
            similarProduct = new SimilarProduct();
            similarProduct.setProductId(productId);
            similarProduct.setSimilarProductId(similarProductId);
            similarProduct.setOccurence(1);
        } else {
            similarProduct.setOccurence(similarProduct.getOccurence() + 1);
        }
        similarProductRepository.save(similarProduct);
    }

    public List<SimilarProduct> getSimilarProducts(UUID productId) {
        return similarProductRepository.findByProductId(productId);
    }

    public void deleteAll() {
        similarProductRepository.deleteAll();
    }
}
