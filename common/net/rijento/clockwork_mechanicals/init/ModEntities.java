package net.rijento.clockwork_mechanicals.init;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.rijento.clockwork_mechanicals.ClockworkMechanicals;
import net.rijento.clockwork_mechanicals.entities.EntityMechanicalWorker;

public class ModEntities
{
	private static int NEXT_ID = 0;
	
	public static void preInit()
	{
		registerLivingEntity(EntityMechanicalWorker.class, "mechanical_worker");
	}
	
	
	public static void registerLivingEntity(Class<? extends Entity> entity, String id)
	{
		EntityRegistry.registerModEntity(new ResourceLocation(ClockworkMechanicals.RESOURCE_PREFIX + id), entity, id, NEXT_ID++, ClockworkMechanicals.instance, 80, 3, false);
	}
}
