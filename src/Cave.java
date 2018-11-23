import java.util.LinkedList;
//import java.util.Stack;

public class Cave {
    //Done: make cave with passed size
    private int x,y;
    //map = list of lists: of lists that contain strings
    private LinkedList<LinkedList<LinkedList<String>>> map = new LinkedList<>();
    private int[] caveEntrance = new int[]{1,1};

    Cave(int x, int y){
        this.x = x;
        this.y = y;

        for(int i=0; i < x; i++){
            map.add(new LinkedList<>());
            for(int j=0; j < y; j++){
                map.get(i).add(new LinkedList<>());
                map.get(i).get(j).push("");
            }
        }
        initializeCave();
    }

    //Done: fill cave
    private void initializeCave(){
        boolean placed = false;

        //Done: random place for gold/Wumpus
        while (!placed) {
            int x = (int) (Math.random() * (this.x -1));
            int y = (int) (Math.random() * (this.y -1));

            if(x == caveEntrance[0] && y == caveEntrance[1])
            {
                continue;
            }

            map.get(x).get(y).pop();
            map.get(x).get(y).push("Gold");

            adjacency("Glitter", new int[]{x, y});

            placed = true;
        }

        placed = false;

        while (!placed) {
            int x = (int) (Math.random() * (this.x -1));
            int y = (int) (Math.random() * (this.y -1));

            if(x == caveEntrance[0] && y == caveEntrance[1])
            {
                continue;
            }

            if (!map.get(x).get(y).peekFirst().equals("Gold")) {
                map.get(x).get(y).pop();
                map.get(x).get(y).push("Wumpus");

                adjacency("Smell", new int[]{x, y});

                placed = true;
            }
        }

        for (int x = 0; x < this.x; x++) {
            for (int y = 0; y < this.y; y++) {
                if(x == caveEntrance[0] && y == caveEntrance[1])
                {
                    continue;
                }

                //Done: each place has 20% chance to be pit
                //decimal number between zero and one
                float chance = (float)Math.random();

                //20% chance of pit placement
                //only on empty space
                if (chance < 0.2 && map.get(x).get(y).peekFirst().equals("")){
                    map.get(x).get(y).pop();
                    map.get(x).get(y).push("Pit");
                    adjacency("Breeze", new int[]{x, y});
                }
            }
        }
    }

    private void adjacency(String attribute, int[] location){
        //check adjacent exists in map
        //if exists and empty, add attribute
        int x = location[0];
        int y = location[1];

        //agent can only move up left down right so only need attributes there
        //east
        if ((x + 1) < this.x){
            //'shallow clone' items are not cloned just pointers
            LinkedList east = (LinkedList) map.get(x + 1).get(y).clone();
            if (!east.peekFirst().equals("Wumpus") && !east.peekFirst().equals("Gold") && !east.peekFirst().equals("Pit")) {
                if(!east.peekFirst().equals(attribute)){
                    map.get(x + 1).get(y).remove("");
                    map.get(x + 1).get(y).push(attribute);
                }
            }
        }
        //west
        if ((x - 1) > -1){
            //'shallow clone' items are not cloned just pointers
            LinkedList west = (LinkedList) map.get(x - 1).get(y).clone();
            if(!west.peekFirst().equals("Wumpus") && !west.peekFirst().equals("Gold") && !west.peekFirst().equals("Pit")) {
                if (!west.peekFirst().equals(attribute)) {
                    map.get(x - 1).get(y).remove("");
                    map.get(x - 1).get(y).push(attribute);
                }
            }
        }
        //north
        if ((y + 1) < this.y){
            //'shallow clone' items are not cloned just pointers
            LinkedList north = (LinkedList) map.get(x).get(y + 1).clone();
            if(!north.peekFirst().equals("Wumpus") && !north.peekFirst().equals("Gold") && !north.peekFirst().equals("Pit")) {
                if (!north.peekFirst().equals(attribute)) {
                    map.get(x).get(y + 1).remove("");
                    map.get(x).get(y + 1).push(attribute);
                }
            }
        }
        //south
        if ((y - 1) > -1){
            //'shallow clone' items are not cloned just pointers
            LinkedList south = (LinkedList) map.get(x).get(y - 1).clone();
            if (!south.peekFirst().equals("Wumpus") && !south.peekFirst().equals("Gold") && !south.peekFirst().equals("Pit")) {
                if (!south.peekFirst().equals(attribute)) {
                    map.get(x).get(y - 1).remove("");
                    map.get(x).get(y - 1).push(attribute);
                }
            }
        }
    }

    void trackAgent(int[] location, int[] past){
        map.get(past[0]).get(past[1]).remove("X");
        map.get(location[0]).get(location[1]).add("X");
    }

    //Done: return location attribute
    LinkedList getAttribute(int[] location){
        return map.get(location[0]).get(location[1]);
    }

    boolean wall(int[] location){
        return location[0] >= this.x || location[1] >= this.y || location[0] < 0 || location[1] < 0;
    }

    void revealCaveFull() {
        //complex information heavy print
        //print header row
        for (int x = 0; x < this.x; x++) {
            System.out.printf("%14s", x);
            System.out.printf("%14s", "");
        }
        System.out.println();

        //print cave as table with all values
        for (int y = 0; y < this.x; y++) {
            System.out.print(y);
            for (int x = 0; x < this.y; x++) {
                Object[] item = map.get(x).get(y).toArray();
                System.out.print("(");
                for (int i = 0; i < 3; i++) {
                    if (i < item.length) {
                        System.out.printf("%7s", item[i]);
                    } else {
                        System.out.printf("%7s","");
                    }
                    if(i<2) {
                        System.out.print(", ");
                    }
                }
                System.out.print(") ");
            }
            System.out.println();
        }
        System.out.println();
    }
    void revealCavePretty(){
        //prettier user friendly print
        //print header row
        for (int x = 0; x < this.x; x++) {
            System.out.printf("%5s", x);
            System.out.printf("%4s", "");
        }
        System.out.println();

        //print cave as table with important items
        for (int y = 0; y < this.x; y++) {
            System.out.print(y);
            for (int x = 0; x < this.y; x++) {
                System.out.print("(");
                if(map.get(x).get(y).peekFirst().equals("Wumpus") || map.get(x).get(y).peekFirst().equals("Gold") || map.get(x).get(y).peekFirst().equals("Pit")) {
                    System.out.printf("%6s", map.get(x).get(y).peek());
                }
                else{
                    System.out.printf("%6s", "");
                }
                System.out.print(") ");
            }
            System.out.println();
        }
    }

}