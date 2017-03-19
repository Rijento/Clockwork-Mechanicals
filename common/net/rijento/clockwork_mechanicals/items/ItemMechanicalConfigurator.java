package net.rijento.clockwork_mechanicals.items;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.rijento.clockwork_mechanicals.entities.EntityMechanicalWorker;
import net.rijento.clockwork_mechanicals.lib.ConfiguratorRenderingUtils;
import net.rijento.clockwork_mechanicals.lib.Order;

public class ItemMechanicalConfigurator extends Item
{
	
	public ItemMechanicalConfigurator()
	{
		this.setMaxStackSize(1);
	}
	
	
	@SideOnly(Side.CLIENT)
	private void renderOrders(World world, ItemStack stack)
	{
		Order order = this.getOrders(stack).get(0);
		BlockPos pos = new BlockPos(order.pos.getX(),order.pos.getY(),order.pos.getZ());
		Block block = Block.REGISTRY.getObject(new ResourceLocation("minecraft:redstone_block"));
		double playerDist = Minecraft.getMinecraft().player.getDistance(pos.getX(), pos.getY(), pos.getZ());
		IBlockState stateAt = world.getBlockState(pos);
		boolean isEmpty = world.isAirBlock(pos);
		
		if (playerDist > 50.0)
		{
			return;
		}
		IBlockState state = block.getDefaultState();
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(pos.getX(),pos.getY()+1,pos.getZ());
		
		if (!isEmpty)
		{
			GlStateManager.disableDepth();
            GlStateManager.enableBlend();
            GlStateManager.alphaFunc(GL11.GL_GREATER, 0F);
		}
		GlStateManager.scale(0.8, 0.8, 0.8);
		GlStateManager.translate(0.1, 0.1, 0.1);
		float brightnessX = OpenGlHelper.lastBrightnessX;
        float brightnessY = OpenGlHelper.lastBrightnessY;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 150f, 150f);
		
        List<BakedQuad> blockQuad = ConfiguratorRenderingUtils.getQuad(state);
        ConfiguratorRenderingUtils.renderQuad(blockQuad);
        if (!isEmpty)
        {
        	GlStateManager.enableDepth();
            GlStateManager.disableBlend();
            GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
        }
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, brightnessX, brightnessY);
        GlStateManager.popMatrix();
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
		if (isSelected && worldIn.isRemote && !this.getOrders(stack).isEmpty())
		{
            this.renderOrders(worldIn, stack);
		}
    }
	
	@Override
	public boolean itemInteractionForEntity(ItemStack itemstack, net.minecraft.entity.player.EntityPlayer player, EntityLivingBase entity, net.minecraft.util.EnumHand hand)
	{
		if (entity instanceof EntityMechanicalWorker && player.isSneaking())
		{
			((EntityMechanicalWorker) entity).SetOrders(getOrders(itemstack),false);
			return true;
		}
		return false;
	}
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		ItemStack stack = player.getHeldItem(hand);
		addOrder(pos,"harvest",stack);
        return EnumActionResult.SUCCESS;
    }
	
	public void addOrder(BlockPos pos, String command, ItemStack stack)
	{
		if (!stack.hasTagCompound())
		{
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setTag("Orders", new NBTTagList());
		}
		NBTTagCompound toAdd = (new Order(pos,command)).getOrderNBT();
		stack.getTagCompound().getTagList("Orders", Constants.NBT.TAG_COMPOUND).appendTag(toAdd);
	}
	public List<Order> getOrders(ItemStack stack)
	{
		if (!stack.hasTagCompound())
		{
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setTag("Orders", new NBTTagList());
		}
		List<Order> orders = new ArrayList<Order>();
		for(int i = 0; i < stack.getTagCompound().getTagList("Orders", Constants.NBT.TAG_COMPOUND).tagCount(); i++)
		{
			orders.add(new Order((NBTTagCompound) stack.getTagCompound().getTagList("Orders", Constants.NBT.TAG_COMPOUND).get(i)));
		}
		return orders;
	}
}
