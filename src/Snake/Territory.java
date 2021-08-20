package Snake;

import Interfaces.Observable;
import Interfaces.Observer;

import java.util.List;

public class Territory implements Observer<Physical> {


    private Cell[][] occupancyMap;
    private int height;
    private int width;
    private Head snakeHead;
    private Tail snakeTail;

    private Food food;

    boolean death;

    private static Territory instance;

    public Territory(int height, int width){
        this.occupancyMap = new Cell[width][height];
        for(int x=0; x<width; x++)
            for(int y=0; y<height; y++)
                this.occupancyMap[x][y] = new Cell();
        this.height = height;
        this.width = width;
        death = false;
        instance = this;
    }


    public static Territory getInstance(){ //singleton
        return instance;
    }

    void moveObject(Physical data){
        try {
            if(!death) {
                List<Physical> currentPhysicalList = occupancyMap[data.getXPosition() - 1][data.getYPosition() - 1].objects;
                if(data instanceof Vertebra) {
                    for (Physical x : currentPhysicalList) {
                        if (!(x == data) && x instanceof Vertebra) throw new SnakeBitItselfException(data, x);
                    }
                }
                occupancyMap[data.getPreviousXPosition() - 1][data.getPreviousYPosition() - 1].objects.remove(data);

                if (!(data instanceof Food && ((Food) data).isEaten())) {
                    currentPhysicalList.add(data);
                }
            }
        }
        catch(IndexOutOfBoundsException | SnakeBitItselfException ex){
            // snake stepped out of territory bounds
            System.out.println(ex.getMessage());
            death = true;
        }


    }

    public void hunt(){ //checks whether snake found food or bit itself
        int x = this.getSnakeHead().getXPosition();
        int y = this.getSnakeHead().getYPosition();
        Cell huntingGround = occupancyMap[x-1][y-1];
        boolean foodFound;
        boolean ouch;
        for(Observable<Physical> devouredItem : huntingGround.objects){
            if(devouredItem instanceof Food){
                this.getSnakeHead().chomp((Food)devouredItem);
            }
        }
    }


    public void setSnakeHead(Head snakeHead) {
        this.snakeHead = snakeHead;
        Vertebra vertebra = snakeHead.getBackVertebra();
        while(!(vertebra instanceof Tail)){
            vertebra = vertebra.getBackVertebra();
        }
        this.snakeTail = (Tail)vertebra;
    }

    public Head getSnakeHead() {
        return snakeHead;
    }

    public Food getFood() {
        return food;
    }

    public Tail getSnakeTail(){
        return this.snakeTail;
    }

    public Cell[][] getOccupancyMap() {
        return occupancyMap;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public boolean isSnakeDead() {
        return death;
    }

    @Override
    public void update(Physical data) {
        this.moveObject(data);
        if(data instanceof Food && ((Food)data).isEaten()){
            spawnFood();
        }
    }

    public void initialize(){
        Vertebra vertebra = this.snakeHead;
        while(vertebra != null){
            vertebra.update();
            vertebra = vertebra.backVertebra;
        }
    }

    public void spawnFood(){
        boolean correctPosition = false;
        int x = 0;
        int y = 0;
        while(!correctPosition) {
            x = (int) (Math.random() * this.width) + 1;
            //y = (int) (Math.random() * this.height) + 1;
            y = this.height;
            if(occupancyMap[x-1][y-1].objects.size() == 0) correctPosition = true;
        }
        //int x = 5;
        //int y = 2;
        this.food = new Food(x, y);
        this.occupancyMap[x-1][y-1].objects.add(this.food);
    }

    public void spawnFood(Food food){
        this.food = food;
        this.occupancyMap[this.food.getXPosition()-1][this.food.getYPosition()-1].objects.add(this.food);
    }


    public void printTerritory(){
        System.out.println("---------------------------------------");
        for(int y = height-1; y >= 0; y--){
            for(int x = 0; x < width; x++){
                if(occupancyMap[x][y].objects.size() > 0) {
                    System.out.print("X ");
                }
                else{
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
    }
}
