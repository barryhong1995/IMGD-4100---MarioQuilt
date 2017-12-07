import java.awt.image.BufferedImage;

public class Main {

    public static void main(String args[]){

        ImageOverlay overlay = new ImageOverlay(null);

        BufferedImage frg = overlay.readImage("./smallred.jpg");

        overlay.loadBackground("large.jpg");
        overlay.overlayImages(frg);

        overlay.writeImage("out.jpg", "JPG");


    }

}
