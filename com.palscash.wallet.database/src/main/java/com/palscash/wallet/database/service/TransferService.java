package com.palscash.wallet.database.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.palscash.wallet.database.dao.TransferDao;
import com.palscash.wallet.database.domain.Account;
import com.palscash.wallet.database.domain.Transfer;

@Service
public class TransferService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TransferDao dao;

	@Transactional(readOnly = false)
	public void save(Transfer t) {
		log.debug("Saving: " + t);
		dao.save(t);
		log.debug("Saved: " + t);
	}

	@Transactional(readOnly = true)
	public long getCount(Account acc) {
		long count = dao.countByAccount(acc);
		log.debug("Counted transfers: " + count);
		return count;
	}

	@Transactional(readOnly = true)
	public Page<Transfer> getAllByAccount(Account acc, int page, int size) {
		return dao.findAllByAccount(acc, PageRequest.of(page, size));
	}

	@Transactional(readOnly = true)
	public void delete(Transfer t) {
		dao.delete(t);
		log.debug("Deleted: " + t);
	}

	@Transactional(readOnly = true)
	public Optional<Transfer> findById(long id) {
		return this.dao.findById(id);
	}

	@Transactional(readOnly = true)
	public Transfer getByUuid(String txUuid) {
		return dao.findByUuid(txUuid);
	}

}
