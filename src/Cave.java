import java.util.LinkedList;
import java.util.Stack;

public class Cave {
    //Done: make cave with passed size
    private int x,y;
    //map = list of lists: of lists that contain strings
    private LinkedList<LinkedList<Stack<String>>> map = new LinkedList<>();

    Cave(int x, int y){
        this.x = x;
        this.y = y;

        for(int i=0; i < x; i++){
            map.add(new LinkedList<>());
            for(int j=0; j < y; j++){
                map.get(i).add(new Stack<>());
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

            if (map.get(x).get(y).peek().equals("")) {
                map.get(x).get(y).pop();
                map.get(x).get(y).push("Gold");

                adjacency("Glitter", new int[]{x, y});

                placed = true;
            }
        }

        placed = false;

        while (!placed) {
            int x = (int) (Math.random() * (this.x -1));
            int y = (int) (Math.random() * (this.y -1));

            if (map.get(x).get(y).peek().equals("")) {
                map.get(x).get(y).pop();
                map.get(x).get(y).push("Wumpus");

                adjacency("Smell", new int[]{x, y});

                placed = true;
            }
        }

        for (int x = 0; x < this.x; x++) {
            for (int y = 0; y < this.y; y++) {
                //Done: each place has 20% chance to be pit
                //decimal number between zero and one
                float chance = (float)Math.random();

                //20% chance of pit placement
                //only on empty space
                if (chance < 0.2 && !map.get(x).get(y).peek().equals("Wumpus") && !map.get(x).get(y).peek().equals("Gold")){
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
        int[] caveEntrance = new int[]{1,1};

        if (location != caveEntrance) {
            //east
            if ((x + 1) < this.x && !map.get(x + 1).get(y).peek().equals("Wumpus") && !map.get(x + 1).get(y).peek().equals("Gold") && !map.get(x + 1).get(y).peek().equals("Pit")) {
                if (map.get(x + 1).get(y).peek().equals("")) {
                    map.get(x + 1).get(y).pop();
                }
                map.get(x + 1).get(y).push(attribute);
                //ne
                if ((y + 1) < this.y && !map.get(x + 1).get(y + 1).peek().equals("Wumpus") && !map.get(x + 1).get(y + 1).peek().equals("Gold") && !map.get(x + 1).get(y + 1).peek().equals("Pit")) {
                    if(map.get(x + 1).get(y + 1).peek().equals("")) {
                        map.get(x + 1).get(y + 1).pop();
                    }
                    map.get(x + 1).get(y + 1).push(attribute);
                }
                //se
                if ((y - 1) > 0 && !map.get(x + 1).get(y - 1).peek().equals("Wumpus") && !map.get(x + 1).get(y - 1).peek().equals("Gold") && !map.get(x + 1).get(y - 1).peek().equals("Pit")) {
                    if(map.get(x + 1).get(y - 1).peek().equals("")) {
                        map.get(x + 1).get(y - 1).pop();
                    }
                    map.get(x + 1).get(y - 1).push(attribute);
                }
            }
            //west
            if ((x - 1) > 0 && !map.get(x - 1).get(y).peek().equals("Wumpus") && !map.get(x - 1).get(y).peek().equals("Gold") && !map.get(x - 1).get(y).peek().equals("Pit")) {
                if(map.get(x - 1).get(y).peek().equals("")) {
                    map.get(x - 1).get(y).pop();
                }
                map.get(x - 1).get(y).push(attribute);
                //nw
                if ((y + 1) < this.y && !map.get(x - 1).get(y + 1).peek().equals("Wumpus") && !map.get(x - 1).get(y + 1).peek().equals("Gold") && !map.get(x - 1).get(y + 1).peek().equals("Pit")) {
                    if(map.get(x - 1).get(y + 1).peek().equals("")) {
                        map.get(x - 1).get(y + 1).pop();
                    }
                    map.get(x - 1).get(y + 1).push(attribute);
                }
                //sw
                if ((y - 1) > 0 && !map.get(x - 1).get(y - 1).peek().equals("Wumpus") && !map.get(x - 1).get(y - 1).peek().equals("Gold") && !map.get(x - 1).get(y - 1).peek().equals("Pit")) {
                    if(map.get(x - 1).get(y - 1).peek().equals("")) {
                        map.get(x - 1).get(y - 1).pop();
                    }
                    map.get(x - 1).get(y - 1).push(attribute);
                }
            }
            //north
            if ((y + 1) < this.y && !map.get(x).get(y + 1).peek().equals("Wumpus") && !map.get(x).get(y + 1).peek().equals("Gold") && !map.get(x).get(y + 1).peek().equals("Pit")) {
                if(map.get(x).get(y + 1).peek().equals("")) {
                    map.get(x).get(y + 1).pop();
                }
                map.get(x).get(y + 1).push(attribute);
            }
            //south
            if ((y - 1) > 0 && !map.get(x).get(y - 1).peek().equals("Wumpus") && !map.get(x).get(y - 1).peek().equals("Gold") && !map.get(x).get(y - 1).peek().equals("Pit")) {
                if(map.get(x).get(y - 1).peek().equals("")) {
                    map.get(x).get(y - 1).pop();
                }
                map.get(x).get(y - 1).push(attribute);
            }
        }
    }

    //Done: return location attribute
    String getAttribute(int[] location){
        String attribute;

        attribute = map.get(location[0]).get(location[1]).peek();

        return attribute;
    }
    public boolean wall(int[] location){
        return location[0] > this.x || location[1] > this.y || location[0] < 0 || location[1] < 0;
    }

    public void revealCave(){

        for (int x = 0; x < this.x; x++) {
//            System.out.println("Row " + x);
            for (int y = 0; y < this.y; y++) {
//                System.out.println("Col " + y);
                //System.out.printf("%10s", map[x][y].);
                Object[] item = map.get(x).get(y).toArray();
                System.out.print("(");
                for(int i=0; i<3; i++){
                    //Object item: map.get(x).get(y).toArray()){
                    if (i < item.length) {
                        System.out.printf("%10s", item[i] + ", ");// + " @ " + x + ", " + y);
                    }
                    else {
                        System.out.printf("%10s",", ");
                    }
                }
                System.out.print(")");
            }
            System.out.println();
        }
    }

}



//import java.util.Stack;
//
//public class Cave {
//    //Done: make cave with passed size
//    private int x,y;
//    //map = array of arrays of stacks
//    private Stack<String>[][] map = new S
//
//    Cave(int x, int y){
//        this.x = x;
//        this.y = y;
//        String em = "";
//
//        for(int i=0; i < x; i++){
//            map[i] = new Stack<String>[];
//            for(int j=0; j < y; j++){
//                map[i][j].push("");
//            }
//        }
//        initializeCave();
//    }
//
//    //Done: fill cave
//    private void initializeCave(){
//        boolean placed = false;
//
//        //Done: random place for gold/Wumpus
//        while (!placed) {
//            int x = (int) (Math.random() * (this.x -1));
//            int y = (int) (Math.random() * (this.y -1));
//
//            if (map[x][y].peek().equals("")) {
//                map[x][y].pop();
//                map[x][y].push("Gold");
//
//                adjacency("Glitter", new int[]{x, y});
//
//                placed = true;
//            }
//        }
//
//        placed = false;
//
//        while (!placed) {
//            int x = (int) (Math.random() * (this.x -1));
//            int y = (int) (Math.random() * (this.y -1));
//
//            if (map[x][y].peek().equals("")) {
//                map[x][y].pop();
//                map[x][y].push("Wumpus");
//
//                adjacency("Smell", new int[]{x, y});
//
//                placed = true;
//            }
//        }
//
//        for (int x = 0; x < this.x; x++) {
//            for (int y = 0; y < this.y; y++) {
//                //Done: each place has 20% chance to be pit
//                //decimal number between zero and one
//                float chance = (float)Math.random();
//
//                //20% chance of pit placement
//                //only on empty space
//                if (chance > 0.2 && map[x][y].peek().equals("")){
//                    map[x][y].pop();
//                    map[x][y].push("Pit");
//                    adjacency("Breeze", new int[]{x, y});
//                }
//            }
//        }
//    }
//
//    private void adjacency(String attribute, int[] location){
//        //check adjacent exists in map
//        //if exists and empty, add attribute
//        int x = location[0];
//        int y = location[1];
//        int[] caveEntrance = new int[]{1,1};
//
//        if (map[x][y].peek().equals("") && location != caveEntrance) {
//            //east
//            if ((x + 1) < this.x){// && map[x + 1][y].peek().equals("")) {
//                map[x + 1][y].pop();
//                map[x + 1][y].push(attribute);
//                //ne
//                if ((y + 1) < this.y){// && map[x + 1][y + 1].peek().equals("")) {
//                    map[x + 1][y + 1].pop();
//                    map[x + 1][y + 1].push(attribute);
//                }
//                //se
//                if ((y - 1) > 0){// && map[x + 1][y - 1].peek().equals("")) {
//                    map[x + 1][y - 1].pop();
//                    map[x + 1][y - 1].push(attribute);
//                }
//            }
//            //west
//            if ((x - 1) > 0){// && map[x - 1][y].peek().equals("")) {
//                map[x - 1][y].pop();
//                map[x - 1][y].push(attribute);
//                //nw
//                if ((y + 1) < this.y){// && map[x - 1][y + 1].peek().equals("")) {
//                    map[x - 1][y + 1].pop();
//                    map[x - 1][y + 1].push(attribute);
//                }
//                //sw
//                if ((y - 1) > 0){// && map[x - 1][y - 1].peek().equals("")) {
//                    map[x - 1][y - 1].pop();
//                    map[x - 1][y - 1].push(attribute);
//                }
//            }
//            //north
//            if ((y + 1) < this.y){// && map[x][y + 1].peek().equals("")) {
//                map[x][y + 1].pop();
//                map[x][y + 1].push(attribute);
//            }
//            //south
//            if ((y - 1) > 0){// && map[x][y - 1].peek().equals("")) {
//                map[x][y - 1].pop();
//                map[x][y - 1].push(attribute);
//            }
//        }
//    }
//
//    //Done: return location attribute
//    String getAttribute(int[] location){
//        String attribute;
//
//        attribute = map[location[0]][location[1]].peek();
//
//        return attribute;
//    }
//    public boolean wall(int[] location){
//        return location[0] > this.x || location[1] > this.y || location[0] < 0 || location[1] < 0;
//    }
//
//    public void revealCave(){
//
//        for (int x = 0; x < this.x; x++) {
//            for (int y = 0; y < this.y; y++) {
//                //System.out.printf("%10s", map[x][y].);
//                for(Object item: map[x][y].toArray()){
//                    System.out.printf("%10s", item);
//                }
//            }
//            System.out.println();
//        }
//    }
//
//}
