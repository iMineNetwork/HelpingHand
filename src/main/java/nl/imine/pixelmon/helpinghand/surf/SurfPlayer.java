package nl.imine.pixelmon.helpinghand.surf;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextComponentString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class SurfPlayer {

    private static final Logger logger = LoggerFactory.getLogger(SurfPlayer.class);

    private final EntityPlayer player;
    private final Entity surfNpc;

    public SurfPlayer(EntityPlayer player, Entity surfNpc) {
        this.player = player;
        this.surfNpc = surfNpc;
    }

    public void toggleSurfForPlayer() {
        if (isPlayerSurfing())
            disembarkPlayer();
        else
            attemptEmbarkPlayer();
    }

    private void disembarkPlayer() {
        Vec3d surfNpcPosition = surfNpc.getPositionVector();
        player.dismountRidingEntity();
        player.setPosition(surfNpcPosition.x, surfNpcPosition.y, surfNpcPosition.z);
    }

    private void attemptEmbarkPlayer() {
        Optional<EntityPixelmon> oPokemonWithSurf = getAbleSurfablePokemon();
        if (oPokemonWithSurf.isPresent())
            embarkPlayer();
        else
            sendPlayerNoSurfPokemonMessage();
    }

    private void sendPlayerNoSurfPokemonMessage() {
        player.sendMessage(new TextComponentString("Ye don't have a Pokémon that knoweth to surfeth"));
    }

    private void embarkPlayer() {
        Optional<IBlockState> oBlockState = findDirectionBlockBelowSurfNpc();
        if (oBlockState.isPresent()) {
            IBlockState block = oBlockState.get();
            EnumFacing facing = block.getValue(BlockHorizontal.FACING);
            Vec3d locationToTeleportTo = player.getPositionVector().add(new Vec3d(facing.getDirectionVec().crossProduct(new Vec3i(3,3,3))));
            player.attemptTeleport(locationToTeleportTo.x, locationToTeleportTo.y, locationToTeleportTo.z);
        } else {
            logger.error("SurferDude {} at {}, {}, {} has no set direction!",
                    surfNpc.getName(),
                    surfNpc.getPosition().getX(),
                    surfNpc.getPosition().getY(),
                    surfNpc.getPosition().getZ());
        }

    }

    private Optional<IBlockState> findDirectionBlockBelowSurfNpc() {
        Vec3d positionVector = surfNpc.getPositionVector();
        for(int i = (int) positionVector.y; i > 0; i--) {
            IBlockState blockState = surfNpc.getEntityWorld().getBlockState(new BlockPos(new Vec3i(positionVector.x, i, positionVector.z)));
            return Optional.of(blockState);
        }
        return Optional.empty();
    }

    private boolean isPlayerSurfing() {
        //TODO Check surf pokemon
        return player.isRiding();
    }
}
