package SnakeGraphic;

import Interfaces.Observer;
import Snake.Physical;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import MainPackage.Configuration;
import MainPackage.PaneManager;


public class RenderedVertebra extends Rectangle implements Observer<Physical> {

    Timeline timeline;

    public RenderedVertebra(int x, int y, boolean isHead) {
        super();
        timeline = new Timeline();
        this.widthProperty().bind(PaneManager.getInstance().getDisplayedTerritory().widthProperty().divide(PaneManager.getInstance().getTerritory().getWidth()));
        this.heightProperty().bind(PaneManager.getInstance().getDisplayedTerritory().heightProperty().divide(PaneManager.getInstance().getTerritory().getHeight()));
        this.setFill(isHead?Color.DARKGRAY:Color.DARKGREEN);
        this.setStroke(Color.BLACK);
        this.setArcHeight(15);
        this.setArcWidth(15);
        this.xProperty().bind(this.widthProperty().multiply(x-1));
        this.yProperty().bind(PaneManager.getInstance().getDisplayedTerritory().heightProperty().subtract(this.heightProperty().multiply(y)));

        //System.out.println("Szerokość tabeli: " + PaneManager.getInstance().getTerritory().getWidth());
        //System.out.println("Szerokość kwadratu: " + this.widthProperty().getValue());
    }

    @Override
    public void update(Physical data) {

        if(timeline.getStatus() == Animation.Status.STOPPED) {
            try {
                this.xProperty().unbind();
                this.yProperty().unbind();
                //System.out.println(this.widthProperty().getValue() * (data.getXPosition() - 1));
                KeyValue xValue = new KeyValue(this.xProperty(), this.widthProperty().getValue() * (data.getXPosition() - 1));
                KeyValue yValue = new KeyValue(this.yProperty(), PaneManager.getInstance().getDisplayedTerritory().heightProperty().getValue() - this.heightProperty().getValue() * (data.getYPosition()));

                KeyFrame newFrame = new KeyFrame(Duration.millis(200 / Configuration.speedMultiplier), xValue, yValue);

                timeline = new Timeline();
                timeline.setCycleCount(1);
                timeline.setAutoReverse(false);
                timeline.getKeyFrames().addAll(newFrame);
                timeline.setOnFinished(actionEvent -> {

                    this.xProperty().bind(this.widthProperty().multiply(data.getXPosition() - 1));
                    this.yProperty().bind(PaneManager.getInstance().getDisplayedTerritory().heightProperty().subtract(this.heightProperty().multiply(data.getYPosition())));
                });
                timeline.play();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        else{
            timeline.stop();
            this.xProperty().bind(this.widthProperty().multiply(data.getXPosition() - 1));
            this.yProperty().bind(PaneManager.getInstance().getDisplayedTerritory().heightProperty().subtract(this.heightProperty().multiply(data.getYPosition())));
        }
    }
}
