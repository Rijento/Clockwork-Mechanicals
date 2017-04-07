package net.rijento.clockwork_mechanicals.items;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.BlockStone;
import net.minecraft.block.BlockWorkbench;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.rijento.clockwork_mechanicals.ClockworkMechanicals;
import net.rijento.clockwork_mechanicals.entities.EntityMechanicalWorker;
import net.rijento.clockwork_mechanicals.lib.Order;

public class ItemMechanicalConfigurator extends Item
{
	public int current_task = 0;
	public ItemMechanicalConfigurator()
	{
		this.setMaxStackSize(1);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
		tooltip.add(TextFormatting.WHITE + "Number of orders: " + String.valueOf(this.getNumOrders(stack)));
		tooltip.add(TextFormatting.WHITE + "Right-click to add order.");
		tooltip.add(TextFormatting.WHITE + "Ctrl + right-click to remove order.");
		tooltip.add(TextFormatting.WHITE + "Shift + right-click on mechanical to give orders.");
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
		if (player.isSneaking() && hand == EnumHand.MAIN_HAND){
			player.openGui(ClockworkMechanicals.instance, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
			return EnumActionResult.SUCCESS;
		}
		else if (hand == EnumHand.MAIN_HAND)
		{
			ItemStack stack = player.getHeldItemMainhand();
			if (this.current_task == 1){
				if (worldIn.getBlockState(pos).getBlock() instanceof BlockCrops)
				{
					if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
					{
						removeOrder(pos.down(), "harvest", stack);
						return EnumActionResult.SUCCESS;
					}
					addOrder(pos.down(),"harvest",stack);
					return EnumActionResult.SUCCESS;
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
				{
					removeOrder(pos, "harvest", stack);
					return EnumActionResult.SUCCESS;
				}
				addOrder(pos,"harvest",stack);
		        return EnumActionResult.SUCCESS;
			}
			else if (this.current_task == 2)
			{
				if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
				{
					removeOrder(pos, "chop", stack);
					return EnumActionResult.SUCCESS;
				}
				addOrder(pos,"chop",stack);
		        return EnumActionResult.SUCCESS;
			}
		}
//		if (worldIn.getBlockState(pos).getBlock() instanceof BlockChest)
//		{
//			if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
//			{
//				removeOrder(pos, "dropoff", stack);
//				return EnumActionResult.SUCCESS;
//			}
//			addOrder(pos,"dropoff",stack);
//	        return EnumActionResult.SUCCESS;
//		}
//		
//		if (worldIn.getBlockState(pos).getBlock() instanceof BlockWorkbench)
//		{
//			if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
//			{
//				removeOrder(pos, "craft", stack);
//				return EnumActionResult.SUCCESS;
//			}
//			addOrder(pos,"craft",stack);
//	        return EnumActionResult.SUCCESS;
//		}
//		if (worldIn.getBlockState(pos) ==Blocks.DIRT.getDefaultState().withProperty(PropertyEnum.<BlockDirt.DirtType>create("variant", BlockDirt.DirtType.class), BlockDirt.DirtType.byMetadata(BlockDirt.DirtType.COARSE_DIRT.getMetadata())))
//		{
//			if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
//			{
//				removeOrder(pos, "chop", stack);
//				return EnumActionResult.SUCCESS;
//			}
//			addOrder(pos,"chop",stack);
//	        return EnumActionResult.SUCCESS;
//		}
//		if (worldIn.getBlockState(pos).getBlock() instanceof BlockStone)
//		{
//			if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
//			{
//				removeOrder(pos, "mine", stack);
//				return EnumActionResult.SUCCESS;
//			}
//			Order mine = new Order(pos,"mine");
//			mine.setFacing(facing.getOpposite());
//			addOrder(mine, stack);
//			
//	        return EnumActionResult.SUCCESS;
//		}
		return EnumActionResult.PASS;
		
    }
	
	public void addOrder(BlockPos pos, String command, ItemStack stack)
	{
		List<Order> orders = this.getOrders(stack);
		if (!stack.hasTagCompound())
		{
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setTag("Orders", new NBTTagList());
		}
		Order toAdd = new Order(pos,command);
		NBTTagCompound toAddNBT = toAdd.getOrderNBT();
		if (!orders.contains(toAdd))
		{
			stack.getTagCompound().getTagList("Orders", Constants.NBT.TAG_COMPOUND).appendTag(toAddNBT);
		}
	}
	public void addOrder(Order order, ItemStack stack)
	{
		List<Order> orders = this.getOrders(stack);
		if (!stack.hasTagCompound())
		{
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setTag("Orders", new NBTTagList());
			stack.getTagCompound().setInteger("current_task", 0);
		}
		Order toAdd = order;
		NBTTagCompound toAddNBT = toAdd.getOrderNBT();
		if (!orders.contains(toAdd))
		{
			stack.getTagCompound().getTagList("Orders", Constants.NBT.TAG_COMPOUND).appendTag(toAddNBT);
		}
	}
	
	public void removeOrder(BlockPos pos, String command, ItemStack stack)
	{
		List<Order> orders = this.getOrders(stack);
		if(!orders.isEmpty())
		{
			Order toRemove = new Order(pos, command);
			stack.getTagCompound().getTagList("Orders", Constants.NBT.TAG_COMPOUND).removeTag((orders.indexOf(toRemove)));
		}
	}
	public List<Order> getOrders(ItemStack stack)
	{
		if (!stack.hasTagCompound())
		{
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setTag("Orders", new NBTTagList());
			stack.getTagCompound().setInteger("current_task", 0);
		}
		List<Order> orders = new ArrayList<Order>();
		for(int i = 0; i < stack.getTagCompound().getTagList("Orders", Constants.NBT.TAG_COMPOUND).tagCount(); i++)
		{
			orders.add(new Order((NBTTagCompound) stack.getTagCompound().getTagList("Orders", Constants.NBT.TAG_COMPOUND).get(i)));
		}
		return orders;
	}
	public void setCurrentTask(int value)
	{
		System.out.println("set: "+value);
		this.current_task = value;
		
	}
	public void SaveCurrent(ItemStack stack)
	{
		stack.getTagCompound().setInteger("current_task", this.current_task);
	}
	
	public void loadCurrentTask(ItemStack stack)
	{
		this.current_task = stack.getTagCompound().getInteger("current_task");
	}
	public int getNumOrders(ItemStack stack)
	{
		if (!stack.hasTagCompound())
		{
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setTag("Orders", new NBTTagList());
			stack.getTagCompound().setInteger("current_task", 0);
		}
		return stack.getTagCompound().getTagList("Orders", Constants.NBT.TAG_COMPOUND).tagCount();
	}
	public int getComplexity(ItemStack stack)
	{
		return getNumOrders(stack) % 5;
	}
}
