import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Stack;

public class Agent {
    private Report report = new Report();
    private Cave cave;
    private int[] standing;
    private boolean dead,finished = false;
    //n,e,s,w
    private int[][] directions = {{0,1}, {1,0}, {0,-1}, {-1,0}};
    private LinkedList<int[]> path = new LinkedList<>();

    //Done: can enter cave
    void enterCave(Cave cave){
        this.cave = cave;
        standing = new int[]{1,1};


        int test = 5;
        while(!dead && !finished) {//###########################################################################
            path.push(standing);
            exploreCave();
            test --;
        }
    }

    //Done: create sense to choose movement
    private LinkedList sense(){
        return cave.getAttribute(standing);
    }

    //TODO: create conditional movement for agent
    //Done: agent writes report (update result/track steps taken)
    //Done: can die
    private void exploreCave() {
        //take a feel and report it
        LinkedList feel = sense();
        for (Object attribute : feel.toArray()) {
            boolean ignore = false;
            switch (attribute.toString()) {
                case "Glitter":
                    report.addLog("So close I can almost taste it! ", standing);
                    break;
                case "Smell":
                    report.addLog("Smells funny here. ", standing);
                    break;
                case "Breeze":
                    report.addLog("Do you feel that? ", standing);
                    break;
                case "Wumpus":
                    report.addLog("Shit. ", standing);
                    dead = true;
                    break;
                case "Pit":
                    report.addLog("YAAAAAH-WHO-WHO-WHOEY! ", standing);
                    dead = true;
                    break;
                case "Gold":
                    report.addLog("Oh, there you are! ", standing);
                    getGoldgoHome();
                    break;
                case "A":
                case "":
                case "X":
                    ignore = true;
                    break;
                default:
                    report.addLog("Man, it's really dark in here. Hope this is legible. ", standing);
                    break;
            }
            if (!ignore) {
                report.checkLog(standing);
            }
        }

        //TODO: might loop around a space and get trapped, find a way to fix (visited every node in surrounding area)
        //TODO: make the hashtable actually work so its better, for now just going to use the stack which is bad
        //if found glitter + move has no glitter -> going the wrong way
        //if not enough clues move random
        //if danger clue is removed upon move -> going the right way
        //if smell -> not smell: wumpus has to be around the smell
        boolean choice = false;
//        while (!choice && !dead && !finished) {
            //if not enough information, move a random direction
//            int turn = (int) (Math.random() * (directions.length - 1));
//            int[] step = {standing[0] + directions[turn][0], standing[1] + directions[turn][1]};


            //uses the actual array pointer not the values so always evaluates to false
            //TODO: fix that
            int[] step = {1,1};
            standing = step;
            report.addLog("test", standing);
            int[] step1 = {1,1};
            System.out.println(report.log.containsKey(step));

            if (!cave.wall(step)) {
                if (!path.contains(step)) {
                    standing = step;
                    cave.agentCurrent(standing, path.peek());
                    cave.revealCaveFull();
                    choice = true;
                }
            }
//        }
    }

    private Hashtable recall(int[] location){
        Hashtable<int[],String> surroundings = new Hashtable<>();

        for(int[] direction: directions){
            int[] space = {location[0] + direction[0], location[1] + direction[1]};
            String look = report.checkLog(space);
            if(look != null) {
                surroundings.put(space, look);
            }
        }

        return surroundings;
    }

    //TODO: can hold gold
    private void getGoldgoHome(){
        finished = true;
        while(!path.isEmpty()){
            standing = path.pop();
        }
    }

    //agent sends in report
    Report whatHappened(){
        return report;
    }
}
