import java.util.HashMap;

public class SpriteMap {

    public HashMap<String, Coordinate> map;

    public SpriteMap(){
        map = new HashMap<>();
        map.put("eyeblock", new Coordinate(1, 0));
        map.put("questionblock", new Coordinate(2, 0));
    }

    public Coordinate findCoords(String blockName){
        return map.get(blockName);
    }
}
