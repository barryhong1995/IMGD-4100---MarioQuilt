package LSystem;

public class DrawDown implements IElement {
    int type;

    public DrawDown(int type){
        this.type = type;
    }

    public Grammar performAction(Grammar l){
        l.draw(type);
        l.updatePos(0, 1);
        return l;
    }
}
