package com.example.demoApi.controllers;

import com.example.demoApi.dtos.ProductRecordDto;
import com.example.demoApi.models.ProductModel;
import com.example.demoApi.repositories.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductRepository productRepository;

    @PostMapping
    public ResponseEntity<ProductModel> saveProduct(@RequestBody @Valid ProductRecordDto productRecordDto) {
        var productModel = new ProductModel();
        BeanUtils.copyProperties(productRecordDto, productModel);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productRepository.save(productModel));
    }

    @GetMapping
    public ResponseEntity<List<ProductModel>> getAllProducts(){
        return ResponseEntity
                .ok(productRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneProduct(@PathVariable UUID id){
        Optional<ProductModel> productFound = productRepository.findById(id);

        return productFound.isPresent() ?
                ResponseEntity.status(HttpStatus.OK).body(productFound.get()) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateProduct(
            @PathVariable
            UUID id,
            @RequestBody
            @Valid
            ProductRecordDto productRecordDto
    ){
        Optional<ProductModel> productFound = productRepository.findById(id);

        if (productFound.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }

        var productUpdated = productFound.get();
        BeanUtils.copyProperties(productRecordDto, productUpdated);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productRepository.save(productUpdated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable UUID id){
        Optional<ProductModel> productFound = productRepository.findById(id);

        if (productFound.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }

        productRepository.delete(productFound.get());
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

}
