package fr.thedarven.scenarios.runnable;

import fr.thedarven.TaupeGun;
import fr.thedarven.models.PlayerTaupe;
import fr.thedarven.models.TeamCustom;
import fr.thedarven.models.runnable.PlayerRunnable;
import fr.thedarven.utils.UtilsClass;
import fr.thedarven.utils.api.Title;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.messages.MessagesClass;
import fr.thedarven.utils.teams.TeamUtils;
import org.bukkit.entity.Player;

import java.util.Objects;

public class CreateTeamRunnable extends PlayerRunnable {

    private final TaupeGun main;
    private final Player player;

    public CreateTeamRunnable(TaupeGun main, PlayerTaupe pl, Player player) {
        super(pl);
        this.main = main;
        this.player = player;
    }

    @Override
    protected void operate() {
        this.player.openInventory(this.main.getInventoryRegister().chooseTeamColor.getInventory());

        if (Objects.isNull(this.pl.getCreateTeamName())) {
            this.player.closeInventory();
            return;
        }

        if (this.pl.getCreateTeamName().length() > 16){
            this.player.closeInventory();
            Title.sendActionBar(this.player, "§c" + LanguageBuilder.getContent("TEAM", "nameTooLong", true));
            pl.setCreateTeamName(null);
            return;
        }

        if (TeamUtils.getAllSpectatorTeamName().contains(this.pl.getCreateTeamName()) || UtilsClass.startsWith(this.pl.getCreateTeamName(), TeamUtils.getAllMoleTeamName())
                || UtilsClass.startsWith(this.pl.getCreateTeamName(), TeamUtils.getAllSuperMoleTeamName())
                || this.pl.getCreateTeamName().equals(LanguageBuilder.getContent("TEAM", "nameChoice", true))){
            this.player.closeInventory();
            MessagesClass.CannotTeamCreateNameAlreadyMessage(this.player);
            this.pl.setCreateTeamName(null);
            return;
        }

        TeamCustom.board.getTeams()
                .stream()
                .filter(team -> this.pl.getCreateTeamName().equals(team.getName()))
                .findFirst()
                .ifPresent(team-> {
                    this.player.closeInventory();
                    MessagesClass.CannotTeamCreateNameAlreadyMessage(this.player);
                    this.pl.setCreateTeamName(null);
                });
    }
}
