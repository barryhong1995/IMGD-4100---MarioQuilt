package LSystem;

import Rendering.TestArray;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public abstract class Grammar {

    Coordinate currentPos;
    TestArray t;
    Stack<Coordinate> stack;
    ArrayList<IElement> str;
    HashMap<IElement, ArrayList<IElement>> rules;

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
    	int x = currentPos.getX(); int y = currentPos.getY();
    	if(x > 0 && x < TestArray.LEVEL_WIDTH - 1 && y >= 0 && y < TestArray.LEVEL_HEIGHT){
    		if(t.getVal(x-1, y) != TestArray.GROUND && t.getVal(x+1, y) != TestArray.GROUND &&
    				t.getVal(x, y-1) != TestArray.GROUND){
        		t.setVal(currentPos.getX(), currentPos.getY(), id);
        	}
    	}
    }

	public void generate(int itr, int x, int y) {
		
		this.setCurrentPos(x, y);
		
		if(this instanceof PowerGrammar){
			this.updatePos(0, 5);
		}
		
		for(int i = 0; i < itr; i++){
            buildOneRound();
        }

        for(IElement elt : str){
            elt.performAction(this);
        }
		
	}
}
