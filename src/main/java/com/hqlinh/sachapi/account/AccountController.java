package com.hqlinh.sachapi.account;

import com.hqlinh.sachapi.core.APIResponse;
import com.hqlinh.sachapi.product.ProductDTO;
import com.hqlinh.sachapi.util.DTOUtil;
import com.hqlinh.sachapi.util.ValidationUtil;
import com.hqlinh.sachapi.util.ValueMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class AccountController {
    private final AccountService accountService;

    @PostMapping(value = "/account")
    public ResponseEntity<?> createNewAccount(@RequestBody AccountDTO.AccountRequestDTO accountRequestDTO) throws MethodArgumentNotValidException {
        //Validate
        ValidationUtil.validate(accountRequestDTO, AccountDTO.class);

        log.info("AccountController::createNewAccount request body: {}", ValueMapper.jsonAsString(accountRequestDTO));
        AccountDTO.AccountResponseDTO accountResponseDTO = accountService.create(accountRequestDTO);
        APIResponse<AccountDTO.AccountResponseDTO> response = APIResponse
                .<AccountDTO.AccountResponseDTO>builder()
                .status("SUCCESS")
                .result(accountResponseDTO)
                .build();
        log.info("AccountController::createNewAccount response: {}", ValueMapper.jsonAsString(response));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping(value = "/accounts")
    public ResponseEntity<?> getAccounts() {
        List<AccountDTO.AccountResponseDTO> accountResponseDTOS = accountService.getAccounts();
        APIResponse<List<AccountDTO.AccountResponseDTO>> response = APIResponse
                .<List<AccountDTO.AccountResponseDTO>>builder()
                .status("SUCCESS")
                .result(accountResponseDTOS)
                .build();
        log.info("AccountController::getAccounts response: {}", ValueMapper.jsonAsString(response));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/account/{accountId}")
    public ResponseEntity<?> getAccountById(@PathVariable Long accountId) {
        log.info("AccountController::getAccountById is {}", accountId);
        AccountDTO.AccountResponseDTO accountResponseDTO = accountService.getAccountById(accountId);
        APIResponse<AccountDTO.AccountResponseDTO> response = APIResponse
                .<AccountDTO.AccountResponseDTO>builder()
                .status("SUCCESS")
                .result(accountResponseDTO)
                .build();
        log.info("AccountController::getAccountById response: {}", ValueMapper.jsonAsString(response));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping(value = "/account/{accountId}")
    public ResponseEntity<?> updateAccountById(@PathVariable Long accountId, @RequestBody Map<String, Object> fields) throws MethodArgumentNotValidException {
        //Validate
        ValidationUtil.validate(fields, AccountDTO.class);

        log.info("AccountController::updateAccountById is {}", accountId);
        AccountDTO.AccountResponseDTO accountResponseDTO = accountService.updateAccountById(accountId, fields);
        APIResponse<AccountDTO.AccountResponseDTO> response = APIResponse
                .<AccountDTO.AccountResponseDTO>builder()
                .status("SUCCESS")
                .result(accountResponseDTO)
                .build();
        log.info("AccountController::updateAccountById response: {}", ValueMapper.jsonAsString(response));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping(value = "/account/{accountId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccountById(@PathVariable Long accountId) {
        log.info("AccountController::deleteAccountById is {}", accountId);
        accountService.deleteAccountById(accountId);
        log.info("AccountController::deleteAccountById is ended successfully");
    }

    @PatchMapping(value = "/account/{accountId}/password")
    public ResponseEntity<?> changePassword(@PathVariable Long accountId, @RequestBody AccountDTO.PasswordRequest passwordRequest) throws MethodArgumentNotValidException {
        //Validate
        ValidationUtil.validate(passwordRequest, AccountDTO.class);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
