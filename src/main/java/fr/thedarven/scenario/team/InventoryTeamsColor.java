package fr.thedarven.scenario.team;

import fr.thedarven.TaupeGun;
import fr.thedarven.model.enums.ColorEnum;
import fr.thedarven.player.model.PlayerTaupe;
import fr.thedarven.scenario.builder.ConfigurationInventory;
import fr.thedarven.scenario.utils.AdminConfiguration;
import fr.thedarven.team.model.TeamCustom;
import fr.thedarven.utils.GlobalVariable;
import fr.thedarven.utils.TextInterpreter;
import fr.thedarven.utils.api.titles.ActionBar;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InventoryTeamsColor extends ConfigurationInventory implements AdminConfiguration {

    private static final ColorEnum[] BANNERS_COLOR = {
            ColorEnum.BLACK, ColorEnum.RED, ColorEnum.DARK_GREEN, ColorEnum.BLUE, ColorEnum.DARK_PURPLE,
            ColorEnum.DARK_AQUA, ColorEnum.GRAY, ColorEnum.DARK_GRAY, ColorEnum.GREEN, ColorEnum.YELLOW,
            ColorEnum.AQUA, ColorEnum.LIGHT_PURPLE, ColorEnum.ORANGE, ColorEnum.WHITE};
    private static String SUCCESS_TEAM_CREATE_FORMAT = "L'équipe {teamName} a été créée avec succès.";

    public InventoryTeamsColor(TaupeGun main) {
        super(main, "Choix de la couleur", null, "MENU_TEAM_ADD_COLOR", 2, Material.ACACIA_DOOR, null, 22);
    }

    @Override
    public void loadLanguage(String language) {
        SUCCESS_TEAM_CREATE_FORMAT = LanguageBuilder.getContent("TEAM", "create", language, true);
        super.loadLanguage(language);
    }

    @Override
    protected LanguageBuilder initDefaultTranslation() {
        LanguageBuilder languageElement = super.initDefaultTranslation();
        LanguageBuilder languageTeam = LanguageBuilder.getLanguageBuilder("TEAM");
        languageTeam.addTranslation(GlobalVariable.DEFAULT_LANGUAGE, "create", SUCCESS_TEAM_CREATE_FORMAT);
        return languageElement;
    }

    @Override
    protected Inventory buildAndFillInventory() {
        Inventory inventory = super.buildAndFillInventory();

        for (int color = 0; color < 14; color++) {
            ItemStack banner = new ItemStack(Material.BANNER, 1);
            BannerMeta bannerM = (BannerMeta) banner.getItemMeta();
            bannerM.setBaseColor(BANNERS_COLOR[color].getDyeColor());
            banner.setItemMeta(bannerM);

            int colorRank;
            if (color < 7) {
                colorRank = color + 1;
            } else {
                colorRank = color + 3;
            }
            inventory.setItem(colorRank, banner);
        }
        return inventory;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent e, Player player, PlayerTaupe pl) {
        if (e.getCurrentItem().getType() == Material.BANNER) {
            choiceTeamColor(player, pl, e.getCurrentItem());
        }
    }

    /**
     * Permet à un joueur de choisir la couleur lors de la création d'une équipe
     *
     * @param player    Le Player qui choisit la couleur
     * @param pl        Le PlayerTaupe du Player
     * @param itemStack L'item selectionné par le joueur
     */
    private void choiceTeamColor(Player player, PlayerTaupe pl, ItemStack itemStack) {
        TeamCustom teamCustom = TeamCustom.getTeamCustomByName(pl.getCreateTeamName());
        if (Objects.nonNull(teamCustom)) {
            player.closeInventory();
            this.main.getMessageManager().sendTeamNameAlreadyUsedMessage(player);
            pl.setCreateTeamName(null);
            return;
        }


        DyeColor dyeColor = ((BannerMeta) itemStack.getItemMeta()).getBaseColor();

        new TeamCustom(this.main, pl.getCreateTeamName(), ColorEnum.getByDyeColor(dyeColor), 0, 0, false, true);

        Map<String, String> params = new HashMap<>();
        params.put("teamName", "§e§l" + pl.getCreateTeamName() + "§r§a");
        String successTeamCreateMessage = TextInterpreter.textInterpretation("§a" + SUCCESS_TEAM_CREATE_FORMAT, params);

        new ActionBar(successTeamCreateMessage).sendActionBar(player);

        pl.setCreateTeamName(null);
        player.openInventory(this.main.getScenariosManager().teamsMenu.getLastChild().getInventory());
    }

}
