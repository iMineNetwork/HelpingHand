package nl.imine.pixelmon.helpinghand.listener;

import com.pixelmonmod.pixelmon.api.events.PixelmonSendOutEvent;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import nl.imine.pixelmon.helpinghand.effect.EffectFlash;

public class PixelmonEventListener {

    private final EffectFlash effectFlash;

    public PixelmonEventListener(EffectFlash effectFlash) {
        this.effectFlash = effectFlash;
    }

    @SubscribeEvent
    public void onSendOutPixelmon(PixelmonSendOutEvent evt) {
        if(!evt.isCanceled()) {
            effectFlash.attemptFlash(evt.player, getPixelmonFromNBT(evt.nbt, evt.player.world));
        }
    }

    private EntityPixelmon getPixelmonFromNBT(NBTTagCompound nbt, World world) {
        return (EntityPixelmon) PixelmonEntityList.createEntityFromNBT(nbt, world);
    }
}
