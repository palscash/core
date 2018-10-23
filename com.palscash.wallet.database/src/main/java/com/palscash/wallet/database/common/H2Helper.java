package com.palscash.wallet.database.common;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.commons.lang3.StringUtils;
import org.h2.store.fs.FilePath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class H2Helper {

	private static final Logger log = LoggerFactory.getLogger(H2Helper.class);

	static {

		log.debug("Inited H2Helper");

		FilePathTestWrapper wrapper = new FilePathTestWrapper();
		FilePath.register(wrapper);
	}

	public H2Helper() {

	}

	public static boolean isValidH2Database(File file, String password) {

		File fileToCheck = new File(StringUtils.appendIfMissing(file.getAbsolutePath(), FilePathTestWrapper.EXTENSION));

		if (false == fileToCheck.exists() || false == fileToCheck.canRead()) {
			return false;
		}

		try {

			Class.forName("org.h2.Driver");

			String url = "jdbc:h2:palscash:" + StringUtils.removeEnd(file.getAbsolutePath(), FilePathTestWrapper.EXTENSION) + ";CIPHER=AES;IFEXISTS=TRUE";

			log.debug("Url: " + url);

			Connection conn = DriverManager.getConnection(url, "palscash", password);

			conn.close();

			return true;

		} catch (Exception e) {
			log.error("Error: ", e);
		}

		return false;

	}

}
