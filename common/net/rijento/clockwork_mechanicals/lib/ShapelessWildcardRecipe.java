package net.rijento.clockwork_mechanicals.lib;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.block.Block;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class ShapelessWildcardRecipe extends ShapelessRecipes
{
    protected List<Integer> inputused = new ArrayList<Integer>();
	
    public ShapelessWildcardRecipe(String group, NonNullList<Ingredient> input, @Nonnull ItemStack result)
    {
    	super(group, result, input);
    }
 
    @Override
	public boolean matches(InventoryCrafting inv, World worldIn)
	{
		int ingredientCount = 0;

        for (int i = 0; i < inv.getSizeInventory(); ++i)
        {
            ItemStack itemstack = inv.getStackInSlot(i);
            if (!itemstack.isEmpty())
            {
                ++ingredientCount;
            }
        }
        if (!(ingredientCount == this.getIngredients().size())){return false;}
		int width = inv.getWidth();
		int height = inv.getHeight();
		for (int ing = 0; ing < this.getIngredients().size(); ing++)
		{
			boolean flag = false;
			for (int i = 0; i < inv.getSizeInventory(); ++i)
	        {
            	ItemStack itemstack = inv.getStackInSlot(i);
                Ingredient target = Ingredient.EMPTY;
                target = this.getIngredients().get(ing);

                if (target.apply(itemstack) && !inputused.contains(i))
                {
                	inputused.add(i);
                	flag = true;
                }
	        }
			if (!flag)
			{
				inputused.clear();
				return false;
			}
		}
		inputused.clear();
        return true;
	}
    
    public static class Factory implements IRecipeFactory
    {

		@Override
		public IRecipe parse(JsonContext context, JsonObject json)
		{
			final String group = JsonUtils.getString(json, "group", "");
			final NonNullList<Ingredient> ingredients = NonNullList.create();
			for (JsonElement ele : JsonUtils.getJsonArray(json, "ingredients"))
	            ingredients.add(CraftingHelper.getIngredient(ele, context));
			if (ingredients.isEmpty())
	            throw new JsonParseException("No ingredients for shapeless recipe");
			final ItemStack result = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "result"), context);
			return new ShapelessWildcardRecipe(group, ingredients, result);
		}
    	
    }
}
