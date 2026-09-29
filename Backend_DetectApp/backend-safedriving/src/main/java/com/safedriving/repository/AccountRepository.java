package com.safedriving.repository;

import com.safedriving.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

    Optional<Account> findByUsername(String username);

    Optional<Account> findByUsernameAndIsDeletedFalse(String username);

    boolean existsByUsername(String username);

    boolean existsByUsernameAndIsDeletedFalse(String username);
}
