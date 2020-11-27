package fr.better.rtp.parameter;

import fr.better.rtp.RTPMain;
import fr.better.rtp.entity.PluginLibraryHandler;
import fr.better.rtp.entity.RTP;
import fr.better.tools.command.BetterCommand;
import fr.better.tools.config.BetterConfig;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class TPParam implements BetterCommand.PlayerParameter {

    @Override
    public void action(Player player, List<String> args) {
        BetterConfig config = (BetterConfig) RTPMain.getInstance().getBetterConfig();
        RTP rtp = RTPMain.getInstance().getRTPbyTag(args.get(0));
        if(rtp == null){
            player.sendMessage(config.getMessage("Config.message.error.novalid", true, "§4Erreur: tu dois selectionner un rtp valide !")); return;
        }

        Player target = player;
        boolean bypass = false;

        if(player.hasPermission("admin.rtp.use") && args.size() > 1){
            target = Bukkit.getPlayer(args.get(1));
            if(target == null){
                target = player;
            }

            if(args.size() > 2){
                if(args.get(1).equalsIgnoreCase("true"))bypass = true;
            }
        }

        if(rtp.isCooldown(target) && !target.hasPermission("bypass.rtp.use") && !bypass){
            player.sendMessage(config.getMessage("Config.message.error.nocooldown", true, "§4Erreur: tu es en cooldown !"));
            return;
        }

        PluginLibraryHandler handler = RTPMain.getInstance().getHandler();

        if(handler.hasVault() && rtp.getPrice() != -1 && rtp.getPrice() != 0){
            Economy eco = handler.getVaultEconomy();
            if(!target.hasPermission("bypass.rtp.use") && !bypass){
                if(!eco.hasAccount(target) && !(eco.getBalance(target) >= rtp.getPrice())){
                    target.sendMessage(config.getMessage("Config.message.error.nomoney", true, "§4Erreur: tu n'as pas assez d'argent."));
                    return;
                }

                eco.withdrawPlayer(target, rtp.getPrice());
            }
        }

        rtp.teleport(target);
    }

    @Override
    public String permission() {
        return "rtp.use";
    }

    @Override
    public String utility() {
        return "teleport to an random loc";
    }

    @Override
    public String parameter() {
        return "<tag> [player]";
    }

    @Override
    public int parameterSize() {
        return 1;
    }
}
