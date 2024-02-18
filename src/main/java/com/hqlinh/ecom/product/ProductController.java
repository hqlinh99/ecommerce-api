package com.hqlinh.ecom.product;

import com.hqlinh.ecom.core.APIResponse;
import com.hqlinh.ecom.util.ValidationUtil;
import com.hqlinh.ecom.util.ValueMapper;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final ProductService productService;
    private final Validator validator;

    @PostMapping(value = "/product")
    public ResponseEntity<?> createNewProduct(@RequestBody ProductDTO.ProductRequestDTO productRequestDTO) {
        //Validate
        ValidationUtil.validate(productRequestDTO, ProductDTO.class);

        log.info("ProductController::createNewProduct request body: {}", ValueMapper.jsonAsString(productRequestDTO));
        ProductDTO.ProductResponseDTO productResponseDTO = productService.create(productRequestDTO);
        APIResponse<?> response = APIResponse
                .builder()
                .status("SUCCESS")
                .result(productResponseDTO)
                .build();
        log.info("ProductController::createNewProduct response: {}", ValueMapper.jsonAsString(response));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping(value = "/products")
    public ResponseEntity<?> getProducts() {
        List<ProductDTO.ProductResponseDTO> productResponseDTOS = productService.getProducts();
        APIResponse<?> response = APIResponse
                .builder()
                .status("SUCCESS")
                .result(productResponseDTOS)
                .build();
        log.info("ProductController::getProducts response: {}", ValueMapper.jsonAsString(response));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/product/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable Long productId) {
        log.info("ProductController::getProductById is {}", productId);
        ProductDTO.ProductResponseDTO productResponseDTO = productService.getProductById(productId);
        APIResponse<?> response = APIResponse
                .builder()
                .status("SUCCESS")
                .result(productResponseDTO)
                .build();
        log.info("ProductController::getProductById response: {}", ValueMapper.jsonAsString(response));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PatchMapping(value = "/product/{productId}")
    public ResponseEntity<?> updateProductById(@PathVariable Long productId, @RequestBody Map<String, Object> fields) {
        //Validate
        ValidationUtil.validate(fields, ProductDTO.class);

        log.info("ProductController::updateProductById is {}", productId);
        ProductDTO.ProductResponseDTO productResponseDTO = productService.updateProductById(productId, fields);
        APIResponse<?> response = APIResponse
                .builder()
                .status("SUCCESS")
                .result(productResponseDTO)
                .build();
        log.info("ProductController::updateProductById response: {}", ValueMapper.jsonAsString(response));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping(value = "/product/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProductById(@PathVariable Long productId) {
        log.info("ProductController::deleteProductById is {}", productId);
        productService.deleteProductById(productId);
        log.info("ProductController::deleteProductById is ended successfully");
    }
}
