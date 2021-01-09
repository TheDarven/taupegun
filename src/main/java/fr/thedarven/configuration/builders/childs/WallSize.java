package fr.thedarven.configuration.builders.childs;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;

import fr.thedarven.configuration.builders.InventoryGUI;
import fr.thedarven.configuration.builders.OptionNumeric;
import fr.thedarven.main.metier.EnumConfiguration;
import fr.thedarven.main.metier.NumericHelper;
import fr.thedarven.main.metier.PlayerTaupe;
import fr.thedarven.utils.UtilsClass;

public class WallSize extends OptionNumeric {

	public WallSize(String pName, String pDescription, String pTranslationName, Material pItem, InventoryGUI pParent, int pPosition, NumericHelper infos) {
		super(pName, pDescription, pTranslationName, pItem, pParent, pPosition, infos);
	}
	
	public WallSize(String pName, String pDescription, String pTranslationName, Material pItem, InventoryGUI pParent, NumericHelper infos) {
		super(pName, pDescription, pTranslationName, pItem, pParent, infos);
	}
	
	/**
	 * L'évènement de clique dans l'inventaire
	 * 
	 * @param e L'évènement de clique
	 */
	@EventHandler
	public void clickInventory(InventoryClickEvent e){
		int operation = 0;
		int number = 0;
		if (e.getWhoClicked() instanceof Player && e.getClickedInventory() != null && e.getClickedInventory().equals(this.inventory)) {
			Player p = (Player) e.getWhoClicked();
			PlayerTaupe pl = PlayerTaupe.getPlayerManager(p.getUniqueId());
			e.setCancelled(true);
			
			if (click(p,EnumConfiguration.OPTION) && !e.getCurrentItem().getType().equals(Material.AIR) && pl.getCanClick()) {
				if(e.getCurrentItem().getType().equals(Material.REDSTONE) && e.getRawSlot() == this.getLines()*9-1 && e.getCurrentItem().getItemMeta().getDisplayName().equals(getBackName())){
					p.openInventory(this.getParent().getInventory());
					return;
				}
				
				if (e.getSlot() == 1 && this.morePas > 2) {
					operation = 1;
					number = this.pas*100;
				} else if (e.getSlot() == 2 && this.morePas > 1) {
					operation = 1;
					number = this.pas*10;
				} else if (e.getSlot() == 3) {
					operation = 1;
					number = this.pas;
				} else if (e.getSlot() == 5) {
					operation = 2;
					number = this.pas;
				} else if (e.getSlot() == 6 && this.morePas > 1) {
					operation = 2;
					number = this.pas*10;
				} else if (e.getSlot() == 7 && this.morePas > 2) {
					operation = 2;
					number = this.pas*100;
				}
				
				if (operation == 1) {
					if (this.min < this.value-number) {
						this.value = this.value-number;
					} else {
						this.value = this.min;
					}
					reloadItem();
				} else if (operation == 2) {
					if (this.max > this.value+number) {
						this.value = this.value+number;
					} else {
						this.value = this.max;
					}
					reloadItem();
				}
				
				World world = UtilsClass.getWorld();
				world.getWorldBorder().setCenter(0, 0);
				world.getWorldBorder().setSize(value*2);
				
				delayClick(pl);
			}
		}
	}
}
