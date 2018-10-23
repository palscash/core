package com.palscash.wallet.ui.gui.view;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.palscash.wallet.ui.common.Icons;

@Component
public class Tabs {

	@Autowired
	private SendView sendView;

	@Autowired
	private HomeView homeView;

	@Autowired
	private ReceiveView receiveView;

	private JPanel panel;

	public JComponent getPanel() {

		if (panel == null) {
			init();
		}

		return panel;
	}

	private void init() {

		panel = new JPanel(new BorderLayout());

		JTabbedPane tabs = new JTabbedPane(JTabbedPane.BOTTOM);

		tabs.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				System.out.println("Tab: " + tabs.getSelectedIndex());
			}
		});

		tabs.addTab("Balance", Icons.getIcon("tab_balances_32.png"), homeView.get());
		tabs.addTab("Send", Icons.getIcon("tab_send_32.png"), sendView.get());
		tabs.addTab("Receive", Icons.getIcon("tab_receive_32.png"), receiveView.get());

		panel.add(tabs, BorderLayout.CENTER);

	}

}
