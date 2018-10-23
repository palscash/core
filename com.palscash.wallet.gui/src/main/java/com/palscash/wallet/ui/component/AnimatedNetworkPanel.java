package com.palscash.wallet.ui.component;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.SystemUtils;

import com.palscash.wallet.ui.common.Fonts;
import com.palscash.wallet.ui.common.Icons;
import com.palscash.wallet.ui.common.Images;
import com.palscash.wallet.ui.common.SwingHelper;

@SuppressWarnings("serial")
public class AnimatedNetworkPanel extends JPanel {

	private static int MAX_POINTS = 100;

	private List<NetworkPoint> points = new ArrayList<>();

	private BufferedImage logo;

	public AnimatedNetworkPanel() {
		try {
			logo = ImageIO.read(this.getClass().getResourceAsStream("/images/logo.png"));
		} catch (IOException e) {
		}
	}

	class NetworkPoint extends Point2D.Float {

		public NetworkPoint(float x, float y) {
			super(x, y);
		}

		public float accelX = 0;
		public float accelY = 0;
		public boolean deleted;

		@Override
		public String toString() {
			return "NetworkPoint [accelX=" + accelX + ", accelY=" + accelY + ", deleted=" + deleted + ", x=" + x
					+ ", y=" + y + "]";
		}

	}

	@Override
	protected void paintComponent(Graphics g) {

		long st = System.currentTimeMillis();

		Graphics2D g2 = (Graphics2D) g;

		/*
		 * g.setColor(Color.gray); g2.fillRect(0, 0, getWidth(), getHeight());
		 * 
		 * if (true) return;
		 */

		if (SystemUtils.IS_OS_WINDOWS) {
			SwingHelper.awesomeGraphics(g2);
		} else {
			// g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
			// RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
			// g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			// RenderingHints.VALUE_ANTIALIAS_ON);
			// g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
			// RenderingHints.VALUE_COLOR_RENDER_QUALITY);
			// g2.setRenderingHint(RenderingHints.KEY_DITHERING,
			// RenderingHints.VALUE_DITHER_ENABLE);
			// g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
			// RenderingHints.VALUE_FRACTIONALMETRICS_ON);
			// g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
			// RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			// g2.setRenderingHint(RenderingHints.KEY_RENDERING,
			// RenderingHints.VALUE_RENDER_QUALITY);
			// g2.setRenderingHint(Re-nderingHints.KEY_STROKE_CONTROL,
			// RenderingHints.VALUE_STROKE_NORMALIZE);
		}

		if (points.isEmpty()) {

			for (int i = 0; i < MAX_POINTS; i++) {
				getNewPoint();
			}

		}

		// g2.setColor(Color.BLACK);
		g2.setColor(new Color(243, 245, 245));
		g2.fillRect(0, 0, getWidth(), getHeight());

		// g2.setColor(Color.WHITE);
		Iterator<NetworkPoint> it = points.iterator();

		while (it.hasNext()) {

			NetworkPoint pt = it.next();

			pt.x += pt.accelX;
			pt.y += pt.accelY;

			for (NetworkPoint otherPt : points) {
				if (pt != otherPt) {
					double dist = otherPt.distance(pt);
					if (dist <= MAX_POINTS) {
						int alpha = 100 - (int) dist;
						if (alpha < 0) {
							alpha = 0;
						}
						if (alpha > 0) {

							// g2.setColor(new Color(75, 7, 92, alpha));
							g2.setColor(new Color(193, 215, 76, alpha));

							g2.drawLine((int) pt.x, (int) pt.y, (int) otherPt.x, (int) otherPt.y);
						}
					}
				}
			}

			g2.setColor(Color.WHITE);

			if (pt.x < -255 || pt.y < -255 || pt.x > getWidth() + 255 || pt.y > getHeight() + 255) {
				pt.deleted = true;
			}

		}

		it = points.iterator();
		int deleted = 0;
		while (it.hasNext()) {
			NetworkPoint pt = it.next();
			if (pt.deleted) {
				it.remove();
				deleted++;
			}
		}

		for (int i = 0; i < deleted; i++) {
			getNewPoint();
		}

		g2.drawImage(logo, (getWidth() - logo.getWidth()) / 2, (getHeight() - logo.getHeight()) / 2, null);

		long et = System.currentTimeMillis();

		// System.out.println((et - st));

	}

	private void getNewPoint() {
		NetworkPoint p = new NetworkPoint(RandomUtils.nextFloat(0, getWidth()), RandomUtils.nextFloat(0, getHeight()));
		p.accelX = 1 * (RandomUtils.nextFloat(0, 1) - RandomUtils.nextFloat(0, 1));
		p.accelY = 1 * (RandomUtils.nextFloat(0, 1) - RandomUtils.nextFloat(0, 1));
		points.add(p);
	}

}
