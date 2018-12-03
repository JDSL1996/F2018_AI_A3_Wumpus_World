import java.util.Stack;

public class Player
{
    //
    //member variables
    //

    //the alive state of the player
    private boolean isAlive;
    //has the player found gold
    private boolean foundGold;
    //the players current position
    private Tile currentPosition;
    //the direction the player is facing
    private Direction direction;
    //the number of steps the player has taken
    private int steps;
    //the players log
    private Log log;
    //the current cave that the player is exploring
    private Cave cave;
    //the plath the player has taken
    private Stack<Tile> path;

    public Player() { }

    public void enterCave(Cave cave)
    {
        //
        //Initialize member variables
        //
        initializeMemberVariables(cave);

        //
        //initialize local variables
        //
        int attempts = 0;   //TODO: prevents infinite loop; delete when fixed

        while (isAlive && foundGold == false)
        {
            //mark the players position to explored
            currentPosition.setType(TileType.explored);
            //get the tile that the player is looking at
            Tile facingTile = currentPosition.getNeighbor(direction);

            //if the tile is null; it is a wall
            if(facingTile == null)
            {
                //turn clockwise and try again
                turnClockWise();
                continue;
            }
                move(facingTile);
            //detect adjacent tiles; log anything note worthy
//            detectAdjacentTiles();

            switch (currentPosition.getType())
            {
//                case unExplored:
//                    move(facingTile);
//                    break;
                case explored:
                    turnClockWise();
                    break;
                case gold:
                    break;
                case wumpas:
                    isAlive = false;
                    log.entry("Eaten by Wumpas. Player has died!");
                    break;
                case stink:
                    turnClockWise();
                    break;
                case breeze:
                    turnClockWise();
                    break;
                case pit:
                    isAlive = false;
                    log.entry("Fell into a pit. Player has died! ");
                    break;
            }

            switch (facingTile.getType())
            {
                case unExplored:
                    move(facingTile);
                    break;
                case explored:
                    turnClockWise();
                    break;
                case gold:
                    break;
                case wumpas:
                    isAlive = false;
                    log.entry("Eaten by Wumpas. Player has died!");
                    break;
                case stink:
                    turnClockWise();
                    break;
                case breeze:
                    turnClockWise();
                    break;
                case pit:
                    isAlive = false;
                    log.entry("Fell into a pit. Player has died! ");
                    break;
            }

            //TODO: prevents infinite loop; delete when fixed
            attempts++;
            if(attempts > 5)
                break;
        }
    }

    private void initializeMemberVariables(Cave cave)
    {
        this.cave = cave;
        log = new Log();
        path = new Stack<>();
        isAlive = true;
        foundGold = false;
        currentPosition = cave.startingPositon();
        direction = Direction.south;
        path.push(cave.startingPositon());
    }

    private void move(Tile facingTile)
    {
        currentPosition = facingTile;
        currentPosition.setType(TileType.explored);
        log.entry("Moving to the " + direction);
    }

    private void detectAdjacentTiles()
    {
        for (Tile neighbor : currentPosition.getNeighbors())
        {
            TileType currrentType = neighbor.getType();

            if(currrentType == TileType.breeze)
                log.entry("Detecting a breeze to the " + currentPosition.getDirectionToNeighbor(neighbor));
            else if(currrentType == TileType.stink)
                log.entry("Detecting a stink to the " + currentPosition.getDirectionToNeighbor(neighbor));
        }
    }

    private void turnClockWise()
    {
        Direction currentDirection = direction;

        switch (currentDirection)
        {
            case north:
                direction = Direction.east;
                break;
            case west:
                direction = Direction.north;
                break;
            case east:
                direction = Direction.south;
                break;
            case south:
                direction = Direction.west;
                break;
        }
    }

//    public Boolean noSenseLastTile (int entryNumber) {
//        if(log.getLastdetect(log.getEntryNumber(),log))
//    }

    public void displayLog()
    {
        for (String line : log.getData())
            System.out.println(line);
    }



}
