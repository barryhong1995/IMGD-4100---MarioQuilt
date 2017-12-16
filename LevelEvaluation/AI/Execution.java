package AI;

import java.io.*;

public class Execution {
	public static void main(String args[]) {
		char[][] rawLevelScene = new char[1000][1000];
		Platform[] platforms = new Platform[1000];
		AIData ai = new AIData();
		try {
			// Read level data from file
			BufferedReader fileInput = new BufferedReader(new FileReader("./LevelEvaluation/TestLevel/testLevel.txt"));
			// Print out result from console
			PrintStream out = new PrintStream(new FileOutputStream("level_evaluation.txt"));
			System.setOut(out);
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
			platforms = ai.getPlatformData();
			System.out.println("Number of platforms found: " + ai.getLandCount());
			System.out.println("Branch System of Platforms:");
			for (int i = 0; i < ai.getLandCount(); i++) {
				System.out.print("Platform " + platforms[i].getID() + ": ");
				for (int j = 0; j < platforms[i].getAccessCount(); j++) {
					System.out.print(platforms[i].getAccessList()[j] + " ");
				}
				System.out.println();
			}
			if (ai.testClearLevel()) {
				System.out.println("Level can be cleared!");
				System.out.println("Number of paths: " + ai.getPathCount());
				System.out.println(ai.getPossiblePaths());
			} else System.out.println("Level cannot be cleared!");
		} catch (FileNotFoundException e) {
			// Report status that file is not found
			System.out.println("File is missing!");
		} catch (IOException e) {
			// Report error of file
			System.out.println("File cannot be read!");
		}
	}
}
