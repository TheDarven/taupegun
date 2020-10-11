package fr.thedarven.utils;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.main.TaupeGun;
import fr.thedarven.main.metier.EnumGameState;
import fr.thedarven.main.metier.PlayerTaupe;
import fr.thedarven.utils.messages.MessagesClass;

public class FireworkWin {
	static int task;
	static int timer = 10;
	
	public static void start() {
		if(EnumGameState.isCurrentState(EnumGameState.END_FIREWORK)){
			Bukkit.getScheduler().runTaskTimer(TaupeGun.instance, new Runnable(){
				@Override
	            public void run() {
					for(Player player : Bukkit.getOnlinePlayers()) {
						if(PlayerTaupe.getPlayerManager(player.getUniqueId()).isAlive()) {
							Firework f = (Firework) player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
						    FireworkMeta fM = ((Firework) f).getFireworkMeta();
						    FireworkEffect effect = FireworkEffect.builder()
						    .flicker(true)
						    .withColor(Color.GREEN)
						    .withFade(Color.WHITE)
						    .with(FireworkEffect.Type.BALL)
						    .trail(true)
						    .build();
						       
						    fM.setPower(3);
						    fM.addEffect(effect);
						    f.setFireworkMeta(fM);
						    f.detonate();
						}
					}
					if(timer == 0){
						informationEnd();
						Bukkit.getScheduler().cancelAllTasks();
						EnumGameState.setState(EnumGameState.END);
						timer = 11;
			       }else{
			    	   timer --;
			        }
				}

				private void informationEnd() {
					MessagesClass.FinalTaupeAnnonceMessage();
					if(InventoryRegister.supertaupes.getValue()) {
						MessagesClass.FinalSuperTaupeAnnonceMessage();
					}
					MessagesClass.FinalKillAnnonceMessage();
				}
			},5,5);
		}
	}
}
