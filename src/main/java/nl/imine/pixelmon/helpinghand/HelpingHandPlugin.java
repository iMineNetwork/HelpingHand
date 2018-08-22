package nl.imine.pixelmon.helpinghand;

import com.pixelmonmod.pixelmon.Pixelmon;
import net.minecraftforge.common.MinecraftForge;
import nl.imine.pixelmon.helpinghand.badge.BadgeManager;
import nl.imine.pixelmon.helpinghand.effect.EffectCut;
import nl.imine.pixelmon.helpinghand.effect.EffectFlash;
import nl.imine.pixelmon.helpinghand.listener.PixelmonEventListener;
import nl.imine.pixelmon.helpinghand.move.CustomCut;
import nl.imine.pixelmon.helpinghand.surf.SurfListener;
import nl.imine.pixelmon.helpinghand.util.ExternalMoveRegistryEditor;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;

@Plugin(id = "helpinghand", name = "Helping Hand", version = "1.0", description = "Create events with Conditions and Actions", dependencies = @Dependency(id="pixelmon"))
public class HelpingHandPlugin {

    @Listener
    public void onFMLInitialized(GameStartedServerEvent gameStartedServerEvent) {
        ExternalMoveRegistryEditor externalMoveRegistryEditor = new ExternalMoveRegistryEditor();
        externalMoveRegistryEditor.clearExternalMoves();
        BadgeManager badgeDataProvider = new BadgeManager();
        externalMoveRegistryEditor.addCustomExternalMove(new CustomCut(new EffectCut(badgeDataProvider)));
        SurfListener surfListener = new SurfListener();
        MinecraftForge.EVENT_BUS.register(surfListener);
        Pixelmon.EVENT_BUS.register(surfListener);
        Sponge.getEventManager().registerListeners(this, surfListener);
        Pixelmon.EVENT_BUS.register(new PixelmonEventListener(new EffectFlash(badgeDataProvider)));
    }
}
