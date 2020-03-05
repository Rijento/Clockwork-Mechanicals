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
import net.rijento.clockwork_mechanicals.items.ItemMainspring;

public class EntityAIMechanicalChop extends EntityAIBase
{
	private final EntityMechanicalWorker theMechanical;
	private final BlockPos target;
	private final int priority;
	private int currentTask;
	private int ticks;
	private int workTime;
	
	public EntityAIMechanicalChop(EntityMechanicalWorker workerIn, BlockPos posIn, int priorityIn)
	{
		this.theMechanical = workerIn;
		this.target = posIn.up();
		this.priority = priorityIn;
		this.currentTask = -1;
		this.ticks = -1;
		if (!this.theMechanical.world.isRemote && this.theMechanical.hasMainspring())
		{
			this.workTime = (int)(30 / ItemMainspring.getResistance(this.theMechanical.getMainspring().getItemDamage()));
		}
	}
	
	public boolean shouldUpdate()
	{
		if (this.priority != this.theMechanical.getCurrentTask()){return false;}
		else if (!(this.theMechanical.getTension()-0.75F>0)){return false;}
		else if (this.theMechanical.isWinding == true){return false;}
		else if (this.theMechanical.isWet()){return false;}
		else if (this.theMechanical.world.getBlockState(target).getBlock() == Blocks.AIR && Math.sqrt(this.theMechanical.getDistanceSqToCenter(this.target)) <= 1D){return this.hasSaplings();}
		else if (this.theMechanical.world.getBlockState(target).getBlock() instanceof BlockLog && Math.sqrt(this.theMechanical.getDistanceSqToCenter(this.target)) <= 2D){return true;}
		else if (this.ticks >= 0){return true;}
		else if (this.theMechanical.getDistanceSqToCenter(this.target) <= 1.0D){return false;}
		else{return false;}
	}
	
	@Override
	public boolean shouldExecute()
	{
		if (this.priority != this.theMechanical.getCurrentTask()){return false;}
		else if (!(this.theMechanical.getTension()-0.75F>0)){return false;}
		else if (this.theMechanical.isWinding == true){return false;}
		else if (this.theMechanical.isWet()){return false;}
		else if (this.theMechanical.world.getBlockState(target).getBlock() == Blocks.AIR && Math.sqrt(this.theMechanical.getDistanceSqToCenter(this.target)) <= 1D){return this.hasSaplings();}
		else if (this.theMechanical.world.getBlockState(target).getBlock() instanceof BlockLog && Math.sqrt(this.theMechanical.getDistanceSqToCenter(this.target)) <= 2D)
		{
			this.currentTask = 1;
			if (this.ticks < 0){this.ticks = this.workTime * 5;}
			return true;
		}
		else if (this.ticks >= 0){return true;}
		else if (this.theMechanical.getDistanceSqToCenter(this.target) <= 1.0D){this.theMechanical.nextTask(); return false;}
		else{return false;}
	}
	
	@Override
	public boolean shouldContinueExecuting()
	{
		if (this.theMechanical.isWinding == true){return false;}
		else {return true;}
	}
	
	private boolean hasSaplings() 
	{
		for (int i = 0; i < this.theMechanical.getMechanicalInventory().getSizeInventory(); ++i)
		{
			ItemStack itemstack = this.theMechanical.getMechanicalInventory().getStackInSlot(i);

            if (!itemstack.isEmpty() && (ItemStack.areItemsEqualIgnoreDurability(itemstack, new ItemStack(Blocks.SAPLING))))
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

            if (!itemstack.isEmpty() && (ItemStack.areItemsEqualIgnoreDurability(itemstack, new ItemStack(Blocks.SAPLING))))
            {
            	return this.theMechanical.getMechanicalInventory().decrStackSize(i, 1);
            }
        }
		return new ItemStack(Items.AIR);
	}
	

	public void updateTask()
    {
		if (!this.shouldUpdate()){return;}
		if (this.currentTask == 0)
		{
			this.theMechanical.unwind(0.25F);
			this.theMechanical.world.setBlockState(target, Blocks.SAPLING.getDefaultState().withProperty(PropertyEnum.<BlockPlanks.EnumType>create("type", BlockPlanks.EnumType.class), BlockPlanks.EnumType.byMetadata(this.getSaplings().getMetadata() & 7)));
		}
		else
		{
			if (this.ticks % workTime == 0)
			{
				this.theMechanical.world.destroyBlock(target.up(this.ticks/workTime), true);
				this.theMechanical.unwind(0.5f);
			}
			--this.ticks;
			return;
		}
		this.currentTask = -1;
    }
	
	

}
