package Rendering;

import java.util.Random;

public class TestArray {

    int[][] tArray;

    public static final Integer LEVEL_WIDTH = 100;
    public static final Integer LEVEL_HEIGHT = 18;
    public static final int PLATFORM = -11; //todo: set to -11
    public static final int GROUND = -10;
    public static final int PIPE = 20;

    public static final int AVG_GROUND_HEIGHT = 5;
    public static final int MAX_GROUND_OFFSET = 3;
    public static final int MAX_GAP_LENGTH = 5;
    public static final int OFFSET_CHANCE = 10; //out of 100
    public static final int GAP_CHANCE = 5; //out of 100


    public TestArray(){
        this.tArray = new int[LEVEL_HEIGHT + 1][LEVEL_WIDTH + 1];

//
//        for(int i = 5; i < LEVEL_HEIGHT -1; i++){
//            for(int j = 4; j < 8; j++){
//                tArray[i][j] = GROUND;
//            }
//        }
//        for(int i = 4; i < LEVEL_HEIGHT -1; i++){
//            for(int j = 8; j < 11; j++){
//                tArray[i][j] = GROUND;
//            }
//        }
    }

    public void buildGround(){
        Random ran = new Random();

        int curr = AVG_GROUND_HEIGHT;
        int offset_chance;
        int gap_chance;
        int len_max = 0;
        int count = 0;

        for(int i = 1; i < 10; i++){
            drawCol(i, curr, GROUND);
        }

        for(int i = 10; i < LEVEL_WIDTH; i++) {
            drawCol(i, curr, GROUND);
            if(curr == AVG_GROUND_HEIGHT) {
                offset_chance = ran.nextInt(100);
                gap_chance = ran.nextInt(100);

                if (offset_chance <= OFFSET_CHANCE && LEVEL_WIDTH - i >= 5) {
                    curr += (-1 * MAX_GROUND_OFFSET) + ran.nextInt(2 * MAX_GROUND_OFFSET);
                    len_max = ran.nextInt(Math.min(18, Math.max(0, LEVEL_WIDTH - i - 5)));
                }
                if (gap_chance <= GAP_CHANCE && LEVEL_WIDTH - i >= 5) {
                    curr = 0;
                    len_max = ran.nextInt(2 * MAX_GAP_LENGTH);

                    if (len_max > MAX_GAP_LENGTH) {
                        drawCol(i + (len_max / 2), AVG_GROUND_HEIGHT + 1, PIPE);
                        drawCol(i + (len_max / 2) + 1, AVG_GROUND_HEIGHT + 1, PIPE);

                    }
                }
            } else {
                drawCol(i, curr, GROUND);
                if (count == len_max){
                    count = 0;
                    curr = AVG_GROUND_HEIGHT;
                } else { count++; }
            }
        }
    }

    public void drawCol(int x, int offset, int id){
        for(int i = LEVEL_HEIGHT; i > LEVEL_HEIGHT - offset; i--){
            setVal(x, i, id);
        }
    }

    public Integer getVal(int x, int y){
        return tArray[y][x];
    }

    public void setVal(int x, int y, int val){
        tArray[y][x] = val;
    }

    public void printArray(){
        for(int i = 0; i < LEVEL_HEIGHT; i++){
            for (int j = 0; j < LEVEL_WIDTH; j++){
                System.out.print(this.getVal(j, i) + " ");
            }
            System.out.println();
        }
    }

    public int[][] getArray(){
    	return tArray;
    }

}
