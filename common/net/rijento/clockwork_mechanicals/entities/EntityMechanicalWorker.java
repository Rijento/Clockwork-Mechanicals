package net.rijento.clockwork_mechanicals.entities;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.InventoryHelper;
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
import net.rijento.clockwork_mechanicals.ai.EntityAIMechanicalAttack;
import net.rijento.clockwork_mechanicals.ai.EntityAIMechanicalChop;
import net.rijento.clockwork_mechanicals.ai.EntityAIMechanicalCraft;
import net.rijento.clockwork_mechanicals.ai.EntityAIMechanicalDropOff;
import net.rijento.clockwork_mechanicals.ai.EntityAIMechanicalHarvestFarmland;
import net.rijento.clockwork_mechanicals.ai.EntityAIMechanicalMine;
import net.rijento.clockwork_mechanicals.ai.EntityAIMechanicalMoveToBlock;
import net.rijento.clockwork_mechanicals.ai.EntityAIMechanicalPickUp;
import net.rijento.clockwork_mechanicals.ai.PathNavigateMechanical;
import net.rijento.clockwork_mechanicals.init.ModSoundEvents;
import net.rijento.clockwork_mechanicals.items.ItemMainspring;
import net.rijento.clockwork_mechanicals.lib.Order;

public class EntityMechanicalWorker extends EntityGolem 
{
	public List<Order> orders;
	public boolean isWinding = false;
	private ItemStack Mainspring;
	private int currentTask;
	private final InventoryBasic workerInventory;
	private float prevTension;
	private float tension;
	private float maxTension;
	private float moveSpeed;
	private float windingCost;
	private int windingTimer;
	
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
			return 0.25F * ItemMainspring.getResistance(this.Mainspring.getItemDamage());
		}
		else{return 0.0f;}
    }
	
	@Override
	public void onUpdate()
	{
		if (this.ticksExisted % 20 == 0 
				&& this.hasMainspring() 
				&& this.tension > 1.0F 
				&& !this.isWinding
				&& !this.isWet()) 
			{this.playSound(ModSoundEvents.WORKER_TICK, this.getSoundVolume() / 3F,1.0F);}
		if (this.prevTension < this.tension){this.isWinding = true;}
		else{this.isWinding = false;}
		double d0 = this.posX - this.prevPosX;
        double d1 = this.posZ - this.prevPosZ;
        double f1 = d0 * d0 + d1 * d1;
        float distance = (float)Math.sqrt(f1);
        if (distance > 0.0F){this.unwind(0.25F * distance);}
		super.onUpdate();
		if (this.windingTimer <= 0)
		{
			this.prevTension = this.tension;
		}
		else{this.windingTimer--;}
	}
	
	protected void updateEquipmentIfNeeded(EntityItem itemEntity)
    {
        ItemStack itemstack = itemEntity.getItem();
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
	public boolean hasMainspring()
	{
		return this.Mainspring != null;
	}
	
	public ItemStack getMainspring()
	{
		return this.Mainspring;
	}
	
	public void setMainspring(ItemStack MainspringIn)
	{
		if (this.Mainspring != null && !this.world.isRemote)
		{
			this.entityDropItem(this.Mainspring, 0.5F);
		}
		this.Mainspring = MainspringIn;
		this.tension = 0F;
		this.prevTension = 0F;
		this.maxTension = ItemMainspring.getMaxTension(MainspringIn.getItemDamage());
		this.windingCost = ItemMainspring.getWindingCost(MainspringIn.getItemDamage());
		this.setAIMoveSpeed(0.25F * ItemMainspring.getResistance(MainspringIn.getItemDamage()));
		this.playSound(ModSoundEvents.WORKER_MAINSPRING_INSTALL, this.getSoundVolume(), this.getSoundPitch());
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
				continue;
			case "chop":
				EntityAIMechanicalChop taskChop = new EntityAIMechanicalChop(this, order.pos, i);
				this.tasks.addTask(i, new EntityAIMechanicalMoveToBlock(this, this.getAIMoveSpeed(), order.pos, i));
				this.tasks.addTask(i, taskChop);
				continue;
			case "attack":
				EntityAIMechanicalAttack taskAttack = new EntityAIMechanicalAttack(this, order.pos, i);
				this.tasks.addTask(i, new EntityAIMechanicalMoveToBlock(this, this.getAIMoveSpeed(), order.pos, i));
				this.tasks.addTask(i, taskAttack);
			case "mine":
				EntityAIMechanicalMine taskMine = new EntityAIMechanicalMine(this, order.pos, order.facing, i);
				this.tasks.addTask(i, taskMine);
				continue;
			case "dropoff":
				EntityAIMechanicalDropOff taskDropoff = new EntityAIMechanicalDropOff(this, order.pos, i, order.filter);
				this.tasks.addTask(i, new EntityAIMechanicalMoveToBlock(this, this.getAIMoveSpeed(), order.pos, i));
				this.tasks.addTask(i, taskDropoff);
				continue;
			case "pickup":
				EntityAIMechanicalPickUp taskPickup = new EntityAIMechanicalPickUp(this, order.pos, i, order.filter);
				this.tasks.addTask(i, new EntityAIMechanicalMoveToBlock(this, this.getAIMoveSpeed(), order.pos, i));
				this.tasks.addTask(i, taskPickup);
				continue;
			case "craft":
				EntityAIMechanicalCraft taskCraft = new EntityAIMechanicalCraft(this, i, order.recipe, order.pos);
				this.tasks.addTask(i, new EntityAIMechanicalMoveToBlock(this, this.getAIMoveSpeed(), order.pos, i));
				this.tasks.addTask(i, taskCraft);
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
        if (this.Mainspring != null){compound.setTag("Mainspring", this.Mainspring.writeToNBT(new NBTTagCompound()));}
        else {compound.setTag("Mainspring", (new ItemStack(Items.AIR)).writeToNBT(new NBTTagCompound()));}
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
        ItemStack MainIn = new ItemStack(compound.getCompoundTag("Mainspring"));
        if (MainIn.getItem() instanceof ItemMainspring)
        {
        	this.setMainspring(MainIn);
        }
        this.tension = compound.getFloat("Tension");
        this.prevTension = this.tension;
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
			if (this.tension + rate/this.windingCost <= this.maxTension && this.windingTimer <= 10)
			{
				this.tension += (rate/this.windingCost);
				this.windingTimer = 20;
				this.playSound(ModSoundEvents.WORKER_WIND, this.getSoundVolume(), this.getSoundPitch());
			}
			else if (this.windingTimer <= 5)
			{
				this.playSound(ModSoundEvents.WORKER_WIND_END, this.getSoundVolume(), this.getSoundPitch());
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
        return source == DamageSource.IN_WALL 
        		|| source == DamageSource.ON_FIRE 
        		|| source == DamageSource.CACTUS 
        		|| source.isMagicDamage()
        		|| source == DamageSource.DROWN;
    }
	
	public void onDeath(DamageSource cause)
	{
		super.onDeath(cause);
		InventoryHelper.dropInventoryItems(this.world, this.getPosition(), this.workerInventory);
		if (!this.world.isRemote && this.Mainspring != null)
		{
			this.entityDropItem(this.Mainspring, 0.5F);
		}
	}
}
