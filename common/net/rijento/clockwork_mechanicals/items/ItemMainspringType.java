package net.rijento.clockwork_mechanicals.items;

public enum ItemMainspringType
{
	BRASS(1.0f,1.0f,1.0f,100.0f,"Brass"),
	IRON(1.25f,0.9f,1.0f,100.0f,"Iron"),
	COPPER(0.9f,1.25f,1.0f,100.0f,"Copper"),
	ZINC(1.15f,0.95f,1.0f,100.0f,"Zinc"),
	GOLD(0.8f,1.30f,1.0f,100.0f,"Gold");
	
	private final float resistance;
	private final float flexability;
	private final float winding_cost;
	private final float max_tension;
	private final String id;
	
	ItemMainspringType(float resistance, float flexability, float winding_cost, float max_tension, String id)
	{
		this.resistance = resistance;
		this.flexability = flexability;
		this.winding_cost = winding_cost;
		this.max_tension = max_tension;
		this.id = id;
	}
	public float getResistance()
	{
		return this.resistance;
	}
	public float getFlexability()
	{
		return this.flexability;
	}
	public float getWindingCost()
	{
		return this.winding_cost*this.resistance/this.flexability;
	}
	public float getMaxTension()
	{
		return this.max_tension*this.flexability/this.resistance;
	}
	public String getID()
	{
		return this.id;
	}
	
	public static ItemMainspringType fromOrdinal(int ordinal)
	{
		ItemMainspringType[] mainspring = values();
		
		return mainspring[ordinal > mainspring.length || ordinal < 0 ? 0 : ordinal];
	}
}
