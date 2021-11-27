package ca.qc.bdeb.inf203.superMeduse.gameObjects;

import javafx.scene.paint.Color;

public class Bubble extends GameObject{

    public Bubble(double x, double y,
                  double vy, double ay,
                  double diameter, double maxX){
        super(x, y, 0.00, vy, 0.00, ay, diameter, diameter, maxX,
                Color.rgb(0, 0, 255, 0.4));

    }
}
