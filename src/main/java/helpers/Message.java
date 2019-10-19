package helpers;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Message implements Serializable {
    private  List<Event> log ;
    private  int[][] matrixClock;
//    private List<Event> log2;

    public Message(List<Event> logD, int[][] matrixClockD){
//        this.log = logD;
        this.matrixClock = matrixClockD;
        this.log = new ArrayList<>(logD);
    }

    public int getMessageDetails(){
        return log.size();
    }
}
