package Rendering;

import LSystem.Coordinate;
import generator.Sprite;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageOverlay {

    BufferedImage olImage;
    SpriteMap sprites;

    public static final String SPRITE_PATH = "./GrassTemplate.png";
    public static final Integer B_SIZE = 17;

    public ImageOverlay(BufferedImage bgImage){
        this.olImage = bgImage;
        this.sprites = new SpriteMap();
    }

    public BufferedImage drawBlock(Integer blockID, Coordinate pos){
        BufferedImage block = findBlockImage(blockID);
        return overlayImages(block, pos);
    }

    public BufferedImage overlayImages(BufferedImage fgImage, Coordinate pos){
        if(fgImage.getHeight() > olImage.getHeight()
                || fgImage.getWidth() > olImage.getWidth()){
            return null;
        }

        Graphics2D g = olImage.createGraphics();
        g.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

        g.drawImage(olImage, 0, 0, null);
        g.drawImage(fgImage, B_SIZE * pos.getX(), B_SIZE * pos.getY(), null); //olImage.getHeight() - B_SIZE  * (pos.getY() + 1)

        g.dispose();
        return olImage;
    }

    public BufferedImage findBlockImage(Integer blockID){
        BufferedImage spritePage = readImage(SPRITE_PATH);
        Coordinate c = sprites.findCoords(blockID);

        if(B_SIZE * c.getX() + B_SIZE > spritePage.getWidth() ||
                B_SIZE * c.getY() + B_SIZE > spritePage.getHeight()) { return null; }

        return spritePage.getSubimage(B_SIZE * c.getX(), B_SIZE * c.getY(), B_SIZE, B_SIZE);
    }

    public void loadBackground(String fileLocation) {
        this.olImage = this.readImage(fileLocation);
    }

    public void setSize(int Width, int Height){
    	this.olImage = new BufferedImage(Width * B_SIZE, Height * B_SIZE, BufferedImage.TYPE_INT_RGB);
    	Graphics2D g = olImage.createGraphics();
    	g.setPaint(Sprite.COLOR_SKY);
    	int[] xCoords = {0,0,Width*B_SIZE,Width*B_SIZE};
    	int[] yCoords = {0,Height*B_SIZE,Height*B_SIZE,0};
    	g.fill(new Polygon(xCoords,yCoords,4));
    }
    
    public BufferedImage readImage(String fileLocation) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(fileLocation));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    public void writeImage(String fileLocation, String extension){
        try{
            BufferedImage bi = olImage;
            File outfile = new File(fileLocation);
            ImageIO.write(bi, extension, outfile);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }




}
