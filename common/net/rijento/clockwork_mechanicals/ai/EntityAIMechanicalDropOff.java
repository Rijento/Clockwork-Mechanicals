package net.rijento.clockwork_mechanicals.ai;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.rijento.clockwork_mechanicals.entities.EntityMechanicalWorker;
import net.rijento.clockwork_mechanicals.items.ItemMainspring;
import net.rijento.clockwork_mechanicals.lib.filter.Filter;
import net.rijento.clockwork_mechanicals.lib.filter.KeepAmnt;

public class EntityAIMechanicalDropOff extends EntityAIBase
{
	private final EntityMechanicalWorker theMechanical;
	private final int priority;
	private final BlockPos targetInventory;
	private final static int maxruntime = 100;
	private int runtime;
	private int transferTime;
	private Filter filter;
	
	public EntityAIMechanicalDropOff( EntityMechanicalWorker theMechanicalIn, BlockPos InventoryIn, int priorityIn, @Nullable Filter filterIn)
	{
		this.theMechanical = theMechanicalIn;
		this.targetInventory = InventoryIn;
		this.priority = priorityIn;
		if (!this.theMechanical.world.isRemote)
		{
			this.transferTime = (int)(8 / ItemMainspring.getResistance(this.theMechanical.getMainspring().getItemDamage()));
		}
		if (filterIn != null){this.filter = filterIn;}
	}
	
	public boolean shouldUpdate()
	{
		if (this.priority != this.theMechanical.getCurrentTask()){return false;}
		else if (!(this.theMechanical.getTension()-0.15F>0)){return false;}
		else if (this.theMechanical.isWinding == true){return false;}
		else if (this.theMechanical.isWet()){return false;}
		else if (Math.sqrt(this.theMechanical.getDistanceSqToCenter(this.targetInventory)) > 1.75D){return false;}
		else{return true;}
	}
	
	@Override
	public boolean shouldExecute() {
		if (this.priority != this.theMechanical.getCurrentTask()){return false;}
		else if (!(this.theMechanical.getTension()-0.15F>0)){return false;}
		else if (this.theMechanical.isWinding == true){return false;}
		else if (this.theMechanical.isWet()){return false;}
		else
		{
			this.runtime = 0;
			return true;	
		}
	}
	@Override
	public boolean continueExecuting()
	{
		if (this.theMechanical.isWinding == true){return false;}
		//if (this.fullyempty == true){return true;}
		else if (this.runtime >= this.maxruntime)
		{
			this.theMechanical.nextTask();
			
			this.runtime = 0;
			return false;
		}
		return true;
	}
	
	@Override
	public void updateTask()
    {
		if (!this.shouldUpdate()){return;}
		IBlockState state = this.theMechanical.getEntityWorld().getBlockState(targetInventory);
		Block block = state.getBlock();
		IInventory InventoryOut = null;
		
		if (block.hasTileEntity(state))
        {
            TileEntity tileentity = this.theMechanical.getEntityWorld().getTileEntity(targetInventory);
            if (tileentity instanceof IInventory)
            {
                InventoryOut = (IInventory)tileentity;

                if (InventoryOut instanceof TileEntityChest && block instanceof BlockChest)
                {
                	InventoryOut = ((BlockChest)block).getContainer(this.theMechanical.getEntityWorld(), targetInventory, true);
                }
            }
        }
		else{this.runtime++; return;}
		if (Math.sqrt(this.theMechanical.getDistanceSqToCenter(this.targetInventory)) > 1.75D){return;}
		else
		{
			IInventory mechainicalInventory = this.theMechanical.getMechanicalInventory();
			int size = this.theMechanical.getMechanicalInventory().getSizeInventory();
			for (int i = 0; i < size; ++i)
            {
                if (!mechainicalInventory.getStackInSlot(i).isEmpty() && this.runtime % this.transferTime == 0)
                {
                    ItemStack itemstack = mechainicalInventory.getStackInSlot(i);
                    Item item = mechainicalInventory.getStackInSlot(i).getItem();
                    if (filter != null){
	                    if (!filter.filterStatisfied(itemstack, InventoryOut, mechainicalInventory))
	                    {
	                    	this.runtime++;
	                    	continue;
	                    }
                    }
                    
                    ItemStack itemstack1 = putStackInInventoryAllSlots(mechainicalInventory, InventoryOut, new ItemStack(item, 1, itemstack.getMetadata()));
                    if (itemstack1.isEmpty())
                    {
                    	itemstack.shrink(1);
                    	this.theMechanical.unwind(0.05F);
                    	this.runtime = 0;
                    	InventoryOut.markDirty();
                        break;
                    }
                }
            }
			this.runtime++;
		}
    }
	private ItemStack putStackInInventoryAllSlots(IInventory mechainicalInventory, IInventory InventoryOut, ItemStack itemStack)
	{
		int i = InventoryOut.getSizeInventory();

        for (int j = 0; j < i && !itemStack.isEmpty(); ++j)
        {
            itemStack = insertStack(mechainicalInventory, InventoryOut, itemStack, j);
        }
		return itemStack;
	}
	private ItemStack insertStack(IInventory mechainicalInventory, IInventory InventoryOut, ItemStack itemStack, int slot)
	{
		ItemStack inventorystack = InventoryOut.getStackInSlot(slot);
		if (inventorystack.isEmpty())
        {
			InventoryOut.setInventorySlotContents(slot, itemStack);
            itemStack = ItemStack.EMPTY;
        }
        else if (canCombine(inventorystack, itemStack))
        {
            int i = itemStack.getMaxStackSize() - inventorystack.getCount();
            int j = Math.min(itemStack.getCount(), i);
            itemStack.shrink(j);
            inventorystack.grow(j);
        }
		
		return itemStack;
	}
	
	private static boolean canCombine(ItemStack stack1, ItemStack stack2)
    {
        return stack1.getItem() != stack2.getItem() ? false : (stack1.getMetadata() != stack2.getMetadata() ? false : (stack1.getCount() > stack1.getMaxStackSize() ? false : ItemStack.areItemStackTagsEqual(stack1, stack2)));
    }
}
