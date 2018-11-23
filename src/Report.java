import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

public class Report {
    //TODO: make and print cave results in an intelligent mannor
    private String caveResult;
    //need to not use array as key as hash value changes per array not per contents
    private Hashtable<List, String> log = new Hashtable<>();
    private LinkedList<String> logPrint = new LinkedList<>();
    private String[] possibleResult = new String[]{"Gold", "Dead"};

    void addLog(String event, String attribute, List location){
        //log the event at a location (assume new location each call)
        log.put(location, attribute);
        logPrint.add("[" + String.valueOf(location.get(0)) + ", " + String.valueOf(location.get(1) + "] " + event));
    }
    String checkLog(List location){
        return log.get(location);
    }
    void readLastEntry(){
        System.out.println("log: " + logPrint.peekLast());
    }
    boolean visited(List location){
        return log.containsKey(location);
    }
    void printReport(){
        for(Object event: logPrint.toArray()){
            System.out.println(event);
        }
    }
}
