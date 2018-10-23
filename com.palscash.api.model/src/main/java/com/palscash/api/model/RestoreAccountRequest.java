package com.palscash.api.model;

import java.io.Serializable;

import com.palscash.common.crypto.mnemonics.Mnemonics.Language;

/**
 * Get new account key pair
 */
@SuppressWarnings("serial")
public class RestoreAccountRequest implements Serializable {

	private String language = Language.ENGLISH.name();

	private String[] mnemonics;

	private String curve;

	public RestoreAccountRequest() {
		super();
	}

	public RestoreAccountRequest(Language language, String[] mnemonics, String curve) {
		super();
		this.language = language.name();
		this.mnemonics = mnemonics;
		this.curve = curve;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String[] getMnemonics() {
		return mnemonics;
	}

	public void setMnemonics(String[] mnemonics) {
		this.mnemonics = mnemonics;
	}

	public String getCurve() {
		return curve;
	}

	public void setCurve(String curve) {
		this.curve = curve;
	}

	@Override
	public String toString() {
		return "RestoreAccountRequest [language=" + language + ", curve=" + curve + "]";
	}

}
