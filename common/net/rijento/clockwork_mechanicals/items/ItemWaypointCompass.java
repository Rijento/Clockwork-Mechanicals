package net.rijento.clockwork_mechanicals.items;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.rijento.clockwork_mechanicals.ClockworkMechanicals;

public class ItemWaypointCompass extends Item
{
	public ItemWaypointCompass()
	{ 
		//Below is an edited snippet of code from the ItemCompass from minecraft
		this.addPropertyOverride(new ResourceLocation("waypointangle"), new IItemPropertyGetter()
        {
            @SideOnly(Side.CLIENT)
            double rotation;
            @SideOnly(Side.CLIENT)
            double rota;
            @SideOnly(Side.CLIENT)
            long lastUpdateTick;
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
            {
                if (entityIn == null && !stack.isOnItemFrame())
                {
                    return 0.0F;
                }
                else
                {
                    boolean flag = entityIn != null;
                    Entity entity = (Entity)(flag ? entityIn : stack.getItemFrame());

                    if (worldIn == null)
                    {
                        worldIn = entity.world;
                    }

                    double d0;

                    if (worldIn.provider.isSurfaceWorld() && hasTarget(stack))
                    {
                        double d1 = flag ? (double)entity.rotationYaw : this.getFrameRotation((EntityItemFrame)entity);
                        d1 = MathHelper.positiveModulo(d1 / 360.0D, 1.0D);
                        double d2 = this.getAngleToBlock(getTargetPos(stack), entity)/ (Math.PI * 2D);
                        d0 = 0.5D - (d1 - 0.25D - d2);
                    }
                    else
                    {
                        d0 = Math.random();
                    }

                    if (flag)
                    {
                        d0 = this.wobble(worldIn, d0);
                    }

                    return MathHelper.positiveModulo((float)d0, 1.0F);
                }
			}
			@SideOnly(Side.CLIENT)
            private double wobble(World worldIn, double p_185093_2_)
            {
                if (worldIn.getTotalWorldTime() != this.lastUpdateTick)
                {
                    this.lastUpdateTick = worldIn.getTotalWorldTime();
                    double d0 = p_185093_2_ - this.rotation;
                    d0 = MathHelper.positiveModulo(d0 + 0.5D, 1.0D) - 0.5D;
                    this.rota += d0 * 0.1D;
                    this.rota *= 0.8D;
                    this.rotation = MathHelper.positiveModulo(this.rotation + this.rota, 1.0D);
                }

                return this.rotation;
            }
            @SideOnly(Side.CLIENT)
            private double getFrameRotation(EntityItemFrame p_185094_1_)
            {
                return (double)MathHelper.wrapDegrees(180 + p_185094_1_.facingDirection.getHorizontalIndex() * 90);
            }
            @SideOnly(Side.CLIENT)
            private double getAngleToBlock(BlockPos pos, Entity player)
            {
                return Math.atan2((double)pos.getZ() - player.posZ, (double)pos.getX() - player.posX);
            }
        });
		//above is an edited snippet of code from the ItemCompass from minecraft
    }
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
		tooltip.add(TextFormatting.WHITE + "Shift + right-click to set point.");
		if (hasTarget(stack))
		{
			BlockPos pos = getTargetPos(stack);
			tooltip.add(TextFormatting.WHITE + "Target: " + pos.toString());
		}
		else
		{
			tooltip.add(TextFormatting.WHITE + "Target: None");
		}		
    }
	
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		if (player.isSneaking())
		{
			ItemStack stack = player.getHeldItem(hand);
			this.setTargetPos(pos, stack);
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.PASS;
    }
	
	private void setTargetPos(BlockPos pos, ItemStack stack)
	{
		if (!stack.hasTagCompound())
		{
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setTag("target", new NBTTagCompound());
			stack.getTagCompound().setBoolean("hasTarget", false);
		}
		((NBTTagCompound)stack.getTagCompound().getTag("target")).setInteger("x", pos.getX());
		((NBTTagCompound)stack.getTagCompound().getTag("target")).setInteger("y", pos.getY());
		((NBTTagCompound)stack.getTagCompound().getTag("target")).setInteger("z", pos.getZ());
		stack.getTagCompound().setBoolean("hasTarget", true);
	}
	private BlockPos getTargetPos(ItemStack stack)
	{

		if (!stack.hasTagCompound())
		{
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setTag("target", new NBTTagCompound());
			stack.getTagCompound().setBoolean("hasTarget", false);
		}
		int x = stack.getTagCompound().getCompoundTag("target").getInteger("x");
		int y = stack.getTagCompound().getCompoundTag("target").getInteger("y");
		int z = stack.getTagCompound().getCompoundTag("target").getInteger("z");
		return new BlockPos(x, y, z);
		
	}
	private boolean hasTarget(ItemStack stack)
	{
		if (!stack.hasTagCompound())
		{
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setTag("target", new NBTTagCompound());
			stack.getTagCompound().setBoolean("hasTarget", false);
		}
		return stack.getTagCompound().getBoolean("hasTarget");
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return "item." + ClockworkMechanicals.RESOURCE_PREFIX + "waypointcompass";
	}
}
