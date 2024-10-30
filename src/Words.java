import java.io.Serializable;

public class Words implements Serializable {
    private String word;
    private int points; // Cambiar a long

    public Words(String word, int points) {
        this.word = word;
        this.points = points;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public long getPoints() { // Cambiar a long
        return points;
    }

    public void setPoints(int points) { // Cambiar a long
        this.points = points;
    }

    @Override
    public String toString() {
        return "Word: " + word + ", Points: " + points;
    }
}
