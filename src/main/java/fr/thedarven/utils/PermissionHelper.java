package fr.thedarven.utils;

import org.bukkit.entity.Player;

public class PermissionHelper {

    public static final String EDIT_CONFIGURATION = "taupegun.scenarios";
    public static final String SAY_COMMAND = "taupegun.say";
    public static final String TIMER_COMMAND = "taupegun.timer";
    public static final String START_COMMAND = "taupegun.start";
    public static final String REVIVE_COMMAND = "taupegun.revive";
    public static final String PLAYER_KILL_COMMAND = "taupegun.playerkill";
    public static final String HEAL_COMMAND = "taupegun.heal";
    public static final String G_COMMAND = "taupegun.g";

    public static boolean canPlayerEditConfiguration(Player player) {
        return player.isOp() || player.hasPermission(EDIT_CONFIGURATION);
    }

    public static boolean canPlayerUseSayCommand(Player player) {
        return player.isOp() || player.hasPermission(SAY_COMMAND);
    }

}
