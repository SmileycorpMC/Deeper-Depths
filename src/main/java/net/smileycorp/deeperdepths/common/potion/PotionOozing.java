package net.smileycorp.deeperdepths.common.potion;

import net.minecraft.entity.*;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import java.util.Objects;

/**
 * A basic form of Oozing, still requires Particles, better spawning logic, better
 * removal logic, and polish.
 */
public class PotionOozing extends PotionDeeperDepths
{
    /** The entity to spawn. */
    private final Class spawnEntity;
    /** How many entities to spawn. */
    private final int spawnQuantity;
    /** If the entities should be initialized via `onInitialSpawn`. */
    private final boolean doInitialSpawn;

    protected PotionOozing(String nameIn, boolean isBadEffectIn, int liquidColorIn, Class spawnEntityIn, int spawnQuantityIn, boolean doInitialSpawnIn)
    {
        super(nameIn, isBadEffectIn, liquidColorIn, 4);
        spawnEntity = spawnEntityIn;
        spawnQuantity = spawnQuantityIn;
        doInitialSpawn = doInitialSpawnIn;
    }

    @Override
    public void performEffect(EntityLivingBase entity, int amplifier)
    {
        /* Particles will need to be fed through a Packet Handler. */
        //entity.world.spawnParticle(EnumParticleTypes.SLIME, entity.posX, entity.posY, entity.posZ, 0, 0,0, new int[0]);

        /* Currently hard-coded against Slimes, make configurable later. */
        if (entity instanceof EntitySlime || !entity.isNonBoss()) entity.removePotionEffect(DeeperDepthsPotions.OOZING);

        if(entity.isDead && !entity.world.isRemote)
        {
            for (int i = 0; i < spawnQuantity; i++)
            {
                //** Note that spawnEntity == EntitySlime *
                Entity oozed = Objects.requireNonNull(EntityRegistry.getEntry(spawnEntity)).newInstance(entity.world);

                if (oozed instanceof EntityLiving && doInitialSpawn) ((EntityLiving)oozed).onInitialSpawn(entity.world.getDifficultyForLocation(new BlockPos(entity)), (IEntityLivingData)null);

                if (oozed instanceof EntitySlime) setSlimeStats(oozed, 1);

                /* Currently is placed directly at the entity's death spot, implement 5x5x5 spawn area later. */
                oozed.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.getRNG().nextFloat() * 360.0F, 0.0F);

                entity.world.spawnEntity(oozed);
            }
        }
    }

    /** Slimes are annoyingly privatized, so we forcefully update slimes. */
    public void setSlimeStats(Entity oozed, int size)
    {
        EntitySlime slime = ((EntitySlime)oozed);

        NBTTagCompound nbt = new NBTTagCompound();
        slime.writeEntityToNBT(nbt);
        nbt.setInteger("Size", size);
        slime.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue((double)(size * size));
        slime.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue((double)(0.2F + 0.1F * (float)size));
        slime.setHealth(slime.getMaxHealth());
        //this.experienceValue = size;
        slime.readEntityFromNBT(nbt);
    }
}