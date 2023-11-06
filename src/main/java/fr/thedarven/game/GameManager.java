package fr.thedarven.game;

import fr.thedarven.TaupeGun;
import fr.thedarven.game.model.GameRecap;
import fr.thedarven.game.model.PveDeathRecap;
import fr.thedarven.game.runnable.EndGameRunnable;
import fr.thedarven.game.runnable.GameRunnable;
import fr.thedarven.model.Manager;

import fr.thedarven.game.model.enums.EnumGameState;
import fr.thedarven.player.model.PlayerTaupe;
import fr.thedarven.stats.model.dto.GameDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameManager extends Manager {

	private int timer = 0;
	private int cooldownTimer = 10;
	private final List<GameRecap> gameRecaps = new ArrayList<>();
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

	public long countPveDeath() {
		return this.gameRecaps.stream()
				.filter(recap -> recap instanceof PveDeathRecap)
				.count();
	}

	public void addToRecap(GameRecap recap) {
		this.gameRecaps.add(recap);
	}

	public List<GameRecap> getGameRecaps() {
		return this.gameRecaps;
	}

	public void startGame() {
		EnumGameState.setState(EnumGameState.GAME);
		this.gameRunnable = new GameRunnable(this.main, this);
		gameRunnable.runTaskTimer(this.main,20,20);
	}

	public void endGame() {
		if (Objects.nonNull(this.gameRunnable)) {
			this.gameRunnable.cancel();
		}
		main.getDatabaseManager().updateGameDuration();

		GameDto.endGames();
		EndGameRunnable endGameRunnable = new EndGameRunnable(this.main);
		endGameRunnable.runTaskTimer(this.main,5,5);
	}

	public void setMolesInDb() {
		for (PlayerTaupe pl : PlayerTaupe.getAllPlayerManager()) {
			if (!pl.isTaupe())
				continue;

			if (pl.isSuperTaupe()) {
				this.main.getDatabaseManager()
						.updateMoleMole(pl.getTaupeTeam().getTeamNumber(), pl.getSuperTaupeTeam().getTeamNumber(), pl.getUuid().toString());
			} else {
				this.main.getDatabaseManager()
						.updateMoleMole(pl.getTaupeTeam().getTeamNumber(), 0, pl.getUuid().toString());
			}
		}
	}

	public boolean areMolesRevealed() {
		return this.main.getScenariosManager().molesActivation.getValue() <= this.main.getGameManager().getTimer();
	}

	public boolean areSuperMolesRevealed() {
		return this.main.getScenariosManager().superMoles.getValue() && this.main.getScenariosManager().molesActivation.getValue() + 1200 <= this.main.getGameManager().getTimer();
	}

}
