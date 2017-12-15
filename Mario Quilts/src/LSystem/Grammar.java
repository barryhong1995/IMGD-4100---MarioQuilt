package LSystem;

import Rendering.TestArray;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class Grammar {

    Coordinate currentPos;
    TestArray t;
    Stack<Coordinate> stack;
    ArrayList<IElement> str;
    HashMap<IElement, ArrayList<IElement>> rules;

    public Grammar(TestArray t){
        this.t = t;
        this.stack = new Stack<Coordinate>();
        this.str = new ArrayList<IElement>();
        this.rules = new HashMap<IElement, ArrayList<IElement>>();
        this.currentPos = new Coordinate(5, 10);

        SaveContext save = new SaveContext();
        LoadContext load = new LoadContext();
        MoveToPoint moveUp = new MoveToPoint(5, -2);
        MoveToPoint moveDown = new MoveToPoint(5, 5);
        DrawLeft drawLeft = new DrawLeft(TestArray.PLATFORM);
        DrawRight drawRight = new DrawRight(TestArray.PLATFORM);
        DrawUp drawUp = new DrawUp(TestArray.PLATFORM);
        DrawDown drawDown = new DrawDown(TestArray.PLATFORM);
        Variable var = new Variable();

        ArrayList<IElement> a = new ArrayList<IElement>();
        a.add(drawLeft);
        a.add(drawLeft);
        a.add(save);
        a.add(moveUp);
        a.add(drawLeft);
        a.add(drawLeft);
        a.add(var);
        a.add(load);
        a.add(drawLeft);
        a.add(drawLeft);

        rules.put(var, a);
        str.add(var);
    }


    public void runLsystem(int itr){
        for(int i = 0; i < itr; i++){
            buildOneRound();
        }

        for(IElement elt : str){
            elt.performAction(this);
        }
    }

    public void buildOneRound(){
        ArrayList<IElement> n = new ArrayList<IElement>();

        for(IElement letter : str){
            //System.out.println("HI");
            ArrayList<IElement> phrase = rules.get(letter);

            if(phrase != null){
                for(IElement elt : phrase){
                    n.add(elt);
                }
            } else { n.add(letter); }
        }

        str = n;
    }

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
        this.stack = stack;
    }

    public void updatePos(int x, int y){
        Coordinate c = getCurrentPos();
        c = new Coordinate(c.getX() + x, c.getY() + y);
        setCurrentPos(c);
    }

    public void draw(int id){
        t.setVal(currentPos.getX(), currentPos.getY(), id);
    }
}
