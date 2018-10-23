package com.palscash.wallet.database.service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.palscash.wallet.database.common.FilePathTestWrapper;
import com.palscash.wallet.database.common.H2Helper;
import com.palscash.wallet.database.common.exception.WalletException;
import com.palscash.wallet.database.domain.Account;

public class WalletService {

	private static final Logger log = LoggerFactory.getLogger(WalletService.class);

	private static ClassPathXmlApplicationContext ctx;

	private static File file;

	public static boolean open(File file, String password) {

		log.debug("Open wallet: " + file);

		password = createPassword(password);

		if (false == H2Helper.isValidH2Database(file, password)) {
			log.error("This is not a valid wallet file: " + file);
			return false;
		}

		return openOrCreate(file, password);

	}

	private static String createPassword(String password) {
		String pass = DigestUtils.sha256Hex(password);
		return pass + " " + pass;
	}

	public static boolean create(File file, String password) throws WalletException {

		log.debug("Create wallet: " + file);

		password = createPassword(password);

		if (file.exists()) {
			throw new WalletException("File already exists: " + file);
		}

		// check if writable
		try {
			FileUtils.write(file, "test", StandardCharsets.UTF_8);
			FileUtils.forceDelete(file);
		} catch (Exception e) {
			throw new WalletException("File already exists: " + file);
		}

		return openOrCreate(file, password);

	}

	private static boolean openOrCreate(File f, String password) {

		file = f;

		String walletFile = StringUtils.removeEnd(file.getAbsolutePath(), FilePathTestWrapper.EXTENSION);

		System.setProperty("wallet.file", walletFile);
		System.setProperty("wallet.password", password);

		if (ctx != null) {
			close();
		}

		ctx = new ClassPathXmlApplicationContext("classpath:/spring-wallet-context.xml");

		return true;

	}

	public static ClassPathXmlApplicationContext getCtx() {
		return ctx;
	}

	public static File getFile() {
		return file;
	}

	public static void close() {

		if (ctx != null) {
			ctx.stop();
			ctx.close();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}
		}
	}

	public static AccountService getAccountService() {
		return getCtx().getBean(AccountService.class);
	}

	public static TransferService getTransferService() {
		return getCtx().getBean(TransferService.class);
	}

	public static List<Account> getAccounts() {
		return getAccountService().getAll();
	}

}
