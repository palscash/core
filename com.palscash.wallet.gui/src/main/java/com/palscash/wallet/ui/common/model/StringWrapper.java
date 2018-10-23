package com.palscash.wallet.ui.common.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class StringWrapper implements Serializable {

	private String value;

	public String get() {
		return value;
	}

	public StringWrapper() {
		super();
	}

	public StringWrapper(String value) {
		super();
		this.value = value;
	}

	public void set(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "StringWrapper [value=" + value + "]";
	}

}
