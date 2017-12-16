package generator;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class QuiltGenerator {

	private final static int TL = 1, TM = 2, TR = 4, ML = 8, MR = 16, BL = 32, BM = 64, BR = 128;

	private final static int PIXELS_PER_COORDINATE = 16;
	private final static int DRAWING_SCALE = 2;
	private final static int BLOCKSIZE = PIXELS_PER_COORDINATE * DRAWING_SCALE;
	private final static int HALF_BLOCKSIZE = BLOCKSIZE / 2;
	private final static int QUARTER_BLOCKSIZE = BLOCKSIZE / 4;
	private final static int EIGTH_BLOCKSIZE = BLOCKSIZE / 8;
	private final static int THREE_BLOCKSIZE = HALF_BLOCKSIZE + QUARTER_BLOCKSIZE;

	private int[][] level;
	private int world = 0;
	//How to draw the sky. "Diamonds", "Dirt", "Rectangles", or "NONE"
	private String skyDraw = "Rectangles";
	private ArrayList <Polygon> skyPolygons;
	private BufferedImage output_image;
	private Graphics2D g2D;

	/**
	 * Instantiate a new QuiltGenerator with the level LEVEL and default world =
	 * 0
	 * 
	 * @param LEVEL
	 *            The level to generate a quilt from
	 */
	public QuiltGenerator(int[][] LEVEL) {
		level = LEVEL;
		output_image = new BufferedImage(level[0].length * BLOCKSIZE, level.length * BLOCKSIZE,
				BufferedImage.TYPE_INT_RGB);
		g2D = output_image.createGraphics();
		for (int i = 0; i < level.length; i++){
			for (int j = 0; j < level[0].length; j++){
				switch(level[i][j]){
					case Sprite.ID_DIRT1:
					case Sprite.ID_DIRT2:
					case Sprite.ID_DIRT3:
						level[i][j] = Sprite.ID_GRASS_MM;
						break;
					case Sprite.ID_END:
						level[i][j] = Sprite.ID_AIR;
						break;
					default:
						break;
				}
			}
		}
	}

	/**
	 * Instantiate a new QuiltGenerator with level LEVEL and world WORLD
	 * 
	 * @param LEVEL
	 *            The level to generate a quilt from
	 * @param WORLD
	 *            Which world (0-5) to generate the quilt as
	 */
	public QuiltGenerator(int[][] LEVEL, int WORLD) {
		level = LEVEL;
		world = WORLD;
		output_image = new BufferedImage(level[0].length * BLOCKSIZE, level.length * BLOCKSIZE,
				BufferedImage.TYPE_INT_RGB);
		g2D = output_image.createGraphics();
		for (int i = 0; i < level.length; i++){
			for (int j = 0; j < level[0].length; j++){
				switch(level[i][j]){
					case Sprite.ID_DIRT1:
					case Sprite.ID_DIRT2:
					case Sprite.ID_DIRT3:
						level[i][j] = Sprite.ID_GRASS_MM;
						break;
					case Sprite.ID_END:
						level[i][j] = Sprite.ID_AIR;
						break;
					default:
						break;
				}
			}
		}
	}

	/**
	 * Set the value of world
	 * 
	 * @param WORLD
	 *            Which world (0-5) to generate the quilt as
	 */
	public void setWorld(int WORLD) {
		if (WORLD > 5) {
			world = 5;
		} else if (WORLD < 0) {
			world = 0;
		} else {
			world = WORLD;
		}
	}

	/**
	 * Get the value of world
	 * 
	 * @return Which world (0-5) the quilt is being generated as
	 */
	public int getWorld() {
		return world;
	}

	/**
	 * Get the level
	 * 
	 * @return The level the quilt is being generated from
	 */
	public int[][] getLevel() {
		return level;
	}

	/**
	 * Draw a dirt block at XY
	 * @param X The x coordinate to draw it at
	 * @param Y The y coordinate to draw it at
	 * @param ADJ The adjacent blocks of the same ID
	 */
	private void drawDirt(int X, int Y, int ADJ){
		int DRAW_X = X * BLOCKSIZE, DRAW_Y = Y * BLOCKSIZE;
		int MID_X = DRAW_X + HALF_BLOCKSIZE, MID_Y = DRAW_Y + HALF_BLOCKSIZE;
		int END_X = DRAW_X + BLOCKSIZE, END_Y = DRAW_Y + BLOCKSIZE;
		int QUARTER_X = DRAW_X + QUARTER_BLOCKSIZE, QUARTER_Y = DRAW_Y + QUARTER_BLOCKSIZE;
		int THREE_X = END_X - QUARTER_BLOCKSIZE, THREE_Y = END_Y - QUARTER_BLOCKSIZE;

		boolean left = (ADJ & ML) > 0, right = (ADJ & MR) > 0, up = (ADJ & TM) > 0,
				 down = (ADJ & BM) > 0, upright = (ADJ & TR) > 0, downleft = (ADJ & BL) > 0;
				
		ArrayList <Polygon> dirt_light = new ArrayList <Polygon>();
		ArrayList <Polygon> dirt_dark = new ArrayList <Polygon>();
		
		
		//Bottom Left Dark Diamond (only if left and downleft)
		if (left && downleft){
			int[] xBotLeftDarkDiamond = {(DRAW_X - THREE_BLOCKSIZE),(DRAW_X - QUARTER_BLOCKSIZE),QUARTER_X,(DRAW_X - QUARTER_BLOCKSIZE)};
			int[] yBotLeftDarkDiamond = {THREE_Y,QUARTER_Y,THREE_Y,(END_Y + QUARTER_BLOCKSIZE)};
			dirt_dark.add(new Polygon(xBotLeftDarkDiamond,yBotLeftDarkDiamond,4));
		} else {
			//Bottom Left Dark Triangle (into none)
			if (!left){
				int[] xBotLeftDarkTriangle = {DRAW_X,QUARTER_X,DRAW_X};
				int[] yBotLeftDarkTriangle = {MID_Y,THREE_Y,END_Y};
				dirt_dark.add(new Polygon(xBotLeftDarkTriangle,yBotLeftDarkTriangle,3));
			} else { //Bottom Left Dark Rhombus (into left)
				int[] xBotLeftDarkRhombus = {(DRAW_X - HALF_BLOCKSIZE),DRAW_X,QUARTER_X,DRAW_X};
				int[] yBotLeftDarkRhombus = {END_Y,END_Y,THREE_Y,MID_Y};
				dirt_dark.add(new Polygon(xBotLeftDarkRhombus,yBotLeftDarkRhombus,4));
			}
		}
		//Top Right Dark Diamond (only if up and upright)
		if (up && upright){
			int[] xTopRightDarkDiamond = {QUARTER_X,THREE_X,(END_X + QUARTER_BLOCKSIZE),THREE_X};
			int[] yTopRightDarkDiamond = {(DRAW_Y - QUARTER_BLOCKSIZE),(DRAW_Y - THREE_BLOCKSIZE),(DRAW_Y - QUARTER_BLOCKSIZE),QUARTER_Y};
			dirt_dark.add(new Polygon(xTopRightDarkDiamond,yTopRightDarkDiamond,4));
		} else {
			//Top Right Dark Triangle (into none)
			if (!up){
				int[] xTopRightDarkTriangle = {MID_X,THREE_X,END_X};
				int[] yTopRightDarkTriangle = {DRAW_Y,QUARTER_Y,DRAW_Y};
				dirt_dark.add(new Polygon(xTopRightDarkTriangle,yTopRightDarkTriangle,3));
			} else { //Top Right Dark Rhombus (into up)
				int[] xTopRightDarkRhombus = {MID_X,THREE_X,END_X,END_X};
				int[] yTopRightDarkRhombus = {DRAW_Y,QUARTER_Y,DRAW_Y,(DRAW_Y - HALF_BLOCKSIZE)};
				dirt_dark.add(new Polygon(xTopRightDarkRhombus,yTopRightDarkRhombus,4));
			}
		}
		//Top Right Light Diamond (only if right and upright)
		if (right && upright){
			int[] xTopRightLightDiamond = {THREE_X,(END_X + QUARTER_BLOCKSIZE),(END_X + THREE_BLOCKSIZE),(END_X + QUARTER_BLOCKSIZE)};
			int[] yTopRightLightDiamond = {QUARTER_Y,(DRAW_Y - QUARTER_BLOCKSIZE),QUARTER_Y,THREE_Y};
			dirt_light.add(new Polygon(xTopRightLightDiamond,yTopRightLightDiamond,4));
		} else {
			//Middle Right Light Triangle (into none)
			if (!right){
				int[] xMidRightLightTriangle = {END_X,THREE_X,END_X};
				int[] yMidRightLightTriangle = {DRAW_Y,QUARTER_Y,MID_Y};
				dirt_light.add(new Polygon(xMidRightLightTriangle,yMidRightLightTriangle,3));
			} else { //Middle Right Light Rhombus (into right)
				int[] xMidRightLightRhombus = {END_X,THREE_X,END_X,(END_X + HALF_BLOCKSIZE)};
				int[] yMidRightLightRhombus = {DRAW_Y,QUARTER_Y,MID_Y,DRAW_Y};
				dirt_light.add(new Polygon(xMidRightLightRhombus,yMidRightLightRhombus,4));
			}
		}
		//Bottom Left Light Diamond (only if down and downleft)
		if (down && downleft){
			int[] xBotLeftLightDiamond = {(DRAW_X - QUARTER_BLOCKSIZE),QUARTER_X,THREE_X,QUARTER_X};
			int[] yBotLeftLightDiamond = {(END_Y + QUARTER_BLOCKSIZE),THREE_Y,(END_Y + QUARTER_BLOCKSIZE),(END_Y + THREE_BLOCKSIZE)};
			dirt_light.add(new Polygon(xBotLeftLightDiamond,yBotLeftLightDiamond,4));
		} else {
			//Bottom Right Light Triangle (into none)
			if (!down){
				int[] xBotRightLightTriangle = {DRAW_X,QUARTER_X,MID_X};
				int[] yBotRightLightTriangle = {END_Y,THREE_Y,END_Y};
				dirt_light.add(new Polygon(xBotRightLightTriangle,yBotRightLightTriangle,3));
			} else { //Bottom Left Light Rhombus (into down)
				int[] xBotLeftLightRhombus = {DRAW_X,DRAW_X,QUARTER_X,MID_X};
				int[] yBotLeftLightRhombus = {(END_Y + HALF_BLOCKSIZE),END_Y,THREE_Y,END_Y};
				dirt_light.add(new Polygon(xBotLeftLightRhombus,yBotLeftLightRhombus,4));
			}
		}
		//Bottom Right Dark Diamond (only if right and down)
		if (right && down){
			int[] xBotRightDarkDiamond = {QUARTER_X,THREE_X,(END_X + QUARTER_BLOCKSIZE),THREE_X};
			int[] yBotRightDarkDiamond = {THREE_Y,(END_Y + QUARTER_BLOCKSIZE),THREE_Y,QUARTER_Y};
			dirt_dark.add(new Polygon(xBotRightDarkDiamond,yBotRightDarkDiamond,4));
		} else {
			//Will have to be more than 1 polygon: Middle Dark Rhombus
			int[] xMidDarkRhombus = {QUARTER_X,THREE_X,END_X,MID_X};
			int[] yMidDarkRhombus = {THREE_Y,QUARTER_Y,MID_Y,END_Y};
			dirt_dark.add(new Polygon(xMidDarkRhombus,yMidDarkRhombus,4));
			//Bottom Right Dark Rhombus
			if (right && !down){ //into right only
				int[] xBotRightDarkRhombus = {MID_X,END_X,(END_X + QUARTER_BLOCKSIZE),END_X};
				int[] yBotRightDarkRhombus = {END_Y,MID_Y,THREE_Y,END_Y};
				dirt_dark.add(new Polygon(xBotRightDarkRhombus,yBotRightDarkRhombus,4));
			} else if (!right && down){ //into down only
				int[] xBotRightDarkRhombus = {MID_X,END_X,END_X,THREE_X};
				int[] yBotRightDarkRhombus = {END_Y,MID_Y,END_Y,(END_Y + QUARTER_BLOCKSIZE)};
				dirt_dark.add(new Polygon(xBotRightDarkRhombus,yBotRightDarkRhombus,4));
			} else if (!right && !down){ //Bottom Right Dark Triangle (into none)
				int[] xBotRightDarkTriangle = {MID_X,END_X,END_X};
				int[] yBotRightDarkTriangle = {END_Y,MID_Y,END_Y};
				dirt_dark.add(new Polygon(xBotRightDarkTriangle,yBotRightDarkTriangle,3));
			}
		}
		//Top Left Light Diamond (only if up and left)
		if (up && left){
			int[] xTopLeftLightDiamond = {(DRAW_X - QUARTER_BLOCKSIZE),QUARTER_X,THREE_X,QUARTER_X};
			int[] yTopLeftLightDiamond = {QUARTER_Y,(DRAW_Y - QUARTER_BLOCKSIZE),QUARTER_Y,THREE_Y};
			dirt_light.add(new Polygon(xTopLeftLightDiamond,yTopLeftLightDiamond,4));
		} else { 
			//Will have to be more than 1 polygon: Middle Light Rhombus
			int[] xMidLightRhombus = {DRAW_X,QUARTER_X,THREE_X,MID_X};
			int[] yMidLightRhombus = {MID_Y,THREE_Y,QUARTER_Y,DRAW_Y};
			dirt_light.add(new Polygon(xMidLightRhombus,yMidLightRhombus,4));
			//Top Left Light Rhombus
			if (up && !left){ //into up only
				int[] xTopLeftLightRhombus = {DRAW_X,DRAW_X,MID_X,QUARTER_X};
				int[] yTopLeftLightRhombus = {DRAW_Y,MID_Y,DRAW_Y,(DRAW_Y - QUARTER_BLOCKSIZE)};
				dirt_light.add(new Polygon(xTopLeftLightRhombus,yTopLeftLightRhombus,4));
			} else if (!up && left){ //into left only
				int[] xTopLeftLightRhombus = {(DRAW_X - QUARTER_BLOCKSIZE),DRAW_X,MID_X,DRAW_X};
				int[] yTopLeftLightRhombus = {QUARTER_Y,MID_Y,DRAW_Y,DRAW_Y};
				dirt_light.add(new Polygon(xTopLeftLightRhombus,yTopLeftLightRhombus,4));
			} else { //Top Left Light Triangle (into none)
				int[] xTopLeftLightTriangle = {DRAW_X,DRAW_X,MID_X};
				int[] yTopLeftLightTriangle = {MID_Y,DRAW_Y,DRAW_Y};
				dirt_light.add(new Polygon(xTopLeftLightTriangle,yTopLeftLightTriangle,3));
			}
		}
		
		//////////////////Draw Into Grass Blocks
		int UPLEFT = -2, UP = -2, UPRIGHT = -2;
		int LEFT = -2, RIGHT = -2;
		int DOWNLEFT = -2, DOWN = -2;
		boolean into_up = false, into_upright = false, into_left = false, into_right = false, into_down = false, into_downleft = false;
		if (X > 0) {
			LEFT = level[Y][X - 1];
			if (Y > 0) UPLEFT = level[Y - 1][X - 1];
			if (Y < level.length - 1) DOWNLEFT = level[Y + 1][X - 1];
		}
		if (X < level[0].length - 1) {
			RIGHT = level[Y][X + 1];
			if (Y > 0) UPRIGHT = level[Y - 1][X + 1];
		}
		if (Y > 0) UP = level[Y - 1][X];
		if (Y < level.length - 1) DOWN = level[Y + 1][X];
		//Up can be TL, TM, TR, ML, or MR
		into_up = (UP == Sprite.ID_GRASS_TL || UP == Sprite.ID_GRASS_TM ||
				UP == Sprite.ID_GRASS_TR || UP == Sprite.ID_GRASS_ML ||
				UP == Sprite.ID_GRASS_MR);
		//If (Up is TM, TL, or ML) or (Right is BM, BR, or MR): Upright can be TM, TR, or MR
		//Else: Upright is inaccessible
		into_upright = (UPRIGHT == Sprite.ID_GRASS_TR || UPRIGHT == Sprite.ID_GRASS_TM ||
				UPRIGHT == Sprite.ID_GRASS_MR);
		//Left can be TL, TM, ML, BL, or BM
		into_left = (LEFT == Sprite.ID_GRASS_TL || LEFT == Sprite.ID_GRASS_TM ||
				LEFT == Sprite.ID_GRASS_ML || LEFT == Sprite.ID_GRASS_BL ||
				LEFT == Sprite.ID_GRASS_BM);
		//If (Left is TR, TM, or ML) or (Down is BM, BR, or MR): Downleft can be ML, BL, or BM
		//Else: Downleft is inaccessible
		into_downleft = (DOWNLEFT == Sprite.ID_GRASS_ML ||  DOWNLEFT == Sprite.ID_GRASS_BL ||
				DOWNLEFT == Sprite.ID_GRASS_BM);
		//Down can be ML, MR, BL, BM, or BR
		into_down = (DOWN == Sprite.ID_GRASS_ML || DOWN == Sprite.ID_GRASS_MR ||
				DOWN == Sprite.ID_GRASS_BL || DOWN == Sprite.ID_GRASS_BR ||
				DOWN == Sprite.ID_GRASS_BM);
		//Right can be TM, TR, MR, BM, or BR
		into_right = (RIGHT == Sprite.ID_GRASS_TM || RIGHT == Sprite.ID_GRASS_TR ||
				RIGHT == Sprite.ID_GRASS_MR || RIGHT == Sprite.ID_GRASS_BM ||
				RIGHT == Sprite.ID_GRASS_BR);
		
		////Topleft Light Dirt
		if (into_up && UP != Sprite.ID_GRASS_TL && UP != Sprite.ID_GRASS_ML){ //has to extend into up
			if (into_left && LEFT != Sprite.ID_GRASS_TL && LEFT != Sprite.ID_GRASS_TM){ //has to extend into left
				if (LEFT == Sprite.ID_GRASS_ML){ //deeper
					if (UP == Sprite.ID_GRASS_TM){ //deeper
						int[] xTLLD = {(DRAW_X - THREE_BLOCKSIZE),(DRAW_X - HALF_BLOCKSIZE),END_X,THREE_X};
						int[] yTLLD = {THREE_Y,END_Y,(DRAW_Y - HALF_BLOCKSIZE),(DRAW_Y - THREE_BLOCKSIZE)};
						dirt_light.add(new Polygon(xTLLD,yTLLD,4));
					} else { //not as deep
						int[] xTLLD = {(DRAW_X - THREE_BLOCKSIZE),(DRAW_X - HALF_BLOCKSIZE),THREE_X,MID_X};
						int[] yTLLD = {THREE_Y,END_Y,(DRAW_Y - QUARTER_BLOCKSIZE),(DRAW_Y - HALF_BLOCKSIZE)};
						dirt_light.add(new Polygon(xTLLD,yTLLD,4));
					}
				} else { //not as deep
					if (UP == Sprite.ID_GRASS_TM){ //deeper
						int[] xTLLD = {(DRAW_X - HALF_BLOCKSIZE),(DRAW_X - QUARTER_BLOCKSIZE),END_X,THREE_X};
						int[] yTLLD = {MID_Y,THREE_Y,(DRAW_Y - HALF_BLOCKSIZE),(DRAW_Y - THREE_BLOCKSIZE)};
						dirt_light.add(new Polygon(xTLLD,yTLLD,4));
					} else { //not as deep
						int[] xTLLD = {(DRAW_X - HALF_BLOCKSIZE),(DRAW_X - QUARTER_BLOCKSIZE),THREE_X,MID_X};
						int[] yTLLD = {MID_Y,THREE_Y,(DRAW_Y - QUARTER_BLOCKSIZE),(DRAW_Y - HALF_BLOCKSIZE)};
						dirt_light.add(new Polygon(xTLLD,yTLLD,4));
					}
				}
			} else if (left){ //into left
				if (UP == Sprite.ID_GRASS_TM){ //deeper
					int[] xTLLD = {(DRAW_X - QUARTER_BLOCKSIZE),DRAW_X,END_X,THREE_X};
					int[] yTLLD = {QUARTER_Y,MID_Y,(DRAW_Y - HALF_BLOCKSIZE),(DRAW_Y - THREE_BLOCKSIZE)};
					dirt_light.add(new Polygon(xTLLD,yTLLD,4));
				} else { //not as deep
					int[] xTLLD = {(DRAW_X - QUARTER_BLOCKSIZE),DRAW_X,THREE_X,MID_X};
					int[] yTLLD = {QUARTER_Y,MID_Y,(DRAW_Y - QUARTER_BLOCKSIZE),(DRAW_Y - HALF_BLOCKSIZE)};
					dirt_light.add(new Polygon(xTLLD,yTLLD,4));
				}
			} else { //into none (left)
				if (UP == Sprite.ID_GRASS_TM){ //deeper
					int[] xTLLD = {DRAW_X,DRAW_X,END_X,THREE_X};
					int[] yTLLD = {DRAW_Y,MID_Y,(DRAW_Y - HALF_BLOCKSIZE),(DRAW_Y - THREE_BLOCKSIZE)};
					dirt_light.add(new Polygon(xTLLD,yTLLD,4));
				} else { //not as deep
					int[] xTLLD = {DRAW_X,DRAW_X,THREE_X,MID_X};
					int[] yTLLD = {DRAW_Y,MID_Y,(DRAW_Y - QUARTER_BLOCKSIZE),(DRAW_Y - HALF_BLOCKSIZE)};
					dirt_light.add(new Polygon(xTLLD,yTLLD,4));
				}
			}	
		} else if (up){ //into up
			if (into_left && LEFT != Sprite.ID_GRASS_TL && LEFT != Sprite.ID_GRASS_TM){ //has to extend into left
				if (LEFT == Sprite.ID_GRASS_ML){ //deeper
					int[] xTLLD = {(DRAW_X - THREE_BLOCKSIZE),(DRAW_X - HALF_BLOCKSIZE),MID_X,QUARTER_X};
					int[] yTLLD = {THREE_Y,END_Y,DRAW_Y,(DRAW_Y - QUARTER_BLOCKSIZE)};
					dirt_light.add(new Polygon(xTLLD,yTLLD,4));
				} else { //not as deep
					int[] xTLLD = {(DRAW_X - HALF_BLOCKSIZE),(DRAW_X - QUARTER_BLOCKSIZE),MID_X,QUARTER_X};
					int[] yTLLD = {MID_Y,THREE_Y,DRAW_Y,(DRAW_Y - QUARTER_BLOCKSIZE)};
					dirt_light.add(new Polygon(xTLLD,yTLLD,4));
				}
			}
		} else { //into none (up)
			if (into_left && LEFT != Sprite.ID_GRASS_TL && LEFT != Sprite.ID_GRASS_TM){ //has to extend into left
				if (LEFT == Sprite.ID_GRASS_ML){ //deeper
					int[] xTLLD = {(DRAW_X - THREE_BLOCKSIZE),(DRAW_X - HALF_BLOCKSIZE),MID_X,DRAW_X};
					int[] yTLLD = {THREE_Y,END_Y,DRAW_Y,DRAW_Y};
					dirt_light.add(new Polygon(xTLLD,yTLLD,4));
				} else { //not as deep
					int[] xTLLD = {(DRAW_X - HALF_BLOCKSIZE),(DRAW_X - QUARTER_BLOCKSIZE),MID_X,DRAW_X};
					int[] yTLLD = {MID_Y,THREE_Y,DRAW_Y,DRAW_Y};
					dirt_light.add(new Polygon(xTLLD,yTLLD,4));
				}
			}
		}
		////Bottom Right Dark Dirt
		if (into_right && RIGHT != Sprite.ID_GRASS_BR && RIGHT != Sprite.ID_GRASS_BM){ //has to be extended into right
			if (into_down && DOWN != Sprite.ID_GRASS_BR && DOWN != Sprite.ID_GRASS_MR){ //has to extend into down
				if (DOWN == Sprite.ID_GRASS_BM){ //deeper
					if (RIGHT == Sprite.ID_GRASS_MR){ //deeper
						int[] xBRDD = {DRAW_X,QUARTER_X,(END_X + THREE_BLOCKSIZE),(END_X + HALF_BLOCKSIZE)};
						int[] yBRDD = {(END_Y + HALF_BLOCKSIZE),(END_Y + THREE_BLOCKSIZE),QUARTER_Y,DRAW_Y};
						dirt_dark.add(new Polygon(xBRDD,yBRDD,4));
					} else { //not as deep
						int[] xBRDD = {DRAW_X,QUARTER_X,(END_X + HALF_BLOCKSIZE),(END_X + QUARTER_BLOCKSIZE)};
						int[] yBRDD = {(END_Y + HALF_BLOCKSIZE),(END_Y + THREE_BLOCKSIZE),MID_Y,QUARTER_Y};
						dirt_dark.add(new Polygon(xBRDD,yBRDD,4));
					}
				} else { //not as deep
					if (RIGHT == Sprite.ID_GRASS_MR){ //deeper
						int[] xBRDD = {QUARTER_X,MID_X,(END_X + THREE_BLOCKSIZE),(END_X + HALF_BLOCKSIZE)};
						int[] yBRDD = {(END_Y + QUARTER_BLOCKSIZE),(END_Y + HALF_BLOCKSIZE),QUARTER_Y,DRAW_Y};
						dirt_dark.add(new Polygon(xBRDD,yBRDD,4));
					} else { //not as deep
						int[] xBRDD = {QUARTER_X,MID_X,(END_X + HALF_BLOCKSIZE),(END_X + QUARTER_BLOCKSIZE)};
						int[] yBRDD = {(END_Y + QUARTER_BLOCKSIZE),(END_Y + HALF_BLOCKSIZE),MID_Y,QUARTER_Y};
						dirt_dark.add(new Polygon(xBRDD,yBRDD,4));
					}
				}
			} else if (down) { //into down
				if (RIGHT == Sprite.ID_GRASS_MR){ //deeper
					int[] xBRDD = {MID_X,THREE_X,(END_X + THREE_BLOCKSIZE),(END_X + HALF_BLOCKSIZE)};
					int[] yBRDD = {END_Y,(END_Y + QUARTER_BLOCKSIZE),QUARTER_Y,DRAW_Y};
					dirt_dark.add(new Polygon(xBRDD,yBRDD,4));
				} else { //not as deep
					int[] xBRDD = {MID_X,THREE_X,(END_X + HALF_BLOCKSIZE),(END_X + QUARTER_BLOCKSIZE)};
					int[] yBRDD = {END_Y,(END_Y + QUARTER_BLOCKSIZE),MID_Y,QUARTER_Y};
					dirt_dark.add(new Polygon(xBRDD,yBRDD,4));
				}
			} else { //into none (down)
				if (RIGHT == Sprite.ID_GRASS_MR){ //deeper
					int[] xBRDD = {MID_X,END_X,(END_X + THREE_BLOCKSIZE),(END_X + HALF_BLOCKSIZE)};
					int[] yBRDD = {END_Y,END_Y,QUARTER_Y,DRAW_Y};
					dirt_dark.add(new Polygon(xBRDD,yBRDD,4));
				} else { //not as deep
					int[] xBRDD = {MID_X,END_X,(END_X + HALF_BLOCKSIZE),(END_X + QUARTER_BLOCKSIZE)};
					int[] yBRDD = {END_Y,END_Y,MID_Y,QUARTER_Y};
					dirt_dark.add(new Polygon(xBRDD,yBRDD,4));
				}
			}
		} else if (right){ //into right
			if (into_down && DOWN != Sprite.ID_GRASS_BR && DOWN != Sprite.ID_GRASS_MR){ //has to extend into down
				if (DOWN == Sprite.ID_GRASS_BM){ //deeper
					int[] xBRDD = {DRAW_X,QUARTER_X,(END_X + QUARTER_BLOCKSIZE),END_X};
					int[] yBRDD = {(END_Y + HALF_BLOCKSIZE),(END_Y + THREE_BLOCKSIZE),THREE_Y,MID_Y};
					dirt_dark.add(new Polygon(xBRDD,yBRDD,4));
				} else { //not as deep
					int[] xBRDD = {QUARTER_X,MID_X,(END_X + QUARTER_BLOCKSIZE),END_X};
					int[] yBRDD = {(END_Y + QUARTER_BLOCKSIZE),(END_Y + HALF_BLOCKSIZE),THREE_Y,MID_Y};
					dirt_dark.add(new Polygon(xBRDD,yBRDD,4));
				}
			}
		} else { //into none (right)
			if (into_down && DOWN != Sprite.ID_GRASS_BR && DOWN != Sprite.ID_GRASS_MR){ //has to extend into down
				if (DOWN == Sprite.ID_GRASS_BM){ //deeper
					int[] xBRDD = {DRAW_X,QUARTER_X,END_X,END_X};
					int[] yBRDD = {(END_Y + HALF_BLOCKSIZE),(END_Y + THREE_BLOCKSIZE),END_Y,MID_Y};
					dirt_dark.add(new Polygon(xBRDD,yBRDD,4));
				} else { //not as deep
					int[] xBRDD = {QUARTER_X,MID_X,END_X,END_X};
					int[] yBRDD = {(END_Y + QUARTER_BLOCKSIZE),(END_Y + HALF_BLOCKSIZE),END_Y,MID_Y};
					dirt_dark.add(new Polygon(xBRDD,yBRDD,4));
				}
			}
		}
		////Middle Left Dark Dirt
		if (left || (into_left && LEFT != Sprite.ID_GRASS_BL && LEFT != Sprite.ID_GRASS_BM)){ //extends into left
			if (into_downleft){ //extends into downleft
				if (DOWNLEFT == Sprite.ID_GRASS_BM){ //deeper
					int[] xMLDD = {(DRAW_X - BLOCKSIZE),(DRAW_X - THREE_BLOCKSIZE),QUARTER_X,DRAW_X};
					int[] yMLDD = {(END_Y + HALF_BLOCKSIZE),(END_Y + THREE_BLOCKSIZE),THREE_Y,MID_Y};
					dirt_dark.add(new Polygon(xMLDD,yMLDD,4));
				} else { //not as deep
					int[] xMLDD = {(DRAW_X - THREE_BLOCKSIZE),(DRAW_X - HALF_BLOCKSIZE),QUARTER_X,DRAW_X};
					int[] yMLDD = {(END_Y + QUARTER_BLOCKSIZE),(END_Y + HALF_BLOCKSIZE),THREE_Y,MID_Y};
					dirt_dark.add(new Polygon(xMLDD,yMLDD,4));
				}
			} else if (downleft){ //into downleft
				//int[] xMLDD = {(DRAW_X - HALF_BLOCKSIZE),(DRAW_X - QUARTER_BLOCKSIZE),QUARTER_X,DRAW_X};
				//int[] yMLDD = {END_Y,(END_Y + QUARTER_BLOCKSIZE),THREE_Y,MID_Y};
				//dirt_dark.add(new Polygon(xMLDD,yMLDD,4));
			} else { //only into left
				int[] xMLDD = {(DRAW_X - HALF_BLOCKSIZE),DRAW_X,QUARTER_X,DRAW_X};
				int[] yMLDD = {END_Y,END_Y,THREE_Y,MID_Y};
				dirt_dark.add(new Polygon(xMLDD,yMLDD,4));
			}
		}
		////Middle Right Dark Dirt
		if (up || (into_up && UP != Sprite.ID_GRASS_TR && UP != Sprite.ID_GRASS_MR)){ //extends into up
			if (into_upright){ //extends into upright
				if (UPRIGHT == Sprite.ID_GRASS_MR){ //deeper
					int[] xMRDD = {MID_X,THREE_X,(END_X + THREE_BLOCKSIZE),(END_X + HALF_BLOCKSIZE)};
					int[] yMRDD = {DRAW_Y,QUARTER_Y,(DRAW_Y - THREE_BLOCKSIZE),(DRAW_Y - BLOCKSIZE)};
					dirt_dark.add(new Polygon(xMRDD,yMRDD,4));
				} else {
					int[] xMRDD = {MID_X,THREE_X,(END_X + HALF_BLOCKSIZE),(END_X + QUARTER_BLOCKSIZE)};
					int[] yMRDD = {DRAW_Y,QUARTER_Y,(DRAW_Y - HALF_BLOCKSIZE),(DRAW_Y - THREE_BLOCKSIZE)};
					dirt_dark.add(new Polygon(xMRDD,yMRDD,4));
				}
			} else if (upright){ //into upright
				//int[] xMRDD = {MID_X,THREE_X,(END_X + QUARTER_BLOCKSIZE),END_X};
				//int[] yMRDD = {DRAW_Y,QUARTER_Y,(DRAW_Y - QUARTER_BLOCKSIZE),(DRAW_Y - HALF_BLOCKSIZE)};
				//dirt_dark.add(new Polygon(xMRDD,yMRDD,4));
			} else { //only into up
				int[] xMRDD = {MID_X,THREE_X,END_X,END_X};
				int[] yMRDD = {DRAW_Y,QUARTER_Y,DRAW_Y,(DRAW_Y - HALF_BLOCKSIZE)};
				dirt_dark.add(new Polygon(xMRDD,yMRDD,4));
			}
		}
		////Bottom Left Light Dirt
		if (down || (into_down && DOWN != Sprite.ID_GRASS_BL && DOWN != Sprite.ID_GRASS_ML)){ //extends into down
			if (into_downleft){ //extends into downleft
				if (DOWNLEFT == Sprite.ID_GRASS_ML){ //deeper
					int[] xBLLD = {(DRAW_X - THREE_BLOCKSIZE),(DRAW_X-HALF_BLOCKSIZE),MID_X,QUARTER_X};
					int[] yBLLD = {(END_Y + THREE_BLOCKSIZE),(END_Y + BLOCKSIZE),END_Y,THREE_Y};
					dirt_light.add(new Polygon(xBLLD,yBLLD,4));
				} else { //not as deep
					int[] xBLLD = {(DRAW_X - HALF_BLOCKSIZE),(DRAW_X-QUARTER_BLOCKSIZE),MID_X,QUARTER_X};
					int[] yBLLD = {(END_Y + HALF_BLOCKSIZE),(END_Y + THREE_BLOCKSIZE),END_Y,THREE_Y};
					dirt_light.add(new Polygon(xBLLD,yBLLD,4));
				}
			} else if (downleft){ //into downleft
				int[] xBLLD = {(DRAW_X - QUARTER_BLOCKSIZE),DRAW_X,MID_X,QUARTER_X};
				int[] yBLLD = {(END_Y + QUARTER_BLOCKSIZE),(END_Y + HALF_BLOCKSIZE),END_Y,THREE_Y};
				dirt_light.add(new Polygon(xBLLD,yBLLD,4));
			} else { //into only down
				int[] xBLLD = {DRAW_X,DRAW_X,MID_X,QUARTER_X};
				int[] yBLLD = {END_Y,(END_Y + HALF_BLOCKSIZE),END_Y,THREE_Y};
				dirt_light.add(new Polygon(xBLLD,yBLLD,4));
			}
		}
		////Bottom Right Light Dirt
		if (right || (into_right && RIGHT != Sprite.ID_GRASS_TM && RIGHT != Sprite.ID_GRASS_TR)){ //extends into right
			if (into_upright){ //extends into upright
				if (UPRIGHT == Sprite.ID_GRASS_TM){ //deeper
					int[] xBRLD = {THREE_X,END_X,(END_X + BLOCKSIZE),(END_X + THREE_BLOCKSIZE)};
					int[] yBRLD = {QUARTER_Y,MID_Y,(DRAW_Y - HALF_BLOCKSIZE),(DRAW_Y - THREE_BLOCKSIZE)};
					dirt_light.add(new Polygon(xBRLD,yBRLD,4));
				} else { //not as deep
					int[] xBRLD = {THREE_X,END_X,(END_X + THREE_BLOCKSIZE),(END_X + HALF_BLOCKSIZE)};
					int[] yBRLD = {QUARTER_Y,MID_Y,(DRAW_Y - QUARTER_BLOCKSIZE),(DRAW_Y - HALF_BLOCKSIZE)};
					dirt_light.add(new Polygon(xBRLD,yBRLD,4));
				}
			} else if (upright){ //into upright
				int[] xBRLD = {THREE_X,END_X,(END_X + HALF_BLOCKSIZE),(END_X + QUARTER_BLOCKSIZE)};
				int[] yBRLD = {QUARTER_Y,MID_Y,DRAW_Y,(DRAW_Y - QUARTER_BLOCKSIZE)};
				dirt_light.add(new Polygon(xBRLD,yBRLD,4));
			} else { //into only right
				int[] xBRLD = {THREE_X,END_X,(END_X + HALF_BLOCKSIZE),END_X};
				int[] yBRLD = {QUARTER_Y,MID_Y,DRAW_Y,DRAW_Y};
				dirt_light.add(new Polygon(xBRLD,yBRLD,4));
			}
		}
		////Fringe Cases
		if (UPLEFT == Sprite.ID_GRASS_TL || UPLEFT == Sprite.ID_GRASS_TM || UPLEFT == Sprite.ID_GRASS_ML){
			if (UP == Sprite.ID_GRASS_MM && LEFT == Sprite.ID_GRASS_MM){
				int[] xFringe1 = {(DRAW_X - HALF_BLOCKSIZE),(DRAW_X - QUARTER_BLOCKSIZE),QUARTER_X,DRAW_X};
				int[] yFringe1 = {DRAW_Y,QUARTER_Y,(DRAW_Y - QUARTER_BLOCKSIZE),(DRAW_Y - HALF_BLOCKSIZE)};
				dirt_dark.add(new Polygon(xFringe1,yFringe1,4));
			}
		}
		
		
		for (Polygon poly : dirt_light) {
			drawShape(poly, Sprite.COLOR_DIRT_DARK, Sprite.COLOR_BORDER);
		}
		for (Polygon poly : dirt_dark) {
			drawShape(poly, Sprite.COLOR_DIRT_LIGHT, Sprite.COLOR_BORDER);
		}
	}
	
	/**
	 * Draw a block like a dirt block at XY
	 * @param X The x coordinate to draw it at
	 * @param Y The y coordinate to draw it at
	 * @param ADJ The adjacent blocks of the same ID
	 * @param LIGHT The light color to draw with
	 * @Param DARK The dark color to draw with
	 */
	private void drawLikeDirt(int X, int Y, int ADJ, Color LIGHT, Color DARK){
		int DRAW_X = X * BLOCKSIZE, DRAW_Y = Y * BLOCKSIZE;
		int MID_X = DRAW_X + HALF_BLOCKSIZE, MID_Y = DRAW_Y + HALF_BLOCKSIZE;
		int END_X = DRAW_X + BLOCKSIZE, END_Y = DRAW_Y + BLOCKSIZE;
		int QUARTER_X = DRAW_X + QUARTER_BLOCKSIZE, QUARTER_Y = DRAW_Y + QUARTER_BLOCKSIZE;
		int THREE_X = END_X - QUARTER_BLOCKSIZE, THREE_Y = END_Y - QUARTER_BLOCKSIZE;

		boolean left = (ADJ & ML) > 0, right = (ADJ & MR) > 0, up = (ADJ & TM) > 0,
				 down = (ADJ & BM) > 0, upright = (ADJ & TR) > 0, downleft = (ADJ & BL) > 0;
				
		ArrayList <Polygon> dirt_light = new ArrayList <Polygon>();
		ArrayList <Polygon> dirt_dark = new ArrayList <Polygon>();
		
		
		//Bottom Left Dark Diamond (only if left and downleft)
		if (left && downleft){
			int[] xBotLeftDarkDiamond = {(DRAW_X - THREE_BLOCKSIZE),(DRAW_X - QUARTER_BLOCKSIZE),QUARTER_X,(DRAW_X - QUARTER_BLOCKSIZE)};
			int[] yBotLeftDarkDiamond = {THREE_Y,QUARTER_Y,THREE_Y,(END_Y + QUARTER_BLOCKSIZE)};
			dirt_dark.add(new Polygon(xBotLeftDarkDiamond,yBotLeftDarkDiamond,4));
		} else {
			//Bottom Left Dark Triangle (into none)
			if (!left){
				int[] xBotLeftDarkTriangle = {DRAW_X,QUARTER_X,DRAW_X};
				int[] yBotLeftDarkTriangle = {MID_Y,THREE_Y,END_Y};
				dirt_dark.add(new Polygon(xBotLeftDarkTriangle,yBotLeftDarkTriangle,3));
			} else { //Bottom Left Dark Rhombus (into left)
				int[] xBotLeftDarkRhombus = {(DRAW_X - HALF_BLOCKSIZE),DRAW_X,QUARTER_X,DRAW_X};
				int[] yBotLeftDarkRhombus = {END_Y,END_Y,THREE_Y,MID_Y};
				dirt_dark.add(new Polygon(xBotLeftDarkRhombus,yBotLeftDarkRhombus,4));
			}
		}
		//Top Right Dark Diamond (only if up and upright)
		if (up && upright){
			int[] xTopRightDarkDiamond = {QUARTER_X,THREE_X,(END_X + QUARTER_BLOCKSIZE),THREE_X};
			int[] yTopRightDarkDiamond = {(DRAW_Y - QUARTER_BLOCKSIZE),(DRAW_Y - THREE_BLOCKSIZE),(DRAW_Y - QUARTER_BLOCKSIZE),QUARTER_Y};
			dirt_dark.add(new Polygon(xTopRightDarkDiamond,yTopRightDarkDiamond,4));
		} else {
			//Top Right Dark Triangle (into none)
			if (!up){
				int[] xTopRightDarkTriangle = {MID_X,THREE_X,END_X};
				int[] yTopRightDarkTriangle = {DRAW_Y,QUARTER_Y,DRAW_Y};
				dirt_dark.add(new Polygon(xTopRightDarkTriangle,yTopRightDarkTriangle,3));
			} else { //Top Right Dark Rhombus (into up)
				int[] xTopRightDarkRhombus = {MID_X,THREE_X,END_X,END_X};
				int[] yTopRightDarkRhombus = {DRAW_Y,QUARTER_Y,DRAW_Y,(DRAW_Y - HALF_BLOCKSIZE)};
				dirt_dark.add(new Polygon(xTopRightDarkRhombus,yTopRightDarkRhombus,4));
			}
		}
		//Top Right Light Diamond (only if right and upright)
		if (right && upright){
			int[] xTopRightLightDiamond = {THREE_X,(END_X + QUARTER_BLOCKSIZE),(END_X + THREE_BLOCKSIZE),(END_X + QUARTER_BLOCKSIZE)};
			int[] yTopRightLightDiamond = {QUARTER_Y,(DRAW_Y - QUARTER_BLOCKSIZE),QUARTER_Y,THREE_Y};
			dirt_light.add(new Polygon(xTopRightLightDiamond,yTopRightLightDiamond,4));
		} else {
			//Middle Right Light Triangle (into none)
			if (!right){
				int[] xMidRightLightTriangle = {END_X,THREE_X,END_X};
				int[] yMidRightLightTriangle = {DRAW_Y,QUARTER_Y,MID_Y};
				dirt_light.add(new Polygon(xMidRightLightTriangle,yMidRightLightTriangle,3));
			} else { //Middle Right Light Rhombus (into right)
				int[] xMidRightLightRhombus = {END_X,THREE_X,END_X,(END_X + HALF_BLOCKSIZE)};
				int[] yMidRightLightRhombus = {DRAW_Y,QUARTER_Y,MID_Y,DRAW_Y};
				dirt_light.add(new Polygon(xMidRightLightRhombus,yMidRightLightRhombus,4));
			}
		}
		//Bottom Left Light Diamond (only if down and downleft)
		if (down && downleft){
			int[] xBotLeftLightDiamond = {(DRAW_X - QUARTER_BLOCKSIZE),QUARTER_X,THREE_X,QUARTER_X};
			int[] yBotLeftLightDiamond = {(END_Y + QUARTER_BLOCKSIZE),THREE_Y,(END_Y + QUARTER_BLOCKSIZE),(END_Y + THREE_BLOCKSIZE)};
			dirt_light.add(new Polygon(xBotLeftLightDiamond,yBotLeftLightDiamond,4));
		} else {
			//Bottom Right Light Triangle (into none)
			if (!down){
				int[] xBotRightLightTriangle = {DRAW_X,QUARTER_X,MID_X};
				int[] yBotRightLightTriangle = {END_Y,THREE_Y,END_Y};
				dirt_light.add(new Polygon(xBotRightLightTriangle,yBotRightLightTriangle,3));
			} else { //Bottom Left Light Rhombus (into down)
				int[] xBotLeftLightRhombus = {DRAW_X,DRAW_X,QUARTER_X,MID_X};
				int[] yBotLeftLightRhombus = {(END_Y + HALF_BLOCKSIZE),END_Y,THREE_Y,END_Y};
				dirt_light.add(new Polygon(xBotLeftLightRhombus,yBotLeftLightRhombus,4));
			}
		}
		//Bottom Right Dark Diamond (only if right and down)
		if (right && down){
			int[] xBotRightDarkDiamond = {QUARTER_X,THREE_X,(END_X + QUARTER_BLOCKSIZE),THREE_X};
			int[] yBotRightDarkDiamond = {THREE_Y,(END_Y + QUARTER_BLOCKSIZE),THREE_Y,QUARTER_Y};
			dirt_dark.add(new Polygon(xBotRightDarkDiamond,yBotRightDarkDiamond,4));
		} else {
			//Will have to be more than 1 polygon: Middle Dark Rhombus
			int[] xMidDarkRhombus = {QUARTER_X,THREE_X,END_X,MID_X};
			int[] yMidDarkRhombus = {THREE_Y,QUARTER_Y,MID_Y,END_Y};
			dirt_dark.add(new Polygon(xMidDarkRhombus,yMidDarkRhombus,4));
			//Bottom Right Dark Rhombus
			if (right && !down){ //into right only
				int[] xBotRightDarkRhombus = {MID_X,END_X,(END_X + QUARTER_BLOCKSIZE),END_X};
				int[] yBotRightDarkRhombus = {END_Y,MID_Y,THREE_Y,END_Y};
				dirt_dark.add(new Polygon(xBotRightDarkRhombus,yBotRightDarkRhombus,4));
			} else if (!right && down){ //into down only
				int[] xBotRightDarkRhombus = {MID_X,END_X,END_X,THREE_X};
				int[] yBotRightDarkRhombus = {END_Y,MID_Y,END_Y,(END_Y + QUARTER_BLOCKSIZE)};
				dirt_dark.add(new Polygon(xBotRightDarkRhombus,yBotRightDarkRhombus,4));
			} else if (!right && !down){ //Bottom Right Dark Triangle (into none)
				int[] xBotRightDarkTriangle = {MID_X,END_X,END_X};
				int[] yBotRightDarkTriangle = {END_Y,MID_Y,END_Y};
				dirt_dark.add(new Polygon(xBotRightDarkTriangle,yBotRightDarkTriangle,3));
			}
		}
		//Top Left Light Diamond (only if up and left)
		if (up && left){
			int[] xTopLeftLightDiamond = {(DRAW_X - QUARTER_BLOCKSIZE),QUARTER_X,THREE_X,QUARTER_X};
			int[] yTopLeftLightDiamond = {QUARTER_Y,(DRAW_Y - QUARTER_BLOCKSIZE),QUARTER_Y,THREE_Y};
			dirt_light.add(new Polygon(xTopLeftLightDiamond,yTopLeftLightDiamond,4));
		} else { 
			//Will have to be more than 1 polygon: Middle Light Rhombus
			int[] xMidLightRhombus = {DRAW_X,QUARTER_X,THREE_X,MID_X};
			int[] yMidLightRhombus = {MID_Y,THREE_Y,QUARTER_Y,DRAW_Y};
			dirt_light.add(new Polygon(xMidLightRhombus,yMidLightRhombus,4));
			//Top Left Light Rhombus
			if (up && !left){ //into up only
				int[] xTopLeftLightRhombus = {DRAW_X,DRAW_X,MID_X,QUARTER_X};
				int[] yTopLeftLightRhombus = {DRAW_Y,MID_Y,DRAW_Y,(DRAW_Y - QUARTER_BLOCKSIZE)};
				dirt_light.add(new Polygon(xTopLeftLightRhombus,yTopLeftLightRhombus,4));
			} else if (!up && left){ //into left only
				int[] xTopLeftLightRhombus = {(DRAW_X - QUARTER_BLOCKSIZE),DRAW_X,MID_X,DRAW_X};
				int[] yTopLeftLightRhombus = {QUARTER_Y,MID_Y,DRAW_Y,DRAW_Y};
				dirt_light.add(new Polygon(xTopLeftLightRhombus,yTopLeftLightRhombus,4));
			} else { //Top Left Light Triangle (into none)
				int[] xTopLeftLightTriangle = {DRAW_X,DRAW_X,MID_X};
				int[] yTopLeftLightTriangle = {MID_Y,DRAW_Y,DRAW_Y};
				dirt_light.add(new Polygon(xTopLeftLightTriangle,yTopLeftLightTriangle,3));
			}
		}		
		
		for (Polygon poly : dirt_light) {
			drawShape(poly, LIGHT, Sprite.COLOR_BORDER);
		}
		for (Polygon poly : dirt_dark) {
			drawShape(poly, DARK, Sprite.COLOR_BORDER);
		}
	}
	
	/**
	 * Draw a grass block at XY
	 * @param X The x coordinate to draw the grass at
	 * @param Y The y coordinate to draw the grass at
	 */
	private void drawGrass(int X, int Y) {
		int DRAW_X = X * BLOCKSIZE, DRAW_Y = Y * BLOCKSIZE;
		int MID_X = DRAW_X + HALF_BLOCKSIZE, MID_Y = DRAW_Y + HALF_BLOCKSIZE;
		int END_X = DRAW_X + BLOCKSIZE, END_Y = DRAW_Y + BLOCKSIZE;
		int QUARTER_X = DRAW_X + QUARTER_BLOCKSIZE, QUARTER_Y = DRAW_Y + QUARTER_BLOCKSIZE;
		int THREE_X = END_X - QUARTER_BLOCKSIZE, THREE_Y = END_Y - QUARTER_BLOCKSIZE;

		ArrayList <Polygon> grass_light = new ArrayList <Polygon>();
		ArrayList <Polygon> grass_dark = new ArrayList <Polygon>();
		ArrayList <Polygon> dirt_light = new ArrayList <Polygon>();
		ArrayList <Polygon> dirt_dark = new ArrayList <Polygon>();

		int BLOCK_ID = level[Y][X];

		int UPLEFT = -2, UP = -2, UPRIGHT = -2;
		int LEFT = -2, RIGHT = -2;
		int DOWNLEFT = -2, DOWN = -2, DOWNRIGHT = -2;
		if (X > 0) {
			LEFT = level[Y][X - 1];
			if (Y > 0) UPLEFT = level[Y - 1][X - 1];
			if (Y < level.length - 1) DOWNLEFT = level[Y + 1][X - 1];
		}
		if (X < level[0].length - 1) {
			RIGHT = level[Y][X + 1];
			if (Y > 0) UPRIGHT = level[Y - 1][X + 1];
			if (Y < level.length - 1) DOWNRIGHT = level[Y + 1][X + 1];
		}
		if (Y > 0) UP = level[Y - 1][X];
		if (Y < level.length - 1) DOWN = level[Y + 1][X];

		boolean into_upleft = false, into_up = false, into_upright = false;
		boolean into_left = false, into_right = false;
		boolean into_downleft = false, into_down = false, into_downright = false;
		switch (BLOCK_ID) {
			case Sprite.ID_GRASS_MR: //Middle Right Grass
				//Can move into up, left, downleft, and down
				if (UP == Sprite.ID_GRASS_TR || UP == Sprite.ID_GRASS_MR) into_up = true;
				if (LEFT == Sprite.ID_GRASS_ML) into_left = true;
				if (DOWN == Sprite.ID_GRASS_BR || DOWN == Sprite.ID_GRASS_MR) into_down = true;
				if (DOWNLEFT == Sprite.ID_GRASS_ML || DOWNLEFT == Sprite.ID_GRASS_BL || DOWNLEFT == Sprite.ID_GRASS_BM) into_downleft = true;
				////Shapes that happen regardless of adjacency
				//Middle Light Grass
				int[] x1MR = {MID_X,THREE_X,END_X,END_X};
				int[] y1MR = {MID_Y,THREE_Y,MID_Y,DRAW_Y};
				grass_light.add(new Polygon(x1MR,y1MR,4));
				//Top Dark Grass
				if (into_up){ //rhombus into up
					int[] x2MR = {MID_X,THREE_X,END_X,END_X};
					int[] y2MR = {DRAW_Y,QUARTER_Y,DRAW_Y,(DRAW_Y - HALF_BLOCKSIZE)};
					grass_dark.add(new Polygon(x2MR,y2MR,4));
				} else { //triangle into none
					int[] x2MR = {MID_X,THREE_X,END_X};
					int[] y2MR = {DRAW_Y,QUARTER_Y,DRAW_Y};
					grass_dark.add(new Polygon(x2MR,y2MR,3));
				}
				//Bottom Dark Grass
				if (into_down){ //rhombus into down
					int[] x3MR = {MID_X,END_X,END_X,THREE_X};
					int[] y3MR = {END_Y,MID_Y,END_Y,(END_Y + QUARTER_BLOCKSIZE)};
					grass_dark.add(new Polygon(x3MR,y3MR,4));
				} else { //triangle into none
					int[] x3MR = {MID_X,END_X,END_X};
					int[] y3MR = {END_Y,MID_Y,END_Y};
					grass_dark.add(new Polygon(x3MR,y3MR,3));
				}
				//Top Dark Dirt
				if (into_up){
					if (LEFT == Sprite.ID_GRASS_MM){ //rhombus into left-dirt and up
						int[] x4MR = {(DRAW_X - QUARTER_BLOCKSIZE),DRAW_X,THREE_X,MID_X};
						int[] y4MR = {QUARTER_Y,MID_Y,(DRAW_Y - QUARTER_BLOCKSIZE),(DRAW_Y - HALF_BLOCKSIZE)};
						dirt_dark.add(new Polygon(x4MR,y4MR,4));
					} else if (into_left){ //rhombus into up and left
						int[] x4MR = {(DRAW_X - THREE_BLOCKSIZE),(DRAW_X - HALF_BLOCKSIZE),THREE_X,MID_X};
						int[] y4MR = {THREE_Y,END_Y,(DRAW_Y - QUARTER_BLOCKSIZE),(DRAW_Y - HALF_BLOCKSIZE)};
						dirt_dark.add(new Polygon(x4MR,y4MR,4));
					} else { //rhombus into up only
						int[] x4MR = {DRAW_X,DRAW_X,THREE_X,MID_X};
						int[] y4MR = {DRAW_Y,MID_Y,(DRAW_Y - QUARTER_BLOCKSIZE),(DRAW_Y - HALF_BLOCKSIZE)};
						dirt_dark.add(new Polygon(x4MR,y4MR,4));
					}
				} else {
					if (LEFT == Sprite.ID_GRASS_MM){ //rhombus into left dirt only
						int[] x4MR = {DRAW_X,(DRAW_X - QUARTER_BLOCKSIZE),DRAW_X,MID_X};
						int[] y4MR = {DRAW_Y,QUARTER_Y,MID_Y,DRAW_Y};
						dirt_dark.add(new Polygon(x4MR,y4MR,4));
					} else if (into_left){ //rhombus into left only
						int[] x4MR = {(DRAW_X - THREE_BLOCKSIZE),(DRAW_X - HALF_BLOCKSIZE),MID_X,DRAW_X};
						int[] y4MR = {THREE_Y,END_Y,DRAW_Y,DRAW_Y};
						dirt_dark.add(new Polygon(x4MR,y4MR,4));
					} else { //triangle into none
						int[] x4MR = {DRAW_X,DRAW_X,MID_X};
						int[] y4MR = {DRAW_Y,MID_Y,DRAW_Y};
						dirt_dark.add(new Polygon(x4MR,y4MR,3));
					}
				}
				//Bottom Dark Dirt
				if (into_down){
					if (into_downleft){ //rhombus into down and downleft
						if (DOWNLEFT == Sprite.ID_GRASS_ML){
							int[] x5MR = {(DRAW_X - THREE_BLOCKSIZE),(DRAW_X - HALF_BLOCKSIZE),THREE_X,MID_X};
							int[] y5MR = {(END_Y + THREE_BLOCKSIZE),(END_Y + BLOCKSIZE),THREE_Y,MID_Y};
							dirt_dark.add(new Polygon(x5MR,y5MR,4));
						} else { //not as deep
							int[] x5MR = {(DRAW_X - HALF_BLOCKSIZE),(DRAW_X - QUARTER_BLOCKSIZE),THREE_X,MID_X};
							int[] y5MR = {(END_Y + HALF_BLOCKSIZE),(END_Y + THREE_BLOCKSIZE),THREE_Y,MID_Y};
							dirt_dark.add(new Polygon(x5MR,y5MR,4));
						}
					} else if (DOWNLEFT == Sprite.ID_GRASS_MM){ //into down and dirt-downleft
						int[] x5MR = {(DRAW_X - QUARTER_BLOCKSIZE),DRAW_X,THREE_X,MID_X};
						int[] y5MR = {(END_Y + QUARTER_BLOCKSIZE),(END_Y + HALF_BLOCKSIZE),THREE_Y,MID_Y};
						dirt_dark.add(new Polygon(x5MR,y5MR,4));
					} else { //rhombus into down only
						int[] x5MR = {DRAW_X,DRAW_X,THREE_X,MID_X};
						int[] y5MR = {END_Y,(END_Y + HALF_BLOCKSIZE),THREE_Y,MID_Y};
						dirt_dark.add(new Polygon(x5MR,y5MR,4));
					}
				} else { //rhombus into none
					int[] x5MR = {DRAW_X,MID_X,THREE_X,MID_X};
					int[] y5MR = {END_Y,END_Y,THREE_Y,MID_Y};
					dirt_dark.add(new Polygon(x5MR,y5MR,4));
				}
				//Middle Light Dirt
				if (into_left || LEFT == Sprite.ID_GRASS_MM){
					if (into_downleft) { //rhombus into left and downleft
						if (DOWNLEFT == Sprite.ID_GRASS_BM){ //deeper
							int[] x6MR = {(DRAW_X - BLOCKSIZE),(DRAW_X - THREE_BLOCKSIZE),THREE_X,MID_X};
							int[] y6MR = {(END_Y + HALF_BLOCKSIZE),(END_Y + THREE_BLOCKSIZE),QUARTER_Y,DRAW_Y};
							dirt_light.add(new Polygon(x6MR,y6MR,4));
						} else { //not as deep
							int[] x6MR = {(DRAW_X - THREE_BLOCKSIZE),(DRAW_X - HALF_BLOCKSIZE),THREE_X,MID_X};
							int[] y6MR = {(END_Y + QUARTER_BLOCKSIZE),(END_Y + HALF_BLOCKSIZE),QUARTER_Y,DRAW_Y};
							dirt_light.add(new Polygon(x6MR,y6MR,4));
						}
					} else if (DOWNLEFT == Sprite.ID_GRASS_MM){ //dirt
						int[] x6MR = {(DRAW_X - HALF_BLOCKSIZE),(DRAW_X - QUARTER_BLOCKSIZE),THREE_X,MID_X};
						int[] y6MR = {END_Y,(END_Y + QUARTER_BLOCKSIZE),QUARTER_Y,DRAW_Y};
						dirt_light.add(new Polygon(x6MR,y6MR,4));
					} else { //rhombus into left only
						int[] x6MR = {(DRAW_X - HALF_BLOCKSIZE),DRAW_X,THREE_X,MID_X};
						int[] y6MR = {END_Y,END_Y,QUARTER_Y,DRAW_Y};
						dirt_light.add(new Polygon(x6MR,y6MR,4));
					}
				} else { //rhombus into none
					int[] x6MR = {DRAW_X,DRAW_X,THREE_X,MID_X};
					int[] y6MR = {MID_Y,END_Y,QUARTER_Y,DRAW_Y};
					dirt_light.add(new Polygon(x6MR,y6MR,4));
				}
				break;
			case Sprite.ID_GRASS_ML: //Middle Left Grass
				//Can move into up, upright, right, and down
				if (UP == Sprite.ID_GRASS_TL || UP == Sprite.ID_GRASS_ML) into_up = true;
				if (RIGHT == Sprite.ID_GRASS_MR) into_right = true;
				if (DOWN == Sprite.ID_GRASS_BL || DOWN == Sprite.ID_GRASS_ML) into_down = true;
				if (UPRIGHT == Sprite.ID_GRASS_TM || UPRIGHT == Sprite.ID_GRASS_MR || UPRIGHT == Sprite.ID_GRASS_TR) into_upright = true;
				////Shapes that happen regardless of adjacency
				//Middle Dark Grass
				int[] x1ML = {DRAW_X,DRAW_X,MID_X,QUARTER_X};
				int[] y1ML = {MID_Y,END_Y,MID_Y,QUARTER_Y};
				grass_dark.add(new Polygon(x1ML,y1ML,4));
				//Top Light Grass
				if (into_up){ //rhombus into up
					int[] x2ML = {DRAW_X,DRAW_X,MID_X,QUARTER_X};
					int[] y2ML = {DRAW_Y,MID_Y,DRAW_Y,(DRAW_Y - QUARTER_BLOCKSIZE)};
					grass_light.add(new Polygon(x2ML,y2ML,4));
				} else { //triangle into none
					int[] x2ML = {DRAW_X,DRAW_X,MID_X};
					int[] y2ML = {DRAW_Y,MID_Y,DRAW_Y};
					grass_light.add(new Polygon(x2ML,y2ML,3));
				}
				//Bottom Light Grass
				if (!into_down){ //rhombus into down
					int[] x3ML = {DRAW_X,QUARTER_X,MID_X,DRAW_X};
					int[] y3ML = {END_Y,THREE_Y,END_Y,(END_Y + HALF_BLOCKSIZE)};
					grass_light.add(new Polygon(x3ML,y3ML,4));
				} else { //triangle into none
					int[] x3ML = {DRAW_X,QUARTER_X,MID_X};
					int[] y3ML = {END_Y,THREE_Y,END_Y};
					grass_light.add(new Polygon(x3ML,y3ML,3));
				}
				//Top Light Dirt
				if (into_up){
					if (into_upright){ //rhombus into up and upright
						if (UPRIGHT == Sprite.ID_GRASS_MR){ //deeper
							int[] x4ML = {QUARTER_X,MID_X,(END_X + THREE_BLOCKSIZE),(END_X + HALF_BLOCKSIZE)};
							int[] y4ML = {QUARTER_Y,MID_Y,(DRAW_Y - THREE_BLOCKSIZE),(DRAW_Y - BLOCKSIZE)};
							dirt_light.add(new Polygon(x4ML,y4ML,4));
						} else { //not as deep
							int[] x4ML = {QUARTER_X,MID_X,(END_X + HALF_BLOCKSIZE),(END_X + QUARTER_BLOCKSIZE)};
							int[] y4ML = {QUARTER_Y,MID_Y,(DRAW_Y - HALF_BLOCKSIZE),(DRAW_Y - THREE_BLOCKSIZE)};
							dirt_light.add(new Polygon(x4ML,y4ML,4));
						}
					} else if (UPRIGHT == Sprite.ID_GRASS_MM){ //into up and dirt-upright
						int[] x4ML = {QUARTER_X,MID_X,(END_X + QUARTER_BLOCKSIZE),END_X};
						int[] y4ML = {QUARTER_Y,MID_Y,(DRAW_Y - QUARTER_BLOCKSIZE),(DRAW_Y - HALF_BLOCKSIZE)};
						dirt_light.add(new Polygon(x4ML,y4ML,4));
					} else { //rhombus into up only
						int[] x4ML = {QUARTER_X,MID_X,END_X,END_X};
						int[] y4ML = {QUARTER_Y,MID_Y,DRAW_Y,(DRAW_Y - HALF_BLOCKSIZE)};
						dirt_light.add(new Polygon(x4ML,y4ML,4));
					}
				} else { //rhombus into none
					int[] x4ML = {QUARTER_X,MID_X,END_X,MID_X};
					int[] y4ML = {QUARTER_Y,MID_Y,DRAW_Y,DRAW_Y};
					dirt_light.add(new Polygon(x4ML,y4ML,4));
				}
				//Bottom Light Dirt
				if (into_down){
					if (RIGHT == Sprite.ID_GRASS_MM){ //into right dirt and down
						int[] x5ML = {QUARTER_X,MID_X,(END_X + QUARTER_BLOCKSIZE),END_X};
						int[] y5ML = {(END_Y + QUARTER_BLOCKSIZE),(END_Y + HALF_BLOCKSIZE),THREE_Y,MID_Y};
						dirt_light.add(new Polygon(x5ML,y5ML,4));
					} else if (into_right) { //rhombus into down and right
						int[] x5ML = {QUARTER_X,MID_X,(END_X + THREE_BLOCKSIZE),(END_X + HALF_BLOCKSIZE)};
						int[] y5ML = {(END_Y + QUARTER_BLOCKSIZE),(END_Y + HALF_BLOCKSIZE),QUARTER_Y,DRAW_Y};
						dirt_light.add(new Polygon(x5ML,y5ML,4));
					} else { //rhombus into down only
						int[] x5ML = {QUARTER_X,MID_X,END_X,END_X};
						int[] y5ML = {(END_Y + QUARTER_BLOCKSIZE),(END_Y + HALF_BLOCKSIZE),END_Y,MID_Y};
						dirt_light.add(new Polygon(x5ML,y5ML,4));
					}
				} else {
					if (RIGHT == Sprite.ID_GRASS_MM){ //into right dirt only
						int[] x5ML = {MID_X,END_X,(END_X + QUARTER_BLOCKSIZE),END_X};
						int[] y5ML = {END_Y,END_Y,THREE_Y,MID_Y};
						dirt_light.add(new Polygon(x5ML,y5ML,4));
					} else if (into_right) { //rhombus into right only
						int[] x5ML = {MID_X,END_X,(END_X + THREE_BLOCKSIZE),(END_X + HALF_BLOCKSIZE)};
						int[] y5ML = {END_Y,END_Y,QUARTER_Y,DRAW_Y};
						dirt_light.add(new Polygon(x5ML,y5ML,4));
					} else { //triangle into none
						int[] x5ML = {MID_X,END_X,END_X};
						int[] y5ML = {END_Y,END_Y,MID_Y};
						dirt_light.add(new Polygon(x5ML,y5ML,3));
					}
				}
				//Middle Dark Dirt
				if (into_right || RIGHT == Sprite.ID_GRASS_MM){
					if (into_upright){ //rhombus into right and upright
						if (UPRIGHT == Sprite.ID_GRASS_TM){ //deeper
							int[] x6ML = {QUARTER_X,MID_X,(END_X + BLOCKSIZE),(END_X + THREE_BLOCKSIZE)};
							int[] y6ML = {THREE_Y,END_Y,(DRAW_Y - HALF_BLOCKSIZE),(DRAW_Y - THREE_BLOCKSIZE)};
							dirt_dark.add(new Polygon(x6ML,y6ML,4));
						} else { //not as deep
							int[] x6ML = {QUARTER_X,MID_X,(END_X + THREE_BLOCKSIZE),(END_X + HALF_BLOCKSIZE)};
							int[] y6ML = {THREE_Y,END_Y,(DRAW_Y - QUARTER_BLOCKSIZE),(DRAW_Y - HALF_BLOCKSIZE)};
							dirt_dark.add(new Polygon(x6ML,y6ML,4));
						}
					} else { //rhombus into right only
						if (UPRIGHT == Sprite.ID_GRASS_MM){ //deeper for dirt
							int[] x6ML = {QUARTER_X,MID_X,(END_X + HALF_BLOCKSIZE),(END_X + QUARTER_BLOCKSIZE)};
							int[] y6ML = {THREE_Y,END_Y,DRAW_Y,(DRAW_Y - QUARTER_BLOCKSIZE)};
							dirt_dark.add(new Polygon(x6ML,y6ML,4));
						} else { //not as deep
							int[] x6ML = {QUARTER_X,MID_X,(END_X + HALF_BLOCKSIZE),END_X};
							int[] y6ML = {THREE_Y,END_Y,DRAW_Y,DRAW_Y};
							dirt_dark.add(new Polygon(x6ML,y6ML,4));
						}
					}
				} else { //rhombus into none
					int[] x6ML = {QUARTER_X,MID_X,END_X,END_X};
					int[] y6ML = {THREE_Y,END_Y,MID_Y,DRAW_Y};
					dirt_dark.add(new Polygon(x6ML,y6ML,4));
				}
				break;
			case Sprite.ID_GRASS_BM: //Bottom Middle Grass
				//Can move into left, up, upright, and right
				if (LEFT == Sprite.ID_GRASS_BL || LEFT == Sprite.ID_GRASS_BM) into_left = true;
				if (RIGHT == Sprite.ID_GRASS_BR || RIGHT == Sprite.ID_GRASS_BM) into_right = true;
				if (UP == Sprite.ID_GRASS_TM) into_up = true;
				if (UPRIGHT == Sprite.ID_GRASS_TM || UPRIGHT == Sprite.ID_GRASS_TR || UPRIGHT == Sprite.ID_GRASS_MR) into_upright = true;
				////Shapes that happen regardless of adjacency
				//Middle Light Grass
				int[] x1BM = {DRAW_X,MID_X,THREE_X,MID_X};
				int[] y1BM = {END_Y,END_Y,THREE_Y,MID_Y};
				grass_light.add(new Polygon(x1BM,y1BM,4));
				//Left Dark Grass
				if (into_left){ //rhombus into left
					int[] x2BM = {DRAW_X,QUARTER_X,DRAW_X,(DRAW_X - HALF_BLOCKSIZE)};
					int[] y2BM = {END_Y,THREE_Y,MID_Y,END_Y};
					grass_dark.add(new Polygon(x2BM,y2BM,4));
				} else { //triangle into none
					int[] x2BM = {DRAW_X,QUARTER_X,DRAW_X};
					int[] y2BM = {END_Y,THREE_Y,MID_Y};
					grass_dark.add(new Polygon(x2BM,y2BM,3));
				}
				//Right Dark Grass
				if (into_right){ //rhombus into right
					int[] x3BM = {END_X,MID_X,END_X,(END_X + QUARTER_BLOCKSIZE)};
					int[] y3BM = {MID_Y,END_Y,END_Y,THREE_Y};
					grass_dark.add(new Polygon(x3BM,y3BM,4));
				} else { //triangle into none
					int[] x3BM = {END_X,MID_X,END_X};
					int[] y3BM = {MID_Y,END_Y,END_Y};
					grass_dark.add(new Polygon(x3BM,y3BM,3));
				}
				//Left Dark Dirt
				if (into_left){
					if (into_up){ //rhombus into left and up
						int[] x4BM = {(DRAW_X - HALF_BLOCKSIZE),(DRAW_X - QUARTER_BLOCKSIZE),END_X, THREE_X};
						int[] y4BM = {MID_Y,THREE_Y,(DRAW_Y - HALF_BLOCKSIZE),(DRAW_Y - THREE_BLOCKSIZE)};
						dirt_dark.add(new Polygon(x4BM,y4BM,4));
					} else if (UP == Sprite.ID_GRASS_MM){ //into dirty-up and left
						int[] x4BM = {(DRAW_X - HALF_BLOCKSIZE),(DRAW_X - QUARTER_BLOCKSIZE),MID_X, QUARTER_X};
						int[] y4BM = {MID_Y,THREE_Y,DRAW_Y,(DRAW_Y - QUARTER_BLOCKSIZE)};
						dirt_dark.add(new Polygon(x4BM,y4BM,4));
					} else { //rhombus into left only
						int[] x4BM = {(DRAW_X - HALF_BLOCKSIZE),(DRAW_X - QUARTER_BLOCKSIZE),MID_X, DRAW_X};
						int[] y4BM = {MID_Y,THREE_Y,DRAW_Y,DRAW_Y};
						dirt_dark.add(new Polygon(x4BM,y4BM,4));
					}
				} else {
					if (into_up){ //rhombus into up only
						int[] x4BM = {DRAW_X,DRAW_X,END_X, THREE_X};
						int[] y4BM = {DRAW_Y,MID_Y,(DRAW_Y - HALF_BLOCKSIZE),(DRAW_Y - THREE_BLOCKSIZE)};
						dirt_dark.add(new Polygon(x4BM,y4BM,4));
					} else if (UP == Sprite.ID_GRASS_MM){ //into dirty-up only
						int[] x4BM = {DRAW_X,DRAW_X,MID_X,QUARTER_X};
						int[] y4BM = {DRAW_Y,MID_Y,DRAW_Y,(DRAW_Y - QUARTER_BLOCKSIZE)};
						dirt_dark.add(new Polygon(x4BM,y4BM,4));
					} else { //triangle into none
						int[] x4BM = {DRAW_X,DRAW_X,MID_X};
						int[] y4BM = {DRAW_Y,MID_Y,DRAW_Y};
						dirt_dark.add(new Polygon(x4BM,y4BM,3));
					}
				}
				//Middle Light Dirt
				if (into_up || UP == Sprite.ID_GRASS_MM){
					if (into_upright){ //rhombus into up and upright
						if (UPRIGHT == Sprite.ID_GRASS_MR){ //deeper
							int[] x5BM = {DRAW_X,QUARTER_X,(END_X + THREE_BLOCKSIZE),(END_X + HALF_BLOCKSIZE)};
							int[] y5BM = {MID_Y,THREE_Y,(DRAW_Y - THREE_BLOCKSIZE),(DRAW_Y - BLOCKSIZE)};
							dirt_light.add(new Polygon(x5BM,y5BM,4));
						} else { //not as deep
							int[] x5BM = {DRAW_X,QUARTER_X,(END_X + HALF_BLOCKSIZE),(END_X + QUARTER_BLOCKSIZE)};
							int[] y5BM = {MID_Y,THREE_Y,(DRAW_Y - HALF_BLOCKSIZE),(DRAW_Y - THREE_BLOCKSIZE)};
							dirt_light.add(new Polygon(x5BM,y5BM,4));
						}
					} else if (UPRIGHT == Sprite.ID_GRASS_MM){ //rhombus into up and dirt-upright
						int[] x5BM = {DRAW_X,QUARTER_X,(END_X + QUARTER_BLOCKSIZE),END_X};
						int[] y5BM = {MID_Y,THREE_Y,(DRAW_Y - QUARTER_BLOCKSIZE),(DRAW_Y - HALF_BLOCKSIZE)};
						dirt_light.add(new Polygon(x5BM,y5BM,4));
					} else { //rhombus into up only
						int[] x5BM = {DRAW_X,QUARTER_X,END_X,END_X};
						int[] y5BM = {MID_Y,THREE_Y,DRAW_Y,(DRAW_Y - HALF_BLOCKSIZE)};
						dirt_light.add(new Polygon(x5BM,y5BM,4));
					}
				} else { //rhombus into none
					int[] x5BM = {DRAW_X,QUARTER_X,END_X,MID_X};
					int[] y5BM = {MID_Y,THREE_Y,DRAW_Y,DRAW_Y};
					dirt_light.add(new Polygon(x5BM,y5BM,4));
				}
				//Right Dark Dirt
				if (into_right){
					if (into_upright){ //rhombus into right and upright
						if (UPRIGHT == Sprite.ID_GRASS_TM){ //deeper
							int[] x6BM = {MID_X,THREE_X,(END_X + BLOCKSIZE),(END_X + THREE_BLOCKSIZE)};
							int[] y6BM = {MID_Y,THREE_Y,(DRAW_Y - HALF_BLOCKSIZE),(DRAW_Y - THREE_BLOCKSIZE)};
							dirt_dark.add(new Polygon(x6BM,y6BM,4));
						} else { //not as deep
							int[] x6BM = {MID_X,THREE_X,(END_X + THREE_BLOCKSIZE),(END_X + HALF_BLOCKSIZE)};
							int[] y6BM = {MID_Y,THREE_Y,(DRAW_Y - QUARTER_BLOCKSIZE),(DRAW_Y - HALF_BLOCKSIZE)};
							dirt_dark.add(new Polygon(x6BM,y6BM,4));
						}
					} else { //rhombus into right only
						int[] x6BM = {MID_X,THREE_X,(END_X + HALF_BLOCKSIZE),END_X};
						int[] y6BM = {MID_Y,THREE_Y,DRAW_Y,DRAW_Y};
						dirt_dark.add(new Polygon(x6BM,y6BM,4));
					}
				} else { //rhombus into none
					int[] x6BM = {MID_X,THREE_X,END_X,END_X};
					int[] y6BM = {MID_Y,THREE_Y,MID_Y,DRAW_Y};
					dirt_dark.add(new Polygon(x6BM,y6BM,4));
				}
				break;
			case Sprite.ID_GRASS_TM: //Top Middle Grass
				//Can move into left, downleft, right, and down
				if (LEFT == Sprite.ID_GRASS_TL || LEFT == Sprite.ID_GRASS_TM) into_left = true;
				if (RIGHT == Sprite.ID_GRASS_TR || RIGHT == Sprite.ID_GRASS_TM) into_right = true;
				if (DOWN == Sprite.ID_GRASS_BM) into_down = true;
				if (DOWNLEFT == Sprite.ID_GRASS_ML || DOWNLEFT == Sprite.ID_GRASS_BL || DOWNLEFT == Sprite.ID_GRASS_BM) into_downleft = true;
				////Shapes that happen regardless of adjacency:
				//Middle Dark Grass
				int[] x1TM = {QUARTER_X,MID_X,END_X,MID_X};
				int[] y1TM = {QUARTER_Y,MID_Y,DRAW_Y,DRAW_Y};
				grass_dark.add(new Polygon(x1TM,y1TM,4));
				//Left Light Grass
				if (into_left){ //rhombus into left
					int[] x2TM = {(DRAW_X - QUARTER_BLOCKSIZE),DRAW_X,MID_X,DRAW_X};
					int[] y2TM = {QUARTER_Y,MID_Y,DRAW_Y,DRAW_Y};
					grass_light.add(new Polygon(x2TM,y2TM,4));
				} else { //triangle into none
					int[] x2TM = {DRAW_X,MID_X,DRAW_X};
					int[] y2TM = {MID_Y,DRAW_Y,DRAW_Y};
					grass_light.add(new Polygon(x2TM,y2TM,3));
				}
				//Right Light Grass
				if (into_right){ //rhombus into right
					int[] x3TM = {THREE_X,END_X,(END_X + HALF_BLOCKSIZE),END_X};
					int[] y3TM = {QUARTER_Y,MID_Y,DRAW_Y,DRAW_Y};
					grass_light.add(new Polygon(x3TM,y3TM,4));
				} else { //triangle into none
					int[] x3TM = {THREE_X,END_X,END_X};
					int[] y3TM = {QUARTER_Y,MID_Y,DRAW_Y};
					grass_light.add(new Polygon(x3TM,y3TM,3));
				}
				//Right Light Dirt
				if (DOWN == Sprite.ID_GRASS_MM){ //Down is dirt
					if (into_right) { //rhombus into right and down
						int[] x4TM = {MID_X,THREE_X,(END_X + HALF_BLOCKSIZE),(END_X + QUARTER_BLOCKSIZE)};
						int[] y4TM = {END_Y,(END_Y + QUARTER_BLOCKSIZE),MID_Y,QUARTER_Y};
						dirt_light.add(new Polygon(x4TM,y4TM,4));
					} else { //rhombus into down only
						int[] x4TM = {MID_X,THREE_X,END_X,END_X};
						int[] y4TM = {END_Y,(END_Y + QUARTER_BLOCKSIZE),END_Y,MID_Y};
						dirt_light.add(new Polygon(x4TM,y4TM,4));
					}
				} else {
					if (into_down){
						if (into_right){ //rhombus into right and down
							int[] x4TM = {DRAW_X,QUARTER_X,(END_X + HALF_BLOCKSIZE),(END_X + QUARTER_BLOCKSIZE)};
							int[] y4TM = {(END_Y + HALF_BLOCKSIZE),(END_Y + THREE_BLOCKSIZE),MID_Y,QUARTER_Y};
							dirt_light.add(new Polygon(x4TM,y4TM,4));
						} else { //rhombus into down only
							int[] x4TM = {DRAW_X,QUARTER_X,END_X,END_X};
							int[] y4TM = {(END_Y + HALF_BLOCKSIZE),(END_Y + THREE_BLOCKSIZE),END_Y,MID_Y};
							dirt_light.add(new Polygon(x4TM,y4TM,4));
						}
					} else { //triangle into none
						if (into_right) { //rhombus into right
							int[] x4TM = {MID_X,END_X,(END_X + HALF_BLOCKSIZE),(END_X + QUARTER_BLOCKSIZE)};
							int[] y4TM = {END_Y,END_Y,MID_Y,QUARTER_Y};
							dirt_light.add(new Polygon(x4TM,y4TM,4));
						} else { //triangle into none
							int[] x4TM = {MID_X,END_X,END_X};
							int[] y4TM = {END_Y,END_Y,MID_Y};
							dirt_light.add(new Polygon(x4TM,y4TM,3));
						}
					}
				}
				//Middle Dark Dirt
				if (into_down || DOWN == Sprite.ID_GRASS_MM){ //Into Down or Down is dirt
					if (into_downleft){ //rhombus into down and downleft
						if (DOWNLEFT == Sprite.ID_GRASS_ML){ //deeper
							int[] x5TM = {(DRAW_X - THREE_BLOCKSIZE),(DRAW_X - HALF_BLOCKSIZE),END_X,THREE_X};
							int[] y5TM = {(END_Y + THREE_BLOCKSIZE),(END_Y + BLOCKSIZE),MID_Y,QUARTER_Y};
							dirt_dark.add(new Polygon(x5TM,y5TM,4));
						} else { //not as deep
							int[] x5TM = {(DRAW_X - HALF_BLOCKSIZE),(DRAW_X - QUARTER_BLOCKSIZE),END_X,THREE_X};
							int[] y5TM = {(END_Y + HALF_BLOCKSIZE),(END_Y + THREE_BLOCKSIZE),MID_Y,QUARTER_Y};
							dirt_dark.add(new Polygon(x5TM,y5TM,4));
						}
					} else { //rhombus into down
						if (DOWNLEFT == Sprite.ID_GRASS_MM && DOWN == Sprite.ID_GRASS_MM){//dirty
							int[] x5TM = {(DRAW_X - QUARTER_BLOCKSIZE),DRAW_X,END_X,THREE_X};
							int[] y5TM = {(END_Y + QUARTER_BLOCKSIZE),(END_Y + HALF_BLOCKSIZE),MID_Y,QUARTER_Y};
							dirt_dark.add(new Polygon(x5TM,y5TM,4));
						} else {
							int[] x5TM = {DRAW_X,DRAW_X,END_X,THREE_X};
							int[] y5TM = {END_Y,(END_Y + HALF_BLOCKSIZE),MID_Y,QUARTER_Y};
							dirt_dark.add(new Polygon(x5TM,y5TM,4));
						}
					}
				} else { //rhombus into none
					int[] x5TM = {DRAW_X,MID_X,END_X,THREE_X};
					int[] y5TM = {END_Y,END_Y,MID_Y,QUARTER_Y};
					dirt_dark.add(new Polygon(x5TM,y5TM,4));
				}
				//Left Light Dirt
				if (into_left){
					if (DOWNLEFT == Sprite.ID_GRASS_MM){ //Downleft is dirt
						int[] x4TR = {(DRAW_X - HALF_BLOCKSIZE),(DRAW_X - QUARTER_BLOCKSIZE),MID_X,QUARTER_X};
						int[] y4TR = {END_Y,(END_Y + QUARTER_BLOCKSIZE),MID_Y,QUARTER_Y};
						dirt_light.add(new Polygon(x4TR,y4TR,4));
					} else { 
						if (into_downleft){ //rhombus into left and downleft
							if (DOWNLEFT == Sprite.ID_GRASS_ML || DOWNLEFT == Sprite.ID_GRASS_BL){ //not as deep
								int[] x6TM = {(DRAW_X - THREE_BLOCKSIZE),(DRAW_X - HALF_BLOCKSIZE),MID_X,QUARTER_X};
								int[] y6TM = {(END_Y + QUARTER_BLOCKSIZE),(END_Y + HALF_BLOCKSIZE),MID_Y,QUARTER_Y};
								dirt_light.add(new Polygon(x6TM,y6TM,4));
							} else { //can go deeper
								int[] x6TM = {(DRAW_X - BLOCKSIZE),(DRAW_X - THREE_BLOCKSIZE),MID_X,QUARTER_X};
								int[] y6TM = {(END_Y + HALF_BLOCKSIZE),(END_Y + THREE_BLOCKSIZE),MID_Y,QUARTER_Y};
								dirt_light.add(new Polygon(x6TM,y6TM,4));
							}
						} else { //rhombus into left
							int[] x6TM = {(DRAW_X - HALF_BLOCKSIZE),DRAW_X,MID_X,QUARTER_X};
							int[] y6TM = {END_Y,END_Y,MID_Y,QUARTER_Y};
							dirt_light.add(new Polygon(x6TM,y6TM,4));
						}
					}
				} else { //rhombus into none
					int[] x6TM = {DRAW_X,DRAW_X,MID_X,QUARTER_X};
					int[] y6TM = {MID_Y,END_Y,MID_Y,QUARTER_Y};
					dirt_light.add(new Polygon(x6TM,y6TM,4));
				}
				break;
			case Sprite.ID_GRASS_TR:
				//Can move into left, downleft, and down
				if (LEFT == Sprite.ID_GRASS_TM || LEFT == Sprite.ID_GRASS_TL) into_left = true;
				if (DOWN == Sprite.ID_GRASS_MR || DOWN == Sprite.ID_GRASS_BR) into_down = true;
				if (DOWNLEFT == Sprite.ID_GRASS_BM || DOWNLEFT == Sprite.ID_GRASS_BL || DOWNLEFT == Sprite.ID_GRASS_ML) into_downleft = true;
				////Shapes that happen regardless of adjacency:
				//Topright Dark Grass
				int[] x0TR = {QUARTER_X,MID_X,END_X,MID_X};
				int[] y0TR = {QUARTER_Y,MID_Y,DRAW_Y,DRAW_Y};
				grass_dark.add(new Polygon(x0TR,y0TR,4));
				//Topright Light Grass
				int[] x1TR = {MID_X,THREE_X,END_X,END_X};
				int[] y1TR = {MID_Y,THREE_Y,MID_Y,DRAW_Y};
				grass_light.add(new Polygon(x1TR,y1TR,4));
				//Left Light Grass
				if (into_left){ //rhombus into left
					int[] x2TR = {DRAW_X,MID_X,DRAW_X,(DRAW_X - QUARTER_BLOCKSIZE)};
					int[] y2TR = {MID_Y,DRAW_Y,DRAW_Y,QUARTER_Y};
					grass_light.add(new Polygon(x2TR,y2TR,4));
				} else { //triangle into none
					int[] x2TR = {DRAW_X,MID_X,DRAW_X};
					int[] y2TR = {MID_Y,DRAW_Y,DRAW_Y};
					grass_light.add(new Polygon(x2TR,y2TR,3));
				}
				//Right Dark Grass
				if (into_down){ //rhombus into down
					int[] x3TR = {MID_X,END_X,END_X,THREE_X};
					int[] y3TR = {END_Y,MID_Y,END_Y,(END_Y + QUARTER_BLOCKSIZE)};
					grass_dark.add(new Polygon(x3TR,y3TR,4));
				} else { //triangle into none
					int[] x3TR = {MID_X,END_X,END_X};
					int[] y3TR = {END_Y,MID_Y,END_Y};
					grass_dark.add(new Polygon(x3TR,y3TR,3));
				}
				//Left Light Dirt
				if (into_left){
					if (DOWNLEFT == Sprite.ID_GRASS_MM){ //Downleft is dirt
						int[] x4TR = {(DRAW_X - HALF_BLOCKSIZE),(DRAW_X - QUARTER_BLOCKSIZE),MID_X,QUARTER_X};
						int[] y4TR = {END_Y,(END_Y + QUARTER_BLOCKSIZE),MID_Y,QUARTER_Y};
						dirt_light.add(new Polygon(x4TR,y4TR,4));
					} else { 
						if (into_downleft){ //rhombus into left and downleft
							if (DOWNLEFT == Sprite.ID_GRASS_BM){ //deeper
								int[] x4TR = {(DRAW_X - BLOCKSIZE),(DRAW_X - THREE_BLOCKSIZE),MID_X,QUARTER_X};
								int[] y4TR = {(END_Y + HALF_BLOCKSIZE),(END_Y + THREE_BLOCKSIZE),MID_Y,QUARTER_Y};
								dirt_light.add(new Polygon(x4TR,y4TR,4));
							} else { //not as deep
								int[] x4TR = {(DRAW_X - THREE_BLOCKSIZE),(DRAW_X - HALF_BLOCKSIZE),MID_X,QUARTER_X};
								int[] y4TR = {(END_Y + QUARTER_BLOCKSIZE),(END_Y + HALF_BLOCKSIZE),MID_Y,QUARTER_Y};
								dirt_light.add(new Polygon(x4TR,y4TR,4));
							}
						} else { //rhombus into left only
							int[] x4TR = {(DRAW_X - HALF_BLOCKSIZE),DRAW_X,MID_X,QUARTER_X};
							int[] y4TR = {END_Y,END_Y,MID_Y,QUARTER_Y};
							dirt_light.add(new Polygon(x4TR,y4TR,4));
						}
					}
				} else { //rhombus into none
					int[] x4TR = {DRAW_X,DRAW_X,MID_X,QUARTER_X};
					int[] y4TR = {MID_Y,END_Y,MID_Y,QUARTER_Y};
					dirt_light.add(new Polygon(x4TR,y4TR,4));
				}
				//Right Dark Dirt
				if (into_down){
					if (DOWNLEFT == Sprite.ID_GRASS_MM){ //Downleft is dirt
						int[] x5TR = {(DRAW_X - QUARTER_BLOCKSIZE),DRAW_X,THREE_X,MID_X};
						int[] y5TR = {(END_Y + QUARTER_BLOCKSIZE),(END_Y + HALF_BLOCKSIZE),THREE_Y,MID_Y};
						dirt_dark.add(new Polygon(x5TR,y5TR,4));
					} else {
						if (into_downleft){ //rhombus into down and downleft
							if (DOWNLEFT == Sprite.ID_GRASS_ML){ //deeper
								int[] x5TR = {(DRAW_X - THREE_BLOCKSIZE),(DRAW_X - HALF_BLOCKSIZE),THREE_X,MID_X};
								int[] y5TR = {(END_Y + THREE_BLOCKSIZE),(END_Y + BLOCKSIZE),THREE_Y,MID_Y};
								dirt_dark.add(new Polygon(x5TR,y5TR,4));
							} else { //not as deep
								int[] x5TR = {(DRAW_X - HALF_BLOCKSIZE),(DRAW_X - QUARTER_BLOCKSIZE),THREE_X,MID_X};
								int[] y5TR = {(END_Y + HALF_BLOCKSIZE),(END_Y + THREE_BLOCKSIZE),THREE_Y,MID_Y};
								dirt_dark.add(new Polygon(x5TR,y5TR,4));
							}
						} else { //rhombus into down only
							int[] x5TR = {DRAW_X,DRAW_X,THREE_X,MID_X};
							int[] y5TR = {END_Y,(END_Y + HALF_BLOCKSIZE),THREE_Y,MID_Y};
							dirt_dark.add(new Polygon(x5TR,y5TR,4));
						}
					}
				} else { //rhombus into none
					int[] x5TR = {DRAW_X,MID_X,THREE_X,MID_X};
					int[] y5TR = {END_Y,END_Y,THREE_Y,MID_Y};
					dirt_dark.add(new Polygon(x5TR,y5TR,4));
				}
				break;
			case Sprite.ID_GRASS_TL:
				//Can move into down and right
				if (RIGHT == Sprite.ID_GRASS_TM || RIGHT == Sprite.ID_GRASS_TR) into_right = true;
				if (DOWN == Sprite.ID_GRASS_ML || DOWN == Sprite.ID_GRASS_BL) into_down = true;
				////Shapes that happen regardless of adjacency:
				//Top Left Light Grass
				int[] x0TL = {DRAW_X,MID_X,DRAW_X};
				int[] y0TL = {MID_Y,DRAW_Y,DRAW_Y};
				grass_light.add(new Polygon(x0TL,y0TL,3));
				//Top Left Dark Grass
				int[] x1TL = {DRAW_X,DRAW_X,MID_X,END_X};
				int[] y1TL = {END_Y,MID_Y,DRAW_Y,DRAW_Y};
				grass_dark.add(new Polygon(x1TL,y1TL,4));
				//Middle Dark Dirt
				int[] x2TL = {QUARTER_X,MID_X,END_X,THREE_X};
				int[] y2TL = {THREE_Y,END_Y,MID_Y,QUARTER_Y};
				dirt_dark.add(new Polygon(x2TL,y2TL,4));
				//Right Light Grass
				if (into_right){ //Right rhombus into right
					int[] x3TL = {THREE_X,END_X,(END_X + HALF_BLOCKSIZE),END_X};
					int[] y3TL = {QUARTER_Y,MID_Y,DRAW_Y,DRAW_Y};
					grass_light.add(new Polygon(x3TL,y3TL,4));
				} else { //Right triangle into none
					int[] x3TL = {THREE_X,END_X,END_X};
					int[] y3TL = {QUARTER_Y,MID_Y,DRAW_Y};
					grass_light.add(new Polygon(x3TL,y3TL,3));
				}
				//Left Light Grass
				if (into_down){
					if (DOWN == Sprite.ID_GRASS_BL){ //Left diamond into down
						int[] x4TL = {DRAW_X,QUARTER_X,MID_X,QUARTER_X};
						int[] y4TL = {END_Y,THREE_Y,END_Y,(END_Y + QUARTER_BLOCKSIZE)};
						grass_light.add(new Polygon(x4TL,y4TL,4));
					} else { //Left rhombus into down
						int[] x4TL = {DRAW_X,QUARTER_X,MID_X,DRAW_X};
						int[] y4TL = {END_Y,THREE_Y,END_Y,(END_Y + HALF_BLOCKSIZE)};
						grass_light.add(new Polygon(x4TL,y4TL,4));
					}
				} else { //Left triangle into none
					int[] x4TL = {DRAW_X,QUARTER_X,MID_X};
					int[] y4TL = {END_Y,THREE_Y,END_Y};
					grass_light.add(new Polygon(x4TL,y4TL,3));
				}
				//Bottom Light Dirt
				if (into_right){
					if (into_down){ //rhombus into right and down
						int[] x5TL = {QUARTER_X,MID_X,(END_X + HALF_BLOCKSIZE),(END_X + QUARTER_BLOCKSIZE)};
						int[] y5TL = {(END_Y + QUARTER_BLOCKSIZE),(END_Y + HALF_BLOCKSIZE),MID_Y,QUARTER_Y};
						dirt_light.add(new Polygon(x5TL,y5TL,4));
					} else { //rhombus into right only
						int[] x5TL = {MID_X,END_X,(END_X + HALF_BLOCKSIZE),(END_X + QUARTER_BLOCKSIZE)};
						int[] y5TL = {END_Y,END_Y,MID_Y,QUARTER_Y};
						dirt_light.add(new Polygon(x5TL,y5TL,4));
					}
				} else {
					if (into_down){ //rhombus into down only
						int[] x5TL = {QUARTER_X,MID_X,END_X,END_X};
						int[] y5TL = {(END_Y + QUARTER_BLOCKSIZE),(END_Y + HALF_BLOCKSIZE),END_Y,MID_Y};
						dirt_light.add(new Polygon(x5TL,y5TL,4));
					} else { //triangle into none
						int[] x5TL = {MID_X,END_X,END_X};
						int[] y5TL = {END_Y,END_Y,MID_Y};
						dirt_light.add(new Polygon(x5TL,y5TL,3));
					}
				}
				break;
			case Sprite.ID_GRASS_BR:
				//Can move left and up
				if (UP == Sprite.ID_GRASS_MR || UP == Sprite.ID_GRASS_TR) into_up = true;
				if (LEFT == Sprite.ID_GRASS_BM || LEFT == Sprite.ID_GRASS_BL) into_left = true;
				////Shapes that happen regardless of adjacency
				//Bottom Right Light Grass
				int[] x0BR = {DRAW_X,MID_X,END_X,END_X};
				int[] y0BR = {END_Y,END_Y,MID_Y,DRAW_Y};
				grass_light.add(new Polygon(x0BR,y0BR,4));
				//Bottom Right Dark Grass
				int[] x1BR = {MID_X,END_X,END_X};
				int[] y1BR = {END_Y,END_Y,MID_Y};
				grass_dark.add(new Polygon(x1BR,y1BR,3));
				//Bottom Light Dirt
				int[] x2BR = {DRAW_X,QUARTER_X,THREE_X,MID_X};
				int[] y2BR = {MID_Y,THREE_Y,QUARTER_Y,DRAW_Y};
				dirt_light.add(new Polygon(x2BR,y2BR,4));
				//Left Dark Grass
				if (into_left){ //rhombus into left
					int[] x3BR = {(DRAW_X - HALF_BLOCKSIZE),DRAW_X,QUARTER_X,DRAW_X};
					int[] y3BR = {END_Y,END_Y,THREE_Y,MID_Y};
					grass_dark.add(new Polygon(x3BR,y3BR,4));
				} else { //triangle into nothing
					int[] x3BR = {DRAW_X,QUARTER_X,DRAW_X};
					int[] y3BR = {END_Y,THREE_Y,MID_Y};
					grass_dark.add(new Polygon(x3BR,y3BR,3));
				}
				//Right Dark Grass
				if (into_up){ //rhombus into up
					int[] x4BR = {MID_X,THREE_X,END_X,END_X};
					int[] y4BR = {DRAW_Y,QUARTER_Y,DRAW_Y,(DRAW_Y - HALF_BLOCKSIZE)};
					grass_dark.add(new Polygon(x4BR,y4BR,4));
				} else { //triangle into nothing
					int[] x4BR = {MID_X,THREE_X,END_X};
					int[] y4BR = {DRAW_Y,QUARTER_Y,DRAW_Y};
					grass_dark.add(new Polygon(x4BR,y4BR,3));
				}
				//Top Dark Dirt
				if (into_up){
					if (into_left){ //rhombus into left and up
						int[] x5BR = {(DRAW_X - QUARTER_BLOCKSIZE),(DRAW_X - HALF_BLOCKSIZE),MID_X,THREE_X};
						int[] y5BR = {THREE_Y,MID_Y,(DRAW_Y - HALF_BLOCKSIZE),(DRAW_Y - QUARTER_BLOCKSIZE)};
						dirt_dark.add(new Polygon(x5BR,y5BR,4));
					} else { //rhombus into up only
						int[] x5BR = {DRAW_X,DRAW_X,MID_X,THREE_X};
						int[] y5BR = {MID_Y,DRAW_Y,(DRAW_Y - HALF_BLOCKSIZE),(DRAW_Y - QUARTER_BLOCKSIZE)};
						dirt_dark.add(new Polygon(x5BR,y5BR,4));
					}
				} else {
					if (into_left){ //rhombus into left only
						int[] x5BR = {(DRAW_X - QUARTER_BLOCKSIZE),(DRAW_X - HALF_BLOCKSIZE),DRAW_X,MID_X};
						int[] y5BR = {THREE_Y,MID_Y,DRAW_Y,DRAW_Y};
						skyPolygons.add(new Polygon(x5BR,y5BR,4));
					} else { //triangle into none
						int[] x5BR = {DRAW_X,DRAW_X,MID_X};
						int[] y5BR = {MID_Y,DRAW_Y,DRAW_Y};
						dirt_dark.add(new Polygon(x5BR,y5BR,3));
					}
				}
				break;
			case Sprite.ID_GRASS_BL:
				//Can move into up, upright, and right
				if (UP == Sprite.ID_GRASS_ML || UP == Sprite.ID_GRASS_TL) into_up = true;
				if (RIGHT == Sprite.ID_GRASS_BM || RIGHT == Sprite.ID_GRASS_BR) into_right = true;
				if (UPRIGHT == Sprite.ID_GRASS_TM || UPRIGHT == Sprite.ID_GRASS_TR || UPRIGHT == Sprite.ID_GRASS_MR) into_upright = true;
				////Shapes that happen regardless of adjacency:
				//Bottom Left Light Grass
				int[] x0BL = {DRAW_X,MID_X,THREE_X,MID_X};
				int[] y0BL = {END_Y,MID_Y,THREE_Y,END_Y};
				grass_light.add(new Polygon(x0BL,y0BL,4));
				//Bottom Left Dark Grass
				int[] x1BL = {QUARTER_X,DRAW_X,DRAW_X,MID_X};
				int[] y1BL = {QUARTER_Y,MID_Y,END_Y,MID_Y};
				grass_dark.add(new Polygon(x1BL,y1BL,4));
				//Top Light Grass
				if (into_up){ //diamond into up
					int[] x2BL = {DRAW_X,DRAW_X,MID_X,QUARTER_X};
					int[] y2BL = {DRAW_Y,MID_Y,DRAW_Y,(DRAW_Y - QUARTER_BLOCKSIZE)};
					grass_light.add(new Polygon(x2BL,y2BL,4));
				} else { //triangle into none
					int[] x2BL = {DRAW_X,DRAW_X,MID_X};
					int[] y2BL = {DRAW_Y,MID_Y,DRAW_Y};
					grass_light.add(new Polygon(x2BL,y2BL,3));
				}
				//Bottom Dark Grass
				if (into_right){ //diamond into right
					int[] x3BL = {END_X,MID_X,END_X,(END_X + QUARTER_BLOCKSIZE)};
					int[] y3BL = {MID_Y,END_Y,END_Y,THREE_Y};
					grass_dark.add(new Polygon(x3BL,y3BL,4));
				} else { //triangle into none
					int[] x3BL = {END_X,MID_X,END_X};
					int[] y3BL = {MID_Y,END_Y,END_Y};
					grass_dark.add(new Polygon(x3BL,y3BL,3));
				}
				//Top Light Dirt
				if (into_up){
					if (into_upright){ //rhombus into up and upright
						if (UPRIGHT == Sprite.ID_GRASS_MR){ //deeper
							int[] x4BL = {QUARTER_X,MID_X,(END_X + THREE_BLOCKSIZE),(END_X + HALF_BLOCKSIZE)};
							int[] y4BL = {QUARTER_Y,MID_Y,(DRAW_Y - THREE_BLOCKSIZE),(DRAW_Y - BLOCKSIZE)};
							dirt_light.add(new Polygon(x4BL,y4BL,4));
						} else { //not as deep
							int[] x4BL = {QUARTER_X,MID_X,(END_X + HALF_BLOCKSIZE),(END_X + QUARTER_BLOCKSIZE)};
							int[] y4BL = {QUARTER_Y,MID_Y,(DRAW_Y - HALF_BLOCKSIZE),(DRAW_Y - THREE_BLOCKSIZE)};
							dirt_light.add(new Polygon(x4BL,y4BL,4));
						}
					} else if (UPRIGHT == Sprite.ID_GRASS_MM){
						int[] x4BL = {QUARTER_X,MID_X,(END_X + QUARTER_BLOCKSIZE),END_X};
						int[] y4BL = {QUARTER_Y,MID_Y,(DRAW_Y - QUARTER_BLOCKSIZE),(DRAW_Y - HALF_BLOCKSIZE)};
						dirt_light.add(new Polygon(x4BL,y4BL,4));
					} else { //rhombus into up only
						int[] x4BL = {QUARTER_X,MID_X,END_X,END_X};
						int[] y4BL = {QUARTER_Y,MID_Y,DRAW_Y,(DRAW_Y - HALF_BLOCKSIZE)};
						dirt_light.add(new Polygon(x4BL,y4BL,4));
					}
				} else { //rhombus into nothing
					int[] x4BL = {QUARTER_X,MID_X,END_X,MID_X};
					int[] y4BL = {QUARTER_Y,MID_Y,DRAW_Y,DRAW_Y};
					dirt_light.add(new Polygon(x4BL,y4BL,4));
				}
				//Bottom Dark Dirt
				if (into_right){
					if (into_upright){ //rhombus into right and upright
						if (UPRIGHT == Sprite.ID_GRASS_TR || UPRIGHT == Sprite.ID_GRASS_MR){ //not as deep
							int[] x5BL = {MID_X,THREE_X,(END_X + THREE_BLOCKSIZE),(END_X + HALF_BLOCKSIZE)};
							int[] y5BL = {MID_Y,THREE_Y,(DRAW_Y - QUARTER_BLOCKSIZE),(DRAW_Y - HALF_BLOCKSIZE)};
							dirt_dark.add(new Polygon(x5BL,y5BL,4));
						} else { //deeper
							int[] x5BL = {MID_X,THREE_X,(END_X + BLOCKSIZE),(END_X + THREE_BLOCKSIZE)};
							int[] y5BL = {MID_Y,THREE_Y,(DRAW_Y - HALF_BLOCKSIZE),(DRAW_Y - THREE_BLOCKSIZE)};
							dirt_dark.add(new Polygon(x5BL,y5BL,4));
						}
						
					} else if (UPRIGHT == Sprite.ID_GRASS_MM){ //rhombus into right and dirt-upright
						int[] x5BL = {MID_X,THREE_X,(END_X + HALF_BLOCKSIZE),(END_X + QUARTER_BLOCKSIZE)};
						int[] y5BL = {MID_Y,THREE_Y,DRAW_Y,(DRAW_Y - QUARTER_BLOCKSIZE)};
						dirt_dark.add(new Polygon(x5BL,y5BL,4));
					} else { //rhombus into right only
						int[] x5BL = {MID_X,THREE_X,(END_X + HALF_BLOCKSIZE),END_X};
						int[] y5BL = {MID_Y,THREE_Y,DRAW_Y,DRAW_Y};
						dirt_dark.add(new Polygon(x5BL,y5BL,4));
					}
				} else { //rhombus into nothing
					int[] x5BL = {MID_X,THREE_X,END_X,END_X};
					int[] y5BL = {MID_Y,THREE_Y,MID_Y,DRAW_Y};
					dirt_dark.add(new Polygon(x5BL,y5BL,4));
				}
				break;
		}

		for (Polygon poly : grass_light) {
			drawShape(poly, Sprite.COLOR_GRASS_LIGHT, Sprite.COLOR_BORDER);
		}
		for (Polygon poly : grass_dark) {
			drawShape(poly, Sprite.COLOR_GRASS_DARK, Sprite.COLOR_BORDER);
		}
		for (Polygon poly : dirt_light) {
			drawShape(poly, Sprite.COLOR_DIRT_LIGHT, Sprite.COLOR_BORDER);
		}
		for (Polygon poly : dirt_dark) {
			drawShape(poly, Sprite.COLOR_DIRT_DARK, Sprite.COLOR_BORDER);
		}
	}
	
	/**
	 * Draw Mario at the appropriate X & Y on the canvas
	 * 
	 * @param X
	 *            The x coordinate to draw Mario at
	 * @param Y
	 *            The y coordinate to draw Mario at
	 */
	private void drawMario(int X, int Y) {
		int DRAW_X = X * BLOCKSIZE, DRAW_Y = Y * BLOCKSIZE;
		int MID_X = DRAW_X + HALF_BLOCKSIZE, MID_Y = DRAW_Y + HALF_BLOCKSIZE;
		int END_X = DRAW_X + BLOCKSIZE, END_Y = DRAW_Y + BLOCKSIZE;

		ArrayList <Polygon> redShapes = new ArrayList <Polygon>(), blueShapes = new ArrayList <Polygon>(),
				blackShapes = new ArrayList <Polygon>(), brownShapes = new ArrayList <Polygon>(),
				skinShapes = new ArrayList <Polygon>();

		// Left hat triangle
		int[] x_hatLeft = {DRAW_X, DRAW_X, MID_X};
		int[] y_hatLeft = { (DRAW_Y - HALF_BLOCKSIZE), (DRAW_Y - BLOCKSIZE),
				(DRAW_Y - THREE_BLOCKSIZE)};
		redShapes.add(new Polygon(x_hatLeft, y_hatLeft, 3));
		// Middle hat triangle
		int[] x_hatMid = {DRAW_X, MID_X, END_X};
		int[] y_hatMid = { (DRAW_Y - BLOCKSIZE), (DRAW_Y - THREE_BLOCKSIZE), (DRAW_Y - BLOCKSIZE)};
		redShapes.add(new Polygon(x_hatMid, y_hatMid, 3));
		// Right hat triangle
		int[] x_hatRight = {END_X, END_X, MID_X};
		int[] y_hatRight = { (DRAW_Y - HALF_BLOCKSIZE), (DRAW_Y - BLOCKSIZE),
				(DRAW_Y - THREE_BLOCKSIZE)};
		redShapes.add(new Polygon(x_hatRight, y_hatRight, 3));
		// Mario's hair
		int[] x_marioHair = {DRAW_X, MID_X, END_X};
		int[] y_marioHair = { (DRAW_Y - HALF_BLOCKSIZE), (DRAW_Y - THREE_BLOCKSIZE),
				(DRAW_Y - HALF_BLOCKSIZE)};
		blackShapes.add(new Polygon(x_marioHair, y_marioHair, 3));
		// Left face triangle
		int[] x_faceLeft = {DRAW_X, DRAW_X, MID_X};
		int[] y_faceLeft = { (DRAW_Y), (DRAW_Y - HALF_BLOCKSIZE), (DRAW_Y - QUARTER_BLOCKSIZE)};
		skinShapes.add(new Polygon(x_faceLeft, y_faceLeft, 3));
		// Middle face triangle
		int[] x_faceMid = {DRAW_X, MID_X, END_X};
		int[] y_faceMid = { (DRAW_Y - HALF_BLOCKSIZE), (DRAW_Y - QUARTER_BLOCKSIZE), (DRAW_Y - HALF_BLOCKSIZE)};
		skinShapes.add(new Polygon(x_faceMid, y_faceMid, 3));
		// Right face triangle
		int[] x_faceRight = {END_X, END_X, MID_X};
		int[] y_faceRight = { (DRAW_Y), (DRAW_Y - HALF_BLOCKSIZE), (DRAW_Y - QUARTER_BLOCKSIZE)};
		skinShapes.add(new Polygon(x_faceRight, y_faceRight, 3));
		// Mario's mustache
		int[] x_marioMustache = {DRAW_X, MID_X, END_X};
		int[] y_marioMustache = { (DRAW_Y), (DRAW_Y - QUARTER_BLOCKSIZE), (DRAW_Y)};
		blackShapes.add(new Polygon(x_marioMustache, y_marioMustache, 3));
		// Mario's shirt
		int[] x_shirt = {DRAW_X, END_X, END_X, DRAW_X};
		int[] y_shirt = {DRAW_Y, DRAW_Y, MID_Y, MID_Y};
		redShapes.add(new Polygon(x_shirt, y_shirt, 4));
		// Pants left triangle
		int[] x_pantsLeft = {DRAW_X, MID_X, DRAW_X};
		int[] y_pantsLeft = {MID_Y, MID_Y, END_Y};
		blueShapes.add(new Polygon(x_pantsLeft, y_pantsLeft, 3));
		// Shoes
		int[] x_shoes = {DRAW_X, MID_X, END_X};
		int[] y_shoes = {END_Y, MID_Y, END_Y};
		brownShapes.add(new Polygon(x_shoes, y_shoes, 3));
		// Pants right triangle
		int[] x_pantsRight = {END_X, MID_X, END_X};
		int[] y_pantsRight = {MID_Y, MID_Y, END_Y};
		blueShapes.add(new Polygon(x_pantsRight, y_pantsRight, 3));

		// Draw Mario
		for (Polygon shape : redShapes) {
			drawShape(shape, Sprite.COLOR_MARIO_RED, Sprite.COLOR_BORDER);
		}
		for (Polygon shape : blueShapes) {
			drawShape(shape, Sprite.COLOR_MARIO_BLUE, Sprite.COLOR_BORDER);
		}
		for (Polygon shape : blackShapes) {
			drawShape(shape, Sprite.COLOR_MARIO_HAIR, Sprite.COLOR_BORDER);
		}
		for (Polygon shape : brownShapes) {
			drawShape(shape, Sprite.COLOR_MARIO_SHOES, Sprite.COLOR_BORDER);
		}
		for (Polygon shape : skinShapes) {
			drawShape(shape, Sprite.COLOR_MARIO_SKIN, Sprite.COLOR_BORDER);
		}
	}

	/**
	 * Draw a question block at XY
	 * 
	 * @param X
	 *            The x coordinate to draw it at
	 * @param Y
	 *            The y coordinate to draw it at
	 * @param ADJ
	 *            The adjacent question blocks
	 * @param up_upright
	 *            Whether the block up & to the right has a question block above
	 *            it
	 * @throws IOException
	 */
	private void drawQuestionBlock(int X, int Y, int ADJ, boolean up_upright){
		drawCenteredDiamondBlock(X, Y, ADJ, up_upright, Sprite.COLOR_QUESTION_BG, Sprite.COLOR_QUESTION_DIAMOND, false);
	}
	
	/**
	 * Draw a diamond-centered block at XY
	 * 
	 * @param X
	 *            The x coordinate to draw it at
	 * @param Y
	 *            The y coordinate to draw it at
	 * @param ADJ
	 *            The adjacent blocks of the same ID
	 * @param up_upright
	 *            Whether the block up & to the right has the same block above
	 *            it
	 * @param color_background
	 *            The background color of the block
	 * @param color_diamond
	 *            The color of the diamond
	 * @param isCoin
	 *            Whether a coin is being drawn. If true, add to skyPolygons
	 *            instead
	 * @throws IOException
	 */
	private void drawCenteredDiamondBlock(int X, int Y, int ADJ, boolean up_upright, Color color_background,
			Color color_diamond, boolean isCoin){
		int DRAW_X = X * BLOCKSIZE, DRAW_Y = Y * BLOCKSIZE;
		int MID_X = DRAW_X + HALF_BLOCKSIZE, MID_Y = DRAW_Y + HALF_BLOCKSIZE;
		int END_X = DRAW_X + BLOCKSIZE, END_Y = DRAW_Y + BLOCKSIZE;
		int QUARTER_X = DRAW_X + QUARTER_BLOCKSIZE, THREE_X = END_X - QUARTER_BLOCKSIZE;

		// Define the center diamond
		int[] xpoints_diamond = {QUARTER_X, MID_X, THREE_X, MID_X};
		int[] ypoints_diamond = {MID_Y, DRAW_Y, MID_Y, END_Y};
		Polygon middleDiamond = new Polygon(xpoints_diamond, ypoints_diamond, 4);
		boolean left = (ADJ & ML) > 0, right = (ADJ & MR) > 0, up = (ADJ & TM) > 0, down = (ADJ & BM) > 0,
				upleft = (ADJ & TL) > 0, upright = (ADJ & TR) > 0, downleft = (ADJ & BL) > 0,
				downright = (ADJ & BR) > 0;

		ArrayList <Polygon> triangles = new ArrayList <Polygon>();
		// alone or no directly adjacent (ok if only corners)
		// {TESTED}
		if ( ! (left || right || up || down)) {
			// Bottom left triangle (small equilateral)
			int[] x1 = {DRAW_X, QUARTER_X, MID_X}, y1 = {END_Y, MID_Y, END_Y};
			triangles.add(new Polygon(x1, y1, 3));
			// Top right triangle (small equilateral)
			int[] x2 = {MID_X, THREE_X, END_X}, y2 = {DRAW_Y, MID_Y, DRAW_Y};
			triangles.add(new Polygon(x2, y2, 3));
			// Top left triangle (large right)
			int[] x3 = {DRAW_X, DRAW_X, MID_X}, y3 = {END_Y, DRAW_Y, DRAW_Y};
			triangles.add(new Polygon(x3, y3, 3));
			// Bottom right triangle (large right)
			int[] x4 = {MID_X, END_X, END_X}, y4 = {END_Y, END_Y, DRAW_Y};
			triangles.add(new Polygon(x4, y4, 3));
		}
		// left and not up, downleft, upright, right, or down
		// {TESTED}
		else if (left && ! (right || up || down || upright || downleft)) {
			// Bottom left triangle (small equilateral)
			int[] x1 = {DRAW_X, QUARTER_X, MID_X}, y1 = {END_Y, MID_Y, END_Y};
			triangles.add(new Polygon(x1, y1, 3));
			// Top right triangle (small equilateral)
			int[] x2 = {MID_X, THREE_X, END_X}, y2 = {DRAW_Y, MID_Y, DRAW_Y};
			triangles.add(new Polygon(x2, y2, 3));
			// Top left rhombus (move onto left shape)
			int[] x3 = { (DRAW_X - HALF_BLOCKSIZE), DRAW_X, MID_X, DRAW_X};
			int[] y3 = {END_Y, END_Y, DRAW_Y, DRAW_Y};
			triangles.add(new Polygon(x3, y3, 4));
			// Bottom right triangle (large right)
			int[] x4 = {MID_X, END_X, END_X}, y4 = {END_Y, END_Y, DRAW_Y};
			triangles.add(new Polygon(x4, y4, 3));
		}
		// left and upright and not up, downleft, right, or down
		// {TESTED}
		else if (left && upright && ! (right || up || down || downleft)) {
			// Bottom left triangle (small equilateral)
			int[] x1 = {DRAW_X, QUARTER_X, MID_X}, y1 = {END_Y, MID_Y, END_Y};
			triangles.add(new Polygon(x1, y1, 3));
			// Top right triangle (small equilateral)
			int[] x2 = {MID_X, THREE_X, END_X}, y2 = {DRAW_Y, MID_Y, DRAW_Y};
			triangles.add(new Polygon(x2, y2, 3));
			// Top left rhombus (move onto left shape)
			int[] x3 = { (DRAW_X - HALF_BLOCKSIZE), DRAW_X, MID_X, DRAW_X};
			int[] y3 = {END_Y, END_Y, DRAW_Y, DRAW_Y};
			triangles.add(new Polygon(x3, y3, 4));
			// Bottom right triangle (large right)
			int[] x4 = {MID_X, END_X, END_X}, y4 = {END_Y, END_Y, DRAW_Y};
			triangles.add(new Polygon(x4, y4, 3));
		}
		// left and downleft and not up, upright, right, or down
		// {TESTED}
		else if (left && downleft && ! (right || up || down || upright)) {
			// Bottom left triangle (small equilateral)
			int[] x1 = {DRAW_X, QUARTER_X, MID_X}, y1 = {END_Y, MID_Y, END_Y};
			triangles.add(new Polygon(x1, y1, 3));
			// Top right triangle (small equilateral)
			int[] x2 = {MID_X, THREE_X, END_X}, y2 = {DRAW_Y, MID_Y, DRAW_Y};
			triangles.add(new Polygon(x2, y2, 3));
			// Top left rhombus (move onto left shape and downleft shape)
			int[] x3 = { (DRAW_X - HALF_BLOCKSIZE), (DRAW_X - QUARTER_BLOCKSIZE), MID_X, DRAW_X};
			int[] y3 = {END_Y, (END_Y + HALF_BLOCKSIZE), DRAW_Y, DRAW_Y};
			triangles.add(new Polygon(x3, y3, 4));
			// Bottom right triangle (large right)
			int[] x4 = {MID_X, END_X, END_X}, y4 = {END_Y, END_Y, DRAW_Y};
			triangles.add(new Polygon(x4, y4, 3));
		}
		// left, down, and downleft and not up, right, or upright
		// {TESTED}
		else if (left && down && downleft && ! (right || up || upright)) {
			// Bottom left rhombus (into down)
			int[] x1 = {DRAW_X, DRAW_X, QUARTER_X, MID_X}, y1 = { (END_Y + BLOCKSIZE), END_Y, MID_Y, END_Y};
			triangles.add(new Polygon(x1, y1, 3));
			// Top right triangle (small equilateral)
			int[] x2 = {MID_X, THREE_X, END_X}, y2 = {DRAW_Y, MID_Y, DRAW_Y};
			triangles.add(new Polygon(x2, y2, 3));
			// Top left rhombus (move onto left shape)
			int[] x3 = { (DRAW_X - HALF_BLOCKSIZE), (DRAW_X - QUARTER_BLOCKSIZE), MID_X, DRAW_X};
			int[] y3 = {END_Y, (END_Y + HALF_BLOCKSIZE), DRAW_Y, DRAW_Y};
			triangles.add(new Polygon(x3, y3, 4));
			// Bottom right triangle (large right into down)
			int[] x4 = {MID_X, END_X, END_X}, y4 = {END_Y, (END_Y + BLOCKSIZE), DRAW_Y};
			triangles.add(new Polygon(x4, y4, 3));
		}
		// left, down, upright, and downleft and not up or right
		// {TESTED}
		else if (left && down && upright && downleft && ! (right || up)) {
			// Bottom left rhombus (into down)
			int[] x1 = {DRAW_X, DRAW_X, QUARTER_X, MID_X}, y1 = { (END_Y + BLOCKSIZE), END_Y, MID_Y, END_Y};
			triangles.add(new Polygon(x1, y1, 3));
			// Top right triangle (small equilateral)
			int[] x2 = {MID_X, THREE_X, END_X}, y2 = {DRAW_Y, MID_Y, DRAW_Y};
			triangles.add(new Polygon(x2, y2, 3));
			// Top left rhombus (move onto left shape)
			int[] x3 = { (DRAW_X - HALF_BLOCKSIZE), (DRAW_X - QUARTER_BLOCKSIZE), MID_X, DRAW_X};
			int[] y3 = {END_Y, (END_Y + HALF_BLOCKSIZE), DRAW_Y, DRAW_Y};
			triangles.add(new Polygon(x3, y3, 4));
			// Bottom right triangle (large right into down)
			int[] x4 = {MID_X, END_X, END_X}, y4 = {END_Y, (END_Y + BLOCKSIZE), DRAW_Y};
			triangles.add(new Polygon(x4, y4, 3));
		}
		// left and up and not downleft, upright, right, or down
		// {TESTED}
		else if (left && up && ! (right || down || upright || downleft)) {
			// Bottom left triangle (small equilateral)
			int[] x1 = {DRAW_X, QUARTER_X, MID_X}, y1 = {END_Y, MID_Y, END_Y};
			triangles.add(new Polygon(x1, y1, 3));
			// Bottom right triangle (small equilateral)
			int[] x2 = {MID_X, THREE_X, END_X}, y2 = {END_Y, MID_Y, END_Y};
			triangles.add(new Polygon(x2, y2, 3));
			// Top left rhombus (move onto left shape and up shape)
			int[] x3 = { (DRAW_X - HALF_BLOCKSIZE), QUARTER_X, MID_X, DRAW_X};
			int[] y3 = {END_Y, (DRAW_Y - HALF_BLOCKSIZE), DRAW_Y, END_Y};
			triangles.add(new Polygon(x3, y3, 4));
			// Right triangle (large right and into up)
			int[] x4 = {END_X, MID_X, END_X}, y4 = {END_Y, DRAW_Y, (DRAW_Y - BLOCKSIZE)};
			triangles.add(new Polygon(x4, y4, 3));
		}
		// left, up, and down and not downleft, upright, right
		// {TESTED}
		else if (left && up && down && ! (right || upright || downleft)) {
			// Bottom left rhombus (into down)
			int[] x1 = {DRAW_X, DRAW_X, QUARTER_X, MID_X}, y1 = { (END_Y + BLOCKSIZE), END_Y, MID_Y, END_Y};
			triangles.add(new Polygon(x1, y1, 3));
			// Right triangle (large right and into up)
			int[] x2 = {END_X, MID_X, END_X}, y2 = {END_Y, DRAW_Y, (DRAW_Y - BLOCKSIZE)};
			triangles.add(new Polygon(x2, y2, 3));
			// Top left rhombus (move onto left shape and up shape)
			int[] x3 = { (DRAW_X - HALF_BLOCKSIZE), QUARTER_X, MID_X, DRAW_X};
			int[] y3 = {END_Y, (DRAW_Y - HALF_BLOCKSIZE), DRAW_Y, END_Y};
			triangles.add(new Polygon(x3, y3, 4));
			// Bottom right triangle (large into down)
			int[] x4 = {MID_X, END_X, END_X}, y4 = {END_Y, (END_Y + BLOCKSIZE), DRAW_Y};
			triangles.add(new Polygon(x4, y4, 3));

		}
		// left and up and upright and not downleft, right, or down
		// {TESTED}
		else if (left && up && upright && ! (right || down || downleft)) {
			// Bottom left triangle (small equilateral)
			int[] x1 = {DRAW_X, QUARTER_X, MID_X}, y1 = {END_Y, MID_Y, END_Y};
			triangles.add(new Polygon(x1, y1, 3));
			// Bottom right triangle (large right)
			int[] x2 = {MID_X, END_X, END_X}, y2 = {END_Y, DRAW_Y, END_Y};
			triangles.add(new Polygon(x2, y2, 3));
			// Top left rhombus (move onto left shape and up shape)
			int[] x3 = { (DRAW_X - HALF_BLOCKSIZE), QUARTER_X, MID_X, DRAW_X};
			int[] y3 = {END_Y, (DRAW_Y - HALF_BLOCKSIZE), DRAW_Y, END_Y};
			triangles.add(new Polygon(x3, y3, 4));
			// Right rhombus (into up and upright)
			int[] x4 = {MID_X, END_X, (END_X + HALF_BLOCKSIZE), THREE_X};
			int[] y4 = {DRAW_Y, (DRAW_Y - BLOCKSIZE), (DRAW_Y - BLOCKSIZE), MID_Y};
			triangles.add(new Polygon(x4, y4, 4));
		}
		// left, up, down and upright and not downleft or right
		// {TESTED}
		else if (left && up && down && upright && ! (right || downleft)) {
			// Bottom left rhombus (into down)
			int[] x1 = {DRAW_X, DRAW_X, QUARTER_X, MID_X}, y1 = { (END_Y + BLOCKSIZE), END_Y, MID_Y, END_Y};
			triangles.add(new Polygon(x1, y1, 3));
			// Bottom right triangle (large right)
			int[] x2 = {MID_X, END_X, END_X}, y2 = {END_Y, DRAW_Y, END_Y};
			triangles.add(new Polygon(x2, y2, 3));
			// Top left rhombus (move onto left shape and up shape)
			int[] x3 = { (DRAW_X - HALF_BLOCKSIZE), QUARTER_X, MID_X, DRAW_X};
			int[] y3 = {END_Y, (DRAW_Y - HALF_BLOCKSIZE), DRAW_Y, END_Y};
			triangles.add(new Polygon(x3, y3, 4));
			// Right rhombus (into up and upright)
			int[] x4 = {MID_X, END_X, (END_X + HALF_BLOCKSIZE), THREE_X};
			int[] y4 = {DRAW_Y, (DRAW_Y - BLOCKSIZE), (DRAW_Y - BLOCKSIZE), MID_Y};
			triangles.add(new Polygon(x4, y4, 4));
		}
		// left and up and upright and downleft and not right, or down
		// {TESTED}
		else if (left && up && upright && downleft && ! (right || down)) {
			// Bottom left triangle (small equilateral)
			int[] x1 = {DRAW_X, QUARTER_X, MID_X}, y1 = {END_Y, MID_Y, END_Y};
			triangles.add(new Polygon(x1, y1, 3));
			// Bottom right triangle (large right)
			int[] x2 = {MID_X, END_X, END_X}, y2 = {END_Y, DRAW_Y, END_Y};
			triangles.add(new Polygon(x2, y2, 3));
			// Top left rhombus (move onto left shape and up shape and downleft
			// shape)
			int[] x3 = { (DRAW_X - HALF_BLOCKSIZE), QUARTER_X, MID_X, (DRAW_X - QUARTER_BLOCKSIZE)};
			int[] y3 = {END_Y, (DRAW_Y - HALF_BLOCKSIZE), DRAW_Y, (END_Y + HALF_BLOCKSIZE)};
			triangles.add(new Polygon(x3, y3, 4));
			// Right rhombus (into up and upright)
			int[] x4 = {MID_X, END_X, (END_X + HALF_BLOCKSIZE), THREE_X};
			int[] y4 = {DRAW_Y, (DRAW_Y - BLOCKSIZE), (DRAW_Y - BLOCKSIZE), MID_Y};
			triangles.add(new Polygon(x4, y4, 4));
		}
		// left and up and downleft and not upright, right, or down
		// {TESTED}
		else if (left && up && downleft && ! (right || down || upright)) {
			// Bottom left triangle (small equilateral)
			int[] x1 = {DRAW_X, QUARTER_X, MID_X}, y1 = {END_Y, MID_Y, END_Y};
			triangles.add(new Polygon(x1, y1, 3));
			// Bottom right triangle (small equilateral)
			int[] x2 = {MID_X, THREE_X, END_X}, y2 = {END_Y, MID_Y, END_Y};
			triangles.add(new Polygon(x2, y2, 3));
			// Top left rhombus (move onto left shape and up shape and downleft
			// shape)
			int[] x3 = { (DRAW_X - HALF_BLOCKSIZE), (DRAW_X - QUARTER_BLOCKSIZE), MID_X, QUARTER_X};
			int[] y3 = {END_Y, (END_Y + HALF_BLOCKSIZE), DRAW_Y, (DRAW_Y - HALF_BLOCKSIZE)};
			triangles.add(new Polygon(x3, y3, 4));
			// Right triangle (large right and into up)
			int[] x4 = {END_X, MID_X, END_X}, y4 = {END_Y, DRAW_Y, (DRAW_Y - BLOCKSIZE)};
			triangles.add(new Polygon(x4, y4, 3));
		}
		// left and right and not up, down, upright, or downleft
		else if (left && right && ! (up || down || upright || downleft)) {
			// Bottom left triangle (small equilateral)
			int[] x1 = {DRAW_X, QUARTER_X, MID_X}, y1 = {END_Y, MID_Y, END_Y};
			triangles.add(new Polygon(x1, y1, 3));
			// Top right triangle (small equilateral)
			int[] x2 = {MID_X, THREE_X, END_X}, y2 = {DRAW_Y, MID_Y, DRAW_Y};
			triangles.add(new Polygon(x2, y2, 3));
			// Left rhombus (into left)
			int[] x3 = { (DRAW_X - HALF_BLOCKSIZE), DRAW_X, MID_X, DRAW_X};
			int[] y3 = {END_Y, DRAW_Y, DRAW_Y, END_Y};
			triangles.add(new Polygon(x3, y3, 4));
			// Right rhombus (into right)
			int[] x4 = {MID_X, END_X, (END_X + HALF_BLOCKSIZE), END_X};
			int[] y4 = {END_Y, DRAW_Y, DRAW_Y, END_Y};
			triangles.add(new Polygon(x4, y4, 4));
		}
		// left, right and downleft and not up, down, or upright
		// {TESTED}
		else if (left && right && downleft && ! (up || down || upright)) {
			// Bottom left triangle (small equilateral)
			int[] x1 = {DRAW_X, QUARTER_X, MID_X}, y1 = {END_Y, MID_Y, END_Y};
			triangles.add(new Polygon(x1, y1, 3));
			// Top right triangle (small equilateral)
			int[] x2 = {MID_X, THREE_X, END_X}, y2 = {DRAW_Y, MID_Y, DRAW_Y};
			triangles.add(new Polygon(x2, y2, 3));
			// Left rhombus (into left and downleft)
			int[] x3 = { (DRAW_X - HALF_BLOCKSIZE), DRAW_X, MID_X, (DRAW_X - QUARTER_BLOCKSIZE)};
			int[] y3 = {END_Y, DRAW_Y, DRAW_Y, (END_Y + HALF_BLOCKSIZE)};
			triangles.add(new Polygon(x3, y3, 4));
			// Right rhombus (into right)
			int[] x4 = {MID_X, END_X, (END_X + HALF_BLOCKSIZE), END_X};
			int[] y4 = {END_Y, DRAW_Y, DRAW_Y, END_Y};
			triangles.add(new Polygon(x4, y4, 4));
		}
		// left, right, upright, and downleft and not up, or down
		// {TESTED}
		else if (left && right && upright && downleft && ! (up || down)) {
			// Bottom left triangle (small equilateral)
			int[] x1 = {DRAW_X, QUARTER_X, MID_X}, y1 = {END_Y, MID_Y, END_Y};
			triangles.add(new Polygon(x1, y1, 3));
			// Top right triangle (small equilateral)
			int[] x2 = {MID_X, THREE_X, END_X}, y2 = {DRAW_Y, MID_Y, DRAW_Y};
			triangles.add(new Polygon(x2, y2, 3));
			// Left rhombus (into left and downleft)
			int[] x3 = { (DRAW_X - HALF_BLOCKSIZE), DRAW_X, MID_X, (DRAW_X - QUARTER_BLOCKSIZE)};
			int[] y3 = {END_Y, DRAW_Y, DRAW_Y, (END_Y + HALF_BLOCKSIZE)};
			triangles.add(new Polygon(x3, y3, 4));
			// Right rhombus (into right)
			int[] x4 = {MID_X, (END_X + QUARTER_BLOCKSIZE), (END_X + HALF_BLOCKSIZE), END_X};
			int[] y4 = {END_Y, (DRAW_Y - HALF_BLOCKSIZE), DRAW_Y, END_Y};
			triangles.add(new Polygon(x4, y4, 4));
		}
		// left, right and upright and not up, down, or downleft
		// {TESTED}
		else if (left && right && upright && ! (up || down || downleft)) {
			// Bottom left triangle (small equilateral)
			int[] x1 = {DRAW_X, QUARTER_X, MID_X}, y1 = {END_Y, MID_Y, END_Y};
			triangles.add(new Polygon(x1, y1, 3));
			// Top right triangle (small equilateral)
			int[] x2 = {MID_X, THREE_X, END_X}, y2 = {DRAW_Y, MID_Y, DRAW_Y};
			triangles.add(new Polygon(x2, y2, 3));
			// Left rhombus (into left)
			int[] x3 = { (DRAW_X - HALF_BLOCKSIZE), DRAW_X, MID_X, DRAW_X};
			int[] y3 = {END_Y, DRAW_Y, DRAW_Y, END_Y};
			triangles.add(new Polygon(x3, y3, 4));
			// Right rhombus (into right and upright)
			int[] x4 = {MID_X, (END_X + QUARTER_BLOCKSIZE), (END_X + HALF_BLOCKSIZE), END_X};
			int[] y4 = {END_Y, (DRAW_Y - HALF_BLOCKSIZE), DRAW_Y, END_Y};
			triangles.add(new Polygon(x4, y4, 4));
		}
		// left and right and up and not down, upright, or downleft
		// {TESTED}
		else if (left && right && up && ! (down || upright || downleft)) {
			// Bottom left triangle (small equilateral)
			int[] x1 = {DRAW_X, QUARTER_X, MID_X}, y1 = {END_Y, MID_Y, END_Y};
			triangles.add(new Polygon(x1, y1, 3));
			// Top right rhombus (into up)
			int[] x2 = {MID_X, THREE_X, END_X, END_X};
			int[] y2 = {DRAW_Y, MID_Y, DRAW_Y, (DRAW_Y - BLOCKSIZE)};
			triangles.add(new Polygon(x2, y2, 4));
			// Left rhombus (into left)
			int[] x3 = { (DRAW_X - HALF_BLOCKSIZE), QUARTER_X, MID_X, DRAW_X};
			int[] y3 = {END_Y, (DRAW_Y - HALF_BLOCKSIZE), DRAW_Y, END_Y};
			triangles.add(new Polygon(x3, y3, 4));
			// Right rhombus (into right)
			int[] x4 = {MID_X, END_X, (END_X + HALF_BLOCKSIZE), END_X};
			int[] y4 = {END_Y, DRAW_Y, DRAW_Y, END_Y};
			triangles.add(new Polygon(x4, y4, 4));
		}
		// left, right, up and downleft and not down or upright
		// {TESTED}
		else if (left && right && up && downleft && ! (down || upright)) {
			// Bottom left triangle (small equilateral)
			int[] x1 = {DRAW_X, QUARTER_X, MID_X}, y1 = {END_Y, MID_Y, END_Y};
			triangles.add(new Polygon(x1, y1, 3));
			// Top right rhombus (into up)
			int[] x2 = {MID_X, THREE_X, END_X, END_X};
			int[] y2 = {DRAW_Y, MID_Y, DRAW_Y, (DRAW_Y - BLOCKSIZE)};
			triangles.add(new Polygon(x2, y2, 4));
			// Left rhombus (into left and downleft)
			int[] x3 = { (DRAW_X - HALF_BLOCKSIZE), QUARTER_X, MID_X, (DRAW_X - QUARTER_BLOCKSIZE)};
			int[] y3 = {END_Y, (DRAW_Y - HALF_BLOCKSIZE), DRAW_Y, (END_Y + HALF_BLOCKSIZE)};
			triangles.add(new Polygon(x3, y3, 4));
			// Right rhombus (into right)
			int[] x4 = {MID_X, END_X, (END_X + HALF_BLOCKSIZE), END_X};
			int[] y4 = {END_Y, DRAW_Y, DRAW_Y, END_Y};
			triangles.add(new Polygon(x4, y4, 4));
		}
		// left and right and up and upright and not down or downleft
		// {TESTED}
		else if (left && right && up && upright && ! (down || downleft)) {
			// Bottom left triangle (small equilateral)
			int[] x1 = {DRAW_X, QUARTER_X, MID_X}, y1 = {END_Y, MID_Y, END_Y};
			triangles.add(new Polygon(x1, y1, 3));
			// Top right rhombus (into up and upright)
			int[] x2 = {MID_X, THREE_X, (END_X + HALF_BLOCKSIZE), END_X};
			int[] y2 = {DRAW_Y, MID_Y, (DRAW_Y - BLOCKSIZE), (DRAW_Y - BLOCKSIZE)};
			triangles.add(new Polygon(x2, y2, 4));
			// Left rhombus (into left and up)
			int[] x3 = { (DRAW_X - HALF_BLOCKSIZE), QUARTER_X, MID_X, DRAW_X};
			int[] y3 = {END_Y, (DRAW_Y - HALF_BLOCKSIZE), DRAW_Y, END_Y};
			triangles.add(new Polygon(x3, y3, 4));
			// Right rhombus (into right and upright)
			int[] x4 = {MID_X, (END_X + QUARTER_BLOCKSIZE), (END_X + HALF_BLOCKSIZE), END_X};
			int[] y4 = {END_Y, (DRAW_Y - HALF_BLOCKSIZE), DRAW_Y, END_Y};
			triangles.add(new Polygon(x4, y4, 4));
		}
		// left, right, up, upright, and down and not downleft
		// {TESTED}
		else if (left && right && up && down && upright && ! (downleft)) {
			// Bottom left rhombus (into down)
			int[] x1 = {DRAW_X, DRAW_X, QUARTER_X, MID_X}, y1 = { (END_Y + BLOCKSIZE), END_Y, MID_Y, END_Y};
			triangles.add(new Polygon(x1, y1, 4));
			// Top right rhombus (into up and upright)
			int[] x2 = {MID_X, THREE_X, (END_X + HALF_BLOCKSIZE), END_X};
			int[] y2 = {DRAW_Y, MID_Y, (DRAW_Y - BLOCKSIZE), (DRAW_Y - BLOCKSIZE)};
			triangles.add(new Polygon(x2, y2, 4));
			// Left rhombus (into left and up)
			int[] x3 = { (DRAW_X - HALF_BLOCKSIZE), QUARTER_X, MID_X, DRAW_X};
			int[] y3 = {END_Y, (DRAW_Y - HALF_BLOCKSIZE), DRAW_Y, END_Y};
			triangles.add(new Polygon(x3, y3, 4));
			// Right rhombus (into right and upright and down)
			int[] x4 = {MID_X, (END_X + QUARTER_BLOCKSIZE), (END_X + HALF_BLOCKSIZE), QUARTER_X};
			int[] y4 = {END_Y, (DRAW_Y - HALF_BLOCKSIZE), DRAW_Y, (END_Y + HALF_BLOCKSIZE)};
			triangles.add(new Polygon(x4, y4, 4));
		}
		// left, right, up, upright, and downleft. not down
		// {TESTED}
		else if (left && right && up && upright && downleft && ! (down)) {
			// Bottom left triangle (small equilateral)
			int[] x1 = {DRAW_X, QUARTER_X, MID_X}, y1 = {END_Y, MID_Y, END_Y};
			triangles.add(new Polygon(x1, y1, 3));
			// Top right rhombus (into up and upright)
			int[] x2 = {MID_X, THREE_X, (END_X + HALF_BLOCKSIZE), END_X};
			int[] y2 = {DRAW_Y, MID_Y, (DRAW_Y - BLOCKSIZE), (DRAW_Y - BLOCKSIZE)};
			triangles.add(new Polygon(x2, y2, 4));
			// Left rhombus (into left and up and downleft)
			int[] x3 = { (DRAW_X - HALF_BLOCKSIZE), QUARTER_X, MID_X, (DRAW_X - QUARTER_BLOCKSIZE)};
			int[] y3 = {END_Y, (DRAW_Y - HALF_BLOCKSIZE), DRAW_Y, (END_Y + HALF_BLOCKSIZE)};
			triangles.add(new Polygon(x3, y3, 4));
			// Right rhombus (into right and upright)
			int[] x4 = {MID_X, (END_X + QUARTER_BLOCKSIZE), (END_X + HALF_BLOCKSIZE), END_X};
			int[] y4 = {END_Y, (DRAW_Y - HALF_BLOCKSIZE), DRAW_Y, END_Y};
			triangles.add(new Polygon(x4, y4, 4));
		}
		// left, right, up, down, upright, and downleft
		// {TESTED}
		else if (left && right && up && down && upright && downleft) {
			// Bottom left rhombus (into downleft and down)
			int[] x1 = { (DRAW_X - HALF_BLOCKSIZE), QUARTER_X, MID_X, DRAW_X};
			int[] y1 = { (END_Y + BLOCKSIZE), MID_Y, END_Y, (END_Y + BLOCKSIZE)};
			triangles.add(new Polygon(x1, y1, 4));
			// Top right rhombus (into up and upright)
			int[] x2 = {MID_X, THREE_X, (END_X + HALF_BLOCKSIZE), END_X};
			int[] y2 = {DRAW_Y, MID_Y, (DRAW_Y - BLOCKSIZE), (DRAW_Y - BLOCKSIZE)};
			triangles.add(new Polygon(x2, y2, 4));
			// Left rhombus (into left and up and downleft)
			int[] x3 = { (DRAW_X - HALF_BLOCKSIZE), QUARTER_X, MID_X, (DRAW_X - QUARTER_BLOCKSIZE)};
			int[] y3 = {END_Y, (DRAW_Y - HALF_BLOCKSIZE), DRAW_Y, (END_Y + HALF_BLOCKSIZE)};
			triangles.add(new Polygon(x3, y3, 4));
			// Right rhombus (into right and upright and down)
			int[] x4 = {MID_X, THREE_X, (END_X + HALF_BLOCKSIZE), (END_X + QUARTER_BLOCKSIZE)};
			int[] y4 = {END_Y, (END_Y + HALF_BLOCKSIZE), DRAW_Y, (DRAW_Y - HALF_BLOCKSIZE)};
			triangles.add(new Polygon(x4, y4, 4));
		}
		// left, right, up, down, and downleft and not upright
		// {TESTED}
		else if (left && right && up && down && downleft && ! (upright)) {
			// Bottom left rhombus (into downleft and down)
			int[] x1 = { (DRAW_X - HALF_BLOCKSIZE), QUARTER_X, MID_X, DRAW_X};
			int[] y1 = { (END_Y + BLOCKSIZE), MID_Y, END_Y, (END_Y + BLOCKSIZE)};
			triangles.add(new Polygon(x1, y1, 4));
			// Top right rhombus (into up)
			int[] x2 = {MID_X, THREE_X, END_X, END_X};
			int[] y2 = {DRAW_Y, MID_Y, DRAW_Y, (DRAW_Y - BLOCKSIZE)};
			triangles.add(new Polygon(x2, y2, 4));
			// Left rhombus (into left and up and downleft)
			int[] x3 = { (DRAW_X - HALF_BLOCKSIZE), QUARTER_X, MID_X, (DRAW_X - QUARTER_BLOCKSIZE)};
			int[] y3 = {END_Y, (DRAW_Y - HALF_BLOCKSIZE), DRAW_Y, (END_Y + HALF_BLOCKSIZE)};
			triangles.add(new Polygon(x3, y3, 4));
			// Right rhombus (into rightdown)
			int[] x4 = {MID_X, THREE_X, (END_X + HALF_BLOCKSIZE), END_X};
			int[] y4 = {END_Y, (END_Y + HALF_BLOCKSIZE), DRAW_Y, DRAW_Y};
			triangles.add(new Polygon(x4, y4, 4));
		}
		// left, right, up, and down and not upright or downleft
		// {TESTED}
		else if (left && right && up && down && ! (upright || downleft)) {
			// Bottom left rhombus (into down)
			int[] x1 = {DRAW_X, MID_X, QUARTER_X, DRAW_X};
			int[] y1 = { (END_Y + BLOCKSIZE), END_Y, MID_Y, END_Y};
			triangles.add(new Polygon(x1, y1, 4));
			// Top right rhombus (into up)
			int[] x2 = {MID_X, THREE_X, END_X, END_X};
			int[] y2 = {DRAW_Y, MID_Y, DRAW_Y, (DRAW_Y - BLOCKSIZE)};
			triangles.add(new Polygon(x2, y2, 4));
			// Left rhombus (into left and up)
			int[] x3 = { (DRAW_X - HALF_BLOCKSIZE), QUARTER_X, MID_X, DRAW_X};
			int[] y3 = {END_Y, (DRAW_Y - HALF_BLOCKSIZE), DRAW_Y, END_Y};
			triangles.add(new Polygon(x3, y3, 4));
			// Right rhombus (into rightdown)
			int[] x4 = {MID_X, THREE_X, (END_X + HALF_BLOCKSIZE), END_X};
			int[] y4 = {END_Y, (END_Y + HALF_BLOCKSIZE), DRAW_Y, DRAW_Y};
			triangles.add(new Polygon(x4, y4, 4));
		}
		// right and not left, up, down, or upright
		// {TESTED}
		else if (right && ! (left || up || down || upright)) {
			// Bottom left triangle (small equilateral)
			int[] x1 = {DRAW_X, QUARTER_X, MID_X}, y1 = {END_Y, MID_Y, END_Y};
			triangles.add(new Polygon(x1, y1, 3));
			// Top right triangle (small equilateral)
			int[] x2 = {MID_X, THREE_X, END_X}, y2 = {DRAW_Y, MID_Y, DRAW_Y};
			triangles.add(new Polygon(x2, y2, 3));
			// Left triangle (large right)
			int[] x3 = {DRAW_X, DRAW_X, MID_X}, y3 = {END_Y, DRAW_Y, DRAW_Y};
			triangles.add(new Polygon(x3, y3, 3));
			// Right rhombus (into right)
			int[] x4 = {MID_X, END_X, (END_X + HALF_BLOCKSIZE), END_X};
			int[] y4 = {END_Y, DRAW_Y, DRAW_Y, END_Y};
			triangles.add(new Polygon(x4, y4, 4));
		}
		// right and upright and not left, up, or down
		// {TESTED}
		else if (right && upright && ! (left || up || down)) {
			// Bottom left triangle (small equilateral)
			int[] x1 = {DRAW_X, QUARTER_X, MID_X}, y1 = {END_Y, MID_Y, END_Y};
			triangles.add(new Polygon(x1, y1, 3));
			// Top right triangle (small equilateral)
			int[] x2 = {MID_X, THREE_X, END_X}, y2 = {DRAW_Y, MID_Y, DRAW_Y};
			triangles.add(new Polygon(x2, y2, 3));
			// Left triangle (large right)
			int[] x3 = {DRAW_X, DRAW_X, MID_X}, y3 = {END_Y, DRAW_Y, DRAW_Y};
			triangles.add(new Polygon(x3, y3, 3));
			// Right rhombus (into right and upright)
			int[] x4 = {MID_X, (END_X + QUARTER_BLOCKSIZE), (END_X + HALF_BLOCKSIZE), END_X};
			int[] y4 = {END_Y, (DRAW_Y - HALF_BLOCKSIZE), DRAW_Y, END_Y};
			triangles.add(new Polygon(x4, y4, 4));
		}
		// right and up and not left, down, or upright
		// {TESTED}
		else if (right && up && ! (left || down || upright)) {
			// Bottom left triangle (small equilateral)
			int[] x1 = {DRAW_X, QUARTER_X, MID_X}, y1 = {END_Y, MID_Y, END_Y};
			triangles.add(new Polygon(x1, y1, 3));
			// Top right rhombus (into up)
			int[] x2 = {MID_X, THREE_X, END_X, END_X}, y2 = {DRAW_Y, MID_Y, DRAW_Y, (DRAW_Y - BLOCKSIZE)};
			triangles.add(new Polygon(x2, y2, 4));
			if (upleft) {
				int[] x3 = {DRAW_X, MID_X, QUARTER_X, DRAW_X};
				int[] y3 = {END_Y, DRAW_Y, (DRAW_Y - HALF_BLOCKSIZE), DRAW_Y};
				triangles.add(new Polygon(x3, y3, 4));
			} else {
				// Left triangle (into up)
				int[] x3 = {DRAW_X, MID_X, DRAW_X}, y3 = {END_Y, DRAW_Y, (DRAW_Y - BLOCKSIZE)};
				triangles.add(new Polygon(x3, y3, 3));
			}
			// Right rhombus (into right)
			int[] x4 = {MID_X, END_X, (END_X + HALF_BLOCKSIZE), END_X};
			int[] y4 = {END_Y, DRAW_Y, DRAW_Y, END_Y};
			triangles.add(new Polygon(x4, y4, 4));
		}
		// right and up and down and not left or upright
		// {TESTED}
		else if (right && up && down && ! (left || upright)) {
			// Bottom left rhombus (into down)
			int[] x1 = {DRAW_X, QUARTER_X, MID_X, DRAW_X}, y1 = {END_Y, MID_Y, END_Y, (END_Y + BLOCKSIZE)};
			triangles.add(new Polygon(x1, y1, 4));
			// Top right rhombus (into up)
			int[] x2 = {MID_X, THREE_X, END_X, END_X}, y2 = {DRAW_Y, MID_Y, DRAW_Y, (DRAW_Y - BLOCKSIZE)};
			triangles.add(new Polygon(x2, y2, 4));
			if (upleft) {
				// Left rhombus (into up)
				int[] x3 = {DRAW_X, MID_X, QUARTER_X, DRAW_X};
				int[] y3 = {END_Y, DRAW_Y, (DRAW_Y - HALF_BLOCKSIZE), DRAW_Y};
				triangles.add(new Polygon(x3, y3, 4));
			} else {
				// Left triangle (into up)
				int[] x3 = {DRAW_X, MID_X, DRAW_X}, y3 = {END_Y, DRAW_Y, (DRAW_Y - BLOCKSIZE)};
				triangles.add(new Polygon(x3, y3, 3));
			}
			// Right rhombus (into right and down)
			int[] x4 = {MID_X, END_X, (END_X + HALF_BLOCKSIZE), THREE_X};
			int[] y4 = {END_Y, DRAW_Y, DRAW_Y, (END_Y + HALF_BLOCKSIZE)};
			triangles.add(new Polygon(x4, y4, 4));
		}
		// right and down and not left, up, or upright
		// {TESTED}
		else if (right && down && ! (left || up || upright)) {
			// Bottom left rhombus (into down)
			int[] x1 = {DRAW_X, QUARTER_X, MID_X, DRAW_X}, y1 = {END_Y, MID_Y, END_Y, (END_Y + BLOCKSIZE)};
			triangles.add(new Polygon(x1, y1, 4));
			// Top right triangle (small equilateral)
			int[] x2 = {MID_X, THREE_X, END_X}, y2 = {DRAW_Y, MID_Y, DRAW_Y};
			triangles.add(new Polygon(x2, y2, 3));
			// Left triangle (large right)
			int[] x3 = {DRAW_X, MID_X, DRAW_X}, y3 = {END_Y, DRAW_Y, DRAW_Y};
			triangles.add(new Polygon(x3, y3, 3));
			// Right rhombus (into right and down)
			int[] x4 = {MID_X, END_X, (END_X + HALF_BLOCKSIZE), THREE_X};
			int[] y4 = {END_Y, DRAW_Y, DRAW_Y, (END_Y + HALF_BLOCKSIZE)};
			triangles.add(new Polygon(x4, y4, 4));
		}
		// right and down and upright and not left or up
		// {TESTED}
		else if (right && down && upright && ! (left || up)) {
			// Bottom left rhombus (into down)
			int[] x1 = {DRAW_X, QUARTER_X, MID_X, DRAW_X}, y1 = {END_Y, MID_Y, END_Y, (END_Y + BLOCKSIZE)};
			triangles.add(new Polygon(x1, y1, 4));
			// Top right triangle (small equilateral)
			int[] x2 = {MID_X, THREE_X, END_X}, y2 = {DRAW_Y, MID_Y, DRAW_Y};
			triangles.add(new Polygon(x2, y2, 3));
			// Left triangle (large right)
			int[] x3 = {DRAW_X, MID_X, DRAW_X}, y3 = {END_Y, DRAW_Y, DRAW_Y};
			triangles.add(new Polygon(x3, y3, 3));
			// Right rhombus (into right and down and upright)
			int[] x4 = {MID_X, (END_X + QUARTER_BLOCKSIZE), (END_X + HALF_BLOCKSIZE), THREE_X};
			int[] y4 = {END_Y, (DRAW_Y - HALF_BLOCKSIZE), DRAW_Y, (END_Y + HALF_BLOCKSIZE)};
			triangles.add(new Polygon(x4, y4, 4));
		}
		// right and down and upright and up and not left
		// {TESTED}
		else if (right && down && upright && up && ! (left)) {
			// Bottom left rhombus (into down)
			int[] x1 = {DRAW_X, QUARTER_X, MID_X, DRAW_X}, y1 = {END_Y, MID_Y, END_Y, (END_Y + BLOCKSIZE)};
			triangles.add(new Polygon(x1, y1, 4));
			// Top right rhombus (into up)
			int[] x2 = {MID_X, THREE_X, (END_X + HALF_BLOCKSIZE), END_X};
			int[] y2 = {DRAW_Y, MID_Y, (DRAW_Y - BLOCKSIZE), (DRAW_Y - BLOCKSIZE)};
			triangles.add(new Polygon(x2, y2, 4));
			if (upleft) {
				// Left rhombus (into up)
				int[] x3 = {DRAW_X, MID_X, QUARTER_X, DRAW_X};
				int[] y3 = {END_Y, DRAW_Y, (DRAW_Y - HALF_BLOCKSIZE), DRAW_Y};
				triangles.add(new Polygon(x3, y3, 4));
			} else {
				// Left triangle (into up)
				int[] x3 = {DRAW_X, MID_X, DRAW_X}, y3 = {END_Y, DRAW_Y, (DRAW_Y - BLOCKSIZE)};
				triangles.add(new Polygon(x3, y3, 3));
			}
			// Right rhombus (into right and down and upright)
			int[] x4 = {MID_X, (END_X + QUARTER_BLOCKSIZE), (END_X + HALF_BLOCKSIZE), THREE_X};
			int[] y4 = {END_Y, (DRAW_Y - HALF_BLOCKSIZE), DRAW_Y, (END_Y + HALF_BLOCKSIZE)};
			triangles.add(new Polygon(x4, y4, 4));
		}
		// AD HOC: if up
		else if (up) {
			if ( !right && !upright) {
				int[] xthis1 = {END_X, MID_X, END_X}, ythis1 = {END_Y, DRAW_Y, (DRAW_Y - BLOCKSIZE)};
				triangles.add(new Polygon(xthis1, ythis1, 3));
				// small equilateral
				int[] xthis3 = {MID_X, THREE_X, END_X}, ythis3 = {END_Y, MID_Y, END_Y};
				triangles.add(new Polygon(xthis3, ythis3, 3));
			} else if ( !right) {
				// right triangle bottom right corner
				int[] xthis2 = {MID_X, END_X, END_X};
				int[] ythis2 = {END_Y, END_Y, DRAW_Y};
				triangles.add(new Polygon(xthis2, ythis2, 3));
			}
			if ( !left && !upleft) {
				int[] xthis2 = {DRAW_X, MID_X, DRAW_X}, ythis2 = {END_Y, DRAW_Y, (DRAW_Y - BLOCKSIZE)};
				triangles.add(new Polygon(xthis2, ythis2, 3));
				// small equilateral
				int[] xthis3 = {DRAW_X, QUARTER_X, MID_X}, ythis3 = {END_Y, MID_Y, END_Y};
				triangles.add(new Polygon(xthis3, ythis3, 3));
			} else if ( !left) {
				int[] xthis2 = {DRAW_X, MID_X, QUARTER_X, DRAW_X};
				int[] ythis2 = {END_Y, DRAW_Y, (DRAW_Y - HALF_BLOCKSIZE), DRAW_Y};
				triangles.add(new Polygon(xthis2, ythis2, 4));
				// small equilateral
				int[] xthis3 = {DRAW_X, QUARTER_X, MID_X}, ythis3 = {END_Y, MID_Y, END_Y};
				triangles.add(new Polygon(xthis3, ythis3, 3));
			}
		} else if (right) {
			// small equilateral at top right
			int[] xthis = {MID_X, THREE_X, END_X}, ythis = {DRAW_Y, MID_Y, DRAW_Y};
			triangles.add(new Polygon(xthis, ythis, 3));
		} else if (down) {
			if ( !right && !upright) {
				// small equilateral at top right
				int[] xthis = {MID_X, THREE_X, END_X}, ythis = {DRAW_Y, MID_Y, DRAW_Y};
				triangles.add(new Polygon(xthis, ythis, 3));
			} else if ( !right) {
				// small equilateral at top right
				int[] xthis = {MID_X, THREE_X, END_X}, ythis = {DRAW_Y, MID_Y, DRAW_Y};
				triangles.add(new Polygon(xthis, ythis, 3));
			}
			if ( !left && !upleft) {
				// right triangle at top left
				int[] xthis = {DRAW_X, MID_X, DRAW_X}, ythis = {DRAW_Y, DRAW_Y, END_Y};
				triangles.add(new Polygon(xthis, ythis, 3));
			} else if ( !left) {
				// right triangle at top left
				int[] xthis = {DRAW_X, MID_X, DRAW_X}, ythis = {DRAW_Y, DRAW_Y, END_Y};
				triangles.add(new Polygon(xthis, ythis, 3));
			}
		} else if (true) {

		}
		// if up, upright, and up-upright
		if (up && upright && up_upright) {
			// draw big rhombus across this, up, upright, and up_upright
			int[] xthis = {MID_X, (END_X + QUARTER_BLOCKSIZE), (END_X + HALF_BLOCKSIZE), THREE_X};
			int[] ythis = {DRAW_Y, (DRAW_Y - BLOCKSIZE - HALF_BLOCKSIZE), (DRAW_Y - BLOCKSIZE), MID_Y};
			triangles.add(new Polygon(xthis, ythis, 4));
		}
		//upleft, up, upright, and right, not left, downleft, down, or downright
		if (upleft && up && upright && right && !(left || downleft || down || downright)){
			int[] xEnd = {MID_X,THREE_X,(END_X + HALF_BLOCKSIZE),END_X};
			int[] yEnd = {DRAW_Y,MID_Y,(DRAW_Y - BLOCKSIZE),(DRAW_Y - BLOCKSIZE)};
			triangles.add(new Polygon(xEnd,yEnd,4));			
		}
		
		if (isCoin) {
			if (skyDraw.equals("NONE")){ }
			else {
				skyPolygons.addAll(triangles);
			}
		} else {
			for (Polygon trig : triangles) {
				drawShape(trig, color_background, Sprite.COLOR_BORDER);
			}
		}
		drawShape(middleDiamond, color_diamond, Sprite.COLOR_BORDER);
	}

	/**
	 * Draw a music block at XY
	 * 
	 * @param X
	 *            The x coordinate to draw it at
	 * @param Y
	 *            The y coordinate to draw it at
	 * @param ADJ
	 *            The adjacent blocks of the same ID
	 */
	private void drawMusicBlock(int X, int Y, int ADJ) {
		int DRAW_X = X * BLOCKSIZE, DRAW_Y = Y * BLOCKSIZE;
		int MID_X = DRAW_X + HALF_BLOCKSIZE, MID_Y = DRAW_Y + HALF_BLOCKSIZE;
		int END_X = DRAW_X + BLOCKSIZE, END_Y = DRAW_Y + BLOCKSIZE;
		int QUARTER_X = DRAW_X + QUARTER_BLOCKSIZE, QUARTER_Y = DRAW_Y + QUARTER_BLOCKSIZE;
		int THREE_X = END_X - QUARTER_BLOCKSIZE, THREE_Y = END_Y - QUARTER_BLOCKSIZE;

		boolean left = (ADJ & ML) > 0, right = (ADJ & MR) > 0, up = (ADJ & TM) > 0, down = (ADJ & BM) > 0,
				upleft = (ADJ & TL) > 0, upright = (ADJ & TR) > 0, downleft = (ADJ & BL) > 0,
				downright = (ADJ & BR) > 0;

		// Define the Black diamond and tail for the note
		int[] xpoints_diamond = {QUARTER_X, MID_X, THREE_X, MID_X};
		int[] ypoints_diamond = {THREE_Y, END_Y, THREE_Y, MID_Y};
		Polygon bottomDiamond = new Polygon(xpoints_diamond, ypoints_diamond, 4);
		int[] xpoints_tail = {MID_X, END_X, MID_X}, ypoints_tail = {DRAW_Y, DRAW_Y, MID_Y};
		Polygon noteTail = new Polygon(xpoints_tail, ypoints_tail, 3);
		int[] xpoints_tail2 = {MID_X, THREE_X, THREE_X}, ypoints_tail2 = {MID_Y, QUARTER_Y, THREE_Y};
		Polygon noteTail2 = new Polygon(xpoints_tail2, ypoints_tail2, 3);

		ArrayList <Polygon> triangles = new ArrayList <Polygon>();

		// Checking RIGHT && UPRIGHT
		if (right && upright) {
			// Bottom right rhombus (into right)
			int[] x2 = {MID_X, END_X, (END_X + HALF_BLOCKSIZE), (END_X + HALF_BLOCKSIZE)};
			int[] y2 = {END_Y, END_Y, MID_Y, DRAW_Y};
			triangles.add(new Polygon(x2, y2, 4));
			// Right rhombus (into right and upright)
			int[] x3 = {THREE_X, THREE_X, (END_X + QUARTER_BLOCKSIZE), (END_X + HALF_BLOCKSIZE)};
			int[] y3 = {THREE_Y, QUARTER_Y, (DRAW_Y - QUARTER_BLOCKSIZE), DRAW_Y};
			triangles.add(new Polygon(x3, y3, 4));
		} else if (right) {
			// Bottom right rhombus (into right)
			int[] x2 = {MID_X, END_X, (END_X + HALF_BLOCKSIZE), (END_X + HALF_BLOCKSIZE)};
			int[] y2 = {END_Y, END_Y, MID_Y, DRAW_Y};
			triangles.add(new Polygon(x2, y2, 4));
			// Right rhombus (into right)
			int[] x3 = {THREE_X, THREE_X, END_X, (END_X + HALF_BLOCKSIZE)};
			int[] y3 = {THREE_Y, QUARTER_Y, DRAW_Y, DRAW_Y};
			triangles.add(new Polygon(x3, y3, 4));
		} else { // None to the right
			// Bottom right EQUILATERAL
			int[] x2 = {MID_X, THREE_X, END_X}, y2 = {END_Y, THREE_Y, END_Y};
			triangles.add(new Polygon(x2, y2, 3));
			// Right rhombus
			int[] x3 = {THREE_X, END_X, END_X, THREE_X};
			int[] y3 = {THREE_Y, END_Y, DRAW_Y, QUARTER_Y};
			triangles.add(new Polygon(x3, y3, 4));
		}
		// Checking LEFT && UP
		if (left && up) {
			// Left rhombus (into left)
			int[] x4 = {DRAW_X, MID_X, MID_X, (DRAW_X - HALF_BLOCKSIZE)};
			int[] y4 = {END_Y, MID_Y, DRAW_Y, END_Y};
			triangles.add(new Polygon(x4, y4, 4));
			// Top left RIGHT (into left and up)
			int[] x5 = { (DRAW_X - QUARTER_BLOCKSIZE), (DRAW_X - QUARTER_BLOCKSIZE), QUARTER_X, MID_X};
			int[] y5 = {THREE_Y, QUARTER_Y, (DRAW_Y - QUARTER_BLOCKSIZE), DRAW_Y};
			triangles.add(new Polygon(x5, y5, 4));
		} else if (left) {
			// Left rhombus (into left)
			int[] x4 = {DRAW_X, MID_X, MID_X, (DRAW_X - HALF_BLOCKSIZE)};
			int[] y4 = {END_Y, MID_Y, DRAW_Y, END_Y};
			triangles.add(new Polygon(x4, y4, 4));
			// Top left RIGHT (into left)
			int[] x5 = { (DRAW_X - QUARTER_BLOCKSIZE), (DRAW_X - QUARTER_BLOCKSIZE), DRAW_X, MID_X};
			int[] y5 = {THREE_Y, QUARTER_Y, DRAW_Y, DRAW_Y};
			triangles.add(new Polygon(x5, y5, 4));
		} else if (up) {
			// Left rhombus
			int[] x4 = {DRAW_X, MID_X, MID_X, DRAW_X};
			int[] y4 = {END_Y, MID_Y, DRAW_Y, MID_Y};
			triangles.add(new Polygon(x4, y4, 4));
			// Top left RIGHT (into up)
			int[] x5 = {DRAW_X, DRAW_X, QUARTER_X, MID_X};
			int[] y5 = {MID_Y, DRAW_Y, (DRAW_Y - QUARTER_BLOCKSIZE), DRAW_Y};
			triangles.add(new Polygon(x5, y5, 4));
		} else { // None to the left or up
			// One big rhombus for the whole top left
			int[] x3 = {DRAW_X, MID_X, MID_X, DRAW_X};
			int[] y3 = {DRAW_Y, DRAW_Y, MID_Y, END_Y};
			triangles.add(new Polygon(x3, y3, 4));
		}
		// Checking LEFT && DOWNLEFT
		if (down && downleft) {
			// Bottom left rhombus (into down and downleft)
			int[] x1 = { (DRAW_X - QUARTER_BLOCKSIZE), QUARTER_X, MID_X, (DRAW_X - QUARTER_BLOCKSIZE)};
			int[] y1 = { (END_Y + QUARTER_BLOCKSIZE), THREE_Y, END_Y, (END_Y + THREE_BLOCKSIZE)};
			triangles.add(new Polygon(x1, y1, 4));
		} else if (down) {
			// Bottom left rhombus (into down)
			int[] x1 = {DRAW_X, QUARTER_X, MID_X, DRAW_X};
			int[] y1 = {END_Y, THREE_Y, END_Y, (END_Y + HALF_BLOCKSIZE)};
			triangles.add(new Polygon(x1, y1, 4));
		} else { // None below
			// Bottom left EQUILATERAL
			int[] x1 = {DRAW_X, QUARTER_X, MID_X}, y1 = {END_Y, THREE_Y, END_Y};
			triangles.add(new Polygon(x1, y1, 3));
		}

		for (Polygon trig : triangles) {
			drawShape(trig, Sprite.COLOR_MUSIC_BG, Sprite.COLOR_BORDER);
		}
		drawShape(bottomDiamond, Sprite.COLOR_MUSIC_NOTE, Sprite.COLOR_BORDER);
		drawShape(noteTail, Sprite.COLOR_MUSIC_NOTE, Sprite.COLOR_BORDER);
		drawShape(noteTail2, Sprite.COLOR_MUSIC_NOTE, Sprite.COLOR_BORDER);
	}

	/**
	 * Draw a yellow face block or cloud at XY
	 * 
	 * @param X
	 *            The x coordinate to draw it at
	 * @param Y
	 *            The y coordinate to draw it at
	 * @param MOUTH
	 *            Drawing a cloud if true, drawing a yellow face block otherwise
	 * @param ADJ
	 *            The adjacent blocks of the same ID
	 */
	private void drawFaceBlock(int X, int Y, int ADJ, boolean MOUTH) {
		int DRAW_X = X * BLOCKSIZE, DRAW_Y = Y * BLOCKSIZE;
		int MID_X = DRAW_X + HALF_BLOCKSIZE, MID_Y = DRAW_Y + HALF_BLOCKSIZE;
		int END_X = DRAW_X + BLOCKSIZE, END_Y = DRAW_Y + BLOCKSIZE;
		int QUARTER_X = DRAW_X + QUARTER_BLOCKSIZE, QUARTER_Y = DRAW_Y + QUARTER_BLOCKSIZE;
		int THREE_X = END_X - QUARTER_BLOCKSIZE, THREE_Y = END_Y - QUARTER_BLOCKSIZE;

		boolean left = (ADJ & ML) > 0, right = (ADJ & MR) > 0, up = (ADJ & TM) > 0, down = (ADJ & BM) > 0,
				upleft = (ADJ & TL) > 0, upright = (ADJ & TR) > 0, downleft = (ADJ & BL) > 0,
				downright = (ADJ & BR) > 0;
		// Define left eye
		int[] x_left = {QUARTER_X, DRAW_X, QUARTER_X}, y_left = {THREE_Y, MID_Y, QUARTER_Y};
		Polygon leftEye = new Polygon(x_left, y_left, 3);
		// Define right eye
		int[] x_right = {THREE_X, END_X, THREE_X}, y_right = {THREE_Y, MID_Y, QUARTER_Y};
		Polygon rightEye = new Polygon(x_right, y_right, 3);
		// Define mouth
		int[] x_mouth = {QUARTER_X, MID_X, THREE_X}, y_mouth = {THREE_Y, END_Y, THREE_Y};
		Polygon mouth = new Polygon(x_mouth, y_mouth, 3);

		ArrayList <Polygon> triangles = new ArrayList <Polygon>();

		// Unoptimizable shapes
		int[] x_top = {QUARTER_X, MID_X, THREE_X}, y_top = {QUARTER_Y, DRAW_Y, QUARTER_Y};
		triangles.add(new Polygon(x_top, y_top, 3));
		int[] x_box = {QUARTER_X, THREE_X, THREE_X, QUARTER_X}, y_box = {QUARTER_Y, QUARTER_Y, THREE_Y, THREE_Y};
		triangles.add(new Polygon(x_box, y_box, 4));

		// Base Coverage
		if ( ( !up && !left) || (left && upleft)) {
			// Top Left
			int[] x1 = {DRAW_X, MID_X, DRAW_X}, y1 = {MID_Y, DRAW_Y, DRAW_Y};
			triangles.add(new Polygon(x1, y1, 3));
		}
		if ( ( !up && !right) || (right && upright)) {
			// Top Right
			int[] x1 = {END_X, MID_X, END_X}, y1 = {MID_Y, DRAW_Y, DRAW_Y};
			triangles.add(new Polygon(x1, y1, 3));
		}
		if ( ( !down && !left) || (left && downleft)) {
			// Bottom Left
			int[] x1 = {DRAW_X, MID_X, DRAW_X}, y1 = {MID_Y, END_Y, END_Y};
			triangles.add(new Polygon(x1, y1, 3));
		}
		if ( ( !down && !right) || (right && downright)) {
			// Bottom Right
			int[] x1 = {END_X, MID_X, END_X}, y1 = {MID_Y, END_Y, END_Y};
			triangles.add(new Polygon(x1, y1, 3));
		}

		// Checking LEFT && UP && UPLEFT
		if (left && up && upleft) {
			// Top Left Diamond (into left up and upleft)
			int[] x1 = {DRAW_X, MID_X, DRAW_X, (DRAW_X - HALF_BLOCKSIZE)};
			int[] y1 = {MID_Y, DRAW_Y, (DRAW_Y - HALF_BLOCKSIZE), DRAW_Y};
			triangles.add(new Polygon(x1, y1, 4));
		} else if (left && !up && !upleft) { // only left
			// Top Left (into left)
			int[] x1 = {DRAW_X, MID_X, (DRAW_X - HALF_BLOCKSIZE)};
			int[] y1 = {MID_Y, DRAW_Y, DRAW_Y};
			triangles.add(new Polygon(x1, y1, 3));
		} else if (up) { // up takes priority over down
			// Top Left (into up)
			int[] x1 = {DRAW_X, MID_X, DRAW_X};
			int[] y1 = {MID_Y, DRAW_Y, (DRAW_Y - HALF_BLOCKSIZE)};
			triangles.add(new Polygon(x1, y1, 3));
		} else { // asume none above or to the left
			// Top Left
			int[] x1 = {DRAW_X, MID_X, DRAW_X};
			int[] y1 = {MID_Y, DRAW_Y, DRAW_Y};
			triangles.add(new Polygon(x1, y1, 3));
		}

		// Checking RIGHT && DOWN && DOWNRIGHT
		if (right && down && downright) {
			// Bottom Right Diamond (into right down and downright)
			int[] x1 = {MID_X, END_X, (END_X + HALF_BLOCKSIZE), END_X};
			int[] y1 = {END_Y, MID_Y, END_Y, (END_Y + HALF_BLOCKSIZE)};
			triangles.add(new Polygon(x1, y1, 4));
		} else if (right && !down && !downright) { // only right
			// Bottom Right (into right)
			int[] x1 = {MID_X, END_X, (END_X + HALF_BLOCKSIZE)};
			int[] y1 = {END_Y, MID_Y, END_Y};
			triangles.add(new Polygon(x1, y1, 3));
		} else if (down) { // down takes priority over right
			// Bottom Right (into down)
			int[] x1 = {END_X, MID_X, END_X};
			int[] y1 = {MID_Y, END_Y, (END_Y + HALF_BLOCKSIZE)};
			triangles.add(new Polygon(x1, y1, 3));
		} else { // assume none below or to the right
			// Bottom Right Triangle
			int[] x1 = {MID_X, END_X, END_X};
			int[] y1 = {END_Y, END_Y, MID_Y};
			triangles.add(new Polygon(x1, y1, 3));
		}

		drawShape(leftEye, Sprite.COLOR_FACE_EYES, Sprite.COLOR_BORDER);
		drawShape(rightEye, Sprite.COLOR_FACE_EYES, Sprite.COLOR_BORDER);
		if (MOUTH) { // A cloud block
			drawShape(mouth, Sprite.COLOR_CLOUD_MOUTH, Sprite.COLOR_BORDER);
			for (Polygon trig : triangles) {
				drawShape(trig, Sprite.COLOR_CLOUD_WHITE, Sprite.COLOR_BORDER);
			}
		} else { // A yellow face block
			drawShape(mouth, Sprite.COLOR_FACE_YELLOW, Sprite.COLOR_BORDER);
			for (Polygon trig : triangles) {
				drawShape(trig, Sprite.COLOR_FACE_YELLOW, Sprite.COLOR_BORDER);
			}
		}

	}

	/**
	 * Draw a brown face block at XY
	 * 
	 * @param X
	 *            The x coordinate to draw it at
	 * @param Y
	 *            The y coordinate to draw it at
	 * @param ADJ
	 *            The adjacent blocks of the same ID
	 * @param LENGTH
	 *            The number of brown face blocks to the left (inclusive)
	 * @param HEIGHT
	 *            The number of brown faces above (inclusive)
	 */
	private void drawBrownFaceBlock(int X, int Y, int ADJ, int LENGTH, int HEIGHT) {
		int DRAW_X = X * BLOCKSIZE, DRAW_Y = Y * BLOCKSIZE;
		int MID_X = DRAW_X + HALF_BLOCKSIZE, MID_Y = DRAW_Y + HALF_BLOCKSIZE;
		int END_X = DRAW_X + BLOCKSIZE, END_Y = DRAW_Y + BLOCKSIZE;
		int QUARTER_X = DRAW_X + QUARTER_BLOCKSIZE, QUARTER_Y = DRAW_Y + QUARTER_BLOCKSIZE;
		int THREE_X = END_X - QUARTER_BLOCKSIZE, THREE_Y = END_Y - QUARTER_BLOCKSIZE;

		boolean left = (ADJ & ML) > 0, right = (ADJ & MR) > 0, up = (ADJ & TM) > 0, down = (ADJ & BM) > 0,
				upleft = (ADJ & TL) > 0, upright = (ADJ & TR) > 0, downleft = (ADJ & BL) > 0,
				downright = (ADJ & BR) > 0;
		// Define left eye
		int[] x_left = {QUARTER_X, QUARTER_X, DRAW_X}, y_left = {THREE_Y, MID_Y, QUARTER_Y};
		Polygon leftEye = new Polygon(x_left, y_left, 3);
		// Define right eye
		int[] x_right = {THREE_X, THREE_X, END_X}, y_right = {THREE_Y, MID_Y, QUARTER_Y};
		Polygon rightEye = new Polygon(x_right, y_right, 3);

		ArrayList <Polygon> triangles = new ArrayList <Polygon>();

		if (right && !down) {
			// Right Diamond (into right)
			int[] x1 = {END_X, THREE_X, END_X, (END_X + QUARTER_BLOCKSIZE)};
			int[] y1 = {QUARTER_Y, THREE_Y, END_Y, THREE_Y};
			triangles.add(new Polygon(x1, y1, 4));
		}
		if (left && !down) {
			// Left Diamond (into left)
			int[] x1 = {DRAW_X, QUARTER_X, DRAW_X, (DRAW_X - QUARTER_BLOCKSIZE)};
			int[] y1 = {QUARTER_Y, THREE_Y, END_Y, THREE_Y};
			triangles.add(new Polygon(x1, y1, 4));
		}
		if ( !up) {
			// Top rhombus
			int[] xr = {DRAW_X, QUARTER_X, THREE_X, END_X};
			int[] yr = {QUARTER_Y, MID_Y, MID_Y, QUARTER_Y};
			triangles.add(new Polygon(xr, yr, 4));
		}
		if ( !down) {
			// Bottom rhombus
			int[] xr = {DRAW_X, QUARTER_X, THREE_X, END_X};
			int[] yr = {END_Y, THREE_Y, THREE_Y, END_Y};
			triangles.add(new Polygon(xr, yr, 4));
			if ( !right) {
				// Right rhombus
				int[] x1 = {THREE_X, THREE_X, END_X, END_X};
				int[] y1 = {THREE_Y, THREE_Y, QUARTER_Y, END_Y};
				triangles.add(new Polygon(x1, y1, 4));
			}
			if ( !left) {
				// Left rhombus
				int[] x1 = {QUARTER_X, QUARTER_X, DRAW_X, DRAW_X};
				int[] y1 = {THREE_Y, THREE_Y, QUARTER_Y, END_Y};
				triangles.add(new Polygon(x1, y1, 4));
			}
		} else { // if DOWN
			// Right rhombus (into down)
			int[] xr = {THREE_X, THREE_X, END_X, END_X};
			int[] yr = {THREE_Y, (END_Y + HALF_BLOCKSIZE), (END_Y + QUARTER_BLOCKSIZE), QUARTER_Y};
			triangles.add(new Polygon(xr, yr, 4));
			// Left rhombus (into down)
			int[] xl = {QUARTER_X, QUARTER_X, DRAW_X, DRAW_X};
			int[] yl = {THREE_Y, (END_Y + HALF_BLOCKSIZE), (END_Y + QUARTER_BLOCKSIZE), QUARTER_Y};
			triangles.add(new Polygon(xl, yl, 4));
		}
		// Center rectangle up to top rhombus
		int yCalc = MID_Y - (BLOCKSIZE * (HEIGHT - 1));
		int[] xc = {QUARTER_X, THREE_X, THREE_X, QUARTER_X};
		int[] yc = {THREE_Y, THREE_Y, yCalc, yCalc};
		triangles.add(new Polygon(xc, yc, 4));
		// Top Brown bar
		if ( !up) {
			int xCalc = END_X - (BLOCKSIZE * (LENGTH));
			int[] xb = {xCalc, xCalc, END_X, END_X};
			int[] yb = {DRAW_Y, QUARTER_Y, QUARTER_Y, DRAW_Y};
			triangles.add(new Polygon(xb, yb, 4));
		}

		drawShape(leftEye, Sprite.COLOR_FACE_EYES, Sprite.COLOR_BORDER);
		drawShape(rightEye, Sprite.COLOR_FACE_EYES, Sprite.COLOR_BORDER);
		for (Polygon trig : triangles) {
			drawShape(trig, Sprite.COLOR_FACE_BROWN, Sprite.COLOR_BORDER);
		}

	}

	/**
	 * Draw a coin at XY
	 * 
	 * @param X
	 *            The x coordinate to draw it at
	 * @param Y
	 *            The y coordinate to draw it at
	 * @throws IOException
	 */
	private void drawCoin(int X, int Y, int ADJ, boolean up_upright){
		drawCenteredDiamondBlock(X, Y, ADJ, up_upright, Sprite.COLOR_SKY, Sprite.COLOR_COIN, true);
	}

	/**
	 * Draw the cap of a vertical pipe from the left
	 * 
	 * @param X
	 *            The x coordinate of the left of the cap
	 * @param Y
	 *            The y coordinate of the left of the cap
	 */
	private void drawVerticalPipeEnd(int X, int Y) {
		int DRAW_X = X * BLOCKSIZE, DRAW_Y = Y * BLOCKSIZE;
		int END_X = DRAW_X + BLOCKSIZE, END_Y = DRAW_Y + BLOCKSIZE;

		ArrayList <Polygon> lightGreen = new ArrayList <Polygon>(), green = new ArrayList <Polygon>(),
				darkGreen = new ArrayList <Polygon>();

		// Top of the pipe (LIGHT)
		int[] x_top_light = {DRAW_X, END_X, DRAW_X};
		int[] y_top_light = {DRAW_Y, DRAW_Y, END_Y};
		lightGreen.add(new Polygon(x_top_light, y_top_light, 3));
		// Top of the pipe (MEDIUM)
		int[] x_top_med = {DRAW_X, END_X, (END_X + BLOCKSIZE), END_X};
		int[] y_top_med = {END_Y, END_Y, DRAW_Y, DRAW_Y};
		green.add(new Polygon(x_top_med, y_top_med, 4));
		// Top of the pipe (DARK)
		int[] x_top_dark = { (END_X + BLOCKSIZE), (END_X + BLOCKSIZE), END_X};
		int[] y_top_dark = {DRAW_Y, END_Y, END_Y};
		darkGreen.add(new Polygon(x_top_dark, y_top_dark, 3));

		// Draw The Pipe
		for (Polygon shape : lightGreen) {
			drawShape(shape, Sprite.COLOR_PIPE_LIGHT, Sprite.COLOR_BORDER);
		}
		for (Polygon shape : green) {
			drawShape(shape, Sprite.COLOR_PIPE_GREEN, Sprite.COLOR_BORDER);
		}
		for (Polygon shape : darkGreen) {
			drawShape(shape, Sprite.COLOR_PIPE_DARK, Sprite.COLOR_BORDER);
		}
	}

	/**
	 * Draw the cap of a horizontal pipe from the bottom
	 * 
	 * @param X
	 *            The x coordinate of the bottom of the cap
	 * @param Y
	 *            The y coordinate of the bottom of the cap
	 */
	private void drawHorizontalPipeEnd(int X, int Y) {
		int DRAW_X = X * BLOCKSIZE, DRAW_Y = Y * BLOCKSIZE;
		int MID_X = (X * BLOCKSIZE) + HALF_BLOCKSIZE;
		int END_X = DRAW_X + BLOCKSIZE, END_Y = DRAW_Y + BLOCKSIZE;

		ArrayList <Polygon> lightGreen = new ArrayList <Polygon>(), green = new ArrayList <Polygon>(),
				darkGreen = new ArrayList <Polygon>();

		// Top of the pipe (LIGHT)
		int[] x_top_light = {DRAW_X, MID_X, DRAW_X};
		int[] y_top_light = { (DRAW_Y - BLOCKSIZE), (DRAW_Y - BLOCKSIZE), END_Y};
		lightGreen.add(new Polygon(x_top_light, y_top_light, 3));
		// Top of the pipe (MEDIUM)
		int[] x_top_med = {DRAW_X, MID_X, END_X, MID_X};
		int[] y_top_med = {END_Y, (DRAW_Y - BLOCKSIZE), (DRAW_Y - BLOCKSIZE), END_Y};
		green.add(new Polygon(x_top_med, y_top_med, 4));
		// Top of the pipe (DARK)
		int[] x_top_dark = {MID_X, END_X, END_X};
		int[] y_top_dark = {END_Y, END_Y, (DRAW_Y - BLOCKSIZE)};
		darkGreen.add(new Polygon(x_top_dark, y_top_dark, 3));

		// Draw The Pipe
		for (Polygon shape : lightGreen) {
			drawShape(shape, Sprite.COLOR_PIPE_LIGHT, Sprite.COLOR_BORDER);
		}
		for (Polygon shape : green) {
			drawShape(shape, Sprite.COLOR_PIPE_GREEN, Sprite.COLOR_BORDER);
		}
		for (Polygon shape : darkGreen) {
			drawShape(shape, Sprite.COLOR_PIPE_DARK, Sprite.COLOR_BORDER);
		}
	}

	/**
	 * Draw a Vertical Pipe from the bottom left corner
	 * 
	 * @param X
	 *            The x coordinate of the bottom left corner
	 * @param Y
	 *            The y coordinate of the bottom right corner
	 * @param HEIGHT
	 *            The height of the pipe
	 */
	private void drawVerticalPipe(int X, int Y, int HEIGHT) {
		int DRAW_X = X * BLOCKSIZE, DRAW_Y = Y * BLOCKSIZE;
		int END_X = DRAW_X + BLOCKSIZE, END_Y = DRAW_Y + BLOCKSIZE;
		int TOP = (Y - HEIGHT) * BLOCKSIZE;

		ArrayList <Polygon> lightGreen = new ArrayList <Polygon>(), green = new ArrayList <Polygon>(),
				darkGreen = new ArrayList <Polygon>();

		// Body of the pipe (LIGHT)
		int[] x_body_light = { (DRAW_X + EIGTH_BLOCKSIZE), END_X, (DRAW_X + EIGTH_BLOCKSIZE)};
		int[] y_body_light = { (TOP + BLOCKSIZE), (TOP + BLOCKSIZE), END_Y};
		lightGreen.add(new Polygon(x_body_light, y_body_light, 3));
		// Body of the pipe (MEDIUM)
		int[] x_body_med = { (DRAW_X + EIGTH_BLOCKSIZE), END_X, (END_X + BLOCKSIZE - EIGTH_BLOCKSIZE), END_X};
		int[] y_body_med = {END_Y, END_Y, (TOP + BLOCKSIZE), (TOP + BLOCKSIZE)};
		green.add(new Polygon(x_body_med, y_body_med, 4));
		// Body of the pipe (DARK)
		int[] x_body_dark = { (END_X + BLOCKSIZE - EIGTH_BLOCKSIZE), (END_X + BLOCKSIZE - EIGTH_BLOCKSIZE), END_X};
		int[] y_body_dark = { (TOP + BLOCKSIZE), END_Y, END_Y};
		darkGreen.add(new Polygon(x_body_dark, y_body_dark, 3));

		// Airspace to the left and right
		int[] x_airspace_left = {DRAW_X, DRAW_X, (DRAW_X + EIGTH_BLOCKSIZE), (DRAW_X + EIGTH_BLOCKSIZE)};
		int[] x_airspace_right = { (END_X + BLOCKSIZE), (END_X + BLOCKSIZE), (END_X + BLOCKSIZE - EIGTH_BLOCKSIZE),
				(END_X + BLOCKSIZE - EIGTH_BLOCKSIZE)};
		int[] y_airspace = {END_Y, (TOP + BLOCKSIZE), (TOP + BLOCKSIZE), END_Y};
		skyPolygons.add(new Polygon(x_airspace_left, y_airspace, 4));
		skyPolygons.add(new Polygon(x_airspace_right, y_airspace, 4));

		// Draw The Pipe
		for (Polygon shape : lightGreen) {
			drawShape(shape, Sprite.COLOR_PIPE_LIGHT, Sprite.COLOR_BORDER);
		}
		for (Polygon shape : green) {
			drawShape(shape, Sprite.COLOR_PIPE_GREEN, Sprite.COLOR_BORDER);
		}
		for (Polygon shape : darkGreen) {
			drawShape(shape, Sprite.COLOR_PIPE_DARK, Sprite.COLOR_BORDER);
		}
	}

	/**
	 * Draw a Horizontal Pipe starting from the bottom right corner
	 * 
	 * @param X
	 *            The x coordinate of the bottom right corner
	 * @param Y
	 *            The y coordinate of the bottom right corner
	 * @param LENGTH
	 *            The length of the pipe
	 */
	private void drawHorizontalPipe(int X, int Y, int LENGTH) {
		int DRAW_X = X * BLOCKSIZE, DRAW_Y = Y * BLOCKSIZE;
		int END_X = DRAW_X + BLOCKSIZE, END_Y = DRAW_Y + BLOCKSIZE;
		int SIDE_X = (X - LENGTH) * BLOCKSIZE + BLOCKSIZE;
		int MID_X = SIDE_X + (int) ( (LENGTH / 2.0) * BLOCKSIZE);

		ArrayList <Polygon> lightGreen = new ArrayList <Polygon>(), green = new ArrayList <Polygon>(),
				darkGreen = new ArrayList <Polygon>();

		// Body of the pipe (LIGHT)
		int[] x_body_light = {SIDE_X, MID_X, SIDE_X};
		int[] y_body_light = { (DRAW_Y - BLOCKSIZE + EIGTH_BLOCKSIZE), (DRAW_Y - BLOCKSIZE + EIGTH_BLOCKSIZE),
				(END_Y - EIGTH_BLOCKSIZE)};
		lightGreen.add(new Polygon(x_body_light, y_body_light, 3));
		// Body of the pipe (MEDIUM)
		int[] x_body_med = {SIDE_X, MID_X, END_X, MID_X};
		int[] y_body_med = { (END_Y - EIGTH_BLOCKSIZE), (DRAW_Y - BLOCKSIZE + EIGTH_BLOCKSIZE),
				(DRAW_Y - BLOCKSIZE + EIGTH_BLOCKSIZE), (END_Y - EIGTH_BLOCKSIZE)};
		green.add(new Polygon(x_body_med, y_body_med, 4));
		// Body of the pipe (DARK)
		int[] x_body_dark = {MID_X, END_X, END_X};
		int[] y_body_dark = { (END_Y - EIGTH_BLOCKSIZE), (END_Y - EIGTH_BLOCKSIZE),
				(DRAW_Y - BLOCKSIZE + EIGTH_BLOCKSIZE)};
		darkGreen.add(new Polygon(x_body_dark, y_body_dark, 3));

		// Airspace above and below
		int[] x_airspace = {SIDE_X, SIDE_X, END_X, END_X};
		int[] y_airspace_above = { (DRAW_Y - BLOCKSIZE), (DRAW_Y - BLOCKSIZE + EIGTH_BLOCKSIZE),
				(DRAW_Y - BLOCKSIZE + EIGTH_BLOCKSIZE), (DRAW_Y - BLOCKSIZE)};
		int[] y_airspace_below = {END_Y, (END_Y - EIGTH_BLOCKSIZE), (END_Y - EIGTH_BLOCKSIZE), END_Y};
		skyPolygons.add(new Polygon(x_airspace, y_airspace_above, 4));
		skyPolygons.add(new Polygon(x_airspace, y_airspace_below, 4));

		// Draw The Pipe
		for (Polygon shape : lightGreen) {
			drawShape(shape, Sprite.COLOR_PIPE_LIGHT, Sprite.COLOR_BORDER);
		}
		for (Polygon shape : green) {
			drawShape(shape, Sprite.COLOR_PIPE_GREEN, Sprite.COLOR_BORDER);
		}
		for (Polygon shape : darkGreen) {
			drawShape(shape, Sprite.COLOR_PIPE_DARK, Sprite.COLOR_BORDER);
		}
	}

	/**
	 * Create a rectangular Polygon where the bottom right corner is XY and it
	 * covers all same blocks
	 * 
	 * @param X
	 *            The starting x coordinate
	 * @param Y
	 *            The starting y coordinate
	 * @return The resulting rectangle Polygon
	 */
	private Polygon findUpLeftRect(int X, int Y) {
		/*
		 * Rectangle will be structured in the following way:
		 * (x_points[2],y_points[2]) - (x_points[1],y_points[1]) | |
		 * (x_points[3],y_points[3]) - (x_points[0],y_points[0])
		 */
		int BLOCK_ID = level[Y][X];
		int tempX = X, tempY = Y;
		int length = 1, height = 1;
		while (tempX > 0 && level[tempY][tempX - 1] == BLOCK_ID){
			length++ ;
			tempX-- ;
		}
		while (tempY > 0 && rowValidAbove(X, tempY, length,BLOCK_ID)) {
			height++ ;
			tempY-- ;
		}
		int xBR = (X + 1) * BLOCKSIZE, xTR = xBR;
		int yBR = (Y + 1) * BLOCKSIZE, yBL = yBR;
		int yTR = ( (Y + 1) - height) * BLOCKSIZE, yTL = yTR;
		int xTL = ( (X + 1) - length) * BLOCKSIZE, xBL = xTL;
		int[] x_points = {xBR, xTR, xTL, xBL}, y_points = {yBR, yTR, yTL, yBL};

		return new Polygon(x_points, y_points, 4);
	}

	/**
	 * Return whether a strip of length LENGTH above XY & moving left is the
	 * same as the strip at XY & moving left
	 * 
	 * @param X
	 *            The starting x coordinate
	 * @param Y
	 *            The starting y coordinate
	 * @param LENGTH
	 *            The length of the strip moving left
	 * @return Whether the strips are equivalent
	 */
	private boolean rowValidAbove(int X, int Y, int LENGTH, int ID) {
		boolean result = true;
		for (int i = 0; i < LENGTH; i++ ) {
			if (X - i < 0 || Y - 1 < 0) return false;
			result = result && (level[Y-1][X-i] == ID);
			//result = result && (level[Y][X - i] == level[Y - 1][X - i]);
			if (result != true) {
				return result;
			}
		}
		return result;
	}

	//TODO: Optimize the sky
	/**
	 * Draw the sky polygons
	 */
	private void drawSky() {
		for (Polygon poly : skyPolygons) {
			drawShape(poly, Sprite.COLOR_SKY, Sprite.COLOR_BORDER);
		}
	}

	/**
	 * Draw a shape on the canvas
	 * 
	 * @param POLY
	 *            The shape to draw
	 * @param FILL
	 *            The fill color
	 * @param BORDER
	 *            The border color
	 */
	private void drawShape(Polygon POLY, Color FILL, Color BORDER) {
		g2D.setPaint(FILL);
		g2D.fill(POLY);
		g2D.setColor(BORDER);
		g2D.drawPolygon(POLY);
	}

	/**
	 * Outputs the quilt to a file noted by FILENAME
	 * 
	 * @param FILENAME
	 *            The name of the file to output to
	 * @throws IOException
	 */
	public void outputImageToFile(String FILENAME) throws IOException {
		File f = new File(FILENAME);
		if ( !ImageIO.write(output_image, "PNG", f)) {
			throw new RuntimeException("Unexpected error writing image");
		}
	}

	/**
	 * Generate the Quilt
	 * 
	 * @param VERBOSE
	 *            What level of detail to print to the console with. 0 for none,
	 *            1 for all.
	 * @throws IOException
	 */
	public void generate(int VERBOSE, String Filename){
		int marioX = -1, marioY = -1;
		int ADJ = 0; // This integer stores whether adjacent blocks share the same ID
		boolean up_upright = false; // Whether the block one space right and two spaces up shares the same ID as this
		boolean skyDiamonds = skyDraw.equals("Diamonds");
		skyPolygons = new ArrayList <Polygon>();
		if (skyDraw.equals("NONE")){
			int[] xCanvas = {0,(level[0].length) * BLOCKSIZE,(level[0].length) * BLOCKSIZE,0};
			int[] yCanvas = {0,0,(level.length) * BLOCKSIZE,(level.length) * BLOCKSIZE};
			drawShape(new Polygon(xCanvas,yCanvas,4),Sprite.COLOR_SKY,Sprite.COLOR_BORDER);
		}
		for (int y = 0; y < level.length; y++ ) {
			for (int x = 0; x < level[0].length; x++ ) {
				int thisID = level[y][x], tempID = -2;
				ADJ = 0;
				up_upright = false;
				if (x > 0){
					tempID = level[y][x - 1];
					if ((tempID == thisID) ||
						(skyDiamonds && ((tempID == Sprite.ID_COIN && thisID == Sprite.ID_AIR) ||
						(tempID == Sprite.ID_AIR && thisID == Sprite.ID_COIN)))){
						ADJ = ADJ | ML;
					}
				} // LEFT
				if (x < level[0].length - 1){
					tempID = level[y][x + 1];
					if ((tempID == thisID) ||
						(skyDiamonds && ((tempID == Sprite.ID_COIN && thisID == Sprite.ID_AIR) ||
						(tempID == Sprite.ID_AIR && thisID == Sprite.ID_COIN)))){
						ADJ = ADJ | MR;
					}
				} // RIGHT
				if (y > 0){
					tempID = level[y-1][x];
					if ((tempID == thisID) ||
						(skyDiamonds && ((tempID == Sprite.ID_COIN && thisID == Sprite.ID_AIR) ||
						(tempID == Sprite.ID_AIR && thisID == Sprite.ID_COIN)))){
						ADJ = ADJ | TM;
					}
				} // UP
				if (y < level.length-1){
					tempID = level[y+1][x];
					if ((tempID == thisID) ||
						(skyDiamonds && ((tempID == Sprite.ID_COIN && thisID == Sprite.ID_AIR) ||
						(tempID == Sprite.ID_AIR && thisID == Sprite.ID_COIN)))){
						ADJ = ADJ | BM;
					}
				} // DOWN
				if (y > 0 && x > 0){
					tempID = level[y-1][x-1];
					if ((tempID == thisID) ||
						(skyDiamonds && ((tempID == Sprite.ID_COIN && thisID == Sprite.ID_AIR) ||
						(tempID == Sprite.ID_AIR && thisID == Sprite.ID_COIN)))){
						ADJ = ADJ | TL;
					}
				} // UPLEFT
				if (y > 0 && x < level[0].length - 1){
					tempID = level[y-1][x+1];
					if ((tempID == thisID) ||
						(skyDiamonds && ((tempID == Sprite.ID_COIN && thisID == Sprite.ID_AIR) ||
						(tempID == Sprite.ID_AIR && thisID == Sprite.ID_COIN)))){
						ADJ = ADJ | TR;
					}
				} // UPRIGHT
				if (y < level.length - 1 && x > 0){
					tempID = level[y+1][x-1];
					if ((tempID == thisID) ||
						(skyDiamonds && ((tempID == Sprite.ID_COIN && thisID == Sprite.ID_AIR) ||
						(tempID == Sprite.ID_AIR && thisID == Sprite.ID_COIN)))){
						ADJ = ADJ | BL;
					}
				} // DOWNLEFT
				if (y < level.length - 1 && x < level[0].length - 1){
					tempID = level[y+1][x];
					if ((tempID == thisID) ||
						(skyDiamonds && ((tempID == Sprite.ID_COIN && thisID == Sprite.ID_AIR) ||
						(tempID == Sprite.ID_AIR && thisID == Sprite.ID_COIN)))){
						ADJ = ADJ | BR;
					}
				} // DOWNRIGHT
				if (y > 1 && x < level[0].length - 1){
					tempID = level[y-2][x+1];
					if ((tempID == thisID) ||
						(skyDiamonds && ((tempID == Sprite.ID_COIN && thisID == Sprite.ID_AIR) ||
						(tempID == Sprite.ID_AIR && thisID == Sprite.ID_COIN)))){
						up_upright = true;
					}
				} // UPUPRIGHT
				switch (thisID) {
					case Sprite.ID_QUESTION_BLOCK: // When a question block is discovered
						if (VERBOSE > 0) System.out.println("Question Block at (" + y + "," + x + ")");
						drawQuestionBlock(x, y, ADJ, up_upright);
						break;
					case Sprite.ID_MUSIC_NOTE: // When a music note block is discovered
						if (VERBOSE > 0) System.out.println("Music Note Block at (" + y + "," + x + ")");
						drawMusicBlock(x, y, ADJ);
						break;
					case Sprite.ID_FACE_YELLOW: // When a yellow face block is discovered
						if (VERBOSE > 0) System.out.println("Yellow Face Block at (" + y + "," + x + ")");
						drawFaceBlock(x, y, ADJ, false);
						break;
					case Sprite.ID_FACE_BROWN: // When a brown face block is discovered
						if (VERBOSE > 0) System.out.println("Brown Face Block at (" + y + "," + x + ")");
						int tempYBrown = y;
						int tempHeightBrown = 1;
						while (tempYBrown > 0 && level[tempYBrown - 1][x] == Sprite.ID_FACE_BROWN) {
							tempHeightBrown++ ;
							tempYBrown-- ;
						}
						int tempXBrown = x;
						int tempLengthBrown = 1;
						while (tempXBrown > 0 && level[y][tempXBrown - 1] == Sprite.ID_FACE_BROWN) {
							tempLengthBrown++ ;
							tempXBrown-- ;
						}
						drawBrownFaceBlock(x, y, ADJ, tempLengthBrown, tempHeightBrown);
						break;
					case Sprite.ID_CLOUD: // When a cloud block is discovered
						if (VERBOSE > 0) System.out.println("Cloud Block at (" + y + "," + x + ")");
						drawFaceBlock(x, y, ADJ, true);
						break;
					case Sprite.ID_MARIO:
						if (VERBOSE > 0) System.out.println("Mario at (" + y + "," + x + ")");
						marioX = x;
						marioY = y;
						break;
					case Sprite.ID_COIN:
						if (VERBOSE > 0) System.out.println("Coin at (" + y + "," + x + ")");
						drawCoin(x, y, ADJ, up_upright);
						break;
					case Sprite.ID_PIPE_VERTI_TL: // When the cap of a vertical pipe is discovered
					case Sprite.ID_PIPE_VERTI_BL:
						if (VERBOSE > 0) System.out.println("Left of Vertical Pipe Cap at (" + y + "," + x + ")");
						drawVerticalPipeEnd(x, y);
						break;
					case Sprite.ID_PIPE_VERTI_ML: // When the left part of a vertical pipe body is discovered
						int tempYPipe = y;
						int tempHeightPipe = 1;
						while (tempYPipe > 0 && level[tempYPipe - 1][x] == Sprite.ID_PIPE_VERTI_ML) {
							tempHeightPipe++ ;
							tempYPipe-- ;
						}
						drawVerticalPipe(x, y, tempHeightPipe);
						break;
					case Sprite.ID_PIPE_VERTI_TR: // This pipe will be drawn over but should not act as air
					case Sprite.ID_PIPE_VERTI_MR: // This pipe will be drawn over but should not act as air
					case Sprite.ID_PIPE_VERTI_BR: // This pipe will be drawn over but should not act as air
						break;
					case Sprite.ID_PIPE_HORIZ_BL: // When the cap of a horizontal pipe is discovered
					case Sprite.ID_PIPE_HORIZ_BR:
						if (VERBOSE > 0) System.out.println("Bottom of Horizontal Pipe Cap at (" + y + "," + x + ")");
						drawHorizontalPipeEnd(x, y);
						break;
					case Sprite.ID_PIPE_HORIZ_BM: // When the bottom part of a horizontal pipe body is discovered
						int tempXPipe = x;
						int tempLengthPipe = 1;
						while (tempXPipe > 0 && level[y][tempXPipe - 1] == Sprite.ID_PIPE_HORIZ_BM) {
							tempLengthPipe++ ;
							tempXPipe-- ;
						}
						drawHorizontalPipe(x, y, tempLengthPipe);
					case Sprite.ID_PIPE_HORIZ_TL: // This pipe will be drawn over but should not act as air
					case Sprite.ID_PIPE_HORIZ_TM: // This pipe will be drawn over but should not act as air
					case Sprite.ID_PIPE_HORIZ_TR: // This pipe will be drawn over but should not act as air
						break;
					case Sprite.ID_AIR:
						if (skyDraw.equals("Diamonds")){ //If drawing with centered diamonds
							drawCenteredDiamondBlock(x,y,ADJ,up_upright,Sprite.COLOR_SKY,Sprite.COLOR_SKY,false);
						} else if (skyDraw.equals("Dirt")){ //If drawing like dirt
							drawLikeDirt(x,y,ADJ,Sprite.COLOR_SKY,Sprite.COLOR_SKY);
						} else if (skyDraw.equals("Rectangles")){ //If triangulating
							skyPolygons.add(findUpLeftRect(x, y));
						}
						break;
					case Sprite.ID_ROCK:
						drawLikeDirt(x,y,ADJ,Sprite.COLOR_ROCK_DARK,Sprite.COLOR_ROCK_LIGHT);
					case Sprite.ID_GRASS_TL:
					case Sprite.ID_GRASS_TM:
					case Sprite.ID_GRASS_TR:
					case Sprite.ID_GRASS_ML:
					case Sprite.ID_GRASS_MR:
					case Sprite.ID_GRASS_BL:
					case Sprite.ID_GRASS_BM:
					case Sprite.ID_GRASS_BR:
						drawGrass(x, y);
						break;
					case Sprite.ID_DIRT1: // dirt (Alternatively ID_GRASS_MM)
					case Sprite.ID_DIRT2:
					case Sprite.ID_DIRT3:
						drawDirt(x, y, ADJ);
						break;
					default:
						int[] xTemp = {x * BLOCKSIZE, x * BLOCKSIZE, (x + 1) * BLOCKSIZE, (x + 1) * BLOCKSIZE};
						int[] yTemp = {y * BLOCKSIZE, (y + 1) * BLOCKSIZE, (y + 1) * BLOCKSIZE, y * BLOCKSIZE};
						drawShape(new Polygon(xTemp, yTemp, 4), Color.WHITE, Sprite.COLOR_BORDER);
						break;
				}
			}
		}
		drawMario(marioX, marioY);

		drawSky();

		try {
			outputImageToFile(Filename);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
