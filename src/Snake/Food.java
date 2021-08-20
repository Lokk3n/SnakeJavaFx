package Snake;

public class Food extends Physical {
    public int getNutritionValue() {
        return nutritionValue;
    }

    int nutritionValue;

    public void setNutritionValue(int nutritionValue) {
        this.nutritionValue = nutritionValue;
    }


    public boolean isEaten() {
        return eaten;
    }

    public void setEaten(boolean eaten) {
        this.eaten = eaten;
        this.update();
    }

    boolean eaten;

    public Food(int x, int y){
        super();
        this.nutritionValue = 1;
        this.eaten = false;
        this.XPosition = x;
        this.YPosition = y;
        this.setPreviousXPosition(x);
        this.setPreviousYPosition(y);
        System.out.println("Food x: " + XPosition + " y: " + YPosition);
    }

    @Override
    public String toString(){
        return "Food";
    }
}
