package nl.imine.pixelmon.helpinghand.surf;

import com.flowpowered.math.vector.Vector3d;
import com.pixelmonmod.pixelmon.api.events.BattleStartedEvent;
import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.RideEntityEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class SurfListener {

    public static final String NBT_TAG_MOUNT = "isPlayerMount";

    private static final Logger logger = LoggerFactory.getLogger(SurfListener.class);

    private final Object pluginContainer;

    public SurfListener(Object pluginContainer) {
        this.pluginContainer = pluginContainer;
    }

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
        Optional<Location<World>> oDismountLocation = getDismountLocation(player);
        if (!oDismountLocation.isPresent()) {
            rideEntityEvent.setCancelled(true);
            return;
        }
        rideEntityEvent.getTargetEntity().remove();
        disembarkPlayer(player, oDismountLocation.get());
    }

    private Optional<Location<World>> getDismountLocation(Player player) {
        return findDirectionBlockBelowPlayer(player).map(directionBlock -> directionBlock.get(Keys.DIRECTION).orElse(null))
                .map(facing -> player.getLocation().add(facing.asOffset().mul(new Vector3d(-3, 0, -3))))
                .map(location -> location.add(new Vector3d(0, 2, 0)));
    }

    private void disembarkPlayer(Player player, Location<World> location) {
        Task.builder().delayTicks(1).execute(() -> {
            player.setLocation(location);
        }).submit(pluginContainer);
    }

    private Optional<BlockState> findDirectionBlockBelowPlayer(Player player) {
        Location<World> playerLocation = player.getLocation();
        for (int i = playerLocation.getBlockY(); i > 0; i--) {
            BlockState blockState = playerLocation.getExtent().getBlock(playerLocation.getBlockX(), i, playerLocation.getBlockZ());
            if (blockState.getType().equals(BlockTypes.MAGENTA_GLAZED_TERRACOTTA))
                return Optional.of(blockState);
        }
        return Optional.empty();
    }

    private boolean isEntityAMount(Entity entity) {
        return entity.getEntityData().getBoolean(NBT_TAG_MOUNT);
    }

    private boolean isSurfNpc(Entity target) {
        return target.getName().equals("Surfing Sailor");
    }

}
