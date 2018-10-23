package com.palscash.api.model;

import java.math.BigDecimal;
import java.util.Date;

@SuppressWarnings("serial")
public class TransactionInfo extends BaseModelObject {

	public static final String TX_PREFIX = "tx";

	private String transactionUuid;

	private String fromAddress;

	private String toAddress;

	private BigDecimal amount;

	private String memo;

	private String fee;

	private Date date;

	public String getTransactionUuid() {
		return transactionUuid;
	}

	public void setTransactionUuid(String transactionUuid) {
		this.transactionUuid = transactionUuid;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public String getToAddress() {
		return toAddress;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TransactionDetails [transactionUuid=");
		builder.append(transactionUuid);
		builder.append(", fromAddress=");
		builder.append(fromAddress);
		builder.append(", toAddress=");
		builder.append(toAddress);
		builder.append(", amount=");
		builder.append(amount);
		builder.append(", memo=");
		builder.append(memo);
		builder.append(", fee=");
		builder.append(fee);
		builder.append(", date=");
		builder.append(date);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((transactionUuid == null) ? 0 : transactionUuid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TransactionInfo other = (TransactionInfo) obj;
		if (transactionUuid == null) {
			if (other.transactionUuid != null)
				return false;
		} else if (!transactionUuid.equals(other.transactionUuid))
			return false;
		return true;
	}

	@Override
	public String getType() {
		return "transaction-lookup";
	}

}
