package ramune314159265.velocityserverlinks;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.util.ServerLink;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Plugin(
		id = "velocity-serverlinks",
		name = "serverLinks",
		version = BuildConstants.VERSION,
		authors = {"ramune314159265"}
)
public class ServerLinks {
	public static List<ServerLink> ServerLinkList;
	public static Logger logger;

	@Inject
	private final ProxyServer server;

	@Inject
	public ServerLinks(ProxyServer server, Logger logger) {
		this.server = server;

		ServerLinks.logger = logger;
		ServerLinks.ServerLinkList = new ArrayList<>();
	}

	@Subscribe
	public void onProxyInitialization(ProxyInitializeEvent event) {
		logger.info("event registering");
		server.getEventManager().register(this, new PlayerListener());

		ServerLinks.ServerLinkList.add(ServerLink.serverLink(ServerLink.Type.valueOf("WEBSITE"), "https://example.com"));
	}
}
