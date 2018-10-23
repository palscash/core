package com.palscash.wallet.ui.action;

import java.awt.Window;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.palscash.wallet.database.common.FilePathTestWrapper;
import com.palscash.wallet.database.service.WalletService;
import com.palscash.wallet.ui.common.Alert;
import com.palscash.wallet.ui.common.SwingHelper;
import com.palscash.wallet.ui.common.SynchronousJFXFileChooser;
import com.palscash.wallet.ui.common.model.BooleanWrapper;
import com.palscash.wallet.ui.common.model.StringWrapper;
import com.palscash.wallet.ui.config.ConfigurationService;
import com.palscash.wallet.ui.dialog.EnterPasswordPanel;
import com.palscash.wallet.ui.gui.MainWindow;

import javafx.application.Platform;
import javafx.stage.FileChooser;

@Component
public class OpenWallet {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private MainWindow mainWindow;

	public void open(Window parent, JDialog dialog) {

		javafx.embed.swing.JFXPanel jfx = new javafx.embed.swing.JFXPanel();

		Platform.setImplicitExit(false);

		SynchronousJFXFileChooser chooser = new SynchronousJFXFileChooser(() -> {

			FileChooser ch = new FileChooser();
			
			File initialFolderFile = configurationService.getConfiguration().getFileDialogFolder();

			if (initialFolderFile != null) {
				ch.setInitialDirectory(initialFolderFile);
			}

			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PalsCash Wallet (*" + FilePathTestWrapper.EXTENSION + ")", "*" + FilePathTestWrapper.EXTENSION);
			ch.getExtensionFilters().add(extFilter);

			ch.setTitle("Open wallet file");
			return ch;
		});

		File walletFile = chooser.showOpenDialog();

		if (walletFile == null || false == walletFile.canRead()) {
			return;
		}

		configurationService.getConfiguration().setFileDialogFolder(walletFile.getParent());
		configurationService.saveConfiguration();

		log.debug("Open wallet: " + walletFile);

		// Get password
		final String password = enterPassword(parent);

		// Looks like the operation was cancelled
		if (password == null) {
			log.info("No password entered, cancel");
			return;
		}

		SwingHelper.async(parent, "Opening wallet", () -> {

			try {

				SwingUtilities.invokeLater(() -> {

					if (dialog != null) {
						dialog.dispose();
					}
				});
				
				boolean opened = WalletService.open(walletFile, password);

				if (opened) {

					mainWindow.loadWallet();

				} else {

					Alert.error("Sorry, this wallet can not be loaded");

				}

			} catch (Exception e) {
				log.error("Error: ", e);
			}

		});

	}

	private String enterPassword(Window parent) {

		StringWrapper password = new StringWrapper();

		final BooleanWrapper cancelled = new BooleanWrapper(false);

		while (password.get() == null && cancelled.isFalse()) {

			EnterPasswordPanel pp = new EnterPasswordPanel();

			JDialog eppDialog = SwingHelper.dialog(parent, pp);
			SwingHelper.installEscapeCloseOperation(eppDialog, cancelled);

			pp.btnOk.setEnabled(true);

			ActionListener al = e -> {

				password.set(new String(pp.password.getPassword()));

				eppDialog.dispose();

			};

			pp.btnOk.addActionListener(al);
			pp.password.addActionListener(al);

			eppDialog.setVisible(true);

		}

		return password.get();

	}

}
