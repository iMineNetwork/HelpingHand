package nl.imine.pixelmon.helpinghand.surf;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.BattleStartedEvent;
import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import com.pixelmonmod.pixelmon.api.events.RidePokemonEvent;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.entity.dismount.DismountTypes;
import org.spongepowered.api.event.entity.RideEntityEvent;
import org.spongepowered.api.event.filter.cause.Root;

import java.util.Arrays;
import java.util.List;

public class SurfListener {

    private static final Logger logger = LoggerFactory.getLogger(SurfListener.class);

    public static final String NBT_TAG_MOUNT = "isPlayerMount";

    @SubscribeEvent
    public void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        logger.info("{} interacted with {}", event.getEntityPlayer(), event.getTarget());
        if (isSurfNpc(event.getTarget()))
            new SurfPlayer(event.getEntityPlayer(), event.getTarget()).toggleSurfForPlayer();
        if (isEntityAMount(event.getEntity()))
            event.setCanceled(true);

    }

    @SubscribeEvent
    public void battleStarted(BattleStartedEvent event) {
        if (event.bc.isPvP())
            return;
        List<Entity> npcParticipants = Arrays.asList(event.participant1[0].getEntity(), event.participant2[0].getEntity());
        for (Entity entity : npcParticipants) {
            if (isEntityAMount(entity)) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onCatchStart(CaptureEvent.StartCapture event) {
        if (isEntityAMount(event.getPokemon()))
            event.setCanceled(true);
    }

    @Listener
    public void onDismount(RideEntityEvent.Dismount rideEntityEvent, @Root Player player) {
        logger.info("Allow Dismount for {}: {}", player.getName(), ((EntityPlayerMP)player).getEntityData().getBoolean("allowDismount"));
        if(!((EntityPlayerMP)player).getEntityData().getBoolean("allowDismount")) {
            rideEntityEvent.setCancelled(true);
        } else {
            ((EntityPlayerMP) player).getEntityData().setBoolean("allowDismount", true);
        }
    }

    private boolean isEntityAMount(Entity entity) {
        return entity.getEntityData().getBoolean(NBT_TAG_MOUNT);
    }

    private boolean isSurfNpc(Entity target) {
        return target.getName().equals("Surfing Sailor");
    }

}
