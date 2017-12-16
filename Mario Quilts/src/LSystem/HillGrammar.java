package LSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import Rendering.TestArray;

public class HillGrammar extends Grammar{
	
	public HillGrammar(TestArray t){
		 this.t = t;
	     this.stack = new Stack<Coordinate>();
	     this.str = new ArrayList<IElement>();
	     this.rules = new HashMap<IElement, ArrayList<IElement>>();
	     this.currentPos = new Coordinate(5, 10);
	
	     SaveContext save = new SaveContext();
	     LoadContext load = new LoadContext();
	     MoveToPoint moveUp = new MoveToPoint(2, -2);
	     MoveToPoint moveDown = new MoveToPoint(2, 1);
	     DrawLeft drawLeft = new DrawLeft(TestArray.PLATFORM);
	     DrawRight drawRight = new DrawRight(TestArray.PLATFORM);
	     DrawUp drawUp = new DrawUp(TestArray.PLATFORM);
	     DrawDown drawDown = new DrawDown(TestArray.PLATFORM);
	     Variable var = new Variable();
	
	     ArrayList<IElement> a = new ArrayList<IElement>();
	     a.add(drawRight);
	     a.add(drawRight);
	     a.add(save);
	     a.add(moveUp);
	     a.add(drawRight);
	     a.add(var);
	     a.add(drawRight);
	     a.add(load);
	     a.add(drawRight);
	     a.add(save);
	     a.add(moveDown);
	     a.add(drawRight);
	     a.add(var);
	     a.add(drawRight);
	     a.add(load);
	     a.add(drawRight);

	     rules.put(var, a);
	     str.add(var);
	}
	

}
