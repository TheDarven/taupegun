package fr.thedarven.scenarios.builders;

import fr.thedarven.TaupeGun;
import fr.thedarven.players.PlayerTaupe;
import fr.thedarven.scenarios.kits.InventoryKitsElement;
import fr.thedarven.utils.languages.LanguageBuilder;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class InventoryDelete extends InventoryGUI {

	private static String CONFIRM_ACTION = "✔ Confirmer";
	private static String CANCEL_ACTION = "✘ Annuler";
	
	public InventoryDelete(TaupeGun main, InventoryGUI parent, String pName, String pTranslationName, int pPosition) {
		super(main, pName, "", pTranslationName, 1, Material.STAINED_CLAY, parent, pPosition, (byte) 14);
		
		initItem();
		updateLanguage(getLanguage());
		reloadItem();
	}

	@Override
	public void updateLanguage(String language) {
		CONFIRM_ACTION = LanguageBuilder.getContent("CONTENT", "confirm", language, true);
		CANCEL_ACTION = LanguageBuilder.getContent("CONTENT", "cancel", language, true);
		
		super.updateLanguage(language);
	}

	@Override
	protected LanguageBuilder initDefaultTranslation() {
		LanguageBuilder languageElement = super.initDefaultTranslation();
		
		LanguageBuilder languageContent = LanguageBuilder.getLanguageBuilder("CONTENT");
		languageContent.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "confirm", CONFIRM_ACTION);
		languageContent.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "cancel", CANCEL_ACTION);
		
		return languageElement;
	}




	@Override
	public void reloadInventory() {
		clearChildrenItems();

		AtomicInteger counter = new AtomicInteger(0);
		final int childrenSize = getChildren().size();

		getChildrenValue().forEach(child -> {
			if (child instanceof InventoryKitsElement) {
				modifiyPosition(child, counter.getAndIncrement());
			} else if (childrenSize < 10){
				modifiyPosition(child,childrenSize - 1);
			}
		});
	}
	
	/**
	 * Pour initier les items
	 */
	private void initItem() {
		getInventory().clear();
		ItemStack confirm = new ItemStack(Material.STAINED_CLAY, 1, (byte) 13);
		ItemMeta confirmM = confirm.getItemMeta();
		confirmM.setDisplayName(ChatColor.GREEN + CONFIRM_ACTION);
		confirm.setItemMeta(confirmM);
		getInventory().setItem(2, confirm);
		
		ItemStack cancel = new ItemStack(Material.STAINED_CLAY, 1, (byte) 14);
		ItemMeta cancelM = cancel.getItemMeta();
		cancelM.setDisplayName(ChatColor.RED + CANCEL_ACTION);
		cancel.setItemMeta(cancelM);
		getInventory().setItem(6, cancel);
	}

	@Override
	protected void reloadItems() {
		super.reloadItems();
		reloadItem();
	}
	
	/**
	 * Pour recharger les items dans l'inventaire
	 */
	private void reloadItem() {
		if (Objects.isNull(inventory))
			return;

		ItemStack confirm = this.getInventory().getItem(2);
		if (Objects.nonNull(confirm)) {
			ItemMeta confirmM = confirm.getItemMeta();
			confirmM.setDisplayName(ChatColor.GREEN + CONFIRM_ACTION);
			confirm.setItemMeta(confirmM);
		}


		ItemStack cancel = this.getInventory().getItem(6);
		if (Objects.nonNull(cancel)) {
			ItemMeta cancelM = cancel.getItemMeta();
			cancelM.setDisplayName(ChatColor.RED + CANCEL_ACTION);
			cancel.setItemMeta(cancelM);
		}
	}
	
	/**
	 * Pour supprimer un élément (càd un enfant)
	 * 
	 * @param player Le joueur qui supprime l'élément
	 */
	protected abstract void deleteElement(Player player);

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player player, PlayerTaupe pl) {
		if (e.getCurrentItem().getType() == Material.STAINED_CLAY) {
			if (e.getCurrentItem().getDurability() == 13) {
				deleteElement(player);
			} else if (e.getCurrentItem().getDurability() == 14) {
				player.openInventory(getParent().getInventory());
			}
		}
		delayClick(pl);
	}
	
}
