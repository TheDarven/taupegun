package fr.thedarven.team.model;

import fr.thedarven.TaupeGun;
import fr.thedarven.model.enums.ColorEnum;
import fr.thedarven.player.model.PlayerTaupe;

public class SpectatorTeam extends TeamCustom {

    public SpectatorTeam(TaupeGun main, String name, ColorEnum color) {
        super(main, name, color, false);
    }

    @Override
    public boolean isAlive() {
        return false;
    }

    @Override
    public boolean joinTeam(PlayerTaupe playerTaupe) {
        if (super.joinTeam(playerTaupe)) {
            playerTaupe.setAlive(false);
            return true;
        }
        return false;
    }

    @Override
    protected boolean canAddPlayer() {
        return true;
    }

}
