package helpers;

import java.io.Serializable;
import java.util.List;


public class normalMessage extends Message implements Serializable {
    private  int[][] matrixClock;
    public normalMessage(List<Event> logD, int[][] matrixClockD, int siteId){
        this.siteId = siteId;
        log = logD;
        this.matrixClock = matrixClockD;
        marker = true;
    }
    public int[][] getMatrixClock(){
        return matrixClock;
    }
}
