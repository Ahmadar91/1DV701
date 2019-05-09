/**
 * The enum Method.
 */
public enum Method
{/**
 * Invalid method.
 */
INVALID(0),
    /**
     * Get method.
     */
    GET(1),
    /**
     * Put method.
     */
    PUT(2),
    /**
     * Post method.
     */
    POST(3);

    private int value;

    Method(int value)
    {
        this.value = value;
    }

    public String toString()
    {
        switch (value)
        {
            case 1:
                return "Get";
            case 2:
                return "Put";
            case 3:
                return "Post";
            default:
                return "";
        }
    }

    /**
     * From string method.
     *
     * @param method the method
     * @return the method
     */
    public static Method fromString(String method)
    {
        if (method.equals("GET"))
        {
            return Method.GET;
        } else if (method.equals("PUT"))
        {
            return Method.PUT;
        } else if (method.equals("POST"))
        {
            return Method.POST;
        }
        return Method.INVALID;
    }
}