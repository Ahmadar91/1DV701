import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;


public class TCPEchoClient extends NetworkLayer {


    private static final int MYPORT = 0;
    private Socket socket = new Socket();
    private boolean connected = false;

    public TCPEchoClient(String ip, String port, String bufferSize, String transfer, String message) {

        setIpAddr(ip);
        setPort(port);
        setBufferSize(bufferSize);
        setMessagesTransferRate(transfer);
        setMessage(message);

    }


    public void run() {

        connect();
        if (!connected){
            return;
        }

        // set the array with the buffer size and checks if the buffer size is not to large for the VM
        byte[] buffer = getByteArr();
        String incomingMessage;
        long before = System.currentTimeMillis();
        int counter = 0;
        // while loop for one second
        while (((System.currentTimeMillis() - before) < 1000)) {
            // sleep for the rest of the one second if transfer rate reached max and run time did not one second
            if (sleepUntilOneSecond(counter, before))
                break;

            if (!send(getMessage())) {
                break;
            }

            incomingMessage = receive(buffer);
            if(incomingMessage == null){
                break;
            }

            System.out.println(incomingMessage);

            if (incomingMessage.equals(getMessage())) {
                System.out.printf("%d bytes sent and received\n", incomingMessage.length());
            } else {
                System.out.print("Sent and received message not equal!\n");
            }
            counter++;
        }

        System.out.println("Time: " + (System.currentTimeMillis() - before) + " ms\n");
        System.out.println("Message Sent: " + counter + " Total: " + getMessagesTransferRate());

        close();
    }


    private void connect() {
        /* Create local bind point */
        SocketAddress localBindPoint = new InetSocketAddress(MYPORT);
        try {
            socket.bind(localBindPoint);
            socket.connect(new InetSocketAddress(getIpAddr(), getPort()));
            connected = true;
        } catch (IOException e) {
            System.err.println("Error! could not  connect to socket " + e.getMessage());
        }

    }


    private String receive(byte[] buffer) {

        StringBuilder IncomingMessage = new StringBuilder();
        int bytes = 0;

        do {
            try {
                /* Receiving message */
                bytes = socket.getInputStream().read(buffer);
            } catch (IOException e) {
                System.err.println("Error! could not receive message " + e.getMessage());
                return null;

            }
            if (bytes > 0) IncomingMessage.append(new String(buffer, 0, bytes));
        } while (bytes > 0 && getMessage().length() > IncomingMessage.length());

        return IncomingMessage.toString();
    }


    private boolean send(String message) {

        try {
            /* Send message */
            socket.getOutputStream().write(message.getBytes());
        } catch (IOException e) {
            System.err.println("Error! could not  send message " + e.getMessage());
            return false;
        } catch (NullPointerException e) {
            System.err.println("Message is empty: " + e.getMessage());
            return false;
        }
        return true;
    }


    private void close() {

        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Error! could not  close socket " + e.getMessage());
        }
    }

}
