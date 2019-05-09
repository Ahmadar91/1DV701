import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class TCPEchoServer {


    private static final int MYPORT = 4950;


    private static int BUFSIZE = 5;

    public static void main(String[] args) {

        ServerSocket tcpServer = null;
        try {
            System.out.println("Server Started");
            tcpServer = new ServerSocket(MYPORT);
            int threadId = 1;
            tcpServer.setReuseAddress(true);
            // The main thread is just accepting new connections
            while (true) {
                Socket client = tcpServer.accept();

                RequestHandler clientSock = new RequestHandler(client, threadId++, BUFSIZE);

                // The background thread will handle each client separately
                new Thread(clientSock).start();
            }
        } catch (IOException e) {

            System.err.println("Error! in creating the ServerSocket: " + e.getMessage());
        } finally {
            if (tcpServer != null) {
                try {
                    tcpServer.close();
                } catch (IOException e) {
                    System.err.printf("Error! could not  close socket, %s", e.getMessage());
                }
            }
        }
    }


    private static class RequestHandler implements Runnable {
        private final Socket clientSocket;
        private final int threadId;
        byte[] buf;

        private RequestHandler(Socket socket, int threadId, int BUFSIZE) {
            this.clientSocket = socket;
            this.threadId = threadId;
            buf = new byte[BUFSIZE];
        }

        @Override
        public void run() {

            int messageLength;

            do {
                try {
                    messageLength = clientSocket.getInputStream().read(buf);
                } catch (IOException e) {
                    System.err.printf("Error! could not receive message, %s", e.getMessage());
                    return;
                }

                if (messageLength > 0) {
                    System.out.printf(" Thread ID: %s", threadId);
                    System.out.printf(" TCP echo request from %s", clientSocket.getInetAddress().getHostAddress());
                    System.out.printf(" using port %d\n", clientSocket.getPort());
                    try {

                        clientSocket.getOutputStream().write(buf, 0, messageLength);
                    } catch (IOException e) {
                        System.err.printf("Error! could not send message, %s", e.getMessage());
                        return;
                    } catch (ArrayIndexOutOfBoundsException e1) {
                        System.err.printf("Error! Buffer size does not match message length  , %s", e1.getMessage());
                        return;
                    }

                    System.out.println("Server received: " + messageLength);
                }
            } while (messageLength > 0);

            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.printf("Error! could not  close client socket, %s", e.getMessage());
            }

            System.out.println(" Thread ID: " + threadId + " Terminated");
        }

    }
}

