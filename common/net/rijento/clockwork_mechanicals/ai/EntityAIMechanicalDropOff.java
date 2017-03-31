package net.rijento.clockwork_mechanicals.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.rijento.clockwork_mechanicals.entities.EntityMechanicalWorker;
import net.rijento.clockwork_mechanicals.lib.filter.FilterBase;
import net.rijento.clockwork_mechanicals.lib.filter.KeepAmnt;
import net.rijento.clockwork_mechanicals.lib.filter.Whitelist;

public class EntityAIMechanicalDropOff extends EntityAIBase
{
	private final EntityMechanicalWorker theMechanical;
	private final int priority;
	private final BlockPos targetChest;
	private final static int maxruntime = 100;
	private int runtime;
	
	public EntityAIMechanicalDropOff( EntityMechanicalWorker theMechanicalIn, BlockPos chestIn, int priorityIn)
	{
		this.theMechanical = theMechanicalIn;
		this.targetChest = chestIn;
		this.priority = priorityIn;
	}
	@Override
	public boolean shouldExecute() {
		if (this.priority != this.theMechanical.getCurrentTask()){return false;}
		else if (!(this.theMechanical.getTension()-0.25F>0)){return false;}
		else if (this.theMechanical.isWinding == true){return false;}
		else if (this.theMechanical.isWet()){return false;}
		else
		{
			this.runtime = 0;
			return true;}
	}
	@Override
	public boolean continueExecuting()
	{
		if (this.runtime >= this.maxruntime)
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
		Block block = this.theMechanical.getEntityWorld().getBlockState(targetChest).getBlock();
		if (!(block instanceof BlockChest) || this.theMechanical.getDistanceSqToCenter(this.targetChest.up()) > 1.0D){return;}
		else
		{
			IInventory chestInventory = ((BlockChest)block).getContainer(this.theMechanical.getEntityWorld(), this.targetChest, true);
			IInventory mechainicalInventory = this.theMechanical.getMechanicalInventory();
			int size = this.theMechanical.getMechanicalInventory().getSizeInventory();
			for (int i = 0; i < size; ++i)
            {
                if (!mechainicalInventory.getStackInSlot(i).isEmpty() && this.runtime % 8 == 0)
                {
                    ItemStack itemstack = mechainicalInventory.getStackInSlot(i);
                    Item item = mechainicalInventory.getStackInSlot(i).getItem();
                    boolean flag = true;
                    for (FilterBase filter : this.theMechanical.filters)
                    {
                    	if (filter instanceof KeepAmnt)
                    	{
                    		
                    		flag = ((KeepAmnt)filter).filterStatified(itemstack, mechainicalInventory);
                    		//System.out.println(item.toString() + flag);
                    		break;
                    	}
                    	if (!flag){break;}
                    }
                    if (flag)
                    {
	                    ItemStack itemstack1 = putStackInInventoryAllSlots(mechainicalInventory, chestInventory, new ItemStack(item, 1));
	                    
	                    if (itemstack1.isEmpty())
	                    {
	                    	itemstack.shrink(1);
	                    	this.theMechanical.unwind(0.15F);
	                        chestInventory.markDirty();
	                    }
                    }
                }
            }
			this.runtime++;
		}
    }
	private ItemStack putStackInInventoryAllSlots(IInventory mechainicalInventory, IInventory chestInventory, ItemStack itemStack)
	{
		int i = chestInventory.getSizeInventory();

        for (int j = 0; j < i && !itemStack.isEmpty(); ++j)
        {
            itemStack = insertStack(mechainicalInventory, chestInventory, itemStack, j);
        }
		return itemStack;
	}
	private ItemStack insertStack(IInventory mechainicalInventory, IInventory chestInventory, ItemStack itemStack, int slot)
	{
		ItemStack cheststack = chestInventory.getStackInSlot(slot);
		if (cheststack.isEmpty())
        {
            chestInventory.setInventorySlotContents(slot, itemStack);
            itemStack = ItemStack.EMPTY;
        }
        else if (canCombine(cheststack, itemStack))
        {
            int i = itemStack.getMaxStackSize() - cheststack.getCount();
            int j = Math.min(itemStack.getCount(), i);
            itemStack.shrink(j);
            cheststack.grow(j);
        }
		
		return itemStack;
	}
	private static boolean canCombine(ItemStack stack1, ItemStack stack2)
    {
        return stack1.getItem() != stack2.getItem() ? false : (stack1.getMetadata() != stack2.getMetadata() ? false : (stack1.getCount() > stack1.getMaxStackSize() ? false : ItemStack.areItemStackTagsEqual(stack1, stack2)));
    }
}
