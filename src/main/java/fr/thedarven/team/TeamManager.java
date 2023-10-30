package fr.thedarven.team;

import fr.thedarven.TaupeGun;
import fr.thedarven.model.Manager;
import fr.thedarven.game.model.enums.EnumGameState;
import fr.thedarven.utils.languages.LanguageBuilder;

import java.util.Objects;

public class TeamManager extends Manager {

    private static String SPECTATOR_TEAM_NAME = null;
    private static String MOLE_TEAM_NAME = null;
    private static String SUPERMOLE_TEAM_NAME = null;

    public TeamManager(TaupeGun main) {
        super(main);
    }

    /**
     * Récupérer le nom de l'équipe des spectateurs
     *
     * @return Le nom de l'équipe des spectateurs
     */
    public String getSpectatorTeamName() {
        if (EnumGameState.isCurrentState(EnumGameState.LOBBY, EnumGameState.WAIT)) {
            return LanguageBuilder.getContent("TEAM", "spectatorTeamName", true);
        }
        if (Objects.isNull(SPECTATOR_TEAM_NAME)) {
            SPECTATOR_TEAM_NAME = LanguageBuilder.getContent("TEAM", "spectatorTeamName", true);
        }
        return SPECTATOR_TEAM_NAME;
    }

    /**
     * Récupérer le nom de l'équipe des taupes
     *
     * @return Le nom de l'équipe des taupes
     */
    public String getMoleTeamName() {
        if (EnumGameState.isCurrentState(EnumGameState.LOBBY, EnumGameState.WAIT)) {
            return LanguageBuilder.getContent("TEAM", "moleTeamName", true);
        }
        if (Objects.isNull(MOLE_TEAM_NAME)) {
            MOLE_TEAM_NAME = LanguageBuilder.getContent("TEAM", "moleTeamName", true);
        }
        return MOLE_TEAM_NAME;
    }

    /**
     * Récupérer le nom de l'équipe des supertaupes
     *
     * @return Le nom de l'équipe des supertaupes
     */
    public String getSuperMoleTeamName() {
        if (EnumGameState.isCurrentState(EnumGameState.LOBBY, EnumGameState.WAIT)) {
            return LanguageBuilder.getContent("TEAM", "superMoleTeamName", true);
        }
        if (Objects.isNull(SUPERMOLE_TEAM_NAME)) {
            SUPERMOLE_TEAM_NAME = LanguageBuilder.getContent("TEAM", "superMoleTeamName", true);
        }
        return SUPERMOLE_TEAM_NAME;
    }

}
