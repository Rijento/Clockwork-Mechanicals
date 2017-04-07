package net.rijento.clockwork_mechanicals.lib.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.rijento.clockwork_mechanicals.items.ItemMechanicalConfigurator;
import net.rijento.clockwork_mechanicals.lib.ContainerConfigurator;

public class GuiConfiguratorBase extends GuiContainer
{
	private static final ResourceLocation basicTexture = new ResourceLocation("clockwork_mechanicals:textures/gui/configuraturui_base.png");
	private static final ResourceLocation farmTexture = new ResourceLocation("clockwork_mechanicals:textures/gui/configuraturui_harvest.png");
	private static final ResourceLocation chopTexture = new ResourceLocation("clockwork_mechanicals:textures/gui/configuraturui_chop.png");
	
	private final ItemStack configuratorStack;
	private int selected = 0;
	private Button buttonHarvest;
	private Button buttonChop;
	
	public GuiConfiguratorBase(InventoryPlayer inventory, ItemStack configuratorIn, World worldIn)
	{
		super(new ContainerConfigurator(inventory));
		this.configuratorStack = configuratorIn;
		this.selected = configuratorStack.getTagCompound().getInteger("current_task");
		this.xSize = 178;
		this.ySize = 136;
	}
	@Override
	public void initGui()
    {
        super.initGui();
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        buttonList.clear();
        buttonList.add(buttonHarvest = new Button(0,i+16,j+7));
        buttonList.add(buttonChop = new Button(1,i+34,j+7));
        
    }

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) 
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		if (selected == 0){this.mc.getTextureManager().bindTexture(basicTexture);}
		else if (selected == 1){this.mc.getTextureManager().bindTexture(farmTexture);}
		else if (selected == 2){this.mc.getTextureManager().bindTexture(chopTexture);}
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);

	}
	@Override
	public void updateScreen()
    {
        super.updateScreen();
    }
	
	@Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
	
	@Override
    protected void actionPerformed(GuiButton parButton) 
    {
		if (parButton == buttonHarvest)
		{
			((ItemMechanicalConfigurator)configuratorStack.getItem()).setCurrentTask(1);
			selected = 1;
		}
		else if (parButton == buttonChop)
		{
			((ItemMechanicalConfigurator)configuratorStack.getItem()).setCurrentTask(2);
			selected = 2;
		}
    }

	@SideOnly (Side.CLIENT)
	static class Button extends GuiButton
	{

		public Button(int buttonId, int x, int y) {
			super(buttonId, x, y, 18, 18, "");
			// TODO Auto-generated constructor stub
		}
		@Override
        public void drawButton(Minecraft mc, int parX, int parY)
        {
			
        }
		
	}

}
