package fr.thedarven.configuration;

import java.util.ArrayList;
import org.bukkit.Material;
import org.bukkit.event.Listener;

import org.bukkit.plugin.PluginManager;
import fr.thedarven.main.TaupeGun;


public abstract class Configuration<T> implements Listener{
	
	public T value;
	public int minimal;
	public int maximal;
	public int pas;
	public Material item;
	public String name;
	public ArrayList<String> description;

	public Configuration(T value, int minimal, int maximal, int pas, Material item, String name, ArrayList<String> description){
		this.value = value;
		this.minimal = minimal;
		this.maximal = maximal;
		this.pas = pas;
		this.item = item;
		this.name = name;
		this.description = description;
		PluginManager pm = TaupeGun.getInstance().getServer().getPluginManager();
		pm.registerEvents(this, TaupeGun.getInstance());
	}
	
	public abstract String messageNom();
	
	public abstract String messageEnlever();
	
	public abstract String messageAjouter();
	
	public abstract void actionEnlever();
	
	public abstract void actionAjouter();
}
