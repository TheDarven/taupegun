package fr.thedarven.events.runnable;

import fr.thedarven.TaupeGun;
import fr.thedarven.models.enums.EnumGameState;
import fr.thedarven.models.enums.EnumInventory;
import fr.thedarven.players.PlayerTaupe;
import fr.thedarven.players.runnable.PlayerInventoryRunnable;
import fr.thedarven.teams.TeamCustom;
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

    public TeamSelectionRunnable(TaupeGun main, PlayerTaupe pl) {
        super(main, pl, EnumInventory.TEAM);
    }

    @Override
    protected void operate() {
        Player player = pl.getPlayer();

        if (Objects.nonNull(player) && player.isOnline() && checkOpenedInventory(player) && EnumGameState.isCurrentState(EnumGameState.LOBBY) && this.main.getScenariosManager().ownTeam.getValue()){
            openInventory(player);
        } else {
            this.stopRunnable();
        }
    }

    @Override
    protected Inventory createInventory() {
        Player player = pl.getPlayer();

        if (Objects.isNull(player) || !player.isOnline() || !EnumGameState.isCurrentState(EnumGameState.LOBBY) || !this.main.getScenariosManager().ownTeam.getValue())
            return null;

        String teamChoiceTitle = "§7" + LanguageBuilder.getContent("TEAM", "teamChoiceTitle", true);

        Inventory teamMenu = Bukkit.createInventory(null, 45, teamChoiceTitle);
        TeamCustom playerTeam = PlayerTaupe.getPlayerManager(player.getUniqueId()).getTeam();

        TeamCustom.getAllTeams().forEach(team -> teamMenu.addItem(getItemOfTeam(team, playerTeam)));

        if (Objects.nonNull(playerTeam)) {
            String emptyMessage = "§4" + LanguageBuilder.getContent("TEAM", "leave", true);

            ItemStack item = new ItemStack(Material.BARRIER, 1);
            ItemMeta itemM = item.getItemMeta();
            itemM.setDisplayName(emptyMessage);
            item.setItemMeta(itemM);
            teamMenu.setItem(44, item);
        }

        return teamMenu;
    }

    private ItemStack getItemOfTeam(TeamCustom team, TeamCustom playerTeam) {
        ItemStack item = new ItemStack(Material.BANNER, 1);
        BannerMeta itemM = (BannerMeta) item.getItemMeta();
        itemM.setBaseColor(team.getColorEnum().getDyeColor());
        itemM.setDisplayName(team.getTeam().getPrefix() + team.getName() + " [" + team.getSize() + "/" + TeamCustom.MAX_PLAYER_PER_TEAM + "]");

        if (Objects.nonNull(playerTeam) && playerTeam == team) {
            itemM.addEnchant(Enchantment.LURE, 1, false);
            itemM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        List<String> itemLore = new ArrayList<>();
        if (team.getSize() == 0) {
            String emptyMessage = "§e" + LanguageBuilder.getContent("TEAM", "empty", true);
            itemLore.add(emptyMessage);
        } else {
            team.getPlayers().forEach(ps -> itemLore.add(team.getTeam().getPrefix() + " " + ps.getName()));
        }

        itemM.setLore(itemLore);
        item.setItemMeta(itemM);
        return item;
    }
}
