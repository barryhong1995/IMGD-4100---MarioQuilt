import LSystem.Coordinate;
import Rendering.ImageOverlay;
import Rendering.RenderArray;
import Rendering.TestArray;
import LSystem.Grammar;

public class Main {

    public static void main(String args[]){

        TestArray t = new TestArray();
        RenderArray r = new RenderArray();
        ImageOverlay overlay = new ImageOverlay(null);
        Grammar l = new Grammar(t);


        t.buildGround();
        l.runLsystem(3);
        r.buildRenderArray(t);

        t.printArray();
        System.out.println();
        r.printArray();


        overlay.loadBackground("large.jpg");

        for(int i  = 0; i < TestArray.LEVEL_HEIGHT; i++){
            for(int j = 0; j < TestArray.LEVEL_WIDTH; j++){
                overlay.drawBlock(r.getVal(j, i), new Coordinate(j, i));
            }
        }

        overlay.writeImage("out.jpg", "JPG");


    }

}
