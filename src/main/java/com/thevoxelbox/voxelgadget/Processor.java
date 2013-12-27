package com.thevoxelbox.voxelgadget;

import com.thevoxelbox.voxelgadget.modifier.ComboBlock;
import com.thevoxelbox.voxelgadget.modifier.ModifierType;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dispenser;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class Processor {

	final Map<Integer, ModifierType> config;
	public final BlockFace[] faces = {BlockFace.DOWN, BlockFace.UP, BlockFace.NORTH, BlockFace.EAST, BlockFace.WEST, BlockFace.SOUTH};
	private final VoxelGadget gadget;
	private int offset = 1;
	private int size = 0;
	private int delay = 0;
	private Location offset3D = null;
	private boolean areaEnabled = false;
	private boolean lineEnabled = false;
	private boolean timerEnabled = false;
	private boolean skipFirstWithTimer = false;
	private ItemStack dispensedItem;
	private Block override = null;
	private boolean overrideAbsolute = false;
	private Block filter = null;
	private Block dispenser;
	private ModifierType mode;
	private BlockFace train = null;
	private boolean applyPhysics = true;
	private int current = 0;
	private boolean check = false;
	private boolean checkEnabled = false;
	private Inventory invOverride = null;

	public Processor(Map<Integer, ModifierType> config, VoxelGadget gadget) {
		this.config = config;
		this.gadget = gadget;
	}
	private final ArrayList<ModifierType> checkLater = new ArrayList<ModifierType>();

	/**
	 * Does the heavy lifting when a Dispenser is triggered
	 * @param dispenser The block of the Dispenser that was fired
	 * @param dispensed The block dispensed by the Gadget.
	 * @param initial Should always be true if called from outside of this method. Used for Timer Modifiers.
	 * @return true if the block was actually a Gadget
	 */
	public boolean process(final Block dispenser, final ItemStack dispensed, final boolean initial) {
		if (!initial && !isCheckEnabled()) {
			getMode().callModeModify(this);
			return true;
		}
		this.dispenser = dispenser;
		this.setDispensed(dispensed);
		for (BlockFace face : faces) {
			Block possibleModeBlock = dispenser.getRelative(face);
			ComboBlock possibleModeCombo = new ComboBlock(possibleModeBlock.getTypeId(), possibleModeBlock.getData());
			ModifierType mode_ = getModifierFromConfig(possibleModeCombo);
			if (mode_ != null && mode_.getType() == ModifierType.Type.MODE) {
				this.setMode(mode_);
				this.train = face;
				break;
			}
		}
		if (getTrain() == null) {
			return false;
		}
		for (current = 2; current < 64; current++) {
			Block b = dispenser.getRelative(getTrain(), current);
			ModifierType modifier = getModifierFromConfig(new ComboBlock(b));
			if (modifier == null) {
				break;
			} else if (modifier.getType() == ModifierType.Type.CHECK) {
				checkLater.add(modifier);
				Block behind = dispenser.getRelative(getTrain(), current + 1);
				if (behind.getState() instanceof InventoryHolder) {
					this.setInvOverride(((InventoryHolder) behind.getState()).getInventory());
					current++;
				}
			} else {
				int skip = modifier.callModify(this, dispenser.getRelative(getTrain(), current + 1));
				current += skip;
			}
		}
		for (ModifierType modifier : checkLater) {
			modifier.callModify(this, dispenser.getRelative(getTrain(), current + 1));
		}
		if (initial && isTimerEnabled()) {
			final Processor owner = this;
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(getGadget(), new Runnable() {
				public void run() {
					if (!owner.isCheckEnabled()) {
						owner.process(dispenser, dispensed, false);
					} else {
						(new Processor(config, gadget)).process(dispenser, dispensed, false);
					}
				}
			}, getDelay());
		}
		if (checkEnabled) {
			final Block lastModifierBlock = dispenser.getRelative(train, getCurrent() - 1);
			ModifierType lastModifier = getModifierFromConfig(new ComboBlock(lastModifierBlock));
			boolean doRedstoneBlock = true;
			if (check && lastModifier == ModifierType.PATCH) {
				for (BlockFace face : faces) {
					if (face != train.getOppositeFace()) {
						Block possibleGadget = lastModifierBlock.getRelative(face);
						if (possibleGadget.getState() instanceof Dispenser) {
							Dispenser disp = (Dispenser) possibleGadget.getState();
							//get a random item from the dispenser to simulate it being triggered
							ArrayList<ItemStack> itemsList = new ArrayList<ItemStack>();
							for (int i = 0; i < 9; i++) {
								ItemStack item = disp.getInventory().getItem(i);
								if (item != null && item.getTypeId() != 0) {
									itemsList.add(item);
								}
							}
							if (itemsList.size() > 0) {
								ItemStack random = itemsList.get((new Random()).nextInt(itemsList.size()));
								boolean wasAGadget = (new Processor(config, gadget)).process(disp.getBlock(), random, true);
								if (wasAGadget && face == train) {
									doRedstoneBlock = false;
								}
							}
						}
					}
				}
			} else if (lastModifier == ModifierType.PATCH) {
				Block possibleDispenser = dispenser.getRelative(train, getCurrent());
				if (possibleDispenser.getType() == Material.DISPENSER) {
					ModifierType possibleMode = getModifierFromConfig(new ComboBlock(dispenser.getRelative(train, getCurrent() + 1)));
					if(possibleMode != null && possibleMode.getType() == ModifierType.Type.MODE){
						doRedstoneBlock = false;
					}
				}
			}
			if (doRedstoneBlock) {
				final Block last = dispenser.getRelative(train, getCurrent());
				last.setType((check ? Material.REDSTONE_BLOCK : Material.GLASS));
				last.setData((byte) 0);
				if (getMode() == ModifierType.TOGGLE) {
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(getGadget(), new Runnable() {
						public void run() {
							last.setType(Material.GLASS);
						}
					}, 2);
				}
			}
			return true;
		} else {
			if (!willSkipFirst()) getMode().callModeModify(this);
			return true;
		}
	}

	/**
	 * Gets the ModifierType of a given block based on the user's configuration
	 * @param block The block in question
	 * @return The ModifierType of the given block. Null if the block is not a modifier.
	 */
	public ModifierType getModifierFromConfig(ComboBlock block) {
		return config.get(((block.getID() << 8) | block.getData()));
	}

	/**
	 * Processes offset modifiers. Will add to a value based on where in the tail the modifier was.
	 * @param add 
	 */
	public void addOffset(int add) {
		if (isTimerEnabled()) {
			setDelay(getDelay() + add * 2);
		} else if (isAreaEnabled() || isLineEnabled()) {
			addSize(add);
		} else {
			setOffset(getOffset() + add);
		}
	}

	public void setOffset(int newOffset) {
		offset = newOffset;
	}

	public int getOffset() {
		return offset;
	}

	public void addSize(int add) {
		setSize(getSize() + add);
		if (getSize() > 100) {
			setSize(100);
		} else if (getSize() < -1) {
			setSize(-1);
		}
	}

	public int getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * @return the gadget
	 */
	public VoxelGadget getGadget() {
		return gadget;
	}

	/**
	 * @return the delay
	 */
	public int getDelay() {
		return delay;
	}

	/**
	 * @param delay the delay to set
	 */
	public void setDelay(int delay) {
		this.delay = delay;
	}

	/**
	 * @return the areaEnabled
	 */
	public boolean isAreaEnabled() {
		return areaEnabled;
	}

	/**
	 * @param areaEnabled the areaEnabled to set
	 */
	public void setAreaEnabled(boolean areaEnabled) {
		this.areaEnabled = areaEnabled;
	}

	/**
	 * @return the lineEnabled
	 */
	public boolean isLineEnabled() {
		return lineEnabled;
	}

	/**
	 * @param lineEnabled the lineEnabled to set
	 */
	public void setLineEnabled(boolean lineEnabled) {
		this.lineEnabled = lineEnabled;
	}

	/**
	 * @return the timerEnabled
	 */
	public boolean isTimerEnabled() {
		return timerEnabled;
	}

	/**
	 * @param timerEnabled the timerEnabled to set
	 */
	public void setTimerEnabled(boolean timerEnabled) {
		this.timerEnabled = timerEnabled;
	}

	/**
	 * @return the block
	 */
	public ItemStack getDispensed() {
		return dispensedItem;
	}

	/**
	 * @param block the block to set
	 */
	public void setDispensed(ItemStack block) {
		this.dispensedItem = block;
	}

	/**
	 * @return the override
	 */
	public Block getOverride() {
		return override;
	}

	/**
	 * @param override the override to set
	 */
	public void setOverride(Block override) {
		this.override = override;
	}

	/**
	 * @return the overrideAbsolute
	 */
	public boolean isOverrideAbsolute() {
		return overrideAbsolute;
	}

	/**
	 * @param overrideAbsolute the overrideAbsolute to set
	 */
	public void setOverrideAbsolute(boolean overrideAbsolute) {
		this.overrideAbsolute = overrideAbsolute;
	}

	/**
	 * @return the filter
	 */
	public Block getFilter() {
		return filter;
	}

	/**
	 * @param filter the filter to set
	 */
	public void setFilter(Block filter) {
		this.filter = filter;
	}

	/**
	 * @return the dispenser
	 */
	public Block getDispenser() {
		return dispenser;
	}

	/**
	 * @return the current Mode of the gadget.
	 */
	public ModifierType getMode() {
		return mode;
	}

	/**
	 *
	 * @param mode
	 * @throws IllegalArgumentException if mode is not a Mode Modifier
	 */
	public void setMode(ModifierType mode) {
		if (mode.getType() == ModifierType.Type.MODE || mode.getType() == ModifierType.Type.MODE_OVERRIDE) {
			this.mode = mode;
		} else {
			throw new IllegalArgumentException("Modifier must be a Mode Modifier");
		}
	}

	/**
	 * @return the BlockFace/Direction of the dispenser's train
	 */
	public BlockFace getTrain() {
		return train;
	}

	/**
	 * @return the applyPhysics
	 */
	public boolean applyPhysics() {
		return applyPhysics;
	}

	/**
	 * @param applyPhysics the applyPhysics to set
	 */
	public void setApplyPhysics(boolean applyPhysics) {
		this.applyPhysics = applyPhysics;
	}

	/**
	 * @return the offset3D
	 */
	public Location getOffset3D() {
		return offset3D;
	}

	/**
	 * @param offset3D the offset3D to set
	 */
	public void setOffset3D(Location offset3D) {
		this.offset3D = offset3D;
	}

	/**
	 * @return the current offset from the dispenser
	 */
	public int getCurrent() {
		return current;
	}

	/**
	 * @return the check
	 */
	public boolean getCheck() {
		return check;
	}

	/**
	 * @param check the check to set
	 */
	public void setCheck(boolean check) {
		this.check = check;
		if (checkEnabled == false) {
			checkEnabled = true;
		}
	}

	/**
	 * @return the checkEnabled
	 */
	public boolean isCheckEnabled() {
		return checkEnabled;
	}

	public Inventory getInvOverride() {
		return invOverride;
	}

	public void setInvOverride(Inventory invOverride) {
		this.invOverride = invOverride;
	}

	/**
	 * @return the skipFirstWithTimer
	 */
	public boolean willSkipFirst() {
		return skipFirstWithTimer;
	}

	/**
	 * @param skipFirstWithTimer the skipFirstWithTimer to set
	 */
	public void setWillSkipFirst(boolean skipFirstWithTimer) {
		this.skipFirstWithTimer = skipFirstWithTimer;
	}
}
