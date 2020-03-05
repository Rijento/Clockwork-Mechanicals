package net.rijento.clockwork_mechanicals.lib.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GuiStampingTable extends GuiContainer 
{
	private static final ResourceLocation texture = new ResourceLocation("clockwork_mechanicals:textures/gui/stamping_table.png");

	public GuiStampingTable(InventoryPlayer inventory, Container inventorySlotsIn, World worldIn)
	{
		super(inventorySlotsIn);
		this.xSize = 190;
		this.ySize = 222;
		this.mc.getTextureManager().bindTexture(texture);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		// TODO Auto-generated method stub

	}

}
