package helpers;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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

    public List<Event> getMessageDetails(){
        return log;
    }
    public int[][] getMatrixClock(){
        return matrixClock;
    }

}
