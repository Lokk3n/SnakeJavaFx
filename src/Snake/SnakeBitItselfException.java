package Snake;

public class SnakeBitItselfException extends Exception{

    public SnakeBitItselfException(Physical attacker, Physical victim){
        super("Element " + attacker + " bit: " + victim);
    }
}
