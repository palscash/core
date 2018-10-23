package com.palscash.wallet.database.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.palscash.wallet.database.domain.Account;

public interface AccountDao extends JpaRepository<Account, Integer> {

	Account findByPrivateKey(String privateKey);

	@Modifying
	@Query("update Account acc set acc.balance = :balance where acc.id = :id")
	int updateAccountBalance(@Param("balance") String balance, @Param("id") int id);

}
