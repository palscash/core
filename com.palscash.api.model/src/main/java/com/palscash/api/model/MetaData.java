package com.palscash.api.model;

import java.io.Serializable;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@SuppressWarnings("serial")
public class MetaData implements Serializable {

	protected String responseUuid;

	protected long responseDate;

	protected String responseIsoDate;

	private String comment = "";

	{

		responseUuid = DigestUtils.md5Hex(Long.toHexString(ThreadLocalRandom.current().nextLong())).toUpperCase();

		ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);

		responseDate = utc.toEpochSecond();

		responseIsoDate = DateTimeFormatter.ISO_DATE_TIME.format(utc);

	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getUuid() {
		return responseUuid;
	}

	public long getDate() {
		return responseDate;
	}

	public String getIsoDate() {
		return responseIsoDate;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MetaData [responseUuid=");
		builder.append(responseUuid);
		builder.append(", responseDate=");
		builder.append(responseDate);
		builder.append(", responseIsoDate=");
		builder.append(responseIsoDate);
		builder.append(", comment=");
		builder.append(comment);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((responseUuid == null) ? 0 : responseUuid.hashCode());
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
		MetaData other = (MetaData) obj;
		if (responseUuid == null) {
			if (other.responseUuid != null)
				return false;
		} else if (!responseUuid.equals(other.responseUuid))
			return false;
		return true;
	}

}
