package com.palscash.wallet.ui.common;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


public class Alert {

	public static boolean confirm(String message) {

		JOptionPane optionPane = new JOptionPane(message, JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_CANCEL_OPTION, Icons.getIcon("alert/help.png"));

		JDialog dialog = optionPane.createDialog(message);
		dialog.setIconImages(Icons.getAppIcons());
		dialog.setVisible(true);
		for (Component c:dialog.getComponents()) {
		}

		if (optionPane.getValue() != null && optionPane.getValue() instanceof Integer) {
			return (Integer) optionPane.getValue() == JOptionPane.YES_OPTION;
		}

		return false;
	}

	public static void showLargeInfo(JFrame frame, String message) {

		JDialog dialog = new JDialog(frame, "Information");
		dialog.setModal(true);
		dialog.setLayout(new BorderLayout());

		JEditorPane ta = new JEditorPane();
		ta.setContentType("text/html");
		ta.setEditable(false);
		ta.setOpaque(true);
		ta.setText(message);

		dialog.setSize(600, 400);
		dialog.add(new JScrollPane(ta), BorderLayout.CENTER);

		JButton button = new JButton("Ok", Icons.getIcon("ok.png"));
		JPanel buttonPanel = new JPanel(new BorderLayout());
		buttonPanel.add(button, BorderLayout.EAST);
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		dialog.add(buttonPanel, BorderLayout.PAGE_END);
		dialog.setIconImages(Icons.getAppIcons());
		dialog.setVisible(true);

	}

	public static void warn(String message) {
		JOptionPane optionPane = new JOptionPane();
		optionPane.setMessage(message);
		optionPane.setIcon(Icons.getIcon("alert/warning.png"));
		JDialog dialog = optionPane.createDialog("Warning Message");
		dialog.setIconImages(Icons.getAppIcons());
		dialog.setVisible(true);
	}

	public static void info(String message) {
		JOptionPane optionPane = new JOptionPane();
		optionPane.setMessage(message);
		optionPane.setIcon(Icons.getIcon("alert/info.png"));
		JDialog dialog = optionPane.createDialog("Information Message");
		dialog.setIconImages(Icons.getAppIcons());
		dialog.setVisible(true);
	}

	public static void error(String message) {
		JOptionPane optionPane = new JOptionPane();
		optionPane.setMessage(message);
		optionPane.setIcon(Icons.getIcon("alert/warning.png"));
		JDialog dialog = optionPane.createDialog("Error Message");
		dialog.setIconImages(Icons.getAppIcons());
		dialog.setVisible(true);

	}
}