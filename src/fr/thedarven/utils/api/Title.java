package fr.thedarven.utils.api;

import java.lang.reflect.Field;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction;
import net.minecraft.server.v1_8_R3.PlayerConnection;

public class Title {
	
	public static void title(Player p, String title, String subTitle, int ticks){
		
		IChatBaseComponent chatTitle = ChatSerializer.a("{\"text\": \"" + title + "\"}");
		IChatBaseComponent chatsubTitle = ChatSerializer.a("{\"text\": \"" + subTitle + "\"}");
		
		PacketPlayOutTitle titre = new PacketPlayOutTitle(EnumTitleAction.TITLE, chatTitle);
		PacketPlayOutTitle sousTitre = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, chatsubTitle);
		
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(titre);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(sousTitre);
		
		sendTime(p, ticks);
	}
	
	public static void sendTime(Player pl, int ticks){
		PacketPlayOutTitle p1 = new PacketPlayOutTitle(EnumTitleAction.TIMES, null, 20, ticks, 20);
		((CraftPlayer) pl).getHandle().playerConnection.sendPacket(p1);
	}
	
	public static void sendActionBar(Player pl, String message){
		IChatBaseComponent actionBar = ChatSerializer.a("{\"text\": \"" + message + "\"}");
		PacketPlayOutChat p2 = new PacketPlayOutChat(actionBar, (byte) 2);
		((CraftPlayer) pl).getHandle().playerConnection.sendPacket(p2);
	}
	
	public static void sendTabHF(Player player, String header, String footer){
	    CraftPlayer craftplayer = (CraftPlayer) player;
	    PlayerConnection connection = craftplayer.getHandle().playerConnection;
	    IChatBaseComponent headerJSON = ChatSerializer.a("{\"text\": \"" + header +"\"}");
	    IChatBaseComponent footerJSON = ChatSerializer.a("{\"text\": \"" + footer +"\"}");
	    PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
	  
	    try {
	        Field headerField = packet.getClass().getDeclaredField("a");
	        headerField.setAccessible(true);
	        headerField.set(packet, headerJSON);
	        headerField.setAccessible(!headerField.isAccessible());
	      
	        Field footerField = packet.getClass().getDeclaredField("b");
	        footerField.setAccessible(true);
	        footerField.set(packet, footerJSON);
	        footerField.setAccessible(!footerField.isAccessible());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    
	    connection.sendPacket(packet);
	   
		
	}
}
