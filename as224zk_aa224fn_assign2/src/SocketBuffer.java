import java.io.IOException;
import java.io.InputStream;

/**
 * The type Socket buffer.
 */
public class SocketBuffer
{
    private Buffer buffer = new Buffer();
    private InputStream inputStream;

    /**
     * Instantiates a new Socket buffer.
     *
     * @param inputStream the input stream
     */
    SocketBuffer(InputStream inputStream)
    {
        this.inputStream = inputStream;
    }

    private boolean receive(int length) throws IOException
    {
        int bufferLen = length < 0 ? 1024 : length;
        byte[] data = new byte[bufferLen];
        int total = 0;
        int count;
        while (total < bufferLen)
        {
            count = inputStream.read(data, 0, bufferLen - total);
            if (count == -1)
            {
                throw new IOException("Stopped receiving data");
            }
            buffer.write(data, count);
            if (length == -1) {
                break;
            }
            total += count;
        }
        return true;
    }

    /**
     * Read line string.
     *
     * @return the string
     * @throws IOException the io exception
     */
    public String readLine() throws IOException
    {
        int position = buffer.hasLine();
        while (position < 0)
        {
            receive(-1);
            position = buffer.hasLine();
        }
        return buffer.extractString(position, 2);
    }

    /**
     * Read byte [ ].
     *
     * @param length the length
     * @return the byte [ ]
     * @throws IOException the io exception
     */
    public byte[] read(int length) throws IOException
    {
        if (buffer.getSize() >= length)
        {
            return buffer.get(length);
        } else
        {
            receive(length - buffer.getSize());
            return buffer.get(length);
        }
    }
}
