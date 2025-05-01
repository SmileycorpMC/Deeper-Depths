package com.deeperdepths.common.blocks.tiles;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TileSculkCatalyst extends TileEntity implements ITickable
{
    public final List<SculkCharge> activeSpreaders = new ArrayList<>();

    @Override
    public void update()
    {
        if (world.isRemote) return;

        Iterator<SculkCharge> iter = activeSpreaders.iterator();

        while (iter.hasNext())
        {
            SculkCharge spreader = iter.next();
            boolean stillAlive = spreader.update();

            if (!stillAlive) iter.remove();
        }
    }
}