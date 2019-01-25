package fr.thedarven.configuration.temp;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.Potion;

import fr.thedarven.configuration.builders.InventoryGUI;
import fr.thedarven.configuration.builders.OptionBoolean;

public class PotionII extends OptionBoolean{

	public PotionII(String pName, String pDescription, Material pItem, InventoryGUI pParent, int pPosition, boolean pValue) {
		super(pName, pDescription, pItem, pParent, pPosition, pValue);
	}
	
	public PotionII(String pName, String pDescription, Material pItem, InventoryGUI pParent, boolean pValue) {
		super(pName, pDescription, pItem, pParent, pValue);
	}
	
	
	@EventHandler
    public void onPlayerRightClick(PlayerInteractEvent e) {
        if(value)
            return;
        ItemStack item = e.getItem();
        if(item != null && (item.getType() == Material.POTION)){
        	
        	Potion potion = Potion.fromItemStack(item);
        	
	        if(potion.getLevel() > 1) {
	            e.setCancelled(true);
	            e.getPlayer().sendMessage(ChatColor.GREEN+"Poufff ! Votre potion est passé au niveau 1 !");
	            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.DIG_STONE, 10,1);
	            
	            potion.setLevel(1);
	            
	            PlayerInventory inv = e.getPlayer().getInventory();
	            inv.setItem(inv.getHeldItemSlot(), potion.toItemStack(1));
	        }
        }    
    }
}
