package au.com.teamarrow.canbus.comms;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.Normalizer;

import au.com.teamarrow.canbus.model.CarData;

/**
 * Created by Jason on 8/17/2015.
 */
public class DriverMessageReceiver extends Thread {

    private static final int UDP_SERVER_PORT = 4879;
    private static final int MAX_UDP_DATAGRAM_LEN = 512;
    private boolean keepRunning = true;
    private CarData carData;

    private String receivedMessage = null;
    private String sendMessage = null;
    private String sender = "Chase Car";

    public DriverMessageReceiver(CarData carData) {
        this.carData = carData;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceivedMessage() {
        return receivedMessage;
    }

    public void setSendMessage(String message){
        this.sendMessage = message;
    }

    public void run() {

        InetAddress address = null;
        try {
            address = InetAddress.getByName("239.255.60.60");
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        }

        try {
            //Need to use a multicast socket apparently
            MulticastSocket socket = new MulticastSocket(UDP_SERVER_PORT); // must bind receive side
            socket.joinGroup(address);
            socket.setSoTimeout(100);

            while(keepRunning) {

                byte[] received_bytes = new byte[MAX_UDP_DATAGRAM_LEN];
                byte[] send_bytes = new byte[MAX_UDP_DATAGRAM_LEN];

                sendMessage = carData.getSendMessage();
                carData.setSendMessage(null);
                if (sendMessage != null) {
                    try {
                        send_bytes = sendMessage.getBytes();
                        DatagramPacket send_packet = new DatagramPacket(send_bytes, send_bytes.length, address, UDP_SERVER_PORT);
                        socket.send(send_packet);
                        sendMessage = null;
                    } catch (SocketTimeoutException ex) {
                    }
                }

                try {
                    DatagramPacket received_packet = new DatagramPacket(received_bytes, received_bytes.length);
                    socket.receive(received_packet);
                    receivedMessage = new String(received_packet.getData());
                    String normalized_string = Normalizer.normalize(receivedMessage, Normalizer.Form.NFD);
                    carData.addMessage(sender, normalized_string);
                } catch (SocketTimeoutException ex) {
                }
            }

            if (socket != null) {
                socket.close();
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    public void kill() {
        keepRunning = false;
    }
}
