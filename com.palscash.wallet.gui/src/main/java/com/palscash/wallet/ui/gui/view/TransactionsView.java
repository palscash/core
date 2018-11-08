package com.palscash.wallet.ui.gui.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;

import org.palscash.network.api.client.PalsCashClient;
import org.palscash.network.api.model.TransactionInfo;
import org.palscash.network.api.model.TransactionListResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.palscash.wallet.database.domain.Account;
import com.palscash.wallet.database.domain.Transfer;
import com.palscash.wallet.database.service.WalletService;
import com.palscash.wallet.ui.common.Alert;
import com.palscash.wallet.ui.common.QTextComponentContextMenu;
import com.palscash.wallet.ui.common.SwingHelper;
import com.palscash.wallet.ui.dialog.TransactionsPanel;

@Component
public class TransactionsView {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${app.url.online.faq}")
	private String appUrlOnlineFaq;

	@Value("${app.url.online.help}")
	private String appUrlOnlineHelp;

	@Autowired
	private PalsCashClient client;

	@Autowired
	private TxDetailsViewPopup detailsPopup;

	@Autowired
	@Qualifier("default-task-executor")
	private TaskExecutor taskExecutor;

	private TransactionsPanel homePanel;

	public JPanel get() {

		homePanel = new TransactionsPanel();

		SwingHelper.setFontForJText(homePanel.events);

		new QTextComponentContextMenu(homePanel.events);

		// Show recent transactions

		populateTransactionList();

		homePanel.btnSyncTransactions.addActionListener(e -> {
			syncTransactions();
		});

		homePanel.events.addHyperlinkListener(e -> {
			getTransferDetails(e);
		});

		return homePanel;

	}

	private void syncTransactions() {
		homePanel.btnSyncTransactions.setEnabled(false);

		taskExecutor.execute(() -> {

			log.debug("Get recent transactions");

			Account acc = WalletService.getAccounts().get(0);

			try {

				TransactionListResponse transactionsFromAccount = client.getTransactionsFromAccount(acc.getUuid());

				TransactionListResponse transactionsToAccount = client.getTransactionsToAccount(acc.getUuid());

				List<String> txUuids = new ArrayList<>(transactionsFromAccount.getTransactions());
				txUuids.addAll(transactionsToAccount.getTransactions());

				processTx(acc, txUuids);

				populateTransactionList();

			} catch (Exception ex) {
				log.error("Error: ", ex);
			} finally {
				homePanel.btnSyncTransactions.setEnabled(true);
			}

		});
	}

	private void processTx(Account acc, List<String> txUuids) {

		for (String tx : txUuids) {

			Transfer transfer = WalletService.getTransferService().getByUuid(tx);

			if (transfer == null) {
				Transfer t = new Transfer();
				t.setUuid(tx);
				t.setAccount(acc);
				WalletService.getTransferService().save(t);
			}

		}

	}

	private void populateTransactionList() {

		Page<Transfer> txs = WalletService.getTransferService().getAllByAccount(WalletService.getAccounts().get(0), 0, 200);

		log.debug("Loaded transfers: " + txs.getNumberOfElements());

		StringBuilder sb = new StringBuilder("<html><body>");
		sb.append("<table border='0' width='100%'>");

		sb.append("<tr style='background:#333;color:white;'><th align='right'>Transaction</th><th  align='left'>Date</th><th  align='left'>Amount</th></tr>");

		for (Transfer tx : txs) {

			sb.append("<tr>");

			sb.append("<td nowrap align='center'>");

			sb.append("<a href='" + tx.getUuid() + "'>" + tx.getUuid() + "</a>");

			sb.append("</td><td  style='background:white;color:black;'>");

			if (tx.getDate() != null) {
				sb.append(new SimpleDateFormat("dd MMM yy hh:mm:ss a").format(tx.getDate()));
			} else {
				sb.append("-");
			}

			sb.append("</td><td>");

			if (tx.getAmount() != null) {
				sb.append(tx.getAmount().toPlainString());
			} else {
				sb.append("-");
			}

			sb.append("</td>");

			sb.append("</tr>");
		}

		sb.append("</table>");
		sb.append("</body></html>");

		homePanel.events.setText(sb.toString());

	}

	private void getTransferDetails(HyperlinkEvent e) {

		if (e.getEventType().equals(EventType.ACTIVATED)) {

			String txUuid = e.getDescription();

			try {

				Transfer tx = WalletService.getTransferService().getByUuid(txUuid);

				if (tx.getAmount() == null) {

					log.debug("Get tx details from network: " + txUuid);

					TransactionInfo details = client.getTransactionDetails(txUuid);

					if (details != null && details.getTransactionUuid() != null) {

						tx.setFee(details.getFee());
						tx.setFromAddress(details.getFromAddress());
						tx.setToAddress(details.getToAddress());
						tx.setMemo(details.getMemo());
						tx.setUuid(txUuid);
						tx.setDate(details.getDate());
						tx.setAmount(details.getAmount());
						tx.setAccount(WalletService.getAccounts().get(0));

						log.debug("Tx: " + tx);

						WalletService.getTransferService().save(tx);

					}
				}

				detailsPopup.show(tx);

			} catch (Exception e1) {
				log.error("Error: ", e1);
				Alert.error("Cannot retrieve transaction details");
			}
		}
	}

}
