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

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.ECFieldF2m;
import java.security.spec.ECFieldFp;
import java.security.spec.EllipticCurve;
import java.security.spec.InvalidKeySpecException;

import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveSpec;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.FixedPointCombMultiplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Keys {

	private static final Logger log = LoggerFactory.getLogger(Keys.class);

	private static final String EC = "EC";

	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	/**
	 * Generate an EC random private key
	 * 
	 * @param curveName - EC curve 
	 * @return a generated private key sa java.math.BigInteger
	 */
	public static BigInteger generateRandomPrivateKey(String curveName) {

		try {

			ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec(curveName);
			KeyPairGenerator g = KeyPairGenerator.getInstance(EC, BouncyCastleProvider.PROVIDER_NAME);
			g.initialize(ecSpec, new SecureRandom());
			KeyPair pair = g.generateKeyPair();

			BCECPrivateKey privKey = (BCECPrivateKey) pair.getPrivate();

			return privKey.getD();

		} catch (Exception e) {
			log.error("Error: ", e);
		}

		return null;

	}

	/**
	 * Returns public key bytes from the given private key. To convert a byte
	 * array into a BigInteger, use <tt>
	 * new BigInteger(1, bytes);</tt>
	 */
	public static byte[] getPublicKeyFromPrivate(BigInteger privKey, String curveName) {
		ECPoint point = getPublicPointFromPrivate(privKey, curveName);
		return point.getEncoded(true);
	}
	
	public static byte[] getPublicKeyFromPrivateUncompressed(BigInteger privKey, String curveName) {
		ECPoint point = getPublicPointFromPrivate(privKey, curveName);
		return point.getEncoded(false);
	}	

	/**
	 * Returns public key point from the given private key. To convert a byte
	 * array into a BigInteger, use <tt>
	 * new BigInteger(1, bytes);</tt>
	 */
	private static ECPoint getPublicPointFromPrivate(BigInteger privKey, String curveName) {

		ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec(curveName);

		if (privKey.bitLength() > ecSpec.getN().bitLength()) {
			privKey = privKey.mod(ecSpec.getN());
		}
		return new FixedPointCombMultiplier().multiply(ecSpec.getG(), privKey);
	}

	public static PrivateKey getPrivateKeyFromECBigIntAndCurve(BigInteger s, String curveName) {

		ECParameterSpec ecParameterSpec = ECNamedCurveTable.getParameterSpec(curveName);

		ECPrivateKeySpec privateKeySpec = new ECPrivateKeySpec(s, ecParameterSpec);
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(EC);
			return keyFactory.generatePrivate(privateKeySpec);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Decode a point on this curve which has been encoded using point
	 * compression (X9.62 s 4.2.1 and 4.2.2) or regular encoding.
	 * 
	 * @param curve
	 *            The elliptic curve.
	 * 
	 * @param encoded
	 *            The encoded point.
	 * 
	 * @return the decoded point.
	 * 
	 */
	public static ECPoint decodePoint(EllipticCurve curve, byte[] encoded) {
		ECCurve c = null;

		if (curve.getField() instanceof ECFieldFp) {
			c = new ECCurve.Fp(((ECFieldFp) curve.getField()).getP(), curve.getA(), curve.getB());
		} else {
			int k[] = ((ECFieldF2m) curve.getField()).getMidTermsOfReductionPolynomial();

			if (k.length == 3) {
				c = new ECCurve.F2m(((ECFieldF2m) curve.getField()).getM(), k[2], k[1], k[0], curve.getA(), curve.getB());
			} else {
				c = new ECCurve.F2m(((ECFieldF2m) curve.getField()).getM(), k[0], curve.getA(), curve.getB());
			}
		}

		return c.decodePoint(encoded);
	}

	public static PublicKey toPublicKey(byte[] publicKey, String curve) throws Exception {

		ECParameterSpec ecParameterSpec = ECNamedCurveTable.getParameterSpec(curve);

		ECNamedCurveSpec params = new ECNamedCurveSpec(curve, ecParameterSpec.getCurve(), ecParameterSpec.getG(), ecParameterSpec.getN());

		ECPoint publicPoint = decodePoint(params.getCurve(), publicKey);

		ECPublicKeySpec pubKeySpec = new ECPublicKeySpec(publicPoint, ecParameterSpec);

		KeyFactory keyFactory = KeyFactory.getInstance(EC);

		PublicKey pk = keyFactory.generatePublic(pubKeySpec);

		return pk;

	}

	public static String toPalsCashPrivateKey(BigInteger privKey) {
		return Base58.encode(privKey.toByteArray());
	}

	public static String toPublicKey(BigInteger priv, String curve) {
		return Base58.encode(Keys.getPublicKeyFromPrivate(priv, curve));
	}

	public static BigInteger toBigIntegerFromPrivateKeyBase58(String v) {

		try {

			byte[] decoded = Base58.decode(v);

			return new BigInteger(decoded);

		} catch (Exception e) {
			log.error("Error: ", e);
		}
		return null;

	}

}
