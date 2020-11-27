package fr.better.rtp.parameter;

import fr.better.rtp.RTPMain;
import fr.better.rtp.entity.RTP;
import fr.better.tools.command.BetterCommand;
import org.bukkit.entity.Player;

import java.util.List;

public class ListParam implements BetterCommand.PlayerParameter {
    @Override
    public void action(Player player, List<String> args) {
        player.sendMessage(new String[]{"§3» Brtp List «",
        "§8§m-----------------------"});

        for(RTP rtp : RTPMain.getInstance().getRTPs()){
            player.sendMessage("§8» " + rtp.toText());
        }

        player.sendMessage(new String[]{"§8§m-----------------------",
        "§7Use : §3/brtp tp <param> §7to be teleported"});
    }

    @Override
    public String permission() {
        return "admin.rtp.use";
    }

    @Override
    public String utility() {
        return "list all rtp type";
    }

    @Override
    public String parameter() {
        return "";
    }

    @Override
    public int parameterSize() {
        return 0;
    }
}
