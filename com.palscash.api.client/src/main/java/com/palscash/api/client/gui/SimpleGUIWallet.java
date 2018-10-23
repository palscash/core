package com.palscash.api.client.gui;

import java.awt.BorderLayout;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.palscash.api.client.impl.PalsCashClientImpl;
import com.palscash.api.model.GetBalanceResponse;
import com.palscash.api.model.TransferResponse;
import com.palscash.common.crypto.Curves;
import com.palscash.common.crypto.PalsCashKeyPair;

public class SimpleGUIWallet {

	private static final Logger log = LoggerFactory.getLogger(SimpleGUIWallet.class);

	public static void main(String[] args) throws Exception {

		PalsCashClientImpl client = new PalsCashClientImpl();
		client.init();

		UIManager.setLookAndFeel(new NimbusLookAndFeel());

		JFrame frame = new JFrame("Simple PalsCash Wallet");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		frame.setLocationRelativeTo(null);

		frame.setLayout(new BorderLayout());

		JTextField hostNameTextField = new JTextField();
		{
			JPanel topPanel = new JPanel(new BorderLayout());
			topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			topPanel.add(new JLabel("API Endpoint"), BorderLayout.WEST);
			topPanel.add(hostNameTextField, BorderLayout.CENTER);
			hostNameTextField.setText("http://localhost:8080");
			frame.add(topPanel, BorderLayout.PAGE_START);
		}

		JTabbedPane tabs = new JTabbedPane();
		frame.add(tabs);

		{
			final GetBalancePanel panel = new GetBalancePanel();
			tabs.addTab("Get Balance", panel);

			panel.btnCheck.addActionListener(e -> {
				try {
					client.setHost(hostNameTextField.getText().trim());
					GetBalanceResponse balance = client.getBalance( panel.txtAddress.getText().trim());
					panel.txtBalance.setText(toGroupedAmount(new BigDecimal(balance.getAmount())));
				} catch (Exception e1) {
					log.error("Error: ", e1);
				}
			});

		}

		{
			TransferPanel panel = new TransferPanel();
			tabs.addTab("Transfer", new JScrollPane(panel));

			panel.txtPrivateKey.addActionListener(e -> {

				PalsCashKeyPair kp = PalsCashKeyPair.createFromPrivateKeyBase58(panel.txtPrivateKey.getText().trim());

				panel.txtFrom.setText(kp.getPalsCashAccount().getUuid());

				try {
					client.setHost(hostNameTextField.getText().trim());
					GetBalanceResponse balance = client.getBalance(panel.txtFrom.getText().trim());
					panel.txtBalance.setText(toGroupedAmount(new BigDecimal(balance.getAmount())));
				} catch (Exception e1) {
					log.error("Error: ", e1);
				}

			});
			
			panel.btnCancel.addActionListener(e->{
				panel.txtAmount.setText("");
				panel.txtBalance.setText("");
				panel.txtFrom.setText("");
				panel.txtMemo.setText("");
				panel.txtPrivateKey.setText("");
				panel.txtTo.setText("");
			});
			
			panel.btnTransfer.addActionListener(e-> {
				try {
					client.setHost(hostNameTextField.getText().trim());
					TransferResponse transfer = client.transfer(panel.txtPrivateKey.getText().trim(), panel.txtTo.getText().trim(),new BigDecimal( panel.txtAmount.getText().trim() ), panel.txtMemo.getText().trim());
					JOptionPane.showMessageDialog(null, transfer.getTransactionUuid());
				} catch (Exception e1) {
					log.error("Error: ", e1);
				}
			});
		}

		tabs.addTab("Look up transaction", new LookupTransactionPanel());

		frame.setVisible(true);

	}

	public static String toGroupedAmount(BigDecimal amount) {
		DecimalFormat df = new DecimalFormat("###,###.########");
		return df.format(amount);

	}

}
