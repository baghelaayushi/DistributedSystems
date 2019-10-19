import helpers.Message;
import org.apache.commons.lang3.SerializationUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class MessagingServer {

    private DatagramSocket udpSocket;
    private int port;
    byte[] incomingData = new byte[1024];

    public MessagingServer(int port) throws SocketException, IOException {
        this.port = port;
        this.udpSocket = new DatagramSocket(this.port);
    }

    public void listen() throws Exception {

        while (true){

            DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
            udpSocket.receive(incomingPacket);
            byte[] data = incomingPacket.getData();
            ByteArrayInputStream in = new ByteArrayInputStream(data);
            ObjectInputStream is = new ObjectInputStream(in);
            try {
                Message student = (Message) is.readObject();
                System.out.println("Student object received = "+ student.getMessageDetails());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

}
