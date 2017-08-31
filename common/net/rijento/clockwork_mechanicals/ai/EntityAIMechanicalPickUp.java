package net.rijento.clockwork_mechanicals.ai;

import javax.annotation.Nullable;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.rijento.clockwork_mechanicals.entities.EntityMechanicalWorker;
import net.rijento.clockwork_mechanicals.items.ItemMainspring;
import net.rijento.clockwork_mechanicals.lib.filter.Filter;
import net.rijento.clockwork_mechanicals.lib.filter.KeepAmnt;

public class EntityAIMechanicalPickUp extends EntityAIBase {
	private final EntityMechanicalWorker theMechanical;
	private final int priority;
	private final BlockPos targetInventory;
	private final static int maxruntime = 100;
	private int runtime;
	private boolean fullyFill = true;
	private int transferTime;
	private Filter filter;
	
	public EntityAIMechanicalPickUp( EntityMechanicalWorker theMechanicalIn, BlockPos InventoryIn, int priorityIn, @Nullable Filter filterIn)
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
		else if (!(this.theMechanical.getTension()-0.05F>0)){return false;}
		else if (this.theMechanical.isWinding == true){return false;}
		else if (this.theMechanical.isWet()){return false;}
		else{return true;}
	}
	
	@Override
	public boolean shouldExecute() {
		if (this.priority != this.theMechanical.getCurrentTask()){return false;}
		else if (!(this.theMechanical.getTension()-0.05F>0)){return false;}
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
		if (this.fullyFill == true)
		{
			if (this.checkFull() == true)
			{
				if (this.runtime >= this.maxruntime)
				{
					this.theMechanical.nextTask();
					this.runtime = 0;
					return false;
				}
			}
			else{
				if (this.runtime >= this.maxruntime)
				{
					this.theMechanical.nextTask();
					this.runtime = 0;
					return false;
				}
			}
		}
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
		TileEntity te = this.theMechanical.getEntityWorld().getTileEntity(targetInventory);
		if (!(te instanceof IInventory)){this.runtime++; return;}
		else if (Math.sqrt(this.theMechanical.getDistanceSqToCenter(this.targetInventory)) > 1.75D){return;}
		else
		{
			IInventory InventoryIn = ((IInventory)te);
			IInventory mechainicalInventory = this.theMechanical.getMechanicalInventory();
			int size = InventoryIn.getSizeInventory();
			for (int i = 0; i < size; ++i)
            {
                if (!InventoryIn.getStackInSlot(i).isEmpty() && this.runtime % this.transferTime == 0 && this.runtime != 0)
                {
                    ItemStack itemstack = InventoryIn.getStackInSlot(i);
                    Item item = InventoryIn.getStackInSlot(i).getItem();
                    int flag = 1;
                    if (filter != null){
	                    if (!filter.filterStatisfied(itemstack, mechainicalInventory, InventoryIn))
	                    {
	                    	this.runtime++;
	                    	continue;
	                    }
                    }
                    
                    ItemStack itemstack1 = putStackInInventoryAllSlots(InventoryIn, mechainicalInventory, new ItemStack(item, 1, itemstack.getMetadata()));
                    if (itemstack1.isEmpty())
                    {
                    	itemstack.shrink(1);
                    	this.theMechanical.unwind(0.05F);
                    	InventoryIn.markDirty();
                        mechainicalInventory.markDirty();
                        this.runtime = 0;
                        break;
                    }
                }
            }
			this.runtime++;
		}
    }
	private ItemStack putStackInInventoryAllSlots(IInventory InventoryIn, IInventory  mechainicalInventory, ItemStack itemStack)
	{
		int i =  mechainicalInventory.getSizeInventory();

        for (int j = 0; j < i && !itemStack.isEmpty(); ++j)
        {
            itemStack = insertStack(InventoryIn,  mechainicalInventory, itemStack, j);
        }
		return itemStack;
	}
	private ItemStack insertStack(IInventory InventoryIn, IInventory  mechainicalInventory, ItemStack itemStack, int slot)
	{
		ItemStack mechanicalstack =  mechainicalInventory.getStackInSlot(slot);
		if (mechanicalstack.isEmpty())
        {
			mechainicalInventory.setInventorySlotContents(slot, itemStack);
            itemStack = ItemStack.EMPTY;
        }
        else if (canCombine(mechanicalstack, itemStack))
        {
            int i = itemStack.getMaxStackSize() - mechanicalstack.getCount();
            int j = Math.min(itemStack.getCount(), i);
            itemStack.shrink(j);
            mechanicalstack.grow(j);
        }
		
		return itemStack;
	}
	private static boolean canCombine(ItemStack stack1, ItemStack stack2)
    {
        return stack1.getItem() != stack2.getItem() ? false : (stack1.getMetadata() != stack2.getMetadata() ? false : (stack1.getCount() > stack1.getMaxStackSize() ? false : ItemStack.areItemStackTagsEqual(stack1, stack2)));
    }
	
	private boolean checkFull()
	{
		for (int i = 0; i < this.theMechanical.getMechanicalInventory().getSizeInventory(); ++i)
		{
			ItemStack itemstack = this.theMechanical.getMechanicalInventory().getStackInSlot(i);
            if (itemstack.isEmpty()){return false;}
            else if (itemstack.getCount() < itemstack.getMaxStackSize()){return false;}
        }
	    return true;
	}
}


