package com.palscash.wallet.ui.gui.view;

import java.awt.TrayIcon.MessageType;
import java.math.BigDecimal;

import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.palscash.api.client.PalsCashClient;
import com.palscash.api.model.GetBalanceResponse;
import com.palscash.api.model.GetFeeResponse;
import com.palscash.api.model.TransferResponse;
import com.palscash.common.crypto.PalsCashAccountUuid;
import com.palscash.wallet.database.domain.Account;
import com.palscash.wallet.database.service.WalletService;
import com.palscash.wallet.ui.common.Alert;
import com.palscash.wallet.ui.common.Icons;
import com.palscash.wallet.ui.common.JTextFieldLimit;
import com.palscash.wallet.ui.common.NumberHelper;
import com.palscash.wallet.ui.common.QTextComponentContextMenu;
import com.palscash.wallet.ui.common.SwingHelper;
import com.palscash.wallet.ui.common.TextComponentFocuser;
import com.palscash.wallet.ui.dialog.SendPanel;
import com.palscash.wallet.ui.dialog.TransferConfirmationPanel;
import com.palscash.wallet.ui.gui.MainWindow;

@Component
public class SendView {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PalsCashClient client;

	@Autowired
	private MainWindow mainWindow;

	private Icon loadingIcon = Icons.getIcon("ajax-loader.gif");

	public JPanel get() {

		final SendPanel sendPanel = new SendPanel();

		GetFeeResponse fee = new GetFeeResponse();

		sendPanel.btnSendMax.setEnabled(false);
		sendPanel.btnSendMax.addActionListener(e -> {

			log.debug("Send maximum");

			Account acc = WalletService.getAccountService().getAll().get(0);

			BigDecimal max = acc.getBalanceAsBigDecimal().subtract(new BigDecimal(fee.getFee()));

			if (max.longValue() >= 0) {
				sendPanel.txtAmount.setText(max.toPlainString());
			} else {
				sendPanel.txtAmount.setText("0");
			}

		});

		sendPanel.txtAddressBalance.setIcon(loadingIcon);
		sendPanel.txtFee.setIcon(loadingIcon);

		try {

			final String publicKey58 = WalletService.getAccountService().getAll().get(0).getUuid();

			sendPanel.txtFromAddress.setText(publicKey58);
			sendPanel.txtFromAddress.setCaretPosition(0);
		} catch (Exception e2) {
			log.error("Error: ", e2);
		}

		new Thread(() -> {
			try {
				fee.setFee(client.getFee().getFee());
				log.debug("Got fee: " + fee.getFee());
				sendPanel.btnSendMax.setEnabled(true);
				SwingUtilities.invokeLater(() -> {
					sendPanel.txtFee.setText(fee.getFee());
					sendPanel.txtFee.setIcon(Icons.getIcon("check.png"));
				});

			} catch (Exception e1) {
				log.error("Error: ", e1);
			}
		}).start();

		updateAddressBalance(sendPanel);

		new QTextComponentContextMenu(sendPanel.txtToAddress);
		sendPanel.txtToAddress.addFocusListener(new TextComponentFocuser());

		new QTextComponentContextMenu(sendPanel.txtAmount);
		sendPanel.txtAmount.setDocument(new JTextFieldLimit(32));
		sendPanel.txtAmount.addFocusListener(new TextComponentFocuser());

		new QTextComponentContextMenu(sendPanel.txtMemo);
		sendPanel.txtMemo.setDocument(new JTextFieldLimit(64));
		sendPanel.txtMemo.addFocusListener(new TextComponentFocuser());

		new QTextComponentContextMenu(sendPanel.txtFromAddress);
		sendPanel.txtFromAddress.addFocusListener(new TextComponentFocuser());

		new QTextComponentContextMenu(sendPanel.txtTotalCharge);

		JScrollPane scroll = new JScrollPane(sendPanel);
		SwingHelper.updateScrollPane(scroll);

		sendPanel.txtAmount.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateTotalCharge();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				updateTotalCharge();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateTotalCharge();
			}

			void updateTotalCharge() {
				String amountText = sendPanel.txtAmount.getText();
				if (NumberUtils.isCreatable(amountText) && NumberUtils.isParsable(amountText)) {
					BigDecimal amount = new BigDecimal(amountText);
					BigDecimal total = amount.add(new BigDecimal(fee.getFee()));
					sendPanel.txtTotalCharge.setText(total.toString());
				}
			}

		});

		sendPanel.btnSend.addActionListener(e -> {

			log.debug("Send funds");

			String from = sendPanel.txtFromAddress.getText();
			String to = sendPanel.txtToAddress.getText();

			if (false == NumberUtils.isParsable(sendPanel.txtAmount.getText().trim())) {
				Alert.warn("Please, enter a valid amount");
				return;
			}

			BigDecimal amount = new BigDecimal(sendPanel.txtAmount.getText().trim());

			if (amount.compareTo(BigDecimal.ZERO) < 0) {
				Alert.warn("Please, enter a valid amount");
				return;
			}

			if (PalsCashAccountUuid.isValidAccountAddress(to) == false) {
				Alert.warn("To address is not valid");
				return;
			}

			Account currentAccount = WalletService.getAccountService().getAll().get(0);

			if (amount.compareTo(currentAccount.getBalanceAsBigDecimal()) > 0) {
				Alert.warn("This address only has " + currentAccount.getBalance());
				return;
			}

			log.debug("Send from " + from + " to " + to + ", amount: " + amount);

			TransferConfirmationPanel panel = new TransferConfirmationPanel();
			panel.txtAmount.setText(sendPanel.txtAmount.getText());
			panel.txtFrom.setText(sendPanel.txtFromAddress.getText());
			panel.txtTo.setText(sendPanel.txtToAddress.getText());
			panel.txtMemo.setText(sendPanel.txtMemo.getText());

			new QTextComponentContextMenu(panel.txtAmount);
			new QTextComponentContextMenu(panel.txtFrom);
			new QTextComponentContextMenu(panel.txtTo);
			new QTextComponentContextMenu(panel.txtMemo);

			panel.txtFrom.setCaretPosition(0);
			panel.txtTo.setCaretPosition(0);

			JDialog dialog = SwingHelper.dialog(mainWindow.getFrame(), panel);

			panel.btnCancel.addActionListener(ev -> {
				dialog.dispose();
			});

			panel.btnTransfer.addActionListener(ev -> {

				try {

					BigDecimal amountToSend = new BigDecimal(panel.txtAmount.getText().trim());

					TransferResponse transferResponse = client.transfer(currentAccount.getPrivateKey(), panel.txtTo.getText(), amountToSend, panel.txtMemo.getText());

					if (transferResponse == null || StringUtils.isBlank(transferResponse.getTransactionUuid())) {

						String err = "Transaction failed " + transferResponse == null ? "" : transferResponse.getError();
						mainWindow.showSystemTrayMessage(MessageType.ERROR, err);
						Alert.error(err);

					} else {

						String msg = "Submitted transaction " + transferResponse.getTransactionUuid();
						mainWindow.showSystemTrayMessage(MessageType.INFO, msg);

						Alert.info("Transaction submitted: " + transferResponse.getTransactionUuid());

						updateAddressBalance(sendPanel);

						mainWindow.updateUI();

					}

					dialog.dispose();

				} catch (Exception ex) {
					log.error("Error: ", ex);
				}
			});

			SwingHelper.installEscapeCloseOperation(dialog);
			dialog.setTitle("Confirm transfer");
			dialog.setVisible(true);

		});

		return sendPanel;

	}

	private void updateAddressBalance(SendPanel sendPanel) {

		sendPanel.txtAddressBalance.setIcon(loadingIcon);
		sendPanel.txtAddressBalance.setText("Getting account balance");

		new Thread(() -> {

			try {

				Account acc = WalletService.getAccounts().get(0);

				GetBalanceResponse resp = client.getBalance(acc.getUuid());
				
				acc = WalletService.getAccountService().updateAccountBalance(acc, resp.getAmount());

				sendPanel.txtAddressBalance.setText(NumberHelper.toGroupedAmount(acc.getBalance()));

				sendPanel.txtAddressBalance.setIcon(Icons.getIcon("check.png"));

			} catch (Exception e) {
				log.error("Error: ", e);
			}

		}).start();

	}

}
