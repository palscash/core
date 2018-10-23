package com.palscash.wallet.ui.common;

import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public final class QMenuItem extends JMenuItem {

	private QMenuItem() {
	}

	private QMenuItem(String text) {
	}

	public static JMenuItem getItem(String itemName) {
		return getItem(itemName, null);
	}

	/**
	 * @param itemName
	 * @param icon
	 * @return
	 */
	public static JMenuItem getItem(String itemName, String icon) {

		final JMenuItem menuItem = new JMenuItem(itemName);

		if (icon != null) {
			menuItem.setIcon(Icons.getIcon(icon));
		}

		return menuItem;

	}

}