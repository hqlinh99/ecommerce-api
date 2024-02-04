package com.hqlinh.sachapi.product;

import com.hqlinh.sachapi.util.DTOUtil;
import jakarta.persistence.NoResultException;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
            Product productResult = productRepository.save(product);
            productResponseDTO = DTOUtil.map(productResult, ProductDTO.ProductResponseDTO.class);
        } catch (ProductException.ProductServiceBusinessException ex) {
            log.error("Exception occurred while persisting product to database , Exception message {}", ex.getMessage());
            throw ex;
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
        } catch (ProductException.ProductServiceBusinessException ex) {
            log.error("Exception occurred while retrieving products from database , Exception message {}", ex.getMessage());
            throw ex;
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
        } catch (ProductException.ProductServiceBusinessException ex) {
            log.error("Exception occurred while retrieving product {} from database , Exception message {}", productId, ex.getMessage());
            throw ex;
        }

        log.info("ProductService::getProductById execution ended...");
        return productResponseDTO;
    }

    public ProductDTO.ProductResponseDTO updateProductById(Long productId, Map<String, Object> fields) {
        ProductDTO.ProductResponseDTO productResponseDTO;
        try {
            log.info("ProductService::updateProductById execution started...");

            //CHECK EXISTED
            Product existProduct = DTOUtil.map(getProductById(productId), Product.class);

            //EXECUTE
            fields.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(Product.class, key);
                field.setAccessible(true);
                ReflectionUtils.setField(field, existProduct, value);
            });

            Product productResult = productRepository.save(existProduct);
            productResponseDTO = DTOUtil.map(productResult, ProductDTO.ProductResponseDTO.class);
        } catch (ProductException.ProductServiceBusinessException ex) {
            log.error("Exception occurred while persisting product to database, Exception message {}", ex.getMessage());
            throw ex;
        }

        log.info("ProductService::updateProductById execution ended...");
        return productResponseDTO;
    }

    //deleteProductById
    public void deleteProductById(Long productId) {
        try {
            log.info("ProductService::deleteProductById execution started...");

            //CHECK EXIST
            Product existProduct = DTOUtil.map(getProductById(productId), Product.class);

            //EXECUTE
            productRepository.delete(existProduct);
        } catch (ProductException.ProductServiceBusinessException ex) {
            log.error("Exception occurred while deleting product {} from database, Exception message {}", productId, ex.getMessage());
            throw ex;
        }

        log.info("ProductService::deleteProductById execution ended...");
    }
}
