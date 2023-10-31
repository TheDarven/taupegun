package fr.thedarven.scenario.team.element;

import fr.thedarven.TaupeGun;
import fr.thedarven.model.enums.ColorEnum;
import fr.thedarven.player.model.StatsPlayerTaupe;
import fr.thedarven.scenario.builder.ConfigurationInventory;
import fr.thedarven.scenario.builder.TreeInventory;
import fr.thedarven.scenario.utils.AdminConfiguration;
import fr.thedarven.team.model.TeamCustom;
import fr.thedarven.utils.TextInterpreter;
import fr.thedarven.utils.api.titles.ActionBar;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.scoreboard.Team;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class InventoryTeamsElement extends ConfigurationInventory implements AdminConfiguration {

    public static Map<String, InventoryTeamsElement> teams = new LinkedHashMap<>();
    private ColorEnum colorEnum;

    public InventoryTeamsElement(TaupeGun main, String name, ColorEnum colorEnum) {
        super(main, name, null, "MENU_TEAM_ITEM", 3, Material.BANNER, main.getScenariosManager().teamsMenu, 0);
        this.colorEnum = colorEnum;
        teams.put(name, this);
    }

    @Override
    public TreeInventory build() {
        super.build();
        this.main.getScenariosManager().teamsMenu.reloadInventory();
        return this;
    }

    /**
     * Pour récupérer la couleur de l'équipe
     *
     * @return La couleur
     */
    public ColorEnum getColor() {
        return this.colorEnum;
    }

    /**
     * Pour changer la couleur de l'équipe
     *
     * @param colorEnum La nouvelle couleur
     */
    public void setColor(ColorEnum colorEnum) {
        this.colorEnum = colorEnum;
        updateItemColor();
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
        return this.getName();
    }

    @Override
    protected List<String> getItemDescription() {
        List<String> lores = new ArrayList<>();

        TeamCustom teamCustom = TeamCustom.getTeamCustomByName(getName());
        if (Objects.nonNull(teamCustom)) {
            Team team = teamCustom.getTeam();
            if (!team.getEntries().isEmpty()) {
                lores.add("");
            }
            team.getEntries().forEach(entry -> lores.add("§a• " + entry));
        }
        return lores;
    }

    @Override
    public void reloadInventory() {
        TeamCustom teamCustom = TeamCustom.getTeamCustomByName(getName());
        if (Objects.isNull(teamCustom)) {
            return;
        }

        Team team = teamCustom.getTeam();

        AtomicInteger pos = new AtomicInteger(0);
        team.getEntries().forEach(entry -> {
            getInventory().setItem(pos.getAndIncrement(), this.main.getPlayerManager().getHeadOfPlayer(entry, entry));
        });

        getChildren().forEach(inv -> {
            if (inv instanceof InventoryTeamsPlayers) {
                getInventory().setItem(pos.get(), inv.getItem());
                getInventory().setItem(pos.incrementAndGet(), new ItemStack(Material.AIR, 1));
            }
        });

        updateItemDescription();
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
        itemM.setBaseColor(this.colorEnum.getDyeColor());

        getItem().setItemMeta(itemM);
        if (Objects.nonNull(this.getParent())) {
            this.getParent().updateChildItem(hashCode, getItem(), this);
        }
    }

    /**
     * Pour recharger les items dans l'inventaire
     */
    @Override
    protected ItemStack buildItem(Material material, byte data) {
        ItemStack itemStack = super.buildItem(material, data);

        BannerMeta itemM = (BannerMeta) itemStack.getItemMeta();
        itemM.setBaseColor(colorEnum.getDyeColor());

        itemStack.setItemMeta(itemM);
        return itemStack;
    }

    /**
     * Pour supprimer une équipe
     *
     * @param name Le nom de l'équipe à supprimer
     */
    static public void removeTeam(String name) {
        ConfigurationInventory inv = teams.get(name);
        if (Objects.isNull(inv)) {
            return;
        }

        inv.getChildren()
                .forEach(child -> {
                    if (child instanceof InventoryTeamsPlayers) {
                        InventoryTeamsPlayers.inventories.remove(child);
                    }
                });
        InventoryTeamsPlayers.reloadInventories();

        if (Objects.nonNull(inv.getParent())) {
            inv.getParent().removeChild(inv, true);
        }
        teams.remove(name);
    }

    @Override
    public void onInventoryClick(InventoryClickEvent e, Player player, StatsPlayerTaupe pl) {
        ItemStack item = e.getCurrentItem();
        if (item.getType() == Material.SKULL_ITEM) {
            TeamCustom teamLeave = TeamCustom.getTeamCustomByName(getName());
            if (Objects.isNull(teamLeave))
                return;

            StatsPlayerTaupe playerTaupe = StatsPlayerTaupe.getPlayerTaupeByName(item.getItemMeta().getDisplayName());
            if (Objects.nonNull(playerTaupe) && playerTaupe.getTeam() == teamLeave) {
                teamLeave.leaveTeam(playerTaupe.getUuid());
                sendRemovePlayerTeamMessage(player, e.getCurrentItem().getItemMeta().getDisplayName());
                reloadInventory();
                InventoryTeamsPlayers.reloadInventories();
                return;
            }
        }

        openChildInventory(item, player, pl);
    }

    public void sendRemovePlayerTeamMessage(Player receiver, String target) {
        Map<String, String> params = new HashMap<>();
        params.put("playerName", "§e§l" + target + "§f§r");
        String isRemovingMessage = TextInterpreter.textInterpretation("§f" + LanguageBuilder.getContent("TEAM", "isDeleting", true), params);

        new ActionBar(isRemovingMessage).sendActionBar(receiver);
    }

    /**
     * Permet d'avoir la liste des InventoryTeamsElement
     *
     * @return La liste des InventoryTeamsElement
     */
    public static List<InventoryTeamsElement> getInventoryTeamsElement() {
        return new ArrayList<>(teams.values());
    }

    /**
     * Permet d'avoir l'InventoryTeamsElement d'une TeamCustom
     *
     * @param team La TeamCustom
     * @return <b>L'InventoryTeamsElement</b> de la TeamCustom
     */
    public static InventoryTeamsElement getInventoryTeamsElementOfTeam(TeamCustom team) {
        return teams.get(team.getName());
    }
}

