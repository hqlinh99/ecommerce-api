package com.hqlinh.sachapi.account;

import com.hqlinh.sachapi.core.CustomException;
import com.hqlinh.sachapi.util.DTOUtil;
import jakarta.persistence.NoResultException;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.naming.NoPermissionException;
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

    @SneakyThrows
    public AccountDTO.AccountResponseDTO create(AccountDTO.AccountRequestDTO accountRequestDTO) {
        AccountDTO.AccountResponseDTO accountResponseDTO;
        try {
            log.info("AccountService::create execution started...");

            //CHECK EXISTED
            if (accountRepository.existsByUsername(accountRequestDTO.getUsername())) {
                throw new CustomException.DuplicatedException("Account already exists with username " + accountRequestDTO.getUsername());
            }
            if (accountRepository.existsByEmail(accountRequestDTO.getEmail())) {
                throw new CustomException.DuplicatedException("Account already exists with email " + accountRequestDTO.getEmail());
            }

            //EXECUTE
            accountRequestDTO.setPassword(passwordEncoder.encode(accountRequestDTO.getPassword()));
            Account account = DTOUtil.map(accountRequestDTO, Account.class);

            Account accountResult = accountRepository.save(account);
            accountResponseDTO = DTOUtil.map(accountResult, AccountDTO.AccountResponseDTO.class);
        } catch (AccountException.AccountServiceBusinessException ex) {
            log.error("Exception occurred while persisting account to database, Exception message {}", ex.getMessage());
            throw ex;
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
        } catch (AccountException.AccountServiceBusinessException ex) {
            log.error("Exception occurred while retrieving accounts from database , Exception message {}", ex.getMessage());
            throw ex;
        }

        log.info("AccountService::getAccounts execution ended...");
        return accountResponseDTOS;
    }

    public AccountDTO.AccountResponseDTO getAccountById(Long accountId) {
        AccountDTO.AccountResponseDTO accountResponseDTO;
        try {
            log.info("AccountService::getAccountById execution started...");

            Account account = accountRepository.findById(accountId).orElseThrow(() -> new NoResultException("Account not found with id " + accountId));
            accountResponseDTO = DTOUtil.map(account, AccountDTO.AccountResponseDTO.class);
        } catch (AccountException.AccountServiceBusinessException ex) {
            log.error("Exception occurred while retrieving account {} from database , Exception message {}", accountId, ex.getMessage());
            throw ex;
        }

        log.info("AccountService::getAccountById execution ended...");
        return accountResponseDTO;
    }

    public AccountDTO.AccountResponseDTO updateAccountById(Long accountId, Map<String, Object> fields) {
        AccountDTO.AccountResponseDTO accountResponseDTO;
        try {
            log.info("AccountService::updateAccountById execution started...");

            //CHECK ROLE
            Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (!account.getRole().equals(Role.ADMIN)) {
                //CHECK CURRENT ACCOUNT
                if (!account.getId().equals(accountId))
                    throw new AccessDeniedException("you dont have permission to update this account");

                //CHECK UPDATE ROLE
                if (fields.containsKey("role") && !account.getRole().equals(Role.valueOf(fields.get("role").toString())))
                    throw new AccessDeniedException("you dont have permission to update your role");

                //CHECK UPDATE PASSWORD
                if (fields.containsKey("password"))
                    throw new AccessDeniedException("you must be admin to update your password by this endpoint");
            }

            //CHECK EXISTED
            Account existAccount = accountRepository.findById(accountId).orElseThrow(() -> new NoResultException("Account not found with id " + accountId));

            //EXECUTE
            fields.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(Account.class, key);
                field.setAccessible(true);
                var newValue = key.equals("role") ? Role.valueOf(value.toString()) : key.equals("password") ? passwordEncoder.encode(value.toString()) : value;
                ReflectionUtils.setField(field, existAccount, newValue);
            });

            Account accountResult = accountRepository.save(existAccount);
            accountResponseDTO = DTOUtil.map(accountResult, AccountDTO.AccountResponseDTO.class);
        } catch (AccountException.AccountServiceBusinessException ex) {
            log.error("Exception occurred while persisting account to database, Exception message {}", ex.getMessage());
            throw ex;
        }

        log.info("AccountService::updateAccountById execution ended...");
        return accountResponseDTO;
    }

    public void deleteAccountById(Long accountId) {
        try {
            log.info("AccountService::deleteAccountById execution started...");

            //CHECK ROLE
            Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (!account.getRole().equals(Role.ADMIN))
                //CHECK CURRENT ACCOUNT
                if (!account.getId().equals(accountId))
                    throw new AccessDeniedException("you dont have permission to delete this account");

            //CHECK EXISTED
            Account existAccount = DTOUtil.map(getAccountById(accountId), Account.class);

            //EXECUTE
            accountRepository.delete(existAccount);
        } catch (AccountException.AccountServiceBusinessException ex) {
            log.error("Exception occurred while deleting account {} from database, Exception message {}", accountId, ex.getMessage());
            throw ex;
        }

        log.info("AccountService::deleteAccountById execution ended...");
    }

    @SneakyThrows
    public AccountDTO.AccountResponseDTO changePassword(Long accountId, AccountDTO.PasswordRequest passwordRequest) {
        AccountDTO.AccountResponseDTO accountResponseDTO;
        try {
            log.info("AccountService::updateAccountById execution started...");
            //CHECK EXISTED
            Account existAccount = accountRepository.findById(accountId).orElseThrow(() -> new NoResultException("Account not found with id " + accountId));

            //CHECK OLD PASSWORD
            if (!passwordEncoder.matches(passwordRequest.getPassword(), existAccount.getPassword()))
                throw new AccountException.PasswordNoMatchException("Incorrect old password request");

            //EXECUTE
            existAccount.setPassword(passwordEncoder.encode(passwordRequest.getNewPassword()));
            Account accountResult = accountRepository.save(existAccount);
            accountResponseDTO = DTOUtil.map(accountResult, AccountDTO.AccountResponseDTO.class);
        } catch (AccountException.AccountServiceBusinessException ex) {
            log.error("Exception occurred while persisting account to database, Exception message {}", ex.getMessage());
            throw ex;
        }

        log.info("AccountService::updateAccountById execution ended...");
        return accountResponseDTO;
    }
}
