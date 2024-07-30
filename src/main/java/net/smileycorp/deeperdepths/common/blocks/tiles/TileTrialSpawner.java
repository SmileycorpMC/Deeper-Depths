package net.smileycorp.deeperdepths.common.blocks.tiles;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.PositionImpl;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.smileycorp.atlas.api.recipe.WeightedOutputs;
import net.smileycorp.deeperdepths.common.DeeperDepths;
import net.smileycorp.deeperdepths.common.DeeperDepthsLootTables;
import net.smileycorp.deeperdepths.common.DeeperDepthsSoundEvents;
import net.smileycorp.deeperdepths.common.blocks.enums.EnumTrialSpawnerState;
import net.smileycorp.deeperdepths.common.integration.RaidsIntegration;
import net.smileycorp.deeperdepths.common.potion.DeeperDepthsPotions;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class TileTrialSpawner extends TileEntity implements ITickable {
    
    private EnumTrialSpawnerState state = EnumTrialSpawnerState.INACTIVE;
    private boolean ominous;
    private final Config config;
    private final Config ominous_config;
    private float required_range = 14;
    private List<UUID> active_players = Lists.newArrayList();
    private int cooldown = 0;
    private List<WeakReference<Entity>> current_mobs = Lists.newArrayList();
    private int spawned_mobs = 0;
    private int ejected_items = 0;
    private ResourceLocation loot_table;
    private Entity cached_entity;
    private boolean is_spawning;
    
    public TileTrialSpawner() {
        config = new Config();
        ominous_config = new Config().clearLootTables().addLootTable(DeeperDepthsLootTables.TRIAL_SPAWNER_KEY_OMINOUS, 3)
                .addLootTable(DeeperDepthsLootTables.TRIAL_SPAWNER_LOOT_OMINOUS, 7);
    }
    
    @Override
    public void update() {
        if (world == null) return;
        if (world.isRemote) return;
        if (world.getDifficulty() == EnumDifficulty.PEACEFUL |! world.getGameRules().getBoolean("doMobSpawning")) return;
        if (getActiveConfig().entities.isEmpty()) return;
        if (cooldown > 0) {
            cooldown--;
            return;
        }
        if (state == EnumTrialSpawnerState.INACTIVE) setState(EnumTrialSpawnerState.ACTIVE);
        if (state == EnumTrialSpawnerState.ACTIVE) {
            if (world.getWorldTime() % 20 == 0) detectPlayers();
            if (!is_spawning) return;
            clearInvalidEntities();
            int total = getTotalMobs();
            if (current_mobs.isEmpty() && spawned_mobs >= total) {
                cooldown = 20;
                setState(EnumTrialSpawnerState.EJECTING);
                playSound(DeeperDepthsSoundEvents.TRIAL_SPAWNER_OPEN_SHUTTER, 1);
                loot_table = getActiveConfig().loot_tables.getResult(world.rand);
            }
            int simultaneous = getSimultaneousMobs();
            if (current_mobs.size() < simultaneous && spawned_mobs < total) {
                Config config = getActiveConfig();
                NBTTagCompound nbt = config.entities.getResult(world.rand);
                double x = pos.getX() + (world.rand.nextDouble() - world.rand.nextDouble()) * config.spawn_range + 0.5D;
                double y = pos.getY() + world.rand.nextInt(3) - 1;
                double z = pos.getZ() + (world.rand.nextDouble() - world.rand.nextDouble()) * config.spawn_range + 0.5D;
                Entity entity = AnvilChunkLoader.readWorldEntityPos(nbt, world, x, y, z, false);
                if (!(entity instanceof EntityLiving)) {
                    setState(EnumTrialSpawnerState.INACTIVE);
                    return;
                }
                if (!((EntityLiving) entity).getCanSpawnHere() |! ((EntityLiving) entity).isNotColliding()) return;
                entity.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, world.rand.nextFloat() * 360, 0);
                spawned_mobs++;
                current_mobs.add(new WeakReference<>(entity));
                ((EntityLiving)entity).onInitialSpawn(world.getDifficultyForLocation(entity.getPosition()), null);
                AnvilChunkLoader.spawnEntity(entity, world);
                world.playSound(null, entity.posX, entity.posY, entity.posZ,
                        DeeperDepthsSoundEvents.TRIAL_SPAWNER_SPAWN_MOB, SoundCategory.BLOCKS, 1, 1);
                cooldown = 20;
            }
        }
        if (state == EnumTrialSpawnerState.EJECTING) {
            if (active_players.isEmpty()) {
                setState(EnumTrialSpawnerState.INACTIVE);
                ejected_items = 0;
                playSound(DeeperDepthsSoundEvents.TRIAL_SPAWNER_CLOSE_SHUTTER, 1f);
                cooldown = 36000;
            }
            else {
                Config config = getActiveConfig();
                if (loot_table == null) return;
                List<ItemStack> loot = world.getLootTableManager().getLootTableFromLocation(loot_table)
                        .generateLootForPools(config.loot_table_seed == 0 ? new Random() : new Random(config.loot_table_seed),
                                new LootContext.Builder((WorldServer) world).build());
                if (loot.isEmpty()) {
                    active_players.clear();
                    return;
                }
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
        current_mobs = current_mobs.stream().filter(ref -> ref.get() != null && ref.get().isEntityAlive()).collect(Collectors.toList());
    }
    
    private void detectPlayers() {
        for (EntityPlayer player : world.getPlayers(EntityPlayer.class, this::canActivate)) {
            if (!is_spawning) {
                is_spawning = true;
                markDirty();
            }
            if (player.isPotionActive(DeeperDepthsPotions.BAD_OMEN)) {
                addTrialOmen(player, player.getActivePotionEffect(DeeperDepthsPotions.BAD_OMEN).getAmplifier());
                player.removePotionEffect(DeeperDepthsPotions.BAD_OMEN);
                if (!ominous) setOminous(true);
            } else if (Loader.isModLoaded("raids") && RaidsIntegration.hasBadOmen(player)) {
                addTrialOmen(player, RaidsIntegration.getBadOmenLevel(player));
                if (!ominous) setOminous(true);
            } else if (player.isPotionActive(DeeperDepthsPotions.TRIAL_OMEN) &!ominous) setOminous(true);
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
        return !player.isSpectator() &! player.isCreative() && player.getDistanceSq(pos) <= required_range * required_range;
    }
    
    public void setState(EnumTrialSpawnerState state) {
        this.state = state;
        rebuildCachedEntity();
        markDirty();
    }
    
    public EnumTrialSpawnerState getState() {
        return state;
    }
    
    public boolean isSpawning() {
        return is_spawning;
    }
    
    public void setOminous(boolean isOminous) {
        this.ominous = isOminous;
        if (state != EnumTrialSpawnerState.INACTIVE) reset();
        rebuildCachedEntity();
        markDirty();
    }
    
    public Config getConfig() {
        return config;
    }
    
    public Config getOminousConfig() {
        return ominous_config;
    }
    
    public Config getActiveConfig() {
        return ominous ? ominous_config : config;
    }
    
    private void reset() {
        setState(EnumTrialSpawnerState.INACTIVE);
        active_players.clear();
        cooldown = 0;
        current_mobs.clear();
    }
    
    public boolean isOminous() {
        return ominous;
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
    public void setWorld(World world) {
        super.setWorld(world);
        world.markBlockRangeForRenderUpdate(pos, pos);
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if (nbt.hasKey("state")) state = EnumTrialSpawnerState.values()[nbt.getInteger("state")];
        if (nbt.hasKey("ominous")) ominous = nbt.getBoolean("ominous");
        if (nbt.hasKey("config")) config.readFromNBT(nbt.getCompoundTag("config"));
        if (nbt.hasKey("ominous_config")) ominous_config.readFromNBT(nbt.getCompoundTag("ominous_config"));
        if (nbt.hasKey("loot_table")) loot_table = new ResourceLocation(nbt.getString("loot_table"));
        if (nbt.hasKey("cooldown")) cooldown = nbt.getInteger("cooldown");
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("state", state.ordinal());
        nbt.setBoolean("ominous", ominous);
        if (config != null) nbt.setTag("config", config.writeToNBT());
        if (ominous_config != null) nbt.setTag("ominous_config", ominous_config.writeToNBT());
        if (loot_table != null) nbt.setString("loot_table", loot_table.toString());
        nbt.setInteger("cooldown", cooldown);
        return nbt;
    }
    
    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("state", state.ordinal());
        nbt.setBoolean("ominous", ominous);
        nbt.setBoolean("is_spawning", is_spawning);
        Entity entity = getCachedEntity();
        if (entity != null) nbt.setTag("cached_entity", entity.serializeNBT());
        return nbt;
    }
    
    @Override
    public void handleUpdateTag(NBTTagCompound nbt) {
        if (nbt.hasKey("state")) state = EnumTrialSpawnerState.values()[nbt.getInteger("state")];
        if (nbt.hasKey("ominous")) ominous = nbt.getBoolean("ominous");
        if (nbt.hasKey("is_spawning")) is_spawning = nbt.getBoolean("is_spawning");
        if (nbt.hasKey("cached_entity")) cached_entity = AnvilChunkLoader.readWorldEntity(nbt.getCompoundTag("cached_entity"), world, false);
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
    
    public void modifyConfigs(Consumer<Config> action) {
        action.accept(config);
        action.accept(ominous_config);
        rebuildCachedEntity();
        markDirty();
    }
    
    public Entity getCachedEntity() {
        return cached_entity;
    }
    
    public void setCachedEntity(Entity cached_entity) {
        this.cached_entity = cached_entity;
    }
    
    public void rebuildCachedEntity() {
        NBTTagCompound nbt = getActiveConfig().getEntities().getResult(world.rand);
        if (nbt != null) {
            cached_entity = AnvilChunkLoader.readWorldEntity(nbt, world, false);
            if (cached_entity instanceof EntityLiving) ((EntityLiving) cached_entity).onInitialSpawn(world.getDifficultyForLocation(pos), null);
        }
        
    }
    
    public static class Config {
    
        private int spawn_range = 4, total_entities = 6, simultaneous_entities = 2, total_entities_per_player = 2,
                simultaneous_entities_per_player = 1, ticks_between_spawn = 40;
        private WeightedOutputs<NBTTagCompound> entities = new WeightedOutputs<>(ImmutableMap.of());
        private WeightedOutputs<ResourceLocation> loot_tables = new WeightedOutputs<>(ImmutableMap.of(DeeperDepthsLootTables.TRIAL_SPAWNER_KEY, 1,
                DeeperDepthsLootTables.TRIAL_SPAWNER_LOOT, 1));
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
        
        public WeightedOutputs<ResourceLocation> getLootTables() {
            return loot_tables;
        }
        
        public Config addLootTable(ResourceLocation table, int weight) {
            loot_tables.addEntry(table, weight);
            return this;
        }
        
        public Config clearLootTables() {
            loot_tables.clear();
            return this;
        }
        
        public Config setLootTables(Map<ResourceLocation, Integer> entries) {
            loot_tables = new WeightedOutputs<>(entries);
            return this;
        }
        
        public long getLootTableSeed() {
            return loot_table_seed;
        }
        
        public Config setLootTableSeed(long loot_table_seed) {
            this.loot_table_seed = loot_table_seed;
            return this;
        }
        
        public WeightedOutputs<NBTTagCompound> getEntities() {
            return entities;
        }
        
        public Config addEntity(ResourceLocation entity, int weight) {
            try {
                entities.addEntry(JsonToNBT.getTagFromJson("{id:" + entity +"}"), weight);
            } catch (Exception e) {
                DeeperDepths.error("Error adding entry " + entity, e);
            }
            return this;
        }
        
        public Config addEntity(EntityEntry entity, int weight) {
            try {
                entities.addEntry(JsonToNBT.getTagFromJson("{id:" + entity.getRegistryName() +"}"), weight);
            } catch (Exception e) {
                DeeperDepths.error("Error adding entry " + entity, e);
            }
            return this;
        }
        
        public Config addEntity(NBTTagCompound nbt, int weight) {
            entities.addEntry(nbt, weight);
            return this;
        }
        
        public Config clearEntities() {
            entities.clear();
            return this;
        }
        
        public Config setEntities(Map<NBTTagCompound, Integer> entries) {
            entities = new WeightedOutputs<>(entries);
            return this;
        }
        
        public void readFromNBT(NBTTagCompound nbt) {
            spawn_range = nbt.getInteger("spawn_range");
            total_entities = nbt.getInteger("spawn_range");
            simultaneous_entities = nbt.getInteger("simultaneous_entities ");
            total_entities_per_player = nbt.getInteger("total_entities_per_player");
            simultaneous_entities_per_player = nbt.getInteger("simultaneous_entities_per_player");
            ticks_between_spawn = nbt.getInteger("ticks_between_spawn");
            Map<ResourceLocation, Integer> loot_tables = Maps.newHashMap();
            for (NBTBase entry : nbt.getTagList("loot_tables", 10)) {
                NBTTagCompound compound = (NBTTagCompound) entry;
                loot_tables.put(new ResourceLocation(compound.getString("table")), compound.getInteger("weight"));
            }
            this.loot_tables = new WeightedOutputs<>(loot_tables);
            loot_table_seed = nbt.getLong("loot_table_seed");
            Map<NBTTagCompound, Integer> entities = Maps.newHashMap();
            for (NBTBase entry : nbt.getTagList("entities", 10)) {
                NBTTagCompound compound = (NBTTagCompound) entry;
                entities.put(compound.getCompoundTag("entity"), compound.getInteger("weight"));
            }
            this.entities = new WeightedOutputs<>(entities);
        }
        
        public NBTTagCompound writeToNBT() {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setInteger("spawn_range", spawn_range);
            nbt.setInteger("total_entities", total_entities);
            nbt.setInteger("simultaneous_entities", simultaneous_entities);
            nbt.setInteger("total_entities_per_player", total_entities_per_player);
            nbt.setInteger("simultaneous_entities_per_player", simultaneous_entities_per_player);
            nbt.setInteger("ticks_between_spawn",  ticks_between_spawn);
            NBTTagList loot_tables = new NBTTagList();
            for (Map.Entry<ResourceLocation, Integer> entry : this.loot_tables.getTable()) {
                NBTTagCompound compound = new NBTTagCompound();
                compound.setString("table", entry.getKey().toString());
                compound.setInteger("weight", entry.getValue());
                loot_tables.appendTag(compound);
            }
            nbt.setTag("loot_tables", loot_tables);
            nbt.setLong("loot_table_seed", loot_table_seed);
            NBTTagList entities = new NBTTagList();
            for (Map.Entry<NBTTagCompound, Integer> entry : this.entities.getTable()) {
                NBTTagCompound compound = new NBTTagCompound();
                compound.setTag("entity", entry.getKey());
                compound.setInteger("weight", entry.getValue());
                entities.appendTag(compound);
            }
            nbt.setTag("entities", entities);
            return nbt;
        }
        
    }
    
}
