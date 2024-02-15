package com.hqlinh.sachapi.file;

import com.hqlinh.sachapi.core.APIResponse;
import com.hqlinh.sachapi.util.ValidationUtil;
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

import java.io.File;
import java.io.IOException;
import java.util.List;
@CrossOrigin
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
    @PatchMapping(value = "/file-upload/{fileUploadId}")
    public ResponseEntity<?> updateNameFileUploadById(@PathVariable Long fileUploadId, @RequestBody FileUploadDTO.NameFileUploadRequest fileName) throws MethodArgumentNotValidException {
        //Validate
        ValidationUtil.validate(fileName, FileUploadDTO.class);

        log.info("FileUploadController::updateNameFileUploadById is {}", fileUploadId);
        FileUploadDTO.FileUploadResponseDTO productResponseDTO = fileUploadService.updateNameFileUploadById(fileUploadId, fileName);
        APIResponse<?> response = APIResponse
                .builder()
                .status("SUCCESS")
                .result(productResponseDTO)
                .build();
        log.info("FileUploadController::updateNameFileUploadById response: {}", ValueMapper.jsonAsString(response));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @DeleteMapping(value = "/file-upload/{fileUploadId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFileUploadById(@PathVariable Long fileUploadId) {
        log.info("FileUploadController::deleteFileUploadById is {}", fileUploadId);
        fileUploadService.deleteFileUploadById(fileUploadId);
        log.info("FileUploadController::deleteFileUploadById is ended successfully");
    }
}
