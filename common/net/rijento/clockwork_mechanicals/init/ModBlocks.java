package net.rijento.clockwork_mechanicals.init;

import java.util.ArrayList;
import java.util.Collection;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespacedDefaultedByKey;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.rijento.clockwork_mechanicals.ClockworkMechanicals;
import net.rijento.clockwork_mechanicals.blocks.BlockStampingTable;

public class ModBlocks
{
	public static final Collection<Block> registeredBlocks = new ArrayList<>();
	
	public static final BlockStampingTable stamping_table = new BlockStampingTable();
	
	public static void preInit()
	{
		registerBlock("BlockStampingTable", stamping_table);
	}
	
//	@SideOnly(Side.CLIENT)
//	public static void initCLient(ItemModelMesher mesher)
//	{
//		
//	}
	
	private static <B extends Block> B registerBlock(String name, B block)
	{
		block.setUnlocalizedName(ClockworkMechanicals.RESOURCE_PREFIX + name);
		block.setRegistryName(ClockworkMechanicals.MOD_ID, name);
	
		ForgeRegistries.BLOCKS.register(block);
		registeredBlocks.add(block);
		return block;
	}
	
	private static void regModel(ItemModelMesher mesher, Block block, int meta, String file)
	{
		ModelResourceLocation model = new ModelResourceLocation(ClockworkMechanicals.RESOURCE_PREFIX + file, "inventory");
//		ModelLoader.registerItemVariants(block, model);
//		mesher.registe
	}
}