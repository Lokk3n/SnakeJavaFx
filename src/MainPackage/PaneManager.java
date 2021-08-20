package MainPackage;

import Interfaces.Observer;
import Snake.*;
import SnakeGraphic.RenderedFood;
import SnakeGraphic.RenderedVertebra;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class PaneManager {
    Territory territory;
    private Pane displayedTerritory;
    private TextField scoreCounter;
    private static PaneManager instance;

    public PaneManager(Territory territory, Pane displayedTerritory) {
        this.territory = territory;
        this.displayedTerritory = displayedTerritory;
        instance = this;
    }

    public PaneManager() {
        instance = this;
    }


    public Territory getTerritory() {
        return territory;
    }

    public static PaneManager getInstance(){
        return instance;
    }

    public void setTerritory(Territory territory) {
        this.territory = territory;
    }

    public Pane getDisplayedTerritory() {
        return displayedTerritory;
    }

    public void setDisplayedTerritory(Pane displayedTerritory) {
        this.displayedTerritory = displayedTerritory;
    }


    public void registerNewPhysicals(){
        for(int x = 0; x < territory.getWidth(); x++){
            for(int y = 0; y < territory.getHeight(); y++){
                for(Physical p : territory.getOccupancyMap()[x][y].objects){
                    boolean foundObserver = false;
                    for(Observer<Physical> op : p.getListOfObservers()){
                        if (((p instanceof Vertebra) && (op instanceof RenderedVertebra)) ||
                                ((p instanceof Food) && (op instanceof RenderedFood))) {
                            foundObserver = true;
                            break;
                        }
                    }
                    if(!foundObserver){
                        if(p instanceof Food){
                            RenderedFood newRendered = new RenderedFood(p.getXPosition(), p.getYPosition());
                            p.addObserver(newRendered);
                            displayedTerritory.getChildren().add(newRendered);
                            System.out.println("rendering new food");
                        }
                        else {
                            RenderedVertebra newRendered = new RenderedVertebra(p.getXPosition(), p.getYPosition(), p instanceof Head);
                            p.addObserver(newRendered);
                            displayedTerritory.getChildren().add(newRendered);
                            System.out.println("rendering new vertebra");
                        }
                    }
                }
            }
        }

        displayedTerritory.getChildren().removeIf(x -> x instanceof RenderedFood && ((RenderedFood) x).isGotEaten());
    }

    void clearPane(){
        displayedTerritory.getChildren().removeAll(displayedTerritory.getChildren());
    }
}
