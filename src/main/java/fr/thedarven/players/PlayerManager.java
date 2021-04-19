package fr.thedarven.players;

import fr.thedarven.TaupeGun;
import fr.thedarven.models.Manager;
import fr.thedarven.models.PlayerTaupe;
import fr.thedarven.models.enums.EnumGameState;
import fr.thedarven.utils.api.Title;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;

import java.util.Objects;

public class PlayerManager extends Manager {

    public PlayerManager(TaupeGun main) {
        super(main);
    }

    /**
     * Permet d'envouer un playsound à tous les joueurs connectés
     *
     * @param sound Le son
     */
    public void sendPlaySound(Sound sound) {
        Bukkit.getOnlinePlayers().forEach(p -> p.playSound(p.getLocation(), sound, 1, 1));
    }

    /**
     * Permet d'envoyer un playsound et un title à tous les joueurs connectés
     *
     * @param sound Le son
     * @param title Le title
     */
    public void sendPlaySoundAndTitle(Sound sound, Title title) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getLocation(), sound, 1, 1);
            title.send(player);
        }
    }

    /**
     * Permet d'avoir la tête d'un Player
     *
     * @param pseudonym Le pseudonyme du Player
     * @param headName Le nom de l'item
     * @return La tête du Player
     */
    public ItemStack getHeadOfPlayer(String pseudonym, String headName) {
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
        SkullMeta headM = (SkullMeta) head.getItemMeta();
        headM.setOwner(pseudonym);
        headM.setDisplayName(headName);
        head.setItemMeta(headM);

        return head;
    }

    public void clearPlayer(Player p) {
        if (Objects.nonNull(p.getOpenInventory())) {
            p.getOpenInventory().setCursor(null);
        }
        p.getInventory().clear();
        p.getInventory().setArmorContents(null);
        p.closeInventory();
    }

    public void resetPlayerData(Player player) {
        this.main.getListenerManager().getPlayerJoinQuitListener().loginAction(player);

        for (PotionEffect potion : player.getActivePotionEffects()) {
            player.removePotionEffect(potion.getType());
        }

        player.setHealth(20);
        player.setMaxHealth(20.0);
        player.setFoodLevel(20);
        player.setExhaustion(5F);
        player.setExp(0L+0F);
        player.setLevel(0);
    }

    public void openConfigInventory(Player p) {
        if ((p.isOp() || p.hasPermission("taupegun.scenarios")) && EnumGameState.isCurrentState(EnumGameState.LOBBY))
            p.openInventory(TaupeGun.getInstance().getScenariosManager().menu.getInventory());
        else if(TaupeGun.getInstance().getScenariosManager().scenariosVisible.getValue())
            p.openInventory(TaupeGun.getInstance().getScenariosManager().configurationMenu.getInventory());
    }

    public void sendOrientationMessage(Player player, PlayerTaupe playerTaupe){
        StringBuilder message = new StringBuilder();

        if (Objects.nonNull(playerTaupe.getTeam())) {
            for (PlayerTaupe mate: playerTaupe.getTeam().getAlivesPlayers()) {
                if (!Objects.equals(mate, playerTaupe)) {
                    message.append(getOrientation(player, mate));
                }
            }
        }
        Title.sendActionBar(player, message.toString());
    }

    public static String getOrientation(Player p, PlayerTaupe matePl){
        StringBuilder orientation = new StringBuilder("§l");

        Player mate = matePl.getPlayer();
        if (Objects.isNull(mate))
            return "";

        Location location = p.getLocation();
        Location mateLocation = mate.getLocation();

            double oppose = mateLocation.getZ() - location.getZ();
            oppose = Math.sqrt(oppose * oppose);

            double adjacent = mateLocation.getX() - location.getX();
            adjacent = Math.sqrt(adjacent * adjacent);

            double angle = Math.atan(oppose / adjacent) * (180 / Math.PI);

            int playerOrientation = 0;
            int seeOrientation = 0;
            if (mateLocation.getX() >= location.getX()) {
                if (mateLocation.getZ() >= location.getZ()) {
                    if (angle <= 30.0) {
                        playerOrientation = 2;
                    } else if (angle <= 60.0){
                        playerOrientation = 3;
                    } else {
                        playerOrientation = 4;
                    }
                } else {
                    if (angle <= 30.0) {
                        playerOrientation = 2;

                    } else if (angle <= 60.0) {
                        playerOrientation = 1;
                    } else {
                        playerOrientation = 0;
                    }
                }
            } else if (mateLocation.getX() < location.getX()){
                if (mateLocation.getZ() >= location.getZ()){
                    if (angle <= 30.0) {
                        playerOrientation = 6;
                    } else if (angle <= 60.0) {
                        playerOrientation = 5;
                    } else {
                        playerOrientation = 4;
                    }
                } else {
                    if (angle <= 30.0) {
                        playerOrientation = 6;
                    } else if (angle <= 60.0) {
                        playerOrientation = 7;
                    } else {
                        playerOrientation = 0;
                    }
                }
            }

            // double ylaw = p.getEyeLocation().getYaw();
            double yaw = (location.getYaw() - 90) % 360;
            if (yaw < 0) {
                yaw += 360.0;
            }
            if ((337.5 <= yaw && yaw < 360.0) || (0 <= yaw && yaw <=  22.5)) {
                // OUEST
                seeOrientation = 6;
            } else if (22.5 <= yaw && yaw < 67.5) {
                // NORD OUEST
                seeOrientation = 7;
            } else if (67.5 <= yaw && yaw < 112.5) {
                // NORD
                seeOrientation = 0;
            } else if (112.5 <= yaw && yaw < 157.5) {
                // NORD EST
                seeOrientation = 1;
            } else if (157.5 <= yaw && yaw < 202.5) {
                // EST
                seeOrientation = 2;
            } else if (202.5 <= yaw && yaw < 247.5) {
                // SUD EST
                seeOrientation = 3;
            } else if (247.5 <= yaw && yaw < 292.5) {
                // SUD
                seeOrientation = 4;
            } else if (292.5 <= yaw && yaw < 337.5) {
                // SUD OUEST
                seeOrientation = 5;
            }

            if (Objects.equals(mate.getWorld(), p.getWorld())) {
                int pointOrientation = (playerOrientation - seeOrientation);

                double x = mateLocation.getX(), y = mateLocation.getY(), z = mateLocation.getZ(),
                        blockX = location.getBlockX(), blockY = location.getBlockY(), blockZ = location.getBlockZ();

                int distance = (int) Math.sqrt(Math.pow(x - blockX, 2) + Math.pow(z - blockZ, 2) + Math.pow(y - blockY, 2));
                if (distance <= 100.0) {
                    orientation.append(ChatColor.DARK_GREEN);
                } else if (distance <= 200.0){
                    orientation.append(ChatColor.YELLOW);
                } else if (distance <= 300.0){
                    orientation.append(ChatColor.GOLD);
                } else {
                    orientation.append(ChatColor.RED);
                }


                switch (pointOrientation){
                    case -7:
                    case 1:
                        orientation.append("⬈ ");
                        break;
                    case -6:
                    case 2:
                        orientation.append("➡ ");
                        break;
                    case -5:
                    case 3:
                        orientation.append("⬊ ");
                        break;
                    case -4:
                    case 4:
                        orientation.append("⬇ ");
                        break;
                    case -3:
                    case 5:
                        orientation.append("⬋ ");
                        break;
                    case -2:
                    case 6:
                        orientation.append("⬅ ");
                        break;
                    case -1:
                    case 7:
                        orientation.append("⬉ ");
                        break;
                    case 0:
                        orientation.append("⬆ ");
                        break;
                }

                orientation.append(mate.getName()).append(" §7(").append(distance).append("m)§r     ");
            } else {
                orientation.append("§c").append(mate.getName()).append(" §7(?m)§r     ");
            }
        return orientation.toString();
    }

}
