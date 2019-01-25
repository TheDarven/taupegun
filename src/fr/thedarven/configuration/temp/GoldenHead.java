package fr.thedarven.configuration.temp;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.thedarven.configuration.builders.InventoryGUI;
import fr.thedarven.configuration.builders.OptionNumeric;

public class GoldenHead extends OptionNumeric {

	public GoldenHead(String pName, String pDescription, Material pItem, InventoryGUI pParent, int pPosition, int pMin, int pMax, int pValue, int pPas, int pMorePas, String pAfterName, int pDiviseur) {
		super(pName, pDescription, pItem, pParent, pPosition, pMin, pMax, pValue, pPas, pMorePas, pAfterName, pDiviseur);
	}
	
	public GoldenHead(String pName, String pDescription, Material pItem, InventoryGUI pParent, int pMin, int pMax, int pValue, int pPas, int pMorePas, String pAfterName, int pDiviseur) {
		super(pName, pDescription, pItem, pParent, pMin, pMax, pValue, pPas, pMorePas, pAfterName, pDiviseur);
	}
	
	@EventHandler
	public void onEat(PlayerItemConsumeEvent e){
		Player player = e.getPlayer();
		if(e.getItem().getItemMeta().hasDisplayName()){
			if(e.getItem().getItemMeta().getDisplayName().equals(ChatColor.GOLD+"Golden Head") && this.getValue() > 0){
				player.removePotionEffect(PotionEffectType.REGENERATION);
				player.removePotionEffect(PotionEffectType.ABSORPTION);
				player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20*this.getValue(), 1));
				player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 2400, 0));
			}
		}	
	}
}
