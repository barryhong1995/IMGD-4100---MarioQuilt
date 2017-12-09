public class TestArray {

    int[][] tArray;

    public static final Integer LEVEL_WIDTH = 16;
    public static final Integer LEVEL_HEIGHT = 14;
    public static final int PLATFORM = -11; //todo: set to -11
    public static final int GROUND = -10;

    public TestArray(){
        this.tArray = new int[LEVEL_HEIGHT][LEVEL_WIDTH];
        tArray[2][2] = PLATFORM;
        tArray[2][3] = PLATFORM;
        tArray[2][4] = PLATFORM;
        tArray[2][5] = PLATFORM;

        for(int i = 5; i < LEVEL_HEIGHT -1; i++){
            for(int j = 4; j < 8; j++){
                tArray[i][j] = GROUND;
            }
        }
        for(int i = 4; i < LEVEL_HEIGHT -1; i++){
            for(int j = 8; j < 11; j++){
                tArray[i][j] = GROUND;
            }
        }
    }

    public Integer getVal(int x, int y){
        return tArray[y][x];
    }

    public void printArray(){
        for(int i = 0; i < LEVEL_HEIGHT; i++){
            for (int j = 0; j < LEVEL_WIDTH; j++){
                System.out.print(this.getVal(j, i) + " ");
            }
            System.out.println();
        }
    }
}
