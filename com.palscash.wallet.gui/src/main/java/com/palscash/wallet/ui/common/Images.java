package com.palscash.wallet.ui.common;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Images {

	private static final Log log = LogFactory.getLog(Images.class);

	public static BufferedImage load(String name) {

		try {
			return ImageIO.read(Images.class.getResource("/images/" + name));
		} catch (IOException e) {
			log.error("Error: ", e);
		}

		return null;

	}

	public static ImageIcon imageIcon(String name) {

		try {
			return new ImageIcon(Images.class.getResource("/images/" + name));
		} catch (Exception e) {
			log.error("Error: ", e);
		}

		return null;

	}

}
