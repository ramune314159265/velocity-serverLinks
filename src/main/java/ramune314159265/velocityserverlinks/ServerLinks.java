package ramune314159265.velocityserverlinks;

import com.google.inject.Inject;
import com.moandjiezana.toml.Toml;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyReloadEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.util.ServerLink;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
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
	private final Path configFolder;

	@Inject
	public ServerLinks(ProxyServer server, Logger logger, @DataDirectory Path configFolder) {
		this.server = server;
		this.configFolder = configFolder;

		ServerLinks.logger = logger;
		ServerLinks.ServerLinkList = new ArrayList<>();
	}

	private void loadConf() {
		File folder = configFolder.toFile();
		File configFile = new File(folder, "links.toml");
		if (!configFile.getParentFile().exists()) {
			configFile.getParentFile().mkdirs();
		}

		if (!configFile.exists()) {
			try (InputStream input = ServerLinks.class.getResourceAsStream("/" + configFile.getName())) {
				if (input != null) {
					Files.copy(input, configFile.toPath());
				} else {
					configFile.createNewFile();
				}
			} catch (IOException e) {
				logger.error(e.toString());
			}
		}

		Toml configToml = new Toml().read(configFile);
		ServerLinks.ServerLinkList.clear();
		List<HashMap<String, String>> links = configToml.getList("links");
		links.forEach(link -> {
			try {
				ServerLinks.ServerLinkList.add(ServerLink.serverLink(ServerLink.Type.valueOf(link.get("name")), link.get("url")));
			} catch (IllegalArgumentException e) {
				ServerLinks.ServerLinkList.add(ServerLink.serverLink(MiniMessage.miniMessage().deserialize(link.get("name")), link.get("url")));
			}
		});
	}

	@Subscribe
	public void onProxyInitialization(ProxyInitializeEvent event) {
		loadConf();
		server.getEventManager().register(this, new PlayerListener());
	}

	@Subscribe
	public void onProxyReloaded(ProxyReloadEvent event) {
		this.loadConf();
		logger.info("Reload complete");
	}
}
