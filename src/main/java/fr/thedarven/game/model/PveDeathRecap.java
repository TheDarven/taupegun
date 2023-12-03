package fr.thedarven.game.model;

import com.avaje.ebeaninternal.server.lib.util.StringHelper;
import fr.thedarven.player.model.PlayerTaupe;
import net.md_5.bungee.api.ChatColor;

public class PveDeathRecap extends PlayerDeathRecap {

    private final String originMessage;

    public PveDeathRecap(PlayerTaupe victim, String originMessage) {
        super(victim);
        this.originMessage = originMessage;
    }

    @Override
    public String getMessage() {
        if (StringHelper.isNull(originMessage)) {
            return super.getMessage();
        } else {
            return String.format("%s\n%s%s⋙ %s", super.getMessage(), ChatColor.GRAY, ChatColor.ITALIC, originMessage);
        }
    }
}
