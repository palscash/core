package com.palscash.api.model;

import java.nio.charset.StandardCharsets;

@SuppressWarnings("serial")
public class TransferResponse extends BaseModelObject {

	public static final String ERROR_EXPIRED = "EXPIRED";
	public static final String ERROR_NOT_CONFIRMED = "NOT_CONFIRMER";
	public static final String ERROR_INVALID_TO_ACCOUNT = "INVALID_TO_ACCOUNT";
	public static final String ERROR_INVALID_FROM_ACCOUNT = "INVALID_FROM_ACCOUNT";
	public static final String ERROR_INVALID_AMOUNT = "INVALID_AMOUNT";
	public static final String ERROR_LOCKED_ACCOUNTS = "LOCKED_ACCOUNTS";
	public static final String ERROR_LOCKED_FROM_ACCOUNT = "LOCKED_FROM_ACCOUNT";
	public static final String ERROR_LOCKED_TO_ACCOUNT = "LOCKED_TO_ACCOUNT";
	public static final String ERROR_UNAUTHORIZED = "UNAUTHORIZED";
	public static final String ERROR_INSUFFICIENT_FUNDS = "INSUFFICIENT_FUNDS";
	public static final String ERROR_SYSTEM = "SYSTEM_ERROR";
	public static final String ERROR_TX_REPLAY = "TX_SUBMISSION_ERROR_4000";
	public static final String ERROR_TX_SIGNATURE_UNAUTHORIZED = "TX_SUBMISSION_ERROR_5000";

	public static final String ERROR_REJECTED = "REJECTED";

	private String error;

	private String transactionUuid;

	public static TransferResponse error(String msg) {
		TransferResponse resp = new TransferResponse();
		resp.setError(msg);
		return resp;
	}

	public byte[] toHashData() {
		return (transactionUuid + error).getBytes(StandardCharsets.UTF_8);
	}

	public String getTransactionUuid() {
		return transactionUuid;
	}

	public void setTransactionUuid(String uuid) {
		this.transactionUuid = uuid;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TransferResponse [error=");
		builder.append(error);
		builder.append(", transactionUuid=");
		builder.append(transactionUuid);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public String getType() {
		return "submit-transaction";
	}

}