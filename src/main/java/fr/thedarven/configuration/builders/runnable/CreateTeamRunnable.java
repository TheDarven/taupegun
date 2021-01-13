package fr.thedarven.configuration.builders.runnable;

import fr.thedarven.TaupeGun;
import fr.thedarven.models.PlayerTaupe;
import fr.thedarven.models.TeamCustom;
import fr.thedarven.utils.UtilsClass;
import fr.thedarven.utils.api.Title;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.messages.MessagesClass;
import fr.thedarven.utils.teams.TeamUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CreateTeamRunnable extends BukkitRunnable {

    private TaupeGun main;
    private PlayerTaupe pl;
    private Player p;

    public CreateTeamRunnable(TaupeGun main, PlayerTaupe pl, Player p) {
        this.main = main;
        this.pl = pl;
        this.p = p;
    }

    @Override
    public void run() {
        p.openInventory(TaupeGun.getInstance().getInventoryRegister().choisirCouleurEquipe.getInventory());
        if (pl.getCreateTeamName() == null) {
            p.closeInventory();
            return;
        }
        if (pl.getCreateTeamName().length() > 16){
            p.closeInventory();
            Title.sendActionBar(p, ChatColor.RED + LanguageBuilder.getContent("TEAM", "nameTooLong", true));
            pl.setCreateTeamName(null);
            return;
        }

        if (TeamUtils.getAllSpectatorTeamName().contains(pl.getCreateTeamName()) || UtilsClass.startsWith(pl.getCreateTeamName(), TeamUtils.getAllMoleTeamName())
                || UtilsClass.startsWith(pl.getCreateTeamName(), TeamUtils.getAllSuperMoleTeamName())
                || pl.getCreateTeamName().equals(LanguageBuilder.getContent("TEAM", "nameChoice", true))){
            p.closeInventory();
            MessagesClass.CannotTeamCreateNameAlreadyMessage(p);
            pl.setCreateTeamName(null);
            return;
        }

        TeamCustom.board.getTeams()
                .stream()
                .filter(team -> pl.getCreateTeamName().equals(team.getName()))
                .findFirst()
                .ifPresent(team-> {
                    p.closeInventory();
                    MessagesClass.CannotTeamCreateNameAlreadyMessage(p);
                    pl.setCreateTeamName(null);
                });
    }
}
