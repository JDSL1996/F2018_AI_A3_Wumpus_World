import java.nio.charset.StandardCharsets;
import java.util.*;

public class Agent {
    private Report report = new Report();
    private Cave cave;
    private List<Integer> standing = new ArrayList<>(2);
    private boolean dead,finished = false;
    private int glitterSeen = 0;
    private LinkedList<List<Integer>> path = new LinkedList<>();
    private LinkedList<List<Integer>> backtrack = new LinkedList<>();
    private boolean backtracking = false;
    private int stepCount = 0;

    //Done: can enter cave
    void enterCave(Cave cave){
        this.cave = cave;
        standing.add(1);
        standing.add(1);
        takeStep();

        path.push(standing);

        while(!dead && !finished) {
            choseNextStep();
        }
    }

    //Done: create sense to choose movement
    private LinkedList sense(){
        return cave.getAttribute(standing);
    }

    //TODO: create conditional movement for agent
    //TODO: fire arrow option if wumpus for sure located
    //Done: agent writes report (update result/track steps taken)
    //Done: can die
    private void takeStep() {
        stepCount++;
        if(!report.visited(standing)){
            //take a feel and report it
            LinkedList feel = sense();
            for (Object attribute : feel.toArray()) {
                boolean ignore = false;
                switch (attribute.toString()) {
                    case "Glitter":
                        report.addLog("So close I can almost taste it! ", attribute.toString(), standing);
                        glitterSeen += 1;
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
                        report.setResult("The report just ends there..... and is that... is that blood??", stepCount);
                        break;
                    case "Pit":
                        report.addLog("YAAAAAH-WHO-WHO-WHOEY! ", attribute.toString(), standing);
                        dead = true;
                        report.setResult("The report just ends there.....", stepCount);
                        break;
                    case "Gold":
                        report.addLog("Oh, there you are! ", attribute.toString(), standing);
                        goHome();
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
        }
        //if backtracking
        //adding to end of backtrack will make path the correct order
        if(backtracking){
            backtrack.addLast(standing);
        }
        else{
            path.push(standing);
        }
    }

    private void choseNextStep(){
        //these are changed before this call so this check is needed here
        //no need to chose a next move if were done or dead
        if (!finished && !dead) {
            //TODO: choice order: not die, get gold
            //possible conditions: nothing, gold, pit, wumpus, gold and pit, gold and wumpus, pit and wumpus

            //if glitter
            if (glitterSeen > 0) {
                pursueGold(glitterSeen);
            } else {
                noPlan();
            }
        }
    }

    private void pursueGold(int glitterSoFar){
        //ready to pick a step
        boolean choice = false;

        /*
            if glitter is found with no other items then the gold can be in one of four surrounding spaces
                if a tested space is completely empty then all spaces around it are safe to check as well
                the gold not being here makes us search for a new glitter location
                (as the other two are where we came from and too far away)
                    checking these spaces (the one that will may contain glitter)
                    reduce the possible gold locations by two in only two steps
                    alternatively: backtracking at this point will be just as safe but cost 3 steps for the same outcome
                    it also makes sense to check both as this will reduce search space by three in 4 moves
                        at this point if glitter is found gold is known (between both glitters dependant on movement)
                        if glitter is not found in either space gold is still known but further away
                        (space adjacent to start that hasn't been ruled out yet

             option for 'completely safe' gold search:
             (if all visited spaces are empty of all but glitter this will get us gold in fewest moves)

             if glitter -> move up
                if no gold -> right
                    if no glitter -> left -> left
                        if no glitter -> right -> down -> down = gold
                        else -> down = gold
                    else -> down = gold
         */

        //if first glitter and no other options make no plan choice
        if(report.checkLog(standing).length == 1) {
            //follow noPlan choice and get that choice for further decision making
            //will return 0 -> 3
            //in order the rest are n,e,s,w
            //the larger this int the smaller the search space because it wont go places the agent has been
            //or choose walls so for example if this int is 2 then we know that there is no gold or reason to move n,e
            int firstTurn = noPlan();

            //if finished we found gold and can stop checking things. Otherwise:
            //after step if there is more than one item the space is less safe than backtracking...
            // ...so long as that one item is "" (space has to be initialized for all these calls to work so ""))
            if(!finished) {
                String[] attribute = report.checkLog(standing);
                //if south is dangerous we have to check it anyway, we have no other choice
                //TODO: possible if forced to make dangerous decision opt to open more spaces instead
                if (attribute.length == 1 && attribute[0].equals("") || firstTurn == 2) {
                    boolean testTurn;
                    //turn the next direction
                    testTurn = turn(firstTurn + 1);
                    if(testTurn){
                        takeStep();
                    }

                    //here if there are warnings we don't care but we have to check each attribute to find glitter
                    //we also know there wont be gold so no need to check for it
                    for (String at : report.checkLog(standing)) {
                        //if we didn't get to take a second step skip this part
                        if(!testTurn){
                            break;
                        }
                        //if glitter we know gold is around and this will bring us home
                        if (at.equals("Glitter")) {
                            //this will go the opposite of the first move in both north and east case
                            //for first move being west the gold will have been found (only option)
                            //south needs special condition
                            if (firstTurn < 2) {
                                turn(firstTurn + 2);
                            }
                            //if we get here first turn was south, we need to go north to get gold
                            else {
                                turn(0);
                            }
                            takeStep();
                            break;
                        }
                    }
                    if(!finished) {
                        //only get here if second step contains no glitter

                        //if first step was north, second was northeast want to check northwest

                        //if first was east, north is not possible.
                        //  second step was southeast gold must have been west from first

                        //if first was south second move would have found glitter and therefor gold

                        stepBack();
                        //this one does not get gold
                        //at this point we know for sure that there is no gold north of the glitter
                        //we also know there is no gold east of the glitter
                        if (firstTurn == 0) {
                            //check west since we know its safe
                            //standing north of original turn west
                            testTurn = turn(3);

                            //if we couldn't go west here gold must be south
                            if(testTurn){
                                takeStep();
                            }

                            //here we are standing nw of original glitter
                            //here if there are warnings we don't care but we have to check each attribute to find glitter
                            for (String at : report.checkLog(standing)) {
                                //if we couldn't move west skip checking west
                                if(!testTurn){
                                    break;
                                }
                                //if glitter we know gold is around and this will bring us home
                                if (at.equals("Glitter")) {
                                    //if glitter found here the gold is to the south
                                    //go ahead and refill now that we don't have to backtrack strangely
                                    refillPath();
                                    //go south
                                    turn(2);
                                    takeStep();
                                }
                            }
                            if(!finished) {
                                //here we have ruled out north east and west of original glitter find
                                //gold must be south but we've got to get back to first find

                                //this will put us back north of original
                                stepBack();
                                //and this will put us back on original
                                stepBack();

                                refillPath();

                                turn(2);
                                takeStep();
                            }
                        }
                        //this one gets gold
                        else {
                            stepBack();
                            refillPath();
                            turn(3);
                            takeStep();
                        }
                    }
                }
                //otherwise its safer to backtrack and try another route, knowing we reduced the search space
                //TODO: backtrack second not safe
                else {

                    noPlan();
//                    stepBack();
//                    if(firstTurn == 1){
//                        //turned east but wasn't safe or gold
//                        //now standing back center only choices left are south and west
//
//                    }
                }
            }
        }
        //TODO: choice first not safe
        //if glitter but not safe
        else {
            noPlan();
        }
    }

    //this will keep going until dead or finished through the entire cave
    //first testing north then east(all around the compass)
    //if all new spaces are exhausted it will backtrack one step and try again until a new move can be made
    //then it will refill the path list in the correct order
    //so long as there are end conditions this method is exhaustive and will find them (depth first search)
    private int noPlan(){
        //Done: might loop around a space and get trapped, find a way to fix (visited every node in surrounding area)
        //if not enough information, test direction array until a choice can be made

        int choice = -1;
        for (int turn = 0; turn < 4; turn++) {
            //if turn was a valid move (not wall)
            if (turn(turn)) {
                //if we haven't been here yet (turn changes standing) and this is the first outer loop
                if (!report.visited(standing)) {
                    // if this choice was made as a result of backtracking
                    if(backtracking){
                        refillPath();
                        //current step choice will be added after this call ends
                    }
                    //choice made so take step and stop choosing (soft break)
                    choice = turn;
                    turn = 4;
                    takeStep();
                }
                else{
                    standing = path.peek();
                }
            }
        }
        if (choice == -1) {
            stepBack();
            noPlan();
        }
        return choice;
    }

    private void stepBack(){
        if(!backtracking) {
            backtrack.push(path.pop());
        }
        standing = path.pop();
        backtrack.push(standing);
        backtracking = true;
    }

    private void refillPath(){
        //refill path with old moves (will be in order as backtrack was reversed order (FILO method)
        while(!backtrack.isEmpty()){
            path.push(backtrack.pop());
        }
        backtracking = false;
    }

    private boolean turn(int dir){
        //n,e,s,w
        int[][] directions = {{0,1}, {1,0}, {0,-1}, {-1,0}};

        List<Integer> step = new ArrayList<>(2);

        //if the request is legitimate
        if(dir < directions.length && dir > -1) {
            step.add(standing.get(0) + directions[dir][0]);
            step.add(standing.get(1) + directions[dir][1]);

            //if the choice is not a wall
            if (!cave.wall(step)) {
                standing = step;
                return true;
            }
        }
        return false;
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
    private void goHome(){
        finished = true;
        while(!path.isEmpty()){
            cave.agentTrail(path.pop());
        }
        report.drawCave(cave);
        report.setResult("Got the Gold, Home safe for the night.", stepCount);
    }

    //agent sends in report
    Report whatHappened(){
        return report;
    }
}
