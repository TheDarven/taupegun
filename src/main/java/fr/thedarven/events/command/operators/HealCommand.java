package fr.thedarven.events.command.operators;

import fr.thedarven.TaupeGun;
import fr.thedarven.game.model.enums.EnumGameState;
import fr.thedarven.player.model.PlayerTaupe;
import fr.thedarven.utils.helpers.PermissionHelper;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class HealCommand extends OperatorCommand {

	public HealCommand(TaupeGun main) {
		super(main, new EnumGameState[]{ EnumGameState.GAME }, new String[]{PermissionHelper.HEAL_COMMAND});
	}

	@Override
	public void executeCommand(Player sender, PlayerTaupe pl, Command cmd, String alias, String[] args) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 20, 100, true, false));
		}
		Bukkit.broadcastMessage("§6[TaupeGun] §a" + LanguageBuilder.getContent("COMMAND", "heal",true));
	}
}
