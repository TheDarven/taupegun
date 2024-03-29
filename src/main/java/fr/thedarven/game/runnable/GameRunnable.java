package fr.thedarven.game.runnable;

import fr.thedarven.TaupeGun;
import fr.thedarven.game.GameManager;
import fr.thedarven.model.enums.ColorEnum;
import fr.thedarven.game.model.enums.EnumGameState;
import fr.thedarven.player.model.PlayerTaupe;
import fr.thedarven.scenario.builder.OptionNumeric;
import fr.thedarven.team.model.SpectatorTeam;
import fr.thedarven.team.model.StartTeam;
import fr.thedarven.team.model.TeamCustom;
import fr.thedarven.utils.api.DisableF3;
import fr.thedarven.utils.TextInterpreter;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GameRunnable extends BukkitRunnable {

    final private static int SECONDS_BEFORE_SUPERMOLE_ANNOUNCING = 1200;

    private final TaupeGun main;

    private final GameManager gameManager;

    public GameRunnable(TaupeGun main, GameManager gameManager) {
        this.main = main;
        this.gameManager = gameManager;
    }

    @Override
    public void run() {
        if (this.gameManager.getTimer() == 0){
            this.initGame();
        }

        if (this.gameManager.getTimer() == 2){
            this.gameManager.setMolesInDb();
        }

        this.pvpEnabling();
        this.episodeAnnouncing();
        this.molesAnnouncing();
        this.wallAnnouncing();

        this.processNotAlivePlayers();

        if (EnumGameState.isCurrentState(EnumGameState.GAME)) {
            this.main.getTeamDeletionManager().start();
        } else if (EnumGameState.isCurrentState(EnumGameState.END_FIREWORK)){
            this.gameManager.endGame();
        }

        this.gameManager.setTimer(this.gameManager.getTimer() + 1);
    }

    /**
     * Téléporte les joueurs et initie le monde
     */
    private void initGame() {
        World world = this.main.getWorldManager().getWorld();
        if (Objects.isNull(world)) {
            String worldNotExistMessage = "§e" + LanguageBuilder.getContent("START_COMMAND", "worldNotExist", true);
            Bukkit.broadcastMessage(worldNotExistMessage);
            return;
        }

        this.main.getDatabaseManager().createGame();

        world.setGameRuleValue("doMobSpawning", "true");
        if (this.main.getScenariosManager().daylightCycle.getValue())
            world.setGameRuleValue("doDaylightCycle", "true");
        world.getWorldBorder().setDamageAmount(this.main.getScenariosManager().wallDamage.getValue());
        world.setTime(0);

        this.initGamePlayers();

        this.teleportPlayers(world);

        new SpectatorTeam(this.main, this.main.getTeamManager().getSpectatorTeamName(), ColorEnum.WHITE);

        this.main.getWorldManager().destroyLobby();
    }

    /**
     * Annonce de l'activation du PVP
     */
    private void pvpEnabling() {
        if (!this.main.getScenariosManager().pvp.isValueEquals(this.gameManager.getTimer()))
            return;

        this.main.getPlayerManager().sendPlaySound(Sound.WOLF_GROWL);

        String pvpMessage = "§e" + LanguageBuilder.getContent("GAME", "pvpIsStarting",true);
        Bukkit.broadcastMessage(pvpMessage);
    }

    /**
     * Annonce du début d'un épisode
     */
    private void episodeAnnouncing() {
        int timer = gameManager.getTimer();
        if (this.main.getScenariosManager().episode.isValueGreater(0) && timer != 0 && timer % this.main.getScenariosManager().episode.getValue() == 0) {
            this.main.getPlayerManager().sendPlaySound(Sound.ORB_PICKUP);
            int episodeNumber = (int) (timer / (this.main.getScenariosManager().episode.getValue())) + 1;

            Map<String, String> params = new HashMap<>();
            params.put("episodeNumber", Integer.toString(episodeNumber));
            String episodeMessage = TextInterpreter.textInterpretation("§e" + LanguageBuilder.getContent("GAME", "episodeIsStarting",true), params);
            Bukkit.broadcastMessage(episodeMessage);
        }
    }

    /**
     * Messages de l'annonce des taupes
     */
    public void molesAnnouncing() {
        int timer = this.gameManager.getTimer();
        OptionNumeric molesAnnouncing = this.main.getScenariosManager().molesActivation;

        // 5s AVANT L'ANNONCE DES TAUPES
        if (molesAnnouncing.isValueLowerOrEquals(timer + 6) && molesAnnouncing.isValueGreaterOrEquals(timer + 1)) {
            if (molesAnnouncing.isValueEquals(timer + 6)) {
                String moleAnnouncement = "§a" + LanguageBuilder.getContent("GAME", "moleAnnouncement",true);
                Bukkit.broadcastMessage(moleAnnouncement);
            }
            this.main.getPlayerManager().sendPlaySound(Sound.ORB_PICKUP);
        }

        // ANNONCE DES TAUPES
        if (molesAnnouncing.isValueEquals(timer + 1)) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1, 1);
                PlayerTaupe playerTaupe = PlayerTaupe.getPlayerManager(player.getUniqueId());
                if (playerTaupe.isTaupe()) {
                    sendMoleInfoMessage(player, playerTaupe);
                }
            }
        }


        if (!this.main.getScenariosManager().superMoles.getValue())
            return;

        // 5s AVANT L'ANNONCE DES SUPER TAUPES
        if (molesAnnouncing.isValueLowerOrEquals(timer - SECONDS_BEFORE_SUPERMOLE_ANNOUNCING + 6)
                && molesAnnouncing.isValueGreaterOrEquals(timer - SECONDS_BEFORE_SUPERMOLE_ANNOUNCING + 1)) {
            if (molesAnnouncing.isValueEquals(timer - SECONDS_BEFORE_SUPERMOLE_ANNOUNCING + 6)) {
                String superMoleAnnouncement = "§a" + LanguageBuilder.getContent("GAME", "superMoleAnnouncement",true);
                Bukkit.broadcastMessage(superMoleAnnouncement);
            }
            this.main.getPlayerManager().sendPlaySound(Sound.ORB_PICKUP);
        }

        // ANNONCE DES SUPER TAUPES
        if (molesAnnouncing.isValueEquals(timer - SECONDS_BEFORE_SUPERMOLE_ANNOUNCING + 1)) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1, 1);
                if (PlayerTaupe.getPlayerManager(player.getUniqueId()).isSuperTaupe()) {
                    sendSuperMoleInfoMessage(player);
                }
            }
        }
    }

    /**
     * Messages de l'annonce de réduction du mur
     */
    private void wallAnnouncing() {
        int timer = this.gameManager.getTimer();
        OptionNumeric wallValue = this.main.getScenariosManager().wallShrinkingTime;

        // LE MUR EST A 3min //
        if (wallValue.isValueEquals(timer + 181)) {
            String wallShrinking3Minutes = "§a[TaupeGun]§f "+LanguageBuilder.getContent("GAME", "wallShrinking3Minutes",true);
            Bukkit.broadcastMessage(wallShrinking3Minutes);
            this.main.getPlayerManager().sendPlaySound(Sound.NOTE_PLING);
        }

        // LE MUR EST ENTRE 5s ET 00s //
        if (wallValue.isValueLowerOrEquals(timer + 6) && wallValue.isValueGreaterOrEquals(timer + 1)) {
            this.main.getPlayerManager().sendPlaySound(Sound.NOTE_PLING);
        }

        // LE MUR EST A 00H00 //
        if (wallValue.isValueEquals(timer + 1)) {
            this.main.getPlayerManager().sendPlaySound(Sound.ENDERDRAGON_GROWL);
            this.teleportNetherPlayers();
        }
    }





    /**
     * Initie les joueurs en début de partie
     */
    private void initGamePlayers() {
        for (PlayerTaupe playerTaupe: PlayerTaupe.getAllPlayerManager()) {
            if (playerTaupe.isOnline()) {
                Player player = playerTaupe.getPlayer();
                if (!this.main.getScenariosManager().coordonneesVisibles.getValue()) {
                    new DisableF3().disableF3(player);
                }
                this.main.getPlayerManager().clearPlayer(player);

                if (playerTaupe.getTeam() != null) {
                    this.main.getScenariosManager().startItem.giveItems(player);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 2) );
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 200, 0) );
                    player.setGameMode(GameMode.SURVIVAL);
                }
            }

            if (playerTaupe.getTeam() == null) {
                playerTaupe.setAlive(false);
            }
        }

        Bukkit.getOnlinePlayers().forEach(player -> {
            PlayerTaupe playerTaupe = PlayerTaupe.getPlayerManager(player.getUniqueId());

            if (!this.main.getScenariosManager().coordonneesVisibles.getValue()) {
                new DisableF3().disableF3(player);
            }
            this.main.getPlayerManager().clearPlayer(player);

            if (Objects.isNull(playerTaupe.getTeam())) {
                playerTaupe.setAlive(false);
            } else {
                this.main.getScenariosManager().startItem.giveItems(player);

                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 2) );
                player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 200, 0) );
                player.setGameMode(GameMode.SURVIVAL);
            }
        });
    }

    /**
     * Téléporte les joueurs à leur point de spawn
     */
    private void teleportPlayers(World world) {
        double rayon = this.main.getScenariosManager().wallSizeBefore.getRadius() - 100;
        int Z = -1;
        double X, radius = (6.283184/this.main.getTeamManager().countTeams() - this.main.getTeamManager().getMoleTeams().size() - this.main.getTeamManager().getSuperMoleTeams().size());

        for (TeamCustom team : this.main.getTeamManager().getAllStartLivingTeams()) {
            int teamId = this.main.getDatabaseManager().createTeam(team.getName(), team.getColor().toString());
            Z++;
            X = Z * radius;
            for (PlayerTaupe pl: team.getMembers()) {
                Player player = pl.getPlayer();
                if (Objects.isNull(player))
                    continue;

                this.main.getDatabaseManager().createMole(player, teamId);
                Location spawnTeam = new Location(world,
                        (int) (rayon * Math.cos(X)),
                        world.getHighestBlockYAt(
                                (int) (rayon * Math.cos(X)),
                                (int) (rayon * Math.sin(X))) + 2,
                        (int) (rayon * Math.sin(X)));
                player.teleport(spawnTeam);
            }
        }
    }

    /**
     * Traite les joueurs non vivants
     */
    private void processNotAlivePlayers() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            PlayerTaupe pl = PlayerTaupe.getPlayerManager(player.getUniqueId());
            if (!pl.isAlive() && Objects.isNull(pl.getTeam())) {
                this.main.getTeamManager().getSpectatorTeam().ifPresent(spectatorTeam -> spectatorTeam.joinTeam(pl));
                player.setGameMode(GameMode.SPECTATOR);
            }
        });
    }

    /**
     * Téléporte tous les joueurs qui sont dans le nether dans le monde normal
     */
    private void teleportNetherPlayers() {
        World world = this.main.getWorldManager().getWorld();
        if (Objects.isNull(world)) {
            String worldNotExistMessage = "§e" + LanguageBuilder.getContent("GAME", "cannotTeleportWorldNotFound", true);
            Bukkit.broadcastMessage(worldNotExistMessage);
            return;
        }

        double wallSize = this.main.getScenariosManager().wallSizeAfter.getDiameter();
        long speed = (long) ((long) (this.main.getScenariosManager().wallSizeBefore.getRadius() - this.main.getScenariosManager().wallSizeAfter.getRadius() ) / this.main.getScenariosManager().wallSpeed.getValue());

        WorldBorder border = world.getWorldBorder();
        border.setCenter(0.0, 0.0);
        border.setSize(wallSize, speed);


        int nbTeam = this.main.getTeamManager().countTeams();
        double radius = this.main.getScenariosManager().wallSizeBefore.getRadius() - 200;
        int Z = 0;
        double X;
        for (TeamCustom team: this.main.getTeamManager().getAllTeams()) {
            if (team instanceof SpectatorTeam) {
                Location centerLocation = new Location(world, 0, 150, 0);
                team.getMembersInWorldEnvironment(World.Environment.NETHER).forEach(player -> player.teleport(centerLocation));
            } else if (team instanceof StartTeam) {
                X = Z * Math.PI * 2 / nbTeam;
                Location teleportPoint = new Location(world, (int) (radius * Math.cos(X)), 250, (int) (radius * Math.sin(X)));
                teleportPoint.setY(world.getHighestBlockYAt(teleportPoint) + 2);
                team.getMembersInWorldEnvironment(World.Environment.NETHER).forEach(player -> {
                    player.teleport(teleportPoint);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 100));
                });
            }
            Z++;
        }

        String wallShrinkingMessage = "§a[TaupeGun]§f "+LanguageBuilder.getContent("GAME", "wallShrinking",true);
        Bukkit.broadcastMessage(wallShrinkingMessage);
    }

    /**
     * Sends information about the role of mole : available commands, gameplay objectives.
     *
     * @param receiver
     * @param receiverTaupe
     */
    public void sendMoleInfoMessage(Player receiver, PlayerTaupe receiverTaupe) {
        String moleMessageInfo = "§6"+LanguageBuilder.getContent("CONTENT", "moleMessageInfo", true);
        String moleMessageT = "§6"+LanguageBuilder.getContent("CONTENT", "moleMessageT", true);
        String moleMessageReveal = "§6"+LanguageBuilder.getContent("CONTENT", "moleMessageReveal", true);

        Map<String, String> params = new HashMap<>();
        params.put("kitName", "§e§l" + receiverTaupe.getMoleKit().getName() + "§r§6");
        String moleMessageClaim = TextInterpreter.textInterpretation("§6"+LanguageBuilder.getContent("CONTENT", "moleMessageClaim", true), params);

        receiver.sendMessage(ChatColor.RED + "---------------");
        receiver.sendMessage(moleMessageInfo);
        receiver.sendMessage(moleMessageT);
        receiver.sendMessage(moleMessageReveal);
        receiver.sendMessage(moleMessageClaim);
        receiver.sendMessage(ChatColor.RED + "---------------");
    }

    /**
     * Sends information about the role of super mole : available commands, gameplay objectives.
     *
     * @param receiver
     */
    public void sendSuperMoleInfoMessage(Player receiver) {
        String superMoleMessageInfo = "§6" + LanguageBuilder.getContent("CONTENT", "superMoleMessageInfo", true);
        String superMoleMessageT = "§6" + LanguageBuilder.getContent("CONTENT", "superMoleMessageT", true);
        String superMoleMessageReveal = "§6" + LanguageBuilder.getContent("CONTENT", "superMoleMessageReveal", true);

        receiver.sendMessage(ChatColor.RED + "---------------");
        receiver.sendMessage(superMoleMessageInfo);
        receiver.sendMessage(superMoleMessageT);
        receiver.sendMessage(superMoleMessageReveal);
        receiver.sendMessage(ChatColor.RED + "---------------");
    }
}
