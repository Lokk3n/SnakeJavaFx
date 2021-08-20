package Threading;

import Interfaces.Observable;
import Interfaces.Observer;
import Snake.Territory;
import javafx.application.Platform;
import MainPackage.CommunicationManager;
import MainPackage.Configuration;
import MainPackage.PaneManager;

import java.util.LinkedList;
import java.util.List;

public class RunSnake implements Runnable, Observable<RunSnake> {

    Territory territory;
    PaneManager paneManager;

    public boolean isStart() {
        return start;
    }

    boolean start;

    public void abort() {
        this.abort = true;
    }

    public boolean isRunning() {
        return running;
    }

    boolean running;

    boolean abort;

    public RunSnake(boolean start, Territory territory, PaneManager paneManager){
        this.territory = territory;
        this.paneManager = paneManager;
        this.start = start;
        this.observerList = new LinkedList<>();
    }

    public void start(boolean start){
        this.start = start;
    }

    @Override
    public void run() {
        this.abort = false;
        this.running = true;
        update();
        try {
            while (!this.abort && !territory.isSnakeDead()) {
                //System.out.println("thread cycle");
                if (this.start) {
                    territory.getSnakeHead().moveHeadForward();
                    Platform.runLater(() -> paneManager.registerNewPhysicals());
                    if (!territory.isSnakeDead()) territory.hunt();
                    else {
                        Platform.runLater(() -> CommunicationManager.displayGameOver());
                    }
                    //territory.printTerritory();
                }
                Thread.sleep(500 / Configuration.speedMultiplier);
            }
        }
        catch(Exception e){
            CommunicationManager.displayError(e.getMessage());
        }
        finally {
            this.running = false;
            update();
        }
    }

    List<Observer<RunSnake>> observerList;
    @Override
    public void addObserver(Observer<RunSnake> channel) {
        observerList.add(channel);
    }

    @Override
    public void removeObserver(Observer<RunSnake> channel) {
        observerList.remove(channel);
    }

    @Override
    public void update() {
        for(Observer<RunSnake> x : observerList){
            x.update(this);
        }
    }
}
