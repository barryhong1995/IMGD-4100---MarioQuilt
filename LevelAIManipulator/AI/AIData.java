package AI;

import java.io.File;

public class AIData {
	private enum gridType {
		NONE, PLATFORM, BLOCK, BRICK, QUESTION, PIPE, START, END
	}
	private int levelWidth = 1000;
	private int levelHeight = 1000;
	private gridType[][] levelScene = new gridType[1000][1000];
	private Coordinate startLoc = new Coordinate(0, 0);
	private Coordinate endLoc = new Coordinate(0, 0);
	private Platform[] listPlatform = new Platform[1000];
	private int pathCount = 0;
	private int landCount = 0;
	private int offCount = 0;

	// Import scene data
	public void importScene(char[][] scene, int width, int height) {
		levelWidth = width;
		levelHeight = height;
		convertScene(scene);
	}

	// Retrieve platform data from the scene
	public Platform[] getPlatformData() {
		boolean samePlatform = false;
		for (int j = 1; j < levelHeight; j++) {
			for (int i = 0; i < levelWidth; i++) {
				// Check whether the grid is a land grid
				if (checkLand(levelScene[i][j]) && !checkLand(levelScene[i][j-1])) {
					if (samePlatform) {
						continue;
					} else {
						samePlatform = true;
						// Run along the length of the platform
						int temp = i;
						while (checkLand(levelScene[temp][j]) && !checkLand(levelScene[temp][j-1])) {
							temp++;
							if (temp == levelWidth) {
								temp--;
								break;
							}
						}
						// Save platform to array
						if (temp != i) {
							Coordinate start = new Coordinate(i, j);
							Coordinate end = new Coordinate(temp-1, j);
							listPlatform[landCount] = new Platform(start, end, landCount);
							landCount++;
						}
					}
				} else samePlatform = false;
			}
			samePlatform = false;
		}
		return listPlatform;
	}

	// Test accessible by making a jump between two platforms
	// Test accessible by making a jump between two platforms
	public boolean testJump(Platform startPlatform, Platform endPlatform) {
		// Get start and end coordinates of 2 platforms
		int xS1, xS2, xE1, xE2, y1, y2;

		xS1 = startPlatform.getStart().x;
		xS2 = endPlatform.getStart().x;
		xE1 = startPlatform.getEnd().x;
		xE2 = endPlatform.getEnd().x;

		y1 = startPlatform.getStart().y;
		y2 = endPlatform.getStart().y;

		// Check whether ending platform is too high
		if (y1 - y2 > 4) return false;

		// Check whether the ending platform is too far
		if ((xS2 - xE1 > 5) || (xS1 - xE2 > 5)) return false;

		// Define x-bound region
		int boundSX = xS1 < xS2 ? xS2 : xS1;
		int boundEX = xE2 > xE1 ? xE1 : xE2;

		// Determine jumping point
		Coordinate jumpPoint = new Coordinate(0, 0);
		if (boundSX >= xS1 && boundSX <= xE1) jumpPoint.setX(boundSX);
		else jumpPoint.setX(boundEX);
		jumpPoint.setY(y1 + 1);

		// Try jumping at one spot
		boolean landSuccessful = false;
		// Set coordinate for Mario AI
		Coordinate mario = jumpPoint;
		int tick = 0;
		while (!landSuccessful) {
			if (tick <= 4)  {
				mario.setX(mario.y - 1);	// Rising
			} else mario.setX(mario.y + 1);	// Falling
			// Check whether Mario is about to land on a platform
			for (Platform p : listPlatform) {
			}
			tick++;
			// Fail to land on anything, break
			if (tick == 9) break;
		}

		// Jumping Region:
		// X     X X X
		// X   X X X X X
		// X X X X X X X X
		// X X X X X X X X X
		// X X X X X X X X X


		return true;
	}

	// Retrieve number of land count
	public int getLandCount() {
		return landCount;
	}

	// Retrieve starting position
	public Coordinate getStartLocation() {
		boolean foundLoc = false;
		for (int j = 0; j < levelHeight; j++) {
			for (int i = 0; i < levelWidth; i++) {
				// Get only the first start location (should be only 1), the other duplication will be nullified
				if (levelScene[i][j] == gridType.START) {
					if (foundLoc) {
						levelScene[i][j] = gridType.NONE;
					} else {
						foundLoc = true;
						startLoc = new Coordinate(i, j);
					}
				}
			}
		}
		return startLoc;
	}

	// Retrieve ending position
	public Coordinate getEndLocation() {
		boolean foundLoc = false;
		for (int j = 0; j < levelHeight; j++) {
			for (int i = 0; i < levelWidth; i++) {
				// Get only the first end location (should be only 1), the other duplication will be nullified
				if (levelScene[i][j] == gridType.END) {
					if (foundLoc) {
						levelScene[i][j] = gridType.NONE;
					} else {
						foundLoc = true;
						endLoc = new Coordinate(i, j);
					}
				}
			}
		}
		return endLoc;
	}

	// Check whether a grid can be landed
	public boolean checkLand(gridType type) {
		boolean checker = false;
		switch (type) {
		case NONE:
			checker = false;
			break;
		case PLATFORM:
			checker = true;
			break;
		case BLOCK:
			checker = true;
			break;
		case BRICK:
			checker = true;
			break;
		case QUESTION:
			checker = true;
			break;
		case PIPE:
			checker = true;
			break;
		default:
			checker = false;
			break;	
		}
		return checker;
	}

	// Check whether a grid can be passed through jumping
	public boolean checkJumpPass(gridType type) {
		boolean checker = true;
		switch (type) {
		case NONE:
			checker = true;
			break;
		case PLATFORM:
			checker = true;
			break;
		case BLOCK:
			checker = false;
			break;
		case BRICK:
			checker = false;
			break;
		case QUESTION:
			checker = false;
			break;
		case PIPE:
			checker = false;
			break;
		default:
			checker = true;
			break;	
		}
		return checker;
	}

	// Check whether a grid can be passed through walking
	public boolean checkWalkPass(gridType type) {
		boolean checker = true;
		switch (type) {
		case NONE:
			checker = true;
			break;
		case PLATFORM:
			checker = false;
			break;
		case BLOCK:
			checker = false;
			break;
		case BRICK:
			checker = false;
			break;
		case QUESTION:
			checker = false;
			break;
		case PIPE:
			checker = false;
			break;
		default:
			checker = true;
			break;	
		}
		return checker;
	}

	// Check whether a grid can be broken
	public boolean checkBreakable(gridType type) {
		boolean checker = true;
		switch (type) {
		case NONE:
			checker = false;
			break;
		case PLATFORM:
			checker = false;
			break;
		case BLOCK:
			checker = false;
			break;
		case BRICK:
			checker = true;
			break;
		case QUESTION:
			checker = false;
			break;
		case PIPE:
			checker = false;
			break;
		default:
			checker = false;
			break;	
		}
		return checker;
	}

	// Convert level scene data from char to grid type
	public void convertScene(char[][] sceneChar) {
		for (int j = 0; j < levelHeight; j++) {
			for (int i = 0; i < levelWidth; i++) {
				switch (sceneChar[i][j]) {
				case 'O':
					levelScene[i][j] = gridType.NONE;
					break;
				case 'L':
					levelScene[i][j] = gridType.PLATFORM;
					break;
				case 'X':
					levelScene[i][j] = gridType.BLOCK;
					break;
				case 'B':
					levelScene[i][j] = gridType.BRICK;
					break;
				case '?':
					levelScene[i][j] = gridType.QUESTION;
					break;
				case 'P':
					levelScene[i][j] = gridType.PIPE;
					break;
				case 'S':
					levelScene[i][j] = gridType.START;
					break;
				case 'E':
					levelScene[i][j] = gridType.END;
					break;
				default:
					levelScene[i][j] = gridType.NONE;
					break;
				}
			}
		}
	}
}
