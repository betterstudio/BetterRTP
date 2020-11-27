package fr.better.rtp;

import fr.better.rtp.entity.RTP;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class RTPListener implements Listener {

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e){

        Player player = e.getPlayer();
        String message = e.getMessage();

        RTP rtp = RTPMain.getInstance().getRTPbyTag(RTPMain.getInstance().getBetterConfig().getMessage("Config.default", ""));

        if(rtp != null)
        if(message.startsWith("/rtp")){
            e.setCancelled(true);
            rtp.teleport(player);
        }
    }
}
