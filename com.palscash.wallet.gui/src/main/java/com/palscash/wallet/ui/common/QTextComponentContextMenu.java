package com.palscash.wallet.ui.common;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import org.apache.commons.lang3.StringUtils;

public final class QTextComponentContextMenu extends MouseAdapter {

	private JTextComponent component;

	private UndoManager undo;

	private AdditionalMenu additionalMenu;

	public static interface AdditionalMenu {
		void process(JPopupMenu menu);
	}

	public QTextComponentContextMenu(JTextComponent component, AdditionalMenu menu) {
		this(component);
		this.additionalMenu = menu;
	}

	public QTextComponentContextMenu(JTextComponent component) {

		this.component = component;

		this.initUndoManager();

		component.addMouseListener(this);

	}

	private void initUndoManager() {

		undo = new UndoManager();

		final Document doc = component.getDocument();

		// Listen for undo and redo events
		doc.addUndoableEditListener(new UndoableEditListener() {
			public void undoableEditHappened(UndoableEditEvent evt) {
				undo.addEdit(evt.getEdit());
			}
		});

		// Create an undo action and add it to the text component
		component.getActionMap().put("Undo", new AbstractAction("Undo") {
			public void actionPerformed(ActionEvent evt) {
				try {
					if (undo.canUndo()) {
						undo.undo();
					}
				} catch (CannotUndoException e) {
				}
			}
		});

		// Bind the undo action to ctl-Z
		component.getInputMap().put(KeyStroke.getKeyStroke("control Z"), "Undo");

		// Create a redo action and add it to the text component
		component.getActionMap().put("Redo", new AbstractAction("Redo") {
			public void actionPerformed(ActionEvent evt) {
				try {
					if (undo.canRedo()) {
						undo.redo();
					}
				} catch (CannotRedoException e) {
				}
			}
		});

		// Bind the redo action to ctl-Y
		component.getInputMap().put(KeyStroke.getKeyStroke("control Y"), "Redo");

	}

	private void showMenu(MouseEvent e) {

		this.component.requestFocus();

		final JPopupMenu menu = new JPopupMenu();

		final boolean isTextSelected = StringUtils.isNotBlank(component.getSelectedText());
		final boolean isText = StringUtils.isNotBlank(component.getText());
		final boolean isEditable = component.isEditable();
		final boolean isEnabled = component.isEnabled();

		JMenuItem item = null;
		KeyStroke stroke = null;

		// Cut
		item = QMenuItem.getItem("Cut");
		stroke = KeyStroke.getKeyStroke("control X");
		item.setAccelerator(stroke);
		item.setIcon(Icons.getIcon("textMenu/cut.png"));
		item.setEnabled(isTextSelected && isEditable && isEnabled);
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				component.cut();
			}
		});

		menu.add(item);

		// Copy
		item = QMenuItem.getItem("Copy");
		stroke = KeyStroke.getKeyStroke("control C");
		item.setAccelerator(stroke);
		item.setIcon(Icons.getIcon("textMenu/copy.png"));
		item.setEnabled(isTextSelected);
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				component.copy();
			}
		});

		menu.add(item);

		// Paste
		item = QMenuItem.getItem("Paste");
		stroke = KeyStroke.getKeyStroke("control V");
		item.setAccelerator(stroke);
		item.setIcon(Icons.getIcon("textMenu/paste.png"));
		item.setEnabled(isEditable && isEnabled);
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				component.paste();
			}
		});

		menu.add(item);

		menu.addSeparator();

		// Undo
		item = QMenuItem.getItem("Undo");
		stroke = KeyStroke.getKeyStroke("control Z");
		item.setAccelerator(stroke);
		item.setIcon(Icons.getIcon("textMenu/arrow-undo.png"));
		item.setEnabled(undo.canUndo() && isEditable && isEnabled);
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				undo.undo();
			}
		});

		menu.add(item);

		// Redo
		item = QMenuItem.getItem("Redo");
		stroke = KeyStroke.getKeyStroke("control Y");
		item.setAccelerator(stroke);
		item.setIcon(Icons.getIcon("textMenu/arrow-redo.png"));
		item.setEnabled(undo.canRedo() && isEditable && isEnabled);
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				undo.redo();
			}
		});

		menu.add(item);

		menu.addSeparator();

		// Select All
		item = QMenuItem.getItem("Select All");
		stroke = KeyStroke.getKeyStroke("control A");
		item.setAccelerator(stroke);
		item.setEnabled(isText && isEnabled);
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				component.selectAll();
			}
		});

		menu.add(item);

		// additional menu
		if (additionalMenu != null) {
			additionalMenu.process(menu);
		}

		menu.show(e.getComponent(), e.getX(), e.getY());

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.isPopupTrigger()) {
			this.showMenu(e);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.isPopupTrigger())
			this.showMenu(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.isPopupTrigger())
			this.showMenu(e);
	}

}