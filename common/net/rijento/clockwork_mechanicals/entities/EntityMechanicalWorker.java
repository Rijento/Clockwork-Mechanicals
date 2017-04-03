package net.rijento.clockwork_mechanicals.entities;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeedFood;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.rijento.clockwork_mechanicals.ai.EntityAIMechanicalChop;
import net.rijento.clockwork_mechanicals.ai.EntityAIMechanicalCraft;
import net.rijento.clockwork_mechanicals.ai.EntityAIMechanicalDropOff;
import net.rijento.clockwork_mechanicals.ai.EntityAIMechanicalHarvestFarmland;
import net.rijento.clockwork_mechanicals.ai.EntityAIMechanicalMine;
import net.rijento.clockwork_mechanicals.ai.EntityAIMechanicalMoveToBlock;
import net.rijento.clockwork_mechanicals.ai.PathNavigateMechanical;
import net.rijento.clockwork_mechanicals.items.ItemMainspring;
import net.rijento.clockwork_mechanicals.lib.ContainerBasic;
import net.rijento.clockwork_mechanicals.lib.Order;
import net.rijento.clockwork_mechanicals.lib.filter.FilterBase;
import net.rijento.clockwork_mechanicals.lib.filter.KeepAmnt;

public class EntityMechanicalWorker extends EntityGolem 
{
	public List<Order> orders;
	public List<FilterBase> filters = new ArrayList<FilterBase>();
	public boolean isWinding = false;
	private ItemMainspring Mainspring;
	private int currentTask;
	private final InventoryBasic workerInventory;
	private float tension;
	private float maxTension;
	
	public EntityMechanicalWorker(World worldIn) {
		super(worldIn);
		this.workerInventory = new InventoryBasic("Items", false, 9);
		this.setSize(0.6F, 0.95F);
		this.maxTension = 0F;
		this.navigator = new PathNavigateMechanical(this, worldIn);
		this.setCanPickUpLoot(true);
	}
	protected boolean isAIEnabled()
	{
	   return true;
	}
	@Override
	public float getAIMoveSpeed()
    {
		if (this.Mainspring != null)
		{
			return 0.25F * this.getMainspring().getResistance();
		}
		else{return 0.0f;}
    }
	
	@Override
	public void onUpdate()
	{
		double d0 = this.posX - this.prevPosX;
        double d1 = this.posZ - this.prevPosZ;
        double f1 = d0 * d0 + d1 * d1;
        float distance = (float)Math.sqrt(f1);
        if (distance > 0.0F){this.unwind(0.5F * distance);}
		super.onUpdate();
	}
	
	protected void updateEquipmentIfNeeded(EntityItem itemEntity)
    {
        ItemStack itemstack = itemEntity.getEntityItem();
        ItemStack itemstack1 = this.workerInventory.addItem(itemstack);

        if (itemstack1.isEmpty())
        {
            itemEntity.setDead();
        }
        else
        {
            itemstack.setCount(itemstack1.getCount());
        }
    
    }
	public ItemMainspring getMainspring()
	{
		return this.Mainspring;
	}
	
	public void setMainspring(ItemMainspring MainspringIn)
	{
		if (this.Mainspring instanceof ItemMainspring && !this.world.isRemote)
		{
			this.dropItem(this.Mainspring, 1);
		}
		this.Mainspring = MainspringIn;
		this.maxTension = MainspringIn.getMaxTension();
		this.setAIMoveSpeed(0.25F * MainspringIn.getResistance());
	}
	
	public void SetOrders(List<Order> ordersIn, boolean load)
	{
		if (load == false)
		{
			this.tasks.taskEntries.clear();
			this.currentTask = 0;
		}
		this.orders = ordersIn;
		for (int i = 0; i < this.orders.size(); i++)
		{
			Order order = this.orders.get(i);
			switch(order.command)
			{
			case "harvest":
				EntityAIMechanicalHarvestFarmland taskHarvest = new EntityAIMechanicalHarvestFarmland(this, order.pos, i);
				this.tasks.addTask(i, new EntityAIMechanicalMoveToBlock(this, this.getAIMoveSpeed(), order.pos, i));
				this.tasks.addTask(i, taskHarvest);
				KeepAmnt keepSeeds = new KeepAmnt(new ItemStack(Items.WHEAT_SEEDS), 10);
				KeepAmnt keepSeeds1 = new KeepAmnt(new ItemStack(Items.BEETROOT_SEEDS), 10);
				KeepAmnt keepSeeds2 = new KeepAmnt(new ItemStack(Items.CARROT), 10);
				KeepAmnt keepSeeds3 = new KeepAmnt(new ItemStack(Items.POTATO), 10);
				if (!this.filters.contains(keepSeeds))
				{
					this.filters.add(keepSeeds);
					this.filters.add(keepSeeds1);
					this.filters.add(keepSeeds2);
					this.filters.add(keepSeeds3);
				}
				continue;
			case "dropoff":
				EntityAIMechanicalDropOff taskDropoff = new EntityAIMechanicalDropOff(this, order.pos, i);
				this.tasks.addTask(i, new EntityAIMechanicalMoveToBlock(this, this.getAIMoveSpeed(), order.pos, i));
				this.tasks.addTask(i, taskDropoff);
				continue;
			case "craft":
				InventoryCrafting recipe = new InventoryCrafting(new ContainerBasic(), 3, 3);
				recipe.setInventorySlotContents(5, new ItemStack(Blocks.PLANKS, 1));
				recipe.setInventorySlotContents(8, new ItemStack(Blocks.PLANKS, 1));
				EntityAIMechanicalCraft taskCraft = new EntityAIMechanicalCraft(this, i, recipe);
				this.tasks.addTask(i, new EntityAIMechanicalMoveToBlock(this, this.getAIMoveSpeed(), order.pos, i));
				this.tasks.addTask(i, taskCraft);
				continue;
			case "chop":
				EntityAIMechanicalChop taskChop = new EntityAIMechanicalChop(this, order.pos, i);
				this.tasks.addTask(i, new EntityAIMechanicalMoveToBlock(this, this.getAIMoveSpeed(), order.pos, i));
				this.tasks.addTask(i, taskChop);
				ItemStack temp = new ItemStack(Blocks.SAPLING);
				KeepAmnt keepSaplings = new KeepAmnt(temp, 10);
				if (!this.filters.contains(keepSaplings))
				{
					this.filters.add(keepSaplings);
				}
				continue;
			case "mine":
				EntityAIMechanicalMine taskMine = new EntityAIMechanicalMine(this, order.pos, EnumFacing.NORTH, true, i);
				this.tasks.addTask(i, taskMine);
				KeepAmnt keepTorches = new KeepAmnt(new ItemStack(Blocks.TORCH), 64);
				KeepAmnt keepPickaxe = new KeepAmnt(new ItemStack(Items.WOODEN_PICKAXE), 1);
				if (!this.filters.contains(keepTorches))
				{
					this.filters.add(keepTorches);
				}
				continue;
			}
		}
	}
	public int getCurrentTask(){return this.currentTask;}
	
	public void nextTask()
	{
		this.currentTask = this.currentTask+1;
		if (this.currentTask > this.orders.size() - 1)
		{
			this.currentTask = 0;
		}
	}
	
	public static void registerFixesWorker(DataFixer fixer)
    {
        EntityLiving.registerFixesMob(fixer, EntityMechanicalWorker.class);
        fixer.registerWalker(FixTypes.ENTITY, new ItemStackDataLists(EntityMechanicalWorker.class, new String[] {"Inventory"}));
    }
	
	 @Override
	 public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setTag("Mainspring", new ItemStack(this.Mainspring).writeToNBT(new NBTTagCompound()));
        compound.setFloat("Tension", this.tension);
        compound.setInteger("currentTask", this.currentTask);
        
        
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.workerInventory.getSizeInventory(); ++i)
        {
            ItemStack itemstack = this.workerInventory.getStackInSlot(i);

            if (!itemstack.isEmpty())
            {
                nbttaglist.appendTag(itemstack.writeToNBT(new NBTTagCompound()));
            }
        }
        compound.setTag("Inventory", nbttaglist);
        
        NBTTagList nbttaglist2 = new NBTTagList();
        for (Order order : this.orders)
        {
        	nbttaglist2.appendTag(order.getOrderNBT());
        }
        compound.setTag("Orders", nbttaglist2);
        
    }

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
        ItemStack mainspringStack = new ItemStack(compound.getCompoundTag("Mainspring"));
        Item MainIn = mainspringStack.getItem();
        if (MainIn instanceof ItemMainspring)
        {
        	this.setMainspring((ItemMainspring)MainIn);
        }
        this.tension = compound.getFloat("Tension");
        this.currentTask = compound.getInteger("currentTask");
        
        NBTTagList nbttaglist = compound.getTagList("Inventory", Constants.NBT.TAG_COMPOUND);

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            ItemStack itemstack = new ItemStack(nbttaglist.getCompoundTagAt(i));

            if (!itemstack.isEmpty())
            {
                this.workerInventory.addItem(itemstack);
            }
        }
        NBTTagList nbttaglist2 = compound.getTagList("Orders", Constants.NBT.TAG_COMPOUND);
        this.orders = new ArrayList<Order>();
        for (int i = 0; i < nbttaglist2.tagCount(); ++i)
        {
        	Order toAdd = new Order(nbttaglist2.getCompoundTagAt(i));
        	this.orders.add(toAdd);
        }
        this.SetOrders(this.orders, true);
    }
	
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {
		this.tension = 0F;
    	return this.finalizeMobSpawn(difficulty, livingdata, true);
    }

    public IEntityLivingData finalizeMobSpawn(DifficultyInstance p_190672_1_, @Nullable IEntityLivingData p_190672_2_, boolean p_190672_3_)
    {
        p_190672_2_ = super.onInitialSpawn(p_190672_1_, p_190672_2_);
        return p_190672_2_;
    }
    
    
    
	public void wind(float rate)
	{
		if (this.Mainspring != null){			
			if (this.tension + rate/this.Mainspring.getWindingCost() <= this.maxTension)
			{
				this.tension += (rate/this.Mainspring.getWindingCost());
				this.isWinding = true;
				System.out.println(this.tension);
			}
		}
		this.isWinding = false;
	}
	public void unwind(float amount)
	{
		if (this.tension - amount >= 0)
		{
			this.tension -= amount;
		}
	}
	public float getTension()
	{
		return this.tension;
	}
	
	public InventoryBasic getMechanicalInventory()
	{
		return this.workerInventory;
	}
	public void overrideMechanicalInventory(InventoryBasic inventoryIn)
	{
		for (int i = 0; i < inventoryIn.getSizeInventory(); i++)
		{
			this.workerInventory.setInventorySlotContents(i, inventoryIn.getStackInSlot(i));
		}
	}
	@Override
	public boolean isEntityInvulnerable(DamageSource source)
    {
        return source == DamageSource.IN_WALL || source == DamageSource.ON_FIRE || source == DamageSource.CACTUS;
    }
	
	public void onDeath(DamageSource cause)
	{
		super.onDeath(cause);
		InventoryHelper.dropInventoryItems(this.world, this.getPosition(), this.workerInventory);
		if (!this.world.isRemote)
		{
			this.dropItem(this.Mainspring, 1);
		}
	}
}
