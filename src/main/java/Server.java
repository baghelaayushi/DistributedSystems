import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import helpers.Event;
import helpers.Message;
import helpers.Site;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class Server {


    private static int number_of_hosts = -1;
    private static HashMap<String, Site> siteHashMap = new HashMap<>();
    private static Site mySite = null;

    private static List<Event> log = new ArrayList<>();
    private static int[][] matrixClock;


    public static void bootstrapProject(String selfIdentifier) throws FileNotFoundException {

        try {
            processHosts(selfIdentifier);
            initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static void sendMessage(String userInput,Reservation ob){
        try {
            String input[] = userInput.split(" ");
            String clientName = input[1];
            String destinationAddress = siteHashMap.get(clientName).getIpAddress();
            int port = siteHashMap.get(clientName).getRandomPort();
            int site_number = siteHashMap.get(clientName).getSiteNumber();
//            System.out.println(destinationAddress +" " + port);
            List<Event> NP = new ArrayList<>();
            for(Event e: log){
                if(!ob.hasRec(e,site_number))
                    NP.add(e);
            }
            new MessagingClient(destinationAddress, port).send(new Message(NP, matrixClock));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void initialize() throws Exception{
        // initializing matrix and log for a site
        matrixClock  = new int[number_of_hosts][number_of_hosts];
        log = new ArrayList<>();

        MessagingServer client = new MessagingServer(mySite.getRandomPort());
        Runnable R =  new Runnable() {
            @Override
            public void run() {
                try {
                    client.listen();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread t = new Thread(R);
        t.start();
    }

    private static void processHosts(String self) throws FileNotFoundException {

        BufferedReader hosts = new BufferedReader(new FileReader("./knownhosts.json"));
        Gson gson =new Gson();
        JsonParser parser = new JsonParser();
        JsonObject hostsObject = parser.parse(hosts).getAsJsonObject().get("hosts").getAsJsonObject();

        number_of_hosts = hostsObject.keySet().size();
        int site_number =0 ;

        for (Map.Entry<String, JsonElement> host : hostsObject.entrySet()){
            JsonObject siteInfo = host.getValue().getAsJsonObject();
            Site site = new Site(siteInfo.get("ip_address").getAsString(),
                    siteInfo.get("udp_start_port").getAsString(),
                    siteInfo.get("udp_end_port").getAsString(),site_number++);
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
//        System.out.println("Enter an option");
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
                    ob.getState();
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
                case "send":
                    sendMessage(userInput,ob);
                    break;

                default:
//                    System.out.println("Enter a valid option");
            }

        }
    }

    public static void sendMessage(String userInput){
        try {
            String input[] = userInput.split(" ");
            String clientName = input[1];
            String destinationAddress = siteHashMap.get(clientName).getIpAddress();
            int port = siteHashMap.get(clientName).getRandomPort();
            System.out.println(destinationAddress +" " + port);
            new MessagingClient(destinationAddress, port).send(new Message(log, matrixClock));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
