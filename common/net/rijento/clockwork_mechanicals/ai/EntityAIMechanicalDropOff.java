package net.rijento.clockwork_mechanicals.ai;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.rijento.clockwork_mechanicals.entities.EntityMechanicalWorker;

public class EntityAIMechanicalDropOff extends EntityAIBase
{
	private final EntityMechanicalWorker theMechanical;
	private final int priority;
	private final BlockPos targetChest;
	
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
		else{return true;}
	}
	@Override
	public boolean continueExecuting()
	{
		return true;
	}
	
	@Override
	public void updateTask()
    {
		Block block = this.theMechanical.getEntityWorld().getBlockState(targetChest).getBlock();
		if (!(block instanceof BlockChest)){return;}
		else
		{
			IInventory chestInventory = ((BlockChest)block).getContainer(this.theMechanical.getEntityWorld(), this.targetChest, true);
			IInventory mechainicalInventory = this.theMechanical.getMechanicalInventory();
			int size = this.theMechanical.getMechanicalInventory().getSizeInventory();
			for (int i = 0; i < size; ++i)
            {
                if (!mechainicalInventory.getStackInSlot(i).isEmpty())
                {
                    ItemStack itemstack = mechainicalInventory.getStackInSlot(i).copy();
                    ItemStack itemstack1 = putStackInInventoryAllSlots(this, iinventory, ItemStackHelper.getAndSplit((List<ItemStack>) mechainicalInventory, i, 1));

                    if (itemstack1.isEmpty())
                    {
                        chestInventory.markDirty();
                        return true;
                    }

                    mechainicalInventory.setInventorySlotContents(i, itemstack);
                }
            }
		}
    }
}
