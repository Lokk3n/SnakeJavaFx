package Snake;

import Interfaces.Observable;
import Interfaces.Observer;

import java.util.LinkedList;
import java.util.List;

public class Physical implements Observable<Physical> {


    protected int XPosition;
    protected int YPosition;

    public void setPreviousXPosition(int previousXPosition) {
        this.previousXPosition = previousXPosition;
    }

    public void setPreviousYPosition(int previousYPosition) {
        this.previousYPosition = previousYPosition;
    }

    private int previousXPosition;
    private int previousYPosition;

    private List<Observer<Physical>> listOfObservers;

    public Physical(){
        listOfObservers = new LinkedList<>();
        listOfObservers.add(Territory.getInstance());
        //System.out.println("Dodano obiekt: " + this.getClass().getName());
    }

    public int getXPosition() {
        return XPosition;
    }

    public void setXPosition(int xPosition) {
        this.previousXPosition = this.XPosition;
        this.XPosition = xPosition;
    }

    public int getYPosition() {
        return YPosition;
    }

    public void setYPosition(int YPosition) {
        this.previousYPosition = this.YPosition;
        this.YPosition = YPosition;
    }

    public int getPreviousXPosition() {
        return previousXPosition;
    }

    public int getPreviousYPosition() {
        return previousYPosition;
    }

    public List<Observer<Physical>> getListOfObservers() {
        return listOfObservers;
    }

    @Override
    public void addObserver(Observer<Physical> channel) {
        listOfObservers.add(channel);
        //System.out.println(this.getClass().getName() + " has " + listOfObservers.size() + " observers");
    }

    @Override
    public void removeObserver(Observer<Physical> channel) {
        listOfObservers.remove(channel);
    }

    @Override
    public void update() {
        for(Observer<Physical> x: listOfObservers){
            x.update(this);
        }
    }

    @Override
    public String toString(){
        return "Physical";
    }
}
