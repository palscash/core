package com.palscash.api.model;

import org.apache.commons.lang3.StringUtils;

/**
 * Current network fee
 */
@SuppressWarnings("serial")
public class GetFeeResponse extends BaseModelObject {

	private String fee = StringUtils.EMPTY;

	public GetFeeResponse() {
		super();
	}

	public GetFeeResponse(String v) {
		this();
		this.fee = v;
	}

	@Override
	public String getType() {
		return "get-fee";
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getFee() {
		return fee;
	}

	@Override
	public String toString() {
		return "GetFeeResponse [getType()=" + getType() + ", getFee()=" + getFee() + "]";
	}

}