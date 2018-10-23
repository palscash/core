package com.palscash.api.model;

import java.io.Serializable;

import com.palscash.common.crypto.mnemonics.Mnemonics.Language;

/**
 * Get new account key pair
 */
@SuppressWarnings("serial")
public class GetNewAccountRequest implements Serializable {

	private String language = Language.ENGLISH.name();

	public GetNewAccountRequest() {
		super();
	}

	public GetNewAccountRequest(String language) {
		super();
		this.language = language;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GetNewAccountRequest [language=");
		builder.append(language);
		builder.append("]");
		return builder.toString();
	}

}
