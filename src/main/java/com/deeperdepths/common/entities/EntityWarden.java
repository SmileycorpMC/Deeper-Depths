package com.deeperdepths.common.entities;

import com.deeperdepths.common.DeeperDepthsLootTables;
import com.deeperdepths.common.DeeperDepthsSoundEvents;
import com.deeperdepths.common.capabilities.VibrationHandler;
import com.deeperdepths.common.entities.ai.*;
import com.deeperdepths.common.potion.DeeperDepthsPotions;
import com.deeperdepths.config.EntityConfig;
import com.deeperdepths.config.VibrationsConfig;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIWander;
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
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.Map;
import java.util.UUID;

public class EntityWarden extends EntityMob implements VibrationHandler.Listener {

    private static final DataParameter<Integer> CLIENT_ANGER = EntityDataManager.createKey(EntityWarden.class, DataSerializers.VARINT);
    private static final DataParameter<Byte> STATE = EntityDataManager.createKey(EntityWarden.class, DataSerializers.BYTE);
    private static final DataParameter<Integer> STATE_TIMER = EntityDataManager.createKey(EntityWarden.class, DataSerializers.VARINT);

    private int vibrationCooldown = 40;
    private final Object2IntMap<UUID> angerLevels = new Object2IntOpenHashMap<>();
    private final VibrationHandler.Data vibrationData = new VibrationHandler.Data(this);
    private Vec3d disturbance = null;
    private int sonicBoomCooldown = 40;
    private int burrowingCooldown = 1200;

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
        stepHeight = 1;
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
        tasks.addTask(0, new EntityAIWardenEmerge(this));
        tasks.addTask(0, new EntityAIWardenBurrow(this));
        tasks.addTask(1, new EntityAIWardenSonicBoom(this));
        tasks.addTask(2, new EntityAIAttackMelee(this, 1, true));
        tasks.addTask(3, new EntityAIWardenRoar(this));
        tasks.addTask(4, new EntityAIWardenSniff(this));
        tasks.addTask(6, new EntityAIWardenInvestigateDisturbance(this));
        tasks.addTask(7, new EntityAIWander(this, 0.5, 20));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        EntityConfig.warden.applyAttributes(this);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (world.isRemote) clientTick();
        else serverTick();
    }

    private void serverTick() {
        State state = getState();
        int stateTimer = getStateTimer();
        getVibrationData().tick((WorldServer) world);
        if (state != State.IDLE) {
            if (stateTimer == 0) setState(State.IDLE);
            else dataManager.set(STATE_TIMER, stateTimer - 1);
        }
        if (vibrationCooldown > 0) vibrationCooldown--;
        if (getAttackTarget() != null && sonicBoomCooldown > 0) sonicBoomCooldown--;
        if (getAngerLevel() == Anger.CALM &! getState().isDigging() &! isNoDespawnRequired() && burrowingCooldown > 0) burrowingCooldown--;
        if (ticksExisted + getEntityId() % EntityConfig.wardenDarknessFrequency == 0) {
            PotionEffect effect = new PotionEffect(DeeperDepthsPotions.DARKNESS, EntityConfig.wardenDarknessLength, 0, false, false);
            for (EntityPlayer player : world.getPlayers(EntityPlayer.class, player -> player.getDistanceSq(this)
                    < EntityConfig.wardenDarknessRange * EntityConfig.wardenDarknessRange)) player.addPotionEffect(effect);
        }
        if (ticksExisted % 20 == 0) {
            ObjectIterator<Object2IntMap.Entry<UUID>> iterator = angerLevels.object2IntEntrySet().iterator();
            while (iterator.hasNext()) {
                Object2IntMap.Entry<UUID> entry = iterator.next();
                if (!canTarget(((WorldServer)world).getEntityFromUuid(entry.getKey()))) {
                    iterator.remove();
                    return;
                }
                int anger = entry.getIntValue();
                if (anger <= 1) iterator.remove();
                else entry.setValue(anger - 1);
            }
            dataManager.set(CLIENT_ANGER, getAnger());
        }
    }

    private void clientTick() {
        //wiki states 0 anger is 30bpm (40 ticks between beats) and anger >= 80 is 120bpm (10 ticks between beats)
        //plugging it into a graphing calculator gives the function f(x) = 40 - 0.375x
        //0.375 * 80 = 30 gives the linear rate of change we need to match the vanilla  heartbeat
        if (ticksExisted % (40 - MathHelper.clamp((float)getAnger() / (float) Anger.ANGRY.getMinimumAnger(), 0, 1) * 30f) == 0) {
            world.playSound(posX, posY, posZ, DeeperDepthsSoundEvents.WARDEN_HEARTBEAT, getSoundCategory(), 5, getSoundPitch(), false);
            //sound played clientside, so we can sync the heartbeat animation to the sound here
        }
        if (getState().spawnParticles(getStateTimer())) {
            IBlockState state = world.getBlockState(getPosition().down());
            if (state.getRenderType() == EnumBlockRenderType.INVISIBLE) return;
            for (int i = 0; i < 30; i++) world.spawnParticle(EnumParticleTypes.BLOCK_DUST, posX + (rand.nextFloat() - 0.5) * 1.4, posY,
                    posZ + (rand.nextFloat() - 0.5) * 1.4, 0, 0, 0, Block.getStateId(state));
        }
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
        boolean canHear = !isAIDisabled() && isEntityAlive() &! getState().isDigging() && vibrationCooldown == 0 &&
                (!(entity instanceof EntityLivingBase) || canTarget(entity));
        if (canHear) vibrationCooldown = EntityConfig.wardenVibrationCooldown;
        return canHear;
    }

    @Override
    public void reactToSound(@Nullable Entity entity, SoundEvent sound, double x, double y, double z) {
        if (!isEntityAlive() || getState().isDigging() || (entity != null &! canTarget(entity))) return;
        playSound(DeeperDepthsSoundEvents.WARDEN_TENDRIL_CLICKS, 5, getSoundPitch());
        resetBurrowingCooldown();
        increaseAnger((EntityLivingBase) entity, 35);
        EntityLivingBase angerTarget = getAngerTarget();
        if (angerTarget == entity  || (entity instanceof EntityPlayer &! (angerTarget instanceof EntityPlayer)))
            setDisturbance(new Vec3d(x, y, z));
    }

    @Override
    public VibrationHandler.Data getVibrationData() {
        return vibrationData;
    }

    public Anger getAngerLevel() {
        return Anger.get(getAnger());
    }
    
    public int getAnger() {
        return world.isRemote ? dataManager.get(CLIENT_ANGER) : getAnger(getAngerTarget());
    }

    public int getAnger(@Nullable EntityLivingBase entity) {
        return entity == null ? 0 : angerLevels.getOrDefault(entity.getUniqueID(), 0);
    }

    public EntityLivingBase getAngerTarget() {
        return world.isRemote ? null : angerLevels.object2IntEntrySet().stream().max(Comparator.comparing(Object2IntMap.Entry::getIntValue))
                .map(uuidEntry -> (EntityLivingBase) ((WorldServer) world).getEntityFromUuid(uuidEntry.getKey())).orElse(null);
    }

    public State getState() {
        return State.get(dataManager.get(STATE));
    }

    public void setState(State state) {
        dataManager.set(STATE, (byte) state.ordinal());
        dataManager.set(STATE_TIMER, state.getLength());
    }

    public int getStateTimer() {
        return dataManager.get(STATE_TIMER);
    }

    public int getSonicBoomCooldown() {
        return sonicBoomCooldown;
    }

    public void resetSonicBoomCooldown() {
        sonicBoomCooldown = 40;
    }

    public int getBurrowingCooldown() {
        return burrowingCooldown;
    }

    public void resetBurrowingCooldown() {
        burrowingCooldown = 1200;
    }

    public void increaseAnger(EntityLivingBase entity, int angerIncrease) {
        if (!canTarget(entity) || isAIDisabled()) {
            angerLevels.remove(entity);
            return;
        }
        UUID uuid = entity.getUniqueID();
        int anger = MathHelper.clamp(angerLevels.getOrDefault(uuid, 0) + angerIncrease, 0, 150);
        if (anger == 0) angerLevels.remove(uuid);
        else angerLevels.put(uuid, anger);
        Anger angerLevel = getAngerLevel();
        if (angerLevel != Anger.CALM) resetBurrowingCooldown();
        if (getState() == State.ROARING) return;
        playSound(angerLevel.getListeningSound(), 10, getSoundPitch());
    }

    public Vec3d getDisturbance() {
        return disturbance;
    }

    public void setDisturbance(Vec3d disturbance) {
        this.disturbance = disturbance;
    }

    //wardens disable shields
    @Override
    public boolean attackEntityAsMob(Entity entity) {
        boolean damaged = super.attackEntityAsMob(entity);
        if (damaged && entity instanceof EntityLivingBase) {
            resetSonicBoomCooldown();
            playSound(DeeperDepthsSoundEvents.WARDEN_ATTACK_IMPACT, 10, getSoundPitch());
            EntityLivingBase living = (EntityLivingBase) entity;
            if (!living.isEntityAlive()) increaseAnger(living, -150);
            ItemStack stack = ((EntityLivingBase) entity).getActiveItemStack();
            if (!stack.getItem().isShield(stack, living)) return true;
            if (living instanceof EntityPlayer) ((EntityPlayer) living).disableShield(true);
            else living.resetActiveHand();
        }
        return damaged;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        boolean damaged = super.attackEntityFrom(source, amount);
        if (damaged) {
            Entity attacker = source.getTrueSource();
            if (attacker instanceof EntityLivingBase && attacker.getDistanceSq(this) <= 900)
                increaseAnger((EntityLivingBase) attacker, 100);
        }
        return damaged;
    }

    @Override
    protected void collideWithEntity(Entity entity) {
        super.collideWithEntity(entity);
        if (!canTarget(entity)) return;
        increaseAnger((EntityLivingBase) entity, 100);
    }

    @Override
    public boolean isEntityInvulnerable(DamageSource source) {
        return (getState().isDigging() &! source.isUnblockable()) || super.isEntityInvulnerable(source);
    }

    @Nullable
    @Override
    public EntityLivingBase getAttackTarget() {
        EntityLivingBase target = super.getAttackTarget();
        if (!canTarget(target)) {
            setAttackTarget(null);
            return null;
        }
        return target;
    }

    @Override
    public void setAttackTarget(@Nullable EntityLivingBase entity) {
        super.setAttackTarget(canTarget(entity) ? entity : null);
        resetSonicBoomCooldown();
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return getState() == State.ROARING ? null : getAngerLevel().getAmbientSound();
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

    public static boolean canTarget(@Nullable Entity entity) {
        if (entity == null) return false;
        if (entity instanceof EntityWarden) return false;
        if (!(entity instanceof EntityLivingBase)) return false;
        if (!entity.isEntityAlive() |! entity.isAddedToWorld()) return false;
        if (!(entity instanceof EntityPlayer)) return true;
        EntityPlayer player = (EntityPlayer) entity;
        return !(player.isSpectator() || player.isCreative());
    }

    public enum Anger {

        CALM(0, DeeperDepthsSoundEvents.WARDEN_AMBIENT, DeeperDepthsSoundEvents.WARDEN_LISTENING),
        AGITATED(40, DeeperDepthsSoundEvents.WARDEN_AGITATED, DeeperDepthsSoundEvents.WARDEN_LISTENING),
        ANGRY(80, DeeperDepthsSoundEvents.WARDEN_ANGRY, DeeperDepthsSoundEvents.WARDEN_LISTENING_ANGRY);

        private final int minimumAnger;
        private final SoundEvent ambientSound, listeningSound;

        Anger(int minimumAnger, SoundEvent ambientSound, SoundEvent listeningSound) {
            this.minimumAnger = minimumAnger;
            this.ambientSound = ambientSound;
            this.listeningSound = listeningSound;
        }

        public int getMinimumAnger() {
            return minimumAnger;
        }

        public SoundEvent getAmbientSound() {
            return ambientSound;
        }

        public SoundEvent getListeningSound() {
            return listeningSound;
        }

        public static Anger get(int anger) {
            int length = values().length;
            for (int i = 1; i <= length; i++) {
                Anger value = values()[length - i];
                if (value.minimumAnger <= anger) return value;
            }
            return CALM;
        }

    }

    public enum State {
        IDLE(0),
        EMERGING(134),
        BURROWING(100),
        ROARING(64),
        SNIFFING(64),
        SONIC_BOOM(60);

        private final int length;

        State(int length) {
            this.length = length;
        }

        public int getLength() {
            return length;
        }

        public boolean isDigging() {
            return this == EMERGING || this == BURROWING;
        }

        public boolean spawnParticles(int stateTimer) {
            if (this == EMERGING) return stateTimer > 74;
            return this == BURROWING;
        }

        public static State get(byte id) {
            return values()[MathHelper.clamp(id, 0, values().length)];
        }

    }

}
