import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The type Server main.
 */
public class ServerMain
{
    /**
     * The Port.
     */
    private static int port = 8080;

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args)
    {
        Logger logger = new Console();
        int index = 1;

        ServerSocket serverSocket;
        try
        {
            serverSocket = new ServerSocket(port);
        } catch (IOException e)
        {
            System.out.println("Port is not available");
            return;
        }
        Parser parser = new Parser(logger, "src/resources", "index.html");

        while (true)
        {
            try
            {
                Socket socket = serverSocket.accept();
                Client client = new Client(index++, socket, logger, parser);

                // new thread created for new client
                client.start();

            } catch (IOException e)
            {
                System.out.println("Error while initiating the connection");
            }

        }
    }
}
