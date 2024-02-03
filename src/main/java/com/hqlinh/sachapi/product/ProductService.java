package com.hqlinh.sachapi.product;

import com.hqlinh.sachapi.util.DTOUtil;
import com.hqlinh.sachapi.util.ValueMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import jakarta.validation.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class ProductService {
    private IProductRepository productRepository;
    private Validator validator;

    public ProductDTO.ProductResponseDTO create(ProductDTO.ProductRequestDTO productRequestDTO) {
        ProductDTO.ProductResponseDTO productResponseDTO;
        try {
            log.info("ProductService::create execution started...");

            Product product = DTOUtil.map(productRequestDTO, Product.class);
            log.debug("ProductService:create request parameters {}", ValueMapper.jsonAsString(productRequestDTO));

            Product productResult = productRepository.save(product);
            productResponseDTO = DTOUtil.map(productResult, ProductDTO.ProductResponseDTO.class);
            log.debug("ProductService:create request parameters {}", ValueMapper.jsonAsString(productRequestDTO));
        } catch (ProductException.ProductServiceBusinessException ex) {
            log.error("Exception occurred while persisting product to database , Exception message {}", ex.getMessage());
            throw new ProductException.ProductServiceBusinessException("Exception occurred while create a new product!");
        }

        log.info("ProductService::create execution ended...");
        return productResponseDTO;
    }

    public List<ProductDTO.ProductResponseDTO> getProducts() {
        List<ProductDTO.ProductResponseDTO> productResponseDTOS;
        try {
            log.info("ProductService::getProducts execution started...");

            List<Product> productList = productRepository.findAll();
            productResponseDTOS = productList.isEmpty() ? Collections.emptyList() : DTOUtil.mapList(productList, ProductDTO.ProductResponseDTO.class);
            log.debug("ProductService:getProducts retrieving products from database  {}", ValueMapper.jsonAsString(productResponseDTOS));
        } catch (ProductException.ProductServiceBusinessException ex) {
            log.error("Exception occurred while retrieving products from database , Exception message {}", ex.getMessage());
            throw new ProductException.ProductServiceBusinessException("Exception occurred while fetch products from Database");
        }

        log.info("ProductService::getProductById execution ended...");
        return productResponseDTOS;
    }

    public ProductDTO.ProductResponseDTO getProductById(Long productId) {
        ProductDTO.ProductResponseDTO productResponseDTO;
        try {
            log.info("ProductService::getProductById execution started...");

            Product product = productRepository.findById(productId).orElseThrow(() -> new NoResultException("Product not found with id " + productId));
            productResponseDTO = DTOUtil.map(product, ProductDTO.ProductResponseDTO.class);
            log.debug("ProductService:getProductById retrieving product from database for id {} {}", productId, ValueMapper.jsonAsString(productResponseDTO));
        } catch (ProductException.ProductServiceBusinessException ex) {
            log.error("Exception occurred while retrieving product {} from database , Exception message {}", productId, ex.getMessage());
            throw new ProductException.ProductServiceBusinessException("Exception occurred while fetch product from Database " + productId);
        }

        log.info("ProductService::getProductById execution ended...");
        return productResponseDTO;
    }

    public ProductDTO.ProductResponseDTO updateProductById(Long productId, Map<String, Object> fields) {
        ProductDTO.ProductResponseDTO productResponseDTO;
        try {
            log.info("ProductService::updateProductById execution started...");

            Product existProduct = DTOUtil.map(getProductById(productId), Product.class);
            fields.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(Product.class, key);
                field.setAccessible(true);
                ReflectionUtils.setField(field, existProduct, value);
            });

            Product productResult = productRepository.save(DTOUtil.map(existProduct, Product.class));
            productResponseDTO = DTOUtil.map(productResult, ProductDTO.ProductResponseDTO.class);
            log.debug("ProductService:updateProductById request parameters {}", ValueMapper.jsonAsString(fields));
        } catch (ProductException.ProductServiceBusinessException ex) {
            log.error("Exception occurred while persisting product to database, Exception message {}", ex.getMessage());
            throw new ProductException.ProductServiceBusinessException("Exception occurred while create a new product!");
        }

        log.info("ProductService::updateProductById execution ended...");
        return productResponseDTO;
    }


    //deleteProductById
    public void deleteProductById(Long productId) {
        try {
            log.info("ProductService::deleteProductById execution started...");

            productRepository.delete(DTOUtil.map(getProductById(productId), Product.class));
            log.debug("ProductService:deleteProductById deleting product from database for id {} ", productId);

        } catch (ProductException.ProductServiceBusinessException ex) {
            log.error("Exception occurred while deleting product {} from database, Exception message {}", productId, ex.getMessage());
            throw new ProductException.ProductServiceBusinessException("Exception occurred while delete product from Database " + productId);
        }

        log.info("ProductService::deleteProductById execution ended...");
    }
}
