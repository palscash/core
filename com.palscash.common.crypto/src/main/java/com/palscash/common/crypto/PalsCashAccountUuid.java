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

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * Utilities related to public account uuid
 */
public class PalsCashAccountUuid {

	private static final String ZERO_REPLACEMENT_FOR_BASE58 = "x";

	private static final String ZERO = "0";

	public static final String prefix = "pca";

	private byte[] publicKeyHash;

	private String curve;

	private int curveIndex;

	public PalsCashAccountUuid(byte[] publicKey, String curve) {

		byte[] hash = Hashing.hashPublicKey(publicKey);

		this.publicKeyHash = hash;
		this.curve = curve;
		this.curveIndex = Curves.getCurveIndex(curve);

	}

	public PalsCashAccountUuid(String uuid) {

		try {
			this.publicKeyHash = Base58.decode(uuid.substring(6, uuid.length() - 2));
		} catch (Exception e) {
			e.printStackTrace();
		}

		String curv = uuid.substring(3, 6);
		curv = StringUtils.replace(curv, ZERO_REPLACEMENT_FOR_BASE58, ZERO);
		while (curv.startsWith(ZERO)) {
			curv = StringUtils.removeStart(curv, ZERO);
		}

		curveIndex = Integer.parseInt(curv);
		this.curve = Curves.getCurveName(curveIndex);

	}

	public byte[] toHashData() {
		byte[] data = ArrayUtils.addAll(publicKeyHash, curve.getBytes(StandardCharsets.UTF_8));
		data = ArrayUtils.addAll(data, ByteBuffer.allocate(4).putInt(curveIndex).array());
		return Hashing.ripemd160(data);
	}

	public byte[] getPublicKeyHash() {
		return publicKeyHash;
	}

	public String getPublicKeyHashAsBase58() {
		return Base58.encode(publicKeyHash);
	}

	public String getCurve() {
		return curve;
	}

	public int getCurveAsIndex() {
		return Curves.getCurveIndex(curve);
	}

	public String getUuid() {

		String checksum = Long.toHexString(Checksum.calculate(publicKeyHash));
		checksum = StringUtils.leftPad(checksum, 2, ZERO_REPLACEMENT_FOR_BASE58);
		checksum = StringUtils.replace(checksum, ZERO, ZERO_REPLACEMENT_FOR_BASE58);

		return prefix + Curves.getCurveIndexAsReadable(curve) + Base58.encode(publicKeyHash) + checksum;

	}

	@Override
	public String toString() {
		return "PalsCashAccountUuid [" + getUuid() + "]";
	}

	public static boolean isValidAccountAddress(String publicAccountUuid) {

		if (StringUtils.isBlank(publicAccountUuid)) {
			return false;
		}

		if (false == StringUtils.startsWith(publicAccountUuid, prefix)) {
			return false;
		}

		if (publicAccountUuid.length() < 20) {
			return false;
		}

		// Curve
		String curveIndex = publicAccountUuid.substring(3, 6);
		curveIndex = StringUtils.replace(curveIndex, ZERO_REPLACEMENT_FOR_BASE58, ZERO);

		while (curveIndex.startsWith(ZERO)) {
			curveIndex = StringUtils.removeStart(curveIndex, ZERO);
		}

		if (false == NumberUtils.isCreatable(curveIndex)) {
			return false;
		}

		// Checksum and hash of public key
		String checksum = publicAccountUuid.substring(publicAccountUuid.length() - 2);

		String publicKeyHash = StringUtils.removeEnd(publicAccountUuid, checksum);
		publicKeyHash = publicKeyHash.substring(6);

		checksum = StringUtils.replace(checksum, ZERO_REPLACEMENT_FOR_BASE58, ZERO);

		while (checksum.startsWith(ZERO)) {
			checksum = StringUtils.removeStart(checksum, ZERO);
		}

		// Base58 of public key hash
		byte[] hash = null;
		try {
			hash = Base58.decode(publicKeyHash);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		String crc = Long.toHexString(Checksum.calculate(hash));

		while (crc.startsWith(ZERO)) {
			crc = StringUtils.removeStart(crc, ZERO);
		}

		if (false == crc.equals(checksum)) {
			return false;
		}

		return true;

	}


}
