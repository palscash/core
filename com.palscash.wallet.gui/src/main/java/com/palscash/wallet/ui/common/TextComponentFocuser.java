package com.palscash.wallet.ui.common;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.text.JTextComponent;

public final class TextComponentFocuser extends FocusAdapter {
	public void focusGained(FocusEvent e) {
		if (e.getSource() instanceof JTextComponent) {
			JTextComponent textComponent = (JTextComponent) e.getSource();
			textComponent.selectAll();
		}
	}

}
