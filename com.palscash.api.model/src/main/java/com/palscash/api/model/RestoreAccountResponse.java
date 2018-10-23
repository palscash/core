package com.palscash.api.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * Get new account key pair
 */
@SuppressWarnings("serial")
public class RestoreAccountResponse extends BaseModelObject {

	private String privateKey = StringUtils.EMPTY;

	private String publicKey = StringUtils.EMPTY;

	private String accountUuid = StringUtils.EMPTY;

	private String curve = StringUtils.EMPTY;

	private List<String> mnemonics = new ArrayList<>();

	public RestoreAccountResponse() {
		super();
	}

	public String getAccountUuid() {
		return accountUuid;
	}

	public void setAccountUuid(String accountUuid) {
		this.accountUuid = accountUuid;
	}

	public List<String> getMnemonics() {
		return mnemonics;
	}

	public void setMnemonics(List<String> mnemonics) {
		this.mnemonics = mnemonics;
	}

	public String getCurve() {
		return curve;
	}

	public void setCurve(String curve) {
		this.curve = curve;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	@Override
	public String getType() {
		return "restore-account";
	}

	@Override
	public String toString() {
		return "GetNewAccountResponse [privateKey=" + privateKey + ", publicKey=" + publicKey + ", curve=" + curve + ", mnemonics=" + mnemonics + "]";
	}

}
