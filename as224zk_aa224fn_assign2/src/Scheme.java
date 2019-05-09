/**
 * The enum Scheme.
 */
public enum Scheme
{
    /**
    * Empty scheme.
    */
    EMPTY,
    /**
     * Http scheme.
     */
    HTTP,
    /**
     * Https scheme.
     */
    HTTPS
    ;

    /**
     * To string string.
     *
     * @param scheme the scheme
     * @return the string
     */
    public static String toString(Scheme scheme) {
        switch (scheme) {
            case HTTP: return "http:";
            case HTTPS: return "https:";
            case EMPTY: return "";
            default: return "";
        }
    }

    /**
     * From string scheme.
     *
     * @param scheme the scheme
     * @return the scheme
     */
    public static Scheme fromString(String scheme) {
        if (scheme.startsWith("http:")) {
            return HTTP;
        } else if (scheme.startsWith("https:")) {
            return HTTPS;
        } else {
            return EMPTY;
        }
    }

}
