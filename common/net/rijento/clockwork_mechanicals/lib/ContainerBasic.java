package net.rijento.clockwork_mechanicals.lib;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerBasic extends Container {

	public ContainerBasic()
	{
		
	}
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return false;
	}

}
