package fr.thedarven.utils;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.main.TaupeGun;
import fr.thedarven.main.metier.PlayerTaupe;

public class UtilsClass {
	
	public static boolean startsWith(String text, List<String> testString) {
		for(String test: testString) {
			if(text.startsWith(test))
				return true;
		}
		return false;
	}
	
	public static World getWorld() {
		return Bukkit.getWorlds().get(0);
	}
	
	public static World getWorldNether() {
		return Bukkit.getWorlds().get(1);
	}
	
	public static World getWorldEnd() {
		return Bukkit.getWorlds().get(2);
	}
	
	public static boolean molesEnabled() {
		return InventoryRegister.annoncetaupes.getValue()*60 <= TaupeGun.timer;
	}
	
	public static boolean superMolesEnabled() {
		return InventoryRegister.supertaupes.getValue() && InventoryRegister.annoncetaupes.getValue()*60+1200 <= TaupeGun.timer;
	}
	
	public static UUID playerInGame(String name){
		if(name != null){
			for(PlayerTaupe player : PlayerTaupe.getAlivePlayerManager()) {
				if(Bukkit.getOfflinePlayer(player.getUuid()).getName().equals(name)) {
					return player.getUuid();
				}
			}
		}
		return null;
	}
}
