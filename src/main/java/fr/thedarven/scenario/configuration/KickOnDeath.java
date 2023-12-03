package fr.thedarven.scenario.configuration;

import fr.thedarven.TaupeGun;
import fr.thedarven.events.event.GamePlayerDeathEvent;
import fr.thedarven.game.model.enums.EnumGameState;
import fr.thedarven.scenario.builder.ConfigurationInventory;
import fr.thedarven.scenario.builder.OptionBoolean;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class KickOnDeath extends OptionBoolean {

    public KickOnDeath(TaupeGun main, ConfigurationInventory parent) {
        super(main, "Kick à la mort", "Exclut les joueurs à leur mort.", "MENU_CONFIGURATION_OTHER_DEATH_KICK", Material.REDSTONE_BLOCK, parent, 15, false);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onGamePlayerDeath(GamePlayerDeathEvent event) {
        Player deadPlayer = event.getPlayerTaupe().getPlayer();
        if (!EnumGameState.isCurrentState(EnumGameState.GAME) || deadPlayer == null) {
            return;
        }

        if (this.getValue()) {
            deadPlayer.kickPlayer(LanguageBuilder.getContent("EVENT_DEATH", "deathMumble", true));
        } else {
            deadPlayer.sendMessage("§c" + LanguageBuilder.getContent("EVENT_DEATH", "deathMumble", true));
            deadPlayer.sendMessage("§c" + LanguageBuilder.getContent("EVENT_DEATH", "deathInfo", true));
        }
    }

}
