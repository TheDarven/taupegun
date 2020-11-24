package fr.thedarven.events.commands.moles;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import fr.thedarven.configuration.builders.InventoryGUI;
import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.main.metier.PlayerTaupe;
import fr.thedarven.utils.UtilsClass;

public class ClaimCommand implements CommandExecutor{
	
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player){
			Player p = (Player) sender;
			PlayerTaupe pc = PlayerTaupe.getPlayerManager(p.getUniqueId());
			if(UtilsClass.molesEnabled() && pc.isTaupe() && pc.isAlive()){
				if(cmd.getName().equalsIgnoreCase("claim")) {
					if(!pc.getClaimTaupe().equals("aucun")){
						for(InventoryGUI inv : InventoryRegister.kits.getChildsValue()) {
							if(inv.getName().equals(pc.getClaimTaupe())) {
								Inventory inventaire = inv.getInventory();
								for(int i=0; i<9; i++) {
									if(inventaire.getItem(i) != null && !inventaire.getItem(i).getType().equals(Material.AIR)) {
										p.getWorld().dropItem(p.getLocation(),inventaire.getItem(i));
									}
								}	
							}
						}
						pc.setClaimTaupe("aucun");
					}
				}
			}
		}
		return true;
	}

}
