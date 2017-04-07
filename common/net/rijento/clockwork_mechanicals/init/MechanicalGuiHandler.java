package net.rijento.clockwork_mechanicals.init;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.rijento.clockwork_mechanicals.lib.ContainerConfigurator;
import net.rijento.clockwork_mechanicals.lib.gui.GuiConfiguratorBase;

public class MechanicalGuiHandler implements IGuiHandler 
{
	public static final int CONFIGURATOR_GUI = 0;
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == CONFIGURATOR_GUI)
		{
			return new ContainerConfigurator(player.inventory, player.getHeldItemMainhand());
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == CONFIGURATOR_GUI)
		{
			return new GuiConfiguratorBase(player.inventory, player.getHeldItemMainhand(), world);
		}
		return null;
	}

}
