package fr.thedarven.events.runnable;

import fr.thedarven.TaupeGun;
import fr.thedarven.models.enums.EnumInventory;
import fr.thedarven.players.PlayerTaupe;
import fr.thedarven.players.runnable.PlayerInventoryRunnable;
import fr.thedarven.utils.api.Title;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.TextInterpreter;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.*;

public class InvSeeRunnable extends PlayerInventoryRunnable {

    private final PlayerTaupe viewedPl;

    public InvSeeRunnable(TaupeGun main, PlayerTaupe pl, PlayerTaupe viewedPl) {
        super(main, pl, EnumInventory.INVSEE);
        this.viewedPl = viewedPl;
    }

    @Override
    protected void operate() {
        Player playerWhoWatched = Bukkit.getPlayer(this.pl.getUuid());
        Player viewedPlayer = Bukkit.getPlayer(this.viewedPl.getUuid());

        if (Objects.nonNull(playerWhoWatched) && Objects.nonNull(viewedPlayer) && playerWhoWatched.getGameMode() == GameMode.SPECTATOR && checkOpenedInventory(playerWhoWatched)) {
            openInventory(playerWhoWatched);
        } else {
            this.stopRunnable();
        }
    }

    @Override
    public Inventory createInventory() {
        Player playerWhoWatched = Bukkit.getPlayer(this.pl.getUuid());
        Player viewedPlayer = Bukkit.getPlayer(this.viewedPl.getUuid());

        if (Objects.isNull(playerWhoWatched) || Objects.isNull(viewedPlayer) || playerWhoWatched.getGameMode() != GameMode.SPECTATOR)
            return null;

        Map<String, String> params = new HashMap<>();
        params.put("playerName", viewedPlayer.getName());
        String inventoryTitle = TextInterpreter.textInterpretation("§3" + LanguageBuilder.getContent("INVSEE", "inventoryTitle", true), params);

        Inventory inv = Bukkit.createInventory(null, 45, inventoryTitle);
        ItemStack item = viewedPlayer.getInventory().getHelmet();
        inv.setItem(1, item);
        item = viewedPlayer.getInventory().getChestplate();
        inv.setItem(2, item);
        item = viewedPlayer.getInventory().getLeggings();
        inv.setItem(3, item);
        item = viewedPlayer.getInventory().getBoots();
        inv.setItem(4, item);

        for (int i = 9; i < 36; i++){
            inv.setItem(i, viewedPlayer.getInventory().getItem(i));
        }
        for (int i = 0; i < 9; i++){
            inv.setItem(36 + i, viewedPlayer.getInventory().getItem(i));
        }

        // LEVEL
        params.clear();
        params.put("level", "§a" + viewedPlayer.getLevel() + "§6");
        String levelMessage = TextInterpreter.textInterpretation("§6" + LanguageBuilder.getContent("INVSEE", "level", true), params);

        ItemStack experienceItem = new ItemStack(Material.EXP_BOTTLE, viewedPlayer.getLevel());
        ItemMeta experienceItemM = experienceItem.getItemMeta();
        experienceItemM.setDisplayName(levelMessage);
        experienceItem.setItemMeta(experienceItemM);
        inv.setItem(5, experienceItem);


        // EFFECT
        String effectMessage = "§6" + LanguageBuilder.getContent("INVSEE", "level", true);

        List<String> lores = new ArrayList<>();
        viewedPlayer.getActivePotionEffects().forEach(effect ->
                lores.add("§b" + effect.getType().getName().charAt(0) + effect.getType().getName().toLowerCase().substring(1) + " "
                        + (effect.getAmplifier() + 1) + " : §r" + DurationFormatUtils.formatDuration(effect.getDuration() * 1000L / 20, "mm:ss"))
        );

        ItemStack potion = new ItemStack(Material.POTION, viewedPlayer.getActivePotionEffects().size());
        ItemMeta potionM = potion.getItemMeta();
        potionM.setDisplayName(effectMessage);
        potionM.setLore(lores);
        potion.setItemMeta(potionM);
        inv.setItem(6, potion);

        // HEAL
        DecimalFormat df = new DecimalFormat("0.00");

        params.clear();
        params.put("heart", df.format(viewedPlayer.getHealth()));
        params.put("heartMax", viewedPlayer.getMaxHealth() + "");
        params.put("valueColor", "§c");
        params.put("endValueColor", "§6");
        String heartMessage = TextInterpreter.textInterpretation("§6" + LanguageBuilder.getContent("INVSEE", "heart", true), params);

        ItemStack healthItem = new ItemStack(Material.GOLDEN_APPLE, (int)viewedPlayer.getHealth());
        ItemMeta healthItemM = healthItem.getItemMeta();
        healthItemM.setDisplayName(heartMessage);
        healthItem.setItemMeta(healthItemM);
        inv.setItem(7, healthItem);

        // MOLE
        lores.clear();
        if (!this.main.getGameManager().molesEnabled()) {
            params.clear();
            params.put("valueColor", "§r§k");
            params.put("endValueColor", "§r§e");
            String unknownMoleMessage = TextInterpreter.textInterpretation("§e" + LanguageBuilder.getContent("INVSEE", "unknownMole", true), params);
            lores.add(unknownMoleMessage);
        } else if (this.viewedPl.isTaupe()) {
            params.clear();
            params.put("teamName", "§r" + this.viewedPl.getTaupeTeam().getTeam().getName()+"§e");
            String moleMessage = TextInterpreter.textInterpretation("§e" + LanguageBuilder.getContent("INVSEE", "mole", true), params);
            lores.add(moleMessage);
        } else {
            params.clear();
            params.put("valueColor", "§r");
            params.put("endValueColor", "§e");
            String notMoleMessage = TextInterpreter.textInterpretation("§e" + LanguageBuilder.getContent("INVSEE", "notMole", true), params);
            lores.add(notMoleMessage);
        }

        // SUPERMOLE
        if (!this.main.getGameManager().molesEnabled()) {
            params.clear();
            params.put("valueColor", "§r§k");
            params.put("endValueColor", "§r§e");
            String unknownSuperMoleMessage = TextInterpreter.textInterpretation("§e" + LanguageBuilder.getContent("INVSEE", "unknownSuperMole", true), params);
            lores.add(unknownSuperMoleMessage);
        } else if (this.viewedPl.isSuperTaupe()) {
            params.clear();
            params.put("teamName", "§r" + this.viewedPl.getSuperTaupeTeam().getTeam().getName() + "§e");
            String superMoleMessage = TextInterpreter.textInterpretation("§e" + LanguageBuilder.getContent("INVSEE", "superMole", true), params);
            lores.add(superMoleMessage);
        } else {
            params.clear();
            params.put("valueColor", "§r");
            params.put("endValueColor", "§e");
            String notSuperMoleMessage = TextInterpreter.textInterpretation("§e" + LanguageBuilder.getContent("INVSEE", "notSuperMole", true), params);
            lores.add(notSuperMoleMessage);
        }

        // INFORMATION
        String informationMessage = "§6" + LanguageBuilder.getContent("INVSEE", "information", true);

        ItemStack paper = new ItemStack(Material.PAPER, 1);
        ItemMeta paperM = paper.getItemMeta();
        paperM.setDisplayName(informationMessage);
        paperM.setLore(lores);
        paper.setItemMeta(paperM);
        inv.setItem(8, paper);

        // KILL
        params.clear();
        params.put("kill", "§e" + this.viewedPl.getKill() + "§6");
        String killMessage = TextInterpreter.textInterpretation("§6" + LanguageBuilder.getContent("INVSEE", "kill", true), params);

        Title.sendActionBar(playerWhoWatched, killMessage);
        return inv;
    }
}
