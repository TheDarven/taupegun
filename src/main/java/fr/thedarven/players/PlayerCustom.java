package fr.thedarven.players;

import java.util.Objects;
import java.util.UUID;

import fr.thedarven.TaupeGun;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public abstract class PlayerCustom {
	protected UUID uuid;
	
	protected long lastConnection;
	protected int timePlayed;
	
	private int minedDiamond;
	private int minedIron;
	private int minedGold;
	
	private int threwArrow;
	private int usedSword;
	private double bowForceCounter;
	private double inflictedArrowDamage;
	private double inflictedSwordDamage;
	private double inflictedDamage;
	private double receivedArrowDamage;
	private double receivedSwordDamage;
	private double receivedDamage;
	
	private double healedLife;
	private int ateGoldenApple;
	
	public PlayerCustom(UUID uuid) {
		this.uuid = uuid;
		
		lastConnection = TaupeGun.getInstance().getDatabaseManager().getLongTimestamp();
		timePlayed = 0;
		
		minedDiamond = 0;
		minedIron = 0;
		minedGold = 0;
		
		threwArrow = 0;
		usedSword = 0;
		bowForceCounter = 0;
		inflictedArrowDamage = 0;
		inflictedSwordDamage = 0;
		inflictedDamage = 0;
		receivedArrowDamage = 0;
		receivedSwordDamage = 0;
		receivedDamage = 0;
		
		healedLife = 0;
		ateGoldenApple = 0;
	}
	
	
	/*
	 * UTILS
	 */
	public UUID getUuid() {
		return this.uuid;
	}
	
	public Player getPlayer(){
		return Bukkit.getPlayer(this.uuid);
	}

	public OfflinePlayer getOfflinePlayer() {
		return Bukkit.getOfflinePlayer(this.uuid);
	}
	
	public boolean isOnline(){
		return Objects.nonNull(getPlayer());
	}
	
	
	
	/*
	 * STATS
	 */
	public long getLastConnection() {
		return this.lastConnection;
	}
	
	public int getTimePlayed() {
		return this.timePlayed;
	}
	
	public int getUpdatedTimePlayed() {
		int returnValue = this.timePlayed;
		if(isOnline())
			returnValue += (int)(TaupeGun.getInstance().getDatabaseManager().getLongTimestamp() - this.lastConnection);
		return returnValue;
	}
	
	public void setLastConnection() {
		this.lastConnection = TaupeGun.getInstance().getDatabaseManager().getLongTimestamp();
	}
	
	public void addTimePlayed(int amount) {
		this.timePlayed += amount;
	}


	public int getMinedDiamond() {
		return minedDiamond;
	}


	public int getMinedIron() {
		return minedIron;
	}


	public int getMinedGold() {
		return minedGold;
	}


	public int getThrewArrow() {
		return threwArrow;
	}


	public int getUsedSword() {
		return usedSword;
	}


	public double getInflictedArrowDamage() {
		return inflictedArrowDamage;
	}


	public double getInflictedSwordDamage() {
		return inflictedSwordDamage;
	}


	public double getInflictedDamage() {
		return inflictedDamage;
	}


	public double getReceivedArrowDamage() {
		return receivedArrowDamage;
	}


	public double getReceivedSwordDamage() {
		return receivedSwordDamage;
	}


	public double getReceivedDamage() {
		return receivedDamage;
	}


	public double getHealedLife() {
		return healedLife;
	}


	public int getAteGoldenApple() {
		return ateGoldenApple;
	}

	
	public double getBowForceCounter() {
		return bowForceCounter;
	}
	
	
	

	public void addMinedDiamond(int minedDiamond) {
		this.minedDiamond += minedDiamond;
	}


	public void addMinedIron(int minedIron) {
		this.minedIron += minedIron;
	}


	public void addMinedGold(int minedGold) {
		this.minedGold += minedGold;
	}


	public void addThrowedArrow(int throwedArrow) {
		this.threwArrow += throwedArrow;
	}


	public void addUsedSword(int usedSword) {
		this.usedSword += usedSword;
	}


	public void addInflictedArrowDamage(double inflictedArrowDamage) {
		this.inflictedArrowDamage += inflictedArrowDamage;
	}


	public void addInflictedSwordDamage(double inflictedSwordDamage) {
		this.inflictedSwordDamage += inflictedSwordDamage;
	}


	public void addInflictedDamage(double inflictedDamage) {
		this.inflictedDamage += inflictedDamage;
	}


	public void addReceivedArrowDamage(double receivedArrowDamage) {
		this.receivedArrowDamage += receivedArrowDamage;
	}


	public void addReceivedSwordDamage(double receivedSwordDamage) {
		this.receivedSwordDamage += receivedSwordDamage;
	}


	public void addReceivedDamage(double receivedDamage) {
		this.receivedDamage += receivedDamage;
	}


	public void addHealedLife(double healedLife) {
		this.healedLife += healedLife;
	}


	public void addAteGoldenApple(int ateGoldenApple) {
		this.ateGoldenApple += ateGoldenApple;
	}


	public void addBowForceCounter(double bowForceCounter) {
		this.bowForceCounter += bowForceCounter;
	}

}
