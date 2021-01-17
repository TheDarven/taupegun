package fr.thedarven.scenarios.childs;

import fr.thedarven.scenarios.builders.InventoryGUI;
import fr.thedarven.scenarios.builders.OptionNumeric;
import fr.thedarven.scenarios.helper.NumericHelper;
import fr.thedarven.utils.api.Title;
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
	
	public DiamondLimit(InventoryGUI parent) {
		super("Diamond Limit", "Limite le nombre de diamant que chaque joueur peu miner dans la partie.", "MENU_CONFIGURATION_SCENARIO_DIAMONDLIMIT",
				Material.DIAMOND, parent, new NumericHelper(0, 50, 0, 1, 2, "", 1, true, 1));
		updateLanguage(getLanguage());
	}





	@Override
	public void updateLanguage(String language) {
		EXCEEDED_LIMIT = LanguageBuilder.getContent(getTranslationName(), "exceededLimit", language, true);

		super.updateLanguage(language);
	}

	@Override
	protected LanguageBuilder initDefaultTranslation() {
		LanguageBuilder languageElement = super.initDefaultTranslation();
		languageElement.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "exceededLimit", EXCEEDED_LIMIT);
		
		return languageElement;
	}
	
	
	
	
	/**
	 * Lorsqu'un joueur casse un bloc, sa diamond limit s'incrémente
	 * On le bloque dans le ca soù il a dépasser sa limite
	 * 
	 * @param e L'évènement de bloc cassé
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	final public void onBlockBreak(BlockBreakEvent e){
		if (e.isCancelled())
			return;

		Player player = e.getPlayer();
		if (e.getBlock().getType() != Material.DIAMOND_ORE || this.value <= 0 || player.getGameMode() != GameMode.SURVIVAL)
			return;

		if (playersLimit.containsKey(player.getUniqueId())) {
			if (playersLimit.get(player.getUniqueId()) < this.value) {
				playersLimit.replace(player.getUniqueId(), playersLimit.get(player.getUniqueId()) + 1);
				Title.sendActionBar(player, ChatColor.BLUE + "DiamondLimit : " + ChatColor.WHITE + playersLimit.get(player.getUniqueId()) + "/" + this.value);
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