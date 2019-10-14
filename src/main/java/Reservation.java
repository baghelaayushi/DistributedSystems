
import helpers.ClientInfo;
import helpers.Event;

import java.util.*;

public class Reservation {
    // status is the dictionary
    private static HashMap<String, ClientInfo> status = new HashMap<>();
    private static HashMap<Integer,Integer> flights = new HashMap<>(20);

    private  List<Event> Log = new ArrayList<>();
    private  int[][] Matrix;
    private static final int processId = 1;


    public Reservation(int number_of_hosts,int[][] Matrix,List<Event> Log){
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

        status.put(clientName, new ClientInfo(clientName, flightNumbers, "pending"));

        // adding local insert event
        this.Log.add(new Event("insert",status.get(clientName),processId));
        return "The status is pending";

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

            //adding local delete event
            this.Log.add(new Event("delete",status.get(clientName),processId));

            status.remove(clientName);

        }else{
            return "Cannot schedule reservation for "+ clientName;
        }

        return "Reservation for "+ clientName + " cancelled.";
    }
}



