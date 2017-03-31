package net.rijento.clockwork_mechanicals.lib.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;
import net.rijento.clockwork_mechanicals.lib.ContainerConfigurator;

public class GuiConfiguratorBase extends GuiContainer implements IInteractionObject 
{
	private static final ResourceLocation basicTexture = new ResourceLocation("clockwork_mechanicals:textures/gui/configuratorui_base.png");
	
	private final InventoryPlayer playerInventory;
	
	public GuiConfiguratorBase(InventoryPlayer inventory, World worldIn)
	{
		super(new ContainerConfigurator(inventory));
		this.playerInventory = inventory;
		this.ySize = 133;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) 
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(basicTexture);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasCustomName() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ITextComponent getDisplayName() {
		
		return new TextComponentTranslation("Configurator", new Object[0]);
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
		return new ContainerConfigurator(playerInventory);
	}

	@Override
	public String getGuiID() {
		// TODO Auto-generated method stub
		return null;
	}

}
