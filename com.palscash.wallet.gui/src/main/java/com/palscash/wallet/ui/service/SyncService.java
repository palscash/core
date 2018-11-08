package com.palscash.wallet.ui.service;

import java.awt.TrayIcon.MessageType;
import java.math.BigDecimal;

import javax.annotation.PostConstruct;
import javax.swing.SwingUtilities;

import org.palscash.network.api.client.PalsCashClient;
import org.palscash.network.api.model.GetBalanceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.palscash.wallet.database.domain.Account;
import com.palscash.wallet.database.service.WalletService;
import com.palscash.wallet.ui.common.SwingHelper;
import com.palscash.wallet.ui.gui.MainWindow;
import com.palscash.wallet.ui.gui.view.HomeView;

@Service
public class SyncService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${api.central.url}")
	private String apiCentralUrl;

	@Value("${api.url.address.new}")
	private String apiUrlAddressNew;

	@Autowired
	private MainWindow mainWindow;

	@Autowired
	private PalsCashClient client;
	
	@Autowired
	private HomeView homeView;

	@PostConstruct
	public void init() {
	}

	public void sync(Runnable postSyncFunction) {

		SwingHelper.async(mainWindow.getFrame(), "Synchronising wallet", () -> {

			try {

				// Get balance
				Account account = WalletService.getAccounts().get(0);

				GetBalanceResponse balance = client.getBalance(account.getUuid());

				BigDecimal amount = new BigDecimal(balance.getAmount());

				if (amount.compareTo(account.getBalanceAsBigDecimal()) != 0) {

					account = WalletService.getAccountService().updateAccountBalance(account, balance.getAmount());

					mainWindow.showSystemTrayMessage(MessageType.INFO, "Wallet updated");
					
					homeView.updateBalance();
					
				}

				if (postSyncFunction != null) {
					SwingUtilities.invokeLater(() -> {
						postSyncFunction.run();
					});
				}

			} catch (Exception e) {
				log.error("Error: ", e);
			}
		});
	}

}
