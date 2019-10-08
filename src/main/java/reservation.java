import java.util.*;

public class reservation {
    // status is the dictionary
    private static HashMap<String,ClientInfo> status = new HashMap<>();
    private static HashMap<Integer,Integer> flights = new HashMap<>(20);

    // To check whether a client can reserve a flight or not
    public String reserve(String reservation){

        String input[] = reservation.split(" ");
        String clientName = input[1];
        String flightNumbers[] = input[2].split(",");


        // No client can reserve two flights
        if(status!= null && status.containsKey(clientName))
            return "You can't book more than one flights";

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


        status.put(clientName, new ClientInfo(clientName, flightNumbers, "pending"));
        return "The status is pending";

    }

    public String cancel(String command){

        String input[] = command.split(" ");
        String clientName = input[1];

        if(status.containsKey(clientName)){


            List<Integer> flightsToCancel = status.get(clientName).getFlights();

            for (int i = 0; i < flightsToCancel.size(); i++){
                int currentSeats = flights.get(flightsToCancel.get(i));
                currentSeats++;
                flights.put(flightsToCancel.get(i), currentSeats);
            }

            status.remove(clientName);

        }else{
            return "Cannot schedule reservation for "+ clientName;
        }

        return "Reservation for "+ clientName + "cancelled.";
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
            String response;

            switch (command) {
                case "reserve":
                    response = ob.reserve(userInput);
                    System.out.println(response);
                    break;
                case "cancel":
                    response = ob.cancel(userInput);
                    System.out.println(response);
                    break;

                default:
                    System.out.println("Enter a valid option");
            }

        }

    }
}

class ClientInfo{

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


}

