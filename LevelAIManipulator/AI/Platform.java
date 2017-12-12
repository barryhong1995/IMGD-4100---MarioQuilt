package AI;

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
