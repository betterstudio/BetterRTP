package fr.better.rtp;

import fr.better.rtp.entity.PluginLibraryHandler;
import fr.better.rtp.entity.RTP;
import fr.better.rtp.entity.RTPApi;
import fr.better.rtp.parameter.CooldownParam;
import fr.better.rtp.parameter.ListParam;
import fr.better.rtp.parameter.TPParam;
import fr.better.tools.BetterPlugin;
import fr.better.tools.config.BetterConfig;

import java.util.ArrayList;
import java.util.List;

public class RTPMain extends BetterPlugin implements RTPApi {

    private static RTPMain instance;
    private PluginLibraryHandler handler;

    private List<RTP> rtp;

    @Override
    public void onStart() {

        instance = this;
        loadBetterConfig();
        rtp = new ArrayList<>();
        handler = new PluginLibraryHandler();

        for(String part : ((BetterConfig) getBetterConfig()).getConfigurationSection("RTP").getKeys(false)) {
            RTP rtp = new RTP(part, (BetterConfig) getBetterConfig());
            if(rtp.getCooldown() != -1){
                rtp.startCooldown();
            }
            this.rtp.add(rtp);
        }

        listen(new RTPListener());

        initCommandExecutor("brtp");
        addArguments("tp").setupPlayer(new TPParam());
        addArguments("list").setupPlayer(new ListParam());
        addArguments("cooldown").setupPlayer(new CooldownParam());
    }

    @Override
    public String whoAreYou() {
        return "Qg";
    }

    @Override
    public void onStop() {

    }

    public static RTPMain getInstance() {
        return instance;
    }

    @Override
    public RTP getRTPbyTag(String s) {
        for(RTP r : rtp){
            if(r.getTag().equalsIgnoreCase(s))return r;
        }
        return null;
    }

    public List<RTP> getRTPs() {
        return rtp;
    }

    @Override
    public void addRTP(RTP rtp){ this.rtp.add(rtp); }

    public PluginLibraryHandler getHandler() {
        return handler;
    }
}
