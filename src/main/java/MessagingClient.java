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

    private DatagramSocket Socket;
    private InetAddress serverAddress;
    private int port;
    private Scanner scanner;
    private byte[] buf;

    public MessagingClient(String destinationAddr, int port) throws IOException {
        this.serverAddress = InetAddress.getByName(destinationAddr);
        this.port = port;
        Socket = new DatagramSocket(this.port);
        scanner = new Scanner(System.in);
    }

    public void send(Message message) throws IOException {
        try {


                byte[] incomingData = new byte[1024];

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ObjectOutputStream os = new ObjectOutputStream(outputStream);
                os.writeObject(message);
                byte[] data = outputStream.toByteArray();
                DatagramPacket sendPacket = new DatagramPacket(data, data.length, this.serverAddress, this.port);
                Socket.send(sendPacket);
//                 System.out.println("Message sent from client");
                Socket.close();

        }catch (IOException e) {
            e.printStackTrace();
        }

    }
}
