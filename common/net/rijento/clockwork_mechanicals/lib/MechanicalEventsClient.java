package net.rijento.clockwork_mechanicals.lib;

import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.rijento.clockwork_mechanicals.init.ModItems;
import net.rijento.clockwork_mechanicals.items.ItemMechanicalConfigurator;

public class MechanicalEventsClient
{
	@SubscribeEvent
	public void renderWorldEventClient(RenderWorldLastEvent event)
	{
		if(event.isCanceled()){return;}
		
		EntityPlayerSP player = Minecraft.getMinecraft().player;
		World world = player.getEntityWorld();
		ItemStack stack = player.getHeldItemMainhand();
		ItemStack offStack = player.getHeldItemOffhand();
		Minecraft mc = Minecraft.getMinecraft();
		float partialTicks = event.getPartialTicks();
		
		
		if (stack != null && stack.getItem() == ModItems.mechanical_configurator) {
			
            List<Order> orders = ((ItemMechanicalConfigurator) stack.getItem()).getOrders(stack);
            

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();

            double offsetX = player.prevPosX + (player.posX - player.prevPosX) * (double) partialTicks;
            double offsetY = player.prevPosY + (player.posY - player.prevPosY) * (double) partialTicks;
            double offsetZ = player.prevPosZ + (player.posZ - player.prevPosZ) * (double) partialTicks;

            Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            GlStateManager.enableBlend();
            GlStateManager.enableDepth();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.color(1F, 1F, 1F, 1F);
            
            

            for (Order order : orders)
            {
            	String command = order.command;
            	BlockPos block = order.pos;
            	double playerDist = Minecraft.getMinecraft().player.getDistance(block.getX(), block.getY(), block.getZ());
            	if (playerDist > 50.0){continue;}
            	IBlockState state = world.getBlockState(block);
                double renderX = block.getX() - offsetX;
                double renderY = block.getY() - offsetY + 1;
                double renderZ = block.getZ() - offsetZ;
                
                
                switch (command)
                {
                case("dropoff"):
	                GlStateManager.pushMatrix();
	                GlStateManager.depthFunc(515);
	                GlStateManager.enableRescaleNormal();
	                GlStateManager.depthMask(true);
	            	Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("textures/entity/chest/normal.png"));
	            	GlStateManager.translate(renderX, renderY+1, renderZ+1);
	            	GlStateManager.scale(0.6F, -0.6F, -0.6F);
	            	//GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
	            	GlStateManager.translate(0.34, 0.35, 0.35);
	            	ModelChest chestClosed = new ModelChest();
	            	chestClosed.renderAll();
	            	GlStateManager.disableRescaleNormal();
	            	Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
	            	GlStateManager.popMatrix();
	            	continue;
                case("pickup"):
	                GlStateManager.pushMatrix();
	                GlStateManager.depthFunc(515);
	                GlStateManager.enableRescaleNormal();
	                GlStateManager.depthMask(true);
	            	Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("textures/entity/chest/normal.png"));
	            	GlStateManager.translate(renderX, renderY+1, renderZ+1);
	            	GlStateManager.scale(0.6F, -0.6F, -0.6F);
	            	//GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
	            	GlStateManager.translate(0.34, 0.35, 0.35);
	            	ModelChest chestOpen = new ModelChest();
	            	chestOpen.chestLid.rotateAngleX = -(45 * ((float)Math.PI / 2F));
	            	chestOpen.renderAll();
	            	GlStateManager.disableRescaleNormal();
	            	Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
	            	GlStateManager.popMatrix();
	            	continue;
                case("harvest"):
	            	TextureAtlasSprite spriteDown = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/dirt");
	            	TextureAtlasSprite spriteEast = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/dirt");
	            	TextureAtlasSprite spriteNorth = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/dirt");
	            	TextureAtlasSprite spriteSouth = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/dirt");
	            	TextureAtlasSprite spriteUp = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/farmland_wet");
	            	TextureAtlasSprite spriteWest = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/dirt");
                
	                AxisAlignedBB boundingBox = new AxisAlignedBB(renderX+1.075, renderY+1.075, renderZ+1.075, renderX + 0.125, renderY + 0.125, renderZ + 0.125).contract(0.2, 0.2, 0.2);
	                //Up
	                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	                buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).tex(spriteUp.getInterpolatedU(16), spriteUp.getInterpolatedV(16)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).tex(spriteUp.getInterpolatedU(16), spriteUp.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).tex(spriteUp.getInterpolatedU(0), spriteUp.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).tex(spriteUp.getInterpolatedU(0), spriteUp.getInterpolatedV(16)).endVertex();
	                tessellator.draw();
	                
	                //North
	                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	                buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).tex(spriteNorth.getInterpolatedU(16), spriteNorth.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).tex(spriteNorth.getInterpolatedU(0), spriteNorth.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).tex(spriteNorth.getInterpolatedU(0), spriteNorth.getInterpolatedV(16)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).tex(spriteNorth.getInterpolatedU(16), spriteNorth.getInterpolatedV(16)).endVertex();
	                tessellator.draw();
	                
	                //East
	                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	                buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).tex(spriteEast.getInterpolatedU(16), spriteEast.getInterpolatedV(16)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).tex(spriteEast.getInterpolatedU(16), spriteEast.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).tex(spriteEast.getInterpolatedU(0), spriteEast.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).tex(spriteEast.getInterpolatedU(0), spriteEast.getInterpolatedV(16)).endVertex();
	                tessellator.draw();
	                
	                //south
	                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	                buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).tex(spriteSouth.getInterpolatedU(0), spriteSouth.getInterpolatedV(16)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).tex(spriteSouth.getInterpolatedU(16), spriteSouth.getInterpolatedV(16)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).tex(spriteSouth.getInterpolatedU(16), spriteSouth.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).tex(spriteSouth.getInterpolatedU(0), spriteSouth.getInterpolatedV(0)).endVertex();
	                tessellator.draw();
	                
	                //west
	                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	                buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).tex(spriteWest.getInterpolatedU(16), spriteWest.getInterpolatedV(16)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).tex(spriteWest.getInterpolatedU(16), spriteWest.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).tex(spriteWest.getInterpolatedU(0), spriteWest.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).tex(spriteWest.getInterpolatedU(0), spriteWest.getInterpolatedV(16)).endVertex();
	                tessellator.draw();
	                
	                //Down
	                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	                buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).tex(spriteDown.getInterpolatedU(0), spriteDown.getInterpolatedV(16)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).tex(spriteDown.getInterpolatedU(0), spriteDown.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).tex(spriteDown.getInterpolatedU(16), spriteDown.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).tex(spriteDown.getInterpolatedU(16), spriteDown.getInterpolatedV(16)).endVertex();
	                tessellator.draw();
	                continue;
	                
                case("craft"):
	            	spriteDown = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/planks_oak");
	            	spriteEast = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/crafting_table_side");
	            	spriteNorth = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/crafting_table_side");
	            	spriteSouth = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/crafting_table_side");
	            	spriteUp = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/crafting_table_top");
	            	spriteWest = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/crafting_table_side");
	            	
                
	                boundingBox = new AxisAlignedBB(renderX+1.075, renderY+1.075, renderZ+1.075, renderX + 0.125, renderY + 0.125, renderZ + 0.125).contract(0.2, 0.2, 0.2);
	                //Up
	                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	                buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).tex(spriteUp.getInterpolatedU(16), spriteUp.getInterpolatedV(16)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).tex(spriteUp.getInterpolatedU(16), spriteUp.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).tex(spriteUp.getInterpolatedU(0), spriteUp.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).tex(spriteUp.getInterpolatedU(0), spriteUp.getInterpolatedV(16)).endVertex();
	                tessellator.draw();
	                
	                //North
	                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	                buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).tex(spriteNorth.getInterpolatedU(16), spriteNorth.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).tex(spriteNorth.getInterpolatedU(0), spriteNorth.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).tex(spriteNorth.getInterpolatedU(0), spriteNorth.getInterpolatedV(16)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).tex(spriteNorth.getInterpolatedU(16), spriteNorth.getInterpolatedV(16)).endVertex();
	                tessellator.draw();
	                
	                //East
	                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	                buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).tex(spriteEast.getInterpolatedU(16), spriteEast.getInterpolatedV(16)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).tex(spriteEast.getInterpolatedU(16), spriteEast.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).tex(spriteEast.getInterpolatedU(0), spriteEast.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).tex(spriteEast.getInterpolatedU(0), spriteEast.getInterpolatedV(16)).endVertex();
	                tessellator.draw();
	                
	                //south
	                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	                buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).tex(spriteSouth.getInterpolatedU(0), spriteSouth.getInterpolatedV(16)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).tex(spriteSouth.getInterpolatedU(16), spriteSouth.getInterpolatedV(16)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).tex(spriteSouth.getInterpolatedU(16), spriteSouth.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).tex(spriteSouth.getInterpolatedU(0), spriteSouth.getInterpolatedV(0)).endVertex();
	                tessellator.draw();
	                
	                //west
	                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	                buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).tex(spriteWest.getInterpolatedU(16), spriteWest.getInterpolatedV(16)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).tex(spriteWest.getInterpolatedU(16), spriteWest.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).tex(spriteWest.getInterpolatedU(0), spriteWest.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).tex(spriteWest.getInterpolatedU(0), spriteWest.getInterpolatedV(16)).endVertex();
	                tessellator.draw();
	                
	                //Down
	                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	                buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).tex(spriteDown.getInterpolatedU(0), spriteDown.getInterpolatedV(16)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).tex(spriteDown.getInterpolatedU(0), spriteDown.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).tex(spriteDown.getInterpolatedU(16), spriteDown.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).tex(spriteDown.getInterpolatedU(16), spriteDown.getInterpolatedV(16)).endVertex();
	                tessellator.draw();
	                continue;
                case("chop"):
	            	spriteDown = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/log_oak_top");
	            	spriteEast = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/log_oak");
	            	spriteNorth = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/log_oak");
	            	spriteSouth = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/log_oak");
	            	spriteUp = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/log_oak_top");
	            	spriteWest = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/log_oak");
	            	
                
	                boundingBox = new AxisAlignedBB(renderX+1.075, renderY+1.075, renderZ+1.075, renderX + 0.125, renderY + 0.125, renderZ + 0.125).contract(0.2, 0.2, 0.2);
	                //Up
	                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	                buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).tex(spriteUp.getInterpolatedU(16), spriteUp.getInterpolatedV(16)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).tex(spriteUp.getInterpolatedU(16), spriteUp.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).tex(spriteUp.getInterpolatedU(0), spriteUp.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).tex(spriteUp.getInterpolatedU(0), spriteUp.getInterpolatedV(16)).endVertex();
	                tessellator.draw();
	                
	                //North
	                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	                buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).tex(spriteNorth.getInterpolatedU(16), spriteNorth.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).tex(spriteNorth.getInterpolatedU(0), spriteNorth.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).tex(spriteNorth.getInterpolatedU(0), spriteNorth.getInterpolatedV(16)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).tex(spriteNorth.getInterpolatedU(16), spriteNorth.getInterpolatedV(16)).endVertex();
	                tessellator.draw();
	                
	                //East
	                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	                buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).tex(spriteEast.getInterpolatedU(16), spriteEast.getInterpolatedV(16)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).tex(spriteEast.getInterpolatedU(16), spriteEast.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).tex(spriteEast.getInterpolatedU(0), spriteEast.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).tex(spriteEast.getInterpolatedU(0), spriteEast.getInterpolatedV(16)).endVertex();
	                tessellator.draw();
	                
	                //south
	                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	                buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).tex(spriteSouth.getInterpolatedU(0), spriteSouth.getInterpolatedV(16)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).tex(spriteSouth.getInterpolatedU(16), spriteSouth.getInterpolatedV(16)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).tex(spriteSouth.getInterpolatedU(16), spriteSouth.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).tex(spriteSouth.getInterpolatedU(0), spriteSouth.getInterpolatedV(0)).endVertex();
	                tessellator.draw();
	                
	                //west
	                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	                buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).tex(spriteWest.getInterpolatedU(16), spriteWest.getInterpolatedV(16)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).tex(spriteWest.getInterpolatedU(16), spriteWest.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).tex(spriteWest.getInterpolatedU(0), spriteWest.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).tex(spriteWest.getInterpolatedU(0), spriteWest.getInterpolatedV(16)).endVertex();
	                tessellator.draw();
	                
	                //Down
	                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	                buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).tex(spriteDown.getInterpolatedU(0), spriteDown.getInterpolatedV(16)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).tex(spriteDown.getInterpolatedU(0), spriteDown.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).tex(spriteDown.getInterpolatedU(16), spriteDown.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).tex(spriteDown.getInterpolatedU(16), spriteDown.getInterpolatedV(16)).endVertex();
	                tessellator.draw();
	                continue;
	            case("mine"):
	            	TextureAtlasSprite spritePick = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:items/diamond_pickaxe");
	            	spriteDown = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/stone");
	            	spriteEast = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/stone");
	            	spriteNorth = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/stone");
	            	spriteSouth = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/stone");
	            	spriteUp = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/stone");
	            	spriteWest = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/stone");
	            	
	            
	                boundingBox = new AxisAlignedBB(renderX+1.075, renderY+1.075, renderZ+1.075, renderX + 0.125, renderY + 0.125, renderZ + 0.125).contract(0.2, 0.2, 0.2);
	                //Up
	                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	                buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).tex(spriteUp.getInterpolatedU(16), spriteUp.getInterpolatedV(16)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).tex(spriteUp.getInterpolatedU(16), spriteUp.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).tex(spriteUp.getInterpolatedU(0), spriteUp.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).tex(spriteUp.getInterpolatedU(0), spriteUp.getInterpolatedV(16)).endVertex();
	                tessellator.draw();
	                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	                buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).tex(spritePick.getInterpolatedU(16), spritePick.getInterpolatedV(16)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).tex(spritePick.getInterpolatedU(16), spritePick.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).tex(spritePick.getInterpolatedU(0), spritePick.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).tex(spritePick.getInterpolatedU(0), spritePick.getInterpolatedV(16)).endVertex();
	                tessellator.draw();
	                
	                //North
	                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	                buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).tex(spriteNorth.getInterpolatedU(16), spriteNorth.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).tex(spriteNorth.getInterpolatedU(0), spriteNorth.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).tex(spriteNorth.getInterpolatedU(0), spriteNorth.getInterpolatedV(16)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).tex(spriteNorth.getInterpolatedU(16), spriteNorth.getInterpolatedV(16)).endVertex();
	                tessellator.draw();
	                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	                buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).tex(spritePick.getInterpolatedU(16), spritePick.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).tex(spritePick.getInterpolatedU(0), spritePick.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).tex(spritePick.getInterpolatedU(0), spritePick.getInterpolatedV(16)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).tex(spritePick.getInterpolatedU(16), spritePick.getInterpolatedV(16)).endVertex();
	                tessellator.draw();
	                
	                //East
	                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	                buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).tex(spriteEast.getInterpolatedU(16), spriteEast.getInterpolatedV(16)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).tex(spriteEast.getInterpolatedU(16), spriteEast.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).tex(spriteEast.getInterpolatedU(0), spriteEast.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).tex(spriteEast.getInterpolatedU(0), spriteEast.getInterpolatedV(16)).endVertex();
	                tessellator.draw();
	                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	                buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).tex(spritePick.getInterpolatedU(16), spritePick.getInterpolatedV(16)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).tex(spritePick.getInterpolatedU(16), spritePick.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).tex(spritePick.getInterpolatedU(0), spritePick.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).tex(spritePick.getInterpolatedU(0), spritePick.getInterpolatedV(16)).endVertex();
	                tessellator.draw();
	                
	                //south
	               
	                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	                buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).tex(spriteSouth.getInterpolatedU(0), spriteSouth.getInterpolatedV(16)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).tex(spriteSouth.getInterpolatedU(16), spriteSouth.getInterpolatedV(16)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).tex(spriteSouth.getInterpolatedU(16), spriteSouth.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).tex(spriteSouth.getInterpolatedU(0), spriteSouth.getInterpolatedV(0)).endVertex();
	                tessellator.draw();
	                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	                buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).tex(spritePick.getInterpolatedU(0), spritePick.getInterpolatedV(16)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).tex(spritePick.getInterpolatedU(16), spritePick.getInterpolatedV(16)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).tex(spritePick.getInterpolatedU(16), spritePick.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).tex(spritePick.getInterpolatedU(0), spritePick.getInterpolatedV(0)).endVertex();
	                tessellator.draw();
	                
	                //west
	                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	                buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).tex(spriteWest.getInterpolatedU(16), spriteWest.getInterpolatedV(16)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).tex(spriteWest.getInterpolatedU(16), spriteWest.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).tex(spriteWest.getInterpolatedU(0), spriteWest.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).tex(spriteWest.getInterpolatedU(0), spriteWest.getInterpolatedV(16)).endVertex();
	                tessellator.draw();
	                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	                buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).tex(spritePick.getInterpolatedU(16), spritePick.getInterpolatedV(16)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).tex(spritePick.getInterpolatedU(16), spritePick.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).tex(spritePick.getInterpolatedU(0), spritePick.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).tex(spritePick.getInterpolatedU(0), spritePick.getInterpolatedV(16)).endVertex();
	                tessellator.draw();
	                
	                //Down
	                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	                buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).tex(spriteDown.getInterpolatedU(0), spriteDown.getInterpolatedV(16)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).tex(spriteDown.getInterpolatedU(0), spriteDown.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).tex(spriteDown.getInterpolatedU(16), spriteDown.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).tex(spriteDown.getInterpolatedU(16), spriteDown.getInterpolatedV(16)).endVertex();
	                tessellator.draw();
	                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	                buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).tex(spritePick.getInterpolatedU(0), spritePick.getInterpolatedV(16)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).tex(spritePick.getInterpolatedU(0), spritePick.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).tex(spritePick.getInterpolatedU(16), spritePick.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).tex(spritePick.getInterpolatedU(16), spritePick.getInterpolatedV(16)).endVertex();
	                tessellator.draw();
	                
	                Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
	                continue;
	            case("attack"):
	            	TextureAtlasSprite spriteSword = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:items/diamond_sword");
	            	spriteDown = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/mob_spawner");
	            	spriteEast = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/mob_spawner");
	            	spriteNorth = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/mob_spawner");
	            	spriteSouth = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/mob_spawner");
	            	spriteUp = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/mob_spawner");
	            	spriteWest = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/mob_spawner");
	            	
	            
	                boundingBox = new AxisAlignedBB(renderX+1.075, renderY+1.075, renderZ+1.075, renderX + 0.125, renderY + 0.125, renderZ + 0.125).contract(0.2, 0.2, 0.2);
	                //Up
	                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	                buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).tex(spriteUp.getInterpolatedU(16), spriteUp.getInterpolatedV(16)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).tex(spriteUp.getInterpolatedU(16), spriteUp.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).tex(spriteUp.getInterpolatedU(0), spriteUp.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).tex(spriteUp.getInterpolatedU(0), spriteUp.getInterpolatedV(16)).endVertex();
	                tessellator.draw();
	                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	                buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).tex(spriteSword.getInterpolatedU(16), spriteSword.getInterpolatedV(16)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).tex(spriteSword.getInterpolatedU(16), spriteSword.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).tex(spriteSword.getInterpolatedU(0), spriteSword.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).tex(spriteSword.getInterpolatedU(0), spriteSword.getInterpolatedV(16)).endVertex();
	                tessellator.draw();
	                
	                //North
	                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	                buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).tex(spriteNorth.getInterpolatedU(16), spriteNorth.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).tex(spriteNorth.getInterpolatedU(0), spriteNorth.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).tex(spriteNorth.getInterpolatedU(0), spriteNorth.getInterpolatedV(16)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).tex(spriteNorth.getInterpolatedU(16), spriteNorth.getInterpolatedV(16)).endVertex();
	                tessellator.draw();
	                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	                buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).tex(spriteSword.getInterpolatedU(16), spriteSword.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).tex(spriteSword.getInterpolatedU(0), spriteSword.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).tex(spriteSword.getInterpolatedU(0), spriteSword.getInterpolatedV(16)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).tex(spriteSword.getInterpolatedU(16), spriteSword.getInterpolatedV(16)).endVertex();
	                tessellator.draw();
	                
	                //East
	                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	                buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).tex(spriteEast.getInterpolatedU(16), spriteEast.getInterpolatedV(16)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).tex(spriteEast.getInterpolatedU(16), spriteEast.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).tex(spriteEast.getInterpolatedU(0), spriteEast.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).tex(spriteEast.getInterpolatedU(0), spriteEast.getInterpolatedV(16)).endVertex();
	                tessellator.draw();
	                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	                buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).tex(spriteSword.getInterpolatedU(16), spriteSword.getInterpolatedV(16)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).tex(spriteSword.getInterpolatedU(16), spriteSword.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).tex(spriteSword.getInterpolatedU(0), spriteSword.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).tex(spriteSword.getInterpolatedU(0), spriteSword.getInterpolatedV(16)).endVertex();
	                tessellator.draw();
	                
	                //south
	               
	                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	                buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).tex(spriteSouth.getInterpolatedU(0), spriteSouth.getInterpolatedV(16)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).tex(spriteSouth.getInterpolatedU(16), spriteSouth.getInterpolatedV(16)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).tex(spriteSouth.getInterpolatedU(16), spriteSouth.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).tex(spriteSouth.getInterpolatedU(0), spriteSouth.getInterpolatedV(0)).endVertex();
	                tessellator.draw();
	                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	                buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).tex(spriteSword.getInterpolatedU(0), spriteSword.getInterpolatedV(16)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).tex(spriteSword.getInterpolatedU(16), spriteSword.getInterpolatedV(16)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).tex(spriteSword.getInterpolatedU(16), spriteSword.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).tex(spriteSword.getInterpolatedU(0), spriteSword.getInterpolatedV(0)).endVertex();
	                tessellator.draw();
	                
	                //west
	                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	                buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).tex(spriteWest.getInterpolatedU(16), spriteWest.getInterpolatedV(16)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).tex(spriteWest.getInterpolatedU(16), spriteWest.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).tex(spriteWest.getInterpolatedU(0), spriteWest.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).tex(spriteWest.getInterpolatedU(0), spriteWest.getInterpolatedV(16)).endVertex();
	                tessellator.draw();
	                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	                buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).tex(spriteSword.getInterpolatedU(16), spriteSword.getInterpolatedV(16)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).tex(spriteSword.getInterpolatedU(16), spriteSword.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).tex(spriteSword.getInterpolatedU(0), spriteSword.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).tex(spriteSword.getInterpolatedU(0), spriteSword.getInterpolatedV(16)).endVertex();
	                tessellator.draw();
	                
	                //Down
	                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	                buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).tex(spriteDown.getInterpolatedU(0), spriteDown.getInterpolatedV(16)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).tex(spriteDown.getInterpolatedU(0), spriteDown.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).tex(spriteDown.getInterpolatedU(16), spriteDown.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).tex(spriteDown.getInterpolatedU(16), spriteDown.getInterpolatedV(16)).endVertex();
	                tessellator.draw();
	                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	                buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).tex(spriteSword.getInterpolatedU(0), spriteSword.getInterpolatedV(16)).endVertex();
	                buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).tex(spriteSword.getInterpolatedU(0), spriteSword.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).tex(spriteSword.getInterpolatedU(16), spriteSword.getInterpolatedV(0)).endVertex();
	                buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).tex(spriteSword.getInterpolatedU(16), spriteSword.getInterpolatedV(16)).endVertex();
	                tessellator.draw();
	                
	                Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
	                continue;
                }
            }
		}
	}
}
