package helpers;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Message implements Serializable {
    private static List<Event> log = new ArrayList<>();
    private static int[][] matrixClock;

    public Message(List<Event> log, int[][] matrixClock){
        this.log = log;
        this.matrixClock = matrixClock;
    }

    public int getMessageDetails(){
        return this.log.size();
    }
}
