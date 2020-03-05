package net.rijento.clockwork_mechanicals.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.math.BlockPos;
import net.rijento.clockwork_mechanicals.entities.EntityMechanicalWorker;
import net.rijento.clockwork_mechanicals.items.ItemMainspring;

public class EntityAIMechanicalCraft extends EntityAIBase {

	private final EntityMechanicalWorker theMechanical;
	private final BlockPos pos;
	private final int priority;
	private final InventoryCrafting craftMatrix;
	private final ItemStack result;
	protected int runDelay;
	private final static int maxruntime = 100;
	private int runtime;
	
	public EntityAIMechanicalCraft(EntityMechanicalWorker mechanicalIn, int priorityIn, InventoryCrafting matrixIn, BlockPos posin)
	{
		this.theMechanical = mechanicalIn;
		this.priority = priorityIn;
		this.craftMatrix = matrixIn;
		this.pos = posin;
		this.result = CraftingManager.findMatchingResult(craftMatrix, this.theMechanical.getEntityWorld());
		this.runtime = 0;
		if (!this.theMechanical.world.isRemote && this.theMechanical.hasMainspring())
		{
			this.runDelay = (int) (10F / ItemMainspring.getResistance(this.theMechanical.getMainspring().getItemDamage()));
		}
	}
	
	public boolean shouldUpdate()
	{
		if (this.priority != this.theMechanical.getCurrentTask()){return false;}
		else if (Math.sqrt(this.theMechanical.getDistanceSqToCenter(this.pos)) > 1.75D){return false;}
		else if (!(this.theMechanical.getTension()-0.25F>0)){return false;}
		else if (this.theMechanical.isWinding == true){return false;}
		else if (this.theMechanical.isWet()){return false;}
		else if (this.runtime > this.maxruntime){return false;}
        else{return true;}
	}
	
	@Override
	public boolean shouldExecute() {
		if (this.priority != this.theMechanical.getCurrentTask()){return false;}
		else if (!(this.theMechanical.getTension()-0.25F>0)){return false;}
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
        	return true;
        }
	}
	
	@Override
	public boolean shouldContinueExecuting()
	{
		if (this.theMechanical.isWinding == true){return false;}
		else if (this.runtime > this.maxruntime){this.theMechanical.nextTask(); this.runtime = 0; return false;}
		else {return true;}
	}
	
	public void updateTask()
    {
		if (!this.shouldUpdate()){return;}
		this.attemptCraft();
		runtime++;
    }
	
	public boolean attemptCraft()
	{
		// TODO: check has ingredients
		if (this.theMechanical.getMechanicalInventory().isEmpty()){return false;}
		InventoryBasic ingredients = copy(this.theMechanical.getMechanicalInventory());
		
		for (int i = 0; i < 9; i++)
		{
			boolean flag = false;
			ItemStack test1 = this.craftMatrix.getStackInSlot(i);
			if (test1.isEmpty()){continue;}
			for (int j = 0; j < this.theMechanical.getMechanicalInventory().getSizeInventory(); j++)
			{
				ItemStack test2 = ingredients.getStackInSlot(j);
				if (test2.isEmpty()){continue;}
				
				if ((test1.getItem() != test2.getItem() ? false : (test1.getMetadata() != test2.getMetadata() ? false : ItemStack.areItemStackTagsEqual(test1, test2))))
				{
					ingredients.decrStackSize(j, 1);
					flag = true;
					break;
				}
			}
			if (!flag){return false;}
		}
		this.theMechanical.overrideMechanicalInventory(ingredients);
		this.theMechanical.getMechanicalInventory().addItem(result);
		this.theMechanical.unwind(0.25f);
		
		return true;
	}

	private InventoryBasic copy(InventoryBasic inventory) {
		InventoryBasic ret = new InventoryBasic(inventory.getName(), inventory.hasCustomName(), inventory.getSizeInventory());
		for (int i = 0; i < inventory.getSizeInventory(); i++)
		{
			ret.setInventorySlotContents(i, inventory.getStackInSlot(i).copy());
		}
		return ret;
	}

}
