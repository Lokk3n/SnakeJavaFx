package MainPackage;

import Interfaces.Observable;
import Interfaces.Observer;
import Snake.Food;
import Snake.Head;
import Snake.Territory;
import Threading.RunSnake;

import java.util.LinkedList;
import java.util.List;

public class Controller implements Observable<Controller>, Observer<RunSnake> {
    public Territory getTerritory() {
        return territory;
    }

    Territory territory;
    RunSnake life;
    Thread t;

    public boolean isRunning() {
        return isRunning;
    }

    boolean isRunning;
    static Controller instance;

    public static Controller getInstance(){
        return instance;
    }

    public Controller(){
        instance = this;
        observerList = new LinkedList<>();
    }

    public void startNewGame(){
        //odczytaj config, stwórz planszę
        territory = new Territory(Configuration.mapHeight, Configuration.mapWidth);
        territory.setSnakeHead(new Head(territory.getHeight()/2, territory.getWidth()/2));
        territory.spawnFood();
        territory.initialize();
        territory.getSnakeHead().setNextTarget(true, -1, true);

        PaneManager.getInstance().setTerritory(territory);
        PaneManager.getInstance().registerNewPhysicals();

        life = new RunSnake(false, territory, PaneManager.getInstance());
        life.addObserver(this);
        t = new Thread(life);
        t.start();
    }

    public void startLoadedGame(Territory territory, Head snake, Food food){
        this.territory = territory;
        this.territory.setSnakeHead(snake);
        this.territory.spawnFood(food);
        this.territory.initialize();
        //this.territory.getSnakeHead().setNextTarget(direction, directionPoint);

        PaneManager.getInstance().clearPane();
        PaneManager.getInstance().setTerritory(territory);
        PaneManager.getInstance().registerNewPhysicals();

        life = new RunSnake(false, territory, PaneManager.getInstance());
        life.addObserver(this);
        t = new Thread(life);
        t.start();
    }


    public void goUp(){
            territory.getSnakeHead().setNextTarget(false, 1, false);
    }
    public void goLeft(){
            territory.getSnakeHead().setNextTarget(true, -1, false);
    }
    public void goDown(){
            territory.getSnakeHead().setNextTarget(false, -1, false);
    }
    public void goRight(){
            territory.getSnakeHead().setNextTarget(true, 1, false);
    }


    List<Observer<Controller>> observerList;
    @Override
    public void addObserver(Observer<Controller> channel) {
        observerList.add(channel);
    }

    @Override
    public void removeObserver(Observer<Controller> channel) {
        observerList.remove(channel);
    }

    @Override
    public void update() {
        for(Observer<Controller> x : observerList){
            x.update(this);
        }
    }

    @Override
    public void update(RunSnake data) {
        this.isRunning = data.isRunning();
        update();
    }
}
