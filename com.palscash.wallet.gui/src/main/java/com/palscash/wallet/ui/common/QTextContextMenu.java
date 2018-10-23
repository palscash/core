package com.palscash.wallet.ui.common;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

import org.apache.commons.lang3.StringUtils;

public class QTextContextMenu {

	public static interface QTextComponent {

		void requestFocus();

		String getSelectedText();

		boolean isBlank();

		boolean isEditable();

		boolean isEnabled();

		void cutSelection();

		void copySelection();

		void paste();

		void selectAll();

	}

	private QTextComponent component;
	private JMenuItem cutItem;
	private JMenuItem copyItem;
	private JMenuItem pasteItem;
	private JMenuItem selectAllItem;

	public QTextContextMenu(QTextComponent textComponent) {
		super();
		this.component = textComponent;
	}

	@SuppressWarnings("serial")
	public JPopupMenu getPopupMenu() {

		JPopupMenu popupMenu = new JPopupMenu() {

			@Override
			public void show(Component invoker, int x, int y) {
				updateMenus();
				super.show(invoker, x, y);
			}

		};

		KeyStroke stroke = null;

		cutItem = QMenuItem.getItem("Cut");
		stroke = KeyStroke.getKeyStroke("control X");
		cutItem.setAccelerator(stroke);
		cutItem.setIcon(Icons.getIcon("textMenu/cut.png"));
		cutItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				component.cutSelection();
			}
		});

		popupMenu.add(cutItem);

		copyItem = QMenuItem.getItem("Copy");
		stroke = KeyStroke.getKeyStroke("control C");
		copyItem.setAccelerator(stroke);
		copyItem.setIcon(Icons.getIcon("textMenu/copy.png"));
		copyItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				component.copySelection();
			}
		});

		popupMenu.add(copyItem);

		pasteItem = QMenuItem.getItem("Paste");
		stroke = KeyStroke.getKeyStroke("control V");
		pasteItem.setAccelerator(stroke);
		pasteItem.setIcon(Icons.getIcon("textMenu/paste.png"));
		pasteItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				component.paste();
			}
		});

		popupMenu.add(pasteItem);

		popupMenu.addSeparator();

		selectAllItem = QMenuItem.getItem("Select All");
		stroke = KeyStroke.getKeyStroke("control A");
		selectAllItem.setAccelerator(stroke);
		selectAllItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				component.selectAll();
			}
		});

		popupMenu.add(selectAllItem);

		return popupMenu;

	}

	protected void updateMenus() {

		final boolean isTextSelected = StringUtils.isNotBlank(component.getSelectedText());
		final boolean hasText = false == component.isBlank();
		final boolean isEditable = component.isEditable();
		final boolean isEnabled = component.isEnabled();

		cutItem.setEnabled(isTextSelected && isEditable && isEnabled);
		copyItem.setEnabled(isTextSelected);
		selectAllItem.setEnabled(hasText && isEnabled);
		pasteItem.setEnabled(isEditable && isEnabled);

	}

}
