package fr.thedarven.scenario.team.element.player;

import fr.thedarven.TaupeGun;
import fr.thedarven.player.model.PlayerTaupe;
import fr.thedarven.scenario.builder.page.PageInventory;
import fr.thedarven.team.model.TeamCustom;
import fr.thedarven.utils.GlobalVariable;
import fr.thedarven.utils.TextInterpreter;
import fr.thedarven.utils.api.titles.ActionBar;
import fr.thedarven.utils.helpers.ItemHelper;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TeamsPlayersSelectionPage extends PageInventory<PlayerTaupe, PageableTeamsPlayersSelection> {

    private static String TEAM_FULL_FORMAT = "L'équipe {teamName} est déjà complète.";

    public TeamsPlayersSelectionPage(TaupeGun main, int lines, PageableTeamsPlayersSelection pageableInventory, int page) {
        super(main, "Ajouter un joueur", null, "MENU_TEAM_ITEM_ADD_PLAYER", lines, Material.SKULL, 0, (byte) 0, pageableInventory, page);
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
    protected void refreshInventoryItems() {
        super.refreshInventoryItems();
        if (Objects.isNull(inventory)) {
            return;
        }

        List<PlayerTaupe> elements = getElements();
        for (int i = 0; i < elements.size(); i++) {
            ItemStack item = ItemHelper.addTagOnItemStack(ItemHelper.getPlayerHeadWithName(elements.get(i).getName(), elements.get(i).getName()));
            getInventory().setItem(i, item);
        }
        for (int i = elements.size(); i < getSize(); i++) {
            getInventory().setItem(i, null);
        }
    }

    @Override
    protected void onClickInElementSlot(InventoryClickEvent event, Player player, PlayerTaupe playerTaupe) {
        List<PlayerTaupe> elementsOfPage = getElements();
        if (event.getSlot() >= elementsOfPage.size()) {
            return;
        }

        PlayerTaupe clickedPlayer = elementsOfPage.get(event.getSlot());
        TeamCustom teamJoin = getPageableInventory().getTeam();
        if (teamJoin == null || clickedPlayer == null) {
            return;
        }

        if (teamJoin.isFull()) {
            Map<String, String> params = new HashMap<>();
            params.put("teamName", String.format("§e§l%s§r§c", teamJoin.getName()));
            new ActionBar(String.format("§c%s", TextInterpreter.textInterpretation(TEAM_FULL_FORMAT, params))).sendActionBar(player);
            return;
        }

        teamJoin.joinTeam(clickedPlayer);
        Map<String, String> params = new HashMap<>();
        params.put("playerName", String.format("§e§l%s§a§r", clickedPlayer.getName()));
        String isAddingMessage = String.format("§a%s", TextInterpreter.textInterpretation(LanguageBuilder.getContent("TEAM", "isAdding", true), params));
        new ActionBar(isAddingMessage).sendActionBar(player);
    }

    @Override
    protected void addElement(PlayerTaupe element) {
        refreshInventoryItems();
    }

    @Override
    protected void removeElement(PlayerTaupe element) {
        refreshInventoryItems();
    }
}
