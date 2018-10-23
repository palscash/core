package com.palscash.common.crypto;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Signing {

	private static final Logger log = LoggerFactory.getLogger(Signing.class);

	private static final String SHA256WITH_ECDSA = "SHA256withECDSA";

	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	/**
	 * Sign a UTF-8 string by using a provided private key
	 */
	public static byte[] sign(PrivateKey pvt, String data) throws Exception {
		return sign(pvt, data.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * Sign a UTF-8 string by using a provided private key
	 */
	public static byte[] sign(PrivateKey pvt, byte[] data) throws Exception {

		Signature sign = Signature.getInstance(SHA256WITH_ECDSA, BouncyCastleProvider.PROVIDER_NAME);

		sign.initSign(pvt);

		sign.update(data);

		return sign.sign();

	}

	/**
	 * Check is a Digital signature is valid by using provided RS public key
	 */
	public static boolean isValidSignature(PublicKey pub, byte[] dataToVerify, byte[] signature) {

		try {

			Signature sign = Signature.getInstance(SHA256WITH_ECDSA, BouncyCastleProvider.PROVIDER_NAME);

			sign.initVerify(pub);

			sign.update(dataToVerify);

			return sign.verify(signature);

		} catch (Exception e) {
			log.error("Error: " + e.getMessage());
		}

		return false;

	}

}
