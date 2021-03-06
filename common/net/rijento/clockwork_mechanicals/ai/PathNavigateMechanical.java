package net.rijento.clockwork_mechanicals.ai;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PathNavigateMechanical extends PathNavigate {

	public PathNavigateMechanical(EntityLiving entitylivingIn, World worldIn) {
		super(entitylivingIn, worldIn);
		
	}


    @Override
    protected PathFinder getPathFinder()
    {
        this.nodeProcessor = new WalkNodeProcessor();
        this.nodeProcessor.setCanEnterDoors(true);
        return new PathFinder(this.nodeProcessor);
    }
    @Override
    public float getPathSearchRange()
    {
        return 200f;
    }
    
    @Override
    public void onUpdateNavigation()
    {
    	if (this.entity.isWet()) {this.clearPath();}
    	super.onUpdateNavigation();
    }
    /**
     * If on ground or swimming and can swim
     */

    @Override
    protected boolean canNavigate()
    {
        return true; //this.theEntity.onGround;
    }


    @Override
    protected Vec3d getEntityPosition()
    {
        return new Vec3d(this.entity.posX, (double)this.getPathablePosY(), this.entity.posZ);
    }

    /**
     * Returns path to given BlockPos
     */
    @Override
    public Path getPathToPos(BlockPos pos)
    {
        if (this.world.getBlockState(pos).getMaterial() == Material.AIR)
        {
            BlockPos blockpos;

            for (blockpos = pos.down(); blockpos.getY() > 0 && this.world.getBlockState(blockpos).getMaterial() == Material.AIR; blockpos = blockpos.down())
            {
                ;
            }

            if (blockpos.getY() > 0)
            {
                return super.getPathToPos(blockpos);
            }

            while (blockpos.getY() < this.world.getHeight() && this.world.getBlockState(blockpos).getMaterial() == Material.AIR)
            {
                blockpos = blockpos.up();
            }

            pos = blockpos;
        }

        if (!this.world.getBlockState(pos).getMaterial().isSolid())
        {
            return super.getPathToPos(pos);
        }
        else
        {
            BlockPos blockpos1;

            for (blockpos1 = pos.up(); blockpos1.getY() < this.world.getHeight() && this.world.getBlockState(blockpos1).getMaterial().isSolid(); blockpos1 = blockpos1.up())
            {
                ;
            }
            return super.getPathToPos(blockpos1);
        }
    }

    /**
     * Returns the path to the given EntityLiving. Args : entity
     */

    @Override
    public Path getPathToEntityLiving(Entity entityIn)
    {
        return this.getPathToPos(new BlockPos(entityIn));
    }

    /**
     * Gets the safe pathing Y position for the entity depending on if it can path swim or not
     */
    
    private int getPathablePosY()
    {
        if (this.entity.isInWater())
        {
            int i = (int)this.entity.getEntityBoundingBox().minY;
            Block block = this.world.getBlockState(new BlockPos(MathHelper.floor(this.entity.posX), i, MathHelper.floor(this.entity.posZ))).getBlock();
            int j = 0;

            while (block == Blocks.FLOWING_WATER || block == Blocks.WATER)
            {
                ++i;
                block = this.world.getBlockState(new BlockPos(MathHelper.floor(this.entity.posX), i, MathHelper.floor(this.entity.posZ))).getBlock();
                ++j;

                if (j > 16)
                {
                    return (int)this.entity.getEntityBoundingBox().minY;
                }
            }

            return i;
        }
        else
        {
            return (int)(this.entity.getEntityBoundingBox().minY + 0.5D);
        }
    }
   @Override
   public boolean canEntityStandOnPos(BlockPos pos)
   {
       return true;
   }

    @Override
    protected boolean isDirectPathBetweenPoints(Vec3d posVec31, Vec3d posVec32, int sizeX, int sizeY, int sizeZ)
    {
    	return true;
    }
    @Override 
    protected void checkForStuck(Vec3d positionVec3)
    {
    
    }


 

}
