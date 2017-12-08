import java.awt.image.BufferedImage;

public class Main {

    public static void main(String args[]){

        ImageOverlay overlay = new ImageOverlay(null);

        overlay.loadBackground("large.jpg");
        overlay.drawBlock("questionblock");

        overlay.writeImage("out.jpg", "JPG");


    }

}
