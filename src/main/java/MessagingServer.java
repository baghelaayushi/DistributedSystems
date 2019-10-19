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
    private Reservation reservation;
    public MessagingServer(int port, Reservation reservation) throws SocketException, IOException {
        this.port = port;
        this.udpSocket = new DatagramSocket(this.port);
        this.reservation = reservation;
    }

    public void listen() throws Exception {

        while (true){

            DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
            udpSocket.receive(incomingPacket);
            byte[] data = incomingPacket.getData();
            ByteArrayInputStream in = new ByteArrayInputStream(data);
            ObjectInputStream is = new ObjectInputStream(in);
            try {
                Message message = (Message) is.readObject();
                System.out.println("Student object received = "+ message.getMessageDetails());
                reservation.processReciept(message);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

}
