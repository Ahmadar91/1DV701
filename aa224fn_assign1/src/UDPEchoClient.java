/*
  UDPEchoClient.java
  A simple echo client with no error handling
*/

import java.io.IOException;
import java.net.*;



public class UDPEchoClient extends NetworkLayer {




    private static final int MYPORT = 0;

    private DatagramSocket socket;

    private SocketAddress remoteBindPoint;
    private boolean connected = false;

    {
        try {
            socket = new DatagramSocket(null);
        } catch (SocketException e) {
            System.err.println("Error! could not  close client socket "+ e.getMessage());
        }
    }


    UDPEchoClient(String ip, String port, String bufferSize, String transfer, String message) {

        setIpAddr(ip);
        setPort((port));
        setBufferSize(bufferSize);
        setMessagesTransferRate(transfer);
        setMessageForUDP(message);


    }


    public void run() {

        connect();
        if (!connected){
            return;
        }

        // set the array with the buffer size and checks if the buffer size is not to large for the VM
        byte[] Buffer = getByteArr();
        String incomingMessage;
        long before = System.currentTimeMillis();
        int counter = 0;
        // while loop for one second
        while (((System.currentTimeMillis() - before) < 1000)) {
            // sleep for the rest of the one second if transfer rate reached max and run time did not one second
            if (sleepUntilOneSecond(counter, before))
                break;

            send(getMessage());

            incomingMessage = receive(Buffer);
            if(incomingMessage == null){
                break;
            }
            System.out.println(incomingMessage);

            if (incomingMessage.equals(getMessage())) {
                System.out.printf("%d bytes sent and received\n", incomingMessage.length());
            } else {
                System.out.print("the Sent message and received message are not equal!\n");
            }
            counter++;
        }

        System.out.println("Time: " + (System.currentTimeMillis() - before) + " ms");
        System.out.println("Message Sent: " + counter + " Total: " + getMessagesTransferRate());

        close();
    }


    private void connect() {
        /* Create local bind point */
        SocketAddress localBindPoint = new InetSocketAddress(MYPORT);
        try {
            socket.bind(localBindPoint);
            remoteBindPoint = new InetSocketAddress(getIpAddr(), getPort());
            connected = true;
        } catch (SocketException e) {
            System.err.println("Error! could not  connect to socket "+ e.getMessage());

        }

    }

    /* Receiving message */
    private String receive(byte[] buffer) {
        /* Create datagram packet for receiving message */
        DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
        try {
            /* Receiving message */
            socket.receive(receivePacket);
        } catch (IOException e) {
            System.err.println("Error! could not receive message" + e.getMessage());
           return null;
        }
        return new String(receivePacket.getData(), receivePacket.getOffset(), receivePacket.getLength());
    }


    private boolean send(String message) {
        /* Create datagram packet for sending message */
        DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), remoteBindPoint);
        try {
            /* Send message */
            socket.send(packet);
        } catch (IOException e) {
            System.err.println("Error! could not send message "+e.getMessage());
            return false;
        }catch (NullPointerException e) {
            System.err.println("Message is empty: " + e.getMessage());
            return false;
        }
        return true;
    }


    private void close() {
        socket.close();

    }
}