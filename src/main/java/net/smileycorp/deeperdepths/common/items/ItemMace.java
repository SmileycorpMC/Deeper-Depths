package net.smileycorp.deeperdepths.common.items;

import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.deeperdepths.common.Constants;
import net.smileycorp.deeperdepths.common.DeeperDepthsSoundEvents;
import net.smileycorp.deeperdepths.common.enchantments.DeeperDepthsEnchantments;
import net.smileycorp.deeperdepths.common.entities.EntityWindCharge;

import java.util.List;

public class ItemMace extends ItemDeeperDepths
{
    private final float attackDamage;
    public ItemMace()
    {
        super("mace");
        this.maxStackSize = 1;
        this.setMaxDamage(500);
        this.attackDamage = 5.0F;
    }

    /** Yes, the Mace mines everything x1.5 faster. */
    @Override
    public float getDestroySpeed(ItemStack stack, IBlockState state)
    { return 1.5F; }

    /** Why yes, I did stack all the abilities in the vanilla hitEntity method. It works better than Forge's `LivingHurtEvent`. */
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        if (attacker.world.isRemote) return false;

        stack.damageItem(1, attacker);

        if (attacker.fallDistance > 1.5)
        {
            boolean heavyLand = false;

            int breach_level = EnchantmentHelper.getEnchantmentLevel(DeeperDepthsEnchantments.BREACH, attacker.getHeldItemMainhand());
            int density_level = EnchantmentHelper.getEnchantmentLevel(DeeperDepthsEnchantments.DENSITY, attacker.getHeldItemMainhand());
            int wind_level = EnchantmentHelper.getEnchantmentLevel(DeeperDepthsEnchantments.WIND_BURST, attacker.getHeldItemMainhand());

            float fallDamage = calculateDamage(attacker);
            float breachArmorIgnorePercent = breach_level * 0.15F;
            float breachDamage = fallDamage * breachArmorIgnorePercent;
            float densityAdditionalDamage = attacker.fallDistance * density_level * 0.5F;

            target.attackEntityFrom(Constants.causeMaceDamage(attacker), (fallDamage - breachDamage) + densityAdditionalDamage);
            target.attackEntityFrom(Constants.causeMaceDamage(attacker).setDamageBypassesArmor(), breachDamage + densityAdditionalDamage);

            if (attacker.fallDistance > 3) heavyLand = true;
            attacker.motionY = 2;
            attacker.velocityChanged = true;
            attacker.fallDistance = 0;

            if (wind_level > 0) doWindBurst(attacker, wind_level, target);
            else
            {
                attacker.motionY = 0;
                //attacker.velocityChanged = true;
            }

            attacker.world.playSound(null, attacker.getPosition(), target.onGround ? heavyLand ? DeeperDepthsSoundEvents.MACE_SMASH_GROUND_HEAVY : DeeperDepthsSoundEvents.MACE_SMASH_GROUND : DeeperDepthsSoundEvents.MACE_SMASH_AIR, SoundCategory.PLAYERS, 1, 1);

            pushFromTarget(target, attacker);
            if (target.onGround) spawnSmashParticles(attacker, target);
        }
        return true;
    }

    /** Calculates the additional Damage for the Mace attack, before any enchantments. */
    private static float calculateDamage(EntityLivingBase entity)
    {
        float fall = entity.fallDistance;
        float damageResult = 0;

        for (int i = 1; i <= fall; i++)
        {
            /* First 3 adds 4 damage */
            if (i <= 3) damageResult += 4;
            /* Next 5 adds 2 damage */
            else if (i <= 3 + 5) damageResult += 2;
            /* Dump the rest of Fall directly into Damage after, as each 1 deals 1 damage. */
            else
            {
                damageResult += (fall - i + 1);
                break;
            }
        }
        return damageResult;
    }

    /** Spawns a Wind Charge directly inside the Mace Wielder, for the Wind Charged enchantment. */
    private static void doWindBurst(EntityLivingBase spawnPoint, int enchantmentLevel, EntityLivingBase immune)
    {
        EntityWindCharge entitywindcharge = new EntityWindCharge(spawnPoint.world, spawnPoint, immune);
        entitywindcharge.posY = spawnPoint.posY;
        entitywindcharge.setBurstPower(0.5F * (enchantmentLevel + 1));
        entitywindcharge.setBurstRange(10F);
        entitywindcharge.setBurstInteractRange(5F);
        entitywindcharge.forceSpawn = true;
        entitywindcharge.forceExplode(null);
        spawnPoint.world.spawnEntity(entitywindcharge);
        entitywindcharge.setDead();
    }

    /** Pushes entities away from the struck target. */
    private static void pushFromTarget(Entity centerTarget, Entity ignored)
    {
        float distance = 2.5F;
        float k = MathHelper.floor(centerTarget.posX - (double) distance - 1.0);
        float l = MathHelper.floor(centerTarget.posX + (double) distance + 1.0);
        double i2 = MathHelper.floor(centerTarget.posY - (double) distance - 1.0);
        double i1 = MathHelper.floor(centerTarget.posY + (double) distance + 1.0);
        double j2 = MathHelper.floor(centerTarget.posZ - (double) distance - 1.0);
        double j1 = MathHelper.floor(centerTarget.posZ + (double) distance + 1.0);
        List<Entity> list = centerTarget.world.getEntitiesWithinAABBExcludingEntity(centerTarget, new AxisAlignedBB((double) k, i2, j2, (double) l, i1, j1));

        for (Entity entity : list)
        {
            if (entity != ignored)
            {
                double knockbackResist = 1.0D;
                if (entity instanceof EntityLivingBase) knockbackResist -= ((EntityLivingBase)entity).getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getAttributeValue();

                double d12 = entity.getDistance(entity.posX, entity.posY, entity.posZ) / (double) distance;
                if (d12 <= 1.0)
                {
                    double dx = entity.posX - centerTarget.posX;
                    double dy = entity.posY + (double) entity.getEyeHeight() - centerTarget.posY;
                    double dz = entity.posZ - centerTarget.posZ;
                    double reCheckDistance = (double) MathHelper.sqrt(dx * dx + dy * dy + dz * dz);
                    if (reCheckDistance != 0.0)
                    {
                        dx /= reCheckDistance;
                        dy /= reCheckDistance;
                        dz /= reCheckDistance;
                        double kmult = (0.9 - d12);

                        entity.motionX += dx * kmult * knockbackResist;
                        entity.motionY += dy * kmult * knockbackResist;
                        entity.motionZ += dz * kmult * knockbackResist;
                        entity.velocityChanged = true;
                    }
                }
            }
        }
    }

    /** Spawns a ring of BlockDust Particles around the given entity. */
    private static void spawnSmashParticles(EntityLivingBase attacker, EntityLivingBase target)
    {
        /* Whole particle radius uses the block directly below the Target. */
        IBlockState iblockstate = target.world.getBlockState(target.getPosition().down());
        double radius = 3.0;
        int particleCount = 100;

        for (int i = 0; i < particleCount; i++)
        {
            double angle = 2 * Math.PI * i / particleCount;
            double xOffset = radius * Math.cos(angle);
            double zOffset = radius * Math.sin(angle);
            double x = target.posX + xOffset;
            double y = target.posY;
            double z = target.posZ + zOffset;

            double spreadX = target.getRNG().nextFloat() - target.getRNG().nextFloat();
            double spreadY = target.getRNG().nextFloat()*4 - target.getRNG().nextFloat()*4;
            double spreadZ = target.getRNG().nextFloat() - target.getRNG().nextFloat();

            /* Tweaks made to `hitEntity` might make this unnecessary now. Investigate to see about shortening! */
            if (attacker.world instanceof WorldServer)
            {
                ((WorldServer)attacker.world).spawnParticle(EnumParticleTypes.BLOCK_DUST, target.posX, target.posY, target.posZ, 1, spreadX, spreadY, spreadZ, 0.05, new int[]{Block.getStateId(iblockstate)});
                ((WorldServer)attacker.world).spawnParticle(EnumParticleTypes.BLOCK_DUST, x, y, z, 10, spreadX, spreadY, spreadZ, 0.05, new int[]{Block.getStateId(iblockstate)});
            }
            else
            {
                attacker.world.spawnParticle(EnumParticleTypes.BLOCK_DUST, target.posX, target.posY, target.posZ, spreadX, spreadY, spreadZ, new int[]{Block.getStateId(iblockstate)});
                attacker.world.spawnParticle(EnumParticleTypes.BLOCK_DUST, x, y, z, spreadX, spreadY, spreadZ, new int[]{Block.getStateId(iblockstate)});
            }
        }
    }

    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving)
    {
        if ((double)state.getBlockHardness(worldIn, pos) != 0.0) { stack.damageItem(2, entityLiving); }
        return true;
    }

    public boolean canDestroyBlockInCreative(World world, BlockPos pos, ItemStack stack, EntityPlayer player)
    { return false; }

    @SideOnly(Side.CLIENT)
    public boolean isFull3D()
    { return true; }

    public EnumRarity getRarity(ItemStack stack)
    { return EnumRarity.EPIC; }

    public int getItemEnchantability()
    { return 15; }

    /** Repaired using the Breeze Rod. */
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
    { return repair.getItem() == DeeperDepthsItems.MATERIALS && repair.getMetadata() == 3; }

    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot)
    {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);
        if (equipmentSlot == EntityEquipmentSlot.MAINHAND)
        {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double)this.attackDamage, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -3.4F, 0));
        }

        return multimap;
    }
}