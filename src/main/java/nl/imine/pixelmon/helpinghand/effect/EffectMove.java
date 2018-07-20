package nl.imine.pixelmon.helpinghand.effect;

import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.storage.PixelmonStorage;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public abstract class EffectMove {

    private final AttackBase requiredMove;

    public EffectMove(AttackBase requiredMove) {
        this.requiredMove = requiredMove;
    }

    protected Optional<EntityPixelmon> getPixelmonInPartyWithRequiredMove(EntityPlayerMP owner) {
        return PixelmonStorage.pokeBallManager.getPlayerStorage(owner)
                .map(Stream::of)
                .orElseGet(Stream::empty)
                .flatMap(playerStorage -> Stream.of(playerStorage.partyPokemon))
                .map(pokemon -> PixelmonEntityList.createEntityFromNBT(pokemon, owner.getEntityWorld()))
                .filter(EntityPixelmon.class::isInstance)
                .map(EntityPixelmon.class::cast)
                .filter(this::pixelmonHasRequiredMove)
                .findFirst();
    }

    protected boolean pixelmonHasRequiredMove(EntityPixelmon entityPixelmon) {
        return Stream.of(entityPixelmon.getMoveset().attacks)
                .filter(Objects::nonNull)
                .map(attack -> attack.baseAttack)
                .anyMatch(getRequiredMove()::equals);
    }

    protected AttackBase getRequiredMove() {
        return requiredMove;
    }

}
