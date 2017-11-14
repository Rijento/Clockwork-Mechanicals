package net.rijento.clockwork_mechanicals.items;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockSapling;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
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
import net.rijento.clockwork_mechanicals.lib.ContainerBasic;
import net.rijento.clockwork_mechanicals.lib.Order;
import net.rijento.clockwork_mechanicals.lib.filter.Filter;

public class ItemMechanicalConfigurator extends Item
{
	public int current_task = 0;
	public Filter withdrawFilter = new Filter();
	public Filter depositFilter = new Filter();
	public InventoryCrafting recipe = new InventoryCrafting(new ContainerBasic(), 3, 3);
	public ItemMechanicalConfigurator()
	{
		this.setMaxStackSize(1);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
		tooltip.add(TextFormatting.WHITE + "Number of orders: " + String.valueOf(this.getNumOrders(stack)));
		tooltip.add(TextFormatting.WHITE + "Right-click in offhnad to open GUI.");
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
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
		if (!worldIn.isRemote && handIn == EnumHand.OFF_HAND){
			load(playerIn.getHeldItemOffhand());
			playerIn.openGui(ClockworkMechanicals.instance, 0, worldIn, playerIn.getPosition().getX(), playerIn.getPosition().getY(), playerIn.getPosition().getZ());
			return new ActionResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
		}
        return new ActionResult(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		if (!worldIn.isRemote && hand == EnumHand.MAIN_HAND)
		{
			ItemStack stack = player.getHeldItemMainhand();
			load(stack);
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
				if (worldIn.getBlockState(pos).getBlock() instanceof BlockSapling)
				{
					if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
					{
						removeOrder(pos.down(), "chop", stack);
						return EnumActionResult.SUCCESS;
					}
					addOrder(pos.down(),"chop",stack);
					return EnumActionResult.SUCCESS;
				}
				if (worldIn.getBlockState(pos).getBlock() instanceof BlockLog)
				{
					if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
					{
						removeOrder(pos.down(), "chop", stack);
						return EnumActionResult.SUCCESS;
					}
					addOrder(pos.down(),"chop",stack);
					return EnumActionResult.SUCCESS;
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
				{
					removeOrder(pos, "chop", stack);
					return EnumActionResult.SUCCESS;
				}
				addOrder(pos,"chop",stack);
		        return EnumActionResult.SUCCESS;
			}
			else if (this.current_task == 4)
			{
				if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
				{
					removeOrder(pos, "mine", stack);
					return EnumActionResult.SUCCESS;
				}
				Order mine = new Order(pos,"mine");
				mine.setFacing(player.getHorizontalFacing());
				addOrder(mine, stack);
				
		        return EnumActionResult.SUCCESS;
			}
			else if (this.current_task == 5)
			{
				if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
				{
					removeOrder(pos, "pickup", stack);
					return EnumActionResult.SUCCESS;
				}
				Order pickup = new Order(pos, "pickup");
				pickup.filter = this.withdrawFilter;
				this.addOrder(pickup, stack);
		        return EnumActionResult.SUCCESS;
			}
			else if (this.current_task == 6)
			{
				if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
				{
					removeOrder(pos, "dropoff", stack);
					return EnumActionResult.SUCCESS;
				}
				Order dropoff = new Order(pos, "dropoff");
				dropoff.filter = this.depositFilter;
				this.addOrder(dropoff, stack);
		        return EnumActionResult.SUCCESS;
			}
			else if (this.current_task == 7)
			{
				if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
				{
					removeOrder(pos, "craft", stack);
					return EnumActionResult.SUCCESS;
				}
				Order craft = new Order(pos, "craft");
				craft.recipe = this.recipe;
				this.addOrder(craft, stack);
		        return EnumActionResult.SUCCESS;
			}
		}
//		
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
			if (orders.contains(toRemove))
			{
				stack.getTagCompound().getTagList("Orders", Constants.NBT.TAG_COMPOUND).removeTag((orders.indexOf(toRemove)));
			}
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
	public void save(ItemStack stack)
	{
		if (!stack.hasTagCompound())
		{
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setTag("Orders", new NBTTagList());
			stack.getTagCompound().setInteger("current_task", 0);
			stack.getTagCompound().setTag("filterWithdraw", new Filter().getFilterNBT());
			stack.getTagCompound().setTag("filterDeposit",  new Filter().getFilterNBT());
			stack.getTagCompound().setTag("Recipe", new NBTTagList());
		}
		NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < 9; ++i)
        {
            ItemStack itemstack = this.recipe.getStackInSlot(i);
            nbttaglist.appendTag(itemstack.writeToNBT(new NBTTagCompound()));
        }
        stack.getTagCompound().setTag("Recipe", nbttaglist);
		stack.getTagCompound().setInteger("current_task", this.current_task);
		stack.getTagCompound().setTag("filterWithdraw", this.withdrawFilter.getFilterNBT());
		stack.getTagCompound().setTag("filterDeposit", this.depositFilter.getFilterNBT());
	}
	public void load(ItemStack stack)
	{
		if (!stack.hasTagCompound())
		{
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setTag("Orders", new NBTTagList());
			stack.getTagCompound().setInteger("current_task", 0);
			stack.getTagCompound().setTag("filterWithdraw", new Filter().getFilterNBT());
			stack.getTagCompound().setTag("filterDeposit",  new Filter().getFilterNBT());
			stack.getTagCompound().setTag("Recipe", new NBTTagList());
		}
		NBTTagList nbttaglist = stack.getTagCompound().getTagList("Recipe", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < nbttaglist.tagCount() && i < 9; ++i)
        {
            ItemStack itemstack = new ItemStack(nbttaglist.getCompoundTagAt(i));

            if (!itemstack.isEmpty())
            {
                this.recipe.setInventorySlotContents(i, itemstack);
            }
        }
		this.current_task = stack.getTagCompound().getInteger("current_task");
		this.withdrawFilter = new Filter(stack.getTagCompound().getCompoundTag("filterWithdraw"));
		this.depositFilter = new Filter(stack.getTagCompound().getCompoundTag("filterDeposit"));
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
		return getNumOrders(stack) / 5;
	}
}
