/**
 * The enum Status.
 */
public enum Status
{
    /**
    * The Ok.
    */
    OK(200),
    /**
     * Created status.
     */
    CREATED (201),
    /**
     * Forbidden status.
     */
    FORBIDDEN(403),
    /**
     * Not found status.
     */
    NOT_FOUND (404),
    /**
     * Moved permanently status.
     */
    MOVED_PERMANENTLY (301),
    /**
     * Found status.
     */
    Found (302),
    /**
     * Bad request status.
     */
    Bad_Request (400),
    /**
     * Request timeout status.
     */
    Request_Timeout (408),
    /**
     * Internal server error status.
     */
    Internal_Server_Error (500),
    /**
     * Not implemented status.
     */
    Not_Implemented (501)

    ;

    private int value;

    Status(int value)
    {
        this.value = value;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public int getValue()
    {
        return value;
    }

    public String toString()
    {
        switch (value)
        {
            case 200:
                return "OK";
            case 201:
                return "Created";
            case 301:
                return "Moved Permanently";
            case 302:
                return "Found";
            case 400:
                return "Bad Request";
            case 403:
                return "FORBIDDEN";
            case 404:
                return "Not Found";
            case 408:
                return "Request Timeout";
            case 500:
                return "Internal Server Error";
            case 501:
                return "Not Implemented";

            default:
                return "";
        }


    }
}
