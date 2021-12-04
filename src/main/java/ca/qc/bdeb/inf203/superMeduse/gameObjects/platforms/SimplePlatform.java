package ca.qc.bdeb.inf203.superMeduse.gameObjects.platforms;

import javafx.scene.paint.Color;

public class SimplePlatform extends GamePlatform {

    private static final Color SIMPLE_COLOR = Color.rgb(230, 134, 58);

    public SimplePlatform(double x, double y,
                          double w){
        super(x, y, w, SIMPLE_COLOR);
    }

}
