package fr.thedarven.utils.manager;

import fr.thedarven.TaupeGun;
import fr.thedarven.model.Manager;
import fr.thedarven.scenario.builder.ConfigurationInventory;
import fr.thedarven.utils.GlobalVariable;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LanguageManager extends Manager {

    private String language = GlobalVariable.DEFAULT_LANGUAGE;

    public LanguageManager(TaupeGun main) {
        super(main);
    }

    /**
     * Pour obtenir la langue actuellement selectionnées
     */
    public final String getLanguage() {
        if (Objects.nonNull(this.language)) {
            return this.language;
        }
        return GlobalVariable.DEFAULT_LANGUAGE;
    }

    public final void setLanguage(String language) {
        this.language = language;

        List<ConfigurationInventory> inventories = new ArrayList<>(ConfigurationInventory.getAll());
        inventories.forEach(configurationInventory -> configurationInventory.loadLanguage(this.language));

        Bukkit.getOnlinePlayers().forEach(receiver -> this.main.getMessageManager().updateTabContent(receiver));
    }

}
