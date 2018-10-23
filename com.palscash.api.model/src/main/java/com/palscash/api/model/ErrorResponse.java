package com.palscash.api.model;

@SuppressWarnings("serial")
public class ErrorResponse extends BaseModelObject {

	private String error;

	public ErrorResponse() {
		super();
	}

	public ErrorResponse(String error) {
		super();
		this.error = error;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	@Override
	public String getType() {
		return "error-response";
	}

}
