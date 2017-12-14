package LSystem;

public class DrawRight implements IElement{

    int type;

    public DrawRight(int type){
        this.type = type;
    }

    public Grammar performAction(Grammar l){
        l.draw(type);
        l.updatePos(1, 0);
        return l;
    }
}
