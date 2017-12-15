package Rendering;

import LSystem.Coordinate;

import java.util.HashMap;

public class SpriteMap {

    public HashMap<Integer, Coordinate> map;

    public SpriteMap(){
        map = new HashMap<>();

        map.put(0, new Coordinate(0, 0));
        map.put(1, new Coordinate(1, 0));
        map.put(2, new Coordinate(2, 0));

        map.put(14, new Coordinate(14, 0));
        map.put(15, new Coordinate(15, 0));
        map.put(30, new Coordinate(14, 1));
        map.put(31, new Coordinate(15, 1));


        map.put(152, new Coordinate(8, 9));
        map.put(153, new Coordinate(9, 9));
        map.put(154, new Coordinate(10, 9));
        map.put(155, new Coordinate(11, 9));

        map.put(161, new Coordinate(1, 10));
        map.put(164, new Coordinate(4, 10));

        map.put(219, new Coordinate(11, 13));
        map.put(220, new Coordinate(12, 13));

    }

    public Coordinate findCoords(int blockID){
        return map.get(blockID);
    }
}
