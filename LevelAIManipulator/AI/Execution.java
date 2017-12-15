package AI;

import java.io.*;

public class Execution {
	public static void main(String args[]) {
		char[][] rawLevelScene = new char[1000][1000];
		AIData ai = new AIData();
		try {
			// Read level data from file
			BufferedReader fileInput = new BufferedReader(new FileReader("./LevelAIManipulator/TestLevel/testLevel.txt"));
			String s;
			int maxWidth = 0;
			int height = 0;
			while ((s = fileInput.readLine()) != null) {
				int width = 0;
				for (char c : s.toCharArray()) {
					if (c != ' ') {
						rawLevelScene[width][height] = c;
						width++;
					}
				}
				if (width > maxWidth) maxWidth = width;
				height++;
			}
			fileInput.close();
			// Report status that file is read
			System.out.println("File is imported successfully!");
			ai.importScene(rawLevelScene, maxWidth, height);
			ai.getPlatformData();
			System.out.println("Number of platforms found: " + ai.getLandCount());
			System.out.println(ai.testJump(ai.getPlatformData()[2], ai.getPlatformData()[0]));
		} catch (FileNotFoundException e) {
			// Report status that file is not found
			System.out.println("File is missing!");
		} catch (IOException e) {
			// Report error of file
			System.out.println("File cannot be read!");
		}
	}
}
