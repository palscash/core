package com.palscash.wallet.ui.gui.view;

import java.awt.Dimension;

import javax.swing.JDialog;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.palscash.wallet.database.domain.Transfer;
import com.palscash.wallet.ui.common.QTextComponentContextMenu;
import com.palscash.wallet.ui.common.SwingHelper;
import com.palscash.wallet.ui.dialog.TxDetailsPanel;
import com.palscash.wallet.ui.gui.MainWindow;

@Component
public class TxDetailsViewPopup {

	@Autowired
	private MainWindow win;

	public void show(Transfer tx) {

		TxDetailsPanel panel = new TxDetailsPanel();
		panel.setMaximumSize(new Dimension(400, 400));

		panel.from.setText(tx.getFromAddress());
		panel.to.setText(tx.getToAddress());
		panel.amount.setText(tx.getAmount().toPlainString());
		panel.fee.setText(tx.getFee());
		panel.memo.setText(tx.getMemo());
		panel.uuid.setText(tx.getUuid());
		panel.date.setText(DateFormatUtils.formatUTC(tx.getDate(), DateFormatUtils.SMTP_DATETIME_FORMAT.getPattern()));

		new QTextComponentContextMenu(panel.from);
		new QTextComponentContextMenu(panel.to);
		new QTextComponentContextMenu(panel.amount);
		new QTextComponentContextMenu(panel.fee);
		new QTextComponentContextMenu(panel.memo);
		new QTextComponentContextMenu(panel.date);
		new QTextComponentContextMenu(panel.uuid);
		
		JDialog dialog = SwingHelper.dialog(win.getFrame(), panel);
		dialog.setResizable(false);

		SwingHelper.installEscapeCloseOperation(dialog);

		panel.btnClose.addActionListener(e -> {
			dialog.dispose();
		});

		dialog.setVisible(true);
	}

}
