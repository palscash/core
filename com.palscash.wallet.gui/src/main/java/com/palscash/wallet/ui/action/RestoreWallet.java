package com.palscash.wallet.ui.action;

import java.awt.Window;
import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.SwingUtilities;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.palscash.common.crypto.Curves;
import com.palscash.common.crypto.PalsCashKeyPair;
import com.palscash.common.crypto.mnemonics.Mnemonics;
import com.palscash.common.crypto.mnemonics.Mnemonics.Language;
import com.palscash.wallet.database.common.FilePathTestWrapper;
import com.palscash.wallet.database.domain.Account;
import com.palscash.wallet.database.service.WalletService;
import com.palscash.wallet.ui.common.Alert;
import com.palscash.wallet.ui.common.QTextComponentContextMenu;
import com.palscash.wallet.ui.common.SwingHelper;
import com.palscash.wallet.ui.common.SynchronousJFXFileChooser;
import com.palscash.wallet.ui.common.TextComponentFocuser;
import com.palscash.wallet.ui.config.ConfigurationService;
import com.palscash.wallet.ui.dialog.WalletRestorationPanel;
import com.palscash.wallet.ui.gui.MainWindow;

import javafx.application.Platform;
import javafx.stage.FileChooser;

@Component
public class RestoreWallet {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MainWindow mainWindow;

	@Autowired
	private ConfigurationService configurationService;

	public void restore(Window parent) {

		WalletRestorationPanel panel = new WalletRestorationPanel();

		new QTextComponentContextMenu(panel.txtListOfWords);
		new QTextComponentContextMenu(panel.txtPrivateKey);

		panel.txtListOfWords.addFocusListener(new TextComponentFocuser());
		panel.txtPrivateKey.addFocusListener(new TextComponentFocuser());

		JDialog dialog = SwingHelper.dialog(parent, panel);
		dialog.setTitle("Restore wallet");

		panel.btnRestoreWallet.addActionListener(ev -> {

			// validate private key or list of words
			String privateKey = "";

			if (StringUtils.isNotBlank(panel.txtPrivateKey.getText())) {

				privateKey = panel.txtPrivateKey.getText().trim();

			} else if (StringUtils.isNotBlank(panel.txtListOfWords.getText())) {

				// String words = panel.txtPrivateKey.getText().split("");
				String[] split = StringUtils.split(panel.txtListOfWords.getText().trim(), "\r\n, ");
				List<String> words = Arrays.asList(split);

				BigInteger bi = Mnemonics.generatePrivateKeyFromWords(Language.valueOf(panel.language.getSelectedItem().toString().toUpperCase()), words);

				privateKey = PalsCashKeyPair.createFromPrivateKeyBigInteger(bi, Curves.DEFAULT_CURVE).getPrivateKeyAsBase58();

			} else {

				Alert.warn("Please, enter a private key or a list of words to restore a wallet");
				return;

			}

			new javafx.embed.swing.JFXPanel();
			Platform.setImplicitExit(false);

			SynchronousJFXFileChooser chooser = new SynchronousJFXFileChooser(() -> {
				FileChooser ch = new FileChooser();

				File initialFolderFile = configurationService.getConfiguration().getFileDialogFolder();

				if (initialFolderFile != null) {
					ch.setInitialDirectory(initialFolderFile);
				}

				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PalsCash Wallet (*" + FilePathTestWrapper.EXTENSION + ")", "*" + FilePathTestWrapper.EXTENSION);
				ch.getExtensionFilters().add(extFilter);

				ch.setTitle("Save wallet file");

				return ch;
			});

			File walletFile = chooser.showSaveDialog();

			if (walletFile == null) {
				return;
			}
			
			if (walletFile.exists()) {
				try {
					FileUtils.forceDelete(walletFile);
				} catch (Exception e1) {
					Alert.error("Error deleting file: " + walletFile);
					return;
				}
			}

			// Enter DB file password for AES encryption
			String password = CreateNewWallet.enterPassword(parent);

			// Looks like the operation was cancelled
			if (password == null) {
				log.info("No password entered, cancel");
				return;
			}

			dialog.dispose();

			log.debug("Save new wallet: " + walletFile);

			String privateKeyCopy = privateKey;

			SwingHelper.async(parent, "Restoring wallet...", () -> {

				restore(parent, walletFile, password, privateKeyCopy);

			});

		});

		panel.btnCancel.addActionListener(e -> {
			dialog.dispose();
		});

		SwingHelper.installEscapeCloseOperation(dialog);

		dialog.setVisible(true);
	}

	private void restore(Window parent, File walletFile, String password, String privateKeyCopy) {

		try {

			if (parent != mainWindow.getFrame()) {
				parent.dispose();
			}

			PalsCashKeyPair kp = PalsCashKeyPair.createFromPrivateKeyBase58(privateKeyCopy, Curves.DEFAULT_CURVE);

			WalletService.create(walletFile, password);

			Account acc = new Account();
			acc.setBalance(BigDecimal.ZERO.toPlainString());
			acc.setPrivateKey(kp.getPrivateKeyAsBase58());
			acc.setPublicKey(kp.getPublicKeyAsBase58());
			acc.setUuid(kp.getPalsCashAccount().getUuid());

			WalletService.getAccountService().save(acc);

			SwingUtilities.invokeLater(() -> {
				mainWindow.loadWallet();
			});

		} catch (Exception e) {
			log.error("Error: ", e);
		}
	}

}
