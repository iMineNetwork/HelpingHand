package nl.imine.pixelmon.helpinghand;

import com.pixelmonmod.pixelmon.Pixelmon;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import nl.imine.pixelmon.helpinghand.badge.BadgeManager;
import nl.imine.pixelmon.helpinghand.effect.EffectCut;
import nl.imine.pixelmon.helpinghand.listener.PixelmonEventListener;
import nl.imine.pixelmon.helpinghand.move.CustomCut;
import nl.imine.pixelmon.helpinghand.surf.SurfListener;
import nl.imine.pixelmon.helpinghand.util.ExternalMoveRegistryEditor;

@Mod(modid = "helpinghand", name = "Helping Hand", version = "1.0")
@IFMLLoadingPlugin.DependsOn("pixelmon")
public class HelpingHandPlugin {


    @Mod.EventHandler
    public void onServerStartEvent(FMLInitializationEvent gameStartingServerEvent) {
        ExternalMoveRegistryEditor externalMoveRegistryEditor = new ExternalMoveRegistryEditor();
        externalMoveRegistryEditor.clearExternalMoves();
        externalMoveRegistryEditor.addCustomExternalMove(new CustomCut(new EffectCut(new BadgeManager())));
        MinecraftForge.EVENT_BUS.register(new SurfListener());
        Pixelmon.EVENT_BUS.register(new PixelmonEventListener());
    }
}
