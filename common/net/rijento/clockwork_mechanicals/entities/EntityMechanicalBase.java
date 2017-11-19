package net.rijento.clockwork_mechanicals.entities;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

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
import net.minecraft.util.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.rijento.clockwork_mechanicals.ai.PathNavigateMechanical;
import net.rijento.clockwork_mechanicals.items.ItemMainspring;
import net.rijento.clockwork_mechanicals.lib.Order;

public abstract class EntityMechanicalBase extends EntityGolem
{
	public List<Order> orders;
	public boolean isWinding = false;
	private ItemStack Mainspring;
	private int currentTask;
	private final InventoryBasic mechanicalInventory;
	private float prevTension;
	private float tension;
	private float maxTension;
	private float moveSpeed;
	private float windingCost;
	private final float speedBase;
	private int windingTimer;
	private SoundEvent tickingSound;
	private SoundEvent windingSound;
	private SoundEvent windingEndSound;
	private SoundEvent mainspringInstallSound;
	
	public EntityMechanicalBase(World worldIn, InventoryBasic InventoryIn, float widthIn, float heightIn, float speedBaseIn)
	{
		super(worldIn);
		this.mechanicalInventory = InventoryIn;
		this.setSize(widthIn, heightIn);
		this.maxTension = 0F;
		this.navigator = new PathNavigateMechanical(this, worldIn);
		this.speedBase = speedBaseIn;
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
			return this.speedBase * ItemMainspring.getResistance(this.Mainspring.getItemDamage());
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
			{this.playSound(this.tickingSound, this.getSoundVolume(),1.0F);}
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
	
	@Override
	protected void updateEquipmentIfNeeded(EntityItem itemEntity)
    {
        ItemStack itemstack = itemEntity.getItem();
        ItemStack itemstack1 = this.mechanicalInventory.addItem(itemstack);

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
		//this.setAIMoveSpeed(speedBase * ItemMainspring.getResistance(MainspringIn.getItemDamage()));
		this.playSound(this.mainspringInstallSound, this.getSoundVolume(), this.getSoundPitch());
	}
	
	public abstract void setOrders(List<Order> ordersIn, boolean load);
	
	public int getCurrentTask(){return this.currentTask;}
	
	public void setCurrentTask(int currentTask) {
		this.currentTask = currentTask;
	}
	
	public void nextTask()
	{
		this.setCurrentTask(this.getCurrentTask()+1);
		if (this.getCurrentTask() > this.orders.size() - 1)
		{
			this.setCurrentTask(0);
		}
	}
	public void wind(float rate)
	{
		if (this.Mainspring != null){			
			if (this.tension + rate/this.windingCost <= this.maxTension && this.windingTimer <= 10)
			{
				this.tension += (rate/this.windingCost);
				this.windingTimer = 20;
				this.playSound(this.windingSound, this.getSoundVolume(), this.getSoundPitch());
			}
			else if (this.windingTimer <= 5)
			{
				this.playSound(this.windingEndSound, this.getSoundVolume(), this.getSoundPitch());
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
		return this.mechanicalInventory;
	}
	public void overrideMechanicalInventory(InventoryBasic inventoryIn)
	{
		for (int i = 0; i < inventoryIn.getSizeInventory(); i++)
		{
			this.mechanicalInventory.setInventorySlotContents(i, inventoryIn.getStackInSlot(i));
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
	
	@Override
	 public void writeEntityToNBT(NBTTagCompound compound)
   {
       super.writeEntityToNBT(compound);
       if (this.Mainspring != null){compound.setTag("Mainspring", this.Mainspring.writeToNBT(new NBTTagCompound()));}
       else {compound.setTag("Mainspring", (new ItemStack(Items.AIR)).writeToNBT(new NBTTagCompound()));}
       compound.setFloat("Tension", this.tension);
       compound.setInteger("currentTask", this.getCurrentTask());
       
       
       NBTTagList nbttaglist = new NBTTagList();

       for (int i = 0; i < this.mechanicalInventory.getSizeInventory(); ++i)
       {
           ItemStack itemstack = this.mechanicalInventory.getStackInSlot(i);

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
       this.setCurrentTask(compound.getInteger("currentTask"));
       
       NBTTagList nbttaglist = compound.getTagList("Inventory", Constants.NBT.TAG_COMPOUND);

       for (int i = 0; i < nbttaglist.tagCount(); ++i)
       {
           ItemStack itemstack = new ItemStack(nbttaglist.getCompoundTagAt(i));

           if (!itemstack.isEmpty())
           {
               this.mechanicalInventory.addItem(itemstack);
           }
       }
       NBTTagList nbttaglist2 = compound.getTagList("Orders", Constants.NBT.TAG_COMPOUND);
       this.orders = new ArrayList<Order>();
       for (int i = 0; i < nbttaglist2.tagCount(); ++i)
       {
       	Order toAdd = new Order(nbttaglist2.getCompoundTagAt(i));
       	this.orders.add(toAdd);
       }
       this.setOrders(this.orders, true);
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
    
    public void setTickingSound(SoundEvent event)
	{
		this.tickingSound = event;
	}
	public SoundEvent getTickingSound()
	{
		return this.tickingSound;
	}
	
	public void setWindingEndSound(SoundEvent event)
	{
		this.windingEndSound = event;
	}
	public SoundEvent getWindingEndSound()
	{
		return this.windingEndSound;
	}
	
	public void setWindingSound(SoundEvent event)
	{
		this.windingSound = event;
	}
	public SoundEvent getWindingSound()
	{
		return this.windingSound;
	}
	
	public void setMainspringInstallSound(SoundEvent event)
	{
		this.mainspringInstallSound = event;
	}
	public SoundEvent getMainspringInstallSound()
	{
		return this.mainspringInstallSound;
	}
	
	public void onDeath(DamageSource cause)
	{
		super.onDeath(cause);
		InventoryHelper.dropInventoryItems(this.world, this.getPosition(), this.mechanicalInventory);
		if (!this.world.isRemote && this.Mainspring != null)
		{
			this.entityDropItem(this.Mainspring, 0.5F);
		}
	}	
}
