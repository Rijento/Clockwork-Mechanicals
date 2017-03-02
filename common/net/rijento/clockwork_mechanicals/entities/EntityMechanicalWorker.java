package net.rijento.clockwork_mechanicals.entities;

import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.rijento.clockwork_mechanicals.items.ItemMainspring;
import net.rijento.clockwork_mechanicals.items.ItemMechanicalConfigurator;
import net.rijento.clockwork_mechanicals.lib.EntityAIMechanicalHarvestFarmland;
import net.rijento.clockwork_mechanicals.lib.Order;

public class EntityMechanicalWorker extends EntityGolem 
{
	private List<Order> orders;
	private ItemMainspring Mainspring;
	private final InventoryBasic workerInventory;
	private float tension;
	private float maxTension;
	
	public EntityMechanicalWorker(World worldIn) {
		super(worldIn);
		this.workerInventory = new InventoryBasic("Items", false, 9);
		this.setSize(0.6F, 0.95F);
		this.tension = 0F;
		this.maxTension = 0F;
		this.setCanPickUpLoot(true);
		this.InitEntityAI();
		
	}
	
	@Override
	protected boolean isAIEnabled()
	{
	   return true;
	}
	
	protected void InitEntityAI()
	{
	}
	
	protected void updateAITasks()
	{
		super.updateAITasks();
	}
	
	public void onUpdate()
	{
		super.onUpdate();
		
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
	
	public void SetOrders(ItemMechanicalConfigurator configurator)
	{
		this.orders = configurator.getOrders();
	}
	
	public void wind(float rate)
	{
		if (this.tension + rate <= this.maxTension)
		{
			this.tension += (rate/this.Mainspring.getWindingCost());
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
