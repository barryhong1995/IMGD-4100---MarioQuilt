package LSystem;

public class DrawLeft implements IElement {
    int type;

    public DrawLeft(int type){
        this.type = type;
    }

    public Grammar performAction(Grammar l){
    	l.draw(type);
        l.updatePos(-1, 0);
        return l;
    }
}
