package net.rijento.clockwork_mechanicals.ai;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.rijento.clockwork_mechanicals.entities.EntityMechanicalWorker;

// Attacks any entity within a 3x3 area centered around target, attacks area 3 times then moves to next task.
public class EntityAIMechanicalAttack extends EntityAIBase
{
	private final EntityMechanicalWorker theMechanical;
	private final BlockPos target;
	private final AxisAlignedBB AOE;
	private final int priority;
	private final World world;
	private int runtime = 0;
	
	public EntityAIMechanicalAttack(EntityMechanicalWorker mechanicalIn, BlockPos posIn, int priorityIn)
	{
		this.theMechanical = mechanicalIn;
		this.target = posIn.up();
		this.AOE = new AxisAlignedBB(target).grow(1, 1, 1).offset(0, 1, 0);
		this.priority = priorityIn;
		this.world = mechanicalIn.world;
	}
	
	public boolean shouldUpdate()
	{
		if (this.priority != this.theMechanical.getCurrentTask()){return false;}
		else if (!(this.hasSword())){return false;}
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
		else if (!(this.hasSword())){return false;}
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
		runtime++;
		if (this.runtime % 15 != 0){return;}
		List<EntityLiving> targets = this.getTargets();
		for (EntityLiving target : targets)
		{
			target.attackEntityFrom(DamageSource.causeMobDamage(this.theMechanical), 3 + ((ItemSword)this.getSword().getItem()).getAttackDamage());
			this.damageSword();
		}
		if(!targets.isEmpty()){this.theMechanical.unwind(0.25F);}
		if (this.runtime >= 45)
		{
			this.runtime = 1;
			this.theMechanical.nextTask();
			return;
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
				targets.add((EntityLiving) entity);
			}
		}
		return targets;
	}

	private boolean hasSword()
	{
		for (int i = 0; i < this.theMechanical.getMechanicalInventory().getSizeInventory(); ++i)
		{
			ItemStack itemstack = this.theMechanical.getMechanicalInventory().getStackInSlot(i);
			Item item = itemstack.getItem();

            if (!itemstack.isEmpty() && (item instanceof ItemSword))
            {
            	return true;
            }
        }
		return false;
	}
	
	private ItemStack getSword()
	{
		for (int i = 0; i < this.theMechanical.getMechanicalInventory().getSizeInventory(); ++i)
		{
			ItemStack itemstack = this.theMechanical.getMechanicalInventory().getStackInSlot(i);
			Item item = itemstack.getItem();

            if (!itemstack.isEmpty() && (item instanceof ItemSword))
            {
            	return itemstack;
            }
        }
		return null;
	}
	
	private void damageSword()
	{
		for (int i = 0; i < this.theMechanical.getMechanicalInventory().getSizeInventory(); ++i)
		{
			ItemStack itemstack = this.theMechanical.getMechanicalInventory().getStackInSlot(i);
			Item item = itemstack.getItem();
	
	        if (!itemstack.isEmpty() && (item instanceof ItemSword))
	        {
	        	itemstack.attemptDamageItem(1, this.theMechanical.world.rand, null);
	        	return;
	        }
	    }
	}
	
//	private int getAttackDmamage()
//	{
//		float f = ((ItemSword)this.getSword().getItem()).getAttackDamage();
//		if (f > 1.0F)
//        {
//			ItemStack itemstack = this.getSword();
//            int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, itemstack);
//
//            if (i > 0 && !itemstack.isEmpty())
//            {
//                f += (float)(i * i + 1);
//            }
//        }
//		return (f < 0 ? 0 : f);
//	}
}
