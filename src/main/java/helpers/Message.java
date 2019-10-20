package helpers;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Message implements Serializable {
    int siteId;
    List<Event> log ;
    boolean marker;

    public List<Event> getMessageDetails(){
        return log;
    }
    public int getSiteId() {
        return siteId;
    }
    public boolean getMarker(){
        return marker;
    }

}