import java.util.Hashtable;

public class Agent {
    private Report report;
    private Cave cave;
    private int[] standing;
    private boolean dead,finished = false;

    //Done: can enter cave
    void enterCave(Cave cave){
        this.cave = cave;
        standing = new int[]{1,1};
        int test = 5;
        while(!dead && !finished && test > 0) {
            exploreCave();
            test --;
        }
    }

    //Done: create sense to choose movement
    private String sense(){
        return cave.getAttribute(standing);
    }

    //TODO: create conditional movement for agent
    //Done: agent writes report (update result/track steps taken)
    //Done: can die
    private void exploreCave(){
        //take a feel
        switch(sense()){
            case "Glitter":
                report.addLog("So close I can almost taste it! ", standing);
                break;
            case "Smell":
                report.addLog("Smells funny here: ", standing);
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
            default:
                report.addLog("Man, it's really dark in here. Hope this is legible. ", standing);
               break;
        }

        int[] step = {standing[0]+1, standing[1]};
        if(!cave.wall(step)){
            standing = step;
        }
    }

    private Hashtable recall(int[] location){
        Hashtable<int[],String> surroundings = new Hashtable<>();
        //at 0,0
        //n,ne,e,se,s,sw,w,nw
        int[][] directions = {{0,1}, {1,1}, {1,0}, {1,-1}, {0,-1}, {-1,-1}, {-1,1}};

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
    }

    //agent sends in report
    Report whatHappened(){
        return report;
    }
}
