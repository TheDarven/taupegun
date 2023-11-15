package fr.thedarven.utils.manager;

import fr.thedarven.TaupeGun;
import fr.thedarven.game.model.GameRecap;
import fr.thedarven.game.utils.SortPlayerKill;
import fr.thedarven.kit.KitManager;
import fr.thedarven.model.Manager;
import fr.thedarven.player.model.PlayerTaupe;
import fr.thedarven.scenario.kit.InventoryKits;
import fr.thedarven.team.TeamManager;
import fr.thedarven.team.model.MoleTeam;
import fr.thedarven.team.model.SpectatorTeam;
import fr.thedarven.team.model.SuperMoleTeam;
import fr.thedarven.team.model.TeamCustom;
import fr.thedarven.utils.TextInterpreter;
import fr.thedarven.utils.api.titles.ActionBar;
import fr.thedarven.utils.api.titles.TabMessage;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;
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
        List<MoleTeam> teams = this.main.getTeamManager().getMoleTeams();
        teams.sort(TeamManager.MOLE_TEAM_NUMBER_COMPARATOR);

        for (MoleTeam team : this.main.getTeamManager().getMoleTeams()) {
            StringBuilder listTaupe = new StringBuilder(ChatColor.RED.toString())
                    .append(ChatColor.BOLD)
                    .append(team.getName())
                    .append(" : ")
                    .append(ChatColor.RESET);

            PlayerTaupe.getAllPlayerManager().stream()
                    .filter(pc -> pc.getTaupeTeam() == team)
                    .forEach(pc -> {
                        String startTeamColor = ChatColor.GRAY.toString();
                        if (pc.getStartTeam().isPresent()) {
                            startTeamColor = pc.getStartTeam().get().getColor().getColor();
                        }

                        listTaupe
                                .append(startTeamColor)
                                .append(pc.getName())
                                .append(" ");
                    });

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
        List<SuperMoleTeam> teams = this.main.getTeamManager().getSuperMoleTeams();
        teams.sort(TeamManager.SUPER_MOLE_TEAM_NUMBER_COMPARATOR);

        for (SuperMoleTeam team : teams) {
            StringBuilder listSuperTaupe = new StringBuilder(ChatColor.DARK_RED.toString())
                    .append(ChatColor.BOLD)
                    .append(team.getName())
                    .append(" : ")
                    .append(ChatColor.RESET);

            PlayerTaupe.getAllPlayerManager().stream()
                    .filter(pc -> pc.getSuperTaupeTeam() == team)
                    .forEach(pc -> {
                        String startTeamColor = ChatColor.GRAY.toString();
                        if (pc.getStartTeam().isPresent()) {
                            startTeamColor = pc.getStartTeam().get().getColor().getColor();
                        }

                        listSuperTaupe
                                .append(startTeamColor)
                                .append(pc.getName())
                                .append(" ");
                    });

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

        senderTaupe.getTaupeTeam().getMolePlayers().stream()
                .filter(pc -> !pc.isSuperReveal() && pc.isAlive() && Objects.nonNull(pc.getPlayer()))
                .forEach(pc -> {
                    if (pc.isReveal()) {
                        // String teamMessage = "§l§7"+LanguageBuilder.getContent("EVENT_TCHAT", "teamMessage", InventoryRegister.language.getSelectedLanguage(), true)+"⋙ §r§f"+p.getName()+": "+messageCommand;
                        pc.getPlayer().sendMessage(revealMolesMessage);
                    } else {
                        pc.getPlayer().sendMessage(notRevealMolesMessage);
                    }
                });

        Optional<SpectatorTeam> oSpectatorTeam = this.main.getTeamManager().getSpectatorTeam();
        oSpectatorTeam.ifPresent(teamCustom -> teamCustom.getConnectedMembers()
                .forEach(pc -> pc.getPlayer().sendMessage(spectatorMessage)));
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

        senderTaupe.getSuperTaupeTeam().getSuperMolePlayers().stream()
                .filter(pc -> pc.isAlive() && Objects.nonNull(pc.getPlayer()))
                .forEach(pc -> {
                    if (pc.isSuperReveal()) {
                        pc.getPlayer().sendMessage(revealSuperMolesMessage);
                    } else {
                        pc.getPlayer().sendMessage(notRevealSuperMolesMessage);
                    }
                });

        Optional<SpectatorTeam> oSpectatorTeam = this.main.getTeamManager().getSpectatorTeam();
        oSpectatorTeam.ifPresent(teamCustom -> teamCustom.getConnectedMembers()
                .forEach(pc -> pc.getPlayer().sendMessage(spectatorMessage)));
    }

    /**
     * Sends an error message that there are too many teams
     * @param received The player that received the message
     */
    public void sendTooManyTeamMessage(Player received) {
        Map<String, String> params = new HashMap<>();
        params.put("maxTeam", String.valueOf(TeamCustom.MAX_TEAM_AMOUNT));
        String tooManyTeamMessage = TextInterpreter.textInterpretation(LanguageBuilder.getContent("TEAM", "tooManyTeams", true), params);

        new ActionBar(String.format("%s%s", ChatColor.RED, tooManyTeamMessage)).sendActionBar(received);
    }

    /**
     * Sends an error message that there are too many kits
     * @param received The player that received the message
     */
    public void sendTooManyKitMessage(Player received) {
        Map<String, String> params = new HashMap<>();
        params.put("maxKit", String.valueOf(KitManager.MAX_KIT_AMOUNT));
        String tooManyKitMessage = TextInterpreter.textInterpretation(InventoryKits.TOO_MANY_KITS, params);

        new ActionBar(String.format("%s%s", ChatColor.RED, tooManyKitMessage)).sendActionBar(received);
    }

    public void broadcastDeathMessage(PlayerTaupe deathPlayerTaupe) {
        ChatColor startTeamColor = ChatColor.GRAY;
        if (deathPlayerTaupe.getStartTeam().isPresent()) {
            startTeamColor = deathPlayerTaupe.getStartTeam().get().getColor().getChatColor();
        }
        Map<String, String> params = new HashMap<>();
        params.put("playerName", String.format("%s%s%s", startTeamColor, deathPlayerTaupe.getName(), ChatColor.RESET));
        String deathAllMessage = TextInterpreter.textInterpretation(LanguageBuilder.getContent("EVENT_DEATH", "deathAll", true), params);
        Bukkit.broadcastMessage(deathAllMessage);
    }

    private String getMessageOfArray(String[] words) {
        StringBuilder message = new StringBuilder();
        for (String word : words) {
            message.append(" ").append(word);
        }
        return message.toString();
    }

}
