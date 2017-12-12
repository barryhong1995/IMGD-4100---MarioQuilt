package AI;

public class AIData {
	private enum gridType {
		NONE, PLATFORM, BLOCK, BRICK, QUESTION, PIPE
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
		for (int j = 1; j < levelHeight; j++) {
			for (int i = 0; i < levelWidth; i++) {
				// Check whether the grid is a land grid
				if (checkLand(levelScene[i][j]) && !checkLand(levelScene[i][j-1])) {
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
						Coordinate end = new Coordinate(temp, j);
						listPlatform[landCount] = new Platform(start, end);
						landCount++;
					}
					// Skip forward the iterator
					i = temp;
				}
			}
		}
		return listPlatform;
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
		case PIPE:
			checker = false;
			break;
		default:
			checker = true;
			break;	
		}
		return checker;
	}
	
	// Check whether a grid can be passed through jumping
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
		case PIPE:
			checker = false;
			break;
		default:
			checker = true;
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
				default:
					levelScene[i][j] = gridType.NONE;
					break;
				}
			}
		}
	}
}
