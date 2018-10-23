package com.palscash.wallet.ui.common.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class BooleanWrapper implements Serializable {

	private boolean value;

	public boolean isTrue() {
		return value;
	}

	public boolean isFalse() {
		return !isTrue();
	}

	public BooleanWrapper() {
		super();
	}

	public BooleanWrapper(boolean value) {
		super();
		this.value = value;
	}

	public void set(boolean value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "BooleanWrapper [value=" + value + "]";
	}

}
