package Snake;

public class Vertebra extends Physical{

    protected Vertebra backVertebra;
    protected Vertebra frontVertebra;



    protected void moveForward(){
        this.setXPosition(frontVertebra.getPreviousXPosition());
        this.setYPosition(frontVertebra.getPreviousYPosition());
        update();
        backVertebra.moveForward();
    }

    protected int ateHowMuch(){
        return frontVertebra.ateHowMuch();
    }



    public Vertebra getBackVertebra() {
        return backVertebra;
    }

    public void setBackVertebra(Vertebra backVertebra) {
        this.backVertebra = backVertebra;
    }

    public Vertebra getFrontVertebra() {
        return frontVertebra;
    }

    public void setFrontVertebra(Vertebra frontVertebra) {
        this.frontVertebra = frontVertebra;
    }

    public Vertebra(){
        super();
    }

    public Vertebra(Vertebra front, Vertebra back){
        super();
        front.setBackVertebra(this);
        back.setFrontVertebra(this);
        this.setFrontVertebra(front);
        this.setBackVertebra(back);
        this.XPosition = front.getPreviousXPosition();
        this.YPosition = front.getPreviousYPosition();
        this.setPreviousXPosition(XPosition);
        this.setPreviousYPosition(YPosition);
        update();
    }

    @Override
    public String toString(){
        Vertebra front = this.frontVertebra;
        int number = 0;
        while(front != null) {
            front = front.frontVertebra;
            number+=1;
        }
        return "Vertebra " + number;
    }
}
