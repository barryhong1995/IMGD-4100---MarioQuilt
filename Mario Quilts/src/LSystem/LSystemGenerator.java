package LSystem;

import java.util.ArrayList;
import java.util.Random;

import Rendering.TestArray;

public class LSystemGenerator {
	
	TestArray t;
	ArrayList<Grammar> grammars;
	
	public LSystemGenerator(TestArray t){
		
		this.t = t;
		grammars = new ArrayList<Grammar>();
		grammars.add(new HillGrammar(t));
		grammars.add(new PyramidGrammar(t));
		grammars.add(new PowerGrammar(t));
	}
	
	public void generate(){
		
		Random rand = new Random();
		
		for(int i = 0; i < TestArray.LEVEL_WIDTH; i += 20){
			Grammar select = grammars.get(rand.nextInt(grammars.size()));
			
			int j = 0;
			while(t.getVal(i, j) != TestArray.GROUND && j < TestArray.LEVEL_HEIGHT){
				j++;
			}
			
			if(j != TestArray.LEVEL_HEIGHT){
				j--;
				select.generate(rand.nextInt(1) + 1, i, j);			
			}
			
		}
	}

}
