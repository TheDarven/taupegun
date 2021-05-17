package fr.thedarven.messages;

import fr.thedarven.TaupeGun;
import fr.thedarven.models.Manager;
import fr.thedarven.players.PlayerTaupe;
import fr.thedarven.teams.TeamCustom;
import fr.thedarven.utils.api.titles.ActionBar;
import fr.thedarven.utils.api.titles.TabMessage;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Objects;

public class MessageManager extends Manager {

    public MessageManager(TaupeGun main) {
        super(main);
    }

    /**
     * Sends message to a player that he cannot use a command because he is not an operator.
     *
     * @param receiver player to send message to
     */
    public void sendNotOperatorMessage(Player receiver) {
        String operatorMessage = "§c" + LanguageBuilder.getContent("COMMAND", "operator", true);
        receiver.sendMessage(operatorMessage);
    }

    /**
     * Updates the list content of player.
     *
     * @param receiver player to send message to
     */
    public void updateTabContent(Player receiver) {
        String authorMessage = LanguageBuilder.getContent("SCOREBOARD", "author", true);
        new TabMessage("§6------------------\nTaupeGun\n------------------\n", "\n§2" + authorMessage + "\n§adiscord.gg/HZyS5T7").sendTabTitle(receiver);
    }

    /**
     * Sends a message that the team name is already used or is reserved by the game.
     *
     * @param receiver player to send message to
     */
    public void sendTeamNameAlreadyUsedMessage(Player receiver) {
        String nameAlreadyUsedMessage = "§c" + LanguageBuilder.getContent("TEAM", "nameAlreadyUsed", true);
        new ActionBar(nameAlreadyUsedMessage).sendActionBar(receiver);
    }

    /**
     * Sends a message with the list of moles players.
     *
     * @param receiver player to send message to
     */
    public void sendTaupeListMessage(Player receiver) {
        for (TeamCustom team : TeamCustom.getTaupeTeams()) {
            StringBuilder listTaupe = new StringBuilder("§c" + ChatColor.BOLD + team.getTeam().getName() + ": " + ChatColor.RESET + "§c");
            PlayerTaupe.getAllPlayerManager().stream()
                    .filter(pc -> pc.getTaupeTeam() == team)
                    .forEach(pc -> listTaupe.append(pc.getName()).append(" "));

            if (Objects.nonNull(receiver)) {
                receiver.sendMessage(listTaupe.toString());
            } else {
                Bukkit.broadcastMessage(listTaupe.toString());
            }
        }
    }

    /**
     * Sends a message with the list of supermoles players.
     *
     * @param receiver player to send message to
     */
    public void sendSuperTaupeListMessage(Player receiver) {
        for (TeamCustom team : TeamCustom.getSuperTaupeTeams()) {
            StringBuilder listTaupe = new StringBuilder(ChatColor.DARK_RED + "" + ChatColor.BOLD + team.getTeam().getName() + ": " + ChatColor.RESET + "" + ChatColor.DARK_RED);
            PlayerTaupe.getAllPlayerManager().stream()
                    .filter(pc -> pc.getSuperTaupeTeam() == team)
                    .forEach(pc -> listTaupe.append(pc.getName()).append(" "));

            if (Objects.nonNull(receiver)) {
                receiver.sendMessage(listTaupe.toString());
            } else {
                Bukkit.broadcastMessage(listTaupe.toString());
            }
        }
    }

    /**
     * Mole sends message on one of the moles' private chat.
     *
     * @param sender player who is sending
     * @param senderTaupe PlayerMole of the sender
     * @param words an arrays of the message content
     */
    public void moleSendsMoleMessage(Player sender, PlayerTaupe senderTaupe, String[] words) {
        if (senderTaupe.isSuperReveal()) {
            return;
        }

        String content = getMessageOfArray(words);

        String revealMolesMessage = "§e" + LanguageBuilder.getContent("EVENT_TCHAT", "teamMessage", true) + "§7" + sender.getName() + ": " + content;
        String notRevealMolesMessage = "§c" + sender.getName() + ":" + content;
        String spectatorMessage = "§c[" + senderTaupe.getTaupeTeam().getName() + "] " + sender.getName() + ":" + content;

        senderTaupe.getTaupeTeam().getTaupeTeamPlayers().stream()
                .filter(pc -> !pc.isSuperReveal() && pc.isAlive() && Objects.nonNull(pc.getPlayer()))
                .forEach(pc -> {
                    if (pc.isReveal()) {
                        // String teamMessage = "§l§7"+LanguageBuilder.getContent("EVENT_TCHAT", "teamMessage", InventoryRegister.language.getSelectedLanguage(), true)+"⋙ §r§f"+p.getName()+": "+messageCommand;
                        pc.getPlayer().sendMessage(revealMolesMessage);
                    } else {
                        pc.getPlayer().sendMessage(notRevealMolesMessage);
                    }
                });

        TeamCustom spectatorTeam = TeamCustom.getSpectatorTeam();
        if (Objects.nonNull(spectatorTeam)) {
            spectatorTeam.getConnectedPlayers()
                    .forEach(pc -> pc.getPlayer().sendMessage(spectatorMessage));
        }
    }

    /**
     * SuperMole sends message on one of the supermoles' private chat.
     *
     * @param sender player who is sending
     * @param senderTaupe PlayerMole of the sender
     * @param words an arrays of the message content
     */
    public void superMoleSendsSuperMoleMessage(Player sender, PlayerTaupe senderTaupe, String[] words) {
        String content = getMessageOfArray(words);

        String revealSuperMolesMessage = "§e" + LanguageBuilder.getContent("EVENT_TCHAT", "teamMessage", true) +
                "§7" + sender.getName() + ": " + content;
        String notRevealSuperMolesMessage = "§4" + sender.getName() + ":" + content;
        String spectatorMessage = ChatColor.DARK_RED + "[" + senderTaupe.getSuperTaupeTeam().getName() + "] " + sender.getName() + ":" + content;

        senderTaupe.getSuperTaupeTeam().getSuperTaupeTeamPlayers().stream()
                .filter(pc -> pc.isAlive() && Objects.nonNull(pc.getPlayer()))
                .forEach(pc -> {
                    if (pc.isSuperReveal()) {
                        pc.getPlayer().sendMessage(revealSuperMolesMessage);
                    } else {
                        pc.getPlayer().sendMessage(notRevealSuperMolesMessage);
                    }
                });

        TeamCustom spectatorTeam = TeamCustom.getSpectatorTeam();
        if (Objects.nonNull(spectatorTeam)) {
            spectatorTeam.getConnectedPlayers()
                    .forEach(pc -> pc.getPlayer().sendMessage(spectatorMessage));
        }
    }

    private String getMessageOfArray(String[] words) {
        StringBuilder message = new StringBuilder();
        for (String word : words) {
            message.append(" ").append(word);
        }
        return message.toString();
    }

}
