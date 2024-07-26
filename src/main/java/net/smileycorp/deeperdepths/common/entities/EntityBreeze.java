package net.smileycorp.deeperdepths.common.entities;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.smileycorp.deeperdepths.animation.EZAnimation;
import net.smileycorp.deeperdepths.animation.EZAnimationHandler;
import net.smileycorp.deeperdepths.animation.IAnimatedEntity;
import net.smileycorp.deeperdepths.common.DeeperDepthsSoundEvents;

public class EntityBreeze extends EntityMob implements IAnimatedEntity {
    //the number in the animation only states the duration of the animation
    public static final EZAnimation ANIMATION_SHOOT = EZAnimation.create(30);


    public static final DataParameter<Boolean> SHOOT_ATTACK = EntityDataManager.createKey(EntityBreeze.class, DataSerializers.BOOLEAN);

    /** use DataParamater booleans for animations for best results */
    protected boolean isShootAttack() {return this.dataManager.get(SHOOT_ATTACK);}
    protected void setShootAttack(boolean value) {this.dataManager.set(SHOOT_ATTACK, Boolean.valueOf(value));}

    public EntityBreeze(World worldIn) {
        super(worldIn);
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(SHOOT_ATTACK, Boolean.valueOf(false));
    }

    protected SoundEvent getAmbientSound()
    { return this.onGround ? DeeperDepthsSoundEvents.BREEZE_IDLE_GROUND : DeeperDepthsSoundEvents.BREEZE_IDLE_AIR; }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    { return DeeperDepthsSoundEvents.BREEZE_HURT; }

    protected SoundEvent getDeathSound() {
        return DeeperDepthsSoundEvents.BREEZE_DEATH;
    }

    //used for animation system
    private int animationTick;
    //just a variable that holds what the current animation is
    private EZAnimation currentAnimation;



    int exampleTimerOnly = 40;

    @Override
    public void onUpdate() {
        super.onUpdate();
        IBlockState iblockstate = this.world.getBlockState(this.getPosition().down());

        if (this.world.isRemote)
        {
            if (iblockstate.getMaterial() != Material.AIR)
            {
                for(int i = 0; i < 1; ++i)
                {
                    this.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.posX, this.posY, this.posZ, ((double)this.rand.nextFloat() - 0.5D) * 0.3D, ((double)this.rand.nextFloat() - 0.5D) * 0.3D, ((double)this.rand.nextFloat() - 0.5D) * 0.3D, Block.getStateId(iblockstate));
                }
            }
        }

        //just testing to see if the animation system is working
        if(exampleTimerOnly < 0) {
            // I wouldn't reccomend using timers as calling animations, I'd just use dataparamater booleans
            this.setAnimation(ANIMATION_SHOOT);
            exampleTimerOnly = 40;
        } else {
            exampleTimerOnly--;
        }


        //sends the Animation Handler constant updates on the animations
        EZAnimationHandler.INSTANCE.updateAnimations(this);
    }

    /** Immune to Fall Damage, instead plays a sound effect. */
    public void fall(float distance, float damageMultiplier)
    { if (distance > 3) this.playSound(DeeperDepthsSoundEvents.BREEZE_LAND, 1.0F, 1.0F); }

    @Override
    public int getAnimationTick() {
        return animationTick;
    }

    @Override
    public void setAnimationTick(int tick) {
        animationTick = tick;
    }

    @Override
    public EZAnimation getAnimation() {
        return currentAnimation;
    }

    @Override
    public void setAnimation(EZAnimation animation) {
        currentAnimation = animation;
    }

    //This is where you store a collective list of all the animations this entity is capable of ONLY USING THIS SYSTEM, there is no walk or idle animations
    @Override
    public EZAnimation[] getAnimations() {
        return new EZAnimation[]{ANIMATION_SHOOT};
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setBoolean("Shoot_Attack", this.isShootAttack());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        this.setShootAttack(nbt.getBoolean("Shoot_Attack"));
    }
}
