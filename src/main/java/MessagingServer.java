import java.io.IOException;
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
        String msg;

        while(true){
            byte[] buf = new byte[256];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            udpSocket.receive(packet);
            msg = new String(packet.getData()).trim();

            System.out.println(packet.getAddress().getHostAddress() + msg);
        }
    }

}
