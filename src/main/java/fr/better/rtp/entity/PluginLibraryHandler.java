package fr.better.rtp.entity;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class PluginLibraryHandler {

    private Economy economy;

    public PluginLibraryHandler() {
        setup();
    }

    public void setup() {
        if(hasPlugin("Vault")){
            RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
            if (economyProvider != null) {
                economy = economyProvider.getProvider();
            }
        }
    }

    public boolean hasVault(){
        return economy != null;
    }

    public Economy getVaultEconomy(){
        return economy;
    }

    private boolean hasPlugin(String plugin){
        return Bukkit.getServer().getPluginManager().isPluginEnabled(plugin);
    }
}
