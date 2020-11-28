package fr.better.rtp.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RandomTeleportEvent extends Event implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Player player;

    private final boolean useIA;
    private boolean cancelled;
    private boolean usePotion, useSong;

    private Location loc;
    private String actionbar, title, sub, message;

    public RandomTeleportEvent(Player player, boolean useIA, Location loc, String actionbar, String title, String sub, String message) {
        super(false);
        this.player = player;
        this.useIA = useIA;
        this.loc = loc;
        this.actionbar = actionbar;
        this.title = title;
        this.sub = sub;
        this.message = message;
        this.usePotion = true;
        this.useSong = true;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isUseIA() {
        return useIA;
    }

    public Location getLocation() {
        return loc;
    }

    public void setUsePotion(boolean use){ usePotion = use; }

    public boolean usePotion() {
        return usePotion;
    }

    public boolean useSong() {
        return useSong;
    }

    public void setUseSong(boolean useSong) {
        this.useSong = useSong;
    }

    public void setLocation(Location loc) {
        this.loc = loc;
    }

    public String getActionbar() {
        return actionbar;
    }

    public void setActionbar(String actionbar) {
        this.actionbar = actionbar;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
