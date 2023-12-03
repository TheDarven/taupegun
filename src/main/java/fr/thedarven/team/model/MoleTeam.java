package fr.thedarven.team.model;

import fr.thedarven.TaupeGun;
import fr.thedarven.model.enums.ColorEnum;
import fr.thedarven.player.model.PlayerTaupe;

import java.util.List;
import java.util.stream.Collectors;

public class MoleTeam extends TeamCustom {

    private final int number;

    public MoleTeam(TaupeGun main, String name, ColorEnum color, boolean alive, int number) {
        super(main, name, color, alive);
        this.number = number;
    }

    public int getTeamNumber() {
        return this.number;
    }

    /**
     * @return The list of player for whom this team is their mole team
     */
    public List<PlayerTaupe> getMolePlayers() {
        return PlayerTaupe.getAllPlayerManager()
                .stream()
                .filter(p -> p.getTaupeTeam() == this)
                .collect(Collectors.toList());
    }

}
