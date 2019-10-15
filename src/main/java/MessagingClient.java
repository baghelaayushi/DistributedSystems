import helpers.Message;

import java.io.IOException;
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

    public void send(Message toBeSent) throws IOException{


            String bs = "hello";
            System.out.println("I WILL BE SENDING A MESSAGE" + this.serverAddress +" "+  this.port);

            DatagramPacket p = new DatagramPacket(toBeSent.toString().getBytes(), toBeSent.toString().getBytes().length, serverAddress, port);

            this.udpSocket.send(p);

    }
}
