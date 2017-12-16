package generator;

import java.awt.Color;

public class Sprite {
	////////////////BLOCK IDS
	////MARIO
	public static final int ID_MARIO = -1;
	public static final int ID_END = -500;
	
	////SPECIAL BLOCKS
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
	//Grass
	public static final int ID_GRASS_TL = 152;
	public static final int ID_GRASS_TM = 161;
	public static final int ID_GRASS_TR = 153;
	public static final int ID_GRASS_ML = 154;
	public static final int ID_GRASS_MM = 164; //just plain dirt
	public static final int ID_GRASS_MR = 155;
	public static final int ID_GRASS_BL = 89;
	public static final int ID_GRASS_BM = 90;
	public static final int ID_GRASS_BR = 91;
	//Dirt
	public static final int ID_DIRT1 = 164;
	public static final int ID_DIRT2 = 219;
	public static final int ID_DIRT3 = 220;
	////Pipes
	//Horizontal
	public static final int ID_PIPE_HORIZ_TL = 11;
	public static final int ID_PIPE_HORIZ_BL = 27;
	public static final int ID_PIPE_HORIZ_TM = 12;
	public static final int ID_PIPE_HORIZ_BM = 28;
	public static final int ID_PIPE_HORIZ_TR = 13;
	public static final int ID_PIPE_HORIZ_BR = 29;
	//Vertical
	public static final int ID_PIPE_VERTI_TL = 14;
	public static final int ID_PIPE_VERTI_TR = 15;
	public static final int ID_PIPE_VERTI_ML = 30;
	public static final int ID_PIPE_VERTI_MR = 31;
	public static final int ID_PIPE_VERTI_BL = 46;
	public static final int ID_PIPE_VERTI_BR = 47;
	
	////////////////Colors
	public static final Color COLOR_BORDER = new Color(255,0,110);
	//Mario
	public static final Color COLOR_MARIO_RED = new Color(255,55,32); //E13720
	public static final Color COLOR_MARIO_SKIN = new Color(248, 208, 192); //F8D0C0
	public static final Color COLOR_MARIO_HAIR = new Color(0,0,0); //000000
	public static final Color COLOR_MARIO_BLUE = new Color(64,128,152); //408098
	public static final Color COLOR_MARIO_SHOES = new Color(136,88,24); //885818
	//Question Block
	public static final Color COLOR_QUESTION_DIAMOND = new Color(192,192,192); //C0C0C0
	public static final Color COLOR_QUESTION_BG = new Color(253,214,0); //FDD600
	//Question Block
	public static final Color COLOR_MUSIC_NOTE = new Color(89,89,89); //595959
	public static final Color COLOR_MUSIC_BG = new Color(221,221,221); //DDDDDD
	//Pipes
	public static final Color COLOR_PIPE_LIGHT = new Color(195,255,29); //C3FF1D
	public static final Color COLOR_PIPE_GREEN = new Color(80,205,0); //50CD00
	public static final Color COLOR_PIPE_DARK = new Color(27,147,19); //1B9313
	//Face Blocks
	public static final Color COLOR_FACE_EYES = new Color(64,64,64); //404040
	public static final Color COLOR_FACE_YELLOW = new Color(248,216,32); //F8D820
	public static final Color COLOR_FACE_BROWN = new Color(136,88,24); //885818
	public static final Color COLOR_CLOUD_WHITE = new Color(242,242,242); //F2F2F2
	public static final Color COLOR_CLOUD_MOUTH = new Color(248,0,128); //F80080
	//Coin
	public static final Color COLOR_COIN = new Color(248,192,0); //F8C000
	//Sky
	public static final Color COLOR_SKY = new Color(173,213,248); //ADD5F8
	//Grass
	public static final Color COLOR_GRASS_LIGHT = new Color(0,200,0); //00C800
	public static final Color COLOR_GRASS_DARK = new Color(0,128,0); //008000
	public static final Color COLOR_DIRT_LIGHT = new Color(216,160,56); //D8A038
	public static final Color COLOR_DIRT_DARK = new Color(136,88,24); //885818
	//Rock
	public static final Color COLOR_ROCK_LIGHT = new Color(192,192,192); //C0C0C0
	public static final Color COLOR_ROCK_DARK = new Color(112,112,112); //707070
	
	private static int M = ID_MARIO, m = ID_MUSIC_NOTE, C = ID_CLOUD, c = ID_COIN, D = ID_GRASS_MM;
	private static int Y = ID_FACE_YELLOW, B = ID_FACE_BROWN, R = ID_ROCK, Q = ID_QUESTION_BLOCK;
	private static int TL = ID_GRASS_TL, TM = ID_GRASS_TM, TR = ID_GRASS_TR, ML = ID_GRASS_ML;
	private static int BL = ID_GRASS_BL, BM = ID_GRASS_BM, BR = ID_GRASS_BR, MR = ID_GRASS_MR;
	private static int PVTL = ID_PIPE_VERTI_TL, PVTR = ID_PIPE_VERTI_TR;
	private static int PVML = ID_PIPE_VERTI_ML, PVMR = ID_PIPE_VERTI_MR;
	private static int PVBL = ID_PIPE_VERTI_BL, PVBR = ID_PIPE_VERTI_BR;
	private static int PHTL = ID_PIPE_HORIZ_TL, PHTM = ID_PIPE_HORIZ_TM, PHTR = ID_PIPE_HORIZ_TR;
	private static int PHBL = ID_PIPE_HORIZ_BL, PHBM = ID_PIPE_HORIZ_BM, PHBR = ID_PIPE_HORIZ_BR;
	
	public static final int[][] TEST_LEVEL = {
			{0, 	0,	0,		0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},
			{c, 	c,	c,		0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	c,	c,	c,	c,	c,	0},
			{C, 	C,	C,		C,	C,	C,	C,	0,	0,	C,	C,	C,	C,	C,	C,	C,	C,	C,	C,	C,	C,	C,	C,	C},
			{0, 	0,	0,		0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},
			{c, 	0,	0,		0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},
			{c, 	0,	0,		0,	0,	0,	0,	0,	0,	0,	0,	0,	m,	m,	m,	m,	0,	0,	0,	0,	0,	0,	0,	0},
			{R, 	c,	0,		0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},
			{R, 	R,	0,		0,	0,	0,	Y,	Y,	B,	Y,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},
			{TM,	TM,	TM,		TM,	TR,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	Y,	Q,	Y,	Y,	0,	0,	0,	0},
			{D, 	D,	D,		D,	MR,	0,	0,	0,	M,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},
			{D, 	D,	D,		D,	MR,	0,	0,	0,	M,	0,	0,	Q,	Q,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},
			{D, 	D,	D,		D,	TL,	TM,	TM,	TM,	TM,	TR,	0,	0,	0,	0,	0,	TL,	TM,	TM,	TM,	TM,	TR,	0,	0,	0},
			{D, 	PVTL,PVTR,	D,	ML,	D,	D,	D,	D,	MR,	0,	0,	0,	0,	0,	ML,	D,	D,	D,	D,	MR,	0,	0,	0},
			{TR,	PVML,PVMR,	TL,	TM,	TM,	TM,	TM,	TM,	TM,	TM,	TM,	TM,	TM,	TM,	D,	D,	D,	D,	D,	D,	TM,	TM,	TM},
			{MR,	PVML,PVMR,	ML,	D,	D,	D,	D,	D,	D,	D,	D,	D,	D,	D,	D,	D,	D,	D,	D,	D,	D,	D,	D},
	};
	
}
