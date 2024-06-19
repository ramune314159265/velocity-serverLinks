package ramune314159265.velocityserverlinks;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerConnectedEvent;

public class PlayerListener {
	@Subscribe
	public void onServerConnectedEvent(ServerConnectedEvent event) {
		ServerLinks.logger.info("aaaa");
		ServerLinks.logger.info(ServerLinks.ServerLinkList.toString());
		event.getPlayer().setServerLinks(ServerLinks.ServerLinkList);
	}
}
