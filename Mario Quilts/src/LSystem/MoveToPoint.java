package LSystem;

public class MoveToPoint implements IElement{

    int x, y;

    public MoveToPoint(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Grammar performAction(Grammar l){
        l.updatePos(x, y);
        return l;
    }
}
