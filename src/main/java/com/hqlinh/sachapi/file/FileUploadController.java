package com.hqlinh.sachapi.file;

import com.hqlinh.sachapi.core.APIResponse;
import com.hqlinh.sachapi.util.ValueMapper;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class FileUploadController {
    private final FileUploadService fileUploadService;
    private final Validator validator;

    @PostMapping(value = "/file-upload")
    public ResponseEntity<?> uploadSingleFile(@RequestParam("multipartFile") MultipartFile multipartFile) throws IOException {
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

    @GetMapping(value = "/file-uploads")
    public ResponseEntity<?> getFileUploads() {
        List<FileUploadDTO.FileUploadResponseDTO> fileUploadResponseDTOS = fileUploadService.getFileUploads();
        APIResponse<?> response = APIResponse
                .builder()
                .status("SUCCESS")
                .result(fileUploadResponseDTOS)
                .build();
        log.info("FileUploadController::getFileUploads response: {}", ValueMapper.jsonAsString(response));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/file-upload/{fileUploadId}")
    public ResponseEntity<?> getFileUploadById(@PathVariable Long fileUploadId) {
        log.info("FileUploadController::getFileUploadById is {}", fileUploadId);
        FileUploadDTO.FileUploadResponseDTO productResponseDTO = fileUploadService.getFileUploadById(fileUploadId);
        APIResponse<?> response = APIResponse
                .builder()
                .status("SUCCESS")
                .result(productResponseDTO)
                .build();
        log.info("FileUploadController::getFileUploadById response: {}", ValueMapper.jsonAsString(response));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
//
//    @PatchMapping(value = "/product/{productId}")
//    public ResponseEntity<?> updateFileUploadById(@PathVariable Long productId, @RequestBody Map<String, Object> fields) throws MethodArgumentNotValidException {
//        //Validate
//        ValidationUtil.validate(fields, FileDTO.class);
//
//        log.info("FileUploadController::updateFileUploadById is {}", productId);
//        FileDTO.FileUploadResponseDTO productResponseDTO = productService.updateFileUploadById(productId, fields);
//        APIResponse<FileDTO.FileUploadResponseDTO> response = APIResponse
//                .<FileDTO.FileUploadResponseDTO>builder()
//                .status("SUCCESS")
//                .result(productResponseDTO)
//                .build();
//        log.info("FileUploadController::updateFileUploadById response: {}", ValueMapper.jsonAsString(response));
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//
//    @DeleteMapping(value = "/product/{productId}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void deleteFileUploadById(@PathVariable Long productId) {
//        log.info("FileUploadController::deleteFileUploadById is {}", productId);
//        productService.deleteFileUploadById(productId);
//        log.info("FileUploadController::deleteFileUploadById is ended successfully");
//    }
}
