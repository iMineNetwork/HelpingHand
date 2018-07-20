package nl.imine.pixelmon.helpinghand.move;

import com.pixelmonmod.pixelmon.blocks.multiBlocks.BlockTree;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.externalMoves.Cut;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import nl.imine.pixelmon.helpinghand.effect.EffectCut;

public class CustomCut extends Cut {

    private final EffectCut effectCut;

    public CustomCut(EffectCut effectCut) {
        this.effectCut = effectCut;
    }

    @Override
    public boolean execute(EntityPixelmon pixelmon, RayTraceResult target, int moveIndex) {
        if (isPixelmonNotUserOwned(pixelmon) || isTargetABlock(target) || !isOwnerAPlayer(pixelmon))
            return false;
        BlockPos pos = target.getBlockPos();
        Block block = pixelmon.world.getBlockState(pos).getBlock();
        if (isBlockATree(block) && executorIsAPlayer(pixelmon.getOwner()))
            return effectCut.breakBlock((EntityPlayerMP) pixelmon.getOwner(), pos);
        return false;
    }

    private boolean executorIsAPlayer(EntityLivingBase owner) {
        return owner instanceof EntityPlayerMP;
    }

    private boolean isOwnerAPlayer(EntityPixelmon pixelmon) {
        return pixelmon.getOwner() instanceof EntityPlayerMP;
    }

    private boolean isBlockATree(Block block) {
        return (block instanceof BlockTree);
    }

    private boolean isTargetABlock(RayTraceResult target) {
        return target.typeOfHit != RayTraceResult.Type.BLOCK;
    }

    private boolean isPixelmonNotUserOwned(EntityPixelmon user) {
        return !user.playerOwned;
    }
}
