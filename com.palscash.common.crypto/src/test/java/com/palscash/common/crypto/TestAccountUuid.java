package com.palscash.common.crypto;

import junit.framework.TestCase;

public class TestAccountUuid extends TestCase {

	public void test() {

		try {
			new PalsCashAccountUuid("wer");
			fail();
		} catch (Exception e) {
		}

		PalsCashKeyPair kp = PalsCashKeyPair.createFromPrivateKeyBase58("WMjiwcRwn6b4XfVo1sk87w2TSLWD9bwJ8dN9L7w1uop");

		{
			PalsCashAccountUuid acc = kp.getPalsCashAccount();
			assertEquals("secp256k1", acc.getCurve());
			assertEquals(27, acc.getCurveAsIndex());
			assertEquals("UKJ3YHEmX3od3cvqh4jP3P8AC2Q", Base58.encode(acc.getPublicKeyHash()));
			assertEquals("UKJ3YHEmX3od3cvqh4jP3P8AC2Q", acc.getPublicKeyHashAsBase58());
			assertEquals("pcax27UKJ3YHEmX3od3cvqh4jP3P8AC2Q61", acc.getUuid());
			assertEquals("2SrCwUXzv68NBGKoMoXF2qqjZtSn", Base58.encode(acc.toHashData()));
			assertEquals("PalsCashAccountUuid [pcax27UKJ3YHEmX3od3cvqh4jP3P8AC2Q61]", acc.toString());
		}

		assertTrue(PalsCashAccountUuid.isValidAccountAddress("pcax27UKJ3YHEmX3od3cvqh4jP3P8AC2Q61"));
		assertFalse(PalsCashAccountUuid.isValidAccountAddress(""));
		assertFalse(PalsCashAccountUuid.isValidAccountAddress(null));
		assertFalse(PalsCashAccountUuid.isValidAccountAddress("vcax27UKJ3YHEmX3od3cvqh4jP3P8AC2"));
		assertFalse(PalsCashAccountUuid.isValidAccountAddress("pcax27UKJ3YHEmX3od3cvqh4jP3P8AC2Q6"));
		assertFalse(PalsCashAccountUuid.isValidAccountAddress("pcaxxxUKJ3YHEmX3od3cvqh4jP3P8AC2Q6"));

		{
			PalsCashAccountUuid acc = new PalsCashAccountUuid("pcax27UKJ3YHEmX3od3cvqh4jP3P8AC2Q61");
			assertEquals("secp256k1", acc.getCurve());
			assertEquals(27, acc.getCurveAsIndex());
			assertEquals("UKJ3YHEmX3od3cvqh4jP3P8AC2Q", Base58.encode(acc.getPublicKeyHash()));
			assertEquals("UKJ3YHEmX3od3cvqh4jP3P8AC2Q", acc.getPublicKeyHashAsBase58());
			assertEquals("pcax27UKJ3YHEmX3od3cvqh4jP3P8AC2Q61", acc.getUuid());
			assertEquals("2SrCwUXzv68NBGKoMoXF2qqjZtSn", Base58.encode(acc.toHashData()));
			assertEquals("PalsCashAccountUuid [pcax27UKJ3YHEmX3od3cvqh4jP3P8AC2Q61]", acc.toString());

		}

	}

}