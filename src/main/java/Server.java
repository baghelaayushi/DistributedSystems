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


    public static void bootstrapProject(String selfIdentifier) throws FileNotFoundException {

        processHosts(selfIdentifier);
        initialize();

    }

    private static void initialize(){
        // initializing matrix and log for a site
        matrixClock  = new int[number_of_hosts][number_of_hosts];
        log = new ArrayList<>();
    }

    private static void processHosts(String self) throws FileNotFoundException {

        BufferedReader hosts = new BufferedReader(new FileReader("src/bin/knownhosts.json"));

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
            if(host.getKey().equalsIgnoreCase(self)){
                mySite = site;
            }
        }

    }



    public static void main( String[] args) {

        boolean devMode = true;
        String self = args.length == 0 ? "alpha" : args[0];
        if(args.length < 0 && !devMode){
            System.out.println("Select the current site");
            System.exit(0);
        }



        try {
            bootstrapProject(self);
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
                case "view":
                    ob.viewDictionary();
                    break;
                case "log":
                    ob.viewLog();
                    break;
                case "clock":
                    ob.viewClock();
                    break;
                default:
                    System.out.println("Enter a valid option");
            }

        }
    }

}
