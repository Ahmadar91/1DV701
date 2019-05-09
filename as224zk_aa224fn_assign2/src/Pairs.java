import java.util.ArrayList;
import java.util.List;

/**
 * The type Pairs.
 */
public class Pairs
{
    /**
     * The type Pair.
     */
    class Pair
    {
        private String key;
        private String value;

        /**
         * Instantiates a new Pair.
         *
         * @param key   the key
         * @param value the value
         */
        Pair(String key, String value)
        {
            this.key = key;
            this.value = value;
        }

        /**
         * Gets key.
         *
         * @return the key
         */
        public String getKey()
        {
            return key;
        }

        /**
         * Gets value.
         *
         * @return the value
         */
        public String getValue()
        {
            return value;
        }

    }

    private List<Pair> pairs = new ArrayList<>();

    /**
     * Instantiates a new Pairs.
     */
    public Pairs() {

    }

    /**
     * Instantiates a new Pairs.
     *
     * @param buffer the buffer
     */
    public Pairs(Buffer buffer) {
        extractFromBuffer(buffer);
    }

    /**
     * Add.
     *
     * @param <T>   the type parameter
     * @param key   the key
     * @param value the value
     */
    public <T> void add(String key, T value)
    {
        add(new Pair(key, String.valueOf(value)));
    }

    /**
     * Add.
     *
     * @param pair the pair
     */
    public void add(Pair pair)
    {
        pairs.add(pair);
    }

    /**
     * Dump string.
     *
     * @return the string
     */
    public String dump()
    {
        StringBuilder sb = new StringBuilder();
        for (Pair pair : pairs)
        {
            sb.append(pair.getKey()).append(": ").append(pair.getValue()).append("\r\n");
        }
        sb.append("\r\n");
        return sb.toString();
    }

    /**
     * Gets value.
     *
     * @param key the key
     * @return the value
     */
    public String getValue(String key)
    {
        return getValue(key, false);
    }

    /**
     * Gets value.
     *
     * @param key           the key
     * @param caseSensitive the case sensitive
     * @return the value
     */
    public String getValue(String key, boolean caseSensitive)
    {
        Pair pair = get(key, caseSensitive);
        if (pair == null)
        {
            return "";
        }
        return pair.getValue();
    }

    /**
     * Get pair.
     *
     * @param key the key
     * @return the pair
     */
    public Pair get(String key)
    {
        return get(key, false);
    }

    /**
     * Get pair.
     *
     * @param key           the key
     * @param caseSensitive the case sensitive
     * @return the pair
     */
    public Pair get(String key, boolean caseSensitive)
    {
        for (Pair pair : pairs)
        {
            if (!caseSensitive)
            {
                if (pair.getKey().equalsIgnoreCase(key))
                {
                    return pair;
                }
            } else
            {
                if (pair.getKey().equals(key))
                {
                    return pair;
                }
            }

        }
        return null;
    }

    /**
     * Gets keys.
     *
     * @return the keys
     */
    public List<String> getKeys() {
        List<String> keys = new ArrayList<>();
        for (Pair pair : pairs)
        {
            keys.add(pair.getKey());
        }
        return keys;
    }

    public String toString() {
        return dump();
    }

    /**
     * Extract from buffer.
     *
     * @param buffer the buffer
     */
    public void extractFromBuffer(Buffer buffer) {
        if (buffer == null) {
            return;
        }
        String line;
        int linePosition = buffer.hasLine();
        while (linePosition >= 0)
        {
            line = buffer.extractString(linePosition, 2);
            if ((line == null) || (line.isEmpty()) || line.equals("\r\n"))
            {
                break;
            }
            // Skip Boundaries
            String[] headerLine = line.split(": ");
            if (headerLine.length == 2)
            {
                add(headerLine[0], headerLine[1]);
            } else {
                add(line, "");
            }
            linePosition = buffer.hasLine();
        }
    }

}
