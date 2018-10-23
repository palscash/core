package com.palscash.wallet.ui.gui.view;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.io.ByteArrayOutputStream;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.palscash.wallet.database.service.WalletService;
import com.palscash.wallet.ui.common.QTextComponentContextMenu;
import com.palscash.wallet.ui.common.TextComponentFocuser;
import com.palscash.wallet.ui.dialog.ReceivePanel;

import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;

@Component
public class ReceiveView {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@SuppressWarnings("serial")
	public JPanel get() {

		ReceivePanel panel = new ReceivePanel();

		try {

			final String publicKey58 = WalletService.getAccountService().getAll().get(0).getUuid();

			panel.txtAddress.setText(publicKey58);

			new QTextComponentContextMenu(panel.txtAddress);
			panel.txtAddress.addFocusListener(new TextComponentFocuser());
			panel.txtAddress.setCaretPosition(0);

			panel.panelCenter.setLayout(new BorderLayout());

			panel.panelCenter.add(new JPanel() {

				@Override
				protected void paintComponent(Graphics g) {

					ByteArrayOutputStream os = QRCode.from(publicKey58).withSize(getWidth(), getHeight())
							.to(ImageType.PNG).stream();

					ImageIcon imageData = new ImageIcon(os.toByteArray());

					g.drawImage(imageData.getImage(), (getWidth() - imageData.getIconWidth()) / 2,
							(getHeight() - imageData.getIconHeight()) / 2, null);

				}

			}, BorderLayout.CENTER);

		} catch (Exception e) {
			log.error("Error: ", e);
		}

		// start background transfer listener

		return panel;

	}

}
