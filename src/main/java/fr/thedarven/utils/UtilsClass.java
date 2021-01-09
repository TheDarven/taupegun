package fr.thedarven.utils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.TaupeGun;
import fr.thedarven.main.metier.EnumGameState;
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
		return TaupeGun.getInstance().getInventoryRegister().annoncetaupes.getValue() <= TaupeGun.getInstance().getGameManager().getTimer();
	}
	
	public static boolean superMolesEnabled() {
		return TaupeGun.getInstance().getInventoryRegister().supertaupes.getValue() && TaupeGun.getInstance().getInventoryRegister().annoncetaupes.getValue()+1200 <= TaupeGun.getInstance().getGameManager().getTimer();
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
	
	public static void clearPlayer(Player p) {
		if(p.getOpenInventory() != null)
			p.getOpenInventory().setCursor(null);
		p.getInventory().clear();
		p.getInventory().setArmorContents(null);
		p.closeInventory();
	}
	
	public static void openConfigInventory(Player p) {
		if((p.isOp() || p.hasPermission("taupegun.scenarios")) && EnumGameState.isCurrentState(EnumGameState.LOBBY))
			p.openInventory(TaupeGun.getInstance().getInventoryRegister().menu.getInventory());
		else if(TaupeGun.getInstance().getInventoryRegister().scenariosvisibles.getValue())
			p.openInventory(TaupeGun.getInstance().getInventoryRegister().configuration.getInventory());
	}

	public static int getTimestamp() {
		long date = new Date().getTime();
		return (int) ((date-(date%100))/1000);
	}

	public static long getLongTimestamp() {
		long date = new Date().getTime();
		return (date-(date%100))/1000;
	}
}
