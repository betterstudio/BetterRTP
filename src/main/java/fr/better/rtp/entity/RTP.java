package fr.better.rtp.entity;

import fr.better.rtp.RTPMain;
import fr.better.rtp.api.PlayerRandomTeleportEvent;
import fr.better.tools.config.BetterConfig;
import fr.better.tools.config.Change;
import fr.better.tools.utils.ActionBar;
import fr.better.tools.utils.Title;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class RTP {

    private final String tag;

    private int min, max, cooldown, price;
    private World world;
    private String permission;

    private boolean title, sub, actionbar, message;
    private List<PotionEffect> potions;
    private int invincibilityTime;
    private boolean bypassClaim;
    private String sound;

    private HashMap<Player, Integer> cooldowns;


    public RTP(int min, World world, String permission, String tag) {
        this.min = min;
        this.world = world;
        this.permission = permission;
        this.tag = tag;
    }

    public RTP(int min, int max, int cooldown, int price, World world, String permission, String tag, boolean title, boolean sub, boolean actionbar, boolean message, List<PotionEffect> potions, int invincibilityTime, boolean bypassClaim, String sound) {
        this.min = min;
        this.max = max;
        this.cooldown = cooldown;
        this.price = price;
        this.world = world;
        this.permission = permission;
        this.tag = tag;
        this.title = title;
        this.sub = sub;
        this.actionbar = actionbar;
        this.message = message;
        this.potions = potions;
        this.invincibilityTime = invincibilityTime;
        this.bypassClaim = bypassClaim;
        this.sound = sound;
        if(cooldown != 0 && cooldown == 0)cooldowns = new HashMap<>();
    }

    public RTP(String tag, BetterConfig config){

        this.tag = tag;
        this.min = config.getInt("RTP." + tag + ".min", 50);
        this.world = Bukkit.getWorld(config.getString("RTP." + tag + ".world", ""));
        if(world == null)world = Bukkit.getWorlds().get(0);
        this.permission = config.getString("RTP." + tag + ".permission", "");
        this.max = config.getInt("RTP." + tag + ".max", -1);
        this.cooldown = config.getInt("RTP." + tag + ".cooldown", -1);
        this.price = config.getInt("RTP." + tag + ".price", -1);
        if(config.isString("RTP." + tag + ".sound"))
        this.sound = config.getString("RTP." + tag + ".sound", "");

        this.actionbar = config.isString("RTP." + tag + ".action-bar");
        this.title = config.isString("RTP." + tag + ".title");
        this.sub = config.isString("RTP." + tag + ".sub");
        this.message = config.isString("RTP." + tag + ".message");

        if(config.isConfigurationSection("RTP." + tag + ".effect")){
            System.out.println("good");
            this.potions = new ArrayList<>();

            for(String key : config.getConfigurationSection("RTP." + tag + ".effect").getKeys(false)){
                potions.add(getPotionEffet(config, "RTP." + tag + ".effect." + key));
            }
        }

        if(cooldown != -1)cooldowns = new HashMap<>();
    }

    private PotionEffect getPotionEffet(BetterConfig config, String where){
        int time = config.getInt(where + ".time", 100);
        int power = config.getInt(where + ".level", 1)-1;
        PotionEffectType type = PotionEffectType.getByName(config.getString(where + ".type", "SPEED"));
        boolean particle = config.getBoolean(where + ".particle", true);

        if(type == null)type = PotionEffectType.SPEED;
        System.out.println(new PotionEffect(type, power, time, particle).toString());
        return new PotionEffect(type, power, time, particle);
    }

    //////////////////////
    ////////////////////// USEFUL FUNCTION
    //////////////////////

    public void startCooldown(){
        Bukkit.getScheduler().runTaskTimerAsynchronously(RTPMain.getInstance(), new Runnable() {
            @Override
            public void run() {
                for(Player p : cooldowns.keySet()){
                    int cool = cooldowns.get(p);

                    if(cool == 0){
                        cooldowns.remove(p);
                        continue;
                    }

                    cooldowns.replace(p, cool--);
                }
            }
        }, 20, 0);
    }

    public void teleport(Player target) {

        Random r = new Random();
        BetterConfig config = (BetterConfig) RTPMain.getInstance().getBetterConfig();

        int x = r.nextInt(max-min)+min;
        int z = r.nextInt(max-min)+min;
        int y = config.getInt("Config.y", 100);

        boolean useIA = config.getBoolean("Config.useIA", false);

        if(useIA){
            y = getYByAnIA(world, x, z, y);
        }

        Location loc = new Location(world, x, y, z);

        PlayerRandomTeleportEvent event = new PlayerRandomTeleportEvent(target, useIA, loc,
                config.getMessage("RTP." + tag + ".actionbar-message", true, ""),
                config.getMessage("RTP." + tag + ".title-message", true, "", new Change("!x!", "" + loc.getBlockX()),
                new Change("!z!", "" + loc.getBlockZ()), new Change("!y!", "" + loc.getBlockY())),
                config.getMessage("RTP." + tag + ".subtitle-message", true, "", new Change("!x!", "" + loc.getBlockX()),
                new Change("!z!", "" + loc.getBlockZ()), new Change("!y!", "" + loc.getBlockY())),
                config.getMessage("RTP." + tag + ".message", true, "", new Change("!x!", "" + loc.getBlockX()),
                new Change("!z!", "" + loc.getBlockZ()), new Change("!y!", "" + loc.getBlockY())));
        Bukkit.getPluginManager().callEvent(event);
        if(event.isCancelled())return;

        if(potions != null && event.usePotion()){
            System.out.println("JE");
            for(PotionEffect potionEffect : potions){
                target.addPotionEffect(potionEffect);
            }
        }
        target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, invincibilityTime*20, 255, false));
        if(!sound.isEmpty() && event.useSong()){
            try{
                Sound sound = Sound.valueOf(this.sound);
                sound.name();
                target.playSound(target.getLocation(), sound, 5, 5);
            }catch(Exception e) {
                target.playNote(target.getLocation(), Instrument.PIANO, Note.natural(1, Note.Tone.A));
            }
        }
        if(!event.getActionbar().isEmpty())new ActionBar(target).send(event.getActionbar());
        if(!event.getTitle().isEmpty() && !event.getSub().isEmpty()){
            new Title(target).send(event.getTitle(), event.getSub(), 5, 30, 5);
        }else if(!event.getTitle().isEmpty()){
            new Title(target).send(event.getTitle(), "", 5, 30, 5);
        }else if(!event.getSub().isEmpty()){
            new Title(target).send("", event.getSub(), 5, 30, 5);
        }
        if(!event.getMessage().isEmpty())target.sendMessage(event.getMessage());
        target.teleport(event.getLocation());
    }

    public String toText() {
        StringBuilder string = new StringBuilder();
        string.append("§6" + tag + " §7( world : §3" + world.getName() + "§7 , min : §3" + min );
        if(max != -1)string.append(" §7, max : §3" + max);
        if(price != -1 && price != 0)string.append(" §7, price : " + price);
        if(!permission.isEmpty())string.append(" §7, perm : §3" + permission);
        if(cooldown != -1 && cooldown != 0)string.append(" §7, cooldown : §6" + cooldown);
        string.append(" §7)");
        return string.toString();
    }

    private int getYByAnIA(World world, int x, int z, int sup){
        return world.getHighestBlockYAt(x, z)+sup;
    }


    public boolean isCooldown(Player target) {
        return cooldowns.containsKey(target);
    }
    public int getCooldownOf(Player player){
        if(isCooldown(player))return cooldowns.get(player).intValue();
        return 0;
    }
    public Set<Player> getAllCooldowned(){ return cooldowns.keySet(); }

    //////////////////////
    ////////////////////// GETTER SETTER
    //////////////////////


    public String getTag() {
        return tag;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
        cooldowns = new HashMap<>();
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public boolean hasTitle() {
        return title;
    }

    public boolean hasSubtitle() {
        return sub;
    }

    public boolean hasActionbar() {
        return actionbar;
    }

    public boolean hasMessage() {
        return message;
    }

    public List<PotionEffect> getPotions() {
        return potions;
    }

    public void setPotions(List<PotionEffect> potions) {
        this.potions = potions;
    }

    public int getInvincibilityTime() {
        return invincibilityTime;
    }

    public void setInvincibilityTime(int invincibilityTime) {
        this.invincibilityTime = invincibilityTime;
    }

    public boolean hasBypassClaim() {
        return bypassClaim;
    }

    public void setBypassClaim(boolean bypassClaim) {
        this.bypassClaim = bypassClaim;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }
}
