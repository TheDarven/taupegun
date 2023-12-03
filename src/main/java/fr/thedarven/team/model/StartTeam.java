package fr.thedarven.team.model;

import fr.thedarven.TaupeGun;
import fr.thedarven.game.model.enums.EnumGameState;
import fr.thedarven.model.enums.ColorEnum;
import fr.thedarven.player.model.PlayerTaupe;

public class StartTeam extends TeamCustom {

    public StartTeam(TaupeGun main, String name, ColorEnum color, boolean alive) {
        super(main, name, color, alive);
    }

    @Override
    public boolean joinTeam(PlayerTaupe playerTaupe) {
        if (super.joinTeam(playerTaupe)) {
            if (EnumGameState.isCurrentState(EnumGameState.LOBBY)) {
                playerTaupe.setStartTeam(this);
            }
            return true;
        }
        return false;
    }

}
