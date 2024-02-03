package com.hqlinh.sachapi.file;

import com.hqlinh.sachapi.core.APIResponse;
import com.hqlinh.sachapi.util.ValueMapper;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class FileUploadController {
    private final FileUploadService fileUploadService;
    private final Validator validator;

    @PostMapping(value = "/file-upload")
    public ResponseEntity<?> uploadSingleFile(@RequestParam("multipartFile") MultipartFile multipartFile) throws IOException {
        //Validate


        log.info("FileController::uploadSingleFile request body: {}", multipartFile.getName());
        FileUploadDTO.FileUploadResponseDTO fileResponseDTO = fileUploadService.uploadSingleFile(multipartFile);
        APIResponse<?> response = APIResponse
                .builder()
                .status("SUCCESS")
                .result(fileResponseDTO)
                .build();
        log.info("FileController::uploadSingleFile response: {}", ValueMapper.jsonAsString(response));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

//    @GetMapping(value = "/products")
//    public ResponseEntity<?> getProducts() {
//        List<FileDTO.ProductResponseDTO> productResponseDTOS = productService.getProducts();
//        APIResponse<List<FileDTO.ProductResponseDTO>> response = APIResponse
//                .<List<FileDTO.ProductResponseDTO>>builder()
//                .status("SUCCESS")
//                .result(productResponseDTOS)
//                .build();
//        log.info("ProductController::getProducts response: {}", ValueMapper.jsonAsString(response));
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//
//    @GetMapping(value = "/product/{productId}")
//    public ResponseEntity<?> getProductById(@PathVariable Long productId) {
//        log.info("ProductController::getProductById is {}", productId);
//        FileDTO.ProductResponseDTO productResponseDTO = productService.getProductById(productId);
//        APIResponse<FileDTO.ProductResponseDTO> response = APIResponse
//                .<FileDTO.ProductResponseDTO>builder()
//                .status("SUCCESS")
//                .result(productResponseDTO)
//                .build();
//        log.info("ProductController::getProductById response: {}", ValueMapper.jsonAsString(response));
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//
//    @PatchMapping(value = "/product/{productId}")
//    public ResponseEntity<?> updateProductById(@PathVariable Long productId, @RequestBody Map<String, Object> fields) throws MethodArgumentNotValidException {
//        //Validate
//        ValidationUtil.validate(fields, FileDTO.class);
//
//        log.info("ProductController::updateProductById is {}", productId);
//        FileDTO.ProductResponseDTO productResponseDTO = productService.updateProductById(productId, fields);
//        APIResponse<FileDTO.ProductResponseDTO> response = APIResponse
//                .<FileDTO.ProductResponseDTO>builder()
//                .status("SUCCESS")
//                .result(productResponseDTO)
//                .build();
//        log.info("ProductController::updateProductById response: {}", ValueMapper.jsonAsString(response));
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//
//    @DeleteMapping(value = "/product/{productId}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void deleteProductById(@PathVariable Long productId) {
//        log.info("ProductController::deleteProductById is {}", productId);
//        productService.deleteProductById(productId);
//        log.info("ProductController::deleteProductById is ended successfully");
//    }
}
