package com.thevoxelbox.voxelgadget.modifier;

import com.thevoxelbox.voxelgadget.Processor;
import org.bukkit.block.Block;

public class BlockToggleMode extends AbstractModeModifier {

	@Override
	public int modify(Processor p, Block nextBlock) {
		if (p.getDispensed().getTypeId() == 387) { //handle for blueprints
			BlueprintModifier blueprint = new BlueprintModifier();
			if (blueprint.checkIfExists(p)) blueprint.remove(p);
			else blueprint.paste(p);
			return 0;
		}
		Block existing = p.getDispenser().getRelative(p.getTrain().getOppositeFace(), (p.isLineEnabled()
				|| p.isAreaEnabled() ? p.getSize() + (p.isLineEnabled() ? 2 : 1) : p.getOffset()));
		if (p.getOffset3D() != null) existing = p.getOffset3D().getBlock();
		if (p.isOverrideAbsolute()) {
			toggleBetween(existing, p.getDispensed().getTypeId(), p.getDispensed().getData().getData(), p.getOverride().getTypeId(), p.getOverride().getData(), p);
		} else {
			int placeID = p.getDispensed().getTypeId();
			byte placeData = p.getDispensed().getData().getData();
			if(p.getOverride() != null){
				placeID = p.getOverride().getTypeId();
				placeData = p.getOverride().getData();
			}
			toggleBetween(existing, 0, (byte) 0, placeID, placeData, p);
		}
		return 0;
	}
	
	private void toggleBetween(Block existing, int idA, byte dataA, int idB, byte dataB, Processor p){
		//System.out.println("Toggling between " + idA + ":" + dataA + " and " + idB + ":" + dataB);
		//System.out.println("Current = " + existing.getTypeId() + ":" + existing.getData());
		int placeID;
		byte placeData;
		if(existing.getTypeId() == idB && existing.getData() == dataB){
			placeID = idA;
			placeData = dataA;
		} else {
			placeID = idB;
			placeData = dataB;
		}
		//System.out.println("Placing " + placeID + ":" + placeData);
		this.setBlock(existing, placeID, placeData, p.applyPhysics(), p);
	}
	
}
