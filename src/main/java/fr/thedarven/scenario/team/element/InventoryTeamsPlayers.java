package fr.thedarven.scenario.team.element;

import fr.thedarven.TaupeGun;
import fr.thedarven.player.model.PlayerTaupe;
import fr.thedarven.scenario.builder.ConfigurationInventory;
import fr.thedarven.scenario.builder.TreeInventory;
import fr.thedarven.scenario.utils.AdminConfiguration;
import fr.thedarven.team.model.TeamCustom;
import fr.thedarven.utils.GlobalVariable;
import fr.thedarven.utils.TextInterpreter;
import fr.thedarven.utils.api.titles.ActionBar;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class InventoryTeamsPlayers extends ConfigurationInventory implements AdminConfiguration {

    private static String TEAM_FULL_FORMAT = "L'équipe {teamName} est déjà complète.";
    protected static List<InventoryTeamsPlayers> inventories = new ArrayList<>();

    public InventoryTeamsPlayers(TaupeGun main, ConfigurationInventory parent) {
        super(main, "Ajouter un joueur", null, "MENU_TEAM_ITEM_ADD_PLAYER", 6, Material.ARMOR_STAND, parent, 0);
        inventories.add(this);
    }

    @Override
    public TreeInventory build() {
        super.build();
        reloadInventory();
        return this;
    }

    @Override
    public void loadLanguage(String language) {
        TEAM_FULL_FORMAT = LanguageBuilder.getContent("TEAM", "full", language, true);
        super.loadLanguage(language);
    }

    @Override
    protected LanguageBuilder initDefaultTranslation() {
        LanguageBuilder languageElement = super.initDefaultTranslation();
        LanguageBuilder languageTeam = LanguageBuilder.getLanguageBuilder("TEAM");
        languageTeam.addTranslation(GlobalVariable.DEFAULT_LANGUAGE, "full", TEAM_FULL_FORMAT);
        return languageElement;
    }

    @Override
    public void reloadInventory() {
        int i = 0;
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerTaupe pl = PlayerTaupe.getPlayerManager(player.getUniqueId());
            if (Objects.isNull(pl.getTeam())) {
                getInventory().setItem(i, this.main.getPlayerManager().getHeadOfPlayer(player.getName(), player.getName()));
                i++;
            }
        }
        getInventory().setItem(i, new ItemStack(Material.AIR, 1));
    }


    /**
     * Recharge les objets des inventaires
     */
    public static void reloadInventories() {
        inventories.forEach(InventoryTeamsPlayers::reloadInventory);
    }

    @Override
    public void onInventoryClick(InventoryClickEvent e, Player player, PlayerTaupe pl) {
        ItemStack item = e.getCurrentItem();
        if (item.getType() != Material.SKULL_ITEM) {
            return;
        }

        TeamCustom teamJoin = TeamCustom.getTeamCustomByName(this.getParent().getInventory().getName());
        if (Objects.isNull(teamJoin)) {
            return;
        }

        if (teamJoin.getSize() < 9) {
            PlayerTaupe playerTaupe = PlayerTaupe.getPlayerTaupeByName(item.getItemMeta().getDisplayName());
            if (Objects.nonNull(playerTaupe)) {
                teamJoin.joinTeam(playerTaupe);
                sendAddPlayerTeamMessage(player, playerTaupe);
                this.getParent().openInventory(player);
            }
        } else {
            Map<String, String> params = new HashMap<>();
            params.put("teamName", "§e§l" + teamJoin.getName() + "§r§c");
            new ActionBar(TextInterpreter.textInterpretation("§c" + TEAM_FULL_FORMAT, params)).sendActionBar(player);
        }
    }

    public void sendAddPlayerTeamMessage(Player receiver, PlayerTaupe addedPlayer) {
        Map<String, String> params = new HashMap<>();
        params.put("playerName", "§e§l" + addedPlayer.getName() + "§a§r");
        String isAddingMessage = TextInterpreter.textInterpretation("§a" + LanguageBuilder.getContent("TEAM", "isAdding", true), params);

        new ActionBar(isAddingMessage).sendActionBar(receiver);
    }

}
