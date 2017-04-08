package net.rijento.clockwork_mechanicals.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.rijento.clockwork_mechanicals.entities.EntityMechanicalWorker;
import net.rijento.clockwork_mechanicals.items.ItemMainspring;

public class EntityAIMechanicalMine extends EntityAIBase 
{
	private final boolean returnsWhenLow;
	private final EntityMechanicalWorker theMechanical;
	private final BlockPos start;
	private final EnumFacing direction;
	private final int priority;
	private int runtime;
	private int breaktime;
	private int runtest;
	
	
	public EntityAIMechanicalMine( EntityMechanicalWorker theMechanicalIn, BlockPos posIn, EnumFacing directionIn, boolean returnwhenlow, int priorityIn)
	{
		this.theMechanical = theMechanicalIn;
		this.start = posIn;
		this.direction = directionIn;
		this.returnsWhenLow = returnwhenlow;
		this.priority = priorityIn;
		this.runtime = 1;
		if (!this.theMechanical.world.isRemote)
		{
			this.runtest = (int)(50 / ItemMainspring.getResistance(this.theMechanical.getMainspring().getItemDamage()));
		}
	}
	@Override
	public boolean shouldExecute() 
	{
		if (this.priority != this.theMechanical.getCurrentTask()){return false;}
		else if (!(this.theMechanical.getTension()-0.75F>0)){return false;}
		else if (this.theMechanical.isWinding == true){return false;}
		else if (this.theMechanical.isWet()){return false;}
		else if (this.theMechanical.isEntityInsideOpaqueBlock()){return false;}
		else if (this.checkFull()){this.theMechanical.nextTask(); return false;}
		else if (this.returnsWhenLow)
		{
			if (this.shouldReturn())
			{
				this.theMechanical.nextTask();
				return false;
			}
			else
			{
				if (this.runtime > this.runtest){this.runtime = 1;}
				return true;
			}
		}
		else
		{
			if (this.runtime > this.runtest){this.runtime = 1;}
			return true;
		}
	}
	@Override
	public boolean continueExecuting()
	{
		if (this.theMechanical.isWinding == true){return false;}
		else {return true;}
	}
	
	
	@Override
	public void updateTask()
    {
		if (this.tunnel())
		{
			this.torch();
			BlockPos pos = this.theMechanical.getPosition().offset(this.direction);
			this.theMechanical.getNavigator().tryMoveToXYZ(pos.getX(), pos.getY(), pos.getZ(), this.theMechanical.getAIMoveSpeed());
		}
		this.runtime++;
    }
	
	 /**
     * Breaks two blocks to make a tunnel
     */
	private boolean tunnel()
	{
		World world = this.theMechanical.getEntityWorld();
		BlockPos pos = this.theMechanical.getPosition();
		BlockPos topBreak = pos.offset(this.direction).up();
		BlockPos botBreak = pos.offset(this.direction);
		Boolean toptest = world.getBlockState(topBreak).getBlock().isBlockSolid(world, topBreak, this.direction.getOpposite()) && !(world.getBlockState(topBreak).getBlock() == Blocks.BEDROCK) && !(world.getBlockState(topBreak).getBlock() == Blocks.OBSIDIAN);
		Boolean bottest = world.getBlockState(botBreak).getBlock().isBlockSolid(world, botBreak, this.direction.getOpposite()) && !(world.getBlockState(topBreak).getBlock() == Blocks.BEDROCK) && !(world.getBlockState(topBreak).getBlock() == Blocks.OBSIDIAN);
		if (toptest)
		{
			if (this.runtime % this.runtest == 0){
				world.destroyBlock(topBreak, true);
				this.theMechanical.unwind(0.5f);
			}
			return false;
		}
		else if (bottest && !(world.getBlockState(botBreak).getBlock() instanceof BlockTorch))
		{
			if (this.runtime % this.runtest == 0)
			{
				world.destroyBlock(botBreak, true);
				this.theMechanical.unwind(0.5f);
				return true;
			}
			return false;
		}
		else if (this.runtime % 15 == 0){runtime = 1; return true;}
	
		return false;
	}
	
	/**
     * places a torch every 8 blocks
     */
	private void torch()
	{
		World world = this.theMechanical.getEntityWorld();
		BlockPos pos = this.theMechanical.getPosition();
		double blocks = Math.floor(Math.sqrt(this.theMechanical.getDistanceSq(this.start)));
		if (blocks % 8 == 0 && this.hasTorch() && world.getBlockState(pos.down()).getBlock().canPlaceTorchOnTop(world.getBlockState(pos.down()), world, pos))
		{
			this.getTorch();
			this.theMechanical.unwind(0.25F);
			world.setBlockState(pos, Blocks.TORCH.getDefaultState());
		}
	}
	private boolean hasTorch()
	{
		for (int i = 0; i < this.theMechanical.getMechanicalInventory().getSizeInventory(); ++i)
		{
			ItemStack itemstack = this.theMechanical.getMechanicalInventory().getStackInSlot(i);

            if (!itemstack.isEmpty() && (ItemStack.areItemsEqual(itemstack, new ItemStack(Blocks.TORCH))))
            {
                return true;
            }
        }
	    return false;
	}
	
	private boolean hasPickaxe()
	{
		for (int i = 0; i < this.theMechanical.getMechanicalInventory().getSizeInventory(); ++i)
		{
			ItemStack itemstack = this.theMechanical.getMechanicalInventory().getStackInSlot(i);
			Item item = itemstack.getItem();

            if (!itemstack.isEmpty() && (item instanceof ItemPickaxe))
            {
            	return true;
            }
        }
		return false;
	}
	private void damagePickaxe()
	{
		for (int i = 0; i < this.theMechanical.getMechanicalInventory().getSizeInventory(); ++i)
		{
			ItemStack itemstack = this.theMechanical.getMechanicalInventory().getStackInSlot(i);
			Item item = itemstack.getItem();
	
	        if (!itemstack.isEmpty() && (item instanceof ItemPickaxe))
	        {
	        	item.setDamage(itemstack, item.getDamage(itemstack)+1);
	        	return;
	        }
	    }
	}
	
	private ItemStack getTorch()
	{
		for (int i = 0; i < this.theMechanical.getMechanicalInventory().getSizeInventory(); ++i)
		{
			ItemStack itemstack = this.theMechanical.getMechanicalInventory().getStackInSlot(i);

            if (!itemstack.isEmpty() && (ItemStack.areItemsEqual(itemstack, new ItemStack(Blocks.TORCH))))
            {
            	return this.theMechanical.getMechanicalInventory().decrStackSize(i, 1);
            }
        }
		return new ItemStack(Items.AIR);
	}
	private boolean checkFull()
	{
		for (int i = 0; i < this.theMechanical.getMechanicalInventory().getSizeInventory(); ++i)
		{
			ItemStack itemstack = this.theMechanical.getMechanicalInventory().getStackInSlot(i);
            if (itemstack.isEmpty()){return false;}
        }
	    return true;
	}
	private boolean shouldReturn()
	{
		World world = this.theMechanical.getEntityWorld();
		BlockPos pos = this.theMechanical.getPosition();
		double blocks = Math.floor(Math.sqrt(this.theMechanical.getDistanceSq(this.start)));
		if (((blocks + 10) * 0.25D) >= this.theMechanical.getTension()){return true;}
		else if(blocks >= 100){return true;}
		else{return false;}
	}
}
