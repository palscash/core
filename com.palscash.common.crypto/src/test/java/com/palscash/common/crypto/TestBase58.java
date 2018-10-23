package com.palscash.common.crypto;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

import org.bouncycastle.util.Arrays;

import junit.framework.TestCase;

public class TestBase58 extends TestCase {

	public void test() {

		new Base58();

		String input = "Hello World!";
		String output = "2NEpo7TZRRrLZSi2U";

		assertEquals("B", Base58.encode(BigInteger.TEN.toByteArray()));
		assertEquals("1", Base58.encode(BigInteger.ZERO.toByteArray()));
		assertEquals("2", Base58.encode(BigInteger.ONE.toByteArray()));

		assertEquals(output, Base58.encode(input.getBytes(StandardCharsets.UTF_8)));

		assertTrue(Base58.isBase58(output));
		assertFalse(Base58.isBase58("2NEpo7TZRRrLZSi2U/+"));

		assertEquals("", Base58.encode(new byte[] {}));

		try {
			assertTrue(Arrays.areEqual(new byte[0], Base58.decode("")));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		try {
			BigInteger bi = Base58.decodeToBigInteger("12312");
			assertEquals(201841, bi.intValue());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

	}

}
