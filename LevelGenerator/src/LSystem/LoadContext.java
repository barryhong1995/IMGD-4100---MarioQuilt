package LSystem;

import java.util.Stack;

public class LoadContext implements IElement {

    public Grammar performAction(Grammar l){
        Stack<Coordinate> stack = l.getStack();
        Coordinate c = stack.pop();
        l.setCurrentPos(c);
        return l;
    }
}
