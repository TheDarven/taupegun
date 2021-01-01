package fr.thedarven.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Permet de désactiver l'affichage des coordonnées dans le F3
 *
 * @author RolynDev
 * @author https://www.spigotmc.org/resources/nopos.11692/
 */
public class DisableF3 {
	public static void disableF3(Player player) {
		try {
	    	Class<?> packetClass = getNMSClass("PacketPlayOutEntityStatus");
			Constructor<?> packetConstructor = packetClass.getConstructor(new Class[] { getNMSClass("Entity"), Byte.TYPE });
			Object packet = packetConstructor.newInstance(new Object[] { getHandle(player), Byte.valueOf((byte) 22) });
			Method sendPacket = getNMSClass("PlayerConnection").getMethod("sendPacket", new Class[] { getNMSClass("Packet") });
			sendPacket.invoke(getConnection(player), new Object[] { packet });
		} catch (ClassNotFoundException e) {
			Bukkit.getConsoleSender().sendMessage("§c[§6No§ePos§c] §4Error: ClassNotFound !");
		} catch (NoSuchMethodException e) {
			Bukkit.getConsoleSender().sendMessage("§c[§6No§ePos§c] §4Error: NoSuchMethod !");
		} catch (SecurityException e) {
			Bukkit.getConsoleSender().sendMessage("§c[§6No§ePos§c] §4Error: SecurityException !");
		} catch (InstantiationException e) {
			Bukkit.getConsoleSender().sendMessage("§c[§6No§ePos§c] §4Error: InstantiationException !");
		} catch (IllegalAccessException e) {
			Bukkit.getConsoleSender().sendMessage("§c[§6No§ePos§c] §4Error: IllegalAccess !");
		} catch (IllegalArgumentException e) {
			Bukkit.getConsoleSender().sendMessage("§c[§6No§ePos§c] §4Error: IllegalArgument !");
		} catch (InvocationTargetException e) {
			Bukkit.getConsoleSender().sendMessage("§c[§6No§ePos§c] §4Error: InvocationTarget !");
		} catch (NoSuchFieldException e) {
			Bukkit.getConsoleSender().sendMessage("§c[§6No§ePos§c] §4Error: NoSuchField !");
		}
	}
  
	public static void enableF3(Player player) {
		try {
			Class<?> packetClass = getNMSClass("PacketPlayOutEntityStatus");
			Constructor<?> packetConstructor = packetClass.getConstructor(new Class[] { getNMSClass("Entity"), Byte.TYPE });
			Object packet = packetConstructor.newInstance(new Object[] { getHandle(player), Byte.valueOf((byte) 23) });
			Method sendPacket = getNMSClass("PlayerConnection").getMethod("sendPacket", new Class[] { getNMSClass("Packet") });
			sendPacket.invoke(getConnection(player), new Object[] { packet });
		} catch (ClassNotFoundException e) {
			Bukkit.getConsoleSender().sendMessage("§c[§6No§ePos§c] §4Error: ClassNotFound !");
		} catch (NoSuchMethodException e) {
			Bukkit.getConsoleSender().sendMessage("§c[§6No§ePos§c] §4Error: NoSuchMethod !");
		} catch (SecurityException e) {
			Bukkit.getConsoleSender().sendMessage("§c[§6No§ePos§c] §4Error: SecurityException !");
		} catch (InstantiationException e) {
			Bukkit.getConsoleSender().sendMessage("§c[§6No§ePos§c] §4Error: InstantiationException !");
		} catch (IllegalAccessException e) {
			Bukkit.getConsoleSender().sendMessage("§c[§6No§ePos§c] §4Error: IllegalAccess !");
		} catch (IllegalArgumentException e) {
			Bukkit.getConsoleSender().sendMessage("§c[§6No§ePos§c] §4Error: IllegalArgument !");
		} catch (InvocationTargetException e) {
			Bukkit.getConsoleSender().sendMessage("§c[§6No§ePos§c] §4Error: InvocationTarget !");
		} catch (NoSuchFieldException e) {
			Bukkit.getConsoleSender().sendMessage("§c[§6No§ePos§c] §4Error: NoSuchField !");
		}
	}
  
	private static Class<?> getNMSClass(String nmsClassString) throws ClassNotFoundException {
		String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
		String name = "net.minecraft.server." + version + nmsClassString;
		Class<?> nmsClass = Class.forName(name);
		return nmsClass;
	}
  
	private static Object getConnection(Player player) throws SecurityException, NoSuchMethodException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Field conField = getHandle(player).getClass().getField("playerConnection");
		Object con = conField.get(getHandle(player));
		return con;
	}
  
	private static Object getHandle(Player player) throws SecurityException, NoSuchMethodException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Method getHandle = player.getClass().getMethod("getHandle", new Class[0]);
		Object nmsPlayer = getHandle.invoke(player, new Object[0]);
		return nmsPlayer;
	}
}