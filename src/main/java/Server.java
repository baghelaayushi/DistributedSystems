import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import helpers.Event;
import helpers.Site;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Server {


    private static int number_of_hosts = -1;
    private static HashMap<String, Site> siteHashMap = new HashMap<>();
    private static Site mySite = null;

    private static List<Event> log = new ArrayList<>();
    private static int[][] matrixClock;


    public static void bootstrapProject() throws FileNotFoundException {

        processHosts();
        initialize();

    }

    private static void initialize(){
        // initializing matrix and log for a site
        matrixClock  = new int[number_of_hosts][number_of_hosts];
        log = new ArrayList<>();
    }

    private static void processHosts() throws FileNotFoundException {

        BufferedReader hosts = new BufferedReader(new FileReader("./src/bin/knownhosts.json"));

        Gson gson =new Gson();
        JsonParser parser = new JsonParser();
        JsonObject hostsObject = parser.parse(hosts).getAsJsonObject().get("hosts").getAsJsonObject();

        number_of_hosts = hostsObject.keySet().size();

        for (Map.Entry<String, JsonElement> host : hostsObject.entrySet()){
            JsonObject siteInfo = host.getValue().getAsJsonObject();
            Site site = new Site(siteInfo.get("ip_address").getAsString(),
                    siteInfo.get("udp_start_port").getAsString(),
                    siteInfo.get("udp_end_port").getAsString());
            siteHashMap.put(host.getKey(), site);

            //TODO : Update this with environment variables
            if(host.getKey().equalsIgnoreCase("ALPHA")){
                mySite = site;
            }
        }
    }



    public static void main( String[] args) {

        try {
            bootstrapProject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        acceptUserInput();

    }

    private static void acceptUserInput() {

        Reservation ob = new Reservation(matrixClock, log);

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
                case "recover":
                    break;

                default:
                    System.out.println("Enter a valid option");
            }

        }
    }

}
