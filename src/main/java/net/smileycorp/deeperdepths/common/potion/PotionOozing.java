package net.smileycorp.deeperdepths.common.potion;

import net.minecraft.entity.*;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import java.util.Objects;

/**
 * A basic form of Oozing, still requires Particles, better spawning logic, better
 * removal logic, and polish.
 */
@Mod.EventBusSubscriber
public class PotionOozing extends PotionDeeperDepths
{
    /** The entity to spawn. */
    private static Class spawnEntity;
    /** How many entities to spawn. */
    private static int spawnQuantity;
    /** If the entities should be initialized via `onInitialSpawn`. */
    private static boolean doInitialSpawn;

    protected PotionOozing(String nameIn, boolean isBadEffectIn, int liquidColorIn, Class spawnEntityIn, int spawnQuantityIn, boolean doInitialSpawnIn)
    {
        super(nameIn, isBadEffectIn, liquidColorIn, 4);
        spawnEntity = spawnEntityIn;
        spawnQuantity = spawnQuantityIn;
        doInitialSpawn = doInitialSpawnIn;
    }

    /** Uses Forge's `LivingDeathEvent`, as repeatedly scanning if an Entity is dead is silly. */
    @SubscribeEvent
    public static void onOozingDeathEvent(LivingDeathEvent event)
    {
        EntityLivingBase entity = event.getEntityLiving();

        /* Currently hard-coded against Silverfish, make configurable later. */
        if (entity instanceof EntitySlime || !entity.isNonBoss()) entity.removePotionEffect(DeeperDepthsPotions.OOZING);

        if (!entity.isPotionActive(DeeperDepthsPotions.OOZING)) return;

        if(!entity.world.isRemote)
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
        /* Remove after preforming the effect once. */
        entity.removePotionEffect(DeeperDepthsPotions.OOZING);
    }

    @Override
    public void spawnParticles(EntityLivingBase entity)
    { ((WorldServer)entity.world).spawnParticle(EnumParticleTypes.SLIME, entity.posX, entity.posY + (entity.height / 2), entity.posZ, 1, entity.width/3, entity.height/2, entity.width/3, 0.0); }

    /** Slimes are annoyingly privatized, so we forcefully update slimes. */
    public static void setSlimeStats(Entity oozed, int size)
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