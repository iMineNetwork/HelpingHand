package nl.imine.pixelmon.helpinghand.effect;

import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.blocks.multiBlocks.BlockTree;
import com.pixelmonmod.pixelmon.enums.items.EnumBadges;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import nl.imine.pixelmon.helpinghand.badge.BadgeDataProvider;

import java.util.Collections;

public class EffectRockSmash extends EffectBreakBlock {

    public EffectRockSmash(BadgeDataProvider badgeDataProvider) {
        super(badgeDataProvider, AttackBase.getAttackBase("cut").orElseThrow(() -> new IllegalStateException("Move not found")), Collections.singletonList(BlockTree.getBlockFromName("pixelmon:boulder")));
    }

    @Override
    protected void doBreakBlock(World world, BlockPos blockPos) {
        world.destroyBlock(blockPos, false);
        //TODO Implement Fossils
    }

    @Override
    public EnumBadges getRequiredBadge() {
        return EnumBadges.VolcanoBadge;
    }
}
