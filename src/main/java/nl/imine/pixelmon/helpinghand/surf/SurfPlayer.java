package nl.imine.pixelmon.helpinghand.surf;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import java.util.Optional;

public class SurfPlayer {

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
        Block block = findDirectionBlockBelowSurfNpc();
        Vec3d locationToTeleportTo = player.getPositionVector().add((blockFacingVector * 3));
    }

    private boolean isPlayerSurfing() {
        //TODO Check surf pokemon
        return player.isRiding();
    }
}
