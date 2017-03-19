package net.rijento.clockwork_mechanicals.lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.client.model.obj.OBJModel;

public class ConfiguratorRenderingUtils implements IResourceManagerReloadListener 
{
	public static Map<IBlockState, List<BakedQuad>> quads = new HashMap<IBlockState, List<BakedQuad>>();
	public static Map<ResourceLocation, IBakedModel> models = new HashMap<ResourceLocation, IBakedModel>();
	
	public static List<BakedQuad> getQuad(IBlockState state)
	{
		if (!quads.containsKey(state))
		{
			IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state);
			List<BakedQuad> quadList = new ArrayList<BakedQuad>();
			
			if (model instanceof OBJModel.OBJBakedModel)
			{
				quadList.addAll(model.getQuads(state, null, 0));
			}
			else
			{
				for (EnumFacing face : EnumFacing.VALUES)
				{
					quadList.addAll(model.getQuads(state, face, 0));
				}
			}
			quads.put(state, quadList);
		}
		if (quads.containsKey(state))
		{
			return quads.get(state);
		}
		return new ArrayList<BakedQuad>();
	}
	
	public static void renderQuad(List<BakedQuad> quadList)
	{
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer vertexBuffer = tessellator.getBuffer();
		
		
		for (BakedQuad quad : quadList)
		{
			vertexBuffer.begin(7, DefaultVertexFormats.ITEM);
			vertexBuffer.addVertexData(quad.getVertexData());
			vertexBuffer.putColor4(0xFF000000);
			Vec3i vec3i = quad.getFace().getDirectionVec();
			vertexBuffer.putNormal((float) vec3i.getX(), (float) vec3i.getY(), (float) vec3i.getZ());
			tessellator.draw();
		}
		
	}
	
	
	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) 
	{
		quads.clear();
		models.clear();
	}
}
