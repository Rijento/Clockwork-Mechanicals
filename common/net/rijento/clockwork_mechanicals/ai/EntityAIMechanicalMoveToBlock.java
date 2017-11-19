package net.rijento.clockwork_mechanicals.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.rijento.clockwork_mechanicals.entities.EntityMechanicalWorker;

public class EntityAIMechanicalMoveToBlock extends EntityAIBase 
{	
	private final EntityMechanicalWorker theMechanical;
	private final int priority;
	protected boolean isAboveDestination;
	protected int runDelay;
	private final double movementSpeed;
	protected BlockPos destinationBlock;
    private int timeoutCounter;
    
	public EntityAIMechanicalMoveToBlock(EntityMechanicalWorker theMechanicalIn, double speedIn, BlockPos pos, int priorityIn)
	{
		this.destinationBlock = pos;
		this.priority = priorityIn;
		this.movementSpeed = speedIn;
		this.theMechanical = theMechanicalIn;
		this.runDelay = 20;
	}
	
	public boolean shouldUpdate()
	{
		if (this.priority != this.theMechanical.getCurrentTask()){return false;}
		else if (!(this.theMechanical.getTension()-0.1F>0)){return false;}
		else if (this.theMechanical.isWinding == true){return false;}
		else if (this.theMechanical.isWet()){return false;}
        else{return true;}
	}
	
	@Override
	public boolean shouldExecute()
    {
		if (this.priority != this.theMechanical.getCurrentTask()){return false;}
		else if (!(this.theMechanical.getTension()-0.1F>0)){return false;}
		else if (this.theMechanical.isWinding == true){return false;}
		else if (this.theMechanical.isWet()){return false;}
		else if (this.runDelay > 0)
		{
			--this.runDelay;
			return false;
		}
        else
        {
        	this.runDelay = 20;
        	this.timeoutCounter = 0;
        	return true;
        }
    }
	
	@Override
	public boolean shouldContinueExecuting()
    {
		if (this.theMechanical.isWinding == true){return false;}
		if (this.getIsAboveDestination() == true)
		{
			return false;
		}
		else
		{
			return true;
		}
    }
	
	@Override
	public void startExecuting()
    {
        this.theMechanical.getNavigator().tryMoveToXYZ((double)((float)this.destinationBlock.getX()), (double)(this.destinationBlock.getY() + 1), (double)((float)this.destinationBlock.getZ()), this.movementSpeed);
        this.timeoutCounter = 0;
        this.runDelay = 20;
    }
	
	@Override
	public void updateTask()
    {
		if (!this.shouldUpdate()){return;}
        if (Math.sqrt(this.theMechanical.getDistanceSqToCenter(this.destinationBlock)) > 0.75D)
        {
            this.isAboveDestination = false;
            ++this.timeoutCounter;

            this.theMechanical.getNavigator().tryMoveToXYZ((double)((float)this.destinationBlock.getX()), (double)(this.destinationBlock.getY()), (double)((float)this.destinationBlock.getZ()), this.movementSpeed);
            
            if ((this.timeoutCounter < 10))
            {
            	this.theMechanical.getNavigator().clearPath();
            	
            }
            if (this.timeoutCounter > 20)
            {
            	this.timeoutCounter = 0;
            }
       
        }
        else
        {
            this.isAboveDestination = true;
            //this.theMechanical.nextTask();
            this.timeoutCounter = 0;
            
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
