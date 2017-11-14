package net.rijento.clockwork_mechanicals.ai;

import net.minecraft.block.BlockTorch;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.rijento.clockwork_mechanicals.entities.EntityMechanicalWorker;
import net.rijento.clockwork_mechanicals.items.ItemMainspring;


//TODO: make mechanicals use fortune enchant or silk touch enchant as well. Make drops automatically add to inventory (less messy)

public class EntityAIMechanicalMine extends EntityAIBase 
{
	private final EntityMechanicalWorker theMechanical;
	private final World world;
	private final BlockPos start;
	private final EnumFacing direction;
	private final int priority;
	private int runtime;
	private float breakprog = 0.0F;
	
	
	public EntityAIMechanicalMine( EntityMechanicalWorker theMechanicalIn, BlockPos posIn, EnumFacing directionIn, int priorityIn)
	{
		this.theMechanical = theMechanicalIn;
		this.world = this.theMechanical.world;
		this.start = posIn;
		this.direction = directionIn;
		this.priority = priorityIn;
		this.runtime = 1;
	}
	
	public boolean shouldUpdate()
	{
		if (this.priority != this.theMechanical.getCurrentTask()){return false;}
		else if (!(this.theMechanical.getTension()-0.75F>0)){return false;}
		else if (this.theMechanical.isWinding == true){return false;}
		else if (this.theMechanical.isWet()){return false;}
		else if (this.theMechanical.isEntityInsideOpaqueBlock()){return false;}
		else if (!this.hasPickaxe()){return false;}
		else if (this.checkFull()){this.theMechanical.nextTask(); return false;}
		else if (this.shouldReturn()){return false;}
		else{return true;}
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
		else if (this.shouldReturn()) //return if reached block limit
		{
			this.theMechanical.nextTask();
			return false;
		}
		else
		{
			if(this.runtime >= 100){this.runtime = 1;}
			return true;
		}
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
		if (!this.shouldUpdate()){return;}
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
		BlockPos pos = this.theMechanical.getPosition();
		BlockPos topBreak = pos.offset(this.direction).up();
		BlockPos botBreak = pos.offset(this.direction);
		Boolean toptest = this.world.getBlockState(topBreak).getBlockFaceShape(this.world, topBreak, this.direction.getOpposite()) == BlockFaceShape.SOLID && !(this.world.getBlockState(topBreak).getBlock() == Blocks.BEDROCK);// && !(world.getBlockState(topBreak).getBlock() == Blocks.OBSIDIAN);
		Boolean bottest = this.world.getBlockState(botBreak).getBlockFaceShape(this.world, botBreak, this.direction.getOpposite()) == BlockFaceShape.SOLID && !(this.world.getBlockState(topBreak).getBlock() == Blocks.BEDROCK);// && !(world.getBlockState(topBreak).getBlock() == Blocks.OBSIDIAN);
		if (toptest)
		{
			float speed = this.getDigSpeed(this.world.getBlockState(topBreak)) * (this.theMechanical.hasMainspring() ? ItemMainspring.getResistance(this.theMechanical.getMainspring().getItemDamage()) : 0);
			float hardness = this.world.getBlockState(topBreak).getBlockHardness(this.world, topBreak);
			float perTick = speed/hardness/30F;
			this.breakprog += perTick;
			this.world.sendBlockBreakProgress(this.theMechanical.getEntityId(), topBreak, (int)(this.breakprog * 10F) - 1);
			if (this.breakprog >= 1.0F)
			{
				this.world.destroyBlock(topBreak, true);
				this.damagePickaxe();
				this.theMechanical.unwind(0.5f);
				this.breakprog = 0;
			}
			return false;
		}
		else if (bottest && !(this.world.getBlockState(botBreak).getBlock() instanceof BlockTorch))
		{
			float speed = this.getDigSpeed(this.world.getBlockState(botBreak)) * ItemMainspring.getResistance(this.theMechanical.getMainspring().getItemDamage());
			float hardness = this.world.getBlockState(botBreak).getBlockHardness(this.world, botBreak);
			float perTick = speed/hardness/30F;
			this.breakprog += perTick;
			this.world.sendBlockBreakProgress(this.theMechanical.getEntityId(), botBreak, (int)(this.breakprog * 10F) - 1);
			if (this.breakprog >= 1.0F)
			{
				this.world.destroyBlock(botBreak, true);
				this.theMechanical.unwind(0.5f);
				this.damagePickaxe();
				this.breakprog = 0;
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
	
	private ItemStack getPickaxe()
	{
		for (int i = 0; i < this.theMechanical.getMechanicalInventory().getSizeInventory(); ++i)
		{
			ItemStack itemstack = this.theMechanical.getMechanicalInventory().getStackInSlot(i);
			Item item = itemstack.getItem();

            if (!itemstack.isEmpty() && (item instanceof ItemPickaxe))
            {
            	return itemstack;
            }
        }
		return null;
	}
	
	private void damagePickaxe()
	{
		for (int i = 0; i < this.theMechanical.getMechanicalInventory().getSizeInventory(); ++i)
		{
			ItemStack itemstack = this.theMechanical.getMechanicalInventory().getStackInSlot(i);
			Item item = itemstack.getItem();
	
	        if (!itemstack.isEmpty() && (item instanceof ItemPickaxe))
	        {
	        	itemstack.attemptDamageItem(1, this.theMechanical.world.rand, null);
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
		BlockPos pos = this.theMechanical.getPosition();
		double blocks = Math.floor(Math.sqrt(this.theMechanical.getDistanceSq(this.start)));
		if (((blocks + 10) * 0.25D) >= this.theMechanical.getTension()){return true;}
		else if(blocks >= 100){return true;}
		else{return false;}
	}
	
	private float getDigSpeed(IBlockState blockIn)
	{
		float f = this.getPickaxe().getDestroySpeed(blockIn);
		if (f > 1.0F)
        {
			ItemStack itemstack = this.getPickaxe();
            int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, itemstack);

            if (i > 0 && !itemstack.isEmpty())
            {
                f += (float)(i * i + 1);
            }
        }
		return (f < 0 ? 0 : f);
	}
}
