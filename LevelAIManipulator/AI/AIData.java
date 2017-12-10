package AI;

public class AIData {
	private enum gridType {
		NONE, PLATFORM, BLOCK, BRICK, QUESTION, PIPE
	}
	private int levelWidth = 1000;
	private int levelHeight = 1000;
	private char[][] levelScene = new char[1000][1000];
	private Coordinate startLoc = new Coordinate(0, 0);
	private Coordinate endLoc = new Coordinate(0, 0);
	private Platform[] listPlatform = new Platform[1000];
	private int pathCount = 0;
	private int landCount = 0;
	private int offCount = 0;
	
	// Import scene data
	public void importScene(char[][] scene, int width, int height) {
		levelScene = scene;
		levelWidth = width;
		levelHeight = height;
	}
	
	// Analyze data and retrieve all platforms
	public void getPlatforms() {
		for (int j = 0; j < levelHeight; j++) {
			for (int i = 0; i < levelWidth; i++) {
				
			}
		}
	}
	
	// Platform Class
	public class Platform {
		Coordinate start;
		Coordinate end;
		// Constructor
		public Platform(Coordinate start, Coordinate end) {
			this.start = start;
			this.end = end;
		}
		// Calculate length of platform
		public int getLength() {
			int length = end.x - start.x;
			return length;
		}
	}
	
	// Coordinate Class to use for location
	public class Coordinate {
		int x;
		int y;
		// Constructor
		public Coordinate(int x, int y) {
			this.x = x;
			this.y = y;
		}
		// Assign x-coordinate
		public void setX(int x) {
			this.x = x;
		}
		// Assign y-coordinate
		public void setY(int y) {
			this.y = y;
		}
	}
}
