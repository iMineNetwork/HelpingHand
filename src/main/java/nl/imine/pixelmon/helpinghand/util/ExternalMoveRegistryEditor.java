package nl.imine.pixelmon.helpinghand.util;

import com.pixelmonmod.pixelmon.entities.pixelmon.externalMoves.ExternalMoveBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.externalMoves.ExternalMoveRegistry;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ExternalMoveRegistryEditor {

    private static final String EXTERNAL_MOVE_LIST_FIELD_NAME = "externalMoveList";

    public void clearExternalMoves() {
        getExternalMoves().clear();
    }

    public void addCustomExternalMove(ExternalMoveBase move) {
        getExternalMoves().add(move);
    }

    private ArrayList<ExternalMoveBase> getExternalMoves() {
        try {
            Field listField = ExternalMoveRegistry.class.getDeclaredField(EXTERNAL_MOVE_LIST_FIELD_NAME);
            listField.setAccessible(true);
            return (ArrayList<ExternalMoveBase>) listField.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
