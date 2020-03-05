package net.rijento.clockwork_mechanicals.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelIronGolem;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerIronGolemFlower;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.util.ResourceLocation;
import net.rijento.clockwork_mechanicals.entities.EntityMechanicalWorker;

public class RenderMechanicalWorker extends RenderLiving<EntityMechanicalWorker>
{
	private static final ResourceLocation IRON_GOLEM_TEXTURES = new ResourceLocation("textures/entity/iron_golem.png");

    public RenderMechanicalWorker(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelMechanicalWorker(0.4F), 0.5F);
    }

    protected void applyRotations(EntityMechanicalWorker entityLiving, float p_77043_2_, float p_77043_3_, float partialTicks)
    {
        super.applyRotations(entityLiving, p_77043_2_, p_77043_3_, partialTicks);

        if ((double)entityLiving.limbSwingAmount >= 0.01D)
        {
            float f = 13.0F;
            float f1 = entityLiving.limbSwing - entityLiving.limbSwingAmount * (1.0F - partialTicks) + 6.0F;
            float f2 = (Math.abs(f1 % 13.0F - 6.5F) - 3.25F) / 3.25F;
            GlStateManager.rotate(6.5F * f2, 0.0F, 0.0F, 1.0F);
        }
    }

	@Override
	protected ResourceLocation getEntityTexture(EntityMechanicalWorker entity) {
		return IRON_GOLEM_TEXTURES;
	}
	
	protected void preRenderCallback(EntityMechanicalWorker entitylivingbaseIn, float partialTickTime)
    {
        GlStateManager.scale(0.4F, 0.4F, 0.4F);
    }
}