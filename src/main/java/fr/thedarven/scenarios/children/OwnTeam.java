package fr.thedarven.scenarios.children;

import fr.thedarven.TaupeGun;
import fr.thedarven.events.runnable.TeamSelectionRunnable;
import fr.thedarven.models.enums.EnumGameState;
import fr.thedarven.players.PlayerTaupe;
import fr.thedarven.scenarios.builders.InventoryGUI;
import fr.thedarven.scenarios.builders.OptionBoolean;
import fr.thedarven.scenarios.helper.ConfigurationPlayerItem;
import fr.thedarven.scenarios.helper.ConfigurationPlayerItemConditional;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class OwnTeam extends OptionBoolean implements ConfigurationPlayerItemConditional {

    private static String TEAM_CHOICE = "Choix de l'équipe";

    public OwnTeam(TaupeGun main, InventoryGUI parent) {
        super(main, "Choisir son équipe", "Donne la possibilité aux joueurs de rejoindre eux-mêmes les équipes.", "MENU_CONFIGURATION_OTHER_TEAM",
                Material.BANNER, parent, 7, true, (byte) 10);
        this.configurationPlayerItem = new ConfigurationPlayerItem(this, 8, this.getPlayerItemItem());
    }


    @Override
    protected void setValue(boolean value) {
        super.setValue(value);
        this.reloadPlayersItem();
    }


    @Override
    public void updateLanguage(String language) {
        TEAM_CHOICE = LanguageBuilder.getContent(getTranslationName(), "teamChoice", language, true);
        super.updateLanguage(language);
    }

    @Override
    protected LanguageBuilder initDefaultTranslation() {
        LanguageBuilder languageElement = super.initDefaultTranslation();
        languageElement.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "teamChoice", TEAM_CHOICE);

        return languageElement;
    }

    @Override
    public final ItemStack getPlayerItemItem() {
        ItemStack banner = new ItemStack(Material.BANNER, 1, (byte) 15);
        ItemMeta bannerM = banner.getItemMeta();
        bannerM.setDisplayName("§e" + TEAM_CHOICE);
        banner.setItemMeta(bannerM);
        return banner;
    }

    @Override
    public final void onPlayerItemClick(PlayerTaupe pl) {
        if (EnumGameState.isCurrentState(EnumGameState.LOBBY)) {
            createAndOpenTeamsInventory(pl);
        }
    }

    private void createAndOpenTeamsInventory(PlayerTaupe pl) {
        TeamSelectionRunnable teamSelectionRunnable = (TeamSelectionRunnable) pl.getRunnable(TeamSelectionRunnable.class);
        if (Objects.isNull(teamSelectionRunnable)) {
            teamSelectionRunnable = new TeamSelectionRunnable(this.main, pl);
            teamSelectionRunnable.runTaskTimer(this.main,1,10);
        }
        teamSelectionRunnable.openInventory();
    }

    @Override
    public boolean isPlayerItemEnable(Player player) {
        return EnumGameState.isCurrentState(EnumGameState.LOBBY) && this.value;
    }
}
