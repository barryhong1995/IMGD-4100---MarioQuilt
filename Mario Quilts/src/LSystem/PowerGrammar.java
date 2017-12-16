package LSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import Rendering.TestArray;

public class PowerGrammar extends Grammar{
	
	public PowerGrammar(TestArray t){
		 this.t = t;
	     this.stack = new Stack<Coordinate>();
	     this.str = new ArrayList<IElement>();
	     this.rules = new HashMap<IElement, ArrayList<IElement>>();
	     this.currentPos = new Coordinate(30, 10);
	
	     SaveContext save = new SaveContext();
	     LoadContext load = new LoadContext();
	     MoveToPoint moveLeft = new MoveToPoint(-4, -2);
	     MoveToPoint moveRight = new MoveToPoint(4, -2);
	     DrawLeft drawLeft = new DrawLeft(TestArray.BLOCK);
	     DrawRight drawRight = new DrawRight(TestArray.BLOCK);
	     DrawUp drawUp = new DrawUp(TestArray.BLOCK);
	     DrawDown drawDown = new DrawDown(TestArray.BLOCK);
	     Variable var = new Variable();
	
	     ArrayList<IElement> a = new ArrayList<IElement>();
	     
	     a.add(save);
	     a.add(moveLeft);
	     a.add(drawRight);
	     a.add(var);
	     a.add(drawRight);
	     a.add(drawRight);
	     a.add(load);
	     a.add(save);
	     a.add(moveRight);
	     a.add(drawLeft);
	     a.add(var);
	     a.add(drawLeft);
	     a.add(drawLeft);
	     a.add(load);
	
	     rules.put(var, a);
	     
	     str.add(drawRight);
	     str.add(drawRight);
	     str.add(var);
	     str.add(drawRight);
	     str.add(drawRight);
	     str.add(drawRight);
	}

}
