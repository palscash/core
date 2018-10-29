/*

MIT License

Copyright (c) 2017 ZDP Developers
Copyright (c) 2018 PalsCash Team

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package com.palscash.common.crypto;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class PalsCashKeyPair implements Serializable {

	private static final Logger log = LoggerFactory.getLogger(PalsCashKeyPair.class);

	private BigInteger bi;

	private String curve;

	private PalsCashKeyPair() {
	}

	public static PalsCashKeyPair createFromPrivateKeyBigInteger(BigInteger bigint, String curve) {
		PalsCashKeyPair pair = new PalsCashKeyPair();
		pair.bi = bigint;
		pair.curve = curve;
		return pair;
	}

	public static PalsCashKeyPair createFromPrivateKeyBase58(String privateKeyB58) {
		return createFromPrivateKeyBase58(privateKeyB58, Curves.DEFAULT_CURVE);
	}

	public static PalsCashKeyPair createFromPrivateKeyBase58(String privateKeyB58, String curve) {

		BigInteger privKey = Keys.toBigIntegerFromPrivateKeyBase58(privateKeyB58);

		PalsCashKeyPair pair = new PalsCashKeyPair();
		pair.bi = privKey;
		pair.curve = curve;

		return pair;

	}

	public static PalsCashKeyPair createRandom(String curve) {

		final BigInteger priv = Keys.generateRandomPrivateKey(curve);

		PalsCashKeyPair pair = new PalsCashKeyPair();
		pair.bi = priv;
		pair.curve = curve;

		return pair;

	}

	public String getPrivateKeyAsBase58() {
		return Keys.toPalsCashPrivateKey(bi);
	}

	public BigInteger getPrivateKeyAsBigInteger() {
		return bi;
	}

	public PrivateKey getPrivateKeyAsPrivateKey() {
		return Keys.getPrivateKeyFromECBigIntAndCurve(bi, curve);
	}

	private byte[] generatePublicKey() {
		if (bi == null) {
			throw new IllegalArgumentException("PrivateKey is null");
		}

		return Keys.getPublicKeyFromPrivate(bi, curve);
	}

	public byte[] getPublicKeyAsBytes() {
		return generatePublicKey();
	}

	public PublicKey getPublicKeyAsPublicKey() {
		try {
			return Keys.toPublicKey(getPublicKeyAsBytes(), curve);
		} catch (Exception e) {
			log.error("Error: ", e);
		}
		return null;
	}

	public String getPublicKeyAsBase58() {
		return Base58.encode(getPublicKeyAsBytes());
	}

	public PalsCashAccountUuid getPalsCashAccount() {

		byte[] pub = Keys.getPublicKeyFromPrivate(bi, curve);

		return new PalsCashAccountUuid(pub, curve);
	}

	public byte[] sign(byte[] data) throws Exception {
		return Signing.sign(getPrivateKeyAsPrivateKey(), data);
	}

	public byte[] sign(String data) throws Exception {
		return Signing.sign(getPrivateKeyAsPrivateKey(), data);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bi == null) ? 0 : bi.hashCode());
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
		PalsCashKeyPair other = (PalsCashKeyPair) obj;
		if (bi == null) {
			if (other.bi != null)
				return false;
		} else if (!bi.equals(other.bi))
			return false;
		return true;
	}

}
