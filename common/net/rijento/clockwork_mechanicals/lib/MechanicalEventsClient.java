package net.rijento.clockwork_mechanicals.lib;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.init.Blocks;
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
            VertexBuffer buffer = tessellator.getBuffer();

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
            	BlockPos block = order.pos;
            	double playerDist = Minecraft.getMinecraft().player.getDistance(block.getX(), block.getY(), block.getZ());
            	if (playerDist > 50.0)
    			{
    				return;
    			}
            	IBlockState state = world.getBlockState(block);
                double renderX = block.getX() - offsetX;
                double renderY = block.getY() - offsetY +1;
                double renderZ = block.getZ() - offsetZ;
                
                
                AxisAlignedBB boundingBox = new AxisAlignedBB(renderX, renderY, renderZ, renderX + 1, renderY + 1, renderZ + 1).expand(-0.2, -0.2, -0.2);
                float colour = 1F;
                List<BakedQuad> blockQuad = ConfiguratorRenderingUtils.getQuad(state);
                TextureAtlasSprite spriteDown = null;
                TextureAtlasSprite spriteEast = null;
                TextureAtlasSprite spriteNorth = null;
                TextureAtlasSprite spriteSouth = null;
                TextureAtlasSprite spriteUp = null;
                TextureAtlasSprite spriteWest = null;
                for (BakedQuad quad : blockQuad)
        		{
                	switch (quad.getFace())
                	{
					case DOWN:
						spriteDown = quad.getSprite();
						continue;
					case EAST:
						spriteEast = quad.getSprite();
						continue;
					case NORTH:
						spriteNorth = quad.getSprite();
						continue;
					case SOUTH:
						spriteSouth = quad.getSprite();
						continue;
					case UP:
						spriteUp = quad.getSprite();
						continue;
					case WEST:
						spriteWest = quad.getSprite();
						continue;
					default:
						break;
                	}
        		}
                
                
                buffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
                buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).color(colour, colour, colour, colour).endVertex();
                buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).color(colour, colour, colour, colour).endVertex();
                buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).color(colour, colour, colour, colour).endVertex();
                buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).color(colour, colour, colour, colour).endVertex();
                buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).color(colour, colour, colour, colour).endVertex();
                tessellator.draw();
                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
                if (spriteUp == null)
                {
                	spriteUp = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/farmland_wet");
                }
                buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).tex(spriteUp.getInterpolatedU(16), spriteUp.getInterpolatedV(16)).endVertex();
                buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).tex(spriteUp.getInterpolatedU(16), spriteUp.getInterpolatedV(0)).endVertex();
                buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).tex(spriteUp.getInterpolatedU(0), spriteUp.getInterpolatedV(0)).endVertex();
                buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).tex(spriteUp.getInterpolatedU(0), spriteUp.getInterpolatedV(16)).endVertex();
                
                tessellator.draw();
                buffer.begin(1, DefaultVertexFormats.POSITION_COLOR);
                buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).color(colour, colour, colour, colour).endVertex();
                buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).color(colour, colour, colour, colour).endVertex();
                buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).color(colour, colour, colour, colour).endVertex();
                buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).color(colour, colour, colour, colour).endVertex();
                buffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).color(colour, colour, colour, colour).endVertex();
                buffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).color(colour, colour, colour, colour).endVertex();
                buffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).color(colour, colour, colour, colour).endVertex();
                buffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).color(colour, colour, colour, colour).endVertex();
                tessellator.draw();
             
            }
		}
	}
}
