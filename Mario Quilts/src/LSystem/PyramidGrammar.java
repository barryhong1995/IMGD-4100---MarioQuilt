package LSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import Rendering.TestArray;

public class PyramidGrammar extends Grammar{
	public PyramidGrammar(TestArray t){
		 this.t = t;
	     this.stack = new Stack<Coordinate>();
	     this.str = new ArrayList<IElement>();
	     this.rules = new HashMap<IElement, ArrayList<IElement>>();
	     this.currentPos = new Coordinate(35, 10);
	
//	     SaveContext save = new SaveContext();
//	     LoadContext load = new LoadContext();
//	     MoveToPoint moveUp = new MoveToPoint(2, -2);
//	     MoveToPoint moveDown = new MoveToPoint(2, 1);
	     DrawLeft drawLeft = new DrawLeft(TestArray.BLOCK);
	     DrawRight drawRight = new DrawRight(TestArray.BLOCK);
	     DrawUp drawUp = new DrawUp(TestArray.BLOCK);
	     DrawDown drawDown = new DrawDown(TestArray.BLOCK);
	     Variable var = new Variable();
	
	     ArrayList<IElement> a = new ArrayList<IElement>();
	     a.add(drawUp);
	     a.add(drawRight);
	     a.add(drawUp);
	     a.add(drawRight);
	     a.add(var);
	     a.add(drawRight);
	     a.add(drawDown);
	     a.add(drawRight);
	     a.add(drawDown);
	
	     rules.put(var, a);
	     str.add(var);
	}
}
