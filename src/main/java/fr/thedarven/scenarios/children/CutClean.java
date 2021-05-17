package fr.thedarven.scenarios.children;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenarios.builders.InventoryGUI;
import fr.thedarven.scenarios.builders.OptionBoolean;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;

public class CutClean extends OptionBoolean {
	
	public CutClean(TaupeGun main, InventoryGUI parent) {
		super(main, "CutClean", "Aucun cuisson n'est nécessaire avec ce scénario.", "MENU_CONFIGURATION_SCENARIO_CUTCLEAN", Material.IRON_INGOT, parent, false);
	}
	
	/**
	 * Lorsqu'un item spawn, on le spawn cuit
	 * 
	 * @param e L'évènement de spawn d'un item
	 */
	@EventHandler
	final public void onItemSpawn(ItemSpawnEvent e){
		if (!this.value)
			return;

		ItemStack item = e.getEntity().getItemStack();
		if (Objects.isNull(item))
			return;

		ItemStack newItem = null;
		ExperienceOrb exp;

		switch (item.getType()){
			case IRON_ORE:
				newItem = new ItemStack(Material.IRON_INGOT);
				exp = (ExperienceOrb) e.getLocation().getWorld().spawnEntity(e.getLocation(), EntityType.EXPERIENCE_ORB);
				exp.setExperience(1);
				break;
			case GOLD_ORE:
				newItem = new ItemStack(Material.GOLD_INGOT);
				exp = (ExperienceOrb) e.getLocation().getWorld().spawnEntity(e.getLocation(), EntityType.EXPERIENCE_ORB);
				exp.setExperience(1);
				break;
			case SAND:
				newItem = new ItemStack(Material.GLASS);
				break;
		}

		if (Objects.nonNull(newItem)) {
			e.getEntity().setItemStack(newItem);
		}
	}
	
	/**
	 * Lorsqu'un mob meurt, on drop la nourriture cuite
	 * 
	 * @param e L'évènement de mort d'un mob
	 */
	@EventHandler
	final public void onEntityDeath(EntityDeathEvent e){
		if (!this.value)
			return;

		List<ItemStack> loots = e.getDrops();
		for (int i = loots.size() - 1; i >= 0; i--){

			ItemStack item = loots.get(i);
			if (Objects.isNull(item))
				continue;

			switch (item.getType()){
				case RAW_BEEF:
					loots.remove(i);
					loots.add(new ItemStack(Material.COOKED_BEEF));
					break;
				case PORK:
					loots.remove(i);
					loots.add(new ItemStack(Material.GRILLED_PORK));
					break;
				case RAW_CHICKEN:
					loots.remove(i);
					loots.add(new ItemStack(Material.COOKED_CHICKEN));
					break;
				case MUTTON:
					loots.remove(i);
					loots.add(new ItemStack(Material.COOKED_MUTTON));
					break;
				case RABBIT:
					loots.remove(i);
					loots.add(new ItemStack(Material.COOKED_RABBIT));
					break;
				default:
					return;
			}
		}
	}

}
