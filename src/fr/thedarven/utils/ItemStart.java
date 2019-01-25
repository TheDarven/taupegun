package fr.thedarven.utils;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import fr.thedarven.configuration.builders.InventoryRegister;

public class ItemStart implements Listener{

	public static void itemStart(Player p){
		int i;
		p.getInventory().clear();
		for(i=0; i<45; i++) {
			if(i<4) {
				p.getInventory().setItem(39-i, InventoryRegister.startitem.getInventory().getItem(i));
			}else if(i<36) {
				p.getInventory().setItem(i, InventoryRegister.startitem.getInventory().getItem(i));
			}else {
				p.getInventory().setItem(i-36, InventoryRegister.startitem.getInventory().getItem(i));
			}
		}
	}
	
}
