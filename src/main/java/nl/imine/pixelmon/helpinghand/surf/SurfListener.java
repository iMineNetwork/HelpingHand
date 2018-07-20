package nl.imine.pixelmon.helpinghand.surf;

import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SurfListener {

    @SubscribeEvent
    public void onEntityInteract(PlayerInteractEvent.EntityInteract entityInteractEvent) {
        if(isSurfNpc(entityInteractEvent.getTarget()))
            new SurfPlayer(entityInteractEvent.getEntityPlayer(), entityInteractEvent.getTarget()).toggleSurfForPlayer();
    }


    private boolean isSurfNpc(Entity target) {
        return true;
    }
}
