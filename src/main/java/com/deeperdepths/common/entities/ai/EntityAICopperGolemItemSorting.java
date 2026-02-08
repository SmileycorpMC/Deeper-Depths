package com.deeperdepths.common.entities.ai;

import com.deeperdepths.common.blocks.tiles.TileCopperChest;
import com.deeperdepths.common.entities.EntityCopperGolem;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;

import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The Copper Golem sorting AI. Swaps about stages while moving items.
 *
 * This feels like a deck of cards AAAAAGH
 * */
public class EntityAICopperGolemItemSorting extends EntityAIBase
{
    private final EntityCopperGolem golem;

    private enum Stage
    {
        IDLE,
        WALK,
        INTERACT
    }
    /** Containers that have been checked, and thus can be ignored. */
    public Set<BlockPos> checkedContainers = new HashSet<>();

    private Stage currentStage = Stage.IDLE;
    private int stageTicks = 0;
    private static final int INVENTORY_TIME = 60;

    /** Where the Golem needs to move to. */
    public BlockPos chestTarget;
    public BlockPos moveTarget;


    public EntityAICopperGolemItemSorting(EntityCopperGolem golemIn)
    {
        this.golem = golemIn;
        this.setMutexBits(3);
    }

    @Override
    public boolean shouldExecute()
    {
        if (!golem.getHeldItem(EnumHand.MAIN_HAND).isEmpty() || chestTarget != null) return true;

        stageTicks++;

        /* Every 7 seconds, attempt to search for a target chest. */
        if (stageTicks >= 7 * 20)
        {
            //System.out.print("Looking for a chest!");
            findNextTaskPositions();
            stageTicks = 0;
        }

        return false;
    }


    @Override
    public void startExecuting()
    {
        stageTicks = 0;
        golem.resetTaskMemory();
    }

    @Override
    public boolean shouldContinueExecuting()
    { return chestTarget != null || !golem.getHeldItem(EnumHand.MAIN_HAND).isEmpty(); }

    @Override
    public void resetTask()
    {
        currentStage = Stage.IDLE;
        stageTicks = 0;
        golem.getNavigator().clearPath();
    }

    @Override
    public void updateTask()
    {
        stageTicks++;

        switch (currentStage)
        {
            case IDLE:
                findNextTaskPositions();
                break;
            case WALK:
                golem.getLookHelper().setLookPosition(chestTarget.getX() + 0.5, chestTarget.getY(), chestTarget.getZ() + 0.5, 25.0F, 25.0F);

                if (golem.getDistanceSqToCenter(moveTarget) < 1D)
                {
                    //System.out.print("CLOSE ENOUGJH");
                    golem.getNavigator().clearPath();


                    TileEntity te = golem.world.getTileEntity(chestTarget);
                    if (te instanceof TileEntityChest)
                    {
                        /* Holy shit did you know this is a vanilla method used ONLY for BONEMEAL??? This is amazing. */
                        ((TileEntityChest)te).openInventory(net.minecraftforge.common.util.FakePlayerFactory.getMinecraft((net.minecraft.world.WorldServer)golem.world));
                    }
                    else
                    {
                        fullResetSortingTasks();
                        return;
                    }

                    stageTicks = 0;
                    currentStage = Stage.INTERACT;
                }
                else
                {
                    if (stageTicks % 20 == 0 || golem.getNavigator().noPath())
                    {
                        if (!golem.getNavigator().tryMoveToXYZ(moveTarget.getX() + 0.5, moveTarget.getY(), moveTarget.getZ() + 0.5, 1.0))
                        {
                            //System.out.print(" PATHFINDING FAILED! OH NOOOOOO");
                        }
                    }
                }
                break;
            case INTERACT:
                golem.getLookHelper().setLookPosition(chestTarget.getX() + 0.5, chestTarget.getY(), chestTarget.getZ() + 0.5, 25.0F, 25.0F);
                if (++stageTicks >= INVENTORY_TIME)
                { interactWithChest(); }
                break;
        }
    }

    /** Attempts to find the next relevant chests in the AI order. */
    private void findNextTaskPositions()
    {
        Iterable<BlockPos> candidates = getCandidateChests();

        for (BlockPos chestPos : candidates)
        {
            BlockPos movePos = findPathablePosNearChest(chestPos);
            if (movePos == null) continue;

            chestTarget = chestPos;
            moveTarget = movePos;
            currentStage = Stage.WALK;
            stageTicks = 0;
            return;
        }

        fullResetSortingTasks();
    }

    /** Checks for a position near the chest that can be reached. */
    private BlockPos findPathablePosNearChest(BlockPos chestPos)
    {
        EnumFacing facing = EnumFacing.NORTH;
        IBlockState state = golem.world.getBlockState(chestPos);
        if (state.getBlock() instanceof BlockChest)
        { facing = state.getValue(BlockChest.FACING); }

        /* Sorts priority of allowed positions, so golems prefer the front of the chest! */
        List<BlockPos> candidates = new ArrayList<>();
        for (int dy : new int[] {0, 1, -1})
        {
            BlockPos base = chestPos.up(dy);
            candidates.add(base.offset(facing));
            candidates.add(base.offset(facing).offset(facing.rotateY()));
            candidates.add(base.offset(facing).offset(facing.rotateYCCW()));
            candidates.add(base.offset(facing.rotateY()));
            candidates.add(base.offset(facing.rotateYCCW()));
            candidates.add(base.offset(facing.getOpposite()));
        }

        for (BlockPos pos : candidates)
        {
            /* Since there's no clean way to check 'does this path REACH the position we want', instead compare the end point with the position we want. */
            Path path = golem.getNavigator().getPathToPos(pos);
            if (path == null) continue;

            PathPoint end = path.getFinalPathPoint();
            if (end == null) continue;

            if (end.x == pos.getX() && end.y == pos.getY() && end.z == pos.getZ()) return pos.toImmutable();
        }

        return null;
    }

    private Iterable<BlockPos> getCandidateChests()
    {
        List<BlockPos> result = new ArrayList<>();

        if (!golem.getHeldItem(EnumHand.MAIN_HAND).isEmpty())
        {
            for (BlockPos pos : golem.memoryDepositContainers)
            {
                if (isValidChestTarget(pos)) result.add(pos);
            }
        }

        for (BlockPos pos : findNearbyChests(8))
        {
            if (isValidChestTarget(pos)) result.add(pos);
        }

        return result;
    }

    private List<BlockPos> findNearbyChests(int radius)
    {
        List<BlockPos> result = new ArrayList<>();
        BlockPos base = golem.getPosition();

        for (BlockPos pos : BlockPos.getAllInBox( base.add(-radius, -2, -radius), base.add(radius, 2, radius)))
        {
            if (checkedContainers.contains(pos)) continue;
            if (golem.world.getTileEntity(pos) instanceof TileEntityChest) result.add(pos.toImmutable());
        }
        return result;
    }

    private boolean isValidChestTarget(BlockPos pos)
    {
        if (checkedContainers.contains(pos)) return false;

        TileEntity te = golem.world.getTileEntity(pos);
        if (!(te instanceof TileEntityChest)) return false;

        /* If hand is empty, only look for Copper Chests. */
        if (golem.getHeldItem(EnumHand.MAIN_HAND).isEmpty() && te instanceof TileCopperChest) return true;

        /* If holding an item, only find NON-COPPER chests. */
        if (!golem.getHeldItem(EnumHand.MAIN_HAND).isEmpty() && !(te instanceof TileCopperChest)) return true;

        return false;
    }



    private boolean canAccept(TileEntityChest chest)
    {
        if (chest.isEmpty()) return true;
        for (int i = 0; i < chest.getSizeInventory(); i++)
        {
            ItemStack slotStack = chest.getStackInSlot(i);
            if (ItemStack.areItemsEqual(slotStack, golem.getHeldItem(EnumHand.MAIN_HAND)) && slotStack.getCount() < slotStack.getMaxStackSize()) return true;
        }
        return false;
    }

    private void interactWithChest()
    {
        TileEntity te = golem.world.getTileEntity(chestTarget);

        if (!(te instanceof TileEntityChest))
        {
            fullResetSortingTasks();
            return;
        }
        TileEntityChest chest = (TileEntityChest) te;

        if (golem.getHeldItem(EnumHand.MAIN_HAND).isEmpty())
        {
            if (!chest.isEmpty()) withdrawItems(chest);
            else
            {
                checkedContainers.add(chestTarget);
                chest.closeInventory(net.minecraftforge.common.util.FakePlayerFactory.getMinecraft((net.minecraft.world.WorldServer)golem.world));
                stageTicks = 0;
                currentStage = Stage.IDLE;
                return;
            }
        }
        else
        {
            /* Only deposit in acceptable chests, otherwise, find a different one! */
            if (canAccept(chest)) depositItems(chest);
            else
            {
                checkedContainers.add(chestTarget);
                chest.closeInventory(net.minecraftforge.common.util.FakePlayerFactory.getMinecraft((net.minecraft.world.WorldServer)golem.world));
                stageTicks = 0;
                currentStage = Stage.IDLE;
                return;
            }
        }

        chest.markDirty();
        chest.closeInventory(net.minecraftforge.common.util.FakePlayerFactory.getMinecraft((net.minecraft.world.WorldServer)golem.world));

        stageTicks = 0;
        currentStage = Stage.IDLE;
    }

    private void withdrawItems(TileEntityChest chest)
    {
        ItemStack held = golem.getHeldItem(EnumHand.MAIN_HAND);
        if (!held.isEmpty()) return;

        ItemStack collected = ItemStack.EMPTY;

        for (int i = 0; i < chest.getSizeInventory(); i++)
        {
            ItemStack slotStack = chest.getStackInSlot(i);
            if (slotStack.isEmpty()) continue;

            /* If hand is empty and slot isn't, start taking items. */
            if (collected.isEmpty())
            {
                int take = Math.min(16, slotStack.getCount());
                collected = slotStack.splitStack(take);
            }
            else if (ItemStack.areItemsEqual(collected, slotStack))
            {
                int space = 16 - collected.getCount();
                if (space <= 0) break;

                int take = Math.min(space, slotStack.getCount());
                ItemStack taken = slotStack.splitStack(take);
                collected.grow(taken.getCount());
            }

            if (collected.getCount() >= 16) break;
        }

        if (!collected.isEmpty()) golem.setHeldItem(EnumHand.MAIN_HAND, collected);
    }

    private void depositItems(TileEntityChest chest)
    {
        ItemStack held = golem.getHeldItem(EnumHand.MAIN_HAND);
        if (held.isEmpty()) return;
        boolean depositedSomething = false;

        for (int i = 0; i < chest.getSizeInventory(); i++)
        {
            ItemStack slotStack = chest.getStackInSlot(i);

            if (slotStack.isEmpty())
            {
                chest.setInventorySlotContents(i, held.splitStack(Math.min(held.getCount(), held.getMaxStackSize())));
                depositedSomething = true;
            }
            else if (ItemStack.areItemsEqual(slotStack, held) && slotStack.getCount() < slotStack.getMaxStackSize())
            {
                int move = Math.min(slotStack.getMaxStackSize() - slotStack.getCount(), held.getCount());
                slotStack.grow(move);
                held.shrink(move);
                depositedSomething = true;
            }
        }

        //checkedContainers.add(targetPosition);

        if (depositedSomething)
        {
            /* This effectivly sorts the memory based on the most recent interaction, making it preferred to repeat. */
            if (golem.memoryDepositContainers.contains(chestTarget)) golem.memoryDepositContainers.remove(chestTarget);
            //golem.memoryDepositContainers.add(targetPosition);

            /* If all items have been deposited, immediately try to repeat*/
            if (held.isEmpty())
            {
                fullResetSortingTasks();
            }
        }
    }

    /** Use after completing the entire Task via depositing, or when forced to reset. */
    private void fullResetSortingTasks()
    {
        stageTicks = 0;
        currentStage = Stage.IDLE;
        checkedContainers.clear();
    }
}