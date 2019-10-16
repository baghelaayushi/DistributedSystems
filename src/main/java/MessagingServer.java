import helpers.Message;
import org.apache.commons.lang3.SerializationUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class MessagingServer {

    private DatagramSocket udpSocket;
    private int port;

    public MessagingServer(int port) throws SocketException, IOException {
        this.port = port;
        this.udpSocket = new DatagramSocket(this.port);
    }

    public void listen() throws Exception {
        System.out.println("Running at "+ InetAddress.getLocalHost());

            byte[] buf = new byte[256];
            System.out.println("HERE");
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            udpSocket.receive(packet);
            Message yourObject = SerializationUtils.deserialize(packet.getData());
        System.out.println();
            System.out.println(yourObject.getMessageDetails());

    }

}
