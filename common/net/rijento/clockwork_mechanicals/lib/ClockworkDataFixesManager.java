package net.rijento.clockwork_mechanicals.lib;

import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.DataFixesManager;
import net.rijento.clockwork_mechanicals.entities.EntityMechanicalWorker;

public class ClockworkDataFixesManager extends DataFixesManager 
{
	public static DataFixer createFixer()
    {
		 DataFixer datafixer = new DataFixer(922);
	     datafixer = new net.minecraftforge.common.util.CompoundDataFixer(datafixer);
	     EntityMechanicalWorker.registerFixesWorker(datafixer);
	     return datafixer;
    }
}
