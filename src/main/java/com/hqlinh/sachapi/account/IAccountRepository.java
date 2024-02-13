package com.hqlinh.sachapi.account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IAccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);
    Optional<Account> findByProviderId(String providerId);
    boolean existsByEmail(String email);
    boolean existsByProviderId(String providerId);

}
