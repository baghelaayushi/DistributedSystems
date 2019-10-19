package helpers;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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

    public List<Event> getMessageDetails(){
        return this.log;
    }
    public int[][] getMatrix(){
        return this.matrixClock;
    }
    public JsonObject codeToJSON(Message message){
        Gson gson = new Gson();
        JsonArray log = new JsonArray();
        for(Event e: message.getMessageDetails()){
            String client_ob = gson.toJson(e);
            log.add(client_ob);
        }

        String clock = "";
        for(int i[]: message.getMatrix()){
            for(int j:i){
                clock += Integer.toString(j);
            }
        }
        JsonArray clockObject = new JsonArray();
        clockObject.add(clock);
        JsonObject jsonMessage = new JsonObject();
        jsonMessage.add("log:",log);
        jsonMessage.add("clock:",clockObject);

        return jsonMessage;
    }

}
