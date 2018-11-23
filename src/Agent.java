import java.util.*;

public class Agent {
    private Report report = new Report();
    private Cave cave;
    private List<Integer> standing = new ArrayList<>(2);
    private boolean dead,finished = false;
    //n,e,s,w
    private int[][] directions = {{0,1}, {1,0}, {0,-1}, {-1,0}};
    private LinkedList<List<Integer>> path = new LinkedList<>();

    //Done: can enter cave
    void enterCave(Cave cave){
        this.cave = cave;
        standing.add(1);
        standing.add(1);


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
                    report.addLog("So close I can almost taste it! ", attribute.toString(), standing);
                    break;
                case "Smell":
                    report.addLog("Smells funny here. ", attribute.toString(), standing);
                    break;
                case "Breeze":
                    report.addLog("Do you feel that? ", attribute.toString(), standing);
                    break;
                case "Wumpus":
                    report.addLog("Shit. ", attribute.toString(), standing);
                    dead = true;
                    break;
                case "Pit":
                    report.addLog("YAAAAAH-WHO-WHO-WHOEY! ", attribute.toString(), standing);
                    dead = true;
                    break;
                case "Gold":
                    report.addLog("Oh, there you are! ", attribute.toString(), standing);
                    getGoldgoHome();
                    break;
                case "A":
                case "X":
                    ignore = true;
                    break;
                default:
                    report.addLog("Man, it's really dark in here. Hope this is legible. ", attribute.toString(), standing);
                    break;
            }
            if (!ignore) {
                report.readLastEntry();
            }
        }

        //TODO: choice order: not die, get gold
        //possible conditions: nothing, gold, pit, wumpus, gold and pit, gold and wumpus, pit and wumpus


        //if glitter
//        if(cave.getAttribute(standing).equals(""))

        //if found glitter + move has no glitter -> going the wrong way
        //if not enough clues move random
        //if danger clue is removed upon move -> going the right way
        //if smell -> not smell: wumpus has to be around the smell

        //TODO: might loop around a space and get trapped, find a way to fix (visited every node in surrounding area)
        ////if not enough information, test direction array until a choice can be made
        boolean choice = false;
        List<Integer> step = new ArrayList<>(2);
        if(!finished && !dead) {
            for (int turn = 0; turn < directions.length; turn++) {
                step.clear();

                step.add(standing.get(0) + directions[turn][0]);
                step.add(standing.get(1) + directions[turn][1]);

                if (!cave.wall(step)) {
                    if (!report.visited(step)) {
                        standing = step;
                        choice = true;
                        //choice made so break
                        turn = directions.length;
                    }
                }
            }
            if (!choice) {
                System.out.println("IDK what to do!");
                finished = true;
            }
        }
        //update and show path for testing########################################################################
        cave.agentCurrent(standing, path.peek());
        cave.revealCaveFull();
    }

//    private Hashtable recall(int[] location){
//        Hashtable<int[],String> surroundings = new Hashtable<>();
//
//        for(int[] direction: directions){
//            int[] space = {location[0] + direction[0], location[1] + direction[1]};
//            String look = report.checkLog(space);
//            if(look != null) {
//                surroundings.put(space, look);
//            }
//        }
//
//        return surroundings;
//    }

    //TODO: can hold gold
    private void getGoldgoHome(){
        finished = true;
//        while(!path.isEmpty()){
//            path.pop();
//        }
    }

    //agent sends in report
    Report whatHappened(){
        return report;
    }
}
