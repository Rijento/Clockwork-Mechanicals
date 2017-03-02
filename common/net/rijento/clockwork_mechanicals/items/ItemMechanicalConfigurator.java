package net.rijento.clockwork_mechanicals.items;

import java.util.List;

import net.minecraft.item.Item;
import net.rijento.clockwork_mechanicals.lib.Order;

public class ItemMechanicalConfigurator extends Item
{
	private List<Order> orders;
	
	public ItemMechanicalConfigurator()
	{
		this.setMaxStackSize(1);
	}
	public List<Order> getOrders()
	{
		return this.orders;
	}
}
