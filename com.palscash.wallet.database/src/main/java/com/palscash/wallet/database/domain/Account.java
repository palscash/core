package com.palscash.wallet.database.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "account")
public class Account implements Serializable {

	@Id
	@Column(name = "ID", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "PRIV_KEY", nullable = false, unique = true)
	private String privateKey;

	@Column(name = "PUB_KEY", nullable = false)
	private String publicKey;

	@Column(name = "UUID", nullable = false)
	private String uuid;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Transfer> transfers;

	@Column(nullable = true)
	private Date updated;

	@Column
	private String balance;

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public List<Transfer> getTransfers() {
		if (transfers == null) {
			transfers = new ArrayList<Transfer>();
		}
		return transfers;
	}

	public void setTransfers(List<Transfer> transfers) {
		this.transfers = transfers;
	}

	@PrePersist
	public void prePersist() {
		updated = new Date();
	}

	@PreUpdate
	public void preUpdate() {
		updated = new Date();
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", privateKey=" + privateKey + ", publicKey=" + publicKey + ", uuid=" + uuid + ", transfers=" + transfers + ", updated=" + updated + ", balance=" + balance + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		Account other = (Account) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public BigDecimal getBalanceAsBigDecimal() {
		return new BigDecimal(balance);
	}

}
