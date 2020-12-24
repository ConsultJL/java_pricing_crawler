package com.consultjl.webcrawler.services;

import com.consultjl.webcrawler.models.Product;
import com.consultjl.webcrawler.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Iterable<Product> list() {
        return productRepository.findAll();
    }
}
