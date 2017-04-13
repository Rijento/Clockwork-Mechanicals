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

public class EntityAIMechanicalDropOff extends EntityAIBase
{
	private final EntityMechanicalWorker theMechanical;
	private final int priority;
	private final BlockPos targetInventory;
	private final static int maxruntime = 100;
	private int runtime;
	private boolean fullyempty = true;
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
		if (this.fullyempty == true)
		{
			if (this.checkEmpty() == true)
			{
				if (this.runtime >= this.maxruntime)
				{
					this.theMechanical.nextTask();
					this.runtime = 0;
					return false;
				}
			}
			else{return true;}
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
		TileEntity te = this.theMechanical.getEntityWorld().getTileEntity(targetInventory);
		if (this.theMechanical.getDistanceSqToCenter(this.targetInventory) > 3.0D){return;}
		else if (!(te instanceof IInventory)){this.runtime++; return;}
		else
		{
			IInventory InventoryOut = ((IInventory)te);
			IInventory mechainicalInventory = this.theMechanical.getMechanicalInventory();
			int size = this.theMechanical.getMechanicalInventory().getSizeInventory();
			for (int i = 0; i < size; ++i)
            {
                if (!mechainicalInventory.getStackInSlot(i).isEmpty() && this.runtime % this.transferTime == 0)
                {
                    ItemStack itemstack = mechainicalInventory.getStackInSlot(i);
                    Item item = mechainicalInventory.getStackInSlot(i).getItem();
                    if (filter != null){
	                    if (!filter.filterStatisfied(itemstack))
	                    {
	                    	this.runtime++;
	                    	continue;
	                    }
                    }
                    int flag = 1;
                    for (KeepAmnt filter : this.theMechanical.filters)
                    {
                    	flag = filter.filterStatified(itemstack, mechainicalInventory);
                    	if (flag == 0){break;}
                    }
                    if (!(flag == 0))
                    {
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
	
	private boolean checkEmpty()
	{
		if (this.theMechanical.getMechanicalInventory().isEmpty()){return true;}
		if (this.theMechanical.filters.isEmpty())
		{
			return this.theMechanical.getMechanicalInventory().isEmpty();
		}
		else
		{
			IInventory mechainicalInventory = this.theMechanical.getMechanicalInventory();
			int size = this.theMechanical.getMechanicalInventory().getSizeInventory();
			for (int i = 0; i < size; ++i)
            {
                if (!mechainicalInventory.getStackInSlot(i).isEmpty())
                {
                    ItemStack itemstack = mechainicalInventory.getStackInSlot(i);
                    int flag = 1;
                    for (KeepAmnt filter : this.theMechanical.filters)
                    {
                    	flag = filter.filterStatified(itemstack, mechainicalInventory);
                    	if (flag == 1){return false;}
                    }
                    if (flag == -1)
                    {
                    	return false;
                    }
                }
            }
			return true;
		}
	}
}
