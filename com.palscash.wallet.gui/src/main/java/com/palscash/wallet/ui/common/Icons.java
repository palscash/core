package com.palscash.wallet.ui.common;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class Icons {

	private static final Log log = LogFactory.getLog(Icons.class);

	private static List<Image> appIcons = new ArrayList<>();

	public static Image load(String name) {

		try {
			String path = "/icons/" + name;

			// log.debug("Loading icon: " + path);

			BufferedImage image = ImageIO.read(Icons.class.getResource(path));

			return image;

		} catch (IOException e) {
			log.error("Error: icon was not loaded: " + name, e);
		}

		return null;

	}

	public static Icon loadFromPath(String path) {

		try {
			return new ImageIcon(ImageIO.read(Icons.class.getResource(path)));
		} catch (IOException e) {
			log.error("Error: icon was not loaded: " + path, e);
		}

		return null;

	}

	public static Icon loadAsIcon(String name) {

		try {
			return new ImageIcon(load(name));
		} catch (Exception e) {
			log.error("Error: " + name, e);
		}

		return null;

	}

	public static List<Image> getAppIcons() {
		if (appIcons.isEmpty()) {
			appIcons.add(new ImageIcon(Icons.class.getResource("/icons/app/128.png")).getImage());
			appIcons.add(new ImageIcon(Icons.class.getResource("/icons/app/256.png")).getImage());
			appIcons.add(new ImageIcon(Icons.class.getResource("/icons/app/32.png")).getImage());
			appIcons.add(new ImageIcon(Icons.class.getResource("/icons/app/512.png")).getImage());
			appIcons.add(new ImageIcon(Icons.class.getResource("/icons/app/64.png")).getImage());
			
		}
		return appIcons;
	}

	public static Icon getIcon(String name) {
		return loadAsIcon(name);
	}

	public static Image getAppIcon() {
		Image ICON = Icons.load("icon_16.png");
		return ICON;
	}

}
