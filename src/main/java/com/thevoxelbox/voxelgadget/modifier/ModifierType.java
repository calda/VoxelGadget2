package com.thevoxelbox.voxelgadget.modifier;

import com.thevoxelbox.voxelgadget.Processor;
import org.bukkit.Material;

public enum ModifierType {

    PLACE(new BlockPlaceMode(), Type.MODE, new ComboBlock(Material.IRON_BLOCK)),
    TOGGLE(new BlockToggleMode(), Type.MODE, new ComboBlock(Material.LAPIS_BLOCK)),
    REMOVE(new BlockRemoveMode(), Type.MODE, new ComboBlock(Material.DIAMOND_BLOCK)),
    NO_PHYSICS(new NoPhysicsModifier(), Type.SPECIAL, new ComboBlock(Material.WOOL, (byte) 8)),
    OVERRIDE(null, Type.SPECIAL, new ComboBlock(Material.WOOL, (byte) 14)),
    INVENTORY(new InventoryModifier(), Type.SPECIAL, new ComboBlock(Material.WOOL, (byte) 12)),
    TIMER(null, Type.SPECIAL, new ComboBlock(Material.WOOL, (byte) 7)),
    LINE(null, Type.SPECIAL, new ComboBlock(Material.WOOL, (byte) 2)),
    AREA(null, Type.SPECIAL, new ComboBlock(Material.WOOL, (byte) 1)),
    FILTER(null, Type.SPECIAL, new ComboBlock(Material.WOOL, (byte) 4)),
    SKIP(null, Type.SPECIAL, new ComboBlock(Material.WOOL, (byte) 0)),
    DUMMY(null, Type.SPECIAL, new ComboBlock(Material.WOOL, (byte) 6)),
    ADD1(new OffsetModifier(1), Type.OFFSET, new ComboBlock(Material.WOOL, (byte) 9)),
    ADD5(new OffsetModifier(5), Type.OFFSET, new ComboBlock(Material.WOOL, (byte) 11)),
    ADD10(new OffsetModifier(10), Type.OFFSET, new ComboBlock(Material.WOOL, (byte) 10)),
    ADD25(new OffsetModifier(25), Type.OFFSET, new ComboBlock(Material.WOOL, (byte) 3)),
    SUBTRACT1(new OffsetModifier(-1), Type.OFFSET, new ComboBlock(Material.WOOL, (byte) 5)),
    SUBTRACT5(new OffsetModifier(-5), Type.OFFSET, new ComboBlock(Material.WOOL, (byte) 15));

    public enum Type {
        MODE, OFFSET, SPECIAL, FUN;
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
     * @param p the Processor that called the method
     * @return true if the modifications were successful
     */
    public boolean callModify(Processor p) {
        if (modifier != null) return modifier.modify(p);
        return true;
    }

    /**
     * Calls the modeModify() method of the Mode Modifier
     * @param p the Processor that called the method
     * @return false if the Modifier is not a Mode Modifier
     */
    public boolean callModeModify(Processor p) {
        if (modifier instanceof AbstractModeModifier) return ((AbstractModeModifier) modifier).modeModify(p);
        else return false;
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
