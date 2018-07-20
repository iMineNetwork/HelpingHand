package nl.imine.pixelmon.helpinghand.effect;

import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.items.EnumBadges;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import nl.imine.pixelmon.helpinghand.badge.BadgeDataProvider;
import nl.imine.pixelmon.helpinghand.badge.BadgeRequired;

import java.util.Objects;

public class EffectFlash extends EffectMove implements BadgeRequired {

    private static final String MOVE_NAME_FLASH = "Flash";
    private static final String POTION_EFFECT_NAME_BLINDNESS = "minecraft:blindness";
    private static final String POTION_EFFECT_NAME_NIGHT_VISION = "minecraft:night_vision";

    private final BadgeDataProvider badgeDataProvider;

    public EffectFlash(BadgeDataProvider badgeDataProvider) {
        super(AttackBase.getAttackBase(MOVE_NAME_FLASH).orElseThrow(() -> new IllegalStateException("The AttackBase for move '" + MOVE_NAME_FLASH + "' could not be found")));
        this.badgeDataProvider = badgeDataProvider;
    }

    public void attemptFlash(EntityPlayerMP entityPlayerMP, EntityPixelmon pixelmon) {
        if (!isPlayerBlinded(entityPlayerMP) || !hasPlayerObtainedBadge(entityPlayerMP) || !pixelmonHasRequiredMove(pixelmon))
            return;
        flashPlayer(entityPlayerMP);
        removeBlindness(entityPlayerMP);
    }


    private void flashPlayer(EntityPlayerMP entityPlayerMP) {
        Potion nightVisionPotionType = Objects.requireNonNull(Potion.getPotionFromResourceLocation(POTION_EFFECT_NAME_NIGHT_VISION));
        entityPlayerMP.addPotionEffect(new PotionEffect(nightVisionPotionType, Integer.MAX_VALUE, 0, true, false));
    }

    private void removeBlindness(EntityPlayerMP entityPlayerMP) {
        Potion blindnessPotionType = Objects.requireNonNull(Potion.getPotionFromResourceLocation(POTION_EFFECT_NAME_BLINDNESS));
        entityPlayerMP.removePotionEffect(blindnessPotionType);
    }

    private boolean isPlayerBlinded(EntityPlayerMP entityPlayerMP) {
        Potion blindnessPotionType = Objects.requireNonNull(Potion.getPotionFromResourceLocation(POTION_EFFECT_NAME_BLINDNESS));
        return entityPlayerMP.getActivePotionEffect(blindnessPotionType) != null;
    }

    private boolean hasPlayerObtainedBadge(EntityLivingBase user) {
        return badgeDataProvider.hasBadge(user.getUniqueID(), getRequiredBadge());
    }

    @Override
    public EnumBadges getRequiredBadge() {
        return EnumBadges.BoulderBadge;
    }
}
