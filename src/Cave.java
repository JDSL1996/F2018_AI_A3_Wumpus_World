//import java.util.Stack;

import java.util.Random;

public class Cave {

    //
    //member variables
    //

    //the width of this cave
    private final int width;
    //the height of this cave
    private final int height;
    //the cave data for each tile
    private final Tile[][] map;
    //the random instance for this cave
    private Random random;

    //
    //constructors
    //

    //creates an instance of cave with provided width and height
    public Cave(int width, int height, int seed)
    {//begin Cave
        //
        //initialize member variables
        //
        this.width = width;
        this.height = height;
        map = new Tile[width][height];
        random = new Random(seed);
        //initialize map data
        initialize();
        //place the tiles on the map
        placeTiles();
    }//end Cave

    //
    //methods
    //


    public Tile startingPositon()
    {
        return map[0][0];
    }

    //initializes the map data
    private void initialize()
    {//begin initialize
        //
        //initialize map
        //
        for (int y = 0; y < this.height; y++)
        {
            for (int x = 0; x < this.width; x++)
            {
                map[x][y] = new Tile(x, y, TileType.unExplored);
            }
        }
        //
        //setup neighbors
        //
        for (int y = 0; y < this.height; y++)
        {
            for (int x = 0; x < this.width; x++)
            {
                addNeighbors(map[x][y]);
            }
        }
    }//end initialize

    private void placeTiles()
    {
        //place gold
        placeRandomTile(TileType.gold);
        //place wumpas
        placeRandomTile(TileType.wumpas);

        //place pits
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                Tile currentTile = map[x][y];

                if(currentTile == startingPositon())
                    continue;


                if(currentTile.getType() == TileType.unExplored && isReachable(currentTile))
                {
                    float chance = random.nextFloat();

                    if(chance < 0.20f)
                    {
                        currentTile.setType(TileType.pit);

                        for (Tile neighbor : currentTile.getNeighbors())
                        {
                            if(isReachable(neighbor))
                            {
                                if(neighbor.getType() == TileType.unExplored && neighbor != startingPositon())
                                    neighbor.setType(TileType.breeze);

                            }
                        }
                    }

                }
            }
        }
    }



    private void placeRandomTile(TileType tileType)
    {
        //
        //initialize local variables
        //
        int attempts = 0;
        boolean isPlaced = false;
        final int mapSize = width * height;

        //run while there is no placement or the attempts is less than the size of the map
        while (isPlaced == false || attempts > mapSize)
        {//begin while
            //The positional data for placement
            int xPosition = random.nextInt(width);
            int yPosition = random.nextInt(height);

            //The tile to place
            Tile selectedTile = map[xPosition][yPosition];

            //if the selected tile is not the start, check neighbors
            if(selectedTile != startingPositon() && selectedTile.getType() == TileType.unExplored)
            {//begin if
                    if(isReachable(selectedTile))
                    {//begin if

                        //place the tile
                        map[selectedTile.getX()][selectedTile.getY()].setType(tileType);

                        //if tileType is wumpas, then ass smell to neighbors
                        if(tileType == TileType.wumpas)
                        {
                            for (Tile neighbor : selectedTile.getNeighbors())
                            {
                                //if neighbor is unexplored, then mark as stink
                                if(neighbor.getType() == TileType.unExplored)
                                    neighbor.setType(TileType.stink);
                            }
                        }
                        //successfully placed
                        isPlaced = true;
                    }//end if
            }//end if
            //count attempt
            attempts++;
        }//end while
    }

    private boolean isReachable(Tile tile)
    {//begin isReachable
        //store the number of unexplored tiles neighboring the selected tile
        int unexploredTileCount = 0;

        //check all neighbors
        for (Tile neighbor : tile.getNeighbors())
        {//begin for
            //if tile is unexplored, then count it
            if(neighbor.getType() == TileType.unExplored)
                unexploredTileCount++;
        }//end for

        //if the number of unexplored neighbors is greater than 1 then it is reachable
        if(unexploredTileCount > 1)
            return true;
        else
            return false;
    }//end isReachable

    //adds all neighboring tiles
    //parameters:
    //tile = the tile to add neighbors to
    private void addNeighbors(Tile tile)
    {//begin addNeigbors
        //
        //get the position of the tile
        //
        int x = tile.getX();
        int y = tile.getY();

        //check west
        if(isBounded(x - 1, y))
            tile.addNeighbor(map[x - 1][y], Direction.west);
        //check east
        if(isBounded(x + 1, y))
            tile.addNeighbor(map[x + 1][y], Direction.east);
        //check south
        if(isBounded(x, y + 1))
            tile.addNeighbor(map[x][y + 1], Direction.south);
        //check north
        if(isBounded(x, y - 1))
            tile.addNeighbor(map[x][y - 1], Direction.north);
    }//end addNeigbors

    private boolean isBounded(int x, int y)
    {
        //if x is NOT found on the map return false
        if(x < 0 || x >= width)
            return false;
        //if y is NOT found on the map return false
        if(y < 0 || y >= height)
            return false;

        //found - return true
        return true;
    }

    public void display()
    {
        for (int y = 0; y < this.height; y++)
        {
            for (int x = 0; x < this.width; x++)
            {
                Tile currentTile = map[x][y];
                TileType currentType = currentTile.getType();

                char tile = ' ';

                switch (currentType)
                {
                    case unExplored:
                        tile = ' ';
                        break;
                    case explored:
                        tile = 'X';
                        break;
                    case gold:
                        tile = 'G';
                        break;
                    case wumpas:
                        tile = 'W';
                        break;
                    case stink:
                        tile = 'S';
                        break;
                    case breeze:
                        tile = 'B';
                        break;
                    case pit:
                        tile = 'P';
                        break;
                }

                System.out.print("( " + tile + " )");
                System.out.print("\t");
            }
            System.out.println();
        }
    }
}