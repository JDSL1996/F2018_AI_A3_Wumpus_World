import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

public class Report {
    //TODO: make and print cave results in an intelligent mannor
    private String caveResult;
    private int stepsTaken;
    private Cave cave;
    //need to not use array as key as hash value changes per array not per contents
    private Hashtable<Coord, String[]> log = new Hashtable<>();
    private LinkedList<String> logPrint = new LinkedList<>();

    void addLog(String event, String attribute, Coord location){
        //log the event at a location (assume new location each call)
        if(log.containsKey(location)){
            String[] old = log.get(location);
            String[] newAttributes = new String[old.length +1];
            System.arraycopy(old,0,newAttributes,0,old.length-1);
            newAttributes[newAttributes.length-1] = attribute;

            log.put(location, newAttributes);
        }else{
            String[] at = new String[]{attribute};
            log.put(location, at);
        }
        logPrint.add("[" + String.valueOf(location.get(0)) + ", " + String.valueOf(location.get(1) + "] " + event));
    }
    String[] checkLog(Coord location){
        return log.get(location);
    }
    void readLastEntry(){
        System.out.println("log: " + logPrint.peekLast());
    }
    boolean visited(Coord location){
        return log.containsKey(location);
    }

    void setResult(String result, int steps){
        caveResult = result;
        stepsTaken = steps;
    }

    void drawCave(Cave cave){
        this.cave = cave;
    }

    void printReport(){
        cave.revealCavePretty();
        System.out.println("The report reads as follows:");
        for(Object event: logPrint.toArray()){
            System.out.println(event);
        }
        System.out.println("Agent has steps taken marked as: " + stepsTaken);
        System.out.println(caveResult + "\n");
    }
}
