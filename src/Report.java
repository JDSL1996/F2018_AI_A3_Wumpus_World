import java.util.Hashtable;
import java.util.LinkedList;

public class Report {
    //TODO: make and print cave results in an intelligent mannor
    private String caveResult;
    public Hashtable<int[], String> log = new Hashtable<>();
    private LinkedList<String> logPrint = new LinkedList<>();
    private String[] possibleResult = new String[]{"Gold", "Dead"};

    void addLog(String event, int[] location){
        //log the event at a location (assume new location each call)
        log.put(location, event);
        logPrint.add("[" + String.valueOf(location[0]) + ", " + String.valueOf(location[1] + "] " + event));
    }
    String checkLog(int[] location){
        System.out.print("log: " + log.get(location) + "@ ");
        System.out.println(location[0] + ", " + location[1]);
        return log.get(location);
    }
    void printReport(){
        for(Object event: logPrint.toArray()){
            System.out.println(event);
        }
    }
}
