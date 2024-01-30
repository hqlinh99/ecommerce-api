package com.hqlinh.sachapi.account;

import com.hqlinh.sachapi.core.APIResponse;
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

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class AccountController {
    private final AccountService accountService;
    private final Validator validator;

    @PostMapping(value = "/account")
    public ResponseEntity<?> createNewAccount(@RequestBody @Valid AccountDTO.AccountRequestDTO accountRequestDTO) {
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
        Set<Set<ConstraintViolation<AccountDTO.AccountRequestDTO>>> constraintsSet = new HashSet<>();
        fields.forEach((String key, Object value) -> constraintsSet.add(validator.validateValue(AccountDTO.AccountRequestDTO.class, key, value)));
        if (!constraintsSet.isEmpty()) {
            BindingResult bindingResult = new BeanPropertyBindingResult(fields, "Map<String, Object>");
            constraintsSet.forEach(constraints -> {
                constraints.forEach(violation -> {
                    bindingResult.addError(new FieldError(
                            "Map<String, Object>",
                            violation.getPropertyPath().toString(),
                            violation.getMessage()
                    ));
                });
            });
            if (bindingResult.hasErrors())
                throw new MethodArgumentNotValidException(null, bindingResult);
        }

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
}
