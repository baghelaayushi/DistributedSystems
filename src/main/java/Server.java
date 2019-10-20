import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import helpers.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class Server {


    private static int number_of_hosts = -1;
    private static HashMap<String, Site> siteHashMap = new HashMap<>();
    private static Site mySite = null;

//    private static int[][] matrixClock;

    private static Reservation ob;

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
            MessagingClient client = new MessagingClient(destinationAddress, port);

            List<Event> NP = new ArrayList<>();
            for(Event e: ob.getLog()){
                if(!ob.hasRec(e,site_number))
                    NP.add(e);
            }
            client.send(new normalMessage(NP, ob.getMatrix(), mySite.getSiteNumber()));
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void sendAll(Reservation ob){
        for(Map.Entry<String,Site> client :siteHashMap.entrySet()){

            if(client.getValue().getSiteNumber() == mySite.getSiteNumber())
                continue;

            try {
                String destinationAddress = client.getValue().getIpAddress();
                int port = client.getValue().getRandomPort();
                int site_number = client.getValue().getSiteNumber();
                MessagingClient mClient = new MessagingClient(destinationAddress, port);

                List<Event> NP = new ArrayList<>();
                for (Event e : ob.getLog()) {
                    if (!ob.hasRec(e, site_number))
                        NP.add(e);
                }
                mClient.send(new normalMessage(NP, ob.getMatrix(), mySite.getSiteNumber()));
                mClient.close();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    public void sendSmallMessage(String userInput,Reservation ob){
        try {
            String input[] = userInput.split(" ");
            String clientName = input[1];
            String destinationAddress = siteHashMap.get(clientName).getIpAddress();
            int port = siteHashMap.get(clientName).getRandomPort();
            int site_number = siteHashMap.get(clientName).getSiteNumber();
//            System.out.println(destinationAddress +" " + port);
            List<Event> NP = new ArrayList<>();
            for(Event e: ob.getLog()){
                if(!ob.hasRec(e,site_number))
                    NP.add(e);
            }
            new MessagingClient(destinationAddress, port).send(new smallMessage(NP, ob.getMatrix()[mySite.getSiteNumber()], mySite.getSiteNumber()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendSmallAll(Reservation ob){
        for(Map.Entry<String,Site> client :siteHashMap.entrySet()){
            try {
                String destinationAddress = client.getValue().getIpAddress();
                int port = client.getValue().getRandomPort();
                int site_number = client.getValue().getSiteNumber();
                List<Event> NP = new ArrayList<>();
                for (Event e : ob.getLog()) {
                    if (!ob.hasRec(e, site_number))
                        NP.add(e);
                }
                new MessagingClient(destinationAddress, port).send(new smallMessage(NP,  ob.getMatrix()[mySite.getSiteNumber()], mySite.getSiteNumber()));
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }


    private static void initialize() throws Exception{
        // initializing matrix and log for a site
        MessagingServer server = new MessagingServer(mySite.getRandomPort());

        Runnable R =  new Runnable() {
            @Override
            public void run() {
                try {
                    server.listen();
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


        boolean devMode = false;
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
        Server server = new Server();
        Scanner in = new Scanner(System.in);
        String userInput;
//        System.out.println("Enter an option");
        while (!(userInput = in.nextLine()).equals("exit")){

            String input[] = userInput.split(" ");
            String command = input[0];
            String response;

            switch (command) {
                case "reserve":
                    response = getReservation().reserve(userInput);
                    System.out.println(response);
                    break;
                case "cancel":
                    response = getReservation().cancel(userInput);
                    System.out.println(response);
                    break;
                case "recover":
                    getReservation().getState();
                    break;
                case "view":
                    getReservation().viewDictionary();
                    break;
                case "log":
                    getReservation().viewLog();
                    break;
                case "clock":
                    getReservation().viewClock();
                    break;
                case "send":
                    sendMessage(userInput,getReservation());
                    break;
                case "sendall":
                    sendAll(getReservation());
                    break;
                case "smallsend":
                    server.sendSmallMessage(userInput,getReservation());
                    break;
                case "smallsendall":
                    server.sendSmallAll(getReservation());
                    break;
                default:
//                  System.out.println("Enter a valid option");
            }

        }
    }

    public static Reservation getReservation(){
        if(ob == null){
            ob = new Reservation(number_of_hosts, mySite.getSiteNumber());
        }
        return ob;
    }

    public static int getTotalSites(){
        return siteHashMap.size();
    }

}
