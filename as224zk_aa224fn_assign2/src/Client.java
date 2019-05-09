import java.io.*;
import java.net.Socket;
import java.net.SocketException;

/**
 * The type Client.
 */
public class Client extends Thread
{
    final private int id;
    final private Socket socket;
    final private Logger logger;
    final private Parser parser;
    private SocketBuffer socketBuffer;

    /**
     * Instantiates a new Client.
     *
     * @param id     the id
     * @param socket the socket
     * @param logger the logger
     * @param parser the parser
     */
    public Client(int id, Socket socket, Logger logger, Parser parser)
    {
        this.socket = socket;
        this.logger = logger;
        this.id = id;
        this.parser = parser;
    }

    private void logMessage(String message)
    {
        logger.print(String.format("[%d][%s:%d]: %s", id, socket.getInetAddress().toString(), socket.getPort(), message));
    }

    @Override
    public void run()
    {
        logMessage("Connected");
        try
        {
            socketBuffer = new SocketBuffer(socket.getInputStream());
        } catch (IOException e)
        {
            logMessage(e.getMessage());
            return;
        }


        while (!Thread.interrupted())

        {
            try
            {
                int TIMEOUT = 120000;   // Set time out to 2 min
                socket.setSoTimeout(TIMEOUT);
                Request request = readRequest();
                if (request == null)
                {
                    break;
                }
                parser.handle(this, request);
            } catch (SocketException e)
            {
                sendResponse(new StockResponse(Status.Request_Timeout));
            }
            catch (Exception e)
            {
                sendResponse(new StockResponse(Status.Internal_Server_Error));
            }

        }

        try

        {
            socket.close();
        } catch (IOException e)

        {
            logMessage(e.getMessage());
        }

        logMessage("Disconnected");

    }

    // Read the request
    // --------------------------
    private Request readRequest()
    {
        Request request;
        try
        {
            request = readRequestLine();
            readRequestHeaders(request);
            if ((request.getMethod() == Method.POST) || (request.getMethod() == Method.PUT))
            {
                int contentLength = toInteger(request.getHeaders().getValue("Content-Length"), -1);
                if (contentLength > 0)
                {
                    readRequestBody(request, contentLength);
                }
            }
        } catch (IOException e)
        {
            logMessage(e.getMessage());
            return null;
        }
        logMessage(request.toString());
        return request;
    }

    // Read the request line
    private Request readRequestLine() throws IOException
    {
        String line = socketBuffer.readLine();
        if (line == null)
        {
            throw new IOException("Empty request line");
        }
        String[] requestLine = line.split(" ");
        if (requestLine.length != 3)
        {
            sendResponse(new StockResponse(Status.Bad_Request));
            throw new IOException("Invalid request line");
        }
        Method method = Method.fromString(requestLine[0]);
        if (method == Method.INVALID)
        {
            sendResponse(new StockResponse(Status.Not_Implemented));
            throw new IOException("Request method Not Implemented");
        }

        Uri uri = Uri.parse(requestLine[1]);
        return new Request(method, uri, requestLine[2]);
    }

    // Read the rest of the request lines (headers)
    private void readRequestHeaders(Request request) throws IOException
    {
        Pairs pairs = new Pairs();
        while (true)
        {
            String line = socketBuffer.readLine();
            if ((line == null) || (line.isEmpty()) || line.equals("\r\n"))
            {
                break;
            }
            String[] headerLine = line.split(": ");
            if (headerLine.length != 2)
            {
                pairs.add(line, "");
            } else
            {
                pairs.add(headerLine[0], headerLine[1]);
            }
        }
        request.setHeaders(pairs);
    }

    /**
     * Read request body string.
     *
     * @param request       the request
     * @param contentLength the content length
     * @return the string
     */
    public String readRequestBody(Request request, int contentLength)
    {
        byte[] body;
        try
        {
            body = socketBuffer.read(contentLength);
        } catch (IOException e)
        {
            e.printStackTrace();
            return "";
        }
        System.out.println(new String(body));
        request.setBody(new Buffer(body));
        return new String(body);
    }

    /**
     * Send response.
     *
     * @param response the response
     */
    public void sendResponse(Response response)
    {

        logMessage("sending " + response.toString());

        try
        {
            socket.getOutputStream().write(response.getBuffer().get());
            socket.getOutputStream().flush();
        } catch (IOException e)
        {
            logMessage(e.getMessage());
            return;
        }
        logMessage("response sent");


    }
    /*
    Convert a String to integer
     */
    private int toInteger(String value, int defaultValue)
    {
        try
        {
            return Integer.parseInt(value);
        } catch (Exception e)
        {
            return defaultValue;
        }
    }
}
