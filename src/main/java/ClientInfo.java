import java.util.ArrayList;
import java.util.List;

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