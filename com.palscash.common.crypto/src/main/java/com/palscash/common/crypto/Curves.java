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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Curves {

	private static final Logger log = LoggerFactory.getLogger(Curves.class);

	public static final String DEFAULT_CURVE = "secp256k1";
	public static final int DEFAULT_CURVE_INDEX = 45;
	
	private static final Map<Integer, String> curves = new HashMap<>();

	static {

		Security.addProvider(new BouncyCastleProvider());
		
		log.debug("Load curves:");

		try {
			List<String> lines = IOUtils.readLines(Curves.class.getResourceAsStream("/curves.txt"), StandardCharsets.UTF_8);
			for (String line : lines) {
				String[] split = line.split("\\s+");
				curves.put(Integer.parseInt(split[0]), split[1]);
			}

		} catch (IOException e) {
			log.error("Error:", e);
		}

		log.debug("Available curves:");

		for (int i : curves.keySet()) {
			log.debug(i + " " + curves.get(i));
		}

	}

	public static List<String> getAvailableCurves() {
		final List<String> values = new ArrayList<>(curves.values());
		Collections.sort(values);
		return Collections.unmodifiableList(values);
	}

	public static String getCurveName(int i) {
		return curves.get(i);
	}

	public static String getCurveName(String v) {
		return curves.get(Integer.parseInt(v));
	}

	public static String getCurveIndexAsReadable(String curve) {
		int index = getCurveIndex(curve);
		return StringUtils.leftPad(Integer.toString(index), 3, "x");
	}

	public static int getCurveIndex(String curve) {
		for (int i : curves.keySet()) {
			if (curve.equals(curves.get(i))) {
				return i;
			}
		}
		return -1;
	}

}
