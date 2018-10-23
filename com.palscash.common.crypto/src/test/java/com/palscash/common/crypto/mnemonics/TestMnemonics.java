package com.palscash.common.crypto.mnemonics;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import com.palscash.common.crypto.Curves;
import com.palscash.common.crypto.PalsCashKeyPair;
import com.palscash.common.crypto.mnemonics.Mnemonics.Language;

import junit.framework.TestCase;

public class TestMnemonics extends TestCase {

	public void test() {

		{
			PalsCashKeyPair kp = PalsCashKeyPair.createRandom(Curves.DEFAULT_CURVE);
			List<String> words1 = Mnemonics.generateWords(Language.ENGLISH, kp.getPrivateKeyAsBase58());
			List<String> words2 = Mnemonics.generateWords(Language.ENGLISH, kp.getPrivateKeyAsBigInteger().toByteArray());
			assertTrue(words1.equals(words2));
		}

		{
			PalsCashKeyPair kp = PalsCashKeyPair.createFromPrivateKeyBase58("WMjiwcRwn6b4XfVo1sk87w2TSLWD9bwJ8dN9L7w1uop");
			List<String> words1 = Mnemonics.generateWords(Language.ENGLISH, kp.getPrivateKeyAsBase58());
			List<String> words2 = Mnemonics.generateWords(Language.ENGLISH, kp.getPrivateKeyAsBigInteger().toByteArray());

			assertTrue(words1.equals(words2));
			List<String> words = Arrays.asList("always", "clean", "normal", "primary", "connect", "skill", "always", "bundle", "napkin", "mass", "wreck", "answer", "never", "suffer", "under", "layer", "infant", "glove", "robot", "song", "gorilla", "orbit", "basket", "jacket");
			assert (words.equals(words1));
		}

		{
			List<String> words = Arrays.asList("always", "clean", "normal", "primary", "connect", "skill", "always", "bundle", "napkin", "mass", "wreck", "answer", "never", "suffer", "under", "layer", "infant", "glove", "robot", "song", "gorilla", "orbit", "basket", "jacket");
			BigInteger privateKeyFromWords = Mnemonics.generatePrivateKeyFromWords(Language.ENGLISH, words);
			PalsCashKeyPair kp1 = PalsCashKeyPair.createFromPrivateKeyBigInteger(privateKeyFromWords, Curves.DEFAULT_CURVE);
			PalsCashKeyPair kp2 = PalsCashKeyPair.createFromPrivateKeyBase58("WMjiwcRwn6b4XfVo1sk87w2TSLWD9bwJ8dN9L7w1uop");
			assertEquals(kp1, kp2);
		}

	}

}
