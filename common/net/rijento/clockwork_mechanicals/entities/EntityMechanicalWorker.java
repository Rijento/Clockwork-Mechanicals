package net.rijento.clockwork_mechanicals.entities;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.rijento.clockwork_mechanicals.items.ItemMainspring;
import net.rijento.clockwork_mechanicals.lib.EntityAIMechanicalHarvestFarmland;
import net.rijento.clockwork_mechanicals.lib.EntityAIMechanicalMoveToBlock;
import net.rijento.clockwork_mechanicals.lib.Order;

public class EntityMechanicalWorker extends EntityGolem 
{
	public List<Order> orders;
	public boolean isWinding= false;
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
		this.setCanPickUpLoot(true);
	}
	protected boolean isAIEnabled()
	{
	   return true;
	}
	protected void InitEntityAI()
	{
	}
	
	@Override
	public void onUpdate()
	{
		double d0 = this.posX - this.prevPosX;
        double d1 = this.posZ - this.prevPosZ;
        double f1 = d0 * d0 + d1 * d1;
        float distance = (float)Math.sqrt(f1);
        if (distance > 0.0F){this.unwind(0.1F * distance);}
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
			this.tasks.addTask(i, new EntityAIMechanicalMoveToBlock(this, this.getAIMoveSpeed(), order.pos, i));
			switch(order.command)
			{
			case "harvest":
				EntityAIMechanicalHarvestFarmland task = new EntityAIMechanicalHarvestFarmland(this, i);
				this.tasks.addTask(i, task);
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
	
	
	protected void entityInit()
    {
        super.entityInit();
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
				System.out.print(this.tension);
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
