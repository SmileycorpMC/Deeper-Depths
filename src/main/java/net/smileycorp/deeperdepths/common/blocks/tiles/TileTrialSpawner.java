package net.smileycorp.deeperdepths.common.blocks.tiles;

import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.PositionImpl;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.smileycorp.deeperdepths.common.DeeperDepths;
import net.smileycorp.deeperdepths.common.DeeperDepthsLootTables;
import net.smileycorp.deeperdepths.common.DeeperDepthsSoundEvents;
import net.smileycorp.deeperdepths.common.blocks.enums.EnumTrialSpawnerState;
import net.smileycorp.deeperdepths.common.integration.RaidsIntegration;
import net.smileycorp.deeperdepths.common.potion.DeeperDepthsPotions;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class TileTrialSpawner extends TileEntity implements ITickable {
    
    private EnumTrialSpawnerState state = EnumTrialSpawnerState.INACTIVE;
    private boolean isOminous;
    private final Config config;
    private final Config ominous_config;
    private float required_range = 14;
    private List<UUID> active_players = Lists.newArrayList();
    private int cooldown = 0;
    private List<WeakReference<Entity>> current_mobs = Lists.newArrayList();
    private int spawned_mobs = 0;
    private int ejected_items = 0;
    
    public TileTrialSpawner() {
        config = new Config();
        ominous_config = new Config().setLootTable(DeeperDepthsLootTables.TRIAL_SPAWNER_LOOT_OMINOUS);
    }
    
    @Override
    public void update() {
        if (world == null) return;
        if (world.getDifficulty() == EnumDifficulty.PEACEFUL |! world.getGameRules().getBoolean("doMobSpawning")) return;
        if (world.isRemote) return;
        if (cooldown > 0) {
            cooldown--;
            return;
        }
        if (world.getWorldTime() % 20 == 0 && state != EnumTrialSpawnerState.EJECTING) detectPlayers();
        if (state == EnumTrialSpawnerState.ACTIVE) {
            clearInvalidEntities();
            int total = getTotalMobs();
            if (current_mobs.isEmpty() && spawned_mobs >= total) {
                cooldown = 20;
                setState(EnumTrialSpawnerState.EJECTING);
                playSound(DeeperDepthsSoundEvents.TRIAL_SPAWNER_OPEN_SHUTTER, 1);
            }
            int simultaneous = getSimultaneousMobs();
            if (current_mobs.size() < simultaneous && spawned_mobs < total) {
                Config config = getActiveConfig();
                EntityLivingBase entity = (EntityLivingBase) config.entity.newInstance(world);
                entity.posX = pos.getX() + (world.rand.nextDouble() - world.rand.nextDouble()) * config.spawn_range + 0.5D;
                entity.posY = pos.getY() + world.rand.nextInt(3) - 1;
                entity.posZ = pos.getZ() + (world.rand.nextDouble() - world.rand.nextDouble()) * config.spawn_range + 0.5D;
                entity.readFromNBT(config.spawn_nbt);
                spawned_mobs++;
                current_mobs.add(new WeakReference<>(entity));
                world.spawnEntity(entity);
                world.playSound(null, entity.posX, entity.posY, entity.posZ,
                        DeeperDepthsSoundEvents.TRIAL_SPAWNER_SPAWN_MOB, SoundCategory.BLOCKS, 1, 1);
            }
        }
        if (state == EnumTrialSpawnerState.EJECTING) {
            if (active_players.isEmpty()) {
                setState(EnumTrialSpawnerState.INACTIVE);
                ejected_items = 0;
                playSound(DeeperDepthsSoundEvents.TRIAL_SPAWNER_CLOSE_SHUTTER, 1f);
            }
            else {
                Config config = getActiveConfig();
                if (config.loot_table == null) return;
                List<ItemStack> loot = world.getLootTableManager().getLootTableFromLocation(config.loot_table)
                        .generateLootForPools(config.loot_table_seed == 0 ? new Random() : new Random(config.loot_table_seed),
                                new LootContext.Builder((WorldServer) world).build());
                if (loot.isEmpty()) return;
                BehaviorDefaultDispenseItem.doDispense(world, loot.get(0),2, EnumFacing.UP,
                        new PositionImpl(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5));
                playSound(DeeperDepthsSoundEvents.VAULT_EJECT_ITEM, 0.8f + ejected_items * 0.4f);
                ejected_items++;
                active_players.remove(0);
                cooldown = 20;
            }
        }
    }
    
    private void clearInvalidEntities() {
        for (WeakReference<Entity> ref : current_mobs) {
            if (ref.get() == null) current_mobs.remove(ref);
            else if (!ref.get().isEntityAlive()) current_mobs.remove(ref);
        }
    }
    
    private void detectPlayers() {
        for (EntityPlayer player : world.getPlayers(EntityPlayer.class, this::canActivate)) {
            if (player.isPotionActive(DeeperDepthsPotions.BAD_OMEN)) {
                addTrialOmen(player, player.getActivePotionEffect(DeeperDepthsPotions.BAD_OMEN).getAmplifier());
                player.removePotionEffect(DeeperDepthsPotions.BAD_OMEN);
                if (!isOminous) setOminous(true);
            } else if (Loader.isModLoaded("raids") && RaidsIntegration.hasBadOmen(player)) {
                addTrialOmen(player, RaidsIntegration.getBadOmenLevel(player));
                if (!isOminous) setOminous(true);
            } else if (player.isPotionActive(DeeperDepthsPotions.TRIAL_OMEN) &!isOminous) setOminous(true);
            if (active_players.isEmpty() && state == EnumTrialSpawnerState.INACTIVE) setState(EnumTrialSpawnerState.ACTIVE);
           if (!active_players.contains(player.getUniqueID())) {
               active_players.add(player.getUniqueID());
               world.playSound(null, player.posX, player.posY, player.posZ,
                       DeeperDepthsSoundEvents.TRIAL_SPAWNER_DETECT_PLAYER, SoundCategory.BLOCKS, 1, 1);
           }
        }
    }
    
    private void playSound(SoundEvent event, float pitch) {
        world.playSound(null, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f,
                event, SoundCategory.BLOCKS, 1, pitch);
    }
    
    private int getTotalMobs() {
        return getActiveConfig().total_entities + active_players.size() * getActiveConfig().total_entities_per_player;
    }
    
    private int getSimultaneousMobs() {
        return getActiveConfig().simultaneous_entities + active_players.size() * getActiveConfig().simultaneous_entities_per_player;
    }
    
    public static void addTrialOmen(EntityPlayer player, int bad_omen_amplifier) {
        player.addPotionEffect(new PotionEffect(DeeperDepthsPotions.TRIAL_OMEN, 18000 * (bad_omen_amplifier + 1)));
        player.removePotionEffect(DeeperDepthsPotions.BAD_OMEN);
        if (Loader.isModLoaded("raids")) RaidsIntegration.removeBadOmen(player);
    }
    
    public boolean canActivate(EntityPlayer player) {
        DeeperDepths.info(player.isSpectator() + ", " + player.isCreative() + ", " + player.getDistanceSq(pos));
        return !player.isSpectator() &! player.isCreative() && player.getDistanceSq(pos) <= required_range * required_range;
    }
    
    public void setState(EnumTrialSpawnerState state) {
        this.state = state;
        markDirty();
    }
    
    public EnumTrialSpawnerState getState() {
        return state;
    }
    
    public void setOminous(boolean isOminous) {
        this.isOminous = isOminous;
        if (state != EnumTrialSpawnerState.INACTIVE) reset();
        markDirty();
    }
    
    public Config getConfig() {
        return config;
    }
    
    public Config getOminousConfig() {
        return ominous_config;
    }
    
    public Config getActiveConfig() {
        return isOminous ? ominous_config : config;
    }
    
    private void reset() {
        setState(EnumTrialSpawnerState.INACTIVE);
        active_players.clear();
        cooldown = 0;
        current_mobs.clear();
    }
    
    public boolean isOminous() {
        return isOminous;
    }
    
    @Override
    public void markDirty() {
        IBlockState state = world.getBlockState(pos);
        world.markBlockRangeForRenderUpdate(pos, pos);
        world.notifyBlockUpdate(pos, state, state, 3);
        world.scheduleBlockUpdate(pos, getBlockType(), 0, 0);
        super.markDirty();
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if (nbt.hasKey("state")) state = EnumTrialSpawnerState.values()[nbt.getInteger("state")];
        if (nbt.hasKey("ominous")) isOminous = nbt.getBoolean("ominous");
        if (nbt.hasKey("config")) config.readFromNBT(nbt.getCompoundTag("config"));
        if (nbt.hasKey("ominous_config")) ominous_config.readFromNBT(nbt.getCompoundTag("ominous_config"));
        markDirty();
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("state", state.ordinal());
        nbt.setBoolean("ominous", isOminous);
        if (config != null) nbt.setTag("config", config.writeToNBT());
        if (ominous_config != null) nbt.setTag("ominous_config", ominous_config.writeToNBT());
        return nbt;
    }
    
    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("state", state.ordinal());
        nbt.setBoolean("ominous", isOminous);
        return nbt;
    }
    
    @Override
    public void handleUpdateTag(NBTTagCompound nbt) {
        if (nbt.hasKey("state")) state = EnumTrialSpawnerState.values()[nbt.getInteger("state")];
        if (nbt.hasKey("ominous")) isOminous = nbt.getBoolean("ominous");
    }
    
    @Override
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
    }
    
    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        handleUpdateTag(pkt.getNbtCompound());
        world.markBlockRangeForRenderUpdate(pos, pos);
    }
    
    
    public void setEntityType(EntityEntry entity, @Nullable NBTTagCompound spawn_nbt) {
        config.entity = entity;
        ominous_config.entity = entity;
        if (spawn_nbt != null) {
            config.spawn_nbt = spawn_nbt;
            ominous_config.spawn_nbt = spawn_nbt;
        }
    }
    
    public static class Config {
    
        private int spawn_range = 4, total_entities = 6, simultaneous_entities = 2, total_entities_per_player = 2,
                simultaneous_entities_per_player = 1, ticks_between_spawn = 40;
        private ResourceLocation loot_table = DeeperDepthsLootTables.TRIAL_SPAWNER_LOOT;
        private long loot_table_seed;
        
        public int getSpawnRange() {
            return spawn_range;
        }
        
        public Config setSpawnRange(int spawn_range) {
            this.spawn_range = spawn_range;
            return this;
        }
        
        public int getTotalEntities() {
            return total_entities;
        }
        
        public Config setTotalEntities(int total_entities) {
            this.total_entities = total_entities;
            return this;
        }
        
        public int getSimultaneousEntities() {
            return simultaneous_entities;
        }
        
        public Config setSimultaneousEntities(int simultaneous_entities) {
            this.simultaneous_entities = simultaneous_entities;
            return this;
        }
        
        public int getTotalEntitiesPerPlayer() {
            return total_entities_per_player;
        }
        
        public Config setTotalEntitiesPerPlayer(int total_entities_per_player) {
            this.total_entities_per_player = total_entities_per_player;
            return this;
        }
        
        public int getSimultaneousEntitiesPerPlayer() {
            return simultaneous_entities_per_player;
        }
        
        public Config setSimultaneousEntitiesPerPlayer(int simultaneous_entities_per_player) {
            this.simultaneous_entities_per_player = simultaneous_entities_per_player;
            return this;
        }
        
        public int getTicksBetweenSpawn() {
            return ticks_between_spawn;
        }
        
        public Config setTicksBetweenSpawn(int ticks_between_spawn) {
            this.ticks_between_spawn = ticks_between_spawn;
            return this;
        }
        
        public ResourceLocation getLootTable() {
            return loot_table;
        }
        
        public Config setLootTable(ResourceLocation loot_table) {
            this.loot_table = loot_table;
            return this;
        }
        
        public long getLootTableSeed() {
            return loot_table_seed;
        }
        
        public Config setLootTableSeed(long loot_table_seed) {
            this.loot_table_seed = loot_table_seed;
            return this;
        }
        
        public EntityEntry getEntity() {
            return entity;
        }
        
        public Config setEntity(EntityEntry entity) {
            this.entity = entity;
            return this;
        }
        
        public NBTTagCompound getSpawnNbt() {
            return spawn_nbt;
        }
        
        public Config setSpawnNbt(NBTTagCompound spawn_nbt) {
            this.spawn_nbt = spawn_nbt;
            return this;
        }
        
        private EntityEntry entity = ForgeRegistries.ENTITIES.getValue(new ResourceLocation("minecraft:zombie"));
        private NBTTagCompound spawn_nbt = new NBTTagCompound();
        
        public void readFromNBT(NBTTagCompound nbt) {
            spawn_range = nbt.getInteger("spawn_range");
            total_entities = nbt.getInteger("spawn_range");
            simultaneous_entities = nbt.getInteger("simultaneous_entities ");
            total_entities_per_player = nbt.getInteger("total_entities_per_player");
            simultaneous_entities_per_player = nbt.getInteger("simultaneous_entities_per_player");
            ticks_between_spawn = nbt.getInteger("ticks_between_spawn");
            loot_table = new ResourceLocation(nbt.getString("loot_table"));
            loot_table_seed = nbt.getLong("loot_table_seed");
        }
        
        public NBTTagCompound writeToNBT() {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setInteger("spawn_range", spawn_range);
            nbt.setInteger("total_entities", total_entities);
            nbt.setInteger("simultaneous_entities", simultaneous_entities);
            nbt.setInteger("total_entities_per_player", total_entities_per_player);
            nbt.setInteger("simultaneous_entities_per_player", simultaneous_entities_per_player);
            nbt.setInteger("ticks_between_spawn",  ticks_between_spawn);
            nbt.setLong("loot_table_seed", loot_table_seed);
            return nbt;
        }
        
    }
    
}
