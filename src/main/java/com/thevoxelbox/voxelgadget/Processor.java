package com.thevoxelbox.voxelgadget;

import com.thevoxelbox.voxelgadget.modifier.ComboBlock;
import com.thevoxelbox.voxelgadget.modifier.ModifierType;
import java.util.HashMap;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

public class Processor {

	final HashMap<ModifierType, ComboBlock> config;
	final BlockFace[] faces = {BlockFace.DOWN, BlockFace.UP, BlockFace.NORTH, BlockFace.EAST, BlockFace.WEST, BlockFace.SOUTH};

	private final VoxelGadget gadget;
	private int offset = 1;
	private int size = 0;
	private int delay = 0;
	private Location offset3D = null;
	private boolean areaEnabled = false;
	private boolean lineEnabled = false;
	private boolean timerEnabled = false;
	private ItemStack block;
	private Block override = null;
	private boolean overrideAbsolute = false;
	private Block filter = null;
	private Block dispenser;
	private ModifierType mode;
	private BlockFace train = null;
	private boolean applyPhysics = true;
	private int current = 0;

	public Processor(HashMap<ModifierType, ComboBlock> config, boolean infinite, VoxelGadget gadget) {
		this.config = config;
		this.gadget = gadget;
	}

	public int process(final Block dispenser, final ItemStack block, final boolean initial) {
		this.dispenser = dispenser;
		this.setBlock(block);
		for (BlockFace face : faces) {
			Block possibleModeBlock = dispenser.getRelative(face);
			ComboBlock possibleModeCombo = new ComboBlock(possibleModeBlock.getTypeId(), possibleModeBlock.getData());
			ModifierType mode = getModifierFromConfig(possibleModeCombo);
			if (mode != null && mode.getType() == ModifierType.Type.MODE) {
				this.setMode(mode);
				this.train = face;
				break;
			}
		}
		if (getTrain() == null) return 0;
		if (!initial) return getMode().callModeModify(this);
		for (current = 2; current < 64; current++) {
			Block b = dispenser.getRelative(getTrain(), current);
			ModifierType modifier = getModifierFromConfig(new ComboBlock(b));
			if (modifier == null) break;
			else {
				int skip = modifier.callModify(this);
				current += skip;
			}
		}
		if (initial && isTimerEnabled()) {
			final Processor owner = this;
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(getGadget(), new Runnable() {
				public void run() {
					owner.process(dispenser, block, false);
				}
			}, getDelay());
		}
		return getMode().callModeModify(this);
	}

	public ModifierType getModifierFromConfig(ComboBlock block) {
		for (Entry<ModifierType, ComboBlock> type : config.entrySet()) {
			if (type.getValue().getID() == block.getID()) {
				if (block.getID() == Material.WOOL.getId()) {
					if (type.getValue().getData() == block.getData()) return type.getKey();
				} else return type.getKey();
			}
		}
		return null;
	}

	public void addOffset(int add) {
		if (isTimerEnabled()) setDelay(getDelay() + add * 2);
		else if (isAreaEnabled() || isLineEnabled()) addSize(add);
		else setOffset(getOffset() + add);
	}

	public void setOffset(int newOffset) {
		offset = newOffset;
	}

	public int getOffset() {
		return offset;
	}

	public void addSize(int add) {
		setSize(getSize() + add);
		if (getSize() > 100) setSize(100);
		else if (getSize() < -1) setSize(-1);
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
	public ItemStack getBlock() {
		return block;
	}

	/**
	 * @param block the block to set
	 */
	public void setBlock(ItemStack block) {
		this.block = block;
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
		if (mode.getType() != ModifierType.Type.MODE) throw new IllegalArgumentException("Modifier must be a Mode Modifier");
		this.mode = mode;
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

}
