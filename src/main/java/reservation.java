import java.util.HashMap;
import java.util.Scanner;

public class reservation {
    // status is the dictionary
    private static HashMap<String,String> status = new HashMap<>();
    private static HashMap<Integer,Integer> flights = new HashMap<>(20);

    // To check whether a client can reserve a flight or not
    public String reserve(String clientId, String[] flight){
        // No client can reserve two flights
        if(status!= null && status.containsKey(clientId))
            return "You can't book more than one flights";

        // for all flights that the client wants
        for(String flightNo: flight){

            // only allow flight booking if seats are available
            // else change request to pending
            if(flights.get(Integer.parseInt(flightNo))>=1) {
                int seats = flights.get(Integer.parseInt(flightNo));
                flights.replace(Integer.parseInt(flightNo),seats-1);
                status.put(clientId, "pending");
            }
            else
                return "Failed";
        }
        return "The status is pending";

    }
    public String reserveSeat(String reservation){

        String input[] = reservation.split(" ");
        String clientName = input[1];
        String flightNumbers[] = input[2].split(",");

        return reserve(clientName,flightNumbers);

    }

    public static void main( String[] args) {

        flights = new HashMap<>(20);
        for(int i=1;i<=20;i++)
            flights.put(i,2);

        status = new HashMap<>();

        reservation ob = new reservation();

        Scanner in = new Scanner(System.in);

        String userInput;

        while (!(userInput = in.nextLine()).equals("exit")){

            String input[] = userInput.split(" ");
            String command = input[0];

            switch (command) {
                case "reserve":
                    ob.reserveSeat(userInput);
                    break;
                case "cancel":
                    break;

                default:
                    System.out.println("Enter a valid option");
            }

        }

    }
}

