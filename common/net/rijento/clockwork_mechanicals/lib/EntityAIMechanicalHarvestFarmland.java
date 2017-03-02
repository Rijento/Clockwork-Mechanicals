package net.rijento.clockwork_mechanicals.lib;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.rijento.clockwork_mechanicals.entities.EntityMechanicalWorker;

public class EntityAIMechanicalHarvestFarmland extends EntityAIMoveToBlock
{
    private final EntityMechanicalWorker theMechanical;
    private boolean hasFarmItem;
    private int currentTask;

    public EntityAIMechanicalHarvestFarmland(EntityMechanicalWorker theMechanicalIn, double speedIn)
    {
        super(theMechanicalIn, speedIn, 0);
        this.theMechanical = theMechanicalIn;
    }
    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (!(this.theMechanical.getTension()>0))
    	if (this.runDelay <= 0)
        {
            this.currentTask = -1;
            this.hasFarmItem = this.isFarmItemInInventory();
        }

        return super.shouldExecute();
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
    	return false;
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        super.updateTask();
        this.theMechanical.getLookHelper().setLookPosition((double)this.destinationBlock.getX() + 0.5D, (double)(this.destinationBlock.getY() + 1), (double)this.destinationBlock.getZ() + 0.5D, 10.0F, (float)this.theMechanical.getVerticalFaceSpeed());

        if (this.getIsAboveDestination())
        {
            World world = this.theMechanical.world;
            BlockPos blockpos = this.theMechanical.getPosition();
            IBlockState iblockstate = world.getBlockState(blockpos);
            Block block = iblockstate.getBlock();

            if (this.currentTask == 0 && block instanceof BlockCrops && ((BlockCrops)block).isMaxAge(iblockstate))
            {
                world.destroyBlock(blockpos, true);
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
                            flag = true;
                        }
                        else if (itemstack.getItem() == Items.POTATO)
                        {
                            world.setBlockState(blockpos, Blocks.POTATOES.getDefaultState(), 3);
                            flag = true;
                        }
                        else if (itemstack.getItem() == Items.CARROT)
                        {
                            world.setBlockState(blockpos, Blocks.CARROTS.getDefaultState(), 3);
                            flag = true;
                        }
                        else if (itemstack.getItem() == Items.BEETROOT_SEEDS)
                        {
                            world.setBlockState(blockpos, Blocks.BEETROOTS.getDefaultState(), 3);
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
            this.runDelay = 0;
            this.theMechanical.unwind(0.25F);
        }
    }

    /**
     * Return true to set given position as destination
     */
    protected boolean shouldMoveTo(World worldIn, BlockPos pos)
    {
    	pos = this.theMechanical.getPosition().down(1);
    	Block block = worldIn.getBlockState(pos).getBlock();
        System.out.println(this.currentTask);
        if (block == Blocks.FARMLAND)
        {
            pos = pos.up();
            System.out.println(pos);
            IBlockState iblockstate = worldIn.getBlockState(pos);
            block = iblockstate.getBlock();

            if (block instanceof BlockCrops && ((BlockCrops)block).isMaxAge(iblockstate) &&  (this.currentTask == 0 || this.currentTask < 0))
            {
                this.currentTask = 0;
                return true;
            }

            if (iblockstate.getMaterial() == Material.AIR && this.hasFarmItem && (this.currentTask == 1 || this.currentTask < 0))
            {
            	this.currentTask = 1;
                return true;
            }
        }

        return false;
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