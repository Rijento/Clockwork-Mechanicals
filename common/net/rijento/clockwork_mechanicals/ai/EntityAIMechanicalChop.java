package net.rijento.clockwork_mechanicals.ai;

import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.rijento.clockwork_mechanicals.entities.EntityMechanicalWorker;

public class EntityAIMechanicalChop extends EntityAIBase
{
	private final EntityMechanicalWorker theMechanical;
	private final BlockPos target;
	private final int priority;
	private int currentTask;
	private int ticks;
	
	public EntityAIMechanicalChop(EntityMechanicalWorker workerIn, BlockPos posIn, int priorityIn)
	{
		this.theMechanical = workerIn;
		this.target = posIn.up();
		this.priority = priorityIn;
		this.currentTask = -1;
		this.ticks = -1;
	}
	
	@Override
	public boolean shouldExecute()
	{
		if (this.priority != this.theMechanical.getCurrentTask()){return false;}
		else if (!(this.theMechanical.getTension()-0.5F>0)){return false;}
		else if (this.theMechanical.isWinding == true){return false;}
		else if (this.theMechanical.isWet()){return false;}
		else if (this.theMechanical.world.getBlockState(target).getBlock() == Blocks.AIR && this.theMechanical.getDistanceSqToCenter(this.target) <= 1D){return this.hasSaplings();}
		else if (this.theMechanical.world.getBlockState(target).getBlock() instanceof BlockLog && this.theMechanical.getDistanceSqToCenter(this.target) <= 2D)
		{
			this.currentTask = 1;
			if (this.ticks < 0){this.ticks = 100;}
			return true;
		}
		else if (this.ticks >= 0){return true;}
		else if (this.theMechanical.getDistanceSqToCenter(this.target) <= 1.0D){this.theMechanical.nextTask(); return false;}
		else{return false;}
	}
	
	private boolean hasSaplings() 
	{
		for (int i = 0; i < this.theMechanical.getMechanicalInventory().getSizeInventory(); ++i)
		{
			ItemStack itemstack = this.theMechanical.getMechanicalInventory().getStackInSlot(i);

            if (!itemstack.isEmpty() && (ItemStack.areItemStackTagsEqual(itemstack, new ItemStack(Blocks.SAPLING))))
            {
            	this.currentTask = 0;
                return true;
            }
        }
		if (this.theMechanical.getDistanceSqToCenter(this.target) <= 0.6D){this.theMechanical.nextTask();}
	    return false;
	}
	private ItemStack getSaplings()
	{
		for (int i = 0; i < this.theMechanical.getMechanicalInventory().getSizeInventory(); ++i)
		{
			ItemStack itemstack = this.theMechanical.getMechanicalInventory().getStackInSlot(i);

            if (!itemstack.isEmpty() && (ItemStack.areItemStackTagsEqual(itemstack, new ItemStack(Blocks.SAPLING))))
            {
            	return this.theMechanical.getMechanicalInventory().decrStackSize(i, 1);
            }
        }
		return new ItemStack(Items.AIR);
	}
	

	public void updateTask()
    {
		if (this.currentTask == 0)
		{
			this.theMechanical.world.setBlockState(target, Blocks.SAPLING.getDefaultState().withProperty(PropertyEnum.<BlockPlanks.EnumType>create("type", BlockPlanks.EnumType.class), BlockPlanks.EnumType.byMetadata(this.getSaplings().getMetadata() & 7)));
		}
		else
		{
			if (this.ticks % 20 == 0)
			{
				this.theMechanical.world.destroyBlock(target.up(this.ticks/20), true);
				this.theMechanical.unwind(0.5f);
			}
			--this.ticks;
			return;
		}
		this.currentTask = -1;
    }
	
	

}
