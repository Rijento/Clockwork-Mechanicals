package net.rijento.clockwork_mechanicals.lib;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.rijento.clockwork_mechanicals.items.ItemMechanicalConfigurator;
import net.rijento.clockwork_mechanicals.lib.gui.GhostSlot;

public class ContainerConfigurator extends Container
{
	public int current_task = 0;
	public final ItemStack configuratorStack;
	public final InventoryPlayer playerInventory;
	public List<GhostSlot> ghostSlots = Lists.<GhostSlot>newArrayList();
	public InventoryBasic filterWithdraw;
	public InventoryBasic filterDeposit;
	
	public ContainerConfigurator(InventoryPlayer playerInventory, ItemStack cnfgIn)
	{
		this.playerInventory = playerInventory;
		this.configuratorStack = cnfgIn;
		this.filterWithdraw = ((ItemMechanicalConfigurator)configuratorStack.getItem()).withdrawFilter.filterInventory;
		this.filterDeposit = ((ItemMechanicalConfigurator)configuratorStack.getItem()).depositFilter.filterInventory;
		this.current_task = cnfgIn.getTagCompound().getInteger("current_task");
		if (this.current_task == 1 || this.current_task == 2 || this.current_task == 3 || this.current_task == 8)
		{
			for (int l = 0; l < 3; ++l)
	        {
	            for (int k = 0; k < 9; ++k)
	            {
	                this.addSlotToContainer(new Slot(playerInventory, k + l * 9 + 9, 8 + k * 18, l * 18 + 51));
	            }
	        }
	
	        for (int i1 = 0; i1 < 9; ++i1)
	        {
	            this.addSlotToContainer(new Slot(playerInventory, i1, 8 + i1 * 18, 109));
	        }
		}
		else
		{
			if (this.current_task == 5)
			{
				for (int i = 0; i < 3; ++i)
				{
					for (int j = 0; j < 3; ++j)
					{
						this.addGhostSlot(new GhostSlot(filterWithdraw, j + i * 3, 16 + j * 18, 43 + i * 18));
					}
				}
			}
			if (this.current_task == 6)
			{
				for (int i = 0; i < 3; ++i)
				{
					for (int j = 0; j < 3; ++j)
					{
						this.addGhostSlot(new GhostSlot(filterDeposit, j + i * 3, 16 + j * 18, 43 + i * 18));
					}
				}
			}
			for (int l = 0; l < 3; ++l)
	        {
	            for (int k = 0; k < 9; ++k)
	            {
	                this.addSlotToContainer(new Slot(playerInventory, k + l * 9 + 9, 8 + k * 18, l * 18 + 140));
	            }
	        }
	
	        for (int i1 = 0; i1 < 9; ++i1)
	        {
	            this.addSlotToContainer(new Slot(playerInventory, i1, 8 + i1 * 18, 198));
	        }
		}
	}
	
	public void addGhostSlot(GhostSlot slot)
	{
		this.ghostSlots.add(slot);
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}
	@Override
	public void onContainerClosed(EntityPlayer playerIn)
    {
		super.onContainerClosed(playerIn);
		((ItemMechanicalConfigurator)configuratorStack.getItem()).save(configuratorStack);
    }

	public void update()
	{
		this.inventorySlots.clear();
		this.ghostSlots.clear();
		if (this.current_task == 1 || this.current_task == 2 || this.current_task == 3 || this.current_task == 8)
		{
			for (int l = 0; l < 3; ++l)
	        {
	            for (int k = 0; k < 9; ++k)
	            {
	                this.addSlotToContainer(new Slot(playerInventory, k + l * 9 + 9, 8 + k * 18, l * 18 + 51));
	            }
	        }
	
	        for (int i1 = 0; i1 < 9; ++i1)
	        {
	            this.addSlotToContainer(new Slot(playerInventory, i1, 8 + i1 * 18, 109));
	        }
		}
		else
		{
			if (this.current_task == 5)
			{
				for (int i = 0; i < 3; ++i)
				{
					for (int j = 0; j < 3; ++j)
					{
						this.addGhostSlot(new GhostSlot(filterWithdraw, j + i * 3, 16 + j * 18, 43 + i * 18));
					}
				}
			}
			if (this.current_task == 6)
			{
				for (int i = 0; i < 3; ++i)
				{
					for (int j = 0; j < 3; ++j)
					{
						this.addGhostSlot(new GhostSlot(filterDeposit, j + i * 3, 16 + j * 18, 43 + i * 18));
					}
				}
			}
			for (int l = 0; l < 3; ++l)
	        {
	            for (int k = 0; k < 9; ++k)
	            {
	                this.addSlotToContainer(new Slot(playerInventory, k + l * 9 + 9, 8 + k * 18, l * 18 + 140));
	            }
	        }
	
	        for (int i1 = 0; i1 < 9; ++i1)
	        {
	            this.addSlotToContainer(new Slot(playerInventory, i1, 8 + i1 * 18, 198));
	        }
		}
	}
}
