package fr.thedarven.game;

import fr.thedarven.TaupeGun;
import fr.thedarven.game.runnable.EndGameRunnable;
import fr.thedarven.game.runnable.GameRunnable;
import fr.thedarven.models.Manager;
import org.bukkit.Bukkit;

import fr.thedarven.models.EnumGameState;
import fr.thedarven.models.PlayerTaupe;
import fr.thedarven.statsgame.RestGame;

import java.util.Objects;

public class GameManager extends Manager {

	private int timer = 0;
	private int cooldownTimer = 0;
	private GameRunnable gameRunnable;

	public GameManager(TaupeGun main){
		super(main);
	}

	public int getTimer(){
		return this.timer;
	}

	public void setTimer(int timer){
		this.timer = timer;
	}

	public int getCooldownTimer(){
		return this.cooldownTimer;
	}

	public void setCooldownTimer(int cooldownTimer){
		this.cooldownTimer = cooldownTimer;
	}

	public void decreaseCooldownTimer(){
		this.cooldownTimer--;
	}

	public void startGame() {
		EnumGameState.setState(EnumGameState.GAME);
		this.gameRunnable = new GameRunnable(this.main, this);
		gameRunnable.runTaskTimer(this.main,20,20);
	}

	public void endGame() {
		if (!Objects.isNull(this.gameRunnable)) {
			this.gameRunnable.cancel();
		}
		main.getDatabaseManager().updateGameDuration();

		RestGame.endGames();
		Bukkit.getScheduler().runTaskTimer(this.main, new EndGameRunnable(this.main),5,5);
	}

	public void setMolesInDb() {
		for (PlayerTaupe pl : PlayerTaupe.getAllPlayerManager()) {
			if (!pl.isTaupe())
				continue;

			if (pl.isSuperTaupe()) {
				this.main.getDatabaseManager()
						.updateMoleMole(pl.getTaupeTeam().getTaupeTeamNumber(), pl.getSuperTaupeTeam().getSuperTaupeTeamNumber(), pl.getUuid().toString());
			} else {
				this.main.getDatabaseManager()
						.updateMoleMole(pl.getTaupeTeam().getTaupeTeamNumber(), 0, pl.getUuid().toString());
			}
		}
	}
	
	/* private static void damagesEnabling() {
		if(TaupeGun.timer == 60) {
			for(Player player : Bukkit.getOnlinePlayers())
				player.playSound(player.getLocation(), Sound.WOLF_GROWL, 1, 1);

			String pvpMessage = "§e"+LanguageBuilder.getContent("GAME", "pvpIsStarting", InventoryRegister.language.getSelectedLanguage(), true);
			Bukkit.broadcastMessage(pvpMessage);
		}
	} */
}
