
import com.google.gson.*;
import helpers.ClientInfo;
import helpers.Event;
import helpers.Message;

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
        private  int processId;


    public Reservation(int[][] Matrix,List<Event> Log, int processId){
        for(int i=1;i<=20;i++)
            flights.put(i,2);

        // initializing matrix and log for a site
        this.Matrix  = Matrix;
        this.Log = Log;
        this.processId = processId;
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
            String concatFlights = info.getFlights().toString().replaceAll("\\[","").replaceAll("\\]","").replaceAll(" ","");
            String row = report.getKey()+ " "+ concatFlights + " " + info.getStatus();
            row = row.replaceAll("\\[", "").replaceAll("\\]","");
            System.out.println(row);
        }
    }

    public void viewLog(){

        for (Event event : Log){

            String row;
            String concatFlights = event.getOperation().getFlights().toString().replaceAll("\\[","").replaceAll("\\]","").replaceAll(" ","");
            if(event.getOperationType() == "insert"){
                row = event.getOperationType() + " " +
                        event.getOperation().getClientName()  + " " +
                        concatFlights;
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
                if(j!= Matrix[0].length -1)
                    System.out.print(Matrix[i][j]+" ");
                else
                    System.out.print(Matrix[i][j]);

            }
            System.out.println();
        }
    }
    public boolean hasRec(Event e,int k){
        return getMatrix()[k][e.getNodeId()] >= e.getTime();
    }

    public List<Event> getLog(){
        return this.Log;
    }
    public int[][] getMatrix(){
        return this.Matrix;
    }
    public void updateDictionary(List<Event> NE){
        HashSet<String> deleteSet = new HashSet<>();
        HashSet<String> insertSet = new HashSet<>();
        for(Event e: NE){
            if(e.getOperationType().equals("delete"))
                deleteSet.add(e.getOperation().getClientName());
            else
                insertSet.add(e.getOperation().getClientName());

        }
        for(Event e:NE){
            if(insertSet.contains(e.getOperation().getClientName())&& !(deleteSet.contains(e.getOperation().getClientName())))
                status.put(e.getOperation().getClientName(),e.getOperation());
        }
    }
    public void logTruncation(List<Event> NE, int totalSites){
        Collection<Event> col = new ArrayList<Event>(this.Log);
        col.addAll(NE);

        this.Log = new ArrayList<>(col);
        List<Event> partialLog = new ArrayList<>();
        boolean marker = true;
        for(Event e:this.Log){
            marker = true;
            for(int j=0;j<totalSites;j++){
                if(!hasRec(e,j)) {
                    marker = false;
                    break;
                }
            }
            if(!marker)
                partialLog.add(e);
        }
        this.Log = partialLog;
    }
    public void update(Message message,int receivedSiteID){
            List<Event> NE = new ArrayList<>();
            List<Event> myLog = this.Log;
            List<Event> receivedLog = message.getMessageDetails();
            int receivedClock[][] = message.getMatrixClock();
            for(Event e:receivedLog){
                if(!hasRec(e,processId)){
                    NE.add(e);
                }
            }
            updateDictionary(NE);
            System.out.println();
            for(int i[]:receivedClock){
              for (int j: i)
                  System.out.print(j);
              System.out.println();
            }
            for(int i=0;i<Server.getTotalSites();i++){
                Matrix[processId][i] = Integer.max(Matrix[processId][i],receivedClock[receivedSiteID][i]);
            }
            System.out.println("Total sites are");
            System.out.println(Server.getTotalSites());
            for(int i=0;i<Server.getTotalSites();i++){
                for(int j=0;j<Server.getTotalSites();j++){
                    Matrix[i][j] = Integer.max(Matrix[i][j],receivedClock[i][j]);
                }
            }
            System.out.println("truncating the log:");
            logTruncation(NE,Server.getTotalSites());

    }


}



