package com.palscash.api.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

@SuppressWarnings("serial")
public class TransferRequest implements Serializable {

	private String from;

	private String to;

	private String amount;

	private String memo;

	private String requestUuid;

	private String signature;

	private String publicKey;

	private long time = System.currentTimeMillis();

	public String getUniqueTransactionUuid() {
		return getFrom() + getAmount() + getTo() + getRequestUuid() + time;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getAmount() {
		return amount;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getRequestUuid() {

		if (StringUtils.isBlank(requestUuid)) {
			requestUuid = UUID.randomUUID().toString();
		}

		return requestUuid;
	}

	public void setRequestUuid(String requestUuid) {
		this.requestUuid = requestUuid;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public BigDecimal getAmountAsBigDecimal() {
		BigDecimal bd = new BigDecimal(getAmount());
		bd = bd.setScale(8, RoundingMode.CEILING);
		return bd;
	}

	@Override
	public String toString() {
		return "TransferRequest [from=" + from + ", to=" + to + ", amount=" + amount + ", memo=" + memo + ", requestUuid=" + requestUuid + ", signature=" + signature + ", publicKey=" + publicKey + ", time=" + time + "]";
	}

}
