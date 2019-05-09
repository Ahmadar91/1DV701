/**
 * The type Response.
 */
public class Response
{
    private Status status;
    private Pairs pairs = new Pairs();
    private Buffer buffer;

    /**
     * Instantiates a new Response.
     */
    Response()
    {

    }

    /**
     * Instantiates a new Response.
     *
     * @param status the status
     */
    Response(Status status)
    {
        this.status = status;
    }

    /**
     * Sets status.
     *
     * @param status the status
     */
    public void setStatus(Status status)
    {
        this.status = status;
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public Status getStatus()
    {
        return status;
    }

    /**
     * Sets pairs.
     *
     * @param pairs the pairs
     */
    public void setPairs(Pairs pairs)
    {
        this.pairs = pairs;
    }

    /**
     * Gets pairs.
     *
     * @return the pairs
     */
    public Pairs getPairs()
    {
        return pairs;
    }

    /**
     * Write.
     *
     * @param data the data
     */
    public void write(byte[] data)
    {
        buffer.write(data);
    }

    /**
     * Build.
     */
    public void build()
    {
        String statusMessage = status.toString();
        String headersString = pairs.dump();

        buffer = new Buffer(15 + statusMessage.length() + headersString.length());
        buffer.write("HTTP/1.1 ");
        buffer.write(status.getValue() + " ");
        buffer.write(statusMessage + "\r\n");
        buffer.write(headersString);
    }

    /**
     * Gets buffer.
     *
     * @return the buffer
     */
    public Buffer getBuffer()
    {
        return buffer;
    }

    public String toString()
    {
        return String.format("Status: %s", status.toString());
    }
}


