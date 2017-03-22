package net.rijento.clockwork_mechanicals.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.rijento.clockwork_mechanicals.entities.EntityMechanicalWorker;

public class EntityAIMechanicalHarvestFarmland extends EntityAIBase
{
    private final EntityMechanicalWorker theMechanical;
    private boolean hasFarmItem;
    /** Used to determine what the mechanical does: -1 = nothing, 0 = harvest crop, 1 = plant crop */
    private int currentTask;
    /** The "priority" of the task, only executes if the entity's current task matches this number. */
    private final int priority;

    public EntityAIMechanicalHarvestFarmland(EntityMechanicalWorker theMechanicalIn, int priorityIn)
    {
        this.theMechanical = theMechanicalIn;
        this.priority = priorityIn;
    }
    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
    	if (this.priority != this.theMechanical.getCurrentTask()){return false;}
    	else if (this.theMechanical.isWinding == true){return false;}
    	else if (this.theMechanical.getTension()-0.25F < 0){return false;}
    	else if (this.theMechanical.isWet()){return false;}
    	else
    	{
	    	this.currentTask = -1;
	    	this.determineTask(this.theMechanical.getEntityWorld());
	        this.hasFarmItem = this.isFarmItemInInventory();
	        return true;
    	}
	}

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
    	super.continueExecuting();
    	return false;
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        World world = this.theMechanical.world;
        BlockPos blockpos = this.theMechanical.getPosition();
        IBlockState iblockstate = world.getBlockState(blockpos);
        Block block = iblockstate.getBlock();

        if (this.currentTask == 0 && block instanceof BlockCrops && ((BlockCrops)block).isMaxAge(iblockstate))
        {
            world.destroyBlock(blockpos, true);
            this.theMechanical.unwind(0.25F);
        }
        else if (this.currentTask == 1 && iblockstate.getMaterial() == Material.AIR)
        {
            InventoryBasic inventorybasic = this.theMechanical.getMechanicalInventory();

            for (int i = 0; i < inventorybasic.getSizeInventory(); ++i)
            {
                ItemStack itemstack = inventorybasic.getStackInSlot(i);
                boolean flag = false;

                if (!itemstack.isEmpty())
                {
                    if (itemstack.getItem() == Items.WHEAT_SEEDS)
                    {
                        world.setBlockState(blockpos, Blocks.WHEAT.getDefaultState(), 3);
                        this.theMechanical.unwind(0.25F);
                        flag = true;
                    }
                    else if (itemstack.getItem() == Items.POTATO)
                    {
                        world.setBlockState(blockpos, Blocks.POTATOES.getDefaultState(), 3);
                        this.theMechanical.unwind(0.25F);
                        flag = true;
                    }
                    else if (itemstack.getItem() == Items.CARROT)
                    {
                        world.setBlockState(blockpos, Blocks.CARROTS.getDefaultState(), 3);
                        this.theMechanical.unwind(0.25F);
                        flag = true;
                    }
                    else if (itemstack.getItem() == Items.BEETROOT_SEEDS)
                    {
                        world.setBlockState(blockpos, Blocks.BEETROOTS.getDefaultState(), 3);
                        this.theMechanical.unwind(0.25F);
                        flag = true;
                    }
                }

                if (flag)
                {
                    itemstack.shrink(1);

                    if (itemstack.isEmpty())
                    {
                        inventorybasic.setInventorySlotContents(i, ItemStack.EMPTY);
                    }

                    break;
                }
            }
        }
        this.currentTask = -1;
    }
    
    protected void determineTask(World worldIn)
    {
    	BlockPos pos = this.theMechanical.getPosition().down(1);
    	Block block = worldIn.getBlockState(pos).getBlock();
        if (block == Blocks.FARMLAND)
        {
            pos = pos.up();
            IBlockState iblockstate = worldIn.getBlockState(pos);
            block = iblockstate.getBlock();

            if (block instanceof BlockCrops && ((BlockCrops)block).isMaxAge(iblockstate) &&  (this.currentTask == 0 || this.currentTask < 0))
            {
                this.currentTask = 0;
            }

            if (iblockstate.getMaterial() == Material.AIR && this.hasFarmItem && (this.currentTask == 1 || this.currentTask < 0))
            {
            	this.currentTask = 1;
            }
        }
    }
    public boolean isFarmItemInInventory()
    {
        for (int i = 0; i < this.theMechanical.getMechanicalInventory().getSizeInventory(); ++i)
        {
            ItemStack itemstack = this.theMechanical.getMechanicalInventory().getStackInSlot(i);

            if (!itemstack.isEmpty() && (itemstack.getItem() == Items.WHEAT_SEEDS || itemstack.getItem() == Items.POTATO || itemstack.getItem() == Items.CARROT || itemstack.getItem() == Items.BEETROOT_SEEDS))
            {
                return true;
            }
        }

        return false;
    }
}