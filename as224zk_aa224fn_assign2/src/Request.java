/**
 * The type Request.
 */
public class Request
{
    private Method method;
    private Pairs headers;
    private Uri uri;
    private String protocol;
    private Buffer body;

    /**
     * Instantiates a new Request.
     *
     * @param method   the method
     * @param uri      the uri
     * @param protocol the protocol
     */
    public Request(Method method, Uri uri, String protocol)
    {
        this.method = method;
        this.protocol = protocol;
        this.uri = uri;
    }

    /**
     * Gets method.
     *
     * @return the method
     */
    public Method getMethod()
    {
        return method;
    }

    /**
     * Gets headers.
     *
     * @return the headers
     */
    public Pairs getHeaders()
    {
        return headers;
    }

    /**
     * Sets method.
     *
     * @param method the method
     */
    public void setMethod(Method method)
    {
        this.method = method;
    }

    /**
     * Sets headers.
     *
     * @param headers the headers
     */
    public void setHeaders(Pairs headers)
    {
        this.headers = headers;
    }


    public String toString()
    {
        return String.format("%s %s", method.toString(), uri.toString());
    }

    /**
     * Gets body.
     *
     * @return the body
     */
    public Buffer getBody()
    {
        return body;
    }

    /**
     * Sets body.
     *
     * @param body the body
     */
    public void setBody(Buffer body)
    {
        this.body = body;
    }

    /**
     * Gets uri.
     *
     * @return the uri
     */
    public Uri getUri()
    {
        return uri;
    }

}
