/**
 * The type Uri.
 */
public class Uri
{
    private Scheme scheme;
    private String authority;
    private String path;
    private Pairs query;

    /*
        The main reason I had to create this class is to deal with 302 error. Where a client has to insert a username and password in the uri query to access the admin directory
     */
    /**
     * Instantiates a new Uri.
     */
    public Uri()
    {

    }

    /**
     * Instantiates a new Uri.
     *
     * @param scheme    the scheme
     * @param authority the authority
     * @param path      the path
     */
    public Uri(Scheme scheme, String authority, String path)
    {
        this(scheme, authority, path, new Pairs());
    }

    /**
     * Instantiates a new Uri.
     *
     * @param scheme    the scheme
     * @param authority the authority
     * @param path      the path
     * @param query     the query
     */
    public Uri(Scheme scheme, String authority, String path, Pairs query)
    {
        this.scheme = scheme;
        this.authority = authority;
        this.path = path;
        this.query = query;
    }

    /**
     * Parse uri.
     *
     * @param uri the uri
     * @return the uri
     */
    public static Uri parse(String uri)
    {
        Scheme scheme = Scheme.fromString(uri);

        // Authority
        String authority = "";
        int startPos = uri.indexOf("//");
        int endPosition;
        if (startPos >= 0)
        {
            endPosition = uri.indexOf("/", startPos + 2);
            authority = uri.substring(startPos, endPosition >= 0 ? endPosition - startPos : uri.length());
            uri = uri.substring(startPos + authority.length());
        }


        // Path
        String path = "";
        startPos = uri.indexOf("/");
        if (startPos >= 0)
        {
            endPosition = uri.indexOf("?", startPos + 1);
            path = uri.substring(startPos, endPosition >= 0 ? endPosition - startPos : uri.length());
            uri = uri.substring(startPos + authority.length());
        }

        Pairs query = new Pairs();
        startPos = uri.indexOf("?");
        if (startPos >= 0)
        {
            query = parseQuery(uri.substring(startPos + 1));
        }
        return new Uri(scheme, authority, path, query);
    }

    /**
     * Parse query pairs.
     *
     * @param uri the uri
     * @return the pairs
     */
    public static Pairs parseQuery(String uri)
    {
        Pairs query = new Pairs();
        int equalPos = uri.indexOf("=");
        int endPos = 0;
        String key, value;
        while (equalPos >= 0)
        {
            key = uri.substring(endPos, equalPos);
            endPos = uri.indexOf("&", equalPos);
            if (endPos >= 0)
            {
                value = uri.substring(equalPos + 1, endPos);
                ++endPos;
            } else
            {
                value = uri.substring(equalPos + 1);
            }

            query.add(key, value);

            equalPos = uri.indexOf("=", equalPos + 1);
        }
        return query;
    }

    /**
     * Gets path.
     *
     * @return the path
     */
    public String getPath()
    {
        return path;
    }

    /**
     * Gets query.
     *
     * @return the query
     */
    public Pairs getQuery()
    {
        return query;
    }

    public String toString()
    {
        return Scheme.toString(scheme) + authority + path;
    }
}
