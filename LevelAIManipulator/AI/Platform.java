package AI;

public class Platform {
	Coordinate start;
	Coordinate end;
	int id = 0;
	int[] accessible = new int[100];
	int accessCount = 0;
	
	// Constructor
	public Platform(Coordinate start, Coordinate end, int id) {
		this.start = start;
		this.end = end;
		this.id = id;
	}
	
	// Add new ID to accessible list
	public void addAccessible(int id) {
		accessible[accessCount] = id;
		accessCount++;
	}
	
	// Remove ID from accessible list
	public void removeAccessible(int id) {
		for (int i = 0; i < accessCount; i++) {
			if (accessible[i] == id) {
				accessible[i] = accessible[accessCount - 1];
				accessCount--;
				break; // There should only be one unique ID
			}
		}
	}
	
	// Check whether an ID is in accessible list
	public boolean isAccessible(int id) {
		boolean found = false;
		for (int i = 0; i < accessCount; i++) {
			if (accessible[i] == id) {
				found = true;
				break; // There should only be one unique ID
			}
		}
		return found;
	}
	
	// Check whether a coordinate is part of platform
	public boolean isPlatformPart(int x, int y) {
		if (y != start.y) {
			return false;
		}
		
		if (x > end.x || x < start.x) {
			return false;
		}
		
		return true;
	}
	
	// Calculate length of platform
	public int getLength() {
		int length = end.x - start.x;
		return length;
	}
	
	// Get start coordinate
	public Coordinate getStart() {
		return start;
	}

	// Get end coordinate 
	public Coordinate getEnd() {
		return end;
	}

	// Get platform ID
	public int getID() {
		return id;
	}
	
	// Get list of accessible platforms
	public int[] getAccessList() {
		return accessible;
	}
	
	// Get number of accessible platforms
	public int getAccessCount() {
		return accessCount;
	}
}
