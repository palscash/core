package com.palscash.common.crypto;

import java.math.BigInteger;

import junit.framework.TestCase;

public class TestHashing extends TestCase {

	public void test() {

		new Hashing();

		{
			byte[] hash = Hashing.hashPublicKey(BigInteger.TEN.toByteArray());
			assertEquals("vbuhaT4cydfpHbg8iuYXUz4sNw1", Base58.encode(hash));
		}

		{
			byte[] hash = Hashing.whirlpool(BigInteger.TEN.toByteArray());
			assertEquals("3kTAByGdKJhMMcoiLGadT4rK5nXuv63mB1umiQNj1M9n8UDagvjv8RUTsDoX83WkQKWg6SQxHPHy9tkhkezkEp7F", Base58.encode(hash));
		}

		{
			byte[] hash = Hashing.whirlpool("10");
			assertEquals("57QWgTUEStxknXqmkfEedr1sKbKnXDvTeLqRxQFW6QZa57nKLDFmzi4vkNZZVPtm9hfdHomVsTCge1WmJNYigG9V", Base58.encode(hash));
		}
		{
			byte[] hash = Hashing.ripemd160(BigInteger.TEN.toByteArray());
			assertEquals("3gqDZXP6bQ6NPdSXrwSWgWEneaRD", Base58.encode(hash));
		}
		{
			byte[] hash = Hashing.ripemd160("test me");
			assertEquals("3PMC8aFmG8jrepQPttp9WjpXiHns", Base58.encode(hash));
		}
	}

}