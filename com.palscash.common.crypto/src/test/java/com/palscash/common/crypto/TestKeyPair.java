package com.palscash.common.crypto;

import java.nio.charset.StandardCharsets;

import junit.framework.TestCase;

public class TestKeyPair extends TestCase {

	public void test() {

		PalsCashKeyPair kp = PalsCashKeyPair.createFromPrivateKeyBase58("WMjiwcRwn6b4XfVo1sk87w2TSLWD9bwJ8dN9L7w1uop");

		assertEquals("EC", kp.getPrivateKeyAsPrivateKey().getAlgorithm());
		assertEquals("PKCS#8", kp.getPrivateKeyAsPrivateKey().getFormat());
		assertEquals("49RVQMnzvQ7axFF24aYGJkVpqEHUmEcsXMFSGPbTcK56V5kT3aZk9x9nY7UZmywwryak1t78wKdkeRNN2zrpgH6TdYsGBq8gArc1", Base58.encode(kp.getPrivateKeyAsPrivateKey().getEncoded()));

		assertEquals("25vtJ5gPLrRkpyCwjvCYxqWakX3dCNfqGbXsR3GJabgiY", Base58.encode(kp.getPublicKeyAsBytes()));
		assertEquals("EC", kp.getPublicKeyAsPublicKey().getAlgorithm());
		assertEquals("X.509", kp.getPublicKeyAsPublicKey().getFormat());
		assertEquals("25vtJ5gPLrRkpyCwjvCYxqWakX3dCNfqGbXsR3GJabgiY", kp.getPublicKeyAsBase58());
		assertEquals("PZ8Tyr4Nx8MHsRAGMpZmZ6TWY63dXWSCyhPfaE29sLZAWrS5foL3gEkXNBTMPYfjVcBdiwoWhLzobs5Y5DaaGiS1svyoLdmc4oyoTQwmKbkUnLry88vfQvZg", Base58.encode(kp.getPublicKeyAsPublicKey().getEncoded()));

		PalsCashKeyPair kpCopy = PalsCashKeyPair.createFromPrivateKeyBase58("WMjiwcRwn6b4XfVo1sk87w2TSLWD9bwJ8dN9L7w1uop");

		assertTrue(kp.equals(kpCopy));
		assertTrue(kp.hashCode() == kpCopy.hashCode());
		assertFalse(kp.equals(""));
		assertFalse(kp.equals(null));
		assertTrue(kp.equals(kp));

		try {
			String text = "Hello people!";
			byte[] sign = kp.sign(text);
			assertTrue(Signing.isValidSignature(kp.getPublicKeyAsPublicKey(), text.getBytes(StandardCharsets.UTF_8), sign));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		try {
			String text = "Hello people!";
			byte[] sign = kp.sign(text.getBytes(StandardCharsets.UTF_8));
			assertTrue(Signing.isValidSignature(kp.getPublicKeyAsPublicKey(), text.getBytes(StandardCharsets.UTF_8), sign));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		try {
			String text = "Hello people!";
			byte[] sign = kp.sign(text);
			assertFalse(Signing.isValidSignature(kp.getPublicKeyAsPublicKey(), (text + " ").getBytes(StandardCharsets.UTF_8), sign));
		} catch (Exception e) {
		}

		{
			PalsCashKeyPair emptyKp = PalsCashKeyPair.createFromPrivateKeyBigInteger(null, "");
			assertFalse(emptyKp.equals(kp));
			assertFalse(kp.equals(emptyKp));
		}
		
		new Signing();

	}

}