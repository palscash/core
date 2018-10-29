/*

MIT License

Copyright (c) 2017 ZDP Developers
Copyright (c) 2018 PalsCsah Team

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
*//*

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
