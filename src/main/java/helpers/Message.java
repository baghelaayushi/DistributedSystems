package helpers;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Message implements Serializable {

    public int getSiteId() {
        return siteId;
    }

    private int siteId;
    private  List<Event> log ;
    private  int[][] matrixClock;

    public Message(List<Event> logD, int[][] matrixClockD, int siteId){
        this.matrixClock = matrixClockD;
        this.log = new ArrayList<>(logD);
        this.siteId = siteId;
    }

    public List<Event> getMessageDetails(){
        return log;
    }
    public int[][] getMatrixClock(){
        return matrixClock;
    }

}
