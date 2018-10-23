package com.palscash.wallet.database.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.palscash.wallet.database.domain.Account;
import com.palscash.wallet.database.domain.Transfer;

public interface TransferDao extends JpaRepository<Transfer, Long> {

	long countByAccount(Account acc);

	Page<Transfer> findAllByAccount(Account acc, Pageable page);

	Transfer findByUuid(String txUuid);


}
