package net.rijento.clockwork_mechanicals.lib.gui;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
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
	
	public void draw(Minecraft mc, RenderItem itemRender, int guiLeft, int guiTop, int mouseX, int mouseY)
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
	    	GL11.glDisable(GL11.GL_LIGHTING);
	        GL11.glDisable(GL11.GL_DEPTH_TEST);
	        GL11.glEnable(GL11.GL_BLEND);
	        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.3F);
	        drawGradientRect(this.xPos + guiLeft + 1, this.yPos + guiTop + 1, this.xPos + guiLeft + 16 + 1, this.yPos + guiTop + 16 + 1, 0.0, 0x608B8B8B, 0x608B8B8B);
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
	    GL11.glDisable(GL11.GL_LIGHTING);
	    itemRender.zLevel = 0.0F;
	}
	void advancedToggle(){}
	
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
    
    void mouseClicked(int x, int y){}
    boolean advancedState(){return false;}
    
    protected void drawGradientRect(int left, int top, int right, int bottom, double zLevel, int startColor, int endColor)
    {
        float f = (float)(startColor >> 24 & 255) / 255.0F;
        float f1 = (float)(startColor >> 16 & 255) / 255.0F;
        float f2 = (float)(startColor >> 8 & 255) / 255.0F;
        float f3 = (float)(startColor & 255) / 255.0F;
        float f4 = (float)(endColor >> 24 & 255) / 255.0F;
        float f5 = (float)(endColor >> 16 & 255) / 255.0F;
        float f6 = (float)(endColor >> 8 & 255) / 255.0F;
        float f7 = (float)(endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        vertexbuffer.pos((double)right, (double)top, zLevel).color(f1, f2, f3, f).endVertex();
        vertexbuffer.pos((double)left, (double)top, zLevel).color(f1, f2, f3, f).endVertex();
        vertexbuffer.pos((double)left, (double)bottom, zLevel).color(f5, f6, f7, f4).endVertex();
        vertexbuffer.pos((double)right, (double)bottom, zLevel).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
}
