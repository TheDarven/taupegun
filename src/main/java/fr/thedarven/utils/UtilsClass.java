package fr.thedarven.utils;

import fr.thedarven.TaupeGun;
import fr.thedarven.models.enums.EnumGameState;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.List;

public class UtilsClass {
	
	public static boolean startsWith(String text, List<String> testString) {
		for(String test: testString) {
			if(text.startsWith(test))
				return true;
		}
		return false;
	}
	
	public static boolean molesEnabled() {
		return TaupeGun.getInstance().getScenariosManager().molesActivation.getValue() <= TaupeGun.getInstance().getGameManager().getTimer();
	}
	
	public static boolean superMolesEnabled() {
		return TaupeGun.getInstance().getScenariosManager().superMoles.getValue() && TaupeGun.getInstance().getScenariosManager().molesActivation.getValue()+1200 <= TaupeGun.getInstance().getGameManager().getTimer();
	}
	
	public static void clearPlayer(Player p) {
		if(p.getOpenInventory() != null)
			p.getOpenInventory().setCursor(null);
		p.getInventory().clear();
		p.getInventory().setArmorContents(null);
		p.closeInventory();
	}
	
	public static void openConfigInventory(Player p) {
		if ((p.isOp() || p.hasPermission("taupegun.scenarios")) && EnumGameState.isCurrentState(EnumGameState.LOBBY))
			p.openInventory(TaupeGun.getInstance().getScenariosManager().menu.getInventory());
		else if(TaupeGun.getInstance().getScenariosManager().scenariosVisible.getValue())
			p.openInventory(TaupeGun.getInstance().getScenariosManager().configurationMenu.getInventory());
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
