import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import helpers.Event;
import helpers.Message;
import org.apache.commons.lang3.SerializationUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class MessagingClient {

    private DatagramSocket udpSocket;
    private InetAddress serverAddress;
    private int port;
    private Scanner scanner;

    public MessagingClient(String destinationAddr, int port) throws IOException {
        this.serverAddress = InetAddress.getByName(destinationAddr);
        this.port = port;
        udpSocket = new DatagramSocket(this.port);
        scanner = new Scanner(System.in);
    }

    public void send(Message message) throws IOException {
        Gson gson = new Gson();
        String sendMessage = gson.toJson(message.codeToJSON(message));
        byte buf[] = sendMessage.getBytes();
        DatagramPacket p = new DatagramPacket(buf, buf.length, serverAddress, port);
        this.udpSocket.send(p);
        System.out.println(buf.length);
        System.out.println(sendMessage);

        /*ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
//        System.out.println("THIS IS THE OTHER VERSION" + message.getMessageDetails());

        //byte[] yourBytes = SerializationUtils.serialize(message);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutput os = new ObjectOutputStream(outputStream);
        os.writeObject(message);
        byte[] yourBytes = outputStream.toByteArray();
        System.out.println(yourBytes.length);
        try {
            DatagramPacket p = new DatagramPacket(yourBytes, yourBytes.length, serverAddress, port);
            this.udpSocket.send(p);
            System.out.println(message.getMessageDetails());
            System.out.println("alpha sent");
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
                // ignore close exception
            }
        }*/


    }
}
