package nl.imine.pixelmon.helpinghand.effect;

import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import nl.imine.pixelmon.helpinghand.badge.BadgeDataProvider;
import nl.imine.pixelmon.helpinghand.badge.BadgeRequired;

import java.util.List;

public abstract class EffectBreakBlock implements BadgeRequired {

    private final BadgeDataProvider badgeDataProvider;
    private final AttackBase requiredMove;
    private final List<Block> affectedBlocks;

    public EffectBreakBlock(BadgeDataProvider badgeDataProvider, AttackBase requiredMove, List<Block> affectedBlocks) {
        this.badgeDataProvider = badgeDataProvider;
        this.requiredMove = requiredMove;
        this.affectedBlocks = affectedBlocks;
    }

    public final boolean breakBlock(EntityLivingBase user, BlockPos pos) {
        if (!hasPlayerObtainedBadge(user) || !canBreakBlock(user.world.getBlockState(pos)))
            return false;
        doBreakBlock(user.world, pos);
        return true;
    }

    private boolean canBreakBlock(IBlockState blockState) {
        return affectedBlocks.contains(blockState.getBlock());
    }

    private boolean hasPlayerObtainedBadge(EntityLivingBase user) {
        return badgeDataProvider.hasBadge(user.getUniqueID(), getRequiredBadge());
    }

    protected abstract void doBreakBlock(World world, BlockPos blockPos);

}
