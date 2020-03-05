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

public class EntityMechanicalWorker extends EntityMechanicalBase
{

	public EntityMechanicalWorker(World worldIn)
	{
		super(worldIn, new InventoryBasic("Items", false, 9), 0.6F, 0.95F, 0.25F);
		this.setTickingSound(ModSoundEvents.WORKER_TICK);
		this.setWindingSound(ModSoundEvents.WORKER_WIND);
		this.setWindingEndSound(ModSoundEvents.WORKER_WIND_END);
		this.setMainspringInstallSound(ModSoundEvents.WORKER_MAINSPRING_INSTALL);
	}
	@Override
	protected float getSoundVolume()
	{
		return super.getSoundVolume() / 3.0F;
	}

	@Override
	public void setOrders(List<Order> ordersIn, boolean load)
	{
		if (load == false)
			{
				this.tasks.taskEntries.clear();
				this.setCurrentTask(0);
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
	public static void registerFixesWorker(DataFixer fixer)
    {
        EntityLiving.registerFixesMob(fixer, EntityMechanicalWorker.class);
        fixer.registerWalker(FixTypes.ENTITY, new ItemStackDataLists(EntityMechanicalWorker.class, new String[] {"Inventory"}));
    }
}