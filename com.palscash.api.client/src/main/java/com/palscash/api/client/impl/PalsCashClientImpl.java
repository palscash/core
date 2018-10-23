package com.palscash.api.client.impl;

import java.math.BigDecimal;
import java.net.URI;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.palscash.api.Urls;
import com.palscash.api.client.PalsCashClient;
import com.palscash.api.model.GetAccountResponse;
import com.palscash.api.model.GetBalanceResponse;
import com.palscash.api.model.GetFeeResponse;
import com.palscash.api.model.GetNewAccountRequest;
import com.palscash.api.model.GetNewAccountResponse;
import com.palscash.api.model.PingResponse;
import com.palscash.api.model.RestoreAccountRequest;
import com.palscash.api.model.RestoreAccountResponse;
import com.palscash.api.model.TransactionCountResponse;
import com.palscash.api.model.TransactionInfo;
import com.palscash.api.model.TransactionListResponse;
import com.palscash.api.model.TransferRequest;
import com.palscash.api.model.TransferResponse;
import com.palscash.common.crypto.Base58;
import com.palscash.common.crypto.Curves;
import com.palscash.common.crypto.PalsCashKeyPair;
import com.palscash.common.crypto.mnemonics.Mnemonics.Language;

@Component
public class PalsCashClientImpl implements PalsCashClient {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private RestTemplate restTemplate;

	private String host;

	@PostConstruct
	public void init() {
		restTemplate = new RestTemplate();
	}

	@Override
	public PingResponse ping() throws Exception {
		URI uri = new URI(host + Urls.URL_PING);
		log.trace("Ping: " + uri);
		return this.restTemplate.getForObject(uri, PingResponse.class);

	}

	@Override
	public GetFeeResponse getFee() throws Exception {
		URI uri = new URI(host + Urls.URL_GET_TX_FEE);
		log.trace("Get fee: " + uri);
		return restTemplate.getForObject(uri, GetFeeResponse.class);

	}

	@Override
	public GetNewAccountResponse getNewAccount(Language language) throws Exception {
		final URI uri = new URI(host + Urls.URL_GET_NEW_ACCOUNT);
		return restTemplate.postForObject(uri, new GetNewAccountRequest(language.name()), GetNewAccountResponse.class);
	}

	@Override
	public GetNewAccountResponse getNewAccount() throws Exception {
		final URI uri = new URI(host + Urls.URL_GET_NEW_ACCOUNT);
		return restTemplate.getForObject(uri, GetNewAccountResponse.class);

	}

	@Override
	public GetBalanceResponse getBalance(String address) throws Exception {

		final URI uri = new URI(host + StringUtils.replace(Urls.URL_GET_BALANCE, "{address}", address));

		log.debug("getBalance: " + uri);

		final GetBalanceResponse response = restTemplate.getForObject(uri, GetBalanceResponse.class);

		log.debug("gotBalance: " + response);

		return response;

	}

	@Override
	public TransferResponse transfer(String privateKeyB58, String to, BigDecimal amount, String memo) throws Exception {

		final URI uri = new URI(host + Urls.URL_TRANSFER);

		PalsCashKeyPair kp = PalsCashKeyPair.createFromPrivateKeyBase58(privateKeyB58, Curves.DEFAULT_CURVE);

		log.debug("transfer: " + uri);

		TransferRequest req = new TransferRequest();

		req.setAmount(amount.toPlainString());
		req.setFrom(kp.getPalsCashAccount().getUuid());
		req.setMemo(memo);
		req.setRequestUuid(UUID.randomUUID().toString());
		req.setPublicKey(kp.getPublicKeyAsBase58());
		req.setTo(to);

		req.setSignature(Base58.encode(kp.sign(req.getUniqueTransactionUuid())));
		
		System.out.println(new ObjectMapper().writeValueAsString(req));

		return restTemplate.postForObject(uri, req, TransferResponse.class);

	}

	@Override
	public TransactionInfo getTransactionDetails(String uuid) throws Exception {

		final URI uri = new URI(host + StringUtils.replace(Urls.URL_GET_TX_DETAILS, "{uuid}", uuid));

		log.debug("getTransactionDetails: " + uri);

		final TransactionInfo response = restTemplate.getForObject(uri, TransactionInfo.class);

		return response;

	}

	@Override
	public GetAccountResponse getAccount(String address) throws Exception {

		final URI uri = new URI(host + StringUtils.replace(Urls.URL_GET_ACCOUNT, "{address}", address));

		log.debug("getAccount: " + uri);

		final GetAccountResponse response = restTemplate.getForObject(uri, GetAccountResponse.class);

		return response;

	}

	@Override
	public TransactionCountResponse getTransactionCount(String address) throws Exception {

		final URI uri = new URI(host + StringUtils.replace(Urls.URL_GET_TX_COUNT, "{uuid}", address));

		log.debug("getTransactionCount: " + uri);

		final TransactionCountResponse response = restTemplate.getForObject(uri, TransactionCountResponse.class);

		return response;

	}

	@Override
	public TransactionListResponse getTransactionsFromAccount(String address) throws Exception {

		final URI uri = new URI(host + StringUtils.replace(Urls.URL_LIST_TX_FROM, "{uuid}", address));

		log.debug("getTransactionsFromAccount: " + uri);

		final TransactionListResponse response = restTemplate.getForObject(uri, TransactionListResponse.class);

		return response;

	}

	@Override
	public TransactionListResponse getTransactionsToAccount(String address) throws Exception {

		final URI uri = new URI(host + StringUtils.replace(Urls.URL_LIST_TX_TO, "{uuid}", address));

		log.debug("getTransactionsToAccount: " + uri);

		final TransactionListResponse response = restTemplate.getForObject(uri, TransactionListResponse.class);

		return response;

	}

	@Override
	public RestoreAccountResponse restoreAccount(String[] mnemonics) throws Exception {

		final URI uri = new URI(host + Urls.URL_GET_RESTORE_ACCOUNT);

		restTemplate.postForObject(uri, new RestoreAccountRequest(Language.ENGLISH, mnemonics, Curves.DEFAULT_CURVE),
				RestoreAccountResponse.class);

		return null;
	}

	@Override
	public void setHost(String host) {
		this.host = host;
	}

	@Override
	public String getHost() {
		return host;
	}

}
