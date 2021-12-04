package ca.qc.bdeb.inf203.superMeduse;

import javafx.scene.input.KeyCode;

import java.util.HashMap;

/**
 * Class used to keep track of what keys are being used on the keyboard
 */
public class Input {

    private static HashMap<KeyCode, Boolean> keys = new HashMap<>();

    public static boolean isKeyPressed(KeyCode code) {
        return keys.getOrDefault(code, false);
    }

    public static void setKeyPressed(KeyCode code, boolean isPressed) {
        keys.put(code, isPressed);
    }

}
