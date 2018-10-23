package com.palscash.wallet.ui.action;

import java.awt.Window;
import java.io.File;
import java.math.BigDecimal;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.Arrays;
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
import com.palscash.wallet.ui.common.I18n;
import com.palscash.wallet.ui.common.QTextComponentContextMenu;
import com.palscash.wallet.ui.common.SwingHelper;
import com.palscash.wallet.ui.common.SynchronousJFXFileChooser;
import com.palscash.wallet.ui.common.TextComponentFocuser;
import com.palscash.wallet.ui.common.model.BooleanWrapper;
import com.palscash.wallet.ui.common.model.StringWrapper;
import com.palscash.wallet.ui.config.ConfigurationService;
import com.palscash.wallet.ui.dialog.PasswordPanel;
import com.palscash.wallet.ui.dialog.WalletCreationPanel;
import com.palscash.wallet.ui.gui.MainWindow;

import javafx.application.Platform;
import javafx.stage.FileChooser;

@Component
public class CreateNewWallet {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private I18n i18n;

	@Autowired
	private MainWindow mainWindow;

	@Autowired
	private ConfigurationService configurationService;
	
	public void create(Window parent) {

		WalletCreationPanel panel = new WalletCreationPanel();

		new QTextComponentContextMenu(panel.txtSeed);
		new QTextComponentContextMenu(panel.txtMnemonics);
		new QTextComponentContextMenu(panel.txtPublicAddress);

		panel.txtSeed.addFocusListener(new TextComponentFocuser());
		panel.txtMnemonics.addFocusListener(new TextComponentFocuser());
		panel.txtPublicAddress.addFocusListener(new TextComponentFocuser());

		JDialog newWalletDialog = SwingHelper.dialog(parent, panel);
		newWalletDialog.setTitle("New wallet");

		long st = System.currentTimeMillis();
		PalsCashKeyPair kp = generateWalletInfo(panel);
		long et = System.currentTimeMillis();

		log.debug("generateWalletInfo took: " + (et - st) + " ms");

		panel.txtPublicAddress.setText(kp.getPalsCashAccount().getUuid());

		panel.btnCreatWallet.addActionListener(ev -> {

			if (false == Alert.confirm("Did you write down the wallet private key or list of words?")) {
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
				walletFile.delete();
			}

			newWalletDialog.dispose();

			log.debug("Save new wallet: " + walletFile);

			// Enter DB file password for AES encryption
			String password = enterPassword(parent);

			// Looks like the operation was cancelled
			if (password == null) {
				log.info("No password entered, cancel");
				return;
			}

			log.debug("Save new wallet: " + walletFile);

			SwingHelper.async(mainWindow.getFrame(), "Creating new wallet...", () -> {

				try {
					
					if (parent != mainWindow.getFrame()) {
						parent.dispose();
					}
					
					WalletService.create(walletFile, password);

					Account acc = new Account();
					acc.setBalance(BigDecimal.ZERO.toPlainString());
					acc.setPrivateKey(kp.getPrivateKeyAsBase58());
					acc.setPublicKey(kp.getPublicKeyAsBase58());
					acc.setUuid(kp.getPalsCashAccount().getUuid());

					WalletService.getAccountService().save(acc);

					mainWindow.loadWallet();


				} catch (Exception e) {
					log.error("Error: ", e);
				}
			});

		});

		panel.btnCancel.addActionListener(e -> {
			newWalletDialog.dispose();
		});

		SwingHelper.installEscapeCloseOperation(newWalletDialog);

		newWalletDialog.setVisible(true);

	}

	public static String enterPassword(Window parent) {

		StringWrapper password = new StringWrapper();

		final BooleanWrapper cancelled = new BooleanWrapper(false);

		while (password.get() == null && cancelled.isFalse()) {

			PasswordPanel pp = new PasswordPanel();

			JDialog eppDialog = SwingHelper.dialog(parent, pp);
			SwingHelper.installEscapeCloseOperation(eppDialog, cancelled);

			pp.btnOk.setEnabled(true);

			pp.btnOk.addActionListener(e -> {

				if (pp.password.getPassword().length == 0 || pp.passwordConfirm.getPassword().length == 0) {

					JOptionPane.showMessageDialog(parent, "Please, enter passwords", "Warning", JOptionPane.WARNING_MESSAGE);

				} else if (pp.password.getPassword().length > 0 && Arrays.areEqual(pp.password.getPassword(), pp.passwordConfirm.getPassword())) {

					// Empty?

					String pass = new String(pp.password.getPassword());

					if (StringUtils.isBlank(pass) || StringUtils.contains(pass, ' ')) {
						JOptionPane.showMessageDialog(parent, "Sorry, spaces/tabs are not allowed", "Warning", JOptionPane.WARNING_MESSAGE);
						return;
					}

					password.set(pass);

					eppDialog.dispose();

				} else {

					JOptionPane.showMessageDialog(parent, "The passwords do not match", "Warning", JOptionPane.WARNING_MESSAGE);

				}

			});

			pp.btnCancel.addActionListener(e -> {
				cancelled.set(true);
				eppDialog.dispose();
			});

			eppDialog.setVisible(true);

		}

		return password.get();

	}

	private PalsCashKeyPair generateWalletInfo(WalletCreationPanel panel) {

		PalsCashKeyPair kp = PalsCashKeyPair.createRandom(Curves.DEFAULT_CURVE);

		try {

			panel.txtSeed.setText(kp.getPrivateKeyAsBase58());

			Language l = Language.ENGLISH;

			List<String> generateWords = Mnemonics.generateWords(l, kp.getPrivateKeyAsBase58());
			String words = StringUtils.join(generateWords, IOUtils.LINE_SEPARATOR);
			panel.txtMnemonics.setText(words);
			panel.txtMnemonics.setCaretPosition(0);

		} catch (Exception e) {
			log.error("Error: ", e);
		}

		return kp;

	}

}
