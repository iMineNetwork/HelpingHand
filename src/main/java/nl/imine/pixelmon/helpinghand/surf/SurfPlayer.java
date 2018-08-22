package nl.imine.pixelmon.helpinghand.surf;

import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.storage.PixelmonStorage;
import com.pixelmonmod.pixelmon.storage.PlayerStorage;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

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
        logger.info("Disembarking Player");
        Vec3d surfNpcPosition = surfNpc.getPositionVector();
        ((Player) player).getVehicle().ifPresent(org.spongepowered.api.entity.Entity::clearPassengers);
        player.setPosition(surfNpcPosition.x, surfNpcPosition.y, surfNpcPosition.z);
        player.getEntityData().setBoolean("allowDismount", true);
    }

    private void attemptEmbarkPlayer() {
        logger.info("Embarking Player");
        PixelmonStorage.pokeBallManager.getPlayerStorage((EntityPlayerMP) player).ifPresent(playerStorage -> {
            Optional<EntityPixelmon> oPokemonWithSurf = getAbleSurfablePokemon(playerStorage);
            if (oPokemonWithSurf.isPresent())
                embarkPlayer(oPokemonWithSurf.get());
            else
                sendPlayerNoSurfPokemonMessage();
        });
    }

    private Optional<EntityPixelmon> getAbleSurfablePokemon(PlayerStorage playerStorage) {
        return Stream.of(playerStorage.partyPokemon)
                .filter(Objects::nonNull)
                .map(pokemon -> PixelmonEntityList.createEntityFromNBT(pokemon, player.getEntityWorld()))
                .filter(EntityPixelmon.class::isInstance)
                .map(EntityPixelmon.class::cast)
                .filter(this::pixelmonHasSurf)
                .findFirst();
    }

    protected boolean pixelmonHasSurf(EntityPixelmon entityPixelmon) {
        return Stream.of(entityPixelmon.getMoveset().attacks)
                .filter(Objects::nonNull)
                .map(attack -> attack.baseAttack)
                .anyMatch(baseAttack -> AttackBase.getAttackBase("Surf").map(baseAttack::equals).orElse(false));
    }

    private void sendPlayerNoSurfPokemonMessage() {
        player.sendMessage(new TextComponentString("Ye don't have a Pokémon that knoweth to surfeth"));
    }

    private void embarkPlayer(EntityPixelmon pixelmon) {
        findDirectionBlockBelowSurfNpc().ifPresent(block -> {
            EnumFacing facing = block.getValue(BlockHorizontal.FACING);
            Vec3d locationToTeleportTo = player.getPositionVector().add(multiplyVector(facing.getDirectionVec(), new Vec3d(-3, 0, -3)));
            EntityPixelmon entityPixelmon = spawnWildClone(pixelmon, player.getEntityWorld(), locationToTeleportTo);
            player.startRiding(entityPixelmon);
            entityPixelmon.updatePassenger(player);
        });
    }

    private Optional<IBlockState> findDirectionBlockBelowSurfNpc() {
        Vec3d positionVector = surfNpc.getPositionVector();
        for (int i = (int) positionVector.y; i > 0; i--) {
            IBlockState blockState = surfNpc.getEntityWorld().getBlockState(new BlockPos(new Vec3i(positionVector.x, i, positionVector.z)));
            if (blockState.getBlock().equals(Blocks.MAGENTA_GLAZED_TERRACOTTA))
                return Optional.of(blockState);
        }
        return Optional.empty();
    }

    private boolean isPlayerSurfing() {
        //TODO Check surf pokemon
        return player.isRiding();
    }

    private Vec3d multiplyVector(Vec3i original, Vec3d product) {
        return new Vec3d(original.getX() * product.x, original.getY() * product.y, original.getZ() * product.z);
    }

    private EntityPixelmon spawnWildClone(EntityPixelmon template, World world, Vec3d location) {
        EntityPixelmon pokemon = PokemonSpec.from(template.getPokemonName()).create(world);
        pokemon.setPosition(location.x, location.y, location.z);
        pokemon.canDespawn = false;
        pokemon.setSpawnLocation(pokemon.getDefaultSpawnLocation());
        pokemon.getEntityData().setBoolean(SurfListener.NBT_TAG_MOUNT, true);
        world.spawnEntity(pokemon);
        return pokemon;
    }
}
