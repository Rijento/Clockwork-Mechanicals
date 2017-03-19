package net.rijento.clockwork_mechanicals.items;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.rijento.clockwork_mechanicals.entities.EntityMechanicalWorker;
import net.rijento.clockwork_mechanicals.init.ModItems;
import net.rijento.clockwork_mechanicals.lib.Order;

public class ItemMechanicalConfigurator extends Item
{
	
	public ItemMechanicalConfigurator()
	{
		this.setMaxStackSize(1);
	}
	
	@Override
	public boolean itemInteractionForEntity(ItemStack itemstack, net.minecraft.entity.player.EntityPlayer player, EntityLivingBase entity, net.minecraft.util.EnumHand hand)
	{
		if (entity instanceof EntityMechanicalWorker && player.isSneaking())
		{
			((EntityMechanicalWorker) entity).SetOrders(getOrders(itemstack),false);
			return true;
		}
		return false;
	}
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		ItemStack stack = player.getHeldItem(hand);
		addOrder(pos,"harvest",stack);
        return EnumActionResult.SUCCESS;
    }
	
	public void addOrder(BlockPos pos, String command, ItemStack stack)
	{
		if (!stack.hasTagCompound())
		{
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setTag("Orders", new NBTTagList());
		}
		NBTTagCompound toAdd = (new Order(pos,command)).getOrderNBT();
		stack.getTagCompound().getTagList("Orders", Constants.NBT.TAG_COMPOUND).appendTag(toAdd);
	}
	public List<Order> getOrders(ItemStack stack)
	{
		List<Order> orders = new ArrayList<Order>();
		for(int i = 0; i < stack.getTagCompound().getTagList("Orders", Constants.NBT.TAG_COMPOUND).tagCount(); i++)
		{
			orders.add(new Order((NBTTagCompound) stack.getTagCompound().getTagList("Orders", Constants.NBT.TAG_COMPOUND).get(i)));
		}
		return orders;
	}
}
