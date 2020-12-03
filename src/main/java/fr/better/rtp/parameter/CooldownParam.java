package fr.better.rtp.parameter;

import fr.better.rtp.RTPMain;
import fr.better.rtp.entity.RTP;
import fr.better.rtp.api.RTPApi;
import fr.better.tools.command.BetterCommand;
import fr.better.tools.config.Change;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class CooldownParam implements BetterCommand.PlayerParameter {

    @Override
    public void action(Player player, List<String> args) {

        RTP rtp = RTPApi.getInstance().getRTPbyTag(args.get(0));
        if(rtp == null){
            player.sendMessage(RTPMain.getInstance().getBetterConfig()
                    .getMessage("Config.message.error.novalid", true,
                            "§4Erreur: tu dois selectionner un rtp valide !"));
            return;
        }

        Player target = player;
        if(player.hasPermission("admin.rtp.use") && args.size() > 1){
            target = Bukkit.getPlayer(args.get(1));
            if(target == null) target = player;
        }

        if(rtp.isCooldown(target)){
            int i = rtp.getCooldownOf(target);
            target.sendMessage(RTPMain.getInstance().getBetterConfig()
                    .getMessage("Message.cooldown-want", true, "§7Ton cooldown est de : §3"+ i +" §3secondes",
                            new Change("!cooldown!", i+""), new Change("!rtp!", rtp.getTag())));

        }else{
            target.sendMessage(RTPMain.getInstance().getBetterConfig()
                    .getMessage("Message.no-cooldown", true, "§7Tu n'as pas de cooldown",
                            new Change("!rtp!", rtp.getTag())));
        }
    }

    @Override
    public String permission() {
        return "cooldown.rtp.use";
    }

    @Override
    public String utility() {
        return "see your cooldown";
    }

    @Override
    public String parameter() {
        return "<rtp> [player]";
    }

    @Override
    public int parameterSize() {
        return 1;
    }
}
