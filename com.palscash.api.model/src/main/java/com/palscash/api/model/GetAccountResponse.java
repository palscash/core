package com.palscash.api.model;

import java.util.Date;

/**
 * Get account information
 */
@SuppressWarnings("serial")
public class GetAccountResponse extends BaseModelObject {

	private String uuid;

	private Date createDate;

	private Date lastUpdateDate;

	private String transactionChainHash;

	private String balance;

	private int height;

	public String getTransactionChainHash() {
		return transactionChainHash;
	}

	public void setTransactionChainHash(String transactionChainHash) {
		this.transactionChainHash = transactionChainHash;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GetAccountResponse [uuid=");
		builder.append(uuid);
		builder.append(", createDate=");
		builder.append(createDate);
		builder.append(", lastUpdateDate=");
		builder.append(lastUpdateDate);
		builder.append(", transactionChainHash=");
		builder.append(transactionChainHash);
		builder.append(", balance=");
		builder.append(balance);
		builder.append(", height=");
		builder.append(height);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		GetAccountResponse other = (GetAccountResponse) obj;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}

	@Override
	public String getType() {
		return "get-account-response";
	}

}
