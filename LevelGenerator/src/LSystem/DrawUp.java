package LSystem;

public class DrawUp implements IElement {

    int type;

    public DrawUp(int type){
        this.type = type;
    }

    public Grammar performAction(Grammar l){
        l.draw(type);
        l.updatePos(0, -1);
        return l;
    }
}
