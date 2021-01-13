package fr.thedarven.configuration.builders.childs;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.thedarven.configuration.builders.InventoryGUI;
import fr.thedarven.configuration.builders.OptionNumeric;
import fr.thedarven.configuration.builders.helper.NumericHelper;

public class GoldenHead extends OptionNumeric {

	public GoldenHead(String pName, String pDescription, String pTranslationName, Material pItem, InventoryGUI pParent, int pPosition, NumericHelper infos) {
		super(pName, pDescription, pTranslationName, pItem, pParent, pPosition, infos);
	}
	
	public GoldenHead(String pName, String pDescription, String pTranslationName, Material pItem, InventoryGUI pParent, NumericHelper infos) {
		super(pName, pDescription, pTranslationName, pItem, pParent, infos);
	}
	
	/**
	 * Lorsqu'on mange une golden head
	 * 
	 * @param e L'évènement de mangeage
	 */
	@EventHandler
	public void onEat(PlayerItemConsumeEvent e){
		Player player = e.getPlayer();
		if(e.getItem().getItemMeta().hasDisplayName()){
			if(e.getItem().getItemMeta().getDisplayName().equals(ChatColor.GOLD+"Golden Head") && this.value > 0){
				player.removePotionEffect(PotionEffectType.REGENERATION);
				player.removePotionEffect(PotionEffectType.ABSORPTION);
				player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * this.value, 1));
				player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 2400, 0));
			}
		}	
	}
}
