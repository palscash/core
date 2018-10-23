package com.palscash.wallet.ui.config;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class Configuration implements Serializable {

	private static final Logger log = LoggerFactory.getLogger(Configuration.class);

	private String lastWalletFile;

	private String fileDialogFolder;

	private List<String> recentWallets = new ArrayList<>();

	public File getFileDialogFolder() {

		if (fileDialogFolder != null) {

			File initialFolderFile = new File(fileDialogFolder);

			if (initialFolderFile.exists()) {
				return initialFolderFile;
			}

		}

		return null;
	}

	public void setFileDialogFolder(String fileDialogFolder) {
		this.fileDialogFolder = fileDialogFolder;
		log.info("fileDialogFolder: " + fileDialogFolder);
	}

	public List<String> getRecentWallets() {
		return recentWallets;
	}

	public void setRecentWallets(List<String> recentWallets) {
		this.recentWallets = recentWallets;
	}

	public String getLastWalletFile() {
		return lastWalletFile;
	}

	public void setLastWalletFile(String lastWalletFile) {
		this.lastWalletFile = lastWalletFile;
	}

}
