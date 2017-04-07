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
	private static final ResourceLocation slayTexture = new ResourceLocation("clockwork_mechanicals:textures/gui/configuraturui_slay.png");
	private static final ResourceLocation mineTexture = new ResourceLocation("clockwork_mechanicals:textures/gui/configuraturui_mine.png");
	private static final ResourceLocation withdrawTexture = new ResourceLocation("clockwork_mechanicals:textures/gui/configuraturui_take.png");
	private static final ResourceLocation depositTexture = new ResourceLocation("clockwork_mechanicals:textures/gui/configuraturui_deposit.png");
	private static final ResourceLocation craftTexture = new ResourceLocation("clockwork_mechanicals:textures/gui/configuraturui_craft.png");
	private static final ResourceLocation windTexture = new ResourceLocation("clockwork_mechanicals:textures/gui/configuraturui_wind.png");


	
	private final ItemStack configuratorStack;
	private int selected = 0;
	private Button buttonHarvest;
	private Button buttonChop;
	private Button buttonSlay;
	private Button buttonMine;
	private Button buttonTake;
	private Button buttonDeposit;
	private Button buttonCraft;
	private Button buttonWind;
	public GuiConfiguratorBase(InventoryPlayer inventory, ItemStack configuratorIn, World worldIn)
	{
		super(new ContainerConfigurator(inventory, configuratorIn));
		this.configuratorStack = configuratorIn;
		this.selected = configuratorStack.getTagCompound().getInteger("current_task");
		this.xSize = 176;
		this.ySize = 136;
	}
	@Override
	public void initGui()
    {
        super.initGui();
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        buttonList.clear();
        buttonList.add(buttonHarvest = new Button(1,i+16,j+7));
        buttonList.add(buttonChop = new Button(2,i+34,j+7));
        buttonList.add(buttonSlay = new Button(3,i+52,j+7));
        buttonList.add(buttonMine = new Button(4,i+70,j+7));
        buttonList.add(buttonTake = new Button(5,i+88,j+7));
        buttonList.add(buttonDeposit = new Button(6,i+106,j+7));
        buttonList.add(buttonCraft = new Button(7,i+124,j+7));
        buttonList.add(buttonWind = new Button(8,i+142,j+7));
        
    }

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) 
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		if (selected == 0){this.mc.getTextureManager().bindTexture(basicTexture);}
		else if (selected == 1){this.xSize = 176; this.ySize = 136; this.mc.getTextureManager().bindTexture(farmTexture);}
		else if (selected == 2){this.xSize = 176; this.ySize = 136; this.mc.getTextureManager().bindTexture(chopTexture);}
		else if (selected == 3){this.xSize = 176; this.ySize = 136; this.mc.getTextureManager().bindTexture(slayTexture);}
		else if (selected == 4){this.xSize = 176; this.ySize = 222; this.mc.getTextureManager().bindTexture(mineTexture);}
		else if (selected == 5){this.xSize = 176; this.ySize = 222; this.mc.getTextureManager().bindTexture(withdrawTexture);}
		else if (selected == 6){this.xSize = 176; this.ySize = 222; this.mc.getTextureManager().bindTexture(depositTexture);}
		else if (selected == 7){this.xSize = 176; this.ySize = 222; this.mc.getTextureManager().bindTexture(craftTexture);}
		else if (selected == 8){this.xSize = 176; this.ySize = 136; this.mc.getTextureManager().bindTexture(windTexture);}
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);

	}
	@Override
	public void updateScreen()
    {
        super.updateScreen();
        this.buttonList.clear();
        this.initGui();
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
			((ItemMechanicalConfigurator)configuratorStack.getItem()).current_task = 1;
			((ContainerConfigurator)this.inventorySlots).current_task = 1;
			selected = 1;
			((ContainerConfigurator) this.inventorySlots).update();
		}
		else if (parButton == buttonChop)
		{
			((ItemMechanicalConfigurator)configuratorStack.getItem()).current_task = 2;
			((ContainerConfigurator)this.inventorySlots).current_task = 2;
			selected = 2;
			((ContainerConfigurator) this.inventorySlots).update();
		}
		else if (parButton == buttonSlay)
		{
			((ItemMechanicalConfigurator)configuratorStack.getItem()).current_task = 3;
			((ContainerConfigurator)this.inventorySlots).current_task = 3;
			selected = 3;
			((ContainerConfigurator) this.inventorySlots).update();
		}
		else if (parButton == buttonMine)
		{
			((ItemMechanicalConfigurator)configuratorStack.getItem()).current_task = 4;
			((ContainerConfigurator)this.inventorySlots).current_task = 4;
			selected = 4;
			((ContainerConfigurator) this.inventorySlots).update();
		}
		else if (parButton == buttonTake)
		{
			((ItemMechanicalConfigurator)configuratorStack.getItem()).current_task = 5;
			((ContainerConfigurator)this.inventorySlots).current_task = 5;
			selected = 5;
			((ContainerConfigurator) this.inventorySlots).update();
		}
		else if (parButton == buttonDeposit)
		{
			((ItemMechanicalConfigurator)configuratorStack.getItem()).current_task = 6;
			((ContainerConfigurator)this.inventorySlots).current_task = 6;
			selected = 6;
			((ContainerConfigurator) this.inventorySlots).update();
		}
		else if (parButton == buttonCraft)
		{
			((ItemMechanicalConfigurator)configuratorStack.getItem()).current_task = 7;
			((ContainerConfigurator)this.inventorySlots).current_task = 7;
			selected = 7;
			((ContainerConfigurator) this.inventorySlots).update();
		}
		else if (parButton == buttonWind)
		{
			((ItemMechanicalConfigurator)configuratorStack.getItem()).current_task = 8;
			((ContainerConfigurator)this.inventorySlots).current_task = 8;
			selected = 8;
			((ContainerConfigurator) this.inventorySlots).update();
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
