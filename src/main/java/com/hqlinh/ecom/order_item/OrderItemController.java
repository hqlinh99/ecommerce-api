package com.hqlinh.ecom.order_item;

import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class OrderItemController {
    private final OrderItemService orderItemService;
    private final Validator validator;

//    @PostMapping(value = "/file-upload")
//    public ResponseEntity<?> uploadSingleFile(@RequestParam("multipartFile") MultipartFile multipartFile) throws IOException {
//        log.info("FileController::uploadSingleFile request body: {}", multipartFile.getName());
//        OrderItemDTO.FileUploadResponseDTO fileResponseDTO = fileUploadService.uploadSingleFile(multipartFile);
//        APIResponse<?> response = APIResponse
//                .builder()
//                .status("SUCCESS")
//                .result(fileResponseDTO)
//                .build();
//        log.info("FileController::uploadSingleFile response: {}", ValueMapper.jsonAsString(response));
//        return new ResponseEntity<>(response, HttpStatus.CREATED);
//    }
//
//    @GetMapping(value = "/file-uploads")
//    public ResponseEntity<?> getFileUploads() {
//        List<OrderItemDTO.FileUploadResponseDTO> fileUploadResponseDTOS = fileUploadService.getFileUploads();
//        APIResponse<?> response = APIResponse
//                .builder()
//                .status("SUCCESS")
//                .result(fileUploadResponseDTOS)
//                .build();
//        log.info("FileUploadController::getFileUploads response: {}", ValueMapper.jsonAsString(response));
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//
//    @GetMapping(value = "/file-upload/{fileUploadId}")
//    public ResponseEntity<?> getFileUploadById(@PathVariable Long fileUploadId) {
//        log.info("FileUploadController::getFileUploadById is {}", fileUploadId);
//        OrderItemDTO.FileUploadResponseDTO productResponseDTO = fileUploadService.getFileUploadById(fileUploadId);
//        APIResponse<?> response = APIResponse
//                .builder()
//                .status("SUCCESS")
//                .result(productResponseDTO)
//                .build();
//        log.info("FileUploadController::getFileUploadById response: {}", ValueMapper.jsonAsString(response));
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
////
//    @PatchMapping(value = "/file-upload/{fileUploadId}")
//    public ResponseEntity<?> updateNameFileUploadById(@PathVariable Long fileUploadId, @RequestBody OrderItemDTO.NameFileUploadRequest fileName) throws MethodArgumentNotValidException {
//        //Validate
//        ValidationUtil.validate(fileName, OrderItemDTO.class);
//
//        log.info("FileUploadController::updateNameFileUploadById is {}", fileUploadId);
//        OrderItemDTO.FileUploadResponseDTO productResponseDTO = fileUploadService.updateNameFileUploadById(fileUploadId, fileName);
//        APIResponse<?> response = APIResponse
//                .builder()
//                .status("SUCCESS")
//                .result(productResponseDTO)
//                .build();
//        log.info("FileUploadController::updateNameFileUploadById response: {}", ValueMapper.jsonAsString(response));
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//    @DeleteMapping(value = "/file-upload/{fileUploadId}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void deleteFileUploadById(@PathVariable Long fileUploadId) {
//        log.info("FileUploadController::deleteFileUploadById is {}", fileUploadId);
//        fileUploadService.deleteFileUploadById(fileUploadId);
//        log.info("FileUploadController::deleteFileUploadById is ended successfully");
//    }
}
