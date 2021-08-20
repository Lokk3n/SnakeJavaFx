package SnakeGraphic;

import Interfaces.Observer;
import Snake.Food;
import Snake.Physical;
import javafx.animation.Timeline;
import javafx.scene.shape.Ellipse;
import MainPackage.PaneManager;

public class RenderedFood extends Ellipse implements Observer<Physical> {

    Timeline timeline;

    public boolean isGotEaten() {
        return gotEaten;
    }

    boolean gotEaten;


    public RenderedFood(int x, int y) {
        super();
        timeline = new Timeline();
        this.radiusXProperty().bind(PaneManager.getInstance().getDisplayedTerritory().widthProperty().divide(PaneManager.getInstance().getTerritory().getWidth()).divide(2));
        this.radiusYProperty().bind(PaneManager.getInstance().getDisplayedTerritory().heightProperty().divide(PaneManager.getInstance().getTerritory().getHeight()).divide(2));

        this.centerXProperty().bind(this.radiusXProperty().multiply(x-1).multiply(2).add(this.radiusXProperty()));
        this.centerYProperty().bind(PaneManager.getInstance().getDisplayedTerritory().heightProperty().subtract(this.radiusYProperty().multiply(y-1).multiply(2).add(this.radiusYProperty())));

        System.out.println("Szerokość tabeli: " + PaneManager.getInstance().getTerritory().getWidth());
        System.out.println("Promien: " + this.radiusXProperty().getValue());
        System.out.println("RenderedFood x: " + this.centerXProperty().getValue());
        System.out.println("RenderedFood y: " + this.centerYProperty().getValue());
    }

    @Override
    public void update(Physical data) {
        if(((Food)data).isEaten()){
            this.gotEaten = true;
        }
    }
}
