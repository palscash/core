package com.palscash.api.client;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

public class ValidationNodeUrls {

	public static List<String> getAll() throws IOException {
		return IOUtils
				.readLines( //
						ValidationNodeUrls.class.getResourceAsStream("/validation_nodes"), StandardCharsets.UTF_8) //
				.stream() //
				.filter(l -> false == StringUtils.isBlank(l) //
						&& false == l.startsWith("#")) //
				.collect(Collectors.toList() //
		); //
	}

	public static String getRandomHost() throws IOException {
		List<String> list = getAll();
		return list.get(RandomUtils.nextInt(0, list.size()));
	}

}
