/**
 * The type Buffer.
 */
public class Buffer
{
    private int size = 0;
    private byte[] buffer;

    /**
     * Instantiates a new Buffer.
     */
    Buffer()
    {

    }

    /**
     * Instantiates a new Buffer.
     *
     * @param preAllocated the pre allocated
     */
    public Buffer(int preAllocated)
    {
        allocate(preAllocated);
    }

    /**
     * Instantiates a new Buffer.
     *
     * @param data the data
     */
    public Buffer(byte[] data)
    {
        allocate(data.length);
        System.arraycopy(data, 0, buffer, 0, data.length);
        size = data.length;
    }

    /**
     * Write.
     *
     * @param data the data
     */
    public void write(String data)
    {
        write(data.getBytes());
    }

    /**
     * Write.
     *
     * @param data the data
     */
    public void write(byte[] data)
    {
        write(data, data.length);
    }

    /**
     * Write.
     *
     * @param data   the data
     * @param length the length
     */
    public void write(byte[] data, int length)
    {
        allocate(this.size + length);
        for (int i = 0; i < length; ++i)
        {
            buffer[this.size + i] = data[i];
        }
        this.size += length;
    }
    /*
    Allocate the buffer and return the size of it
     */
    private int allocate(int size)
    {
        if ((buffer == null) || (buffer.length < size))
        {
            if (this.size == 0)
            {
                buffer = new byte[size];
            } else
            {
                byte[] tmp = new byte[this.size];
                System.arraycopy(buffer, 0, tmp, 0, this.size);
                buffer = new byte[size];
                System.arraycopy(tmp, 0, buffer, 0, this.size);
            }

            return size;
        }
        return 0;
    }

    /**
     * Extract int (length) of the buffer.
     *
     * @param length the length
     * @return the int
     */
    public int extract(int length)
    {
        if (length > size)
        {
            return 0;
        }
        System.arraycopy(buffer, length, buffer, 0, this.size - length);
        this.size -= length;
        return this.size - length;
    }

    /**
     * Check if it Has bytes and return position. \
     * This method is used to check for \r\n in the headers (check hasLine() method).. and for (--) in order to find the boundary file when reading the file in bytes.
     *
     * @param bytes the bytes
     * @return the int
     */
    public int hasBytes(byte[] bytes)
    {
        for (int i = 0; i < size; ++i)
        {
            int x = 0;
            while (x < bytes.length)
            {
                if (((i + x) >= buffer.length) || (buffer[i + x] != bytes[x]))
                {
                    break;
                }
                ++x;
            }
            if (x == bytes.length)
            {
                return i;
            }
        }
        return -1;
    }

    /**
     * Has line int.
     *
     * @return the int
     */
    public int hasLine()
    {
        return hasBytes(new byte[]{'\r', '\n'});
    }

    /**
     * Extract string string.
     *
     * @param position the position
     * @param extra    the extra
     * @return the string
     */
    public String extractString(int position, int extra)
    {
        String line = new String(buffer, 0, position);
        extract(position + extra);
        return line;
    }

    /**
     * Gets size.
     *
     * @return the size
     */
    public int getSize()
    {
        return size;
    }

    /**
     * Get byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] get()
    {
        return get(size);
    }

    /**
     * Get byte [ ].
     *
     * @param length the length
     * @return the byte [ ]
     */
    public byte[] get(int length)
    {
        byte[] data = new byte[length];
        System.arraycopy(buffer, 0, data, 0, length);
        return data;
    }
}
