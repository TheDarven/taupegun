package fr.thedarven.utils;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;

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
}