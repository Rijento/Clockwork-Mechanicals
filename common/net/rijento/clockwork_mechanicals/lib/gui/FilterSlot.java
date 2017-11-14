package net.rijento.clockwork_mechanicals.lib.gui;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;

public class FilterSlot extends GhostSlot {
	protected DecrementButton decrementBtn;
	protected IncrementButton incrementBtn;
	public boolean advanced;

	public FilterSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
		incrementBtn = new IncrementButton(0, xPosition, yPosition, this.getStack());
		decrementBtn = new DecrementButton(1, xPosition + 13, yPosition, this.getStack());
		advanced = false;
	}
	
	public boolean isMouseOver(int mx, int my)
	{
		if (advanced)
		{
			return mx >= xPos && mx < (xPos + 16) && my >= yPos && my < (yPos + 16) && !incrementBtn.isMouseOver() && !decrementBtn.isMouseOver();
		}
		else
		{
			return mx >= xPos && mx < (xPos + 16) && my >= yPos && my < (yPos + 16);
		}
	}
	
	@Override
	public void draw(Minecraft mc, RenderItem itemRender, int guiLeft, int guiTop, int mouseX, int mouseY)
	{
		if (advanced)
		{
		    itemRender.zLevel = -25.0F;
		    GL11.glEnable(GL11.GL_LIGHTING);
		    GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		    GL11.glEnable(GL11.GL_DEPTH_TEST);
		    RenderHelper.enableGUIStandardItemLighting();
		    ItemStack stack = this.getStack();
		    if (stack != null)
		    {
		    	itemRender.renderItemAndEffectIntoGUI(stack, this.xPos + guiLeft + 1, this.yPos + guiTop + 1);
		    	itemRender.renderItemOverlays(mc.fontRenderer, stack, this.xPos + guiLeft + 1, this.yPos + guiTop + 1);
		    	GL11.glDisable(GL11.GL_LIGHTING);
		        GL11.glDisable(GL11.GL_DEPTH_TEST);
		        GL11.glEnable(GL11.GL_BLEND);
		        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.3F);
		        drawGradientRect(this.xPos + guiLeft + 1, this.yPos + guiTop + 1, this.xPos + guiLeft + 16 + 1, this.yPos + guiTop + 16 + 1, 0.0, 0x60e68b8b, 0x60e68b8b);
		        GL11.glEnable(GL11.GL_DEPTH_TEST);
		        GL11.glEnable(GL11.GL_LIGHTING);
		    }
		    if (this.isMouseOver(mouseX - guiLeft, mouseY - guiTop))
		    {
		    	GL11.glDisable(GL11.GL_LIGHTING);
		        GL11.glDisable(GL11.GL_DEPTH_TEST);
		        GL11.glColorMask(true, true, true, true);
		        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.3F);
		        drawGradientRect(this.xPos + guiLeft + 1, this.yPos + guiTop + 1, this.xPos + guiLeft + 16 + 1, this.yPos + guiTop + 16 + 1, 0.0, 0x80FFFFFF, 0x80FFFFFF);
		        GL11.glColorMask(true, true, true, true);
		        GL11.glEnable(GL11.GL_DEPTH_TEST);
		        GL11.glEnable(GL11.GL_LIGHTING);
		    }
		    incrementBtn.drawButton(mc, mouseX, mouseY, guiLeft, guiTop);
		    decrementBtn.drawButton(mc, mouseX, mouseY, guiLeft, guiTop);
		    
		    GL11.glDisable(GL11.GL_LIGHTING);
		    itemRender.zLevel = 0.0F;
		}
		else
		{
			super.draw(mc, itemRender, guiLeft, guiTop, mouseX, mouseY);
		}
	}
	@Override
    public int getSlotStackLimit()
    {
		if (this.advanced){ return 64;}
		else {return super.getSlotStackLimit();}
    }
	@Override
	void advancedToggle()
	{
		if(this.advanced) {this.getStack().setCount(1); this.advanced = false;}
		else {this.advanced = true;}
	}
	@Override
	void mouseClicked(int x, int y)
	{
		this.incrementBtn.mouseReleased(x, y);
		this.decrementBtn.mouseReleased(x, y);
		
	}
	@Override
	boolean advancedState()
	{
		return this.advanced;
	}
	@Override
    public void putStack(ItemStack stack)
	{
	    super.putStack(stack);
	    incrementBtn.setStack(this.getStack());
	    decrementBtn.setStack(this.getStack());
    }
}




class IncrementButton extends GuiButton
{
	private static final ResourceLocation TEXTURE = new ResourceLocation("clockwork_mechanicals:textures/gui/incdecbuttons.png");
	private ItemStack stack;
	public IncrementButton(int buttonId, int x, int y, ItemStack stackIn)
	{
		super(buttonId, x, y, 5, 5, "");
		stack = stackIn;
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY)
	{
		if (this.enabled && this.visible && this.hovered)
		{
			if (this.stack.getCount() < 64){this.stack.grow(1);}
		}
	}
	
	protected void setStack(ItemStack stackIn)
	{
		this.stack = stackIn;
	}
	
	public void drawButton(Minecraft mc, int mouseX, int mouseY, int guiLeft, int guiTop)
    {
		if (this.visible)
        {
			this.zLevel = 150;
            mc.getTextureManager().bindTexture(TEXTURE);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.x + guiLeft && mouseY >= this.y + guiTop && mouseX < this.x + guiLeft + this.width && mouseY < this.y + guiTop + this.height;
            this.drawTexturedModalRect(this.x + guiLeft, this.y + guiTop, 0, 0, 5, 5);
            this.mouseDragged(mc, mouseX, mouseY);
        }
    }
}

class DecrementButton extends GuiButton
{
	private static final ResourceLocation texture = new ResourceLocation("clockwork_mechanicals:textures/gui/incdecbuttons.png");
	private ItemStack stack;
	public DecrementButton(int buttonId, int x, int y, ItemStack stackIn)
	{
		super(buttonId, x, y, 5, 5, "");
		stack = stackIn;
	}
	
	protected void setStack(ItemStack stackIn)
	{
		this.stack = stackIn;
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY)
	{
		if (this.enabled && this.visible && this.hovered)
		{
			if (this.stack.getCount() > 1){this.stack.shrink(1);}
		}
	}

	public void drawButton(Minecraft mc, int mouseX, int mouseY, int guiLeft, int guiTop)
    {
		if (this.visible)
        {
			this.zLevel = 150;
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.x + guiLeft && mouseY >= this.y + guiTop && mouseX < this.x + guiLeft + this.width && mouseY < this.y + guiTop + this.height;
            mc.getTextureManager().bindTexture(texture);
            this.drawTexturedModalRect(this.x + guiLeft, this.y + guiTop, 0, 5, 5, 5);
            this.mouseDragged(mc, mouseX, mouseY);
        }
    }
}


