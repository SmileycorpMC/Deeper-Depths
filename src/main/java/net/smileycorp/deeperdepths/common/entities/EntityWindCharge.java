package net.smileycorp.deeperdepths.common.entities;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.ForgeEventFactory;
import net.smileycorp.deeperdepths.client.ClientProxy;
import net.smileycorp.deeperdepths.common.DeeperDepthsSoundEvents;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static net.minecraft.block.BlockDoor.HALF;

public class EntityWindCharge extends EntityThrowable
{
    private static final DataParameter<Float> BURST_RANGE = EntityDataManager.createKey(EntityWindCharge.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> BURST_INTENSITY = EntityDataManager.createKey(EntityWindCharge.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> BURST_INTERACT_RANGE = EntityDataManager.createKey(EntityWindCharge.class, DataSerializers.FLOAT);
    private static final DataParameter<Boolean> DO_FALL_REDUCTION = EntityDataManager.createKey(EntityWindCharge.class, DataSerializers.BOOLEAN);
    /** Multiplies the movement speed by this when reflected. Yes, this WILL stack */
    int reflectSpeedMult = 3;
    /** Sets a certain entity to be completely immune. Used so Breeze don't get knockback from their own Wind Charges. */
    protected EntityLivingBase knockbackImmune;
    /** A lot of worthless things copied from EntityThrowable. */
    private int xTile;
    private int yTile;
    private int zTile;
    private Block inTile;
    private int ticksInGround;
    private int ticksInAir;
    public Entity ignoreEntity;
    private int ignoreTime;

    //boolean playerFallReduction;

    public EntityWindCharge(World worldIn, EntityLivingBase throwerIn)
    { super(worldIn, throwerIn); }

    public EntityWindCharge(World worldIn, EntityLivingBase throwerIn, EntityLivingBase knockbackImmuneIn)
    {
        super(worldIn, throwerIn);
        knockbackImmune = knockbackImmuneIn;
    }

    public EntityWindCharge(World worldIn, double x, double y, double z)
    { super(worldIn, x, y, z); }

    public EntityWindCharge(World worldIn)
    {
        super(worldIn);

        this.setSize(0.3125F, 0.3125F);
    }

    protected void entityInit()
    {
        super.entityInit();

        this.dataManager.register(BURST_RANGE, 10F);
        this.dataManager.register(BURST_INTERACT_RANGE, 5F);
        this.dataManager.register(BURST_INTENSITY, 0.8F);
        this.dataManager.register(DO_FALL_REDUCTION, Boolean.FALSE);
    }


    public boolean canBeCollidedWith() {
        return true;
    }

    public float getCollisionBorderSize() {
        return 1.0F;
    }

    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (this.isEntityInvulnerable(source))
        { return false; }
        else
        {
            this.markVelocityChanged();
            if (source.getTrueSource() != null)
            {
                this.playSound(DeeperDepthsSoundEvents.BREEZE_JUMP, 1, 4);
                this.world.spawnParticle(EnumParticleTypes.CRIT, this.posX, this.posY, this.posZ, 0, 0,0);

                Vec3d vec3d = source.getTrueSource().getLookVec();
                if (vec3d != null)
                {
                    this.motionX = vec3d.x * reflectSpeedMult;
                    this.motionY = vec3d.y * reflectSpeedMult;
                    this.motionZ = vec3d.z * reflectSpeedMult;
                }

                if (source.getTrueSource() instanceof EntityLivingBase)
                { this.thrower = (EntityLivingBase)source.getTrueSource(); }

                return true;
            }
            else
            { return false; }
        }
    }

    /** Forces it to Impact. This is used so it can explode instantly. 'null' is supported.  */
    public void forceExplode(@Nullable RayTraceResult result)
    { this.onImpact(result); }

    protected void onImpact(RayTraceResult result)
    {
        SoundEvent burstSound = knockbackImmune instanceof EntityBreeze ? DeeperDepthsSoundEvents.BREEZE_WIND_BURST : DeeperDepthsSoundEvents.WIND_CHARGE_WIND_BURST;

        this.playSound(burstSound, 1, 1);


        if (!this.world.isRemote)
        {
            if (result != null && result.entityHit != null && !(result.entityHit instanceof EntityEnderCrystal))
            {
                result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), (float)1);
                if(this.isBurning()) result.entityHit.setFire(5);
            }

            preformKnockbackEffects();
            checkBlockInteraction(this.getPosition());

            this.world.setEntityState(this, (byte)3);
            this.setDead();



            for (int i = 0; i < this.getBurstRange() * 10; i++)
            {
                EnumParticleTypes type = i < this.getBurstRange() * 10 / 10 ? EnumParticleTypes.EXPLOSION_LARGE : EnumParticleTypes.CLOUD;
                float range = this.getBurstRange()/2;
                double x = this.posX + world.rand.nextFloat() * range - world.rand.nextFloat() * range;
                double y = this.posY + (world.rand.nextFloat() * range - world.rand.nextFloat() * range)/2;
                double z = this.posZ + world.rand.nextFloat() * range - world.rand.nextFloat() * range;

                ((WorldServer)this.world).spawnParticle(type, x, y, z, 1, 0, 0, 0, 0.0);

                /** Client Proxy Particle causes a Crash!!! */
                //ClientProxy.addParticle(type, x, y, z, Color.WHITE);
            }
        }
    }

    /** No gravity, keep going forward!! */
    protected float getGravityVelocity() {
        return 0.0F;
    }

    /** Mostly from Minecraft's Explosion Code, as Wind Charges use the same code, but altered to remove damage. */
    public void preformKnockbackEffects()
    {
        float scale = getBurstRange();
        double knockbackStrength = getBurstPower() + 0.2;
        float k = MathHelper.floor(this.posX - (double) scale - 1.0);
        float l = MathHelper.floor(this.posX + (double) scale + 1.0);
        double i2 = MathHelper.floor(this.posY - (double) scale - 1.0);
        double i1 = MathHelper.floor(this.posY + (double) scale + 1.0);
        double j2 = MathHelper.floor(this.posZ - (double) scale - 1.0);
        double j1 = MathHelper.floor(this.posZ + (double) scale + 1.0);
        List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, new AxisAlignedBB((double) k, i2, j2, (double) l, i1, j1));
        Vec3d vec3d = new Vec3d(this.posX, this.posY, this.posZ);

        for (Entity entity : list)
        {
            Double knockbackResist = 1D;

            if (entity instanceof EntityLivingBase) knockbackResist = knockbackResist - ((EntityLivingBase)entity).getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getAttributeValue();

            if (!entity.isImmuneToExplosions() && entity != this.knockbackImmune)
            {
                double d12 = entity.getDistance(this.posX, this.posY, this.posZ) / (double) scale;
                if (d12 <= 1.0)
                {
                    /* The distance to power calculation breaks down if the Wind Charge is spawned within an entity... so we do a little trick called lying >B) */
                    if (d12 == 0) d12 = 1.85F;

                    double dx = entity.posX - this.posX;
                    double dy = entity.posY + (double) entity.getEyeHeight() - this.posY;
                    double dz = entity.posZ - this.posZ;
                    double distance = (double) MathHelper.sqrt(dx * dx + dy * dy + dz * dz);
                    if (distance != 0.0)
                    {
                        dx /= distance;
                        dy /= distance;
                        dz /= distance;
                        double blockStoppage = (double) this.checkBlockBlocking(vec3d, entity.getEntityBoundingBox());
                        double kmult = (knockbackStrength - d12) * blockStoppage;

                        entity.motionX += (dx * kmult);
                        entity.motionY += (dy * kmult);
                        entity.motionZ += (dz * kmult);
                        entity.velocityChanged = true;
                    }
                }
            }
        }
    }

    /** Mostly from getBlockDensity, but it properly ignores non-solid blocks. Checks how much of the entity is exposed to the explosion. */
    private float checkBlockBlocking(Vec3d vec, AxisAlignedBB bb)
    {
        double dx = 1.0 / ((bb.maxX - bb.minX) * 2.0 + 1.0);
        double dy = 1.0 / ((bb.maxY - bb.minY) * 2.0 + 1.0);
        double dz = 1.0 / ((bb.maxZ - bb.minZ) * 2.0 + 1.0);
        double d3 = (1.0 - Math.floor(1.0 / dx) * dx) / 2.0;
        double d4 = (1.0 - Math.floor(1.0 / dz) * dz) / 2.0;
        if (dx >= 0.0 && dy >= 0.0 && dz >= 0.0)
        {
            int j2 = 0;
            int k2 = 0;
            for(float fx = 0.0F; fx <= 1.0F; fx = (float)((double)fx + dx))
            {
                for(float fy = 0.0F; fy <= 1.0F; fy = (float)((double)fy + dy))
                {
                    for(float fz = 0.0F; fz <= 1.0F; fz = (float)((double)fz + dz))
                    {
                        double d5 = bb.minX + (bb.maxX - bb.minX) * (double)fx;
                        double d6 = bb.minY + (bb.maxY - bb.minY) * (double)fy;
                        double d7 = bb.minZ + (bb.maxZ - bb.minZ) * (double)fz;
                        RayTraceResult result = this.world.rayTraceBlocks(new Vec3d(d5 + d3, d6, d7 + d4), vec, false, true, false);

                        if (result == null)
                        { ++j2; }
                        ++k2;
                    }
                }
            }
            return (float)j2 / (float)k2;
        } else
        { return 0.0F; }
    }

    /** Ray-trace checks surrounding blocks within range. */
    private void checkBlockInteraction(BlockPos pos)
    {
        double radius = (double) getBurstInteractRange();
        /* Uses a list, so the same block isn't interacted with multiple times. */
        List<BlockPos> processedBlocks = new ArrayList<>();

        for (double h1 = -radius; h1 <= radius; h1++)
        {
            for (double i1 = -radius; i1 <= radius; i1++)
            {
                for (double j1 = -radius; j1 <= radius; j1++)
                {
                    BlockPos tPos = pos.add(h1, i1, j1);

                    /* Curves the interaction radius. */
                    if (pos.distanceSq(tPos) > radius * radius)
                    { continue; }

                    /* Toggles between Raytraced interaction, or just anything within this radius. Raytraced has lots of issues currently, likely remove later. */
                    if (false)
                    {
                        RayTraceResult result = this.world.rayTraceBlocks(new Vec3d(pos.getX(), pos.getY(), pos.getZ()), new Vec3d(tPos.getX(), tPos.getY(), tPos.getZ()));
                        if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK)
                        {
                            if (processedBlocks.contains(result.getBlockPos())) { continue; }
                            activateBlocks(result.getBlockPos());
                            processedBlocks.add(result.getBlockPos());
                        }
                    }
                    else
                    {
                        if (processedBlocks.contains(tPos)) { continue; }
                        activateBlocks(tPos);
                        processedBlocks.add(tPos);
                    }
                }
            }
        }
    }

    /** Simply activates blocks. */
    private void activateBlocks(BlockPos pos)
    {
        Block block = this.world.getBlockState(pos).getBlock();
        EntityPlayer countedInteract = thrower instanceof EntityPlayer ? (EntityPlayer) thrower : null;

        if (block instanceof BlockButton || block instanceof BlockTrapDoor || block instanceof BlockDoor && this.world.getBlockState(pos).getValue(HALF) == BlockDoor.EnumDoorHalf.LOWER || block instanceof BlockLever)
        { block.onBlockActivated(this.world, pos, this.world.getBlockState(pos), countedInteract, EnumHand.MAIN_HAND, EnumFacing.UP, 0.5F, 0.5F, 0.5F); }
        /* AAAAAAAHHHHH WHY DOES BlockFenceGate REQUIRE A PLAYER FOR `onBlockActivated`, IT COULD JUST USE THE HIT POS! */
        else if (block instanceof BlockFenceGate)
        {
            boolean isOpen = this.world.getBlockState(pos).getValue(BlockFenceGate.OPEN);
            EnumFacing getFacing = this.world.getBlockState(pos).getValue(BlockFenceGate.FACING);

            world.setBlockState(pos, this.world.getBlockState(pos).withProperty(BlockFenceGate.OPEN, !isOpen).withProperty(BlockFenceGate.FACING, this.rand.nextBoolean() ? getFacing.getOpposite() : getFacing));
            world.playEvent(null, isOpen ? 1014 : 1008, pos, 0);
        }
    }

    /** onUpdate needs to be overridden to handle complex changes. */
    @Override
    public void onUpdate()
    {
        this.lastTickPosX = this.posX;
        this.lastTickPosY = this.posY;
        this.lastTickPosZ = this.posZ;
        if (!this.world.isRemote) { this.setFlag(6, this.isGlowing()); }
        this.onEntityUpdate();

        if (this.throwableShake > 0)
        { --this.throwableShake; }

        if (this.inGround)
        {
            if (this.world.getBlockState(new BlockPos(this.xTile, this.yTile, this.zTile)).getBlock() == this.inTile)
            {
                ++this.ticksInGround;
                if (this.ticksInGround == 1200)
                { this.setDead(); }

                return;
            }

            this.inGround = false;
            this.motionX *= (double)(this.rand.nextFloat() * 0.2F);
            this.motionY *= (double)(this.rand.nextFloat() * 0.2F);
            this.motionZ *= (double)(this.rand.nextFloat() * 0.2F);
            this.ticksInGround = 0;
            this.ticksInAir = 0;
        }
        else
        { ++this.ticksInAir; }

        Vec3d vec3d = new Vec3d(this.posX, this.posY, this.posZ);
        Vec3d vec3d1 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        RayTraceResult raytraceresult = this.world.rayTraceBlocks(vec3d1, vec3d, false, true, false);
        vec3d = new Vec3d(this.posX, this.posY, this.posZ);
        vec3d1 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        if (raytraceresult != null)
        { vec3d1 = new Vec3d(raytraceresult.hitVec.x, raytraceresult.hitVec.y, raytraceresult.hitVec.z); }

        Entity entity = null;
        List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(this.motionX, this.motionY, this.motionZ).grow(1.0));
        double d0 = 0.0;
        boolean flag = false;

        for (Entity value : list)
        {
            Entity entity1 = (Entity) value;
            if (entity1.canBeCollidedWith())
            {
                if (entity1 == this.ignoreEntity)
                { flag = true; }
                else if (this.thrower != null && this.ticksExisted < 2 && this.ignoreEntity == null)
                {
                    this.ignoreEntity = entity1;
                    flag = true;
                }
                else
                {
                    flag = false;
                    AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow(0.30000001192092896);
                    RayTraceResult raytraceresult1 = axisalignedbb.calculateIntercept(vec3d, vec3d1);
                    if (raytraceresult1 != null)
                    {
                        double d1 = vec3d.squareDistanceTo(raytraceresult1.hitVec);
                        if (d1 < d0 || d0 == 0.0)
                        {
                            entity = entity1;
                            d0 = d1;
                        }
                    }
                }
            }
        }

        if (this.ignoreEntity != null)
        {
            if (flag)
            { this.ignoreTime = 2; }
            else if (this.ignoreTime-- <= 0)
            { this.ignoreEntity = null; }
        }

        if (entity != null)
        { raytraceresult = new RayTraceResult(entity); }

        if (raytraceresult != null)
        {
            if (raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK && this.world.getBlockState(raytraceresult.getBlockPos()).getBlock() == Blocks.PORTAL)
            { this.setPortal(raytraceresult.getBlockPos()); }
            else if (!ForgeEventFactory.onProjectileImpact(this, raytraceresult))
            { this.onImpact(raytraceresult); }
        }

        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
        float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float)(MathHelper.atan2(this.motionX, this.motionZ) * 57.29577951308232);

        for(this.rotationPitch = (float)(MathHelper.atan2(this.motionY, (double)f) * 57.29577951308232); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
        { }

        while(this.rotationPitch - this.prevRotationPitch >= 180.0F)
        { this.prevRotationPitch += 360.0F; }

        while(this.rotationYaw - this.prevRotationYaw < -180.0F)
        { this.prevRotationYaw -= 360.0F; }

        while(this.rotationYaw - this.prevRotationYaw >= 180.0F)
        { this.prevRotationYaw += 360.0F; }

        this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
        this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
        float f1 = 1.0F;
        float f2 = this.getGravityVelocity();

        this.motionX *= (double)f1;
        this.motionY *= (double)f1;
        this.motionZ *= (double)f1;
        if (!this.hasNoGravity())
        { this.motionY -= (double)f2; }

        this.setPosition(this.posX, this.posY, this.posZ);
    }

    public void setBurstRange(float size)
    { this.dataManager.set(BURST_RANGE, Float.valueOf(size)); }

    public float getBurstRange()
    { return ((Float)this.dataManager.get(BURST_RANGE)).floatValue(); }

    public void setBurstInteractRange(float size)
    { this.dataManager.set(BURST_INTERACT_RANGE, Float.valueOf(size)); }

    public float getBurstInteractRange()
    { return ((Float)this.dataManager.get(BURST_INTERACT_RANGE)).floatValue(); }

    public void setBurstPower(float size)
    { this.dataManager.set(BURST_INTENSITY, Float.valueOf(size)); }

    public float getBurstPower()
    { return ((Float)this.dataManager.get(BURST_INTENSITY)).floatValue(); }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);

        this.dataManager.set(BURST_RANGE, Float.valueOf(compound.getFloat("BurstRange")));
        this.dataManager.set(BURST_INTERACT_RANGE, Float.valueOf(compound.getFloat("BurstInteractRange")));
        this.dataManager.set(BURST_INTENSITY, Float.valueOf(compound.getFloat("BurstPower")));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);

        compound.setFloat("BurstRange", getBurstRange());
        compound.setFloat("BurstInteractRange", getBurstInteractRange());
        compound.setFloat("BurstPower", getBurstPower());
    }
}