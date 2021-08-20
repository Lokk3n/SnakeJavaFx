package Snake;

import java.util.LinkedList;
import java.util.List;

public class Cell {
    public List<Physical> objects;

    public Cell(){
        objects = new LinkedList<Physical>();
    }
}
