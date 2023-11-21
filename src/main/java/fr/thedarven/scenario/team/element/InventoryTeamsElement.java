package fr.thedarven.scenario.team.element;

import fr.thedarven.TaupeGun;
import fr.thedarven.events.event.team.PlayerJoinTeamEvent;
import fr.thedarven.events.event.team.PlayerLeaveTeamEvent;
import fr.thedarven.events.event.team.TeamDeleteEvent;
import fr.thedarven.game.model.enums.EnumGameState;
import fr.thedarven.model.enums.ColorEnum;
import fr.thedarven.player.model.PlayerTaupe;
import fr.thedarven.scenario.builder.ConfigurationInventory;
import fr.thedarven.scenario.builder.TreeInventory;
import fr.thedarven.scenario.team.element.player.PageableTeamsPlayersSelection;
import fr.thedarven.scenario.utils.AdminConfiguration;
import fr.thedarven.team.model.TeamCustom;
import fr.thedarven.utils.TextInterpreter;
import fr.thedarven.utils.api.titles.ActionBar;
import fr.thedarven.utils.helpers.ItemHelper;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import java.util.*;

public class InventoryTeamsElement extends ConfigurationInventory implements AdminConfiguration {

    private final TeamCustom team;

    public InventoryTeamsElement(TaupeGun main, ConfigurationInventory parent, TeamCustom team) {
        super(main, team.getName(), null, "MENU_TEAM_ITEM", 3, Material.BANNER, parent, 0);
        this.team = team;
    }

    /**
     * Pour changer la couleur de l'équipe
     *
     * @param colorEnum La nouvelle couleur
     */
    public void setColor(ColorEnum colorEnum) {
        // TODO Changer la couleur
        this.team.setColor(colorEnum);
        updateItemColor();
    }

    /**
     * Pour mettre à jour la description
     */
    final protected void updateItemColor() {
        if (Objects.isNull(this.getItem())) {
            return;
        }

        int hashCode = getItem().hashCode();

        BannerMeta itemM = (BannerMeta) getItem().getItemMeta();
        itemM.setBaseColor(this.team.getColor().getDyeColor());

        getItem().setItemMeta(itemM);
        if (Objects.nonNull(this.getParent())) {
            this.getParent().updateChildItem(hashCode, getItem(), this);
        }
    }


    @Override
    protected String getNameOfLanguage(String language) {
        return getName();
    }

    @Override
    protected String getDescriptionOfLanguage(String language) {
        return getDescription();
    }

    @Override
    protected String getInventoryName() {
        return this.getName();
    }

    @Override
    protected String getItemName() {
        return String.format("%s%s", ChatColor.LIGHT_PURPLE, this.getName());
    }

    @Override
    protected List<String> getItemDescription() {
        List<String> itemDescription = new ArrayList<>();
        if (this.team.countMembers() > 0) {
            itemDescription.add("");
            this.team.getMembers().forEach(member -> itemDescription.add(String.format("§a• %s", member.getName())));
        }
        return itemDescription;
    }

    @Override
    protected void refreshInventoryItems() {
        super.refreshInventoryItems();

        int position = 0;
        for (PlayerTaupe member: this.team.getMembers()) {
            getInventory().setItem(position++, ItemHelper.getPlayerHeadWithName(member.getName(), member.getName()));
        }

        for (TreeInventory child: getChildren()) {
            if (child instanceof PageableTeamsPlayersSelection && !team.isFull()) {
                getInventory().setItem(position++, child.getItem());
                getInventory().setItem(position++, new ItemStack(Material.AIR, 1));
            }
        }

        updateItemDescription();
    }

    @EventHandler
    public void onPlayerJoinTeam(PlayerJoinTeamEvent event) {
        if (EnumGameState.isCurrentState(EnumGameState.LOBBY) && event.getTeam() == this.team) {
            this.refreshInventoryItems();
        }
    }

    @EventHandler
    public void onPlayerLeaveTeam(PlayerLeaveTeamEvent event) {
        if (EnumGameState.isCurrentState(EnumGameState.LOBBY) && event.getTeam() == this.team) {
            this.refreshInventoryItems();
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onTeamDelete(TeamDeleteEvent event) {
        if (EnumGameState.isCurrentState(EnumGameState.LOBBY) && event.getTeam() == this.team) {
            deleteInventory(true);
        }
    }

    /**
     * Pour recharger les items dans l'inventaire
     */
    @Override
    protected ItemStack buildItem(Material material, byte itemData) {
        ItemStack itemStack = super.buildItem(material, itemData);

        BannerMeta itemM = (BannerMeta) itemStack.getItemMeta();
        itemM.setBaseColor(this.team.getColor().getDyeColor());

        itemStack.setItemMeta(itemM);
        return itemStack;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent e, Player player, PlayerTaupe pl) {
        ItemStack item = e.getCurrentItem();
        if (item.getType() == Material.SKULL_ITEM) {
            PlayerTaupe playerTaupe = PlayerTaupe.getPlayerTaupeByName(item.getItemMeta().getDisplayName());
            if (Objects.nonNull(playerTaupe) && playerTaupe.getTeam() == this.team) {
                this.team.leaveTeam(playerTaupe.getUuid());
                sendRemovePlayerFromTeamMessage(player, e.getCurrentItem().getItemMeta().getDisplayName());
                return;
            }
        }

        onChildClick(item, player, pl);
    }

    private void sendRemovePlayerFromTeamMessage(Player receiver, String target) {
        Map<String, String> params = new HashMap<>();
        params.put("playerName", String.format("§e§l%s§f§r", target));
        String isRemovingMessage = TextInterpreter.textInterpretation("§f" + LanguageBuilder.getContent("TEAM", "isDeleting", true), params);
        new ActionBar(isRemovingMessage).sendActionBar(receiver);
    }
}

