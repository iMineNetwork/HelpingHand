package nl.imine.pixelmon.helpinghand.badge;

import com.pixelmonmod.pixelmon.enums.items.EnumBadges;
import nl.imine.pixelmon.helpinghand.badge.BadgeDataProvider;

import java.util.UUID;

public class BadgeManager implements BadgeDataProvider {

    public boolean hasBadge(UUID player, EnumBadges badge) {
        return true;
    }
}
