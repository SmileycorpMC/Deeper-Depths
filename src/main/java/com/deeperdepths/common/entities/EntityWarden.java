package com.deeperdepths.common.entities;

import com.deeperdepths.common.DeeperDepthsLootTables;
import com.deeperdepths.common.DeeperDepthsSoundEvents;
import com.deeperdepths.common.capabilities.VibrationHandler;
import com.deeperdepths.common.entities.ai.EntityAIWardenRoar;
import com.deeperdepths.common.entities.ai.EntityAIWardenSniff;
import com.deeperdepths.common.potion.DeeperDepthsPotions;
import com.deeperdepths.config.EntityConfig;
import com.deeperdepths.config.VibrationsConfig;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.UUID;

public class EntityWarden extends EntityMob implements VibrationHandler.Listener {

    private static final DataParameter<Integer> CLIENT_ANGER = EntityDataManager.createKey(EntityWarden.class, DataSerializers.VARINT);
    private static final DataParameter<Byte> STATE = EntityDataManager.createKey(EntityWarden.class, DataSerializers.BYTE);
    private static final DataParameter<Integer> STATE_TIMER = EntityDataManager.createKey(EntityWarden.class, DataSerializers.VARINT);

    private int vibrationCooldown = 40;
    private final Object2IntMap<UUID> angerLevels = new Object2IntOpenHashMap<>();
    private final VibrationHandler.Data vibrationData = new VibrationHandler.Data(this);

    public EntityWarden(World world) {
        super(world);
        setSize(0.9f, 2.9f);
        setPathPriority(PathNodeType.RAIL, 0);
        setPathPriority(PathNodeType.DAMAGE_FIRE, 0);
        setPathPriority(PathNodeType.DANGER_FIRE, 0);
        setPathPriority(PathNodeType.DANGER_CACTUS, 0);
        setPathPriority(PathNodeType.DAMAGE_CACTUS, 0);
        setPathPriority(PathNodeType.DAMAGE_OTHER, 0);
        setPathPriority(PathNodeType.LAVA, 0);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(CLIENT_ANGER, 0);
        dataManager.register(STATE, (byte) 0);
        dataManager.register(STATE_TIMER, 0);
    }

    @Override
    protected void initEntityAI() {
        tasks.addTask(6, new EntityAIWardenRoar(this));
        tasks.addTask(7, new EntityAIWardenSniff(this));
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        if (world instanceof WorldServer) VibrationHandler.getInstance((WorldServer) world).add(this);
    }

    @Override
    public void onRemovedFromWorld() {
        super.onRemovedFromWorld();
        if (world instanceof WorldServer) VibrationHandler.getInstance((WorldServer) world).remove(this);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        EntityConfig.warden.applyAttributes(this);
    }

    @Override
    public void onUpdate() {
        if (world.isRemote) return;
        if (world instanceof WorldServer) getVibrationData().tick((WorldServer) world);
        int stateTimer = getStateTimer();
        if (stateTimer == 0 && getState() != EnumState.IDLE) setState(EnumState.IDLE, 0);
        else dataManager.set(STATE_TIMER, stateTimer - 1);
        if (vibrationCooldown > 0) vibrationCooldown--;
        if (ticksExisted + getEntityId() % EntityConfig.wardenDarknessFrequency == 0) {
            PotionEffect effect = new PotionEffect(DeeperDepthsPotions.DARKNESS, EntityConfig.wardenDarknessLength, 0, false, false);
            for (EntityPlayer player : world.getPlayers(EntityPlayer.class, player -> player.getDistanceSq(this)
                    < EntityConfig.wardenDarknessRange * EntityConfig.wardenDarknessRange)) player.addPotionEffect(effect);
        }
        if (ticksExisted % 20 == 0) {
            ObjectIterator<Object2IntMap.Entry<UUID>> iterator = angerLevels.object2IntEntrySet().iterator();
            while (iterator.hasNext()) {
                Object2IntMap.Entry<UUID> entry = iterator.next();
                int anger = entry.getIntValue();
                if (anger <= 1) iterator.remove();
                else entry.setValue(anger - 1);
            }
            dataManager.set(CLIENT_ANGER, getAnger());
        }
    }

    //wardens disable shields
    @Override
    public boolean attackEntityAsMob(Entity entity) {
        boolean damaged = super.attackEntityAsMob(entity);
        if (damaged && entity instanceof EntityLivingBase) {
            EntityLivingBase living = (EntityLivingBase) entity;
            ItemStack stack = ((EntityLivingBase) entity).getActiveItemStack();
            if (!stack.getItem().isShield(stack, living)) return true;
            if (living instanceof EntityPlayer) ((EntityPlayer) living).disableShield(true);
            else living.resetActiveHand();
        }
        return damaged;
    }

    //wardens hear in their eyes
    @Override
    public Vec3d getListenerPos() {
        return new Vec3d(posX, posY + getEyeHeight(), posZ);
    }

    @Override
    public int getRange() {
        return VibrationsConfig.wardenDetectionRange;
    }

    @Override
    public boolean canHear(@Nullable Entity entity, SoundEvent sound, double x, double y, double z) {
        return !isAIDisabled() && isEntityAlive() && vibrationCooldown == 0 && (!(entity instanceof EntityLivingBase) || canTarget(entity));
    }

    @Override
    public void reactToSound(@Nullable Entity entity, SoundEvent sound, double x, double y, double z) {
        if (!isEntityAlive()) return;
        playSound(DeeperDepthsSoundEvents.WARDEN_TENDRIL_CLICKS, 5, getSoundPitch());
        if (vibrationCooldown == 0) vibrationCooldown = EntityConfig.wardenVibrationCooldown;
    }

    @Override
    public VibrationHandler.Data getVibrationData() {
        return vibrationData;
    }

    public EnumAnger getAngerLevel() {
        return EnumAnger.get(getAnger());
    }
    
    public int getAnger() {
        return world.isRemote ? dataManager.get(CLIENT_ANGER) : getAnger(getAttackTarget());
    }

    public int getAnger(@Nullable EntityLivingBase entity) {
        return entity == null ? 0 : angerLevels.getOrDefault(entity.getUniqueID(), 0);
    }

    @Override
    public boolean isEntityInvulnerable(DamageSource source) {
        return source.isUnblockable() |! getState().isInvunerable();
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return getAngerLevel().idleSound;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return DeeperDepthsSoundEvents.WARDEN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return DeeperDepthsSoundEvents.WARDEN_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block block) {
        playSound(DeeperDepthsSoundEvents.WARDEN_STEP, 10, 1);
    }

    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return DeeperDepthsLootTables.WARDEN_DROPS;
    }

    @Override
    protected float getSoundVolume() {
        return 4;
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    public EnumState getState() {
        return EnumState.get(dataManager.get(STATE));
    }

    public void setState(EnumState state, int ticks) {
        dataManager.set(STATE, (byte) state.ordinal());
        dataManager.set(STATE_TIMER, ticks);
    }

    public int getStateTimer() {
        return dataManager.get(STATE_TIMER);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("vibration_cooldown", vibrationCooldown);
        NBTTagList angerLevels = new NBTTagList();
        for (Map.Entry<UUID, Integer> entry : this.angerLevels.entrySet()) {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setTag("uuid", NBTUtil.createUUIDTag(entry.getKey()));
            nbt.setInteger("anger", entry.getValue());
            angerLevels.appendTag(nbt);
        }
        compound.setTag("anger_levels", angerLevels);
        compound.setByte("state", dataManager.get(STATE));
        compound.setInteger("state_timer", getStateTimer());
        compound.setTag("vibration_data", vibrationData.writeToNBT());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        vibrationCooldown = compound.getInteger("vibration_cooldown");
        if (compound.hasKey("anger_levels", 9)) {
            angerLevels.clear();
            NBTTagList angerLevels = compound.getTagList("anger_levels", 10);
            for (NBTBase nbtBase : angerLevels) try {
                NBTTagCompound nbt = (NBTTagCompound) nbtBase;
                this.angerLevels.put(NBTUtil.getUUIDFromTag(nbt.getCompoundTag("uuid")), nbt.getInteger("anger"));
            } catch (Exception e) {}
        }
        dataManager.set(STATE, compound.getByte("state"));
        dataManager.set(STATE_TIMER, compound.getInteger("state_timer"));
        vibrationData.readFromNBT(compound.getTagList("vibration_data", 10));
    }

    public boolean canTarget(@Nullable Entity entity) {
        if (entity instanceof EntityWarden) return false;
        if (!(entity instanceof EntityLivingBase)) return false;
        if (!(entity instanceof EntityPlayer)) return true;
        EntityPlayer player = (EntityPlayer) entity;
        return !(player.isSpectator() || player.isCreative());
    }

    public void increaseAnger(EntityLivingBase entity, int angerIncrease) {
        UUID uuid = entity.getUniqueID();
        int anger = MathHelper.clamp(angerLevels.getOrDefault(uuid, 0) + angerIncrease, 0, 150);
        if (anger == 0) angerLevels.remove(uuid);
        else angerLevels.put(uuid, anger);
    }

    public enum EnumAnger {

        CALM(0, DeeperDepthsSoundEvents.WARDEN_AMBIENT, DeeperDepthsSoundEvents.WARDEN_LISTENING),
        AGITATED(0, DeeperDepthsSoundEvents.WARDEN_AMBIENT, DeeperDepthsSoundEvents.WARDEN_LISTENING),
        ANGRY(0, DeeperDepthsSoundEvents.WARDEN_AMBIENT, DeeperDepthsSoundEvents.WARDEN_LISTENING);

        private final int minimumAnger;
        private final SoundEvent idleSound, listeningSound;

        EnumAnger(int minimumAnger, SoundEvent idleSound, SoundEvent listeningSound) {
            this.minimumAnger = minimumAnger;
            this.idleSound = idleSound;
            this.listeningSound = listeningSound;
        }

        public int getMinimumAnger() {
            return minimumAnger;
        }

        public SoundEvent getAmbientSound() {
            return idleSound;
        }

        public SoundEvent getListeningSound() {
            return listeningSound;
        }

        public static EnumAnger get(int anger) {
            int length = values().length;
            for (int i = 1; i <= length; i++) {
                EnumAnger value = values()[length - i];
                if (value.minimumAnger <= anger) return value;
            }
            return CALM;
        }

    }

    public enum EnumState {
        IDLE,
        EMERGING,
        BURROWING,
        ROARING,
        SNIFFING;

        public static EnumState get(byte id) {
            return values()[MathHelper.clamp(id, 0, 4)];
        }

        public boolean isInvunerable() {
            return this == EMERGING;
        }

    }

}
