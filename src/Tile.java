import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Tile
{
    private final int x;
    private final int y;

    private int id;

    private TileType type;

    private HashMap<Direction, Tile> neighbors;

    public Tile(int x, int y, TileType type)
    {
        this.x = x;
        this.y = y;
        this.type = type;
        neighbors = new HashMap<>(4);
    }

    public TileType getType()
    {
        return type;
    }

    public void setType(TileType type)
    {
        this.type = type;
    }

    public void addNeighbor(Tile tile, Direction direction)
    {
        neighbors.put(direction, tile);
    }

    public Tile getNeighbor(Direction direction)
    {
        if(neighbors.containsKey(direction))
            return neighbors.get(direction);
        else
            return null;
    }

    public Direction getDirectionToNeighbor(Tile tile)
    {
        for (Direction direction : neighbors.keySet())
        {
            if(getNeighbor(direction) == tile)
                return direction;
        }

        return null;
    }



    public List<Tile> getNeighbors() {
        return Collections.unmodifiableList(new ArrayList<Tile>(neighbors.values()));
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }
}
