package fr.thedarven.configuration.builders;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import fr.thedarven.configuration.builders.teams.InventoryTeams;
import fr.thedarven.main.TaupeGun;
import fr.thedarven.main.constructors.EnumConfiguration;
import fr.thedarven.main.constructors.PlayerTaupe;

public class InventoryGUI extends InventoryBuilder{
	
	protected Inventory inventory;
	protected ArrayList<InventoryGUI> childs = new ArrayList<>();
	private static ArrayList<InventoryGUI> elements = new ArrayList<>();
	
	public InventoryGUI(String pName, String pDescription, int pLines, Material pItem, InventoryGUI pParent, int pPosition, byte pData) {
		super(pName, pDescription, pLines, pItem, pParent, pPosition, pData);
		initInventory();
		elements.add(this);
	}
	
	public InventoryGUI(String pName, String pDescription, int pLines, Material pItem, InventoryGUI pParent, byte pData) {
		super(pName, pDescription, pLines, pItem, pParent, pData);
		initInventory();
		elements.add(this);
	}
	
	public InventoryGUI(String pName, String pDescription, int pLines, Material pItem, InventoryGUI pParent, int pPosition) {
		super(pName, pDescription, pLines, pItem, pParent, pPosition, (byte) 0);
		initInventory();
		elements.add(this);
	}
	
	public InventoryGUI(String pName, String pDescription, int pLines, Material pItem, InventoryGUI pParent) {
		super(pName, pDescription, pLines, pItem, pParent, (byte) 0);
		initInventory();
		elements.add(this);
	}
	
	
	private void initInventory() {
		Inventory inv = Bukkit.createInventory(null, this.getLines()*9, this.getName());
		
		if(this.getParent() != null) {
			ItemStack redstone = new ItemStack(Material.REDSTONE, 1);
			ItemMeta redstoneM = redstone.getItemMeta();
			redstoneM.setDisplayName(ChatColor.RED+"Retour");
			redstone.setItemMeta(redstoneM);
			inv.setItem(this.getLines()*9-1, redstone);
			
			this.getParent().addItem(this);
		}
		
		this.inventory = inv;
	}
	
	public void addItem(InventoryGUI pInventoryGUI) {
		boolean setItem = true;
		if(this.inventory.getSize() <= pInventoryGUI.getPosition() || this.inventory.getItem(pInventoryGUI.getPosition()) != null) {
			int i = 0;
			boolean boucle = true;
			while(boucle && i < this.inventory.getSize()) {
				if(this.inventory.getItem(i) == null){
					boucle = false;
					pInventoryGUI.setPosition(i);
					childs.add(pInventoryGUI);
					setItem = true;
				}
				i++;
			}
			if(boucle) {
				System.out.println("\\033[31mErreur de positionnement de l'item "+pInventoryGUI.getName());
			}
		}else {
			setItem = true;
			childs.add(pInventoryGUI);
		}
		
		if(setItem) {
			this.inventory.setItem(pInventoryGUI.getPosition(), pInventoryGUI.getItem());
		}
	}
	
	public void removeItem(InventoryGUI pInventoryGUI) {
		inventory.remove(pInventoryGUI.getItem());
	}
	
	public void modifiyPosition(InventoryGUI pInventoryGUI, int pPosition) {
		if(inventory.getItem(pPosition) != null) {
			System.out.println("\\033[31mPosition déjà utilisée par un autre item : "+pInventoryGUI.getName());
			return;
		}
		inventory.remove(pInventoryGUI.getItem());
		inventory.setItem(pPosition, pInventoryGUI.getItem());
	}
	
	public void modifyItemstack(int pHashCode, ItemStack pNewItem) {
		for(int i=0; i<inventory.getSize(); i++) {
			if(inventory.getItem(i) != null && inventory.getItem(i).hashCode() == pHashCode) {
				inventory.setItem(i, pNewItem);
				return;
			}
		}
	}
	
	public Inventory getInventory() {
		return inventory;
	}
	
	public ArrayList<InventoryGUI> getChilds(){
		return this.childs;
	}
	
	@EventHandler
	public void dragInventory(InventoryDragEvent e) {
		if(e.getInventory() != null) {
			for(InventoryGUI inventoryGUI : elements) {
				if(e.getInventory().equals(inventoryGUI.getInventory())) {
					e.setCancelled(true);
					return;
				}
			}	
		}
	}
	
	@EventHandler
	public void clickInventory(InventoryClickEvent e){
		
		if((e.isShiftClick() || e.getClick().equals(ClickType.DOUBLE_CLICK)) && e.getClickedInventory() != null) {
			for(InventoryGUI inventoryGUI : elements) {
				if(e.getInventory().equals(inventoryGUI.getInventory())) {
					e.setCancelled(true);
					return;
				}
			}	
		}
		
		if(e.getWhoClicked() instanceof Player && e.getClickedInventory() != null && e.getClickedInventory().equals(this.inventory)) {
			Player p = (Player) e.getWhoClicked();
			e.setCancelled(true);
			
			if(click(p, EnumConfiguration.INVENTAIRE) && !e.getCurrentItem().getType().equals(Material.AIR)) {
				if(e.getCurrentItem().getType().equals(Material.REDSTONE) && e.getRawSlot() == this.getLines()*9-1 && e.getCurrentItem().getItemMeta().getDisplayName().equals("§cRetour")){
					p.openInventory(this.getParent().getInventory());
					return;
				}
				for(InventoryGUI inventoryGUI : childs) {
					if(inventoryGUI.getItem().equals(e.getCurrentItem()) && inventoryGUI != InventoryRegister.addteam) {
						if(inventoryGUI instanceof OptionBoolean || inventoryGUI instanceof OptionNumeric || inventoryGUI instanceof InventoryTeams || inventoryGUI instanceof InventoryStartItem) {
							if(click(p, EnumConfiguration.OPTION)) {
								p.openInventory(inventoryGUI.getInventory());
							}
						}else {
							p.openInventory(inventoryGUI.getInventory());
						}
						return;
					}
				}
			}
		}
	}
	public void delayClick(final PlayerTaupe pl) {
		pl.setCanClick(false);
		new BukkitRunnable(){
			
			@Override
			public void run() {
				pl.setCanClick(true);
				this.cancel();
			}
			
		}.runTaskTimer(TaupeGun.instance,3,20);
	}
	
	public static ArrayList<InventoryGUI> getElements(){
		return elements;
	}
	
}
