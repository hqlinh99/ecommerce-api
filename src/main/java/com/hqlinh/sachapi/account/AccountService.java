package com.hqlinh.sachapi.account;

import com.hqlinh.sachapi.util.DTOUtil;
import com.hqlinh.sachapi.util.ValueMapper;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class AccountService {
    private IAccountRepository accountRepository;
    private PasswordEncoder passwordEncoder;

    public AccountDTO.AccountResponseDTO create(AccountDTO.AccountRequestDTO accountRequestDTO) {
        AccountDTO.AccountResponseDTO accountResponseDTO;
        try {
            log.info("AccountService::create execution started...");

            accountRequestDTO.setPassword(passwordEncoder.encode(accountRequestDTO.getPassword()));
            Account account = DTOUtil.map(accountRequestDTO, Account.class);
            log.debug("AccountService:create request parameters {}", ValueMapper.jsonAsString(accountRequestDTO));

            Account accountResult = accountRepository.save(account);
            accountResponseDTO = DTOUtil.map(accountResult, AccountDTO.AccountResponseDTO.class);
            log.debug("AccountService:create request parameters {}", ValueMapper.jsonAsString(accountRequestDTO));
        } catch (AccountException.AccountServiceBusinessException ex) {
            log.error("Exception occurred while persisting account to database , Exception message {}", ex.getMessage());
            throw new AccountException.AccountServiceBusinessException("Exception occurred while create a new account!");
        }

        log.info("AccountService::create execution ended...");
        return accountResponseDTO;
    }

    public List<AccountDTO.AccountResponseDTO> getAccounts() {
        List<AccountDTO.AccountResponseDTO> accountResponseDTOS;
        try {
            log.info("AccountService::getAccounts execution started...");

            List<Account> accountList = accountRepository.findAll();
            accountResponseDTOS = accountList.isEmpty() ? Collections.emptyList() : DTOUtil.mapList(accountList, AccountDTO.AccountResponseDTO.class);
            log.debug("AccountService:getAccounts retrieving accounts from database  {}", ValueMapper.jsonAsString(accountResponseDTOS));
        } catch (AccountException.AccountServiceBusinessException ex) {
            log.error("Exception occurred while retrieving accounts from database , Exception message {}", ex.getMessage());
            throw new AccountException.AccountServiceBusinessException("Exception occurred while fetch accounts from Database");
        }

        log.info("AccountService::getAccountById execution ended...");
        return accountResponseDTOS;
    }

    public AccountDTO.AccountResponseDTO getAccountById(Long accountId) {
        AccountDTO.AccountResponseDTO accountResponseDTO;
        try {
            log.info("AccountService::getAccountById execution started...");

            Account account = accountRepository.findById(accountId).orElseThrow(() -> new AccountException.AccountNotFoundException("Account not found with id " + accountId));
            accountResponseDTO = DTOUtil.map(account, AccountDTO.AccountResponseDTO.class);
            log.debug("AccountService:getAccountById retrieving account from database for id {} {}", accountId, ValueMapper.jsonAsString(accountResponseDTO));
        } catch (AccountException.AccountServiceBusinessException ex) {
            log.error("Exception occurred while retrieving account {} from database , Exception message {}", accountId, ex.getMessage());
            throw new AccountException.AccountServiceBusinessException("Exception occurred while fetch account from Database " + accountId);
        }

        log.info("AccountService::getAccountById execution ended...");
        return accountResponseDTO;
    }

    public AccountDTO.AccountResponseDTO updateAccountById(Long accountId, Map<String, Object> fields) {
        AccountDTO.AccountResponseDTO accountResponseDTO;
        try {
            log.info("AccountService::updateAccountById execution started...");

            Account existAccount = DTOUtil.map(getAccountById(accountId), Account.class);
            fields.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(Account.class, key);
                field.setAccessible(true);
                ReflectionUtils.setField(field, existAccount, value);
            });

            Account accountResult = accountRepository.save(DTOUtil.map(existAccount, Account.class));
            accountResponseDTO = DTOUtil.map(accountResult, AccountDTO.AccountResponseDTO.class);
            log.debug("AccountService:updateAccountById request parameters {}", ValueMapper.jsonAsString(fields));
        } catch (AccountException.AccountServiceBusinessException ex) {
            log.error("Exception occurred while persisting account to database, Exception message {}", ex.getMessage());
            throw new AccountException.AccountServiceBusinessException("Exception occurred while create a new account!");
        }

        log.info("AccountService::updateAccountById execution ended...");
        return accountResponseDTO;
    }

    public void deleteAccountById(Long accountId) {
        try {
            log.info("AccountService::deleteAccountById execution started...");

            accountRepository.delete(DTOUtil.map(getAccountById(accountId), Account.class));
            log.debug("AccountService:deleteAccountById deleting account from database for id {} ", accountId);

        } catch (AccountException.AccountServiceBusinessException ex) {
            log.error("Exception occurred while deleting account {} from database, Exception message {}", accountId, ex.getMessage());
            throw new AccountException.AccountServiceBusinessException("Exception occurred while delete account from Database " + accountId);
        }

        log.info("AccountService::deleteAccountById execution ended...");
    }
}
