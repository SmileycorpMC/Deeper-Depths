package net.smileycorp.deeperdepths.common.blocks.enums;

import com.google.common.collect.Maps;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.EnumMap;

public enum EnumClusterSize {
    SMALL("small", 1, 0.25, 0.1875),
    MEDIUM("medium", 2, 0.1875, 0.25),
    LARGE("large", 4, 0.1875, 0.3125),
    CLUSTER("cluster", 5, 0.1875, 0.4375);
    
    private final String name;
    private final int light;
    private final EnumMap<EnumFacing, AxisAlignedBB> AABBS = Maps.newEnumMap(EnumFacing.class);
    
    EnumClusterSize(String name, int light, double distanceFromEdge, double height) {
        this.name = name;
        this.light = light;
        for (EnumFacing facing : EnumFacing.values()) AABBS.put(facing, createAABB(facing, distanceFromEdge, height));
    }
    
    public String getName() {
        return name;
    }
    
    public int getLight() {
        return light;
    }
    
    public AxisAlignedBB getAABB(EnumFacing facing) {
        return AABBS.get(facing);
    }
    
    public EnumClusterSize next() {
        return this == CLUSTER ? this : values()[ordinal() + 1];
    }
    
    private static AxisAlignedBB createAABB(EnumFacing facing, double distanceFromEdge, double height) {
        switch (facing) {
            case DOWN:
                return new AxisAlignedBB(distanceFromEdge, 1 - height, distanceFromEdge,
                        1 - distanceFromEdge, 1, 1 - distanceFromEdge);
            case NORTH:
                return new AxisAlignedBB(distanceFromEdge, distanceFromEdge, 1 - height,
                        1 - distanceFromEdge, 1 - distanceFromEdge, 1);
            case SOUTH:
                return new AxisAlignedBB(distanceFromEdge, distanceFromEdge, 0,
                        1 - distanceFromEdge, 1 - distanceFromEdge, height);
            case WEST:
                return new AxisAlignedBB(1 - height, distanceFromEdge, distanceFromEdge,
                        1, 1 - distanceFromEdge, 1 - distanceFromEdge);
            case EAST:
                return new AxisAlignedBB(0, distanceFromEdge, distanceFromEdge,
                        height, 1 - distanceFromEdge, 1 - distanceFromEdge);
            default:
                return new AxisAlignedBB(distanceFromEdge, 0, distanceFromEdge,
                        1 - distanceFromEdge, height, 1 - distanceFromEdge);
        }
    }
    
}
