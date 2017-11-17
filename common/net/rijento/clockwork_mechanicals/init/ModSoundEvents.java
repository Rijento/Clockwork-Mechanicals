package net.rijento.clockwork_mechanicals.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.rijento.clockwork_mechanicals.ClockworkMechanicals;

@ObjectHolder(ClockworkMechanicals.MOD_ID)
public class ModSoundEvents
{
	@ObjectHolder("entity.mechanical_worker.tick")
	public static final SoundEvent WORKER_TICK = createSoundEvent("entity.mechanical_worker.tick");
	@ObjectHolder("entity.mechanical_worker.wind")
	public static final SoundEvent WORKER_WIND = createSoundEvent("entity.mechanical_worker.wind");
	@ObjectHolder("entity.mechanical_worker.wind_end")
	public static final SoundEvent WORKER_WIND_END = createSoundEvent("entity.mechanical_worker.wind_end");
	@ObjectHolder("entity.mechanical_worker.install_mainspring")
	public static final SoundEvent WORKER_MAINSPRING_INSTALL = createSoundEvent("entity.mechanical_worker.install_mainspring");
	
	
	private static SoundEvent createSoundEvent(String name)
	{
		final ResourceLocation soundID = new ResourceLocation(ClockworkMechanicals.MOD_ID, name);
		return new SoundEvent(soundID).setRegistryName(soundID);
	}
	
	@Mod.EventBusSubscriber
	public static class RegistrationHandler
	{
		@SubscribeEvent
		public static void registerSoundEvents(RegistryEvent.Register<SoundEvent> event)
		{
			event.getRegistry().registerAll(
					WORKER_TICK,
					WORKER_WIND,
					WORKER_WIND_END,
					WORKER_MAINSPRING_INSTALL
					);
		}
	}
}
