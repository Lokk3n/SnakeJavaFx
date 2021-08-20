package Snake;

public class Head extends Vertebra {

    private int nextXMovement;
    private int nextYMovement;

    private int amountEaten;

    //private boolean direction;
    private boolean proposedDirection;


    private boolean previousDirection;
    private int proposedAmount;
    private int previousAmount;

    public void moveHeadForward(){
        moveForward();
    }

    @Override
    protected void moveForward(){
            this.setXPosition(this.getXPosition() + nextXMovement);
            this.setYPosition(this.getYPosition() + nextYMovement);
            //System.out.println("Head x: " + XPosition + " y: " + YPosition);
            update();
            this.backVertebra.moveForward();
            //System.out.println("Changed direction from: " + (previousDirection?"Horizontal":"Vertical") + " to " + (proposedDirection?"Horizontal":"Vertical"));
            previousDirection = proposedDirection;
            previousAmount = proposedAmount;
    }

    @Override
    protected int ateHowMuch(){
        int result = this.amountEaten;
        if(result > 0 ) this.amountEaten--;
        return result;
    }

    public void chomp(Food snack){
        amountEaten+=snack.getNutritionValue();
        snack.setEaten(true);
        //System.out.println("Om nom nomed: " + amountEaten);
    }

    public void setNextTarget(boolean direction, int amount, boolean force){
        if(direction != previousDirection || force) {
            proposedDirection = direction;
            proposedAmount = amount;
            System.out.println("Change in direction requested: " + (direction?"Horizontal":"Vertical"));
            if (direction) { //horizontal
                nextXMovement = amount;
                nextYMovement = 0;
            } else { //vertical
                nextYMovement = amount;
                nextXMovement = 0;
            }
        }
    }

    public Head(int height, int width){
        super();
        this.setXPosition(width);
        this.setYPosition(height);
        this.setPreviousXPosition(width);
        this.setPreviousYPosition(height);
        this.proposedDirection = true; //horizontal
        this.backVertebra = new Tail(this);
    }

    public Head(int height, int width, int previousHeight, int previousWidth, boolean proposedDirection, int proposedAmount){ //do rozgrywek wczytywanych
        super();
        this.setXPosition(width);
        this.setYPosition(height);
        this.setPreviousXPosition(previousWidth);
        this.setPreviousYPosition(previousHeight);
        this.proposedDirection = proposedDirection;
        this.proposedAmount = proposedAmount;
        this.previousAmount = proposedAmount;
    }

    public boolean getPreviousDirection() {
        return previousDirection;
    }

    public int getPreviousAmount() {
        return previousAmount;
    }

    @Override
    public String toString(){
        return "Head";
    }
}
