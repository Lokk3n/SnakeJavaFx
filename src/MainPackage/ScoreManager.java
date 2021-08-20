package MainPackage;

import Interfaces.Observer;
import Snake.Head;
import Snake.Physical;
import Snake.Tail;
import Snake.Vertebra;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class ScoreManager implements Observer<Physical> {

    public int getScore() {
        return score.get();
    }

    public IntegerProperty scoreProperty() {
        return score;
    }

    public void setScore(int score) {
        this.score.set(score);
    }


    private final IntegerProperty score;

    static ScoreManager instance;

    public ScoreManager(){
        score = new SimpleIntegerProperty();
        score.set(0);
        instance = this;
    }

    public static ScoreManager getInstance() {
        return instance;
    }

    @Override
    public void update(Physical data) {
        Vertebra front = ((Tail)data).getFrontVertebra();
        int number = 0;
        while(!(front instanceof Head)) {
            front = front.getFrontVertebra();
            number+=1;
        }
        setScore(number);
    }
}
