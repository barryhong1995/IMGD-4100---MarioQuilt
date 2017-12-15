import java.io.IOException;

import LSystem.Coordinate;
import Rendering.ImageOverlay;
import Rendering.RenderArray;
import Rendering.TestArray;
import LSystem.Grammar;
import generator.QuiltGenerator;
import generator.Sprite;

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

        int[][] tArray = t.getArray();
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


    }

}
