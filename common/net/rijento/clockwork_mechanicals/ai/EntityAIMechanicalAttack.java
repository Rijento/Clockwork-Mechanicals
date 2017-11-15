package net.rijento.clockwork_mechanicals.ai;

import java.util.ArrayList;
import java.util.List;

import net.java.games.input.Component.Identifier.Axis;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.rijento.clockwork_mechanicals.entities.EntityMechanicalWorker;

// Attacks any entity within a 3x3 area centered around target
public class EntityAIMechanicalAttack extends EntityAIBase
{
	private final EntityMechanicalWorker theMechanical;
	private final BlockPos target;
	private final AxisAlignedBB AOE;
	private final int priority;
	private final World world;
	
	public EntityAIMechanicalAttack(EntityMechanicalWorker mechanicalIn, BlockPos posIn, int priorityIn)
	{
		this.theMechanical = mechanicalIn;
		this.target = posIn.up();
		this.AOE = new AxisAlignedBB(target);
		this.AOE.expand(1, 0, 1);
		this.AOE.setMaxY(this.target.getY()+2);
		this.priority = priorityIn;
		this.world = mechanicalIn.world;
	}
	
	public boolean shouldUpdate()
	{
		if (this.priority != this.theMechanical.getCurrentTask()){return false;}
		else if (!(this.theMechanical.getTension()-0.75F>0)){return false;}
		else if (this.theMechanical.isWinding == true){return false;}
		else if (this.theMechanical.isWet()){return false;}
		else if (Math.sqrt(this.theMechanical.getDistanceSqToCenter(this.target)) >= 1.0D){return false;}
		else{return true;}
	}
	
	@Override
	public boolean shouldExecute()
	{
		if (this.priority != this.theMechanical.getCurrentTask()){return false;}
		else if (!(this.theMechanical.getTension()-0.75F>0)){return false;}
		else if (this.theMechanical.isWinding == true){return false;}
		else if (this.theMechanical.isWet()){return false;}
		else {return true;}
	}

	@Override
	public boolean shouldContinueExecuting()
	{
		if (this.theMechanical.isWinding == true){return false;}
		else {return true;}
	}
	
	@Override
	public void updateTask()
	{
		if (!shouldUpdate()) {return;}
		for (EntityLiving target : getTargets())
		{
//			target.attackEntityFrom(source, amount)
		}
	}
	
	public List<EntityLiving> getTargets()
	{
		List<EntityLiving> targets = new ArrayList<EntityLiving>();
		List<Entity> all = world.getEntitiesWithinAABBExcludingEntity(this.theMechanical, this.AOE);
		for (Entity entity : all)
		{
			if (entity instanceof EntityLiving)
			{
				System.out.println(entity.getName()); //TODO Remove after testing
				targets.add((EntityLiving) entity);
			}
		}
		return targets;
	}

}
