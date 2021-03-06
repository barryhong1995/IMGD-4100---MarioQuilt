package AI;

import java.util.*;

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
	private ArrayList<ArrayList<Integer>> possiblePaths = new ArrayList<ArrayList<Integer>>();
	private int pathCount = 0;
	private int landCount = 0;

	// Import scene data
	public void importScene(char[][] scene, int width, int height) {
		levelWidth = width;
		levelHeight = height;
		convertScene(scene);
		startLoc = getStartLocation();
		endLoc = getEndLocation();
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
								//temp--;
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
		linkPlatformSystem();
		return listPlatform;
	}

	// Try to go through the level from start to end by traveling through platforms
	// Return true if level is playable
	// Return false if level is not playable
	public boolean testClearLevel() {
		int startID = 0;
		int endID = 0;
		// Find platform that is nearest and below start position and assign it as starting platform
		boolean foundStart = false;
		for (int i = startLoc.y; i < levelHeight; i++) {
			for (int j = 0; j < landCount; j++) {
				if (listPlatform[j].isPlatformPart(startLoc.x, i)) {
					startID = listPlatform[j].getID();
					foundStart = true;
					break;
				}
			}
			if (foundStart) break;
		}
		// Find platform that is nearest and below end position and assign it as starting platform
		boolean foundEnd = false;
		for (int i = endLoc.y; i < levelHeight; i++) {
			for (int j = 0; j < landCount; j++) {
				if (listPlatform[j].isPlatformPart(endLoc.x, i)) {
					endID = listPlatform[j].getID();
					foundEnd = true;
					break;
				}
			}
			if (foundEnd) break;
		}
		// If both start and end locations are available, continue
		// Else throw an error message
		if (foundStart && foundEnd) {
			ArrayList<Integer> path = new ArrayList<Integer>();
			path.add(startID);
			
			// Case of same platform
			if (startID == endID) {
				ArrayList<Integer> singlePath = new ArrayList<Integer>();
				singlePath.add(startID);
				possiblePaths.add(singlePath);
				pathCount = 1;
				return true;
			}
			
			// Else, perform reachable recursive
			reachable(startID, endID, path);
			if (pathCount > 0) {
				return true;		// Level can be cleared
			} else return false;	// Level cannot be cleared
		} else System.out.println("Missing Start and/or End location!");
		return false;
	}
	
	// Helper function for pathfinding from start to end
	public void reachable(int ps, int pe, ArrayList<Integer> currentPath) {
		for (int i = 0; i < landCount; i++) {
			if (listPlatform[i].getID() == ps) {	// Found source platform
				boolean fullDuplicate = true;
				for (int j = 0; j < listPlatform[i].getAccessCount(); j++) {
					// If there exists a platform that can be accessed but not yet passed, path is available
					if (!currentPath.contains(listPlatform[i].getAccessList()[j])) {
						ArrayList<Integer> tempPaths = new ArrayList<Integer>();
						fullDuplicate = false;
						// Add to list of current path
						for (int platform : currentPath) {
							tempPaths.add(platform);
						}
						tempPaths.add(listPlatform[i].getAccessList()[j]);
						// Check whether target platform is reached
						// Else continue to traverse
						if (listPlatform[i].getAccessList()[j] == pe) {
							currentPath.add(listPlatform[i].getAccessList()[j]);
							possiblePaths.add(currentPath);
							pathCount++;
						}
						else reachable(listPlatform[i].getAccessList()[j], pe, tempPaths);
					}
				}
				if (fullDuplicate) break;
				break;	// Found, so no point continuing the loop
			}
		}
	}
	
	// Make platform network that branches reachable
	public void linkPlatformSystem() {
		for (int i = 0; i < landCount; i++) {
			for (int j = 0; j < landCount; j++) {
				// Make sure we are checking 2 different platforms
				if (listPlatform[i].getID() != listPlatform[j].getID()) {
					// Check jump from p1 and p2 and make sure that it has not been recorded
					if (testJump(listPlatform[i], listPlatform[j]) && !listPlatform[i].isAccessible(listPlatform[j].getID())) {
						listPlatform[i].addAccessible(listPlatform[j].getID());	// Record accessible (p1 can reach p2)
					}
				}
			}
		}
	}
	
	// Test accessible by making a jump between two platforms
	public boolean testJump(Platform startPlatform, Platform endPlatform) {
		// Get start and end coordinates of 2 platforms
		int xS1, xS2, xE1, xE2, y1, y2, tick;
		Coordinate mario = new Coordinate(0, 0);
		Coordinate origLoc = new Coordinate(0, 0);
		boolean landSuccessful = false;
		boolean collision = false;

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

		// Try jumping at one spot along the the start platform
		// Put Mario at the start of the start-platform
		mario.setX(xS1);
		mario.setY(y1 - 1);
		for (int i = xS1; i <= xE1; i++) {
			tick = 0;
			landSuccessful = false;
			collision = false;
			origLoc = new Coordinate(mario.x, mario.y);
			while (!collision) {
				if (tick <= 4) mario.setY(mario.y - 1);
				else mario.setY(mario.y + 1);
				// Check whether Mario is about to land on a platform
				for (int j = 0; j < landCount; j++) {
					if (listPlatform[j].isPlatformPart(mario.x, mario.y)) {
						if (checkJumpPass(levelScene[mario.x][mario.y])) {
							if (listPlatform[j].getID() == endPlatform.getID()) {
								collision = true; // To exit the loop
								landSuccessful = true;
								break;
							}
						} else collision = true;
					}
				}
				tick++;
				// Fail to land on anything, break
				if (tick == 9 || landSuccessful) break;
			}
			origLoc.setX(origLoc.x + 1);
			mario.setX(origLoc.x);
			mario.setY(origLoc.y);
			if (landSuccessful) break;
		}
		// Return true if previous one-spot jump attempt is successful
		if (landSuccessful) return landSuccessful;

		// Continue jumping with direction if jumping at one spot failed
		// Determine direction of the jump
		// 0 - LEFT, 1 - RIGHT
		int dir;
		if (xS1 < xS2) dir = 1; // Jump to the right
		else dir = 0;			// Jump to the left

		// Test jump position in forward direction
		if (dir == 1) {
			// Small jump
			//   X
			// X X X
			// X X X
			// Put Mario at the start of the start-platform
			mario.setX(xS1);
			mario.setY(y1 - 1);
			// Make small jump
			for (int i = xS1; i <= xE1; i++) {
				tick = 0;
				landSuccessful = false;
				collision = false;
				origLoc = new Coordinate(mario.x, mario.y);
				while (!collision) {
					tick++;
					if (tick <= 2) mario.setY(mario.y - 1);
					if (tick > 2) mario.setY(mario.y + 1);
					if (tick == 2 || tick == 3) mario.setX(mario.x + 1);
					if (tick == 4) break;
					if (mario.y < 0) mario.setY(0);
					if (mario.x == levelWidth) {
						mario.setX(levelWidth - 1);
					}
					// Check for collision
					for (int j = 0; j < landCount; j++) {
						if (listPlatform[j].isPlatformPart(mario.x, mario.y)) {
							if (tick <= 2 && checkJumpPass(levelScene[mario.x][mario.y])) {
								continue;
							} else {
								collision = true;
								break;
							}
						}
					}
					// Check for landing
					if (!collision) {
						for (int j = mario.y; j < levelHeight - 1; j++) {
							if (checkLand(levelScene[mario.x][j + 1])) {
								if (endPlatform.isPlatformPart(mario.x, j + 1)) {
									landSuccessful = true;
									collision = true; // To exit the loop
									break;
								} else break;
							}
						}
					}
					if (landSuccessful) break;
				}
				origLoc.setX(origLoc.x + 1);
				mario.setX(origLoc.x);
				mario.setY(origLoc.y);
				if (landSuccessful) break;
			}
			// Return true if previous small-jump is successful
			if (landSuccessful) return landSuccessful;
			
			// Half jump
			//   X
			// X X X
			// X X X
			// X X X
			// X X X

			// Put Mario at the start of the start-platform
			mario.setX(xS1);
			mario.setY(y1 - 1);
			// Make half jump
			for (int i = xS1; i <= xE1; i++) {
				tick = 0;
				landSuccessful = false;
				collision = false;
				origLoc = new Coordinate(mario.x, mario.y);
				while (!collision) {
					tick++;
					if (tick <= 4) mario.setY(mario.y - 1);
					if (tick > 4) mario.setY(mario.y + 1);
					if (tick == 4 || tick == 5) mario.setX(mario.x + 1);
					if (tick == 9) break;
					if (mario.y < 0) mario.setY(0);
					if (mario.x == levelWidth) {
						mario.setX(levelWidth - 1);
					}
					// Check for collision
					for (int j = 0; j < landCount; j++) {
						if (listPlatform[j].isPlatformPart(mario.x, mario.y)) {
							if (tick <= 4 && checkJumpPass(levelScene[mario.x][mario.y])) {
								continue;
							} else {
								collision = true;
								break;
							}
						}
					}
					// Check for landing
					if (!collision) {
						for (int j = mario.y; j < levelHeight - 1; j++) {
							if (checkLand(levelScene[mario.x][j + 1])) {
								if (endPlatform.isPlatformPart(mario.x, j + 1)) {
									landSuccessful = true;
									collision = true; // To exit the loop
									break;
								} else break;
							}
						}
					}
					if (landSuccessful) break;
				}
				origLoc.setX(origLoc.x + 1);
				mario.setX(origLoc.x);
				mario.setY(origLoc.y);
				if (landSuccessful) break;
			}
			// Return true if previous half-jump is successful
			if (landSuccessful) return landSuccessful;

			// Full jump
			//     X X 
			//   X X X X
			//   X X X X 
			// X X X X X X 
			// X X X X X X 

			// Put Mario at the start of the start-platform again
			mario.setX(xS1);
			mario.setY(y1 - 1);
			// Make full jump
			for (int i = xS1; i <= xE1; i++) {
				tick = 0;
				landSuccessful = false;
				collision = false;
				origLoc = new Coordinate(mario.x, mario.y);
				while (!collision) {
					tick++;
					if (tick < 5) mario.setY(mario.y - 1);
					if (tick > 5) mario.setY(mario.y + 1);
					if (tick == 2 || tick == 4 || tick == 5 || tick == 6 || tick == 8) mario.setX(mario.x + 1); 
					if (tick == 9) break;
					if (mario.y < 0) mario.setY(0);
					if (mario.x == levelWidth) mario.setX(levelWidth - 1);

					// Check for collision
					for (int j = 0; j < landCount; j++) {
						if (listPlatform[j].isPlatformPart(mario.x, mario.y)) {
							if (tick < 5 && checkJumpPass(levelScene[mario.x][mario.y])) {
								continue;
							} else {
								collision = true;
								break;
							}
						}
					}
					// Check for landing
					if (!collision) {
						for (int j = mario.y; j < levelHeight - 1; j++) {
							if (checkLand(levelScene[mario.x][j + 1])) {
								if (endPlatform.isPlatformPart(mario.x, j + 1)) {
									landSuccessful = true;
									collision = true; // To exit the loop
									break;
								} else break;
							}
						}
					}

					if (landSuccessful) break;
				}
				origLoc.setX(origLoc.x + 1);
				mario.setX(origLoc.x);
				mario.setY(origLoc.y);
				if (landSuccessful) break;
			}

			// Return true if previous full jump is successful
			if (landSuccessful) return landSuccessful;
		}

		// Test jump position in backward direction
		if (dir == 0) {
			// Small jump
			//   X
			// X X X
			// X X X
			// Put Mario at the start of the start-platform
			mario.setX(xE1);
			mario.setY(y1 - 1);
			// Make small jump
			for (int i = xE1; i >= xS1; i--) {
				tick = 0;
				landSuccessful = false;
				collision = false;
				origLoc = new Coordinate(mario.x, mario.y);
				while (!collision) {
					tick++;
					if (tick <= 2) mario.setY(mario.y - 1);
					if (tick > 2) mario.setY(mario.y + 1);
					if (tick == 2 || tick == 3) mario.setX(mario.x - 1);
					if (tick == 4) break;
					if (mario.y < 0) mario.setY(0);
					if (mario.x < 0) mario.setX(0);
					// Check for collision
					for (int j = 0; j < landCount; j++) {
						if (listPlatform[j].isPlatformPart(mario.x, mario.y)) {
							if (tick <= 2 && checkJumpPass(levelScene[mario.x][mario.y])) {
								continue;
							} else {
								collision = true;
								break;
							}
						}
					}
					// Check for landing
					if (!collision) {
						for (int j = mario.y; j < levelHeight - 1; j++) {
							if (checkLand(levelScene[mario.x][j + 1])) {
								if (endPlatform.isPlatformPart(mario.x, j + 1)) {
									landSuccessful = true;
									collision = true; // To exit the loop
									break;
								} else break;
							}
						}
					}
					if (landSuccessful) break;
				}
				origLoc.setX(origLoc.x - 1);
				mario.setX(origLoc.x);
				mario.setY(origLoc.y);
				if (landSuccessful) break;
			}
			// Return true if previous small-jump is successful
			if (landSuccessful) return landSuccessful;
			
			// Half jump
			//   X
			// X X X
			// X X X
			// X X X
			// X X X

			// Put Mario at the end of the start-platform
			mario.setX(xE1);
			mario.setY(y1 - 1);
			// Make half jump
			for (int i = xE1; i >= xS1; i--) {
				tick = 0;
				landSuccessful = false;
				collision = false;
				origLoc = new Coordinate(mario.x, mario.y);
				while (!collision) {
					tick++;
					if (tick <= 4) mario.setY(mario.y - 1);
					if (tick > 4) mario.setY(mario.y + 1);
					if (tick == 4 || tick == 5) mario.setX(mario.x - 1);
					if (tick == 9) break;
					if (mario.y < 0) mario.setY(0);
					if (mario.x < 0) mario.setX(0);

					// Check for collision
					for (int j = 0; j < landCount; j++) {
						if (listPlatform[j].isPlatformPart(mario.x, mario.y)) {
							if (tick <= 4 && checkJumpPass(levelScene[mario.x][mario.y])) {
								continue;
							} else {
								collision = true;
								break;
							}
						}
					}
					// Check for landing
					if (!collision) {
						for (int j = mario.y; j < levelHeight - 1; j++) {
							if (checkLand(levelScene[mario.x][j + 1])) {
								if (endPlatform.isPlatformPart(mario.x, j + 1)) {
									landSuccessful = true;
									collision = true; // To exit the loop
									break;
								} else break;
							}
						}
					}

					if (landSuccessful) break;
				}
				origLoc.setX(origLoc.x - 1);
				mario.setX(origLoc.x);
				mario.setY(origLoc.y);
				if (landSuccessful) break;
			}
			// Return true if previous half-jump is successful
			if (landSuccessful) return landSuccessful;

			// Full jump
			//     X X 
			//   X X X X
			//   X X X X 
			// X X X X X X 
			// X X X X X X 

			// Put Mario at the end of the start-platform again
			mario.setX(xE1);
			mario.setY(y1 - 1);
			// Make full jump
			for (int i = xE1; i >= xS1; i--) {
				tick = 0;
				landSuccessful = false;
				collision = false;
				origLoc = new Coordinate(mario.x, mario.y);
				while (!collision) {
					tick++;
					if (tick < 5) mario.setY(mario.y - 1);
					if (tick > 5) mario.setY(mario.y + 1);
					if (tick == 2 || tick == 4 || tick == 5 || tick == 6 || tick == 8) mario.setX(mario.x - 1); 
					if (tick == 9) break;
					if (mario.y < 0) mario.setY(0);
					if (mario.x < 0) mario.setX(0);

					// Check for collision
					for (int j = 0; j < landCount; j++) {
						if (listPlatform[j].isPlatformPart(mario.x, mario.y)) {
							if (tick < 5 && checkJumpPass(levelScene[mario.x][mario.y])) {
								continue;
							} else {
								collision = true;
								break;
							}
						}
					}
					// Check for landing
					if (!collision) {
						for (int j = mario.y; j < levelHeight - 1; j++) {
							if (checkLand(levelScene[mario.x][j + 1])) {
								if (endPlatform.isPlatformPart(mario.x, j + 1)) {
									landSuccessful = true;
									collision = true; // To exit the loop
									break;
								} else break;
							}
						}
					}
					if (landSuccessful) break;
				}
				origLoc.setX(origLoc.x - 1);
				mario.setX(origLoc.x);
				mario.setY(origLoc.y);
				if (landSuccessful) break;
			}

			// Return true if previous full jump is successful
			if (landSuccessful) return landSuccessful;
		}

		return landSuccessful;
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
	
	// Return number of paths that can clear the level
	public int getPathCount() {
		return pathCount;
	}
	
	// Return list of possible paths
	public ArrayList<ArrayList<Integer>> getPossiblePaths() {
		return possiblePaths;
	}
}
