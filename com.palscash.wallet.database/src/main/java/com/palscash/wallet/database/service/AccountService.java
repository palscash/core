package com.palscash.wallet.database.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.palscash.wallet.database.dao.AccountDao;
import com.palscash.wallet.database.domain.Account;

@Service
public class AccountService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AccountDao dao;

	@Transactional(readOnly = false)
	public Account save(Account acc) {
		log.debug("Saving: " + acc);
		return dao.save(acc);
	}

	@Transactional(readOnly = true)
	public long getCount() {
		long count = dao.count();
		log.debug("Counted accounts: " + count);
		return count;
	}

	@Transactional(readOnly = true)
	public Page<Account> getAll(int page, int size) {
		return dao.findAll(PageRequest.of(page, size));
	}

	@Transactional(readOnly = true)
	public List<Account> getAll() {
		return dao.findAll();
	}

	@Transactional(readOnly = true)
	public void delete(Account acc) {
		dao.delete(acc);
		log.debug("Deleted: " + acc);
	}

	@Transactional(readOnly = true)
	public Optional<Account> findById(int id) {
		return this.dao.findById(id);
	}

	@Transactional(readOnly = false)
	public Account updateAccountBalance(Account acc, String amount) {
		dao.updateAccountBalance(amount, acc.getId());
		return this.findById(acc.getId()).get();
	}

}
