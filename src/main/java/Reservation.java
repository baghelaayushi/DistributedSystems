
import com.google.gson.*;
import helpers.ClientInfo;
import helpers.Event;

import java.io.*;


import java.sql.Timestamp;

import java.util.*;

public class Reservation {
    // status is the dictionary
    private static HashMap<String, ClientInfo> status = new HashMap<>();
    private static HashMap<Integer,Integer> flights = new HashMap<>(20);

    private int clock = 0;
    private  List<Event> Log;
    private  int[][] Matrix;
    private static final int processId = 0;


    public Reservation(int[][] Matrix,List<Event> Log){
        for(int i=1;i<=20;i++)
            flights.put(i,2);

        // initializing matrix and log for a site
        this.Matrix  = Matrix;
        this.Log = Log;
    }

    // To check whether a client can reserve a flight or not
    public String reserve(String reservation){

        String input[] = reservation.split(" ");
        String clientName = input[1];
        String flightNumbers[] = input[2].split(",");


        // No client can reserve two flights
        if(status!= null && status.containsKey(clientName))
            return "You can't book more than one flight";

        // for all flights that the client wants
        for(String flightNo: flightNumbers){

            // only allow flight booking if seats are available
            // else change request to pending
            if(flights.get(Integer.parseInt(flightNo))>=1) {
                int seats = flights.get(Integer.parseInt(flightNo));
                flights.replace(Integer.parseInt(flightNo),seats-1);
            }
            else
                return "Failed";
        }

        // adding events to matrix clock
        this.Matrix[processId][processId]++;
        clock++;

        status.put(clientName, new ClientInfo(clientName, flightNumbers, "pending"));

        // adding local insert event
        this.Log.add(new Event("insert",status.get(clientName),processId,clock));
        saveState();
        return "Reservation submitted for "+clientName+".";

    }

    public String cancel(String command){

        String input[] = command.split(" ");
        String clientName = input[1];

        if(status.containsKey(clientName)){

            //adding event to matrix clock
            this.Matrix[processId][processId]++;
            List<Integer> flightsToCancel = status.get(clientName).getFlights();

            for (int i = 0; i < flightsToCancel.size(); i++){
                int currentSeats = flights.get(flightsToCancel.get(i));
                currentSeats++;
                flights.put(flightsToCancel.get(i), currentSeats);
            }

            clock++;
            //adding local delete event
            this.Log.add(new Event("delete",status.get(clientName),processId, clock));

            status.remove(clientName);

        }else{
            return "Cannot schedule reservation for "+ clientName;
        }
        saveState();

        return "Reservation for "+ clientName + " cancelled.";
    }
    public void saveState(){
        try(FileWriter fw = new FileWriter("persistent_log.json")){
            Gson gson = new Gson();
            JsonArray arr = new JsonArray();
            for(Event e: Log){
                String client_ob = gson.toJson(e);
                arr.add(client_ob);
            }
            fw.append(gson.toJson(arr));

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getState(){
        System.out.println("updating the log....");
        try {
            //convert the json string back to object
            BufferedReader backup = new BufferedReader(new FileReader("persistent_log.json"));
            JsonParser parser = new JsonParser();
            JsonArray parsed = parser.parse(backup).getAsJsonArray();
            Log = new ArrayList<>();

            Gson gson = new Gson();
            for(JsonElement ob: parsed){
                Event event = gson.fromJson(ob.getAsString(),Event.class);
                // adding the events to log
                Log.add(event);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void viewDictionary(){

        for (Map.Entry<String, ClientInfo> report : status.entrySet()){
            ClientInfo info = report.getValue();
            String row = report.getKey()+ " "+ info.getFlights() + " " + info.getStatus();
            row = row.replaceAll("\\[", "").replaceAll("\\]","");
            System.out.println(row);
        }
    }

    public void viewLog(){

        for (Event event : Log){

            String row;
            if(event.getOperationType() == "insert"){
                row = event.getOperationType() + " " +
                        event.getOperation().getClientName()  + " " +
                        event.getOperation().getFlights();
            }else{
                row = event.getOperationType() + " " +
                        event.getOperation().getClientName();
            }

            row = row.replaceAll("\\[", "").replaceAll("\\]","");
            System.out.println(row);

        }
    }

    public void viewClock(){

        for (int i = 0; i < Matrix.length; i++){
            for (int j = 0; j < Matrix[0].length; j++){
                System.out.print(Matrix[i][j] + " ");
            }
            System.out.println();
        }
    }
    public boolean hasRec(Event e,int k){
        return Matrix[k][e.getNodeId()] >= e.getTime();
    }

    public List<Event> getLog(){
        return this.Log;
    }
    public int[][] getMatrix(){
        return this.Matrix;
    }


}



