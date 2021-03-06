package net.rijento.clockwork_mechanicals.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.rijento.clockwork_mechanicals.entities.EntityMechanicalWorker;

public class EntityAIMechanicalWait extends EntityAIBase
{
	 /** The entity that is looking idle. */
    private final EntityMechanicalWorker idleEntity;
    /** An integer used to reset the idle time to the desired value given on construction. */
    private int idleTimeBase;
    /** A decrementing tick that stops the entity from being idle once it reaches 0. */
    private int idleTime;
    /** The "priority" of the task, only executes if the entity's current task matches this number. */
    private final int priority;

    public EntityAIMechanicalWait(EntityMechanicalWorker theMechanicalIn, int timeIn, int priorityIn)
    {
        this.idleEntity = theMechanicalIn;
        this.idleTimeBase = timeIn;
        this.priority = priorityIn;
        this.setMutexBits(3);
    }
    
    public boolean shouldUpdate()
    {
    	if (this.idleEntity.isWinding == true){return false;}
    	else if (this.idleEntity.isWet()){return false;}
    	else if (this.priority == this.idleEntity.getCurrentTask()){return true;}
    	else{return false;}
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
    	if (this.idleEntity.isWinding == true){return false;}
    	else if (this.idleEntity.isWet()){return false;}
    	else if (this.priority == this.idleEntity.getCurrentTask())
    	{
    		this.idleTime = this.idleTimeBase;
    		return true;
    	}
    	else{this.idleEntity.nextTask(); return false;}
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
    	return this.idleTime >= 0;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting(){}

    /**
     * Updates the task
     */
    public void updateTask()
    {
    	if(!this.shouldUpdate()){return;}
    	if (this.priority != this.idleEntity.getCurrentTask()){return;}
        --this.idleTime;
    }
}