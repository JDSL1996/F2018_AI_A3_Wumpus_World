import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Log
{
    private ArrayList<String> data;
    private int entryNumber;

    public Log()
    {
        data = new ArrayList<>();
        entryNumber = 0;
    }
    public int getEntryNumber(){
        return entryNumber;
    }
    public String getLastdetect(int entryNumber,Log data){
        return null;
    }
    public void entry(String line)
    {
        entryNumber++;
        data.add(entryNumber + ") " + line);
        System.out.println(data.get(entryNumber-1)+"This one");
    }

    public List<String> getData() {
        return Collections.unmodifiableList(data);
    }
}
