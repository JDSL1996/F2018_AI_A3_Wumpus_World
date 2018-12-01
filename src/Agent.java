import java.util.*;

public class Agent {
    private Report report;
    private Cave cave;
    private Coord standing;
    private boolean dead,finished;
    private LinkedList<Coord> glitterSeen;
    private boolean glitterFlag, hazard;
    private int breezCount;
    private LinkedList<Coord> path;
    private LinkedList<Coord> backtrack;
    private boolean backtracking, ignore;
    private int stepCount;

    //Done: can enter cave
    void enterCave(Cave cave){
        //initialize things
        this.cave = cave;
        report = new Report();
        dead = false;
        finished = false;
        glitterSeen = new LinkedList<>();
        glitterFlag = false;
        path = new LinkedList<>();
        backtrack = new LinkedList<>();
        backtracking = false;
        stepCount = 0;
        hazard = false;
        breezCount = 0;

        standing = new Coord(0,0);

        System.out.println("Ok, Here goes nothing....");
        System.out.println("**************************");

        takeStep();

        while(!dead && !finished) {
            choseNextStep();
        }
        if(finished){
            goHome();
        }
        else{
            die();
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
                switch (attribute.toString()) {
                    case "Glitter":
                        report.addLog("So close I can almost taste it! ", attribute.toString(), standing);
                        if(!ignore) {
                            glitterFlag = true;
                            glitterSeen.push(standing);
                        }
                        break;
                    case "Smell":
                        report.addLog("Smells funny here. ", attribute.toString(), standing);
                        if(!ignore) {
                            hazard = true;
                        }
                        break;
                    case "Breeze":
                        report.addLog("Do you feel that? ", attribute.toString(), standing);
                        if(!ignore) {
                            hazard = true;
                            breezCount++;
                        }
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
                        finished = true;
                        break;
                    case "A":
                    case "X":
                        break;
                    default:
                        report.addLog("Man, it's really dark in here. Hope this is legible. ", attribute.toString(), standing);
                        break;
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

            if(hazard){
                avoid();
                hazard = false;
            }
//            if(breezy){
//                ignore = true;
//                avoidFall();
//                ignore = false;
//                breezy = false;
//            }
            //else if glitter
            else if (glitterFlag) {
                ignore = true;
                pursueGold();
                ignore = false;
                glitterFlag = false;
            }
            else {
                noPlan();
            }
        }
    }

    private void removeFlags(){
        Coord clean = standing;
        for(int turn = 0; turn < 4; turn++){
            if(turn(turn)){
                report.removeFlag(standing);
            }
            standing = clean;
        }
    }

    private void avoid(){
        Coord firstSmell = standing;
        for(int turn = 0; turn < 4; turn++){
            if(turn(turn) && !report.visited(standing)){
                report.setFlag(standing);
            }
            standing = firstSmell;
        }
        stepBack();
        refillPath();
        noPlan();
    }

    private void avoidFall(){
        //if we find a breeze and we have taken more steps than breeze found
        //we can basically run to a safe place and try again
        //we want to figure out the direction that brought us here and go the next direction instead
        //since the place with the breeze is now marked this set up is already accomplished in the noPlan method
        //(which tries to move to an unopened space and if it cannot just moves to the first available
        if(stepCount > breezCount){
            stepBack();
            noPlan();
            refillPath();
        }
        //otherwise there's not allot to be done so.....
        else {
            noPlan();
        }

    }
    private void secondGlitter(){
        boolean testStep;
        //if standing further north
        if (standing.get(1) > glitterSeen.get(glitterSeen.size() - 1).get(1)) {
            //check south
            testStep = turn(2);
        }
        //if standing further south
        else {
            //check north
            testStep = turn(0);
        }
        //if space was available take the step
        if (testStep) {
            takeStep();
            //if it didn't work (or kill us) step back to try the next direction
            if (!finished || !dead) {
                stepBack();
                refillPath();
            }
        }
        //otherwise fix where were standing
        else {
            standing = path.peek();
        }
    }
    private void pursueGold(){
        //if glitter is seen for the second time gold is between
        if(glitterSeen.size() > 1) {
            /*
                if standing[0] > glitter[0]
                    if standing[1] > glitter[1]
                        gold is either at (standing[0] -1, standing[1]) or (standing[0], standing[1]-1)
                    else
                        gold is either at (standing[0] -1, standing[1]) or (standing[0], standing[1]+1)
                ect ect
            */

            //if standing further east
            if (standing.get(0) > glitterSeen.get(glitterSeen.size() - 1).get(0)) {
                //check north or south
                secondGlitter();
                //if neither of those worked the gold is to the west
                if (!finished || !dead) {
                    //at this point the gold has to be west so west has to be a valid move
                    turn(3);
                    takeStep();
                }
            }
            //if standing further west
            else {
                //check north or south
                secondGlitter();
                //if neither of those worked the gold is to the east
                if (!finished || !dead) {
                    //at this point the gold has to be east so east has to be a valid move
                    turn(1);
                    takeStep();
                }
            }
        }
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
        else if(report.checkLog(standing).length == 1) {
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
                    else {
                        standing = path.peek();
                    }

                    //here if there are warnings we don't care but we have to check each attribute to find glitter
                    for (String at : report.checkLog(standing)) {
                        if(at == null){
                            continue;
                        }
                        //if we didn't get to take a second step skip this part
                        if(!testTurn || finished){
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
                            else {
                                standing = path.peek();
                            }

                            //here we are standing nw of original glitter
                            //here if there are warnings we don't care but we have to check each attribute to find glitter
                            for (String at : report.checkLog(standing)) {
                                if(at == null){
                                    continue;
                                }
                                //if we couldn't move west skip checking west
                                if (!testTurn || finished) {
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
                //Done: backtrack second not safe this is wrong
                //otherwise its safer to backtrack and try another route, knowing we reduced the search space
                else {
                    //get back to glitter
                    stepBack();
                    if(firstTurn == 1){
                        //turned east but wasn't safe or gold
                        //now standing back center only choices left are south and west
                        boolean testTurn;
                        //if we can go south do so
                        testTurn = turn(2);
                        if(testTurn){
                            //if the turn is valid stop backtracking and make the turn
                            refillPath();
                            takeStep();
                            //if the gold wasn't here must be to the west of original
                            if(!finished){
                                //brings us center
                                stepBack();
                                stepBack();
                                refillPath();
                                turn(3);
                                //get the gold
                                takeStep();
                            }
                        }
                        //otherwise it must be west
                        else{
                            turn(3);
                            takeStep();
                        }
                    }
                    //only option left is we tried north but it wasn't safe
                    else{
                        //now standing back center choices left are east, south and west
                        boolean testTurn;
                        //if you can go east try it
                        testTurn = turn(1);
                        if(testTurn){
                            takeStep();
                        }
                        //if it wasn't there try south and west again
                        if(!finished) {
                            stepBack();
                            stepBack();
                            refillPath();
                            //if we can go south do so
                            testTurn = turn(2);
                            if (testTurn) {
                                takeStep();
                                //if the gold wasn't here must be to the west of original
                                if (!finished) {
                                    //brings us center
                                    stepBack();
                                    refillPath();
                                    turn(3);
                                    //get the gold
                                    takeStep();
                                }
                            }
                            //otherwise it must be west
                            else {
                                turn(3);
                                takeStep();
                            }
                        }
                    }
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
                //if we haven't been here yet (turn changes standing)
                if (!report.visited(standing) && !report.getFlag(standing)) {
                    // if this choice was made as a result of backtracking
                    if(backtracking){
                        refillPath();
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
        //if no choice backtrack and try again
        if (choice == -1) {
            stepBack();
            noPlan();
        }
        return choice;
    }

    private void stepBack(){
        if(path.size() > 1) {
            if (!backtracking) {
                backtrack.push(path.pop());
            }
            standing = path.pop();
            report.addLog("Had to go back", "", standing);
            backtrack.push(standing);
            backtracking = true;
        }
    }

    private void refillPath(){
        if(backtracking) {
            //refill path with old moves (will be in order as backtrack was reversed order (FILO method)
            while (!backtrack.isEmpty()) {
                path.push(backtrack.pop());
            }
            path.push(standing);
            backtracking = false;
        }
    }

    private boolean turn(int dir){
        //n,e,s,w
        int[][] directions = {{0,1}, {1,0}, {0,-1}, {-1,0}};

        //if the request is legitimate
        if(dir < directions.length && dir > -1) {
            Coord step = new Coord(standing.get(0) + directions[dir][0], standing.get(1) + directions[dir][1]);

            //if the choice is not a wall
            if (!cave.wall(step)) {
                standing = step;
                return true;
            }
        }
        return false;
    }

    private void die(){
        while(!path.isEmpty()){
            cave.agentTrail(path.pop());
        }
        report.drawCave(cave);
    }

    private void goHome(){
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
