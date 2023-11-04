package fr.thedarven.events.runnable;

import fr.thedarven.TaupeGun;
import fr.thedarven.game.model.enums.EnumGameState;
import fr.thedarven.model.enums.EnumPlayerInventoryType;
import fr.thedarven.player.model.PlayerTaupe;
import fr.thedarven.player.runnable.PlayerInventoryRunnable;
import fr.thedarven.team.model.TeamCustom;
import fr.thedarven.utils.helpers.ItemHelper;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TeamSelectionRunnable extends PlayerInventoryRunnable {

    private static final int LEAVE_POSITION = 44;

    public TeamSelectionRunnable(TaupeGun main, PlayerTaupe pl) {
        super(main, pl, EnumPlayerInventoryType.TEAM);
    }

    @Override
    protected void operate() {
        Player player = pl.getPlayer();

        if (Objects.nonNull(player) && player.isOnline() && checkOpenedInventory(player)
                && EnumGameState.isCurrentState(EnumGameState.LOBBY)
                && this.main.getScenariosManager().ownTeam.getValue()){
            openInventory(player);
        } else {
            this.stopRunnable();
        }
    }

    @Override
    protected Inventory createInventory() {
        Player player = pl.getPlayer();
        if (Objects.isNull(player) || !player.isOnline() || !EnumGameState.isCurrentState(EnumGameState.LOBBY) || !this.main.getScenariosManager().ownTeam.getValue()) {
            return null;
        }

        String teamChoiceTitle = "§7" + LanguageBuilder.getContent("TEAM", "teamChoiceTitle", true);
        Inventory teamMenu = Bukkit.createInventory(null, 45, teamChoiceTitle);
        TeamCustom playerTeam = PlayerTaupe.getPlayerManager(player.getUniqueId()).getTeam();

        this.main.getTeamManager().getAllTeams().forEach(team -> teamMenu.addItem(getItemOfTeam(team, playerTeam)));

        if (Objects.nonNull(playerTeam)) {
            String emptyMessage = "§4" + LanguageBuilder.getContent("TEAM", "leave", true);

            ItemStack leaveTeam = ItemHelper.addTagOnItemStack(new ItemStack(Material.BARRIER, 1));
            ItemMeta leaveTeamM = leaveTeam.getItemMeta();
            leaveTeamM.setDisplayName(emptyMessage);
            leaveTeam.setItemMeta(leaveTeamM);
            teamMenu.setItem(LEAVE_POSITION, leaveTeam);
        }

        return teamMenu;
    }

    private ItemStack getItemOfTeam(TeamCustom team, TeamCustom playerTeam) {
        ItemStack teamBanner = ItemHelper.addTagOnItemStack(new ItemStack(Material.BANNER, 1));
        BannerMeta teamBannerM = (BannerMeta) teamBanner.getItemMeta();
        teamBannerM.setBaseColor(team.getColor().getDyeColor());
        teamBannerM.setDisplayName(team.getColor().getColor() + team.getName() + " [" + team.getSize() + "/" + TeamCustom.MAX_PLAYER_PER_TEAM + "]");

        if (Objects.nonNull(playerTeam) && playerTeam == team) {
            teamBannerM.addEnchant(Enchantment.LURE, 1, false);
            teamBannerM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        List<String> itemLore = new ArrayList<>();
        if (team.getSize() == 0) {
            String emptyMessage = "§e" + LanguageBuilder.getContent("TEAM", "empty", true);
            itemLore.add(emptyMessage);
        } else {
            team.getMembers().forEach(ps -> itemLore.add(String.format("%s %s", team.getColor(), ps.getName())));
        }

        teamBannerM.setLore(itemLore);
        teamBanner.setItemMeta(teamBannerM);
        return teamBanner;
    }
}
