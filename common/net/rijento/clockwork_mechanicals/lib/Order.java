package net.rijento.clockwork_mechanicals.lib;

import net.minecraft.entity.ai.EntityAIBase;

public class Order 
{
	private final String direction;
	private final EntityAIBase command;
	
	public Order(String direction, EntityAIBase command)
	{
		this.direction = direction;
		this.command = command;
	}
	
}
