package com.thevoxelbox.voxelgadget.modifier;

import com.thevoxelbox.voxelgadget.Processor;
import org.bukkit.Material;

public enum ModifierType {

	PLACE(new BlockPlaceMode(), Type.MODE, new ComboBlock(Material.IRON_BLOCK)),
	TOGGLE(new BlockToggleMode(), Type.MODE, new ComboBlock(Material.LAPIS_BLOCK)),
	REMOVE(new BlockRemoveMode(), Type.MODE, new ComboBlock(Material.DIAMOND_BLOCK)),
	NO_PHYSICS(new NoPhysicsModifier(), Type.SPECIAL, new ComboBlock(Material.WOOL, (byte) 8)),
	OVERRIDE(new OverrideModifier(), Type.SPECIAL, new ComboBlock(Material.WOOL, (byte) 14)),
	INVENTORY(new InventoryModifier(), Type.SPECIAL, new ComboBlock(Material.WOOL, (byte) 12)),
	THREE_DIM(new ThreeDimModifier(), Type.SPECIAL, new ComboBlock(Material.WOOL, (byte) 13)),
	TIMER(new TimerModifier(), Type.SPECIAL, new ComboBlock(Material.WOOL, (byte) 7)),
	LINE(new LineModifier(), Type.SPECIAL, new ComboBlock(Material.WOOL, (byte) 2)),
	AREA(new AreaModifier(), Type.SPECIAL, new ComboBlock(Material.WOOL, (byte) 1)),
	FILTER(new FilterModifier(), Type.SPECIAL, new ComboBlock(Material.WOOL, (byte) 4)),
	SKIP(new SkipModifier(), Type.SPECIAL, new ComboBlock(Material.WOOL, (byte) 0)),
	DUMMY(null, Type.SPECIAL, new ComboBlock(Material.WOOL, (byte) 6)),
	ADD1(new OffsetModifier(1), Type.OFFSET, new ComboBlock(Material.WOOL, (byte) 9)),
	ADD5(new OffsetModifier(5), Type.OFFSET, new ComboBlock(Material.WOOL, (byte) 11)),
	ADD10(new OffsetModifier(10), Type.OFFSET, new ComboBlock(Material.WOOL, (byte) 10)),
	ADD25(new OffsetModifier(25), Type.OFFSET, new ComboBlock(Material.WOOL, (byte) 3)),
	SUBTRACT1(new OffsetModifier(-1), Type.OFFSET, new ComboBlock(Material.WOOL, (byte) 5)),
	SUBTRACT5(new OffsetModifier(-5), Type.OFFSET, new ComboBlock(Material.WOOL, (byte) 15)),
	DECODER(new DecoderModifier(), Type.SPECIAL, new ComboBlock(Material.STAINED_CLAY, (byte) 5)),
	SET_INVENTORY(new SetInventoryModifier(), Type.MODE_OVERRIDE, new ComboBlock(Material.STAINED_CLAY, (byte) 13)),
	GET_INVENTORY(new GetInventoryModifier(), Type.MODE_OVERRIDE, new ComboBlock(Material.STAINED_CLAY, (byte) 3)),
	CHECK_PLAYER(new CheckPlayerModifier(), Type.CHECK, new ComboBlock(Material.STAINED_CLAY, (byte) 10)),
	CHECK_LESS(new CheckLessModifier(), Type.CHECK, new ComboBlock(Material.STAINED_CLAY, (byte) 2)),
	CHECK_GREATER(new CheckGreaterModifier(), Type.CHECK, new ComboBlock(Material.STAINED_CLAY, (byte) 15)),
	CHECK_EQUALS(new CheckEqualsModifier(), Type.CHECK, new ComboBlock(Material.STAINED_CLAY, (byte) 7)),
	CHECK_BLOCK_EQUALS(new CheckBlockEqualsModifier(), Type.CHECK, new ComboBlock(Material.STAINED_CLAY, (byte) 12)),
	CHECK_BLOCK_EXISTS(new CheckBlockExistsModifier(), Type.CHECK, new ComboBlock(Material.STAINED_CLAY, (byte) 9));

	public enum Type {

		MODE, MODE_OVERRIDE, OFFSET, SPECIAL, CHECK;
	}

	private final AbstractModifier modifier;
	private final ComboBlock defaultBlock;
	private final Type type;

	private ModifierType(AbstractModifier modifier, Type type, ComboBlock defaultBlock) {
		this.modifier = modifier;
		this.defaultBlock = defaultBlock;
		this.type = type;
	}

	/**
	 * Calls the modify() method of the Modifier
	 *
	 * @param p the Processor that called the method
	 * @return true if the modifications were successful
	 */
	public int callModify(Processor p) {
		if (modifier != null) return modifier.modify(p);
		return 0;
	}

	/**
	 * Calls the modeModify() method of the Mode Modifier
	 *
	 * @param p the Processor that called the method
	 * @return false if the Modifier is not a Mode Modifier
	 */
	public int callModeModify(Processor p) {
		if (modifier instanceof AbstractModeModifier) return ((AbstractModeModifier) modifier).modeModify(p);
		else return 0;
	}

	/**
	 * @return the Type of the ModeModifier
	 */
	public Type getType() {
		return type;
	}

	/**
	 * @return The default block for the Modifier. Used in generating the default configuration.
	 */
	public ComboBlock getDefaultBlock() {
		return defaultBlock;
	}

}
