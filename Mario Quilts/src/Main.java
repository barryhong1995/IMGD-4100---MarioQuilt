import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import LSystem.Coordinate;
import Rendering.ImageOverlay;
import Rendering.RenderArray;
import Rendering.TestArray;
import LSystem.Grammar;
import generator.QuiltGenerator;
import generator.Sprite;
import AI.AIData;

public class Main {

    public static void main(String args[]){

        TestArray t = new TestArray();
        RenderArray r = new RenderArray();
        ImageOverlay overlay = new ImageOverlay(null);
        Grammar l = new Grammar(t);


        t.buildGround();
        l.runLsystem(3);
        r.buildRenderArray(t);

        //t.printArray();
        //System.out.println();
        //r.printArray();

        int[][] rArray = r.getArray();

        
        overlay.setSize(TestArray.LEVEL_WIDTH, TestArray.LEVEL_HEIGHT);
        for(int i  = 0; i < TestArray.LEVEL_HEIGHT; i++){
            for(int j = 0; j < TestArray.LEVEL_WIDTH; j++){
                overlay.drawBlock(r.getVal(j, i), new Coordinate(j, i));
            }
        }
        overlay.writeImage("level_output.jpg", "JPG");
        
        
        QuiltGenerator qG = new QuiltGenerator(rArray);
        qG.generate(0,"quilt_output.png");
		
        QuiltGenerator STANDARD_GENERATION = new QuiltGenerator(Sprite.TEST_LEVEL);
		STANDARD_GENERATION.generate(0,"standard_quilt_output.png");
		
		
        
        AIData ai = new AIData();
        
        try {
			ai.convertSceneFromGeneration(rArray);
		}
		catch (IOException e1) {
			e1.printStackTrace();
		}
        
        char[][] rawLevelScene = new char[rArray[0].length][rArray.length];
        
        try{
        	BufferedReader fileInput = new BufferedReader(new FileReader("text_output.txt"));
        	String s;
        	int maxWidth = 0;
        	int height = 0;
        	while ((s = fileInput.readLine()) != null){
        		int width = 0;
        		for (char c : s.toCharArray()){
        			if (c!= ' '){
        				rawLevelScene[width][height] = c;
        				width++;
        				System.out.print(c + " ");
        				System.out.flush();
        			}
        		}
        		if (width > maxWidth) maxWidth = width;
        		height++;
        	}
        	fileInput.close();
        	//Report status that file is read
        	System.out.println("File is imported successfully!");
        	ai.importScene(rawLevelScene, maxWidth, height);
        	ai.getPlatformData();
			System.out.println("Number of platforms found: " + ai.getLandCount());
        } catch (FileNotFoundException e) {
			// Report status that file is not found
			System.out.println("File is missing!");
		} catch (IOException e) {
			// Report error of file
			System.out.println("File cannot be read!");
		}


    }

}
