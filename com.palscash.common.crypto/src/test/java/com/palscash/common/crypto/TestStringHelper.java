package com.palscash.common.crypto;

import junit.framework.TestCase;

public class TestStringHelper extends TestCase {

	public void test() {

		new StringHelper();
		
		assertEquals("abcdefgh", StringHelper.cleanUpMemo("abcdefgh"));
		assertEquals("abcdefgh", StringHelper.cleanUpMemo("abcdefgh "));
		assertEquals("abcdefgh", StringHelper.cleanUpMemo("abcde%fgh <>#@$^@$&%$%"));

	}

}