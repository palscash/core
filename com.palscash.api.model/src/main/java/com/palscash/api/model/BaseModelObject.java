package com.palscash.api.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class BaseModelObject implements Serializable {

	public abstract String getType();

	protected MetaData responseMetadata = new MetaData();

	public MetaData getResponseMetadata() {
		return responseMetadata;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((responseMetadata == null) ? 0 : responseMetadata.hashCode());
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
		BaseModelObject other = (BaseModelObject) obj;
		if (responseMetadata == null) {
			if (other.responseMetadata != null)
				return false;
		} else if (!responseMetadata.equals(other.responseMetadata))
			return false;
		return true;
	}

}
