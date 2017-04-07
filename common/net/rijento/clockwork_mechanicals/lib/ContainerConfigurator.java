package net.rijento.clockwork_mechanicals.lib;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.rijento.clockwork_mechanicals.items.ItemMechanicalConfigurator;

public class ContainerConfigurator extends Container
{
	public int current_task = 0;
	public final ItemStack configuratorStack;
	public final InventoryPlayer playerInventory;
	
	public ContainerConfigurator(InventoryPlayer playerInventory, ItemStack cnfgIn)
	{
		this.playerInventory = playerInventory;
		this.configuratorStack = cnfgIn;
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

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}
	@Override
	public void onContainerClosed(EntityPlayer playerIn)
    {
		super.onContainerClosed(playerIn);
		((ItemMechanicalConfigurator)configuratorStack.getItem()).saveCurrentTask(configuratorStack);
    }

	public void update()
	{
		this.inventorySlots.clear();
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
