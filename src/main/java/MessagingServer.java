import com.google.gson.*;
import helpers.Event;
import helpers.Message;
import jdk.nashorn.internal.parser.JSONParser;
import org.apache.commons.lang3.SerializationUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class MessagingServer {

    private DatagramSocket udpSocket;
    private int port;
    public Message decodeFromJSON(JsonObject jsonMessage){
        List<Event> log = new ArrayList<>();
        Gson gson = new Gson();
        JsonArray receivedLog = jsonMessage.getAsJsonArray("log:");
        JsonArray receivedMatrix = jsonMessage.getAsJsonArray("clock:");
        for(JsonElement element:receivedLog){
            Event event = gson.fromJson(element.getAsString(),Event.class);
            // adding the events to log
            log.add(event);
        }
        String clock = "";
        for(JsonElement e:receivedMatrix){
            clock = e.getAsString();
        }
        int tempClock[][] = new int[2][2];
        int k = 0;
        for(int i=0;i<tempClock.length;i++){
            for(int j =0;j<tempClock[0].length;j++){
                tempClock[i][j] = (int)clock.charAt(k);
                k++;
            }
        }
        return new Message(log,tempClock);
    }

    public MessagingServer(int port) throws SocketException, IOException {
        this.port = port;
        this.udpSocket = new DatagramSocket(this.port);
    }

    public void listen() throws Exception {
//        System.out.println("Running at "+ InetAddress.getLocalHost());
            byte[] buf = new byte[1024];
            System.out.println("HERE");
            while(true) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                udpSocket.receive(packet);
                String recievedString = new String(packet.getData(),0,packet.getLength());
                System.out.println("Gotcha");
                System.out.println(recievedString);
                JsonParser parser = new JsonParser();
                JsonObject parsed = parser.parse(recievedString).getAsJsonObject();
                Message recievedMessage = decodeFromJSON(parsed);
                System.out.println(recievedMessage.getMessageDetails().size());
                /*ByteArrayInputStream in = new ByteArrayInputStream(packet.getData());
                ObjectInputStream is = new ObjectInputStream(in);
                try {
                    Message yourObject = (Message) is.readObject();
                    System.out.println("Student object received = "+yourObject);
                    System.out.println(yourObject.getMessageDetails().size() + "is the size");
                    for (int i = 0; i < yourObject.getMessageDetails().size(); i++){
                        System.out.println(yourObject.getMessageDetails().get(i).getOperationType());
                    }
                    System.out.println(packet.getData().length);
                    System.out.println(packet.getData()[0]);
                    System.out.println(yourObject.getMessageDetails());
                    System.out.println("beta received");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }*/



                //Message yourObject = SerializationUtils.deserialize(packet.getData());


            }

    }

}
