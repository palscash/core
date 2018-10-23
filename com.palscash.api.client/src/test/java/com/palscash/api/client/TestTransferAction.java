package com.palscash.api.client;

import java.math.BigDecimal;

import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.palscash.api.model.TransferResponse;
import com.palscash.common.crypto.Curves;
import com.palscash.common.crypto.PalsCashKeyPair;

import junit.framework.TestCase;

@RunWith(SpringRunner.class)
@ContextConfiguration("/spring-context.xml")
public class TestTransferAction extends TestCase {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PalsCashClient client;

	@Test
	public void test() throws Exception {
		
		client.setHost(ValidationNodeUrls.getRandomHost());

		TransferResponse transfer = client.transfer("5WqBpifmcJ4iR4WeFmSnN8mUZoHMHrW7tNUXAcMgTEen", "pcax27X9A2dR1r5Ju5GR2az6uvzcH5BLp8f", new BigDecimal("0.0001"), "test");
		
		ObjectMapper mapper = new  ObjectMapper();
		log.debug( mapper.writeValueAsString(transfer) );

	}

}
