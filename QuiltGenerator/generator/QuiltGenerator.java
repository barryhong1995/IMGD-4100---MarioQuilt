package generator;
import java.awt.*;
import java.awt.image.*;
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
	private final static int HALF_BLOCKSIZE = BLOCKSIZE/2;
	private final static int QUARTER_BLOCKSIZE = BLOCKSIZE/4;
	private final static int EIGTH_BLOCKSIZE = BLOCKSIZE/8;
		
	private int[][] level;
	private int world = 0;
	private BufferedImage output_image;
	private Graphics2D g2D;
	
	/**
	 * Instantiate a new QuiltGenerator with the level LEVEL and default world = 0
	 * @param LEVEL The level to generate a quilt from
	 */
	public QuiltGenerator(int[][] LEVEL){
		level = LEVEL;
		output_image = new BufferedImage(LEVEL.length * BLOCKSIZE,
										LEVEL[0].length * BLOCKSIZE,
										BufferedImage.TYPE_INT_RGB);
		g2D = output_image.createGraphics();
	}
	
	/**
	 * Instantiate a new QuiltGenerator with level LEVEL and world WORLD
	 * @param LEVEL The level to generate a quilt from
	 * @param WORLD Which world (0-5) to generate the quilt as
	 */
	public QuiltGenerator(int[][] LEVEL, int WORLD){
		level = LEVEL;
		world = WORLD;
		output_image = new BufferedImage(LEVEL.length * BLOCKSIZE,
				LEVEL[0].length * BLOCKSIZE,
				BufferedImage.TYPE_INT_RGB);
		g2D = output_image.createGraphics();
	}
	
	/**
	 * Set the value of world
	 * @param WORLD Which world (0-5) to generate the quilt as
	 */
	public void setWorld(int WORLD){
		if (WORLD > 5){
			world = 5;
		} else if (WORLD < 0){
			world = 0;
		} else{
			world = WORLD;
		}
	}
	
	/**
	 * Get the value of world
	 * @return Which world (0-5) the quilt is being generated as
	 */
	public int getWorld(){
		return world;
	}
	
	/**
	 * Get the level
	 * @return The level the quilt is being generated from
	 */
	public int[][] getLevel(){
		return level;
	}
	
	/**
	 * Draw Mario at the appropriate X & Y on the canvas
	 * @param X The x coordinate to draw Mario at
	 * @param Y The y coordinate to draw Mario at
	 */
	private void drawMario(int X, int Y){
		int DRAW_X = X * BLOCKSIZE, DRAW_Y = Y * BLOCKSIZE;
		int MID_X = DRAW_X + HALF_BLOCKSIZE, MID_Y = DRAW_Y + HALF_BLOCKSIZE;
		int END_X = DRAW_X + BLOCKSIZE, END_Y = DRAW_Y + BLOCKSIZE;
		
		ArrayList<Polygon> redShapes = new ArrayList<Polygon>(), blueShapes = new ArrayList<Polygon>(),
				blackShapes = new ArrayList<Polygon>(), brownShapes = new ArrayList<Polygon>(),
				skinShapes = new ArrayList<Polygon>();
		
		//Left hat triangle
		int[] x_hatLeft = {DRAW_X,DRAW_X,MID_X};
		int[] y_hatLeft = {(DRAW_Y-HALF_BLOCKSIZE),(DRAW_Y-BLOCKSIZE),(DRAW_Y-HALF_BLOCKSIZE-QUARTER_BLOCKSIZE)};
		redShapes.add(new Polygon(x_hatLeft,y_hatLeft,3));	
		//Middle hat triangle
		int[] x_hatMid = {DRAW_X,MID_X,END_X};
		int[] y_hatMid = {(DRAW_Y-BLOCKSIZE),(DRAW_Y-HALF_BLOCKSIZE-QUARTER_BLOCKSIZE),(DRAW_Y-BLOCKSIZE)};
		redShapes.add(new Polygon(x_hatMid,y_hatMid,3));
		//Right hat triangle
		int[] x_hatRight = {END_X,END_X,MID_X};
		int[] y_hatRight = {(DRAW_Y-HALF_BLOCKSIZE),(DRAW_Y-BLOCKSIZE),(DRAW_Y-HALF_BLOCKSIZE-QUARTER_BLOCKSIZE)};
		redShapes.add(new Polygon(x_hatRight,y_hatRight,3));
		//Mario's hair
		int[] x_marioHair = {DRAW_X,MID_X,END_X};
		int[] y_marioHair = {(DRAW_Y-HALF_BLOCKSIZE),(DRAW_Y-HALF_BLOCKSIZE-QUARTER_BLOCKSIZE),(DRAW_Y-HALF_BLOCKSIZE)};
		blackShapes.add(new Polygon(x_marioHair,y_marioHair,3));
		//Left face triangle
		int[] x_faceLeft = {DRAW_X,DRAW_X,MID_X};
		int[] y_faceLeft = {(DRAW_Y),(DRAW_Y-HALF_BLOCKSIZE),(DRAW_Y-QUARTER_BLOCKSIZE)};
		skinShapes.add(new Polygon(x_faceLeft,y_faceLeft,3));	
		//Middle face triangle
		int[] x_faceMid = {DRAW_X,MID_X,END_X};
		int[] y_faceMid = {(DRAW_Y-HALF_BLOCKSIZE),(DRAW_Y-QUARTER_BLOCKSIZE),(DRAW_Y-HALF_BLOCKSIZE)};
		skinShapes.add(new Polygon(x_faceMid,y_faceMid,3));
		//Right face triangle
		int[] x_faceRight = {END_X,END_X,MID_X};
		int[] y_faceRight = {(DRAW_Y),(DRAW_Y-HALF_BLOCKSIZE),(DRAW_Y-QUARTER_BLOCKSIZE)};
		skinShapes.add(new Polygon(x_faceRight,y_faceRight,3));
		//Mario's mustache
		int[] x_marioMustache = {DRAW_X,MID_X,END_X};
		int[] y_marioMustache = {(DRAW_Y),(DRAW_Y-QUARTER_BLOCKSIZE),(DRAW_Y)};
		blackShapes.add(new Polygon(x_marioMustache,y_marioMustache,3));
		//Mario's shirt
		int[] x_shirt = {DRAW_X,END_X,END_X,DRAW_X};
		int[] y_shirt = {DRAW_Y,DRAW_Y,MID_Y,MID_Y};
		redShapes.add(new Polygon(x_shirt,y_shirt,4));
		//Pants left triangle
		int[] x_pantsLeft = {DRAW_X,MID_X,DRAW_X};
		int[] y_pantsLeft = {MID_Y,MID_Y,END_Y};
		blueShapes.add(new Polygon(x_pantsLeft,y_pantsLeft,3));
		//Shoes
		int[] x_shoes = {DRAW_X,MID_X,END_X};
		int[] y_shoes = {END_Y,MID_Y,END_Y};
		brownShapes.add(new Polygon(x_shoes,y_shoes,3));
		//Pants right triangle
		int[] x_pantsRight = {END_X,MID_X,END_X};
		int[] y_pantsRight = {MID_Y,MID_Y,END_Y};
		blueShapes.add(new Polygon(x_pantsRight,y_pantsRight,3));
		
		//Draw Mario
		for (Polygon shape : redShapes){ drawShape(shape, Sprite.COLOR_MARIO_RED, Sprite.COLOR_BORDER); }
		for (Polygon shape : blueShapes){ drawShape(shape, Sprite.COLOR_MARIO_BLUE, Sprite.COLOR_BORDER); }
		for (Polygon shape : blackShapes){ drawShape(shape, Sprite.COLOR_MARIO_HAIR, Sprite.COLOR_BORDER); }
		for (Polygon shape : brownShapes){ drawShape(shape, Sprite.COLOR_MARIO_SHOES, Sprite.COLOR_BORDER); }
		for (Polygon shape : skinShapes){ drawShape(shape, Sprite.COLOR_MARIO_SKIN, Sprite.COLOR_BORDER); }
	}
	
	/**
	 * Draw a question block at XY
	 * @param X The x coordinate to draw it at
	 * @param Y The y coordinate to draw it at
	 * @param ADJ The adjacent question blocks
	 * @param up_upright Whether the block up & to the right has a quesiton block above it
	 * @throws IOException
	 */
	private void drawQuestionBlock(int X, int Y, int ADJ, boolean up_upright) throws IOException{
		int DRAW_X = X * BLOCKSIZE, DRAW_Y = Y * BLOCKSIZE;
		int MID_X = DRAW_X + HALF_BLOCKSIZE, MID_Y = DRAW_Y + HALF_BLOCKSIZE;
		int END_X = DRAW_X + BLOCKSIZE, END_Y = DRAW_Y + BLOCKSIZE;
		int QUARTER_X = DRAW_X + QUARTER_BLOCKSIZE, THREE_X = END_X - QUARTER_BLOCKSIZE;
		
		//Define the Black diamond for the question mark
		int[] xpoints_diamond = {QUARTER_X,MID_X,THREE_X,MID_X};
		int[] ypoints_diamond = {MID_Y,DRAW_Y,MID_Y,END_Y};
		Polygon middleDiamond = new Polygon(xpoints_diamond,ypoints_diamond,4);
		boolean left = (ADJ & ML) > 0,		right = (ADJ & MR) > 0,
				up = (ADJ & TM) > 0,		down = (ADJ & BM) > 0,
				upleft = (ADJ & TL) > 0,	upright = (ADJ & TR) > 0,
				downleft = (ADJ & BL) > 0,	downright = (ADJ & BR) > 0;
				
		ArrayList<Polygon> triangles = new ArrayList<Polygon>();
		//alone or no directly adjacent (ok if only corners)
		//{TESTED}
		/*	 ____ ____ ____
		 * 	| /\ |    | /\ |
		 * 	|_\/_|____|_\/_|
		 * 	|    | /\/|    |
		 * 	|____|/\/_|____|
		 * 	| /\ |    | /\ |
		 * 	|_\/_|____|_\/_| 
		 */
		if (!(left || right || up || down)){
			//Bottom left triangle (small equilateral)
			int[] x1 = {DRAW_X,QUARTER_X,MID_X},	y1 = {END_Y,MID_Y,END_Y};
			triangles.add(new Polygon(x1,y1,3));
			//Top right triangle (small equilateral)
			int[] x2 = {MID_X,THREE_X,END_X},	y2 = {DRAW_Y,MID_Y,DRAW_Y};
			triangles.add(new Polygon(x2,y2,3));
			//Top left triangle (large right)
			int[] x3 = {DRAW_X,DRAW_X,MID_X},	y3 = {END_Y,DRAW_Y,DRAW_Y};
			triangles.add(new Polygon(x3,y3,3));
			//Bottom right triangle (large right)
			int[] x4 = {MID_X,END_X,END_X},	y4 = {END_Y,END_Y,DRAW_Y};
			triangles.add(new Polygon(x4,y4,3));
		}
		//left and not up, downleft, upright, right, or down
		//{TESTED}
		/*	 ____ ____ ____
		 * 	| /\ |    |    |
		 * 	|_\/_|____|____|
		 * 	| /\/  /\ |    |
		 * 	|_\/__/\/_|____|
		 * 	|    |    | /\ |
		 * 	|____|____|_\/_| 
		 */
		else if (left && !(right || up || down || upright || downleft)){
			//Bottom left triangle (small equilateral)
			int[] x1 = {DRAW_X,QUARTER_X,MID_X},	y1 = {END_Y,MID_Y,END_Y};
			triangles.add(new Polygon(x1,y1,3));
			//Top right triangle (small equilateral)
			int[] x2 = {MID_X,THREE_X,END_X},	y2 = {DRAW_Y,MID_Y,DRAW_Y};
			triangles.add(new Polygon(x2,y2,3));
			//Top left rhombus (move onto left shape)
			int[] x3 = {(DRAW_X-HALF_BLOCKSIZE),DRAW_X,MID_X,DRAW_X};
			int[] y3 = {END_Y,END_Y,DRAW_Y,DRAW_Y};
			triangles.add(new Polygon(x3,y3,4));
			//Bottom right triangle (large right)
			int[] x4 = {MID_X,END_X,END_X},	y4 = {END_Y,END_Y,DRAW_Y};
			triangles.add(new Polygon(x4,y4,3));
		}
		//left and upright and not up, downleft, right, or down
		//{TESTED}
		/*	 ____ ____ ____
		 * 	| /\ |    | /\ |
		 * 	|_\/_|____|_\/_|
		 * 	| /\/  /\/|    |
		 * 	|_\/__/\/_|____|
		 * 	|    |    | /\ |
		 * 	|____|____|_\/_| 
		 */
		else if (left && upright && !(right || up || down || downleft)){
			//Bottom left triangle (small equilateral)
			int[] x1 = {DRAW_X,QUARTER_X,MID_X},	y1 = {END_Y,MID_Y,END_Y};
			triangles.add(new Polygon(x1,y1,3));
			//Top right triangle (small equilateral)
			int[] x2 = {MID_X,THREE_X,END_X},	y2 = {DRAW_Y,MID_Y,DRAW_Y};
			triangles.add(new Polygon(x2,y2,3));
			//Top left rhombus (move onto left shape)
			int[] x3 = {(DRAW_X-HALF_BLOCKSIZE),DRAW_X,MID_X,DRAW_X};
			int[] y3 = {END_Y,END_Y,DRAW_Y,DRAW_Y};
			triangles.add(new Polygon(x3,y3,4));
			//Bottom right triangle (large right)
			int[] x4 = {MID_X,END_X,END_X},	y4 = {END_Y,END_Y,DRAW_Y};
			triangles.add(new Polygon(x4,y4,3));
		}
		//left and downleft and not up, upright, right, or down
		//{TESTED}
		/*	 ____ ____ ____
		 * 	| /\ |    |    |
		 * 	|_\/_|____|____|
		 * 	| /\/  /\ |    |
		 * 	|_\/  /\/_|____|
		 * 	| /\ /    | /\ |
		 * 	|_\/_|____|_\/_| 
		 */
		else if (left && downleft && !(right || up || down || upright)){
			//Bottom left triangle (small equilateral)
			int[] x1 = {DRAW_X,QUARTER_X,MID_X},	y1 = {END_Y,MID_Y,END_Y};
			triangles.add(new Polygon(x1,y1,3));
			//Top right triangle (small equilateral)
			int[] x2 = {MID_X,THREE_X,END_X},	y2 = {DRAW_Y,MID_Y,DRAW_Y};
			triangles.add(new Polygon(x2,y2,3));
			//Top left rhombus (move onto left shape and downleft shape)
			int[] x3 = {(DRAW_X-HALF_BLOCKSIZE),(DRAW_X-QUARTER_BLOCKSIZE),MID_X,DRAW_X};
			int[] y3 = {END_Y,(END_Y+HALF_BLOCKSIZE),DRAW_Y,DRAW_Y};
			triangles.add(new Polygon(x3,y3,4));
			//Bottom right triangle (large right)
			int[] x4 = {MID_X,END_X,END_X},	y4 = {END_Y,END_Y,DRAW_Y};
			triangles.add(new Polygon(x4,y4,3));
		}
		//left, down, and downleft and not up, right, or upright
		//{TESTED}
		/*	 ____ ____ ____
		 * 	| /\ |    |    |
		 * 	|_\/_|____|____|
		 * 	| /\/  /\/|    |
		 * 	|_\/  /\/ |____|
		 * 	| /\ / /\ | /\ |
		 * 	|_\/_|/\/_|_\/_| 
		 */
		else if (left && down && downleft && !(right || up || upright)){
			//Bottom left rhombus (into down)
			int[] x1 = {DRAW_X,DRAW_X,QUARTER_X,MID_X},	y1 = {(END_Y+BLOCKSIZE),END_Y,MID_Y,END_Y};
			triangles.add(new Polygon(x1,y1,3));
			//Top right triangle (small equilateral)
			int[] x2 = {MID_X,THREE_X,END_X},	y2 = {DRAW_Y,MID_Y,DRAW_Y};
			triangles.add(new Polygon(x2,y2,3));
			//Top left rhombus (move onto left shape)
			int[] x3 = {(DRAW_X-HALF_BLOCKSIZE),(DRAW_X-QUARTER_BLOCKSIZE),MID_X,DRAW_X};
			int[] y3 = {END_Y,(END_Y+HALF_BLOCKSIZE),DRAW_Y,DRAW_Y};
			triangles.add(new Polygon(x3,y3,4));
			//Bottom right triangle (large right into down)
			int[] x4 = {MID_X,END_X,END_X},	y4 = {END_Y,(END_Y+BLOCKSIZE),DRAW_Y};
			triangles.add(new Polygon(x4,y4,3));
		}
		//left, down, upright, and downleft and not up or right
		//{TESTED}
		/*	 ____ ____ ____
		 * 	| /\ |    | /\ |
		 * 	|_\/_|____|_\/_|
		 * 	| /\/  /\/|    |
		 * 	|_\/  /\/ |____|
		 * 	| /\ / /\ | /\ |
		 * 	|_\/_|/\/_|_\/_| 
		 */
		else if (left && down && upright && downleft && !(right || up)){
			//Bottom left rhombus (into down)
			int[] x1 = {DRAW_X,DRAW_X,QUARTER_X,MID_X},	y1 = {(END_Y+BLOCKSIZE),END_Y,MID_Y,END_Y};
			triangles.add(new Polygon(x1,y1,3));
			//Top right triangle (small equilateral)
			int[] x2 = {MID_X,THREE_X,END_X},	y2 = {DRAW_Y,MID_Y,DRAW_Y};
			triangles.add(new Polygon(x2,y2,3));
			//Top left rhombus (move onto left shape)
			int[] x3 = {(DRAW_X-HALF_BLOCKSIZE),(DRAW_X-QUARTER_BLOCKSIZE),MID_X,DRAW_X};
			int[] y3 = {END_Y,(END_Y+HALF_BLOCKSIZE),DRAW_Y,DRAW_Y};
			triangles.add(new Polygon(x3,y3,4));
			//Bottom right triangle (large right into down)
			int[] x4 = {MID_X,END_X,END_X},	y4 = {END_Y,(END_Y+BLOCKSIZE),DRAW_Y};
			triangles.add(new Polygon(x4,y4,3));
		}
		//left and up and not downleft, upright, right, or down
		//{TESTED}
		/*	 ____ ____ ____
		 * 	| /\ | /\/|    |
		 * 	|_\/_/ \/ |____|
		 * 	| /\/  /\ |    |
		 * 	|_\/__/\/\|____|
		 * 	|    |    | /\ |
		 * 	|____|____|_\/_| 
		 */
		else if (left && up && !(right || down || upright || downleft)){
			//Bottom left triangle (small equilateral)
			int[] x1 = {DRAW_X,QUARTER_X,MID_X},	y1 = {END_Y,MID_Y,END_Y};
			triangles.add(new Polygon(x1,y1,3));
			//Bottom right triangle (small equilateral)
			int[] x2 = {MID_X,THREE_X,END_X},	y2 = {END_Y,MID_Y,END_Y};
			triangles.add(new Polygon(x2,y2,3));
			//Top left rhombus (move onto left shape and up shape)
			int[] x3 = {(DRAW_X-HALF_BLOCKSIZE),QUARTER_X,MID_X,DRAW_X};
			int[] y3 = {END_Y,(DRAW_Y-HALF_BLOCKSIZE),DRAW_Y,END_Y};
			triangles.add(new Polygon(x3,y3,4));
			//Right triangle (large right and into up)
			int[] x4 = {END_X,MID_X,END_X},	y4 = {END_Y,DRAW_Y,(DRAW_Y-BLOCKSIZE)};
			triangles.add(new Polygon(x4,y4,3));
		}
		//left, up, and down and not downleft, upright, right
		//{TESTED}
		/*	 ____ ____ ____
		 * 	| /\ | /\/|    |
		 * 	|_\/_/ \/ |____|
		 * 	| /\/  /\/|    |
		 * 	|_\/__/\/ |____|
		 * 	|    | /\ | /\ |
		 * 	|____|/\/\|_\/_| 
		 */
		else if (left && up && down && !(right || upright || downleft)){
			//Bottom left rhombus (into down)
			int[] x1 = {DRAW_X,DRAW_X,QUARTER_X,MID_X},	y1 = {(END_Y+BLOCKSIZE),END_Y,MID_Y,END_Y};
			triangles.add(new Polygon(x1,y1,3));
			//Right triangle (large right and into up)
			int[] x2 = {END_X,MID_X,END_X},	y2 = {END_Y,DRAW_Y,(DRAW_Y-BLOCKSIZE)};
			triangles.add(new Polygon(x2,y2,3));
			//Top left rhombus (move onto left shape and up shape)
			int[] x3 = {(DRAW_X-HALF_BLOCKSIZE),QUARTER_X,MID_X,DRAW_X};
			int[] y3 = {END_Y,(DRAW_Y-HALF_BLOCKSIZE),DRAW_Y,END_Y};
			triangles.add(new Polygon(x3,y3,4));
			//Bottom right triangle (large into down)
			int[] x4 = {MID_X,END_X,END_X},	y4 = {END_Y,(END_Y+BLOCKSIZE),DRAW_Y};
			triangles.add(new Polygon(x4,y4,3));
			
		}
		//left and up and upright and not downleft, right, or down
		//{TESTED}
		/*	 ____ ____ ____
		 * 	| /\ | /\/  /\ |
		 * 	|_\/_/ \/  /\/_|
		 * 	| /\/  /\/|    |
		 * 	|_\/__/\/ |____|
		 * 	|    |    | /\ |
		 * 	|____|____|_\/_| 
		 */
		else if (left && up && upright && !(right || down || downleft)){
			//Bottom left triangle (small equilateral)
			int[] x1 = {DRAW_X,QUARTER_X,MID_X},	y1 = {END_Y,MID_Y,END_Y};
			triangles.add(new Polygon(x1,y1,3));
			//Bottom right triangle (large right)
			int[] x2 = {MID_X,END_X,END_X},	y2 = {END_Y,DRAW_Y,END_Y};
			triangles.add(new Polygon(x2,y2,3));
			//Top left rhombus (move onto left shape and up shape)
			int[] x3 = {(DRAW_X-HALF_BLOCKSIZE),QUARTER_X,MID_X,DRAW_X};
			int[] y3 = {END_Y,(DRAW_Y-HALF_BLOCKSIZE),DRAW_Y,END_Y};
			triangles.add(new Polygon(x3,y3,4));
			//Right rhombus (into up and upright)
			int[] x4 = {MID_X,END_X,(END_X+HALF_BLOCKSIZE),THREE_X};
			int[] y4 = {DRAW_Y,(DRAW_Y-BLOCKSIZE),(DRAW_Y-BLOCKSIZE),MID_Y};
			triangles.add(new Polygon(x4,y4,4));
		}
		//left, up, down and upright and not downleft or right
		//{TESTED}
		/*	 ____ ____ ____
		 * 	| /\ | /\/  /\ |
		 * 	|_\/_/ \/  /\/_|
		 * 	| /\/  /\/|    |
		 * 	|_\/__/\/ |____|
		 * 	|    | /\ | /\ |
		 * 	|____|/\/_|_\/_| 
		 */
		else if (left && up && down && upright && !(right || downleft)){
			//Bottom left rhombus (into down)
			int[] x1 = {DRAW_X,DRAW_X,QUARTER_X,MID_X},	y1 = {(END_Y+BLOCKSIZE),END_Y,MID_Y,END_Y};
			triangles.add(new Polygon(x1,y1,3));
			//Bottom right triangle (large right)
			int[] x2 = {MID_X,END_X,END_X},	y2 = {END_Y,DRAW_Y,END_Y};
			triangles.add(new Polygon(x2,y2,3));
			//Top left rhombus (move onto left shape and up shape)
			int[] x3 = {(DRAW_X-HALF_BLOCKSIZE),QUARTER_X,MID_X,DRAW_X};
			int[] y3 = {END_Y,(DRAW_Y-HALF_BLOCKSIZE),DRAW_Y,END_Y};
			triangles.add(new Polygon(x3,y3,4));
			//Right rhombus (into up and upright)
			int[] x4 = {MID_X,END_X,(END_X+HALF_BLOCKSIZE),THREE_X};
			int[] y4 = {DRAW_Y,(DRAW_Y-BLOCKSIZE),(DRAW_Y-BLOCKSIZE),MID_Y};
			triangles.add(new Polygon(x4,y4,4));
		}
		//left and up and upright and downleft and not right, or down
		//{TESTED}
		/*	 ____ ____ ____
		 * 	| /\ | /\/  /\ |
		 * 	|_\/_/ \/  /\/_|
		 * 	| /\/  /\/|    |
		 * 	|_\/  /\/ |____|
		 * 	| /\ /    | /\ |
		 * 	|_\/_|____|_\/_| 
		 */
		else if (left && up && upright && downleft && !(right || down)){
			//Bottom left triangle (small equilateral)
			int[] x1 = {DRAW_X,QUARTER_X,MID_X},	y1 = {END_Y,MID_Y,END_Y};
			triangles.add(new Polygon(x1,y1,3));
			//Bottom right triangle (large right)
			int[] x2 = {MID_X,END_X,END_X},	y2 = {END_Y,DRAW_Y,END_Y};
			triangles.add(new Polygon(x2,y2,3));
			//Top left rhombus (move onto left shape and up shape and downleft shape)
			int[] x3 = {(DRAW_X-HALF_BLOCKSIZE),QUARTER_X,MID_X,(DRAW_X-QUARTER_BLOCKSIZE)};
			int[] y3 = {END_Y,(DRAW_Y-HALF_BLOCKSIZE),DRAW_Y,(END_Y+HALF_BLOCKSIZE)};
			triangles.add(new Polygon(x3,y3,4));
			//Right rhombus (into up and upright)
			int[] x4 = {MID_X,END_X,(END_X+HALF_BLOCKSIZE),THREE_X};
			int[] y4 = {DRAW_Y,(DRAW_Y-BLOCKSIZE),(DRAW_Y-BLOCKSIZE),MID_Y};
			triangles.add(new Polygon(x4,y4,4));
		}
		//left and up and downleft and not upright, right, or down
		//{TESTED}
		/*	 ____ ____ ____
		 * 	| /\ | /\/|    |
		 * 	|_\/_/ \/ |____|
		 * 	| /\/  /\ |    |
		 * 	|_\/  /\/\|____|
		 * 	| /\ /    | /\ |
		 * 	|_\/_|____|_\/_| 
		 */
		else if (left && up && downleft && !(right || down || upright)){
			//Bottom left triangle (small equilateral)
			int[] x1 = {DRAW_X,QUARTER_X,MID_X},	y1 = {END_Y,MID_Y,END_Y};
			triangles.add(new Polygon(x1,y1,3));
			//Bottom right triangle (small equilateral)
			int[] x2 = {MID_X,THREE_X,END_X},	y2 = {END_Y,MID_Y,END_Y};
			triangles.add(new Polygon(x2,y2,3));
			//Top left rhombus (move onto left shape and up shape and downleft shape)
			int[] x3 = {(DRAW_X-HALF_BLOCKSIZE),(DRAW_X-QUARTER_BLOCKSIZE),MID_X,QUARTER_X};
			int[] y3 = {END_Y,(END_Y+HALF_BLOCKSIZE),DRAW_Y,(DRAW_Y-HALF_BLOCKSIZE)};
			triangles.add(new Polygon(x3,y3,4));
			//Right triangle (large right and into up)
			int[] x4 = {END_X,MID_X,END_X},	y4 = {END_Y,DRAW_Y,(DRAW_Y-BLOCKSIZE)};
			triangles.add(new Polygon(x4,y4,3));
		}
		//left and right and not up, down, upright, or downleft
		//{TESTED}
		/*	 ____ ____ ____
		 * 	| /\ |    |    |
		 * 	|_\/_|____|____|
		 * 	| /\/  /\/  /\ |
		 * 	|_\/__/\/__/\/_|
		 * 	|    |    | /\ |
		 * 	|____|____|_\/_| 
		 */
		else if (left && right && !(up || down || upright || downleft)){
			//Bottom left triangle (small equilateral)
			int[] x1 = {DRAW_X,QUARTER_X,MID_X}, y1 = {END_Y,MID_Y,END_Y};
			triangles.add(new Polygon(x1,y1,3));
			//Top right triangle (small equilateral)
			int[] x2 = {MID_X,THREE_X,END_X}, y2 = {DRAW_Y,MID_Y,DRAW_Y};
			triangles.add(new Polygon(x2,y2,3));
			//Left rhombus (into left)
			int[] x3 = {(DRAW_X-HALF_BLOCKSIZE),DRAW_X,MID_X,DRAW_X};
			int[] y3 = {END_Y,DRAW_Y,DRAW_Y,END_Y};
			triangles.add(new Polygon(x3,y3,4));
			//Right rhombus (into right)
			int[] x4 = {MID_X,END_X,(END_X+HALF_BLOCKSIZE),END_X};
			int[] y4 = {END_Y,DRAW_Y,DRAW_Y,END_Y};
			triangles.add(new Polygon(x4,y4,4));
		}
		//left, right and downleft and not up, down, or upright
		//{TESTED}
		/*	 ____ ____ ____
		 * 	| /\ |    |    |
		 * 	|_\/_|____|____|
		 * 	| /\/  /\/  /\ |
		 * 	|_\/  /\/__/\/_|
		 * 	| /\ /    | /\ |
		 * 	|_\/_|____|_\/_| 
		 */
		else if (left && right && downleft && !(up || down || upright)){
			//Bottom left triangle (small equilateral)
			int[] x1 = {DRAW_X,QUARTER_X,MID_X}, y1 = {END_Y,MID_Y,END_Y};
			triangles.add(new Polygon(x1,y1,3));
			//Top right triangle (small equilateral)
			int[] x2 = {MID_X,THREE_X,END_X}, y2 = {DRAW_Y,MID_Y,DRAW_Y};
			triangles.add(new Polygon(x2,y2,3));
			//Left rhombus (into left and downleft)
			int[] x3 = {(DRAW_X-HALF_BLOCKSIZE),DRAW_X,MID_X,(DRAW_X-QUARTER_BLOCKSIZE)};
			int[] y3 = {END_Y,DRAW_Y,DRAW_Y,(END_Y+HALF_BLOCKSIZE)};
			triangles.add(new Polygon(x3,y3,4));
			//Right rhombus (into right)
			int[] x4 = {MID_X,END_X,(END_X+HALF_BLOCKSIZE),END_X};
			int[] y4 = {END_Y,DRAW_Y,DRAW_Y,END_Y};
			triangles.add(new Polygon(x4,y4,4));
		}
		//left, right, upright, and downleft and not up, or down
		//{TESTED}
		/*	 ____ ____ ____
		 * 	| /\ |    | /\ |
		 * 	|_\/_|____/ \/_|
		 * 	| /\/  /\/  /\ |
		 * 	|_\/  /\/__/\/_|
		 * 	| /\ /    | /\ |
		 * 	|_\/_|____|_\/_| 
		 */
		else if (left && right && upright && downleft && !(up || down)){
			//Bottom left triangle (small equilateral)
			int[] x1 = {DRAW_X,QUARTER_X,MID_X}, y1 = {END_Y,MID_Y,END_Y};
			triangles.add(new Polygon(x1,y1,3));
			//Top right triangle (small equilateral)
			int[] x2 = {MID_X,THREE_X,END_X}, y2 = {DRAW_Y,MID_Y,DRAW_Y};
			triangles.add(new Polygon(x2,y2,3));
			//Left rhombus (into left and downleft)
			int[] x3 = {(DRAW_X-HALF_BLOCKSIZE),DRAW_X,MID_X,(DRAW_X-QUARTER_BLOCKSIZE)};
			int[] y3 = {END_Y,DRAW_Y,DRAW_Y,(END_Y+HALF_BLOCKSIZE)};
			triangles.add(new Polygon(x3,y3,4));
			//Right rhombus (into right)
			int[] x4 = {MID_X,(END_X+QUARTER_BLOCKSIZE),(END_X+HALF_BLOCKSIZE),END_X};
			int[] y4 = {END_Y,(DRAW_Y-HALF_BLOCKSIZE),DRAW_Y,END_Y};
			triangles.add(new Polygon(x4,y4,4));
		}
		//left, right and upright and not up, down, or downleft
		//{TESTED}
		/*	 ____ ____ ____
		 * 	| /\ |    | /\ |
		 * 	|_\/_|____/ \/_|
		 * 	| /\/  /\/  /\ |
		 * 	|_\/__/\/__/\/_|
		 * 	|    |    | /\ |
		 * 	|____|____|_\/_| 
		 */
		else if (left && right && upright && !(up || down || downleft)){
			//Bottom left triangle (small equilateral)
			int[] x1 = {DRAW_X,QUARTER_X,MID_X}, y1 = {END_Y,MID_Y,END_Y};
			triangles.add(new Polygon(x1,y1,3));
			//Top right triangle (small equilateral)
			int[] x2 = {MID_X,THREE_X,END_X}, y2 = {DRAW_Y,MID_Y,DRAW_Y};
			triangles.add(new Polygon(x2,y2,3));
			//Left rhombus (into left)
			int[] x3 = {(DRAW_X-HALF_BLOCKSIZE),DRAW_X,MID_X,DRAW_X};
			int[] y3 = {END_Y,DRAW_Y,DRAW_Y,END_Y};
			triangles.add(new Polygon(x3,y3,4));
			//Right rhombus (into right and upright)
			int[] x4 = {MID_X,(END_X+QUARTER_BLOCKSIZE),(END_X+HALF_BLOCKSIZE),END_X};
			int[] y4 = {END_Y,(DRAW_Y-HALF_BLOCKSIZE),DRAW_Y,END_Y};
			triangles.add(new Polygon(x4,y4,4));
		}
		//left and right and up and not down, upright, or downleft
		//{TESTED}
		/*	 ____ ____ ____
		 * 	| /\ | /\/|    |
		 * 	|_\/_/ \/ |____|
		 * 	| /\/  /\/  /\ |
		 * 	|_\/__/\/__/\/_|
		 * 	|    |    | /\ |
		 * 	|____|____|_\/_| 
		 */
		else if (left && right && up && !(down || upright || downleft)){
			//Bottom left triangle (small equilateral)
			int[] x1 = {DRAW_X,QUARTER_X,MID_X}, y1 = {END_Y,MID_Y,END_Y};
			triangles.add(new Polygon(x1,y1,3));
			//Top right rhombus (into up)
			int[] x2 = {MID_X,THREE_X,END_X,END_X};
			int[] y2 = {DRAW_Y,MID_Y,DRAW_Y,(DRAW_Y-BLOCKSIZE)};
			triangles.add(new Polygon(x2,y2,4));
			//Left rhombus (into left)
			int[] x3 = {(DRAW_X-HALF_BLOCKSIZE),QUARTER_X,MID_X,DRAW_X};
			int[] y3 = {END_Y,(DRAW_Y-HALF_BLOCKSIZE),DRAW_Y,END_Y};
			triangles.add(new Polygon(x3,y3,4));
			//Right rhombus (into right)
			int[] x4 = {MID_X,END_X,(END_X+HALF_BLOCKSIZE),END_X};
			int[] y4 = {END_Y,DRAW_Y,DRAW_Y,END_Y};
			triangles.add(new Polygon(x4,y4,4));
		}
		//left, right, up and downleft and not down or upright
		//{TESTED}
		/*	 ____ ____ ____
		 * 	| /\ | /\/|    |
		 * 	|_\/_/ \/ |____|
		 * 	| /\/  /\/  /\ |
		 * 	|_\/  /\/__/\/_|
		 * 	| /\ /    | /\ |
		 * 	|_\/_|____|_\/_| 
		 */
		else if (left && right && up && downleft && !(down || upright)){
			//Bottom left triangle (small equilateral)
			int[] x1 = {DRAW_X,QUARTER_X,MID_X}, y1 = {END_Y,MID_Y,END_Y};
			triangles.add(new Polygon(x1,y1,3));
			//Top right rhombus (into up)
			int[] x2 = {MID_X,THREE_X,END_X,END_X};
			int[] y2 = {DRAW_Y,MID_Y,DRAW_Y,(DRAW_Y-BLOCKSIZE)};
			triangles.add(new Polygon(x2,y2,4));
			//Left rhombus (into left and downleft)
			int[] x3 = {(DRAW_X-HALF_BLOCKSIZE),QUARTER_X,MID_X,(DRAW_X-QUARTER_BLOCKSIZE)};
			int[] y3 = {END_Y,(DRAW_Y-HALF_BLOCKSIZE),DRAW_Y,(END_Y+HALF_BLOCKSIZE)};
			triangles.add(new Polygon(x3,y3,4));
			//Right rhombus (into right)
			int[] x4 = {MID_X,END_X,(END_X+HALF_BLOCKSIZE),END_X};
			int[] y4 = {END_Y,DRAW_Y,DRAW_Y,END_Y};
			triangles.add(new Polygon(x4,y4,4));
		}
		//left and right and up and upright and not down or downleft
		//{TESTED}
		/*	 ____ ____ ____
		 * 	| /\ | /\/| /\ |
		 * 	|_\/_/ \/ / \/_|
		 * 	| /\/  /\/  /\ |
		 * 	|_\/__/\/__/\/_|
		 * 	|    |    | /\ |
		 * 	|____|____|_\/_| 
		 */
		else if (left && right && up && upright && !(down || downleft)){
			//Bottom left triangle (small equilateral)
			int[] x1 = {DRAW_X,QUARTER_X,MID_X}, y1 = {END_Y,MID_Y,END_Y};
			triangles.add(new Polygon(x1,y1,3));
			//Top right rhombus (into up and upright)
			int[] x2 = {MID_X,THREE_X,(END_X+HALF_BLOCKSIZE),END_X};
			int[] y2 = {DRAW_Y,MID_Y,(DRAW_Y-BLOCKSIZE),(DRAW_Y-BLOCKSIZE)};
			triangles.add(new Polygon(x2,y2,4));
			//Left rhombus (into left and up)
			int[] x3 = {(DRAW_X-HALF_BLOCKSIZE),QUARTER_X,MID_X,DRAW_X};
			int[] y3 = {END_Y,(DRAW_Y-HALF_BLOCKSIZE),DRAW_Y,END_Y};
			triangles.add(new Polygon(x3,y3,4));
			//Right rhombus (into right and upright)
			int[] x4 = {MID_X,(END_X+QUARTER_BLOCKSIZE),(END_X+HALF_BLOCKSIZE),END_X};
			int[] y4 = {END_Y,(DRAW_Y-HALF_BLOCKSIZE),DRAW_Y,END_Y};
			triangles.add(new Polygon(x4,y4,4));
		}
		//left, right, up, upright, and down and not downleft
		//{TESTED}
		/*	 ____ ____ ____
		 * 	| /\ | /\/| /\ |
		 * 	|_\/_/ \/ / \/_|
		 * 	| /\/  /\/  /\ |
		 * 	|_\/__/\/  /\/_|
		 * 	|    | /\/| /\ |
		 * 	|____|/\/_|_\/_| 
		 */
		else if (left && right && up && down && upright && !(downleft)){
			//Bottom left rhombus (into down)
			int[] x1 = {DRAW_X,DRAW_X,QUARTER_X,MID_X}, y1 = {(END_Y+BLOCKSIZE),END_Y,MID_Y,END_Y};
			triangles.add(new Polygon(x1,y1,4));
			//Top right rhombus (into up and upright)
			int[] x2 = {MID_X,THREE_X,(END_X+HALF_BLOCKSIZE),END_X};
			int[] y2 = {DRAW_Y,MID_Y,(DRAW_Y-BLOCKSIZE),(DRAW_Y-BLOCKSIZE)};
			triangles.add(new Polygon(x2,y2,4));
			//Left rhombus (into left and up)
			int[] x3 = {(DRAW_X-HALF_BLOCKSIZE),QUARTER_X,MID_X,DRAW_X};
			int[] y3 = {END_Y,(DRAW_Y-HALF_BLOCKSIZE),DRAW_Y,END_Y};
			triangles.add(new Polygon(x3,y3,4));
			//Right rhombus (into right and upright and down)
			int[] x4 = {MID_X,(END_X+QUARTER_BLOCKSIZE),(END_X+HALF_BLOCKSIZE),QUARTER_X};
			int[] y4 = {END_Y,(DRAW_Y-HALF_BLOCKSIZE),DRAW_Y,(END_Y+HALF_BLOCKSIZE)};
			triangles.add(new Polygon(x4,y4,4));
		}
		//left, right, up, upright, and downleft. not down
		//{TESTED}
		/*	 ____ ____ ____
		 * 	| /\ | /\/| /\ |
		 * 	|_\/_/ \/ / \/_|
		 * 	| /\/  /\/  /\ |
		 * 	|_\/  /\/__/\/_|
		 * 	| /\ /    | /\ |
		 * 	|_\/_|____|_\/_| 
		 */
		else if (left && right && up && upright && downleft && !(down)){
			//Bottom left triangle (small equilateral)
			int[] x1 = {DRAW_X,QUARTER_X,MID_X}, y1 = {END_Y,MID_Y,END_Y};
			triangles.add(new Polygon(x1,y1,3));
			//Top right rhombus (into up and upright)
			int[] x2 = {MID_X,THREE_X,(END_X+HALF_BLOCKSIZE),END_X};
			int[] y2 = {DRAW_Y,MID_Y,(DRAW_Y-BLOCKSIZE),(DRAW_Y-BLOCKSIZE)};
			triangles.add(new Polygon(x2,y2,4));
			//Left rhombus (into left and up and downleft)
			int[] x3 = {(DRAW_X-HALF_BLOCKSIZE),QUARTER_X,MID_X,(DRAW_X-QUARTER_BLOCKSIZE)};
			int[] y3 = {END_Y,(DRAW_Y-HALF_BLOCKSIZE),DRAW_Y,(END_Y+HALF_BLOCKSIZE)};
			triangles.add(new Polygon(x3,y3,4));
			//Right rhombus (into right and upright)
			int[] x4 = {MID_X,(END_X+QUARTER_BLOCKSIZE),(END_X+HALF_BLOCKSIZE),END_X};
			int[] y4 = {END_Y,(DRAW_Y-HALF_BLOCKSIZE),DRAW_Y,END_Y};
			triangles.add(new Polygon(x4,y4,4));
		}
		//left, right, up, down, upright, and downleft
		//{TESTED}
		/*	 ____ ____ ____
		 * 	| /\ | /\/| /\ |
		 * 	|_\/_/ \/ / \/_|
		 * 	| /\/  /\/  /\ |
		 * 	|_\/  /\/  /\/_|
		 * 	| /\ / /\ / /\ |
		 * 	|_\/__/\/_|_\/_| 
		 */
		else if (left && right && up && down && upright && downleft){
			//Bottom left rhombus (into downleft and down)
			int[] x1 = {(DRAW_X-HALF_BLOCKSIZE),QUARTER_X,MID_X,DRAW_X};
			int[] y1 = {(END_Y+BLOCKSIZE),MID_Y,END_Y,(END_Y+BLOCKSIZE)};
			triangles.add(new Polygon(x1,y1,4));
			//Top right rhombus (into up and upright)
			int[] x2 = {MID_X,THREE_X,(END_X+HALF_BLOCKSIZE),END_X};
			int[] y2 = {DRAW_Y,MID_Y,(DRAW_Y-BLOCKSIZE),(DRAW_Y-BLOCKSIZE)};
			triangles.add(new Polygon(x2,y2,4));
			//Left rhombus (into left and up and downleft)
			int[] x3 = {(DRAW_X-HALF_BLOCKSIZE),QUARTER_X,MID_X,(DRAW_X-QUARTER_BLOCKSIZE)};
			int[] y3 = {END_Y,(DRAW_Y-HALF_BLOCKSIZE),DRAW_Y,(END_Y+HALF_BLOCKSIZE)};
			triangles.add(new Polygon(x3,y3,4));
			//Right rhombus (into right and upright and down)
			int[] x4 = {MID_X,THREE_X,(END_X+HALF_BLOCKSIZE),(END_X+QUARTER_BLOCKSIZE)};
			int[] y4 = {END_Y,(END_Y+HALF_BLOCKSIZE),DRAW_Y,(DRAW_Y-HALF_BLOCKSIZE)};
			triangles.add(new Polygon(x4,y4,4));
		}
		//left, right, up, down, and downleft and not upright
		//{TESTED}
		/*	 ____ ____ ____
		 * 	| /\ | /\/|    |
		 * 	|_\/_/ \/ |____|
		 * 	| /\/  /\/  /\ |
		 * 	|_\/  /\/  /\/_|
		 * 	| /\ / /\ / /\ |
		 * 	|_\/__/\/_|_\/_| 
		 */
		else if (left && right && up && down && downleft && !(upright)){
			//Bottom left rhombus (into downleft and down)
			int[] x1 = {(DRAW_X-HALF_BLOCKSIZE),QUARTER_X,MID_X,DRAW_X};
			int[] y1 = {(END_Y+BLOCKSIZE),MID_Y,END_Y,(END_Y+BLOCKSIZE)};
			triangles.add(new Polygon(x1,y1,4));
			//Top right rhombus (into up)
			int[] x2 = {MID_X,THREE_X,END_X,END_X};
			int[] y2 = {DRAW_Y,MID_Y,DRAW_Y,(DRAW_Y-BLOCKSIZE)};
			triangles.add(new Polygon(x2,y2,4));
			//Left rhombus (into left and up and downleft)
			int[] x3 = {(DRAW_X-HALF_BLOCKSIZE),QUARTER_X,MID_X,(DRAW_X-QUARTER_BLOCKSIZE)};
			int[] y3 = {END_Y,(DRAW_Y-HALF_BLOCKSIZE),DRAW_Y,(END_Y+HALF_BLOCKSIZE)};
			triangles.add(new Polygon(x3,y3,4));
			//Right rhombus (into rightdown)
			int[] x4 = {MID_X,THREE_X,(END_X+HALF_BLOCKSIZE),END_X};
			int[] y4 = {END_Y,(END_Y+HALF_BLOCKSIZE),DRAW_Y,DRAW_Y};
			triangles.add(new Polygon(x4,y4,4));
		}
		//left, right, up, and down and not upright or downleft
		//{TESTED}
		/*	 ____ ____ ____
		 * 	| /\ | /\/|    |
		 * 	|_\/_/ \/ |____|
		 * 	| /\/  /\/  /\ |
		 * 	|_\/__/\/  /\/_|
		 * 	|    | /\ / /\ |
		 * 	|____|/\/_|_\/_| 
		 */
		else if (left && right && up && down && !(upright || downleft)){
			//Bottom left rhombus (into down)
			int[] x1 = {DRAW_X,MID_X,QUARTER_X,DRAW_X};
			int[] y1 = {(END_Y+BLOCKSIZE),END_Y,MID_Y,END_Y};
			triangles.add(new Polygon(x1,y1,4));
			//Top right rhombus (into up)
			int[] x2 = {MID_X,THREE_X,END_X,END_X};
			int[] y2 = {DRAW_Y,MID_Y,DRAW_Y,(DRAW_Y-BLOCKSIZE)};
			triangles.add(new Polygon(x2,y2,4));
			//Left rhombus (into left and up)
			int[] x3 = {(DRAW_X-HALF_BLOCKSIZE),QUARTER_X,MID_X,DRAW_X};
			int[] y3 = {END_Y,(DRAW_Y-HALF_BLOCKSIZE),DRAW_Y,END_Y};
			triangles.add(new Polygon(x3,y3,4));
			//Right rhombus (into rightdown)
			int[] x4 = {MID_X,THREE_X,(END_X+HALF_BLOCKSIZE),END_X};
			int[] y4 = {END_Y,(END_Y+HALF_BLOCKSIZE),DRAW_Y,DRAW_Y};
			triangles.add(new Polygon(x4,y4,4));
		}
		//right and not left, up, down, or upright
		//{TESTED}
		/*	 ____ ____ ____
		 * 	| /\ |    |    |
		 * 	|_\/_|____|____|
		 * 	|    | /\/  /\ |
		 * 	|____|/\/__/\/_|
		 * 	| /\ |    | /\ |
		 * 	|_\/_|____|_\/_| 
		 */
		else if (right && !(left || up || down || upright)){
			//Bottom left triangle (small equilateral)
			int[] x1 = {DRAW_X,QUARTER_X,MID_X}, y1 = {END_Y,MID_Y,END_Y};
			triangles.add(new Polygon(x1,y1,3));
			//Top right triangle (small equilateral)
			int[] x2 = {MID_X,THREE_X,END_X}, y2 = {DRAW_Y,MID_Y,DRAW_Y};
			triangles.add(new Polygon(x2,y2,3));
			//Left triangle (large right)
			int[] x3 = {DRAW_X,DRAW_X,MID_X}, y3 = {END_Y,DRAW_Y,DRAW_Y};
			triangles.add(new Polygon(x3,y3,3));
			//Right rhombus (into right)
			int[] x4 = {MID_X,END_X,(END_X+HALF_BLOCKSIZE),END_X};
			int[] y4 = {END_Y,DRAW_Y,DRAW_Y,END_Y};
			triangles.add(new Polygon(x4,y4,4));	
		}
		//right and upright and not left, up, or down
		//{TESTED}
		/*	 ____ ____ ____
		 * 	| /\ |    | /\ |
		 * 	|_\/_|____/ \/_|
		 * 	|    | /\/  /\ |
		 * 	|____|/\/__/\/_|
		 * 	| /\ |    | /\ |
		 * 	|_\/_|____|_\/_| 
		 */
		else if (right && upright && !(left || up || down)){
			//Bottom left triangle (small equilateral)
			int[] x1 = {DRAW_X,QUARTER_X,MID_X}, y1 = {END_Y,MID_Y,END_Y};
			triangles.add(new Polygon(x1,y1,3));
			//Top right triangle (small equilateral)
			int[] x2 = {MID_X,THREE_X,END_X}, y2 = {DRAW_Y,MID_Y,DRAW_Y};
			triangles.add(new Polygon(x2,y2,3));
			//Left triangle (large right)
			int[] x3 = {DRAW_X,DRAW_X,MID_X}, y3 = {END_Y,DRAW_Y,DRAW_Y};
			triangles.add(new Polygon(x3,y3,3));
			//Right rhombus (into right and upright)
			int[] x4 = {MID_X,(END_X+QUARTER_BLOCKSIZE),(END_X+HALF_BLOCKSIZE),END_X};
			int[] y4 = {END_Y,DRAW_Y,DRAW_Y,END_Y};
			triangles.add(new Polygon(x4,y4,4));	
		}
		//right and up and not left, down, or upright
		//{TESTED}
		/*	 ____ ____ ____      ____ ____
		 * 	| /\/  /\/|    |    |    |\/\ |
		 * 	|_\/__/\/ |____| or |____| \/_|
		 * 	|    | /\/  /\ |
		 * 	|____|/\/__/\/_|
		 * 	| /\ |    | /\ |
		 * 	|_\/_|____|_\/_| 
		 */
		else if (right && up && !(left || down || upright)){
			//Bottom left triangle (small equilateral)
			int[] x1 = {DRAW_X,QUARTER_X,MID_X}, y1 = {END_Y,MID_Y,END_Y};
			triangles.add(new Polygon(x1,y1,3));
			//Top right rhombus (into up)
			int[] x2 = {MID_X,THREE_X,END_X,END_X}, y2 = {DRAW_Y,MID_Y,DRAW_Y,(DRAW_Y-BLOCKSIZE)};
			triangles.add(new Polygon(x2,y2,4));
			if (upleft){
				int[] x3 = {DRAW_X,MID_X,QUARTER_X,DRAW_X};
				int[] y3 = {END_Y,DRAW_Y,(DRAW_Y-HALF_BLOCKSIZE),DRAW_Y};
				triangles.add(new Polygon(x3,y3,4));
			}
			else{
				//Left triangle (into up)
				int[] x3 = {DRAW_X,MID_X,DRAW_X}, y3 = {END_Y,DRAW_Y,(DRAW_Y-BLOCKSIZE)};
				triangles.add(new Polygon(x3,y3,3));
			}
			//Right rhombus (into right)
			int[] x4 = {MID_X,END_X,(END_X+HALF_BLOCKSIZE),END_X};
			int[] y4 = {END_Y,DRAW_Y,DRAW_Y,END_Y};
			triangles.add(new Polygon(x4,y4,4));
		}
		//right and up and down and not left or upright
		//{TESTED}
		/*	 ____ ____ ____      ____ ____
		 * 	| /\/  /\/|    |    |    |\/\/|
		 * 	|_\/__/\/ |____| or |____| \/ |
		 * 	|    | /\/  /\ |
		 * 	|____|/\/  /\/_|
		 * 	|    | /\ /    |
		 * 	|____|/\/_|____| 
		 */
		else if (right && up && down && !(left || upright)){
			//Bottom left rhombus (into down)
			int[] x1 = {DRAW_X,QUARTER_X,MID_X,DRAW_X}, y1 = {END_Y,MID_Y,END_Y,(END_Y+BLOCKSIZE)};
			triangles.add(new Polygon(x1,y1,4));
			//Top right rhombus (into up)
			int[] x2 = {MID_X,THREE_X,END_X,END_X}, y2 = {DRAW_Y,MID_Y,DRAW_Y,(DRAW_Y-BLOCKSIZE)};
			triangles.add(new Polygon(x2,y2,4));
			if (upleft){
				//Left rhombus (into up)
				int[] x3 = {DRAW_X,MID_X,QUARTER_X,DRAW_X};
				int[] y3 = {END_Y,DRAW_Y,(DRAW_Y-HALF_BLOCKSIZE),DRAW_Y};
				triangles.add(new Polygon(x3,y3,4));
			}
			else{
				//Left triangle (into up)
				int[] x3 = {DRAW_X,MID_X,DRAW_X}, y3 = {END_Y,DRAW_Y,(DRAW_Y-BLOCKSIZE)};
				triangles.add(new Polygon(x3,y3,3));
			}
			//Right rhombus (into right and down)
			int[] x4 = {MID_X,END_X,(END_X+HALF_BLOCKSIZE),THREE_X};
			int[] y4 = {END_Y,DRAW_Y,DRAW_Y,(END_Y+HALF_BLOCKSIZE)};
			triangles.add(new Polygon(x4,y4,4));
		}
		//right and down and not left, up, or upright
		//{TESTED}
		/*	 ____ ____ ____
		 * 	| /\ |    |    |
		 * 	|_\/_|____|____|
		 * 	|    | /\/  /\ |
		 * 	|____|/\/  /\/_|
		 * 	|    | /\ /    |
		 * 	|____|/\/_|____| 
		 */
		else if (right && down && !(left || up || upright)){
			//Bottom left rhombus (into down)
			int[] x1 = {DRAW_X,QUARTER_X,MID_X,DRAW_X}, y1 = {END_Y,MID_Y,END_Y,(END_Y+BLOCKSIZE)};
			triangles.add(new Polygon(x1,y1,4));
			//Top right triangle (small equilateral)
			int[] x2 = {MID_X,THREE_X,END_X}, y2 = {DRAW_Y,MID_Y,DRAW_Y};
			triangles.add(new Polygon(x2,y2,3));
			//Left triangle (large right)
			int[] x3 = {DRAW_X,MID_X,DRAW_X}, y3 = {END_Y,DRAW_Y,DRAW_Y};
			triangles.add(new Polygon(x3,y3,3));
			//Right rhombus (into right and down)
			int[] x4 = {MID_X,END_X,(END_X+HALF_BLOCKSIZE),THREE_X};
			int[] y4 = {END_Y,DRAW_Y,DRAW_Y,(END_Y+HALF_BLOCKSIZE)};
			triangles.add(new Polygon(x4,y4,4));
		}
		//right and down and upright and not left or up
		//{TESTED}
		/*	 ____ ____ ____
		 * 	| /\ |    | /\ |
		 * 	|_\/_|____/ \/_|
		 * 	|    | /\/  /\ |
		 * 	|____|/\/  /\/_|
		 * 	|    | /\ /    |
		 * 	|____|/\/_|____| 
		 */
		else if (right && down && upright && !(left || up)){
			//Bottom left rhombus (into down)
			int[] x1 = {DRAW_X,QUARTER_X,MID_X,DRAW_X}, y1 = {END_Y,MID_Y,END_Y,(END_Y+BLOCKSIZE)};
			triangles.add(new Polygon(x1,y1,4));
			//Top right triangle (small equilateral)
			int[] x2 = {MID_X,THREE_X,END_X}, y2 = {DRAW_Y,MID_Y,DRAW_Y};
			triangles.add(new Polygon(x2,y2,3));
			//Left triangle (large right)
			int[] x3 = {DRAW_X,MID_X,DRAW_X}, y3 = {END_Y,DRAW_Y,DRAW_Y};
			triangles.add(new Polygon(x3,y3,3));
			//Right rhombus (into right and down and upright)
			int[] x4 = {MID_X,(END_X+QUARTER_BLOCKSIZE),(END_X+HALF_BLOCKSIZE),THREE_X};
			int[] y4 = {END_Y,(DRAW_Y-HALF_BLOCKSIZE),DRAW_Y,(END_Y+HALF_BLOCKSIZE)};
			triangles.add(new Polygon(x4,y4,4));
		}
		//right and down and upright and up and not left
		//{TESTED}
		/*	 ____ ____ ____      ____ ____ ____
		 * 	| /\/  /\/| /\ |    |    |\/\/  /\ |
		 * 	|_\/__/\/ / \/_| or |____| \/  /\/_|
		 * 	|    | /\/  /\ |
		 * 	|____|/\/  /\/_|
		 * 	|    | /\ /    |
		 * 	|____|/\/_|____| 
		 */
		else if (right && down && upright && up && !(left)){
			//Bottom left rhombus (into down)
			int[] x1 = {DRAW_X,QUARTER_X,MID_X,DRAW_X}, y1 = {END_Y,MID_Y,END_Y,(END_Y+BLOCKSIZE)};
			triangles.add(new Polygon(x1,y1,4));
			//Top right rhombus (into up)
			int[] x2 = {MID_X,THREE_X,(END_X+HALF_BLOCKSIZE),END_X};
			int[] y2 = {DRAW_Y,MID_Y,(DRAW_Y-BLOCKSIZE),(DRAW_Y-BLOCKSIZE)};
			triangles.add(new Polygon(x2,y2,4));
			if (upleft){
				//Left rhombus (into up)
				int[] x3 = {DRAW_X,MID_X,QUARTER_X,DRAW_X};
				int[] y3 = {END_Y,DRAW_Y,(DRAW_Y-HALF_BLOCKSIZE),DRAW_Y};
				triangles.add(new Polygon(x3,y3,4));
			}
			else{
				//Left triangle (into up)
				int[] x3 = {DRAW_X,MID_X,DRAW_X}, y3 = {END_Y,DRAW_Y,(DRAW_Y-BLOCKSIZE)};
				triangles.add(new Polygon(x3,y3,3));
			}
			//Right rhombus (into right and down and upright)
			int[] x4 = {MID_X,(END_X+QUARTER_BLOCKSIZE),(END_X+HALF_BLOCKSIZE),THREE_X};
			int[] y4 = {END_Y,(DRAW_Y-HALF_BLOCKSIZE),DRAW_Y,(END_Y+HALF_BLOCKSIZE)};
			triangles.add(new Polygon(x4,y4,4));
		}
		//AD HOC: if up
		else if (up){
			if (!right && !upright){
				int[] xthis1 = {END_X,MID_X,END_X}, ythis1 = {END_Y,DRAW_Y,(DRAW_Y-BLOCKSIZE)};
				triangles.add(new Polygon(xthis1,ythis1,3));
				//small equilateral
				int[] xthis3 = {MID_X,THREE_X,END_X}, ythis3 = {END_Y,MID_Y,END_Y};
				triangles.add(new Polygon(xthis3,ythis3,3));
			} else if (!right){
				//right triangle bottom right corner
				int[] xthis2 = {MID_X,END_X,END_X};
				int[] ythis2 = {END_Y,END_Y,DRAW_Y};
				triangles.add(new Polygon(xthis2,ythis2,3));
			}
			if (!left && !upleft){
				int[] xthis2 = {DRAW_X,MID_X,DRAW_X}, ythis2 = {END_Y,DRAW_Y,(DRAW_Y-BLOCKSIZE)};
				triangles.add(new Polygon(xthis2,ythis2,3));
				//small equilateral
				int[] xthis3 = {DRAW_X,QUARTER_X,MID_X}, ythis3 = {END_Y,MID_Y,END_Y};
				triangles.add(new Polygon(xthis3,ythis3,3));
			} else if (!left){
				int[] xthis2 = {DRAW_X,MID_X,QUARTER_X,DRAW_X};
				int[] ythis2 = {END_Y,DRAW_Y,(DRAW_Y-HALF_BLOCKSIZE),DRAW_Y};
				triangles.add(new Polygon(xthis2,ythis2,4));
				//small equilateral
				int[] xthis3 = {DRAW_X,QUARTER_X,MID_X}, ythis3 = {END_Y,MID_Y,END_Y};
				triangles.add(new Polygon(xthis3,ythis3,3));
			}
		} else if (right){
			//small equilateral at top right
			int[] xthis = {MID_X,THREE_X,END_X}, ythis = {DRAW_Y,MID_Y,DRAW_Y};
			triangles.add(new Polygon(xthis,ythis,3));
		} else if (down){
			if (!right && !upright){
				//small equilateral at top right
				int[] xthis = {MID_X,THREE_X,END_X}, ythis = {DRAW_Y,MID_Y,DRAW_Y};
				triangles.add(new Polygon(xthis,ythis,3));
			} else if (!right){
				//small equilateral at top right
				int[] xthis = {MID_X,THREE_X,END_X}, ythis = {DRAW_Y,MID_Y,DRAW_Y};
				triangles.add(new Polygon(xthis,ythis,3));
			}
			if (!left && !upleft){
				//right triangle at top left
				int[] xthis = {DRAW_X,MID_X,DRAW_X}, ythis = {DRAW_Y,DRAW_Y,END_Y};
				triangles.add(new Polygon(xthis,ythis,3));
			} else if (!left){
				//right triangle at top left
				int[] xthis = {DRAW_X,MID_X,DRAW_X}, ythis = {DRAW_Y,DRAW_Y,END_Y};
				triangles.add(new Polygon(xthis,ythis,3));
			}
		}
		else if(true){
			
		}
		//if up, upright, and up-upright
		//{TESTED}
		/*	 ____ ____ ____
		 * 	|    |    | /\ |
		 * 	|____|____/ \/_|
		 * 	|    | /\/  /\ |
		 * 	|____|_\/  /\/_|
		 * 	|    | /\ /    |
		 * 	|____|/\/_|____| 
		 */
		if (up && upright && up_upright){
			//draw big rhombus across this, up, upright, and up_upright
			int[] xthis = {MID_X,(END_X+QUARTER_BLOCKSIZE),(END_X+HALF_BLOCKSIZE),THREE_X};
			int[] ythis = {DRAW_Y,(DRAW_Y-BLOCKSIZE-HALF_BLOCKSIZE),(DRAW_Y-BLOCKSIZE),MID_Y};
			triangles.add(new Polygon(xthis,ythis,4));
		}
		/* 
		 * TL:001	TM:002	TR:004
		 * ML:008	{ me }	MR:016
		 * BL:032	BM:064	BR:0128
		 */
		/*	 ____ ____ ____
		 * 	| /\ | /\ | /\ |
		 * 	|_\/_|_\/_|_\/_|
		 * 	| /\ | /\ | /\ |
		 * 	|_\/_|_\/_|_\/_|
		 * 	| /\ | /\ | /\ |
		 * 	|_\/_|_\/_|_\/_| 
		 */
		
		for (Polygon trig : triangles){
			drawShape(trig, Sprite.COLOR_QUESTIONMARK_BG, Sprite.COLOR_BORDER);
		}
		drawShape(middleDiamond, Sprite.COLOR_QUESTIONMARK_DIAMOND, Sprite.COLOR_BORDER);
	}
	
	
	/**
	 * Draw the cap of a vertical pipe from the left
	 * @param X The x coordinate of the left of the cap
	 * @param Y The y coordinate of the left of the cap
	 */
	private void drawVerticalPipeEnd(int X, int Y){
		int DRAW_X = X * BLOCKSIZE, DRAW_Y = Y * BLOCKSIZE;
		int END_X = DRAW_X + BLOCKSIZE, END_Y = DRAW_Y + BLOCKSIZE;
		
		ArrayList<Polygon> lightGreen = new ArrayList<Polygon>(), green = new ArrayList<Polygon>(),
				darkGreen = new ArrayList<Polygon>();
		
		//Top of the pipe (LIGHT)
		int[] x_top_light = {DRAW_X,END_X,DRAW_X};
		int[] y_top_light = {DRAW_Y,DRAW_Y,END_Y};
		lightGreen.add(new Polygon(x_top_light,y_top_light,3));
		//Top of the pipe (MEDIUM)
		int[] x_top_med = {DRAW_X,END_X,(END_X+BLOCKSIZE),END_X};
		int[] y_top_med = {END_Y,END_Y,DRAW_Y,DRAW_Y};
		green.add(new Polygon(x_top_med,y_top_med,4));
		//Top of the pipe (DARK)
		int[] x_top_dark = {(END_X+BLOCKSIZE),(END_X+BLOCKSIZE),END_X};
		int[] y_top_dark = {DRAW_Y,END_Y,END_Y};
		darkGreen.add(new Polygon(x_top_dark,y_top_dark,3));
		
		//Draw The Pipe
		for (Polygon shape : lightGreen){ drawShape(shape, Sprite.COLOR_PIPE_LIGHT, Sprite.COLOR_BORDER); }
		for (Polygon shape : green){ drawShape(shape, Sprite.COLOR_PIPE_GREEN, Sprite.COLOR_BORDER); }
		for (Polygon shape : darkGreen){ drawShape(shape, Sprite.COLOR_PIPE_DARK, Sprite.COLOR_BORDER); }
	}
	
	/**
	 * Draw the cap of a horizontal pipe from the bottom
	 * @param X The x coordinate of the bottom of the cap
	 * @param Y The y coordinate of the bottom of the cap
	 */
	private void drawHorizontalPipeEnd(int X, int Y){
		int DRAW_X = X * BLOCKSIZE, DRAW_Y = Y * BLOCKSIZE;
		int MID_X = (X * BLOCKSIZE)+HALF_BLOCKSIZE;
		int END_X = DRAW_X + BLOCKSIZE, END_Y = DRAW_Y + BLOCKSIZE;
		
		ArrayList<Polygon> lightGreen = new ArrayList<Polygon>(), green = new ArrayList<Polygon>(),
				darkGreen = new ArrayList<Polygon>();
		
		//Top of the pipe (LIGHT)
		int[] x_top_light = {DRAW_X,MID_X,DRAW_X};
		int[] y_top_light = {(DRAW_Y-BLOCKSIZE),(DRAW_Y-BLOCKSIZE),END_Y};
		lightGreen.add(new Polygon(x_top_light,y_top_light,3));
		//Top of the pipe (MEDIUM)
		int[] x_top_med = {DRAW_X,MID_X,END_X,MID_X};
		int[] y_top_med = {END_Y,(DRAW_Y-BLOCKSIZE),(DRAW_Y-BLOCKSIZE),END_Y};
		green.add(new Polygon(x_top_med,y_top_med,4));
		//Top of the pipe (DARK)
		int[] x_top_dark = {MID_X,END_X,END_X};
		int[] y_top_dark = {END_Y,END_Y,(DRAW_Y-BLOCKSIZE)};
		darkGreen.add(new Polygon(x_top_dark,y_top_dark,3));
		
		//Draw The Pipe
		for (Polygon shape : lightGreen){ drawShape(shape, Sprite.COLOR_PIPE_LIGHT, Sprite.COLOR_BORDER); }
		for (Polygon shape : green){ drawShape(shape, Sprite.COLOR_PIPE_GREEN, Sprite.COLOR_BORDER); }
		for (Polygon shape : darkGreen){ drawShape(shape, Sprite.COLOR_PIPE_DARK, Sprite.COLOR_BORDER); }
	}
	
	/**
	 * Draw a Vertical Pipe from the bottom left corner
	 * @param X The x coordinate of the bottom left corner
	 * @param Y The y coordinate of the bottom right corner
	 * @param HEIGHT The height of the pipe
	 */
	private void drawVerticalPipe(int X, int Y, int HEIGHT){
		int DRAW_X = X * BLOCKSIZE, DRAW_Y = Y * BLOCKSIZE;
		int END_X = DRAW_X + BLOCKSIZE, END_Y = DRAW_Y + BLOCKSIZE;
		int TOP = (Y - HEIGHT)*BLOCKSIZE;
		
		ArrayList<Polygon> lightGreen = new ArrayList<Polygon>(), green = new ArrayList<Polygon>(),
				darkGreen = new ArrayList<Polygon>();
		
		//Body of the pipe (LIGHT)
		int[] x_body_light = {(DRAW_X+EIGTH_BLOCKSIZE),END_X,(DRAW_X+EIGTH_BLOCKSIZE)};
		int[] y_body_light = {(TOP+BLOCKSIZE),(TOP+BLOCKSIZE),END_Y};
		lightGreen.add(new Polygon(x_body_light,y_body_light,3));
		//Body of the pipe (MEDIUM)
		int[] x_body_med = {(DRAW_X+EIGTH_BLOCKSIZE),END_X,(END_X+BLOCKSIZE-EIGTH_BLOCKSIZE),END_X};
		int[] y_body_med = {END_Y,END_Y,(TOP+BLOCKSIZE),(TOP+BLOCKSIZE)};
		green.add(new Polygon(x_body_med,y_body_med,4));
		//Body of the pipe (DARK)
		int[] x_body_dark = {(END_X+BLOCKSIZE-EIGTH_BLOCKSIZE),(END_X+BLOCKSIZE-EIGTH_BLOCKSIZE),END_X};
		int[] y_body_dark = {(TOP+BLOCKSIZE),END_Y,END_Y};
		darkGreen.add(new Polygon(x_body_dark,y_body_dark,3));
		
		//Draw The Pipe
		for (Polygon shape : lightGreen){ drawShape(shape, Sprite.COLOR_PIPE_LIGHT, Sprite.COLOR_BORDER); }
		for (Polygon shape : green){ drawShape(shape, Sprite.COLOR_PIPE_GREEN, Sprite.COLOR_BORDER); }
		for (Polygon shape : darkGreen){ drawShape(shape, Sprite.COLOR_PIPE_DARK, Sprite.COLOR_BORDER); }
	}
	
	/**
	 * Draw a Horizontal Pipe starting from the bottom right corner
	 * @param X The x coordinate of the bottom right corner
	 * @param Y The y coordinate of the bottom right corner
	 * @param LENGTH The length of the pipe
	 */
	private void drawHorizontalPipe(int X, int Y, int LENGTH){
		int DRAW_X = X * BLOCKSIZE, DRAW_Y = Y * BLOCKSIZE;
		int END_X = DRAW_X + BLOCKSIZE, END_Y = DRAW_Y + BLOCKSIZE;
		int SIDE_X = (X - LENGTH)*BLOCKSIZE + BLOCKSIZE;
		int MID_X = SIDE_X + (int)((LENGTH/2.0)*BLOCKSIZE);
		
		ArrayList<Polygon> lightGreen = new ArrayList<Polygon>(), green = new ArrayList<Polygon>(),
				darkGreen = new ArrayList<Polygon>();
		
		//Body of the pipe (LIGHT)
		int[] x_body_light = {SIDE_X,MID_X,SIDE_X};
		int[] y_body_light = {(DRAW_Y-BLOCKSIZE+EIGTH_BLOCKSIZE),(DRAW_Y-BLOCKSIZE+EIGTH_BLOCKSIZE),(END_Y-EIGTH_BLOCKSIZE)};
		lightGreen.add(new Polygon(x_body_light,y_body_light,3));
		//Body of the pipe (MEDIUM)
		int[] x_body_med = {SIDE_X,MID_X,END_X,MID_X};
		int[] y_body_med = {(END_Y-EIGTH_BLOCKSIZE),(DRAW_Y-BLOCKSIZE+EIGTH_BLOCKSIZE),(DRAW_Y-BLOCKSIZE+EIGTH_BLOCKSIZE),(END_Y-EIGTH_BLOCKSIZE)};
		green.add(new Polygon(x_body_med,y_body_med,4));
		//Body of the pipe (DARK)
		int[] x_body_dark = {MID_X,END_X,END_X};
		int[] y_body_dark = {(END_Y-EIGTH_BLOCKSIZE),(END_Y-EIGTH_BLOCKSIZE),(DRAW_Y-BLOCKSIZE+EIGTH_BLOCKSIZE)};
		darkGreen.add(new Polygon(x_body_dark,y_body_dark,3));
		
		//Draw The Pipe
		for (Polygon shape : lightGreen){ drawShape(shape, Sprite.COLOR_PIPE_LIGHT, Sprite.COLOR_BORDER); }
		for (Polygon shape : green){ drawShape(shape, Sprite.COLOR_PIPE_GREEN, Sprite.COLOR_BORDER); }
		for (Polygon shape : darkGreen){ drawShape(shape, Sprite.COLOR_PIPE_DARK, Sprite.COLOR_BORDER); }
	}
	
	/**
	 * Draw a shape on the canvas
	 * @param POLY The shape to draw
	 * @param FILL The fill color
	 * @param BORDER The border color
	 */
	private void drawShape(Polygon POLY, Color FILL, Color BORDER){
		g2D.setPaint(FILL);
		g2D.fill(POLY);
		g2D.setColor(BORDER);
		g2D.drawPolygon(POLY);
	}
		
	/**
	 * Outputs the quilt to a file noted by FILENAME
	 * @param FILENAME The name of the file to output to
	 * @throws IOException
	 */
	public void outputImageToFile(String FILENAME) throws IOException{
		File f = new File(FILENAME);
		if (!ImageIO.write(output_image, "PNG", f)){
			throw new RuntimeException("Unexpected error writing image");
		}
	}

	/**
	 * Generate the Quilt
	 * @param VERBOSE What level of detail to print to the console with. 0 for none, 1 for all.
	 * @throws IOException
	 */
	public void generate(int VERBOSE) throws IOException{
		int marioX = -1, marioY = -1;
		int ADJ = 0; //This integer stores whether adjacent blocks share the same ID
		boolean up_upright = false; //Whether the block one space right and two spaces up shares the same ID as this
		for (int y = 0; y < level.length; y++){
			for (int x = 0; x < level[0].length; x++){
				int thisID = level[y][x];	//for now, only Sprite.ID_QUESTION_BLOCK
				ADJ = 0;	up_upright = false;
				if (x > 0 && level[y][x-1] == thisID){ ADJ = ADJ | ML; } //LEFT
				if (x < level[0].length-1 && level[y][x+1] == thisID){ ADJ = ADJ | MR; } //RIGHT
				if (y > 0 && level[y-1][x] == thisID){ ADJ = ADJ | TM; } //UP
				if (y < level.length-1 && level[y+1][x] == thisID){ ADJ = ADJ | BM; } //DOWN
				if (y > 0 && x > 0 && level[y-1][x-1] == thisID){ ADJ = ADJ | TL; } //UPLEFT
				if (y > 0 && x < level[0].length-1 && level[y-1][x+1] == thisID){ ADJ = ADJ | TR; } //UPRIGHT
				if (y < level.length-1 && x > 0 && level[y+1][x-1] == thisID){ ADJ = ADJ | BL; } //DOWNLEFT
				if (y < level.length-1 && x < level[0].length-1 && level[y+1][x+1] == thisID){ ADJ = ADJ | BR; } //DOWNRIGHT
				if (y > 1 && x < level[0].length-1 && level[y-2][x+1] == thisID){ up_upright = true; } //UPUPRIGHT
				
				switch(thisID){
				case Sprite.ID_QUESTION_BLOCK: //When a question block is discovered
					if (VERBOSE > 0) System.out.println("Question Block at (" + y + "," + x + ")");
					drawQuestionBlock(x,y,ADJ,up_upright);
					break;
				case Sprite.ID_MARIO:
					if (VERBOSE > 0) System.out.println("Mario at (" + y + "," + x + ")");
					marioX = x;
					marioY = y;
					break;
				case Sprite.ID_PIPE_VERTI_TL: //When the cap of a vertical pipe is discovered
				case Sprite.ID_PIPE_VERTI_BL:
					if (VERBOSE > 0) System.out.println("Left of Vertical Pipe Cap at (" + y + "," + x + ")");
					drawVerticalPipeEnd(x,y);
					break;
				case Sprite.ID_PIPE_VERTI_ML: //When the left part of a vertical pipe body is discovered
					int tempY = y;
					int tempHeight = 1;
					while(tempY > 0 && level[tempY-1][x] == Sprite.ID_PIPE_VERTI_ML){
						tempHeight++;
						tempY--;
					}
					drawVerticalPipe(x,y,tempHeight);
					break;
				case Sprite.ID_PIPE_VERTI_TR: //This pipe will be drawn over but should not act as air
				case Sprite.ID_PIPE_VERTI_MR: //This pipe will be drawn over but should not act as air
				case Sprite.ID_PIPE_VERTI_BR: //This pipe will be drawn over but should not act as air
					break;
				case Sprite.ID_PIPE_HORIZ_BL: //When the cap of a horizontal pipe is discovered
				case Sprite.ID_PIPE_HORIZ_BR:
					if (VERBOSE > 0) System.out.println("Bottom of Horizontal Pipe Cap at (" + y + "," + x + ")");
					drawHorizontalPipeEnd(x,y);
					break;
				case Sprite.ID_PIPE_HORIZ_BM: //When the bottom part of a horizontal pipe body is discovered
					int tempX = x;
					int tempLength = 1;
					while (tempX > 0 && level[y][tempX-1] == Sprite.ID_PIPE_HORIZ_BM){
						tempLength++;
						tempX--;
					}
					drawHorizontalPipe(x,y,tempLength);
				case Sprite.ID_PIPE_HORIZ_TL: //This pipe will be drawn over but should not act as air
				case Sprite.ID_PIPE_HORIZ_TM: //This pipe will be drawn over but should not act as air
				case Sprite.ID_PIPE_HORIZ_TR: //This pipe will be drawn over but should not act as air
					break;
				case Sprite.ID_AIR:
				default:
					int[] xTemp = {x*BLOCKSIZE,x*BLOCKSIZE,(x+1)*BLOCKSIZE,(x+1)*BLOCKSIZE};
					int[] yTemp = {y*BLOCKSIZE,(y+1)*BLOCKSIZE,(y+1)*BLOCKSIZE,y*BLOCKSIZE};
					drawShape(new Polygon(xTemp,yTemp,4), Color.WHITE, Sprite.COLOR_BORDER);
					break;					
				}
			}
		}
		drawMario(marioX,marioY);
		outputImageToFile("output.png");
	}
	
	/**
	 * TEST MAIN
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException{
		int VERBOSE = 0;
		int[][] LEVEL = new int[10][10];
		for (int i = 0; i < 10; i++){
			for (int j = 0; j < 10; j++){
				LEVEL[i][j] = 0;//Sprite.ID_QUESTION_BLOCK;
			}
		}
		
		int q = Sprite.ID_QUESTION_BLOCK;
		int m = Sprite.ID_MARIO;
		/*	 ____ ____ ____
		 * 	| /\ | /\ | /\ |
		 * 	|_\/_|_\/_|_\/_|
		 * 	| /\ | /\ | /\ |
		 * 	|_\/_|_\/_|_\/_|
		 * 	| /\ | /\ | /\ |
		 * 	|_\/_|_\/_|_\/_| 
		 */
		LEVEL[1][1] = q;	LEVEL[1][2] = q;	LEVEL[1][3] = q;
		LEVEL[2][1] = q;	LEVEL[2][2] = q;	LEVEL[2][3] = q;
		LEVEL[3][1] = q;	LEVEL[3][2] = q;	LEVEL[3][3] = q;
		
		LEVEL[8][2] = m;
		//Pipe going up
		LEVEL[6][6] = Sprite.ID_PIPE_VERTI_TL; LEVEL[6][7] = Sprite.ID_PIPE_VERTI_TR;
		LEVEL[7][6] = Sprite.ID_PIPE_VERTI_ML; LEVEL[7][7] = Sprite.ID_PIPE_VERTI_MR;
		LEVEL[8][6] = Sprite.ID_PIPE_VERTI_ML; LEVEL[8][7] = Sprite.ID_PIPE_VERTI_MR;
		//Pipe going left
		LEVEL[2][7] = Sprite.ID_PIPE_HORIZ_TL; LEVEL[2][8] = Sprite.ID_PIPE_HORIZ_TM; LEVEL[2][9] = Sprite.ID_PIPE_HORIZ_TM;
		LEVEL[3][7] = Sprite.ID_PIPE_HORIZ_BL; LEVEL[3][8] = Sprite.ID_PIPE_HORIZ_BM; LEVEL[3][9] = Sprite.ID_PIPE_HORIZ_BM;
		//Pipe going right
		LEVEL[4][7] = Sprite.ID_PIPE_HORIZ_TM; LEVEL[4][8] = Sprite.ID_PIPE_HORIZ_TM; LEVEL[5][9] = Sprite.ID_PIPE_HORIZ_TR;
		LEVEL[5][7] = Sprite.ID_PIPE_HORIZ_BM; LEVEL[5][8] = Sprite.ID_PIPE_HORIZ_BM; LEVEL[5][9] = Sprite.ID_PIPE_HORIZ_BR;		
		//Pipe going down
		LEVEL[6][8] = Sprite.ID_PIPE_VERTI_ML; LEVEL[6][9] = Sprite.ID_PIPE_VERTI_MR;
		LEVEL[7][8] = Sprite.ID_PIPE_VERTI_ML; LEVEL[7][9] = Sprite.ID_PIPE_VERTI_MR;
		LEVEL[8][8] = Sprite.ID_PIPE_VERTI_BL; LEVEL[8][9] = Sprite.ID_PIPE_VERTI_BR;
		
		QuiltGenerator qG = new QuiltGenerator(LEVEL);
		qG.generate(VERBOSE);
		
	}
}

