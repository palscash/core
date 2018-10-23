package com.palscash.wallet.ui.start;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pushingpixels.substance.api.skin.SubstanceMarinerLookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceOfficeSilver2007LookAndFeel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.palscash.wallet.database.service.WalletService;
import com.palscash.wallet.ui.common.Fonts;
import com.palscash.wallet.ui.gui.MainWindow;

@Component
public class DesktopWallet {

	private static final Log log = LogFactory.getLog(DesktopWallet.class);

	@Autowired
	private MainWindow mainWindow;

	public static void main(String[] args) throws Exception {

		SwingUtilities.invokeLater(() -> {

			try {

				ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
						"classpath:/com/palscash/wallet/spring-context.xml");

				ctx.start();

				ctx.getBean(DesktopWallet.class).start();

			} catch (Exception e) {
				log.error("Error: ", e);
			}

		});

	}

	public void start() throws Exception {

		if (SystemUtils.IS_OS_WINDOWS) {

			// UIManager.setLookAndFeel(new SubstanceMarinerLookAndFeel());
			// UIManager.setLookAndFeel(new SubstanceGraphiteGlassLookAndFeel() );
			// UIManager.setLookAndFeel(new SubstanceMagellanLookAndFeel());

			UIManager.setLookAndFeel(new SubstanceOfficeSilver2007LookAndFeel());

			// JFrame.setDefaultLookAndFeelDecorated(true);
			// JDialog.setDefaultLookAndFeelDecorated(true);

		} else if (SystemUtils.IS_OS_LINUX) {

			System.setProperty("sun.java2d.opengl", "true");

			UIManager.setLookAndFeel(new SubstanceMarinerLookAndFeel());

		} else if (SystemUtils.IS_OS_MAC_OSX) {

			System.setProperty("apple.laf.useScreenMenuBar", "true");
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", "PalsCash Wallet");

			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		} else {

			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		}

		// Override fonts
		if (SystemUtils.IS_OS_WINDOWS) {
			try {
				setUIFont(new javax.swing.plaf.FontUIResource(Fonts.getMainFont()));
			} catch (Exception e) {
				log.error("Error: ", e);
			}
		}

		this.mainWindow.start();

	}

	private static void setUIFont(javax.swing.plaf.FontUIResource f) {
		java.util.Enumeration keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value != null && value instanceof javax.swing.plaf.FontUIResource)
				UIManager.put(key, f);
		}
	}

	public static void exit() {
		try {
			WalletService.close();
		} catch (Exception e) {
			log.error("Error: ", e);
		}
		System.exit(0);
	}

}
