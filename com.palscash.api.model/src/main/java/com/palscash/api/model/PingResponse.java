package com.palscash.api.model;

@SuppressWarnings("serial")
public class PingResponse extends BaseModelObject {

	public PingResponse() {
		super();
	}

	@Override
	public String getType() {
		return "ping";
	}

	@Override
	public String toString() {
		return "PingResponse [getType()=" + getType() + "]";
	}

}