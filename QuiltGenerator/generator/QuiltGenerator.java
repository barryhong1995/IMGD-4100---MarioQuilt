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

	private int[][] level;
	private int world = 0;
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

	// TODO: Draw Grass (TR, ML, MR, BM) - See: Graph paper pg. 11
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
				if (into_down){
					if (into_right){ //rhombus into right and down
						int[] x4TM = {DRAW_X,QUARTER_X,(END_X + HALF_BLOCKSIZE),(END_X + QUARTER_BLOCKSIZE)};
						int[] y4TM = {(END_Y + HALF_BLOCKSIZE),(END_Y + HALF_BLOCKSIZE + QUARTER_BLOCKSIZE),MID_Y,QUARTER_Y};
						dirt_light.add(new Polygon(x4TM,y4TM,4));
					} else { //rhombus into down only
						int[] x4TM = {DRAW_X,QUARTER_X,END_X,END_X};
						int[] y4TM = {(END_Y + HALF_BLOCKSIZE),(END_Y + HALF_BLOCKSIZE + QUARTER_BLOCKSIZE),END_Y,MID_Y};
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
				//Middle Dark Dirt
				if (into_down){
					if (into_downleft){ //rhombus into down and downleft
						int[] x5TM = {(DRAW_X - HALF_BLOCKSIZE),(DRAW_X - QUARTER_BLOCKSIZE),END_X,THREE_X};
						int[] y5TM = {(END_Y + HALF_BLOCKSIZE),(END_Y + HALF_BLOCKSIZE + QUARTER_BLOCKSIZE),MID_Y,QUARTER_Y};
						dirt_dark.add(new Polygon(x5TM,y5TM,4));
					} else { //rhombus into down
						int[] x5TM = {DRAW_X,DRAW_X,END_X,THREE_X};
						int[] y5TM = {END_Y,(END_Y+HALF_BLOCKSIZE),MID_Y,QUARTER_Y};
						dirt_dark.add(new Polygon(x5TM,y5TM,4));
					}
				} else { //rhombus into none
					int[] x5TM = {DRAW_X,MID_X,END_X,THREE_X};
					int[] y5TM = {END_Y,END_Y,MID_Y,QUARTER_Y};
					dirt_dark.add(new Polygon(x5TM,y5TM,4));
				}
				//Left Light Dirt
				if (into_left){
					if (into_downleft){ //rhombus into left and downleft
						if (DOWNLEFT == Sprite.ID_GRASS_ML || DOWNLEFT == Sprite.ID_GRASS_BL){ //not as deep
							int[] x6TM = {(DRAW_X - HALF_BLOCKSIZE - QUARTER_BLOCKSIZE),(DRAW_X - HALF_BLOCKSIZE),MID_X,QUARTER_X};
							int[] y6TM = {(END_Y + QUARTER_BLOCKSIZE),(END_Y + HALF_BLOCKSIZE),MID_Y,QUARTER_Y};
							dirt_light.add(new Polygon(x6TM,y6TM,4));
						} else { //can go deeper
							int[] x6TM = {(DRAW_X - BLOCKSIZE),(DRAW_X - HALF_BLOCKSIZE - QUARTER_BLOCKSIZE),MID_X,QUARTER_X};
							int[] y6TM = {(END_Y + HALF_BLOCKSIZE),(END_Y + HALF_BLOCKSIZE + QUARTER_BLOCKSIZE),MID_Y,QUARTER_Y};
							dirt_light.add(new Polygon(x6TM,y6TM,4));
						}
					} else { //rhombus into left
						int[] x6TM = {(DRAW_X - HALF_BLOCKSIZE),DRAW_X,MID_X,QUARTER_X};
						int[] y6TM = {END_Y,END_Y,MID_Y,QUARTER_Y};
						dirt_light.add(new Polygon(x6TM,y6TM,4));
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
				if (RIGHT == Sprite.ID_GRASS_TM) into_right = true;
				if (DOWN == Sprite.ID_GRASS_ML || DOWN == Sprite.ID_GRASS_BL) into_down = true;
				////Shapes that happen regardless of adjacency:
				
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
				if (into_up && into_right && (UPRIGHT == Sprite.ID_GRASS_TM || UPRIGHT == Sprite.ID_GRASS_TR || UPRIGHT == Sprite.ID_GRASS_MR)) into_upright = true;
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
							int[] x4BL = {QUARTER_X,MID_X,(END_X + HALF_BLOCKSIZE + QUARTER_BLOCKSIZE),(END_X + HALF_BLOCKSIZE)};
							int[] y4BL = {QUARTER_Y,MID_Y,(DRAW_Y - HALF_BLOCKSIZE - QUARTER_BLOCKSIZE),(DRAW_Y - BLOCKSIZE)};
							dirt_light.add(new Polygon(x4BL,y4BL,4));
						} else { //not as deep
							int[] x4BL = {QUARTER_X,MID_X,(END_X + HALF_BLOCKSIZE),(END_X + QUARTER_BLOCKSIZE)};
							int[] y4BL = {QUARTER_Y,MID_Y,(DRAW_Y - HALF_BLOCKSIZE),(DRAW_Y - HALF_BLOCKSIZE - QUARTER_BLOCKSIZE)};
							dirt_light.add(new Polygon(x4BL,y4BL,4));
						}
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
							int[] x5BL = {MID_X,THREE_X,(END_X + HALF_BLOCKSIZE + QUARTER_BLOCKSIZE),(END_X + HALF_BLOCKSIZE)};
							int[] y5BL = {MID_Y,THREE_Y,(DRAW_Y - QUARTER_BLOCKSIZE),(DRAW_Y - HALF_BLOCKSIZE)};
							dirt_dark.add(new Polygon(x5BL,y5BL,4));
						} else { //deeper
							int[] x5BL = {MID_X,THREE_X,(END_X + BLOCKSIZE),(END_X + HALF_BLOCKSIZE + QUARTER_BLOCKSIZE)};
							int[] y5BL = {MID_Y,THREE_Y,(DRAW_Y - HALF_BLOCKSIZE),(DRAW_Y - HALF_BLOCKSIZE - QUARTER_BLOCKSIZE)};
							dirt_dark.add(new Polygon(x5BL,y5BL,4));
						}
						
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
				(DRAW_Y - HALF_BLOCKSIZE - QUARTER_BLOCKSIZE)};
		redShapes.add(new Polygon(x_hatLeft, y_hatLeft, 3));
		// Middle hat triangle
		int[] x_hatMid = {DRAW_X, MID_X, END_X};
		int[] y_hatMid = { (DRAW_Y - BLOCKSIZE), (DRAW_Y - HALF_BLOCKSIZE - QUARTER_BLOCKSIZE), (DRAW_Y - BLOCKSIZE)};
		redShapes.add(new Polygon(x_hatMid, y_hatMid, 3));
		// Right hat triangle
		int[] x_hatRight = {END_X, END_X, MID_X};
		int[] y_hatRight = { (DRAW_Y - HALF_BLOCKSIZE), (DRAW_Y - BLOCKSIZE),
				(DRAW_Y - HALF_BLOCKSIZE - QUARTER_BLOCKSIZE)};
		redShapes.add(new Polygon(x_hatRight, y_hatRight, 3));
		// Mario's hair
		int[] x_marioHair = {DRAW_X, MID_X, END_X};
		int[] y_marioHair = { (DRAW_Y - HALF_BLOCKSIZE), (DRAW_Y - HALF_BLOCKSIZE - QUARTER_BLOCKSIZE),
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
	private void drawQuestionBlock(int X, int Y, int ADJ, boolean up_upright) throws IOException {
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
			Color color_diamond, boolean isCoin) throws IOException {
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
			int[] y4 = {END_Y, DRAW_Y, DRAW_Y, END_Y};
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
		// {TESTED}
		/*
		 * ____ ____ ____ | | | /\ | |____|____/ \/_| | | /\/ /\ | |____|_\/
		 * /\/_| | | /\ / | |____|/\/_|____|
		 */
		if (up && upright && up_upright) {
			// draw big rhombus across this, up, upright, and up_upright
			int[] xthis = {MID_X, (END_X + QUARTER_BLOCKSIZE), (END_X + HALF_BLOCKSIZE), THREE_X};
			int[] ythis = {DRAW_Y, (DRAW_Y - BLOCKSIZE - HALF_BLOCKSIZE), (DRAW_Y - BLOCKSIZE), MID_Y};
			triangles.add(new Polygon(xthis, ythis, 4));
		}
		/*
		 * TL:001 TM:002 TR:004 ML:008 { me } MR:016 BL:032 BM:064 BR:0128
		 */
		/*
		 * ____ ____ ____ | /\ | /\ | /\ | |_\/_|_\/_|_\/_| | /\ | /\ | /\ |
		 * |_\/_|_\/_|_\/_| | /\ | /\ | /\ | |_\/_|_\/_|_\/_|
		 */
		if (isCoin) {
			skyPolygons.addAll(triangles);
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
			int[] y1 = { (END_Y + QUARTER_BLOCKSIZE), THREE_Y, END_Y, (END_Y + HALF_BLOCKSIZE + QUARTER_BLOCKSIZE)};
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
	private void drawCoin(int X, int Y, int ADJ, boolean up_upright) throws IOException {
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
		while (tempX > 0 && level[tempY][tempX - 1] == BLOCK_ID) {
			length++ ;
			tempX-- ;
		}
		while (tempY > 0 && rowValidAbove(X, tempY, length)) {
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
	private boolean rowValidAbove(int X, int Y, int LENGTH) {
		boolean result = true;
		for (int i = 0; i < LENGTH; i++ ) {
			if (X - i < 0 || Y - 1 < 0) return false;
			result = result && (level[Y][X - i] == level[Y - 1][X - i]);
			if (result != true) {
				return result;
			}
		}
		return result;
	}

	// TODO: You need to optimize the polygons before you can triangulate them. Learn from your misakes Aidan.
	// Do not suffer the same way twice. 3:00 AM Aidan believes in you. Don't let ther faith in you go to waste
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
	public void generate(int VERBOSE) throws IOException {
		int marioX = -1, marioY = -1;
		int ADJ = 0; // This integer stores whether adjacent blocks share the same ID
		boolean up_upright = false; // Whether the block one space right and two spaces up shares the same ID as this
		skyPolygons = new ArrayList <Polygon>();
		for (int y = 0; y < level.length; y++ ) {
			for (int x = 0; x < level[0].length; x++ ) {
				int thisID = level[y][x];
				ADJ = 0;
				up_upright = false;
				if (x > 0 && level[y][x - 1] == thisID) {
					ADJ = ADJ | ML;
				} // LEFT
				if (x < level[0].length - 1 && level[y][x + 1] == thisID) {
					ADJ = ADJ | MR;
				} // RIGHT
				if (y > 0 && level[y - 1][x] == thisID) {
					ADJ = ADJ | TM;
				} // UP
				if (y < level.length - 1 && level[y + 1][x] == thisID) {
					ADJ = ADJ | BM;
				} // DOWN
				if (y > 0 && x > 0 && level[y - 1][x - 1] == thisID) {
					ADJ = ADJ | TL;
				} // UPLEFT
				if (y > 0 && x < level[0].length - 1 && level[y - 1][x + 1] == thisID) {
					ADJ = ADJ | TR;
				} // UPRIGHT
				if (y < level.length - 1 && x > 0 && level[y + 1][x - 1] == thisID) {
					ADJ = ADJ | BL;
				} // DOWNLEFT
				if (y < level.length - 1 && x < level[0].length - 1 && level[y + 1][x + 1] == thisID) {
					ADJ = ADJ | BR;
				} // DOWNRIGHT
				if (y > 1 && x < level[0].length - 1 && level[y - 2][x + 1] == thisID) {
					up_upright = true;
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
						skyPolygons.add(findUpLeftRect(x, y));
						break;
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
					case Sprite.ID_GRASS_MM: // dirt
						// break;
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

		outputImageToFile("output.png");
	}

	/**
	 * TEST MAIN
	 * 
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		int VERBOSE = 0;
		int[][] LEVEL = new int[10][15];
		for (int i = 0; i < LEVEL.length; i++ ) {
			for (int j = 0; j < LEVEL[0].length; j++ ) {
				LEVEL[i][j] = 0;
			}
		}

		int q = Sprite.ID_QUESTION_BLOCK;
		int n = Sprite.ID_MUSIC_NOTE;
		int y = Sprite.ID_FACE_YELLOW;
		int b = Sprite.ID_FACE_BROWN;
		int c = Sprite.ID_CLOUD;
		int d = Sprite.ID_COIN;
		// Question Mark Blocks
		LEVEL[0][0] = q;
		LEVEL[0][1] = q;
		LEVEL[0][2] = q;
		LEVEL[1][0] = q;
		LEVEL[1][1] = q;
		LEVEL[1][2] = q;
		LEVEL[2][0] = q;
		LEVEL[2][1] = q;
		LEVEL[2][2] = q;
		//2x2 square of grass
		LEVEL[3][0] = Sprite.ID_GRASS_TL;	LEVEL[3][1] = Sprite.ID_GRASS_TR;
		LEVEL[4][0] = Sprite.ID_GRASS_BL;	LEVEL[4][1] = Sprite.ID_GRASS_BR;
		
		// Musical Note Blocks
		LEVEL[0][3] = n;
		LEVEL[0][4] = n;
		LEVEL[0][5] = n;
		LEVEL[1][3] = n;
		LEVEL[1][4] = n;
		LEVEL[1][5] = n;
		LEVEL[2][3] = n;
		LEVEL[2][4] = n;
		LEVEL[2][5] = n;
		// Yellow Face Blocks
		LEVEL[0][6] = y;
		LEVEL[0][7] = y;
		LEVEL[0][8] = y;
		LEVEL[1][6] = y;
		LEVEL[1][7] = y;
		LEVEL[1][8] = y;
		LEVEL[2][6] = y;
		LEVEL[2][7] = y;
		LEVEL[2][8] = y;
		// Brown Face Blocks
		LEVEL[0][9] = b;
		LEVEL[0][10] = b;
		LEVEL[0][11] = b;
		LEVEL[1][9] = b;
		LEVEL[1][10] = b;
		LEVEL[1][11] = b;
		LEVEL[2][9] = b;
		LEVEL[2][10] = b;
		LEVEL[2][11] = b;
		// Cloud Blocks
		LEVEL[0][12] = c;
		LEVEL[0][13] = c;
		LEVEL[0][14] = c;
		LEVEL[1][12] = c;
		LEVEL[1][13] = c;
		LEVEL[1][14] = c;
		LEVEL[2][12] = c;
		LEVEL[2][13] = c;
		LEVEL[2][14] = c;
		// Mario
		LEVEL[6][2] = Sprite.ID_MARIO;
		LEVEL[5][2] = Sprite.ID_MARIO;
		// Coins
		LEVEL[6][3] = d;
		LEVEL[6][4] = d;
		LEVEL[6][5] = d;
		// Pipe going up
		LEVEL[7][6] = Sprite.ID_PIPE_VERTI_TL;
		LEVEL[7][7] = Sprite.ID_PIPE_VERTI_TR;
		LEVEL[8][6] = Sprite.ID_PIPE_VERTI_ML;
		LEVEL[8][7] = Sprite.ID_PIPE_VERTI_MR;
		LEVEL[9][6] = Sprite.ID_PIPE_VERTI_ML;
		LEVEL[9][7] = Sprite.ID_PIPE_VERTI_MR;
		// Pipe going left
		LEVEL[3][7] = Sprite.ID_PIPE_HORIZ_TL;
		LEVEL[3][8] = Sprite.ID_PIPE_HORIZ_TM;
		LEVEL[3][9] = Sprite.ID_PIPE_HORIZ_TM;
		LEVEL[4][7] = Sprite.ID_PIPE_HORIZ_BL;
		LEVEL[4][8] = Sprite.ID_PIPE_HORIZ_BM;
		LEVEL[4][9] = Sprite.ID_PIPE_HORIZ_BM;
		// Pipe going right
		LEVEL[5][7] = Sprite.ID_PIPE_HORIZ_TM;
		LEVEL[5][8] = Sprite.ID_PIPE_HORIZ_TM;
		LEVEL[5][9] = Sprite.ID_PIPE_HORIZ_TR;
		LEVEL[6][7] = Sprite.ID_PIPE_HORIZ_BM;
		LEVEL[6][8] = Sprite.ID_PIPE_HORIZ_BM;
		LEVEL[6][9] = Sprite.ID_PIPE_HORIZ_BR;
		// Pipe going down
		LEVEL[7][8] = Sprite.ID_PIPE_VERTI_ML;
		LEVEL[7][9] = Sprite.ID_PIPE_VERTI_MR;
		LEVEL[8][8] = Sprite.ID_PIPE_VERTI_ML;
		LEVEL[8][9] = Sprite.ID_PIPE_VERTI_MR;
		LEVEL[9][8] = Sprite.ID_PIPE_VERTI_BL;
		LEVEL[9][9] = Sprite.ID_PIPE_VERTI_BR;
		// Ground
		LEVEL[7][0] = Sprite.ID_GRASS_TL;
		LEVEL[7][1] = Sprite.ID_GRASS_TM;
		LEVEL[7][2] = Sprite.ID_GRASS_TM;
		LEVEL[7][3] = Sprite.ID_GRASS_TM;
		LEVEL[7][4] = Sprite.ID_GRASS_TM;
		LEVEL[7][5] = Sprite.ID_GRASS_TR;

		LEVEL[8][0] = Sprite.ID_GRASS_BL;
		LEVEL[8][1] = Sprite.ID_GRASS_BM;
		LEVEL[8][2] = Sprite.ID_GRASS_BM;
		LEVEL[8][3] = Sprite.ID_GRASS_BM;
		LEVEL[8][4] = Sprite.ID_GRASS_BM;
		LEVEL[8][5] = Sprite.ID_GRASS_BR;
		
		LEVEL[3][10] = Sprite.ID_GRASS_TL;	LEVEL[3][11] = Sprite.ID_GRASS_TR;
		LEVEL[4][10] = Sprite.ID_GRASS_ML;	LEVEL[4][11] = Sprite.ID_GRASS_MR;
		LEVEL[5][10] = Sprite.ID_GRASS_ML;	LEVEL[5][11] = Sprite.ID_GRASS_MR;
		LEVEL[6][10] = Sprite.ID_GRASS_ML;	LEVEL[6][11] = Sprite.ID_GRASS_MR;
		LEVEL[7][10] = Sprite.ID_GRASS_BL;	LEVEL[7][11] = Sprite.ID_GRASS_BR;
		QuiltGenerator qG = new QuiltGenerator(LEVEL);
		qG.generate(VERBOSE);

	}
}
