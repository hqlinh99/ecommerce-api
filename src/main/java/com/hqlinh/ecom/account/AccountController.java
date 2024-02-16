package com.hqlinh.ecom.account;

import com.hqlinh.ecom.core.APIResponse;
import com.hqlinh.ecom.core.CustomException;
import com.hqlinh.ecom.util.ValidationUtil;
import com.hqlinh.ecom.util.ValueMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class AccountController {
    private final AccountService accountService;

    @PostMapping(value = "/account")
    public ResponseEntity<?> createNewAccount(@RequestBody AccountDTO.AccountRequestDTO accountRequestDTO) throws MethodArgumentNotValidException, CustomException.DuplicatedException {
        //Validate
        ValidationUtil.validate(accountRequestDTO, AccountDTO.class);

        log.info("AccountController::createNewAccount request body: {}", ValueMapper.jsonAsString(accountRequestDTO));
        AccountDTO.AccountResponseDTO accountResponseDTO = accountService.create(accountRequestDTO);
        APIResponse<?> response = APIResponse
                .builder()
                .status("SUCCESS")
                .result(accountResponseDTO)
                .build();
        log.info("AccountController::createNewAccount response: {}", ValueMapper.jsonAsString(response));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @PreAuthorize(value = "hasAnyAuthority(ADMIN.name())")
    @GetMapping(value = "/accounts")
    public ResponseEntity<?> getAccounts() {
        List<AccountDTO.AccountResponseDTO> accountResponseDTOS = accountService.getAccounts();
        APIResponse<?> response = APIResponse
                .builder()
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
        APIResponse<?> response = APIResponse
                .builder()
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
        APIResponse<?> response = APIResponse
                .builder()
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
    public ResponseEntity<?> changePassword(@PathVariable Long accountId, @RequestBody AccountDTO.PasswordRequest passwordRequest) throws MethodArgumentNotValidException, AccountException.PasswordNoMatchException {
        //Validate
        ValidationUtil.validate(passwordRequest, AccountDTO.class);

        log.info("AccountController::changePassword is {}", accountId);
        AccountDTO.AccountResponseDTO accountResponseDTO = accountService.changePassword(accountId, passwordRequest);
        APIResponse<?> response = APIResponse
                .builder()
                .status("SUCCESS")
                .result(accountResponseDTO)
                .build();
        log.info("AccountController::changePassword response: {}", ValueMapper.jsonAsString(response));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
