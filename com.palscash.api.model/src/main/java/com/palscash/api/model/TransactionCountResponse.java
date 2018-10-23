package com.palscash.api.model;

@SuppressWarnings("serial")
public class TransactionCountResponse extends BaseModelObject {

	private long count;

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	@Override
	public String getType() {
		return "transactions-count";
	}

}
