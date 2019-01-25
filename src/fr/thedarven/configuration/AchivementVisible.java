package fr.thedarven.configuration;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class AchivementVisible extends Configuration<Boolean>{

	public AchivementVisible() {
		super(false,0,0,0,Material.WORKBENCH,"Achivements visibles",new ArrayList<String>(Arrays.asList("Affiche les achivements.")));
	}

	@Override
	public String messageNom(){
		if((boolean)this.value){
			return ChatColor.GREEN+this.name+" "+ChatColor.GRAY+"Activé";
		}
		return ChatColor.GREEN+this.name+" "+ChatColor.GRAY+"Désactivé";
	}
	
	@Override
	public String messageEnlever(){
		return ChatColor.AQUA+"Désactiver";
	}
	
	@Override
	public String messageAjouter(){
		return ChatColor.AQUA+"Activer";
	}

	@Override
	public void actionEnlever() {
		Bukkit.getWorld("world").setGameRuleValue("announceAdvancements", "false");
		Bukkit.getWorld("world_nether").setGameRuleValue("announceAdvancements", "false");
		Bukkit.getWorld("world_the_end").setGameRuleValue("announceAdvancements", "false");
		this.value = false;
	}

	@Override
	public void actionAjouter() {
		Bukkit.getWorld("world").setGameRuleValue("announceAdvancements", "true");
		Bukkit.getWorld("world_nether").setGameRuleValue("announceAdvancements", "true");
		Bukkit.getWorld("world_the_end").setGameRuleValue("announceAdvancements", "true");
		this.value = true;
	}
}
