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
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class MessagingServer {

    private DatagramSocket udpSocket;
    private int port;

    public MessagingServer(int port) throws SocketException, IOException {
        this.port = port;
        this.udpSocket = new DatagramSocket(this.port);
    }
    public void updateRecords(Message message){
        if(message.getMarker()) {
            Server.getReservation().update(message, message.getSiteId());
        }
        else
            Server.getReservation().updateSmall(message);
    }


    public void listen() throws Exception {
        byte incomingData[] = new byte[1024];

        while (true){

            DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
            udpSocket.receive(incomingPacket);
            byte[] data = incomingPacket.getData();
            ByteArrayInputStream in = new ByteArrayInputStream(data);
            ObjectInputStream is = new ObjectInputStream(in);
            try {
                Message message = (Message) is.readObject();
                updateRecords(message);

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

//            System.out.println("HERE");

        }
    }

}
