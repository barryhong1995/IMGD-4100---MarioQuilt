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
		
	private int[][] level;
	private int world = 0;
	private BufferedImage output_image;
	private Graphics2D g2D;
	
	private ArrayList<Polygon> shapeQueue;
	private ArrayList<Color> fillQueue;
	private ArrayList<Color> borderQueue;
	
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
		shapeQueue = new ArrayList<Polygon>();
		fillQueue = new ArrayList<Color>();
		borderQueue = new ArrayList<Color>();
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
		shapeQueue = new ArrayList<Polygon>();
		fillQueue = new ArrayList<Color>();
		borderQueue = new ArrayList<Color>();
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
		return;
	}
	
	//TODO: Take a better look at the logic tomorrow. In your sleep deprived state, you made some mistakes
	private void drawQuestionBlock(int X, int Y, int ADJ, boolean up_upright) throws IOException{
		int DRAW_X = X * BLOCKSIZE, DRAW_Y = Y * BLOCKSIZE;
		int MID_X = DRAW_X + HALF_BLOCKSIZE, MID_Y = DRAW_Y + HALF_BLOCKSIZE;
		int QUARTER_X = DRAW_X + QUARTER_BLOCKSIZE, QUARTER_Y = DRAW_Y + QUARTER_BLOCKSIZE;
		int END_X = DRAW_X + BLOCKSIZE, END_Y = DRAW_Y + BLOCKSIZE;
		int THREE_X = END_X - QUARTER_BLOCKSIZE, THREE_Y = END_Y - QUARTER_BLOCKSIZE;
		
		//Define the Black diamond for the question mark
		int[] xpoints_diamond = {QUARTER_X,MID_X,THREE_X,MID_X};
		int[] ypoints_diamond = {MID_Y,DRAW_Y,MID_Y,END_Y};
		Polygon middleDiamond = new Polygon(xpoints_diamond,ypoints_diamond,4);
		boolean left = (ADJ & ML) > 0,		right = (ADJ & MR) > 0,
				up = (ADJ & TM) > 0,		down = (ADJ & BM) > 0,
				upleft = (ADJ & TL) > 0,	upright = (ADJ & TR) > 0,
				downleft = (ADJ & BL) > 0,	downright = (ADJ & BR) > 0;
				
		ArrayList<Polygon> triangles = new ArrayList<Polygon>();
		//{DEFAULT}
		/*
		//Bottom left triangle (small equilateral)
		int[] x1D = {DRAW_X,QUARTER_X,MID_X},	y1D = {END_Y,MID_Y,END_Y};
		triangles.add(new Polygon(x1D,y1D,3));
		//Top right triangle (small equilateral)
		int[] x2D = {MID_X,THREE_X,END_X},	y2D = {DRAW_Y,MID_Y,DRAW_Y};
		triangles.add(new Polygon(x2D,y2D,3));
		//Top left triangle (large right)
		int[] x3D = {DRAW_X,DRAW_X,MID_X},	y3D = {END_Y,DRAW_Y,DRAW_Y};
		triangles.add(new Polygon(x3D,y3D,3));
		//Bottom right triangle (large right)
		int[] x4D = {MID_X,END_X,END_X},	y4D = {END_Y,END_Y,DRAW_Y};
		triangles.add(new Polygon(x4D,y4D,3));
		*/
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
			}
			if (!left && !upleft){
				//right triangle at top right
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
	 * Add a shape to the back of the queue
	 * @param POLY The shape to add
	 * @param FILL The fill color
	 * @param BORDER The border color
	 */
	private void queueShape(Polygon POLY, Color FILL, Color BORDER){
		shapeQueue.add(POLY);
		fillQueue.add(FILL);
		borderQueue.add(BORDER);
	}
	

	/**
	 * Remove the shape in the front of the queue and draw it
	 */
	private boolean dequeueShape(){
		boolean returnVal = shapeQueue.size() > 0;
		if (returnVal){
			Polygon POLY = shapeQueue.remove(0);
			Color FILL = fillQueue.remove(0);
			Color BORDER = borderQueue.remove(0);
			drawShape(POLY,FILL,BORDER);
		}
		return returnVal;
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
	 * TEST MAIN
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException{
		int[][] LEVEL = new int[10][10];
		for (int i = 0; i < 10; i++){
			for (int j = 0; j < 10; j++){
				LEVEL[i][j] = 1;//Sprite.ID_QUESTION_BLOCK;
			}
		}
		int ADJ = 0;	boolean up_upright = false;
		
		int q = Sprite.ID_QUESTION_BLOCK;
		/*	 ____ ____ ____
		 * 	| /\ | /\ | /\ |
		 * 	|_\/_|_\/_|_\/_|
		 * 	| /\ | /\ | /\ |
		 * 	|_\/_|_\/_|_\/_|
		 * 	| /\ | /\ | /\ |
		 * 	|_\/_|_\/_|_\/_| 
		 */
		LEVEL[1][1] = q;	LEVEL[1][2] = 1;	LEVEL[1][3] = q;
		LEVEL[2][1] = q;	LEVEL[2][2] = q;	LEVEL[2][3] = 1;
		LEVEL[3][1] = q;	LEVEL[3][2] = q;	LEVEL[3][3] = q;
		
		QuiltGenerator qG = new QuiltGenerator(LEVEL);
		//I know this is bad locality of reference, but I need to process top-down
		for (int y = 0; y < LEVEL.length; y++){
			for (int x = 0; x < LEVEL[0].length; x++){
				int thisID = LEVEL[y][x];	//for now, only Sprite.ID_QUESTION_BLOCK
				ADJ = 0;	up_upright = false;
				if (x > 0 && LEVEL[y][x-1] == thisID){ //LEFT
					ADJ = ADJ | ML;
				}
				if (x < LEVEL[0].length-1 && LEVEL[y][x+1] == thisID){ //RIGHT
					ADJ = ADJ | MR;
				}
				if (y > 0 && LEVEL[y-1][x] == thisID){ //UP
					ADJ = ADJ | TM;
				}
				if (y < LEVEL.length-1 && LEVEL[y+1][x] == thisID){ //DOWN
					ADJ = ADJ | BM;
				}
				if (y > 0 && x > 0 && LEVEL[y-1][x-1] == thisID){ //UPLEFT
					ADJ = ADJ | TL;
				}
				if (y > 0 && x < LEVEL[0].length-1 && LEVEL[y-1][x+1] == thisID){ //UPRIGHT
					ADJ = ADJ | TR;
				}
				if (y < LEVEL.length-1 && x > 0 && LEVEL[y+1][x-1] == thisID){ //DOWNLEFT
					ADJ = ADJ | BL;
				}
				if (y < LEVEL.length-1 && x < LEVEL[0].length-1 && LEVEL[y+1][x+1] == thisID){ //DOWNRIGHT
					ADJ = ADJ | BR;
				}
				if (y > 1 && x < LEVEL[0].length-1 && LEVEL[y-2][x+1] == thisID){
					up_upright = true;
				}
				//ADJ = 0; //Clear this line, only for testing base without neighbors
				if (thisID == Sprite.ID_QUESTION_BLOCK){
					System.out.println("Question Block at (" + y + "," + x + ")");
					qG.drawQuestionBlock(x,y,ADJ,up_upright);
				} else if (thisID == 1){
					int[] xTemp = {x*BLOCKSIZE,x*BLOCKSIZE,(x+1)*BLOCKSIZE,(x+1)*BLOCKSIZE};
					int[] yTemp = {y*BLOCKSIZE,(y+1)*BLOCKSIZE,(y+1)*BLOCKSIZE,y*BLOCKSIZE};
					qG.drawShape(new Polygon(xTemp,yTemp,4), Color.WHITE, Sprite.COLOR_BORDER);
				}
			}
		}
		while(qG.dequeueShape());
		
		qG.outputImageToFile("output.png");
		
	}
}

