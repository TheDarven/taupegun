package fr.thedarven.scenario.configuration;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenario.builder.ConfigurationInventory;
import fr.thedarven.scenario.builder.OptionNumeric;
import fr.thedarven.scenario.utils.NumericParams;
import fr.thedarven.utils.GlobalVariable;
import fr.thedarven.utils.api.titles.ActionBar;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.HashMap;
import java.util.UUID;

public class DiamondLimit extends OptionNumeric {

    private static String EXCEEDED_LIMIT = "Vous avez dépassé la limite de diamant.";

    private final HashMap<UUID, Integer> playersLimit = new HashMap<>();

    public DiamondLimit(TaupeGun main, ConfigurationInventory parent) {
        super(main, "Diamond Limit", "Limite le nombre de diamant que chaque joueur peut miner dans la partie.", "MENU_CONFIGURATION_SCENARIO_DIAMONDLIMIT",
                Material.DIAMOND, parent, new NumericParams(0, 50, 0, 1, 2, "", 1, true, 1));
    }

    @Override
    public void loadLanguage(String language) {
        EXCEEDED_LIMIT = LanguageBuilder.getContent(this.translationName, "exceededLimit", language, true);
        super.loadLanguage(language);
    }

    @Override
    protected LanguageBuilder initDefaultTranslation() {
        LanguageBuilder languageElement = super.initDefaultTranslation();
        languageElement.addTranslation(GlobalVariable.DEFAULT_LANGUAGE, "exceededLimit", EXCEEDED_LIMIT);
        return languageElement;
    }


    /**
     * Lorsqu'un joueur casse un bloc, sa diamond limit s'incrémente
     * On le bloque dans le ca soù il a dépasser sa limite
     *
     * @param e L'évènement de bloc cassé
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    final public void onBlockBreak(BlockBreakEvent e) {
        if (e.isCancelled()) {
			return;
		}

        Player player = e.getPlayer();
        if (e.getBlock().getType() != Material.DIAMOND_ORE || this.value <= 0 || player.getGameMode() != GameMode.SURVIVAL) {
			return;
		}

        if (playersLimit.containsKey(player.getUniqueId())) {
            if (playersLimit.get(player.getUniqueId()) < this.value) {
                playersLimit.replace(player.getUniqueId(), playersLimit.get(player.getUniqueId()) + 1);
                new ActionBar(ChatColor.BLUE + "DiamondLimit : " + ChatColor.WHITE + playersLimit.get(player.getUniqueId()) + "/" + this.value)
                        .sendActionBar(player);
            } else {
                player.sendMessage("§c" + EXCEEDED_LIMIT);
                e.setCancelled(true);
                e.getBlock().setType(Material.AIR);
            }
        } else {
            playersLimit.put(player.getUniqueId(), 1);
        }
    }

}