package LSystem;

import java.util.Stack;

public class SaveContext implements IElement{

    public void performAction(LSystem l){
        Stack<Coordinate> stack = l.getStack();
        Coordinate currentPos = l.getCurrentPos();
        stack.push(currentPos);
        l.setStack(stack);
    }



}
