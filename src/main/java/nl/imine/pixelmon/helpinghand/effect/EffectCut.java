package nl.imine.pixelmon.helpinghand.effect;

import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.enums.items.EnumBadges;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import nl.imine.pixelmon.helpinghand.badge.BadgeDataProvider;

import java.util.Collections;

public class EffectCut extends EffectBreakBlock {

    public static final String MOVE_NAME_CUT = "Cut";

    public EffectCut(BadgeDataProvider badgeDataProvider) {
        super(badgeDataProvider, AttackBase.getAttackBase(MOVE_NAME_CUT).orElseThrow(() -> new IllegalStateException("The AttackBase for move '" + MOVE_NAME_CUT + "' could not be found")), Collections.singletonList(Block.getBlockFromName("pixelmon:tree")));
    }

    @Override
    protected void doBreakBlock(World world, BlockPos blockPos) {
        world.destroyBlock(blockPos, false);
    }

    @Override
    public EnumBadges getRequiredBadge() {
        return EnumBadges.CascadeBadge;
    }

}
