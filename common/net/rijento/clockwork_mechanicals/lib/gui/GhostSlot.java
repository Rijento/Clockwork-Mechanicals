package net.rijento.clockwork_mechanicals.lib.gui;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class GhostSlot extends Slot {

	public GhostSlot(IInventory inventoryIn, int index, int xPosition, int yPosition)
	{
		super(inventoryIn, index, xPosition, yPosition);
		// TODO Auto-generated constructor stub
	}
	public boolean isMouseOver(int mx, int my)
	{
	    return mx >= xPos && mx < (xPos + 16) && my >= yPos && my < (yPos + 16);
	}
    @Override
    public void putStack(ItemStack stack) {
	    if(stack != null) {
	        stack = stack.copy();
	        stack.setCount(1);
	    }
	    super.putStack(stack);
    }
    @Override
    public int getSlotStackLimit()
    {
    	return 1;
    }

}
