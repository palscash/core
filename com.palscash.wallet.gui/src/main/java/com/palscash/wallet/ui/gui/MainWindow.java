package com.palscash.wallet.ui.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.palscash.wallet.database.service.WalletService;
import com.palscash.wallet.ui.action.CreateNewWallet;
import com.palscash.wallet.ui.action.OpenWallet;
import com.palscash.wallet.ui.action.RestoreWallet;
import com.palscash.wallet.ui.common.Fonts;
import com.palscash.wallet.ui.common.Icons;
import com.palscash.wallet.ui.common.QTextComponentContextMenu;
import com.palscash.wallet.ui.common.SwingHelper;
import com.palscash.wallet.ui.component.AnimatedNetworkPanel;
import com.palscash.wallet.ui.dialog.AboutDialog;
import com.palscash.wallet.ui.dialog.FreshStart;
import com.palscash.wallet.ui.gui.view.HomeView;
import com.palscash.wallet.ui.gui.view.ReceiveView;
import com.palscash.wallet.ui.gui.view.SendView;
import com.palscash.wallet.ui.gui.view.TransactionsView;
import com.palscash.wallet.ui.service.SyncService;
import com.palscash.wallet.ui.start.DesktopWallet;

@Component
public class MainWindow {

	private static final Log log = LogFactory.getLog(MainWindow.class);

	private JFrame frame;

	public static enum View {
		HOME, SEND, RECEIVE, TRANSACTIONS
	}

	private View view = View.HOME;

	@Autowired
	private OpenWallet openWallet;

	@Autowired
	private SyncService syncService;

	private JPanel mainPanel;

	private TrayIcon trayIcon;

	private JLabel statusLabel;

	@Autowired
	private CreateNewWallet createWallet;

	@Autowired
	private RestoreWallet restoreWallet;

	@Autowired
	private SendView sendView;

	@Autowired
	private TransactionsView txView;

	@Autowired
	private HomeView homeView;

	@Autowired
	private ReceiveView receiveView;

	private JToolBar toolbar;

	private AnimatedNetworkPanel animatedPanel;

	public void start() {

		frame = new JFrame();
		frame.setLayout(new BorderLayout());

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension size = new Dimension(750, 742);
		frame.setMinimumSize(size);
		frame.setSize(size);
		frame.setLocationRelativeTo(null);
		frame.setTitle("PalsCash");

		animatedPanel = new AnimatedNetworkPanel();

		mainPanel = new JPanel(new BorderLayout());

		frame.add(mainPanel, BorderLayout.CENTER);

		addComponentToFrame(animatedPanel);

		List<Image> images = Arrays.asList( //
				new ImageIcon(this.getClass().getResource("/icons/app/128.png")).getImage(), //
				new ImageIcon(this.getClass().getResource("/icons/app/256.png")).getImage(), //
				new ImageIcon(this.getClass().getResource("/icons/app/32.png")).getImage(), //
				new ImageIcon(this.getClass().getResource("/icons/app/48.png")).getImage(), //
				new ImageIcon(this.getClass().getResource("/icons/app/64.png")).getImage(), //
				new ImageIcon(this.getClass().getResource("/icons/app/96.png")).getImage() //
		);
		frame.setIconImages(images);

		new Thread(() -> {

			while (true) {

				try {
					animatedPanel.repaint();
					Thread.sleep(10);
				} catch (InterruptedException e) {
					log.error("Error: ", e);
				}
			}
		}).start();

		setupMainMenu();
		setupSystemTray();
		setupStatusBar();

		frame.setVisible(true);

		FreshStart freshStartPanel = new FreshStart();

		JDialog startDialog = SwingHelper.dialog(frame, freshStartPanel);
		startDialog.setTitle("Welcome to PalsCash!");

		freshStartPanel.btnLoadExistingWallet.addActionListener(e -> {
			openWallet.open(startDialog, startDialog);
		});

		freshStartPanel.btnCreateNewWallet.addActionListener(e -> {
			startDialog.dispose();
			createWallet.create(startDialog);
		});

		SwingHelper.installEscapeCloseOperation(startDialog);
		startDialog.setVisible(true);

	}

	private void initUI() {

		showHomeScreen();
		setupToolbar();

	}

	private void setupToolbar() {

		if (toolbar == null) {

			toolbar = new JToolBar();

			JButton btnHome = new JButton("Home", new ImageIcon(this.getClass().getResource("/icons/tab_balances_32.png")));
			btnHome.setVerticalTextPosition(SwingConstants.BOTTOM);
			btnHome.setHorizontalTextPosition(SwingConstants.CENTER);
			toolbar.add(btnHome);
			btnHome.addActionListener(e -> {
				showHomeScreen();
			});

			JButton btnSend = new JButton("Send", new ImageIcon(this.getClass().getResource("/icons/tab_send_32.png")));
			btnSend.setVerticalTextPosition(SwingConstants.BOTTOM);
			btnSend.setHorizontalTextPosition(SwingConstants.CENTER);
			toolbar.add(btnSend);

			btnSend.addActionListener(e -> {
				showSendScreen();
			});

			JButton btnReceive = new JButton("Receive", new ImageIcon(this.getClass().getResource("/icons/tab_receive_32.png")));
			btnReceive.setVerticalTextPosition(SwingConstants.BOTTOM);
			btnReceive.setHorizontalTextPosition(SwingConstants.CENTER);
			toolbar.add(btnReceive);

			btnReceive.addActionListener(e -> {
				showReceiveScreen();
			});

			JButton btnTransactions = new JButton("Transactions", new ImageIcon(this.getClass().getResource("/icons/transactions.png")));
			btnTransactions.setVerticalTextPosition(SwingConstants.BOTTOM);
			btnTransactions.setHorizontalTextPosition(SwingConstants.CENTER);
			btnTransactions.addActionListener(e -> {
				showTransactionsScreen();
			});
			toolbar.add(btnTransactions);

			/*
			 * 
			 * JButton btnAddressBook = new JButton("Address Book", new
			 * ImageIcon(this.getClass().getResource("/icons/address_book.png"))
			 * ); btnAddressBook.setVerticalTextPosition(SwingConstants.BOTTOM);
			 * btnAddressBook.setHorizontalTextPosition(SwingConstants.CENTER);
			 * btnAddressBook.addActionListener(e -> { showAddressBook(); });
			 * toolbar.add(btnAddressBook);
			 */
			toolbar.add(Box.createHorizontalGlue());

			JButton btnSync = new JButton("Synchronise", new ImageIcon(this.getClass().getResource("/icons/sync_32.png")));
			btnSync.setVerticalTextPosition(SwingConstants.BOTTOM);
			btnSync.setHorizontalTextPosition(SwingConstants.CENTER);
			btnSync.addActionListener(e -> {

				syncService.sync(() -> {
					updateUI();
				});

			});
			toolbar.add(btnSync);

			frame.add(toolbar, BorderLayout.PAGE_START);

		}
	}

	public void updateUI() {
		if (view == View.HOME) {
			showHomeScreen();
		} else if (view == View.RECEIVE) {
			showReceiveScreen();
		} else if (view == View.SEND) {
			showSendScreen();
		} else if (view == View.TRANSACTIONS) {
			showTransactionsScreen();
		}
	}

	private void setupStatusBar() {

		JPanel statusPanel = new JPanel(new BorderLayout());

		statusLabel = new JLabel("Connecting to network...");
		statusLabel.setIcon(new ImageIcon(this.getClass().getResource("/icons/ajax-loader.gif")));
		statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		statusPanel.add(statusLabel);

		this.frame.add(statusPanel, BorderLayout.PAGE_END);

	}

	private void setupSystemTray() {

		if (!SystemTray.isSupported()) {
			System.out.println("SystemTray is not supported");
			return;
		}
		trayIcon = new TrayIcon(Icons.load("app/64.png"), "PalsCash");
		trayIcon.setImageAutoSize(true);

		final SystemTray tray = SystemTray.getSystemTray();

		final PopupMenu popup = new PopupMenu();
		trayIcon.setPopupMenu(popup);

		MenuItem newWalletItem = new MenuItem("Send");
		newWalletItem.addActionListener(e -> {
			showSendScreen();
			SwingHelper.bringToFront(frame);
		});
		popup.add(newWalletItem);

		MenuItem openWalletItem = new MenuItem("Receive");
		openWalletItem.addActionListener(e -> {
			showReceiveScreen();
			SwingHelper.bringToFront(frame);
		});
		popup.add(openWalletItem);

		popup.addSeparator();

		MenuItem exitItem = new MenuItem("Exit");
		exitItem.addActionListener(e -> {
			DesktopWallet.exit();
		});
		popup.add(exitItem);

		try {
			tray.add(trayIcon);
		} catch (Exception e) {
			System.out.println("TrayIcon could not be added.");
		}

	}

	private void setupMainMenu() {

		JMenuBar mb = new JMenuBar();

		UIManager.put("Menu.font", Fonts.getMainFont());

		{
			JMenu menu = new JMenu("Wallet");
			mb.add(menu);

			JMenuItem newWalletMenu = new JMenuItem("New wallet", 'N');
			newWalletMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
			menu.add(newWalletMenu);
			newWalletMenu.addActionListener(e -> {
				createWallet.create(frame);
			});

			menu.addSeparator();

			JMenuItem menuOpenWallet = new JMenuItem("Open wallet");

			if (SystemUtils.IS_OS_MAC_OSX == false) {
				menuOpenWallet.setIcon(Icons.getIcon("folder.png"));
			}

			menuOpenWallet.setMnemonic('O');
			menuOpenWallet.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
			menuOpenWallet.addActionListener(e -> {
				openWallet.open(frame, null);
			});
			menu.add(menuOpenWallet);

			JMenuItem menuRestoreWallet = new JMenuItem("Restore wallet");
			menuRestoreWallet.setMnemonic('R');
			menuRestoreWallet.addActionListener(e -> {
				restoreWallet.restore(frame);
			});
			menu.add(menuRestoreWallet);

			menu.addSeparator();

			JMenuItem itemCloseWallet = new JMenuItem("Close wallet", 'c');
			itemCloseWallet.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

			itemCloseWallet.addActionListener(e -> {
				closeWallet();
			});
			menu.add(itemCloseWallet);
			menu.addSeparator();

			JMenuItem exit = new JMenuItem("Exit", 'x');
			exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

			exit.addActionListener(e -> {
				DesktopWallet.exit();
			});
			menu.add(exit);
		}

		/*
		{
			JMenu menu = new JMenu("Help");
			mb.add(menu);

			JMenuItem menuOnlineHelp = new JMenuItem("Online help");

			if (SystemUtils.IS_OS_MAC_OSX == false) {
				menuOnlineHelp.setIcon(Icons.getIcon("menu/information.png"));
			}

			menu.add(menuOnlineHelp);

			menuOnlineHelp.addActionListener(e -> {
				// TODO SwingHelper.browseToUrl(appUrlOnlineHelp);
			});

			JMenuItem menuFaq = new JMenuItem("FAQ");
			menu.add(menuFaq);
			menuFaq.addActionListener(e -> {
				SwingHelper.browseToUrl("https://palscash.com/faq.html");
			});
			menu.add(menuFaq);

			menu.addSeparator();

			JMenuItem aboutMenu = new JMenuItem("About");

			if (SystemUtils.IS_OS_MAC_OSX == false) {
				aboutMenu.setIcon(Icons.getIcon("app/16.png"));
			}

			aboutMenu.addActionListener(e -> {
				AboutDialog aboutDialogPanel = new AboutDialog();
				JDialog aboutDialog = SwingHelper.dialog(frame, aboutDialogPanel);
				aboutDialogPanel.btnOk.addActionListener(ex -> {
					aboutDialog.dispose();
				});
				new QTextComponentContextMenu(aboutDialogPanel.text);
				aboutDialog.setLocationRelativeTo(null);
				SwingHelper.installEscapeCloseOperation(aboutDialog);
				// TODO aboutDialog.setTitle("About " +
				// i18n.get("app.window.title"));
				aboutDialog.setVisible(true);
			});
			menu.add(aboutMenu);
		}
*/
		frame.setJMenuBar(mb);

	}

	public void loadWallet() {

		SwingUtilities.invokeLater(() -> {
			initUI();
		});

	}

	public void setTitle(String text) {
		if (text == null) {
			frame.setTitle("PalsCash");
		} else {
			frame.setTitle("PalsCash (" + text + ")");
		}
	}

	public void setStatusMessage(String msg, Icon icon) {
		if (this.statusLabel != null) {
			this.statusLabel.setText(msg);
			this.statusLabel.setIcon(icon);
		}
	}

	public Window getFrame() {
		return this.frame;
	}

	private void addComponentToFrame(JComponent c) {

		mainPanel.removeAll();
		mainPanel.doLayout();
		mainPanel.revalidate();
		mainPanel.repaint();

		mainPanel.add(c, BorderLayout.CENTER);

	}

	public void showSystemTrayMessage(MessageType type, String msg) {
		trayIcon.displayMessage("PalsCash Message", msg, type);
	}

	private void showSendScreen() {
		this.view = View.SEND;
		JScrollPane scroll = new JScrollPane(sendView.get());
		SwingHelper.updateScrollPane(scroll);
		this.addComponentToFrame(scroll);
	}

	private void showTransactionsScreen() {
		this.view = View.TRANSACTIONS;
		JScrollPane scroll = new JScrollPane(txView.get());
		SwingHelper.updateScrollPane(scroll);
		this.addComponentToFrame(scroll);
	}

	/**
	 * Open 'Receive' screen
	 */
	private void showReceiveScreen() {
		this.view = View.RECEIVE;
		addComponentToFrame(receiveView.get());
	}

	public void showHomeScreen() {
		this.view = View.HOME;
		addComponentToFrame(this.homeView.get());
		setupToolbar();
	}

	private void closeWallet() {

		WalletService.close();

		if (toolbar != null) {
			toolbar.getParent().remove(toolbar);
		}

		toolbar = null;

		addComponentToFrame(animatedPanel);

		setTitle("PalsCash");
	}

}
