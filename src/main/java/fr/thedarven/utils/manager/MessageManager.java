package fr.thedarven.utils.manager;

import fr.thedarven.TaupeGun;
import fr.thedarven.game.model.GameRecap;
import fr.thedarven.game.utils.SortPlayerKill;
import fr.thedarven.model.Manager;
import fr.thedarven.player.model.PlayerTaupe;
import fr.thedarven.team.model.TeamCustom;
import fr.thedarven.utils.TextInterpreter;
import fr.thedarven.utils.api.titles.ActionBar;
import fr.thedarven.utils.api.titles.TabMessage;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
            StringBuilder listTaupe = new StringBuilder(ChatColor.RED.toString())
                    .append(ChatColor.BOLD)
                    .append(team.getTeam().getName())
                    .append(" : ")
                    .append(ChatColor.RESET);

            PlayerTaupe.getAllPlayerManager().stream()
                    .filter(pc -> pc.getTaupeTeam() == team)
                    .forEach(pc -> listTaupe
                            .append(pc.getStartTeam().getColorEnum().getColor())
                            .append(pc.getName())
                            .append(" "));

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
            StringBuilder listSuperTaupe = new StringBuilder(ChatColor.DARK_RED.toString())
                    .append(ChatColor.BOLD)
                    .append(team.getTeam().getName())
                    .append(" : ")
                    .append(ChatColor.RESET);

            PlayerTaupe.getAllPlayerManager().stream()
                    .filter(pc -> pc.getSuperTaupeTeam() == team)
                    .forEach(pc -> listSuperTaupe
                            .append(pc.getStartTeam().getColorEnum().getColor())
                            .append(pc.getName())
                            .append(" "));

            if (Objects.nonNull(receiver)) {
                receiver.sendMessage(listSuperTaupe.toString());
            } else {
                Bukkit.broadcastMessage(listSuperTaupe.toString());
            }
        }
    }

    /**
     * Sends into server chat the recap of the game.
     */
    public void sendGameRecapMessage() {
        List<GameRecap> recaps = this.main.getGameManager().getGameRecaps();
        String recapListMessage = String.format("§l§6%s", LanguageBuilder.getContent("CONTENT", "recapList", true));
        Bukkit.broadcastMessage(" ");
        Bukkit.broadcastMessage(recapListMessage);
        for (GameRecap recap: recaps) {
            Bukkit.broadcastMessage(recap.getMessage());
        }
    }

    /**
     * Sends into server chat the number of kills of each player.
     */
    public void sendKillRankingMessage() {
        String killListMessage = String.format("§l§6%s", LanguageBuilder.getContent("CONTENT", "killList", true));
        Bukkit.broadcastMessage(" ");
        Bukkit.broadcastMessage(killListMessage);
        List<PlayerTaupe> kills = PlayerTaupe.getAllPlayerManager().stream()
                .filter(pc -> pc.getKill() > 0)
                .sorted(new SortPlayerKill())
                .collect(Collectors.toList());
        kills.forEach(pc -> Bukkit.broadcastMessage(String.format("%s%s%s : %s%s", ChatColor.BOLD, ChatColor.GREEN, pc.getName(), ChatColor.RESET, pc.getKill())));

        String killPveMessage = String.format("§2%s", LanguageBuilder.getContent("CONTENT", "killPve", true));
        Map<String, String> params = new HashMap<>();
        params.put("amount", String.valueOf(this.main.getGameManager().countPveDeath()));
        Bukkit.broadcastMessage(TextInterpreter.textInterpretation(killPveMessage, params));
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
