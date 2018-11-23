import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Set;

public class Report {
    //TODO: make and print cave results in an intelligent mannor
    private String caveResult;
    private Hashtable<int[], String> log = new Hashtable<>();
    private LinkedList<String> logPrint = new LinkedList<>();
    private String[] possibleResult = new String[]{"Gold", "Dead"};

    void addLog(String event, int[] location){
        //log the event at a location (assume new location each call)
        log.put(location, event);
        String printString = "[" + String.valueOf(location[0]) + ", " + String.valueOf(location[1]);
        printString += "] " + event;
        logPrint.add(printString);
    }
    String checkLog(int[] location){
        return log.get(location);
    }
    void printReport(){
        for(Object event: logPrint.toArray()){
            System.out.println(event);
        }
    }
//    void printReport(){
//        if(!log.isEmpty()){
//            Set<int[]> key = log.keySet();
//
//            for (int[] direction: key) {
//                System.out.print(log.get(direction) + " was sensed @ ");
//                System.out.println("(" + direction[0] + ", " + direction[1] + ")");
//            }
//        }
//        else{
//            System.out.println("The pages are blank!");
//        }
//    }
}
