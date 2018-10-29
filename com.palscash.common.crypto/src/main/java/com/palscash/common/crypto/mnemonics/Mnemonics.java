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
package com.palscash.common.crypto.mnemonics;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.palscash.common.crypto.Base58;
import com.palscash.common.crypto.Keys;

/**
 * Based on https://github.com/bitcoin/bips/blob/master/bip-0039.mediawiki
 */
public class Mnemonics {

	private static final Logger log = LoggerFactory.getLogger(Keys.class);

	static final int wordsLengthBit = 11;

	public static enum Language {
		CHINESE_SIMPLIFIED, //
		CHINESE_TRADITIONAL, //
		ENGLISH, //
		FRENCH, //
		ITALIAN, //
		JAPANESE, //
		KOREAN, //
		SPANISH //
	}

	// private final Logger log = LoggerFactory.getLogger(this.getClass());

	private static final Map<Language, List<String>> WORDS = new HashMap<>();

	static {
		try {
			WORDS.put(Language.ENGLISH, IOUtils.readLines(Mnemonics.class.getResourceAsStream("/wordlist/english.txt"),
					StandardCharsets.UTF_8));
			/*
			 * WORDS.put(Language.CHINESE_SIMPLIFIED,
			 * IOUtils.readLines(Mnemonics.class.getResourceAsStream(
			 * "/wordlist/chinese_simplified.txt"), StandardCharsets.UTF_8));
			 * WORDS.put(Language.CHINESE_TRADITIONAL,
			 * IOUtils.readLines(Mnemonics.class.getResourceAsStream(
			 * "/wordlist/chinese_traditional.txt"), StandardCharsets.UTF_8));
			 * WORDS.put(Language.FRENCH,
			 * IOUtils.readLines(Mnemonics.class.getResourceAsStream("/wordlist/french.txt")
			 * , StandardCharsets.UTF_8)); WORDS.put(Language.ITALIAN,
			 * IOUtils.readLines(Mnemonics.class.getResourceAsStream("/wordlist/italian.txt"
			 * ), StandardCharsets.UTF_8)); WORDS.put(Language.JAPANESE,
			 * IOUtils.readLines(Mnemonics.class.getResourceAsStream(
			 * "/wordlist/japanese.txt"), StandardCharsets.UTF_8));
			 * WORDS.put(Language.KOREAN,
			 * IOUtils.readLines(Mnemonics.class.getResourceAsStream("/wordlist/korean.txt")
			 * , StandardCharsets.UTF_8)); WORDS.put(Language.SPANISH,
			 * IOUtils.readLines(Mnemonics.class.getResourceAsStream("/wordlist/spanish.txt"
			 * ), StandardCharsets.UTF_8));
			 */
		} catch (Exception e) {
			// log.error("Error: ", e);
			e.printStackTrace();
		}
	}

	public static List<String> generateWords(final Language lang, String privateKey58) {
		try {
			return generateWords(lang, Base58.decode(privateKey58));
		} catch (Exception e) {
			log.error("Error: ", e);
		}

		return Collections.emptyList();
	}

	public static List<String> generateWords(final Language lang, byte[] privateKey) {

		final List<String> words = new ArrayList<>();

		final String seedHash = DigestUtils.sha256Hex(privateKey);

		BigInteger number = new BigInteger(privateKey);
		String binaryString = number.toString(2);

		if (binaryString.length() != 256) {
			binaryString = StringUtils.leftPad(binaryString, 256, "0");
		}

		BigInteger numberChecksumBigInteger = new BigInteger(seedHash.toString(), 16);

		String numberChecksumBinary = numberChecksumBigInteger.toString(2);

		String hash = numberChecksumBinary.substring(0, 8);
		binaryString = binaryString + hash;

		for (int i = 0; i < binaryString.length(); i += wordsLengthBit) {
			String sub = binaryString.substring(i, i + wordsLengthBit);
			int wordIndex = Integer.parseInt(sub, 2);
			words.add(WORDS.get(lang).get(wordIndex));
		}

		return words;

	}

	public static BigInteger generatePrivateKeyFromWords(final Language lang, List<String> words) {

		StringBuilder sb = new StringBuilder();
		for (Object o : words) {
			String word = o.toString();
			int index = WORDS.get(lang).indexOf(word);
			String bin = Integer.toString(index, 2);
			bin = StringUtils.leftPad(bin, wordsLengthBit, '0');
			sb.append(bin);
		}

		// remove last 8 bits
		sb.setLength(sb.length() - 8);

		BigInteger i = new BigInteger(sb.toString(), 2);

		return i;
	}

}
