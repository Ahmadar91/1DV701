import java.util.Date;

/**
 * The type Stock response.
 */
public class StockResponse extends Response
{
    /**
     * Instantiates a new Stock response.
     *
     * @param status the status
     */
    StockResponse(Status status) {
        setStatus(status);
        Pairs headers = new Pairs();
        headers.add("Content-length", 0);
        headers.add("Server", "Ahmad WebServer");
        headers.add("Date", new Date().toString());
        setPairs(headers);
        build();
    }

    /**
     * Instantiates a new Stock response.
     *
     * @param status  the status
     * @param headers the headers
     */
    StockResponse(Status status, Pairs headers) {
        setStatus(status);
        headers.add("Server", "Ahmad WebServer");
        headers.add("Date", new Date().toString());
        setPairs(headers);
        build();
    }
}
