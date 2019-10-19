package helpers;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Message implements Serializable {

    public List<Event> getLog() {
        return log;
    }

    private  List<Event> log ;

    private int processId;
    private  int[][] matrixClock;

    public Message(List<Event> logD, int[][] matrixClockD, int siteNumber){
//        this.log = logD;
        this.matrixClock = matrixClockD;
        this.log = new ArrayList<>(logD);
        this.processId = siteNumber;
    }

    public int getMessageDetails(){
        return log.size();
    }

    public int[][] getMatrixClock() {
        return matrixClock;
    }

    public void setMatrixClock(int[][] matrixClock) {
        this.matrixClock = matrixClock;
    }


    public int getProcessId() {
        return processId;
    }


}
