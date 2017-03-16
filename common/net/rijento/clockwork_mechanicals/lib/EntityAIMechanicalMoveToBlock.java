package net.rijento.clockwork_mechanicals.lib;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.rijento.clockwork_mechanicals.entities.EntityMechanicalWorker;

public class EntityAIMechanicalMoveToBlock extends EntityAIBase 
{	
	private final EntityMechanicalWorker theMechanical;
	protected boolean isAboveDestination;
	protected int runDelay;
	private final double movementSpeed;
	protected BlockPos destinationBlock;
    private int timeoutCounter;
    private int maxStayTicks;
    
	public EntityAIMechanicalMoveToBlock(EntityMechanicalWorker theMechanicalIn, double speedIn, BlockPos pos)
	{
		this.destinationBlock = pos;
		this.movementSpeed = speedIn;
		this.theMechanical = theMechanicalIn;
		this.startExecuting();
	}
	
	public boolean shouldExecute()
    {
		if (this.runDelay > 0)
        {
            --this.runDelay;
            return false;
        }
//        if (!(this.theMechanical.getTension()-0.1F>0))
//        {
//            return false;
//        }
        else
        {
        	this.runDelay = 15 * this.theMechanical.orders.size();
        	return true;
        }
    }
	
	public boolean continueExecuting()
    {
		
		if (this.getIsAboveDestination() == true)
		{
			return false;
		}
		else
		{
			return true;
//			return this.timeoutCounter >= -this.maxStayTicks;
		}
    }
	
	public void startExecuting()
    {
        this.theMechanical.getNavigator().tryMoveToXYZ((double)((float)this.destinationBlock.getX()) + 0.5D, (double)(this.destinationBlock.getY() + 1), (double)((float)this.destinationBlock.getZ()) + 0.5D, this.movementSpeed);
        this.timeoutCounter = 0;
        this.maxStayTicks = 100;
    }
	
	public void updateTask()
    {
        if (this.theMechanical.getDistanceSqToCenter(this.destinationBlock.up()) > 0.1D)
        {
            this.isAboveDestination = false;
            ++this.timeoutCounter;

            if (this.timeoutCounter % 40 == 0)
            {
                this.theMechanical.getNavigator().tryMoveToXYZ((double)((float)this.destinationBlock.getX()) + 0.5D, (double)(this.destinationBlock.getY() + 1), (double)((float)this.destinationBlock.getZ()) + 0.5D, this.movementSpeed);
            }
        }
        else
        {
            this.isAboveDestination = true;
            --this.timeoutCounter;
        }
    }
	
	protected boolean getIsAboveDestination()
    {
        return this.isAboveDestination;
    }

	protected boolean shouldMoveTo(World worldIn, BlockPos pos) 
	{
		return true;
	}

}
