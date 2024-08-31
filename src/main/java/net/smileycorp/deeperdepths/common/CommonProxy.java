package net.smileycorp.deeperdepths.common;

import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.smileycorp.deeperdepths.common.advancements.DeeperDepthsAdvancements;
import net.smileycorp.deeperdepths.common.capabilities.CapabilityWindChargeFall;
import net.smileycorp.deeperdepths.common.entities.DeeperDepthsEntities;
import net.smileycorp.deeperdepths.common.entities.EntityWindCharge;
import net.smileycorp.deeperdepths.common.items.DeeperDepthsItems;
import net.smileycorp.deeperdepths.common.network.NetworkHandler;
import net.smileycorp.deeperdepths.common.network.ParticleMessage;
import net.smileycorp.deeperdepths.common.world.loot.functions.ApplyEnchantments;
import net.smileycorp.deeperdepths.config.BlockConfig;
import net.smileycorp.deeperdepths.config.EntityConfig;
import net.smileycorp.deeperdepths.config.WorldConfig;

public class CommonProxy {
    
    public void preInit(FMLPreInitializationEvent event) {
        EntityConfig.syncConfig(event);
        WorldConfig.syncConfig(event);
        BlockConfig.syncConfig(event);
        MinecraftForge.EVENT_BUS.register(new DeeperDepthsEventHandler());
        DeeperDepthsEntities.registerEntities();
        //Registers Entity Spawns even though I think we'll only have one lmao
        DeeperDepthsEntities.registerEntitySpawns();
        DeeperDepthsLootTables.registerLootTables();
        LootFunctionManager.registerFunction(new ApplyEnchantments.Serializer());
        DeeperDepthsAdvancements.init();
        DeeperDepthsSoundEvents.registerSounds();
    }
    
    public void init(FMLInitializationEvent event)
    {
        NetworkHandler.init();
        CapabilityManager.INSTANCE.register(CapabilityWindChargeFall.ICapabilityWindChargeFall.class, new CapabilityWindChargeFall.Storage(), CapabilityWindChargeFall.WindChargeHorn::new);
    }
    
    public void postInit(FMLPostInitializationEvent event)
    {
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(DeeperDepthsItems.WIND_CHARGE, new BehaviorProjectileDispense()
        {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn)
            {
                NBTTagCompound nbttagcompound = stackIn.getTagCompound();

                EntityWindCharge entitywindcharge = new EntityWindCharge(worldIn, position.getX(), position.getY(), position.getZ());
                entitywindcharge.setBurstPower(nbttagcompound != null && nbttagcompound.hasKey("BurstPower") ? nbttagcompound.getFloat("BurstPower") : 0.9F);
                entitywindcharge.setBurstRange(nbttagcompound != null && nbttagcompound.hasKey("BurstRange") ? nbttagcompound.getFloat("BurstRange") : 2.5F);
                entitywindcharge.setBurstInteractRange(nbttagcompound != null && nbttagcompound.hasKey("BurstInteractRange") ? nbttagcompound.getFloat("BurstInteractRange") : 2.5F);
                entitywindcharge.setPlayerFallReduction(true);

                return entitywindcharge;
            }
        });
    }
    
    public void serverStart(FMLServerStartingEvent event) {
    }


    public void handleAnimationPacket(int entityId, int index) {

    }

    /** Handles spawning of Particles */
    public void spawnParticle(int particleId, World world, double posX, double posY, double posZ, double speedX, double speedY, double speedZ, int... parameters)
    {
        if (world.isRemote)
        { spawnParticle(particleId, posX, posY, posZ, speedX, speedY, speedZ, parameters); }
        else
        { NetworkHandler.network.sendToAllTracking( new ParticleMessage(particleId, posX, posY, posZ, speedX, speedY, speedZ, parameters), new NetworkRegistry.TargetPoint(world.provider.getDimension(), posX, posY, posZ, 0.0D)); }
    }

    /** This exists to be overridden in the ClientProxy! */
    public void spawnParticle(int particleId, double posX, double posY, double posZ, double speedX, double speedY, double speedZ, int... parameters) {}
}
