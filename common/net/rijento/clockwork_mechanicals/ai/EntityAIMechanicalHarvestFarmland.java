package net.rijento.clockwork_mechanicals.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemSeedFood;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.rijento.clockwork_mechanicals.entities.EntityMechanicalWorker;

public class EntityAIMechanicalHarvestFarmland extends EntityAIBase
{
    private final EntityMechanicalWorker theMechanical;
    private final BlockPos position;
    private boolean hasFarmItem;
    /** Used to determine what the mechanical does: -1 = nothing, 0 = harvest crop, 1 = plant crop */
    private int currentTask;
    /** The "priority" of the task, only executes if the entity's current task matches this number. */
    private final int priority;

    public EntityAIMechanicalHarvestFarmland(EntityMechanicalWorker theMechanicalIn, BlockPos posIn, int priorityIn)
    {
        this.theMechanical = theMechanicalIn;
        this.position = posIn;
        this.priority = priorityIn;
    }
    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldUpdate()
    {
    	if (this.priority != this.theMechanical.getCurrentTask()){return false;}
    	else if (this.theMechanical.isWinding == true){return false;}
    	else if (this.theMechanical.getTension()-0.25F < 0){return false;}
    	else if (this.theMechanical.isWet()){return false;}
    	else if (Math.sqrt(this.theMechanical.getDistanceSqToCenter(this.position.up())) <= 0.7D){return true;}
    	else{return false;}
    }
    
    @Override
    public boolean shouldExecute()
    {
    	if (this.priority != this.theMechanical.getCurrentTask()){return false;}
    	else if (this.theMechanical.isWinding == true){return false;}
    	else if (this.theMechanical.getTension()-0.25F < 0){return false;}
    	else if (this.theMechanical.isWet()){return false;}
    	else if (Math.sqrt(this.theMechanical.getDistanceSqToCenter(this.position.up())) <= 0.7D)
    	{
	    	this.currentTask = -1;
	    	this.determineTask(this.theMechanical.getEntityWorld());
	        this.hasFarmItem = this.isFarmItemInInventory();
	        return true;
    	}
    	else{return false;}
	}

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean shouldContinueExecuting()
    {
    	if (this.theMechanical.isWinding == true){return false;}
    	super.shouldContinueExecuting();
    	return false;
    }

    /**
     * Updates the task
     */
    @Override
    public void updateTask()
    {
    	if (!this.shouldUpdate()){return;}
        World world = this.theMechanical.world;
        BlockPos blockpos = this.position.up();//this.theMechanical.getPosition();
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
                    if (itemstack.getItem() instanceof ItemSeeds)
                    {
                        world.setBlockState(blockpos, ((ItemSeeds)itemstack.getItem()).getPlant(world, blockpos), 3);
                        this.theMechanical.unwind(0.25F);
                        flag = true;
                    }
                    else if (itemstack.getItem() instanceof ItemSeedFood)
                    {
                        world.setBlockState(blockpos, ((ItemSeedFood)itemstack.getItem()).getPlant(world, blockpos), 3);
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
        if (Math.sqrt(this.theMechanical.getDistanceSqToCenter(this.position.up())) <= 0.7D && this.currentTask == -1)
        {
        	this.theMechanical.nextTask();
        }
       determineTask(world);
    }
    
    protected void determineTask(World worldIn)
    {
    	BlockPos pos = this.position;//this.theMechanical.getPosition().down(1);
    	Block block = worldIn.getBlockState(pos).getBlock();
        if (block == Blocks.FARMLAND)
        {
            pos = pos.up();
            IBlockState iblockstate = worldIn.getBlockState(pos);
            block = iblockstate.getBlock();

            if (block instanceof BlockCrops && ((BlockCrops)block).isMaxAge(iblockstate) &&  (this.currentTask == 0 || this.currentTask < 0))
            {
                this.currentTask = 0;
                return;
            }

            if (iblockstate.getMaterial() == Material.AIR && this.hasFarmItem && (this.currentTask == 1 || this.currentTask < 0))
            {
            	this.currentTask = 1;
            	return;
            }
        }
        this.currentTask = -1;
        return;
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