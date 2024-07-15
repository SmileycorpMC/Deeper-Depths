package net.smileycorp.deeperdepths.common.blocks.tiles;

import net.minecraft.tileentity.TileEntity;
import net.smileycorp.deeperdepths.common.blocks.enums.EnumVaultState;

public class TileVault extends TileEntity {

    private EnumVaultState state = EnumVaultState.INACTIVE;
    
    public EnumVaultState getState() {
        return state;
    }
    
    public void setState(EnumVaultState state) {
        this.state = state;
    }
    
}
