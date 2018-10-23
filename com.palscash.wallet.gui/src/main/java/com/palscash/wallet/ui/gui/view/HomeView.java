package com.palscash.wallet.ui.gui.view;

import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.palscash.wallet.database.domain.Account;
import com.palscash.wallet.database.service.WalletService;
import com.palscash.wallet.ui.common.NumberHelper;
import com.palscash.wallet.ui.dialog.HomePanel;

@Component
public class HomeView {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private HomePanel homePanel;

	public JPanel get() {

		homePanel = new HomePanel();

		updateBalance();

		return homePanel;

	}

	public void updateBalance() {
		Account acc = WalletService.getAccounts().get(0);
		homePanel.txtBalance.setText(NumberHelper.toGroupedAmount(acc.getBalance()));
	}

}
