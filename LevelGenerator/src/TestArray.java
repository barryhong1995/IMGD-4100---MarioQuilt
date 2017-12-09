public class TestArray {

    int[][] tArray;

    public static final Integer LEVEL_WIDTH = 16;
    public static final Integer LEVEL_HEIGHT = 14;
    public static final int GROUND = 1;

    public TestArray(){
        this.tArray = new int[LEVEL_HEIGHT][LEVEL_WIDTH];
        tArray[2][2] = GROUND;
        tArray[2][3] = GROUND;
        tArray[2][4] = GROUND;
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
