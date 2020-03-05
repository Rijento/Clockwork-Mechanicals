package net.rijento.clockwork_mechanicals.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.rijento.clockwork_mechanicals.ClockworkMechanicals;
import net.rijento.clockwork_mechanicals.init.ModItems;

public class BlockStampingTable extends BlockContainer
{

	public BlockStampingTable()
	{
		super(Material.WOOD);
		setCreativeTab(ClockworkMechanicals.creativeTab);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		// TODO Auto-generated method stub
		return null;
	}

}
