package generator;

import java.awt.Color;

public class Sprite {
	
	private int id;
	private int x = -1, y = -1;
	
	/**
	 * Construct sprite with the given ID
	 * @param ID The ID value to assign to the sprite
	 */
	public Sprite(int ID){
		id = ID;
	}
	
	/**
	 * Get the sprite ID
	 * @return sprite ID
	 */
	public int getID(){
		return id;
	}
	//MARIO
	public static final int ID_MARIO = -1;
	//AIR
	public static final int ID_AIR = 0;
	//FACE BLOCKS
	public static final int ID_FACE_YELLOW = 1;
	public static final int ID_FACE_BROWN = 5;
	//QUESTION BLOCK & COIN
	public static final int ID_QUESTION_BLOCK = 2;
	public static final int ID_COIN = 7;
	//OTHER SPECIAL BLOCKS
	public static final int ID_CLOUD = 3;
	public static final int ID_MUSIC_NOTE = 4;
	public static final int ID_ROCK = 6;
	
	////WALKABLE BLOCKS
	//DIRT
	public static final int ID_DIRT_TL = 9;
	public static final int ID_DIRT_TM = 10;
	public static final int ID_DIRT_TR = 11;
	public static final int ID_DIRT_ML = 25;
	public static final int ID_DIRT_MM = 26;
	public static final int ID_DIRT_MR = 27;
	public static final int ID_DIRT_BL = 41;
	public static final int ID_DIRT_BM = 42;
	public static final int ID_DIRT_BR = 43;
	//Grass
	public static final int ID_GRASS_TL = 57;
	public static final int ID_GRASS_TM = 58;
	public static final int ID_GRASS_TR = 59;
	public static final int ID_GRASS_ML = 73;
	public static final int ID_GRASS_MM = 74;
	public static final int ID_GRASS_MR = 75;
	public static final int ID_GRASS_BL = 89;
	public static final int ID_GRASS_BM = 90;
	public static final int ID_GRASS_BR = 91;
	////Pipes
	//Horizontal
	public static final int ID_PIPE_HORIZ_TL = 22;
	public static final int ID_PIPE_HORIZ_BL = 38;
	public static final int ID_PIPE_HORIZ_TM = 23;
	public static final int ID_PIPE_HORIZ_BM = 39;
	public static final int ID_PIPE_HORIZ_TR = 24;
	public static final int ID_PIPE_HORIZ_BR = 40;
	//Vertical
	public static final int ID_PIPE_VERTI_TL = 20;
	public static final int ID_PIPE_VERTI_TR = 21;
	public static final int ID_PIPE_VERTI_ML = 36;
	public static final int ID_PIPE_VERTI_MR = 37;
	public static final int ID_PIPE_VERTI_BL = 52;
	public static final int ID_PIPE_VERTI_BR = 53;
	
	////Colors
	public static final Color COLOR_BORDER = new Color(255,0,110);
	//Mario
	public static final Color COLOR_MARIO_RED = new Color(255,55,32); //E13720
	public static final Color COLOR_MARIO_SKIN = new Color(248, 208, 192); //F8D0C0
	public static final Color COLOR_MARIO_HAIR = new Color(0,0,0); //000000
	public static final Color COLOR_MARIO_BLUE = new Color(64,128,152); //408098
	public static final Color COLOR_MARIO_SHOES = new Color(136,88,24); //885818
	//Question Mark
	public static final Color COLOR_QUESTIONMARK_DIAMOND = new Color(64,64,64); //404040
	public static final Color COLOR_QUESTIONMARK_BG = new Color(253,214,0); //FDD600
	
}
