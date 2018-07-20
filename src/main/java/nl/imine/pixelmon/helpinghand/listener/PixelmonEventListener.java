package nl.imine.pixelmon.helpinghand.listener;

import com.pixelmonmod.pixelmon.api.events.ExternalMoveEvent;
import com.pixelmonmod.pixelmon.api.events.PixelmonSendOutEvent;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Optional;

public class PixelmonEventListener {

    @SubscribeEvent
    public void onSendOutPixelmon(PixelmonSendOutEvent evt) {
        if(!evt.isCanceled()) {

        }
    }
}
