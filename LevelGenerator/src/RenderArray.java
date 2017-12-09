
public class RenderArray {

    int[][] rArray;

    public static final Integer SPRITE_PAGE_WIDTH = 16;
    public static final Integer SPRITE_PAGE_HEIGHT = 14;

    public RenderArray(){
        this.rArray = new int[TestArray.LEVEL_HEIGHT][TestArray.LEVEL_WIDTH];
    }

    public int getVal(int x, int y){
        return rArray[y][x];
    }

    public void setVal(int x, int y, int val){
        rArray[y][x] = val;
    }

    public void buildRenderArray(TestArray tArray){
        for(int i = 0; i < TestArray.LEVEL_WIDTH; i++){
            for(int j = 0; j < TestArray.LEVEL_HEIGHT; j++) {
                switch (tArray.getVal(i, j)) {
                    case TestArray.GROUND:
                        handleGround(tArray, i, j);
                        break;
                }
            }
        }
    }

    public void handleGround(TestArray tArray, int x, int y){
        if(     tArray.getVal(x-1, y) == TestArray.GROUND &&
                tArray.getVal(x+1, y) == TestArray.GROUND){
            this.setVal(x, y, 161);
            drawToBottom(164, x, y);
        }
        else if(tArray.getVal(x-1, y) == TestArray.GROUND &&
                tArray.getVal(x+1, y) != TestArray.GROUND){
            this.setVal(x, y, 153);
            drawToBottom(155, x, y);
        }
        else if(tArray.getVal(x-1, y) != TestArray.GROUND &&
                tArray.getVal(x+1, y) == TestArray.GROUND){
            this.setVal(x, y, 152);
            drawToBottom(154, x, y);
        }
    }

    public void drawToBottom(int id, int x, int y){
        for(int j = y + 1; j < TestArray.LEVEL_HEIGHT; j++){
            this.setVal(x, j, id);
        }
    }

    public void printArray(){
        for(int i = 0; i < TestArray.LEVEL_HEIGHT; i++){
            for (int j = 0; j < TestArray.LEVEL_WIDTH; j++){
                System.out.print(this.getVal(j, i) + " ");
            }
            System.out.println();
        }
    }


    //class to convert the array to drawn elements

}
