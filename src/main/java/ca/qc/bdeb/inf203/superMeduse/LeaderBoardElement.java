package ca.qc.bdeb.inf203.superMeduse;

public class LeaderBoardElement {

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
