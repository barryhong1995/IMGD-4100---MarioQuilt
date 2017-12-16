package Rendering;

import Rendering.TestArray;

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
        for(int i = 0; i < TestArray.LEVEL_HEIGHT; i++){
            for(int j = 0; j < TestArray.LEVEL_WIDTH; j++) {
                switch (tArray.getVal(j, i)) {
                    case TestArray.PLATFORM:
                        handlePlatform(tArray, j, i);
                        break;
                    case TestArray.GROUND:
                        handleGround(tArray, j, i);
                        break;
                    case TestArray.PIPE:
                    	handlePipe(tArray, j, i);
                        break;
                    case TestArray.BLOCK:
                    	setVal(j, i, 6);
                    	break;
                }
            }
        }
    }
    
    public void handlePipe(TestArray tArray, int x, int y){
        int ch = checkNeighbors(tArray, x, y, TestArray.PIPE);
        int adj = ch & 0xf;

        switch(adj){
            case 0xa:
                this.setVal(x, y, 14);
                break;
            case 0x9:
                this.setVal(x, y, 15);
                break;
            case 0xe:
                this.setVal(x, y, 30);
                break;
            case 0xd:
                this.setVal(x, y, 31);
                break;
            default:
                this.setVal(x, y, 30);
                break;
        }
    }

    public int checkNeighbors(TestArray tArray, int x, int y, int id){

        int result = 0x0;

        if(tArray.getVal(x-1, y) == id){ result |= 0x1; }
        if(tArray.getVal(x+1, y) == id){ result |= 0x2; }
        if(tArray.getVal(x, y-1) == id){ result |= 0x4; }
        if(tArray.getVal(x, y+1) == id){ result |= 0x8; }
        if(tArray.getVal(x-1, y-1) != id){ result |= 0x10; }
        if(tArray.getVal(x+1, y-1) != id){ result |= 0x20; }
        if(tArray.getVal(x-1, y+1) != id){ result |= 0x40; }
        if(tArray.getVal(x+1, y+1) != id){ result |= 0x80; }


        return result;

    }

    public void handleGround(TestArray tArray, int x, int y){

        int ch = checkNeighbors(tArray, x, y, TestArray.GROUND);
        int adj = ch & 0xf;
        int crn = (ch & 0xf0) >> 4;

        switch(adj){
            case 0xb:
                this.setVal(x, y, 161);
                break;
            case 0x9:
                this.setVal(x, y, 153);
                break;
            case 0xa:
                this.setVal(x, y, 152);
                break;
            case 0xe:
                this.setVal(x, y, 154);
                break;
            case 0xd:
                this.setVal(x, y, 155);
                break;
            default:
                //System.out.println(crn);
                switch(crn){
                    case 0x1:
                        this.setVal(x, y, 219);
                        break;
                    case 0x2:
                        this.setVal(x, y, 220);
                        break;
                    default:
                        this.setVal(x, y, 164);
                        break;

                }
                break;
        }
    }

    public void handlePlatform(TestArray tArray, int x, int y){
        this.setVal(x, y, 161);
        for(int j = y+1; j < TestArray.LEVEL_HEIGHT; j++){
            this.setVal(x, j, 164);
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

    public int[][] getArray(){
    	return rArray;
    }
    //class to convert the array to drawn elements

}
