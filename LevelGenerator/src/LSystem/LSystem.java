package LSystem;

import Rendering.TestArray;

import java.util.Stack;

public class LSystem {

    Coordinate currentPos;
    int id;
    TestArray t;
    Stack<Coordinate> stack;

    public Coordinate getCurrentPos(){
        return currentPos;
    }

    public void setCurrentPos(int x, int y){
        currentPos = new Coordinate(x, y);
    }

    public void setCurrentPos(Coordinate c){
        currentPos = c;
    }

    public Stack getStack(){
        return stack;
    }

    public void setStack(Stack<Coordinate> stack){
        stack = stack;
    }
}
