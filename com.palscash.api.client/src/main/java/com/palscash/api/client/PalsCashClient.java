package com.palscash.api.client;

import java.math.BigDecimal;

import com.palscash.api.model.GetAccountResponse;
import com.palscash.api.model.GetBalanceResponse;
import com.palscash.api.model.GetFeeResponse;
import com.palscash.api.model.GetNewAccountResponse;
import com.palscash.api.model.PingResponse;
import com.palscash.api.model.RestoreAccountResponse;
import com.palscash.api.model.TransactionCountResponse;
import com.palscash.api.model.TransactionInfo;
import com.palscash.api.model.TransactionListResponse;
import com.palscash.api.model.TransferResponse;
import com.palscash.common.crypto.mnemonics.Mnemonics.Language;

public interface PalsCashClient {

	void setHost(String host);

	String getHost();

	/**
	 * Ping network
	 */
	PingResponse ping() throws Exception;

	/**
	 * Get current transaction fee from network
	 */
	GetFeeResponse getFee() throws Exception;

	/**
	 * Get new account private/public keys. The account doesn't exist on the network
	 * but its information can be used to send transfers to.
	 */
	GetNewAccountResponse getNewAccount(Language language) throws Exception;

	GetNewAccountResponse getNewAccount() throws Exception;

	/**
	 * Restore an account from a list of mnemonics
	 */
	RestoreAccountResponse restoreAccount(String[] mnemonics) throws Exception;

	/**
	 * Get account balance
	 */
	GetBalanceResponse getBalance(String address) throws Exception;

	/**
	 * Get account info
	 */
	GetAccountResponse getAccount(String address) throws Exception;

	/**
	 * Submit transfer request (synchronous)
	 */
	TransferResponse transfer(String privateKeyB58, String to, BigDecimal amount, String memo) throws Exception;

	/**
	 * Get transaction details by tx uuid
	 */
	TransactionInfo getTransactionDetails(String uuid) throws Exception;

	/**
	 * Get transactions count
	 */
	TransactionCountResponse getTransactionCount(String uuid) throws Exception;

	/**
	 * List transactions FROM account
	 */
	TransactionListResponse getTransactionsFromAccount(String uuid) throws Exception;

	/**
	 * List transactions TO account
	 */
	TransactionListResponse getTransactionsToAccount(String uuid) throws Exception;

}
