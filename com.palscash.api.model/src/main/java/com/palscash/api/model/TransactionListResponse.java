package com.palscash.api.model;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class TransactionListResponse extends BaseModelObject {

	private List<String> transactions = new ArrayList<>();

	public List<String> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<String> transactions) {
		this.transactions = transactions;
	}

	@Override
	public String getType() {
		return "transactions-list";
	}

}
