import java.io.Serializable;

public class Words implements Serializable {
    private final String word;
    private int points;

    public Words(String word, int points) {
        this.word = word;
        this.points = points;
    }

    public String getWord() {
        return word;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }


    @Override
    public String toString() {
        return "Word: " + word + ", Points: " + points;
    }
}
