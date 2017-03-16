package net.rijento.clockwork_mechanicals.entities;

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
import net.rijento.clockwork_mechanicals.items.ItemMainspring;
import net.rijento.clockwork_mechanicals.lib.EntityAIMechanicalMoveToBlock;
import net.rijento.clockwork_mechanicals.lib.EntityAIMechanicalWait;
import net.rijento.clockwork_mechanicals.lib.Order;

public class EntityMechanicalWorker extends EntityGolem 
{
	public List<Order> orders;
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
//		this.InitEntityAI();
		
	}
	
	@Override
	protected boolean isAIEnabled()
	{
	   return true;
	}
	@Override
	public void setMoveForward(float amount)
    {
		this.unwind(0.1F);
        super.setMoveForward(amount);
    }
	@Override
	public void setMoveStrafing(float amount)
    {
		this.unwind(0.15F);
        super.setMoveStrafing(amount);
    }
	
	protected void InitEntityAI()
	{
	}
	
	@Override
	protected void updateAITasks()
	{
	}
	
	protected void updateEquipmentIfNeeded(EntityItem itemEntity)
    {
        ItemStack itemstack = itemEntity.getEntityItem();
        Item item = itemstack.getItem();    
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
	public void SetOrders(List<Order> ordersIn)
	{
		this.tasks.taskEntries.clear();
		this.currentTask = 0;
		this.orders = ordersIn;
		System.out.println(this.orders.size());
		for (int i = 0; i < this.orders.size(); i++)
		{
			Order order = this.orders.get(i);
			this.tasks.addTask(i, new EntityAIMechanicalMoveToBlock(this, this.getAIMoveSpeed(), order.pos, i));
//			switch(order.command)
//			{
//			case "harvest":
//				System.out.println("before4");
//				EntityAIMechanicalHarvestFarmland task = new EntityAIMechanicalHarvestFarmland(this, 0.25F);
//				System.out.println("before5");
//				this.tasks.addTask(1, task);
//				System.out.println("after2");
//				
//			}
			
			
		}
	}
	public int getCurrentTask()
	{
		return this.currentTask;
	}
	
	public void nextTask()
	{
		this.currentTask = this.currentTask+1;
		if (this.currentTask > this.orders.size() - 1)
		{
			this.currentTask = 0;
		}
		System.out.println(this.currentTask);
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
    }

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
        ItemStack mainspringStack = new ItemStack(compound.getCompoundTag("Mainspring"));
        Item MainIn = mainspringStack.getItem();
        if (MainIn instanceof ItemMainspring)
        {
        	System.out.println("WORK");
        	this.setMainspring((ItemMainspring)MainIn);
        }
        this.tension = compound.getFloat("Tension");
        
        NBTTagList nbttaglist = compound.getTagList("Inventory", 10);

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            ItemStack itemstack = new ItemStack(nbttaglist.getCompoundTagAt(i));

            if (!itemstack.isEmpty())
            {
                this.workerInventory.addItem(itemstack);
            }
        }
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
				System.out.print(this.tension);
			}
		}
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
