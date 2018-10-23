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
