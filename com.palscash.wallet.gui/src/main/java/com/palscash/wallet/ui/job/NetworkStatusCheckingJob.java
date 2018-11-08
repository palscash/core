package com.palscash.wallet.ui.job;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.apache.commons.lang3.time.DateUtils;
import org.palscash.network.api.client.PalsCashClient;
import org.palscash.network.api.client.ValidationNodeUrls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.palscash.wallet.ui.common.Icons;
import com.palscash.wallet.ui.gui.MainWindow;

@Component
public class NetworkStatusCheckingJob {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MainWindow mainWindow;

	@Autowired
	private PalsCashClient client;

	private boolean connected;

	private ImageIcon icon = new ImageIcon(this.getClass().getResource("/icons/ajax-loader.gif"));

	private Icon checkIcon = Icons.getIcon("check.png");

	private Icon cancelIcon = Icons.getIcon("cancel.png");

	@PostConstruct
	public void init() throws IOException {

		String host = ValidationNodeUrls.getRandomHost();
		client.setHost(host);

	}

	@Scheduled(fixedDelay = DateUtils.MILLIS_PER_SECOND * 5)
	public void check() throws Exception {

		if (connected == false) {
			mainWindow.setStatusMessage("Checking network connection (" + client.getHost() + ")", icon);
		}

		try {

			client.ping();

			connected = true;

			mainWindow.setStatusMessage("Connected to network (" + client.getHost() + ")", checkIcon);

		} catch (Exception e) {

			log.error("Error: " + e.getMessage());

			mainWindow.setStatusMessage("Network not available (" + client.getHost() + ")", cancelIcon);

			connected = false;

			// Go throught all the nodes, find the one that can be pinged
			List<String> allNodes = ValidationNodeUrls.getAll();

			Collections.shuffle(allNodes);

			for (String node : allNodes) {

				client.setHost(node);

				try {

					log.debug("Connect to: " + node);

					mainWindow.setStatusMessage("Checking network connection (" + client.getHost() + ")", icon);

					client.ping();

					log.debug("Connected to: " + node);

					return;

				} catch (Exception e1) {
					log.error("Can't connect to: " + node);
					Thread.sleep(1000);
				}

			}

			mainWindow.setStatusMessage("Network not available, will re-try shortly...", cancelIcon);
		}

	}

	public boolean isConnected() {
		return connected;
	}

}
