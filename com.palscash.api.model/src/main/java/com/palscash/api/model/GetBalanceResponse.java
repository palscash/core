package com.palscash.api.model;

import org.apache.commons.lang3.StringUtils;

/**
 * Get account balance by public key
 */
@SuppressWarnings("serial")
public class GetBalanceResponse extends BaseModelObject {

	private String amount = StringUtils.EMPTY;

	public GetBalanceResponse() {
		super();
	}

	public GetBalanceResponse(String amount) {
		super();
		this.amount = amount;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GetBalanceResponse [amount=");
		builder.append(amount);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public String getType() {
		return "get-balance-response";
	}

}