package net.rijento.clockwork_mechanicals.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.rijento.clockwork_mechanicals.ClockworkMechanicals;
import net.rijento.clockwork_mechanicals.init.ModItems;

public class BlockStampingTable extends Block 
{

	public BlockStampingTable()
	{
		super(Material.WOOD);
		setCreativeTab(ClockworkMechanicals.creativeTab);
	}

}
