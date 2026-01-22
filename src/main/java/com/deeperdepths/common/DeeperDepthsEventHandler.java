package com.deeperdepths.common;

import com.deeperdepths.common.blocks.BlockCandle;
import com.deeperdepths.common.blocks.BlockLightningRod;
import com.deeperdepths.common.blocks.ICopperBlock;
import com.deeperdepths.common.blocks.IFluidloggable;
import com.deeperdepths.common.blocks.tiles.TileTrialSpawner;
import com.deeperdepths.common.blocks.tiles.TileVault;
import com.deeperdepths.common.capabilities.CapabilityWindChargeFall;
import com.deeperdepths.common.capabilities.DeathLocation;
import com.deeperdepths.common.entities.EntityBreeze;
import com.deeperdepths.common.entities.EntityWindCharge;
import com.deeperdepths.common.items.DeeperDepthsItems;
import com.deeperdepths.common.items.ItemMace;
import com.deeperdepths.common.network.SyncDeathLocationMessage;
import com.deeperdepths.common.potion.DeeperDepthsPotions;
import com.deeperdepths.common.potion.PotionDeeperDepths;
import com.deeperdepths.config.BlockConfig;
import com.deeperdepths.config.LootTablesConfig;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.smileycorp.atlas.api.config.LootTableEntry;

import java.util.Optional;
import java.util.Random;

public class DeeperDepthsEventHandler {
    
    @SubscribeEvent
    public void potionAdded(PotionEvent.PotionAddedEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        if (entity.world.isRemote) return;
        if (event.getOldPotionEffect() != null) return;
        Potion effect = event.getPotionEffect().getPotion();
        SoundEvent sound = effect == DeeperDepthsPotions.BAD_OMEN ? DeeperDepthsSoundEvents.MOB_EFFECT_BAD_OMEN:
                effect == DeeperDepthsPotions.TRIAL_OMEN ? DeeperDepthsSoundEvents.MOB_EFFECT_TRIAL_OMEN : null;
        if (sound != null) entity.world.playSound(null, entity.posX, entity.posY, entity.posZ, sound, entity.getSoundCategory(), 1.0F, 1.0F);
    }
    
    @SubscribeEvent
    public void blockInteract(PlayerInteractEvent.RightClickBlock event) {
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        if (world == null) return;
        ItemStack stack = event.getItemStack();
        IBlockState state = world.getBlockState(pos);
        if (!(state.getBlock() instanceof ICopperBlock)) return;
        ICopperBlock copper = (ICopperBlock) state.getBlock();
        EntityLivingBase entity = event.getEntityLiving();
        if (copper.interactRequiresSneak() &! entity.isSneaking()) return;
        if (stack.getItem().getToolClasses(stack).contains("axe"))
            copper.scrape(entity, world, stack, state, pos, event.getHand());
        if (BlockConfig.isWax(stack)) copper.wax(entity, world, stack, state, pos, event.getHand());
    }
    
    @SubscribeEvent
    public void entityJoinWorld(EntityJoinWorldEvent event) {
        World world = event.getWorld();
        if (!(event.getEntity() instanceof EntityLightningBolt) || world.isRemote) return;
        Random rand = world.rand;
        BlockPos base = event.getEntity().getPosition().down();
        IBlockState state = world.getBlockState(base);
        if (state.getBlock() instanceof BlockLightningRod) ((BlockLightningRod) state.getBlock()).struck(world, base, state);
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(base);
        //copper stripping
        for (int i = 0; i < 10; i++) {
            pos.setPos(base.add(rand.nextInt(3), rand.nextInt(3), rand.nextInt(3)));
            IBlockState state1 = world.getBlockState(pos);
            if (!(state1.getBlock() instanceof ICopperBlock)) continue;
            ICopperBlock copper = (ICopperBlock) state1.getBlock();
            if (copper.isWaxed(state1)) continue;
            copper.scrape(world, state1, pos);
        }
    }

    @SubscribeEvent
    public void remapItems(RegistryEvent.MissingMappings<Item> event) {
        if (Loader.isModLoaded("raids")) return;
        for (RegistryEvent.MissingMappings.Mapping<Item> mapping : event.getAllMappings()) {
            if (mapping.key.equals(new ResourceLocation("raids:ominous_bottle"))) {
                mapping.remap(DeeperDepthsItems.OMINOUS_BOTTLE);
                return;
            }
        }
    }

    @SubscribeEvent
    public void remapEffects(RegistryEvent.MissingMappings<Potion> event) {
        if (Loader.isModLoaded("raids")) return;
        for (RegistryEvent.MissingMappings.Mapping<Potion> mapping : event.getAllMappings()) {
            if (mapping.key.equals(new ResourceLocation("raids:bad_omen"))) {
                mapping.remap(DeeperDepthsPotions.BAD_OMEN);
                return;
            }
        }
    }
    
    @SubscribeEvent
    public void blockPlaceEvent(BlockEvent.EntityPlaceEvent event) {
        World world = event.getWorld();
        if (world.isRemote) return;
        if (event.getPlacedBlock().getMaterial() != Material.FIRE) return;
        if (event.getPlacedAgainst().getBlock() instanceof BlockCandle) {
            Vec3d vec = event.getEntity().getLookVec();
            BlockPos pos = event.getPos().offset(EnumFacing.getFacingFromVector((float) vec.x, (float) vec.y, (float) vec.z));
            IBlockState state = world.getBlockState(pos);
            if (state.getBlock() instanceof BlockCandle) {
                if (state.getValue(BlockCandle.LIT)) return;
                BlockCandle.light(event.getWorld(), state, pos);
                event.setCanceled(true);
            }
        }
    }

    /** Basic, reusable reflecting code, also handles the sound. */
    private void deflectProjectile(Entity projectile)
    {
        double bounceStrength = -1.0D;
        projectile.motionX *= bounceStrength;
        projectile.motionY *= bounceStrength;
        projectile.motionZ *= bounceStrength;
        projectile.velocityChanged = true;

        projectile.world.playSound(null, projectile.getPosition(), DeeperDepthsSoundEvents.BREEZE_DEFLECT, SoundCategory.HOSTILE, 1, 1);
    }

    /** Events have to be used for Projectile Reflection, as `attackEntityFrom` is called within onImpact for most projectiles, which breaks this behavior! */
    @SubscribeEvent
    public void reflectArrowEvent(ProjectileImpactEvent.Arrow event)
    {
        final EntityArrow projectile = event.getArrow();

        if (projectile.getEntityWorld().isRemote) return;
        Entity entity = event.getRayTraceResult().entityHit;

        if (event.getEntity() != null && entity instanceof EntityBreeze)
        {
            deflectProjectile(projectile);
            projectile.shootingEntity = entity;

            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void reflectFireballEvent(ProjectileImpactEvent.Fireball event)
    {
        final EntityFireball projectile = event.getFireball();

        if (projectile.getEntityWorld().isRemote) return;
        Entity entity = event.getRayTraceResult().entityHit;

        if (event.getEntity() != null && entity instanceof EntityBreeze)
        {
            deflectProjectile(projectile);
            double bounceStrength = -1.0D;
            projectile.accelerationX *= bounceStrength;
            projectile.accelerationY *= bounceStrength;
            projectile.accelerationZ *= bounceStrength;
            projectile.shootingEntity = (EntityLivingBase) entity;

            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void reflectThrowableEvent(ProjectileImpactEvent.Throwable event)
    {
        final EntityThrowable projectile = event.getThrowable();
        if (projectile instanceof EntityWindCharge) return;

        if (projectile.getEntityWorld().isRemote) return;
        Entity entity = event.getRayTraceResult().entityHit;

        if (event.getEntity() != null && entity instanceof EntityBreeze)
        {
            deflectProjectile(projectile);
            //projectile.thrower = entityBlocking;
            event.setCanceled(true);
        }
    }

    /** Grabs, and removes the vanilla particles from our Potion Effects.
     *  This is used because setting the Potion Effect to not display particles in the PotionType registry causes no color to be set, due to PotionUtils `getPotionColorFromEffectList`!
     * */
    @SubscribeEvent
    public void removePotionParticles(PotionEvent.PotionAddedEvent event)
    {
        Potion potion = event.getPotionEffect().getPotion();

        if (potion == null || event.getEntityLiving() == null) return;

        if (potion instanceof PotionDeeperDepths && event.getPotionEffect().doesShowParticles())
        {
            event.getEntityLiving().removePotionEffect(potion);
            event.getEntityLiving().addPotionEffect(new PotionEffect(potion, event.getPotionEffect().getDuration(), event.getPotionEffect().getAmplifier(), event.getPotionEffect().getIsAmbient(), false));
        }
    }

    @SubscribeEvent
    public void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof EntityPlayer) {
            event.addCapability(CapabilityWindChargeFall.ID, new CapabilityWindChargeFall.Provider(new CapabilityWindChargeFall.WindChargeHorn(), CapabilityWindChargeFall.WINDBURSTHEIGHT_CAP, null));
            event.addCapability(Constants.loc("death_location"), new DeathLocation.Provider());
        }
    }

    @SubscribeEvent
    public void onFall(LivingFallEvent event)
    {
        if (event.getEntity() == null) return;

        if (event.getEntity().hasCapability(CapabilityWindChargeFall.WINDBURSTHEIGHT_CAP, null))
        {
            CapabilityWindChargeFall.ICapabilityWindChargeFall capWindCharge = event.getEntity().getCapability(CapabilityWindChargeFall.WINDBURSTHEIGHT_CAP, null);

            if (capWindCharge.getUsedWindCharge())
            {
                boolean getTime = event.getEntity().ticksExisted <= capWindCharge.getWindBurstTime();

                //System.out.print("Height is: " + getTime + "Entity is: " + event.getEntity().ticksExisted);

                if (event.getDistance() > 3 && getTime)
                {
                    if (event.getEntity().posY < capWindCharge.getWindBurstHeight())
                    {
                        event.setDistance((float) (capWindCharge.getWindBurstHeight() - event.getEntity().posY) + 2);
                        capWindCharge.setUsedWindCharge(false);
                        capWindCharge.setWindBurstHeight(0.0);
                    }
                    else
                    {
                        capWindCharge.setUsedWindCharge(false);
                        capWindCharge.setWindBurstHeight(0.0);
                        event.setDistance(0);
                    }
                }
                else
                {
                    capWindCharge.setUsedWindCharge(false);
                    capWindCharge.setWindBurstHeight(0.0);
                }
            }
        }
    }
    
    @SubscribeEvent
    public void startTrackingChunk(ChunkWatchEvent.Watch event) {
        Chunk chunk = event.getChunkInstance();
        if (chunk == null) return;
        //visually update vaults and spawners
        for (TileEntity te : chunk.getTileEntityMap().values())
            if (te instanceof TileTrialSpawner || te instanceof TileVault) te.markDirty();
    }

    //makes the mace damage advancement work hopefully
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void livingHurt(LivingAttackEvent event) {
        DamageSource source = event.getSource();
        if (source.getImmediateSource() instanceof EntityPlayerMP)
            if (((EntityPlayerMP) source.getImmediateSource()).getHeldItemMainhand().getItem() instanceof ItemMace)
                if (ItemMace.CACHED_HEALTH == 0) ItemMace.CACHED_HEALTH = event.getEntityLiving().getHealth();
    }

    //extra jank but I wanna make sure the cache gets cleared if the damage is cancelled
    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public void livingHurtEnd(LivingAttackEvent event) {
        if (!event.isCanceled()) return;
        DamageSource source = event.getSource();
        if (source.getImmediateSource() instanceof EntityPlayerMP) {
            if (((EntityPlayerMP) source.getImmediateSource()).getHeldItemMainhand().getItem() instanceof ItemMace)
                if (ItemMace.CACHED_HEALTH != 0) ItemMace.CACHED_HEALTH = 0;
        }
    }

    //yeah, now this is gaming
    @SubscribeEvent
    public void addLoot(LootTableLoadEvent event) {
        for (LootTableEntry entry : LootTablesConfig.ENTRIES) if (entry.canApply(event.getName())) entry.addEntry(event.getTable());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void fillBucket(FillBucketEvent event) {
        World world = event.getWorld();
        RayTraceResult ray = event.getTarget();
        if (ray == null) return;
        if (ray.typeOfHit != RayTraceResult.Type.BLOCK) return;
        BlockPos pos = ray.getBlockPos();
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (!(block instanceof IFluidloggable)) return;
        ItemStack bucket = event.getEmptyBucket();
        if (!bucket.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) return;
        IFluidloggable loggableBlock = (IFluidloggable) block;
        IFluidHandlerItem cap = bucket.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
        EntityPlayer player = event.getEntityPlayer();
        FluidStack fluidStack = FluidUtil.getFluidContained(bucket);
        if (loggableBlock.isFluidLogged(world, pos, state)) {
            if (fluidStack != null) return;
            Optional<Fluid> optional = loggableBlock.getContainedFluid(world, pos, state);
            if (!optional.isPresent()) return;
            Fluid fluid = optional.get();
            fluidStack = new FluidStack(fluid, 1000);
            if (cap.fill(fluidStack, false) < 1000) return;
            player.playSound(fluid.getFillSound(fluidStack), 1, 1);
            loggableBlock.empty(world, pos, state);
            if (!player.isCreative()) cap.fill(fluidStack, true);
        } else  {
            if (fluidStack == null) return;
            if (fluidStack.amount < 1000 |! loggableBlock.canContainFluid(world, pos, state, fluidStack.getFluid())) return;
            FluidStack drained = cap.drain(fluidStack, false);
            if (drained == null) return;
            if (drained.amount < 1000 || drained.getFluid() != fluidStack.getFluid()) return;
            if (world.provider.doesWaterVaporize()) {
                world.playSound(player, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5f, 2.6f + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8f);
                for (int k = 0; k < 8; ++k)
                    world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, pos.getX() + Math.random(), pos.getY() + Math.random(), pos.getZ() + Math.random(), 0, 0, 0);
            } else {
                world.playSound(player, pos, fluidStack.getFluid().getEmptySound(fluidStack), SoundCategory.BLOCKS, 1, 1);
                loggableBlock.fillWithFluid(world, pos, state, fluidStack.getFluid());
            }
            if (!player.isCreative()) cap.drain(fluidStack, true);
        }
        player.addStat(StatList.getObjectUseStats(bucket.getItem()));
        ItemStack container = cap.getContainer().copy();
        event.setFilledBucket(container);
        event.setResult(Event.Result.ALLOW);
    }
    
    //used for sculk sensors, could be useful for other stuff later
    @SubscribeEvent
    public void worldLoad(WorldEvent.Load event) {
        World world = event.getWorld();
        if (!(world instanceof WorldServer)) return;
        world.addEventListener(new DeeperDepthsWorldListener((WorldServer) world));
    }

    @SubscribeEvent
    public void clone(PlayerEvent.Clone event) {
        EntityPlayer original = event.getOriginal();
        EntityPlayer player = event.getEntityPlayer();
        if (!player.hasCapability(DeathLocation.CAPABILITY, null)) return;
        if (event.isWasDeath()) {
            System.out.println(original.getEntityId() + ", " + player.getEntityId());
            player.getCapability(DeathLocation.CAPABILITY, null)
                    .setDeathInformation(original.world.provider.getDimension(), original.getPosition());
            SyncDeathLocationMessage.send(original, original.world.provider.getDimension(), original.getPosition());
            SyncDeathLocationMessage.sendTracking(original, original.world.provider.getDimension(), original.getPosition());
            return;
        }
        if (!original.hasCapability(DeathLocation.CAPABILITY, null)) return;
        Tuple<Integer, BlockPos> data = original.getCapability(DeathLocation.CAPABILITY, null).getDeathInformation();
        if (data == null) return;
        player.getCapability(DeathLocation.CAPABILITY, null).setDeathInformation(data.getFirst(), data.getSecond());
        SyncDeathLocationMessage.send(original, data.getFirst(), data.getSecond());
        SyncDeathLocationMessage.sendTracking(original, data.getFirst(), data.getSecond());
    }

    @SubscribeEvent
    public void logIn(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent event) {
        EntityPlayer player = event.player;
        if (player == null) return;
        if (!(player instanceof EntityPlayerMP)) return;
        if (!player.hasCapability(DeathLocation.CAPABILITY, null)) return;
        Tuple<Integer, BlockPos> data = player.getCapability(DeathLocation.CAPABILITY, null).getDeathInformation();
        if (data == null) return;
        SyncDeathLocationMessage.send(player, data.getFirst(), data.getSecond());
        SyncDeathLocationMessage.sendTracking(player, data.getFirst(), data.getSecond());
    }

    @SubscribeEvent
    public void startTrackingEntity(PlayerEvent.StartTracking event) {
        Entity entity = event.getTarget();
        if (!entity.hasCapability(DeathLocation.CAPABILITY, null)) return;
        Tuple<Integer, BlockPos> data = entity.getCapability(DeathLocation.CAPABILITY, null).getDeathInformation();
        if (data == null) return;
        SyncDeathLocationMessage.send(event.getEntityPlayer(), data.getFirst(), data.getSecond());
    }

}
