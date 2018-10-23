package com.palscash.api.cli;

import java.math.BigDecimal;
import java.net.ConnectException;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.palscash.api.client.PalsCashClient;
import com.palscash.api.model.GetFeeResponse;

@Component
public class PCA {

	private static final Logger log = LoggerFactory.getLogger(PCA.class);

	@Autowired
	private PalsCashClient client;

	public PalsCashClient getClient() {
		return client;
	}

	public static void main(String[] args) {

		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:/spring-context.xml");

		try {
			ctx.getBean(PCA.class).run(args);
		} catch (Exception e) {
			log.error("Error: ", e);
		} finally {
			ctx.stop();
			ctx.close();
		}

	}

	private void run(String[] _args) {

		Options options = new Options();

		options.addOption(Option.builder().longOpt("help").desc("Display this help and exit").hasArg(false).build());

		options.addOption(Option.builder().longOpt("ping").desc("Ping Palscash network").hasArg(false).build());

		options.addOption(Option.builder().longOpt("fee").desc("Get current network transaction fee").hasArg(false).build());

		CommandLineParser parser = new DefaultParser();

		try {

			CommandLine cmd = parser.parse(options, _args);

			// validate that block-size has been set
			if (cmd.hasOption("help")) {
				help(options);
			} else if (cmd.hasOption("ping")) {
				ping();
			} else if (cmd.hasOption("fee")) {
				fee();

			}
		} catch (ParseException e) {
			e.printStackTrace();
			help(options);
		} catch (NoSuchAlgorithmException e) {
			System.err.println(e.getMessage());
		} catch (Exception e) {
			if (e instanceof ConnectException || e.getCause() instanceof ConnectException) {
				System.err.println("Network not available.");
			} else {
				System.err.println(e.getMessage());
			}
		}

	}

	private ObjectMapper getJsonMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		return mapper;
	}

	private void help(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("java -jar pca.jar", options);
		System.exit(0);
	}

	private void fee() throws Exception {
		GetFeeResponse fee = client.getFee();
		System.out.println(fee.getFee());
	}

	private void ping() throws Exception {
		System.out.println("Pinging PalsCash network");
		while (true) {
			long st = System.currentTimeMillis();
			client.ping();
			long et = System.currentTimeMillis();
			System.out.println("time=" + (et - st) + "ms");
			Thread.sleep(1000);
		}
	}

}
