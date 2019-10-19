package helpers;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ClientInfo implements Serializable {



    String clientName;
    String status;
    List<Integer> flightNumbers = new ArrayList<>();


    public ClientInfo(String clientName, String[] flightNumbers, String status){
        this.clientName = clientName;
        this.status = status;
        for (String flight :  flightNumbers){
            this.flightNumbers.add(Integer.parseInt(flight));
        }


    }

    public List<Integer> getFlights(){
        return flightNumbers;
    }

    public String getStatus(){ return  status; }

    public String getClientName() {
        return clientName;
    }

}