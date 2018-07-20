package nl.imine.pixelmon.helpinghand.badge;

import com.pixelmonmod.pixelmon.enums.items.EnumBadges;

import java.util.UUID;

public interface BadgeDataProvider {

    boolean hasBadge(UUID playerId, EnumBadges badge);
}
