package Snake;

public class Tail extends Vertebra{
    @Override
    protected void moveForward(){
        int food = this.ateHowMuch();
        //System.out.println(food + " food left in stomach");
        if(food > 0){
            new Vertebra(this.frontVertebra, this);
            this.setPreviousXPosition(this.getXPosition());
            this.setPreviousYPosition(this.getYPosition());
        }
        else{
            this.setXPosition(frontVertebra.getPreviousXPosition());
            this.setYPosition(frontVertebra.getPreviousYPosition());
        }
        update();
    }

    public Tail(Vertebra vertebra){
        super();
        this.frontVertebra = vertebra;
        this.XPosition = this.frontVertebra.getXPosition()+1;
        this.YPosition = this.frontVertebra.getYPosition();
        this.setPreviousXPosition(this.XPosition);
        this.setPreviousYPosition(this.YPosition);
    }

    public Tail(int x, int y, int prevx, int prevy){
        this.XPosition = x;
        this.YPosition = y;
        this.setPreviousXPosition(prevx);
        this.setPreviousYPosition(prevy);
    }

    @Override
    public String toString(){
        return "Tail";
    }
}
