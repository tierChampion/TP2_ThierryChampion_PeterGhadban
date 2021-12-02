package ca.qc.bdeb.inf203.superMeduse;

import java.io.Serializable;

public class LeaderBoardElement implements Serializable {

    private String name;
    private int score;

    public LeaderBoardElement(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public int getScore() {
        return this.score;
    }

    @Override
    public String toString() {
        return name + " -- " + score + "px";
    }
}
