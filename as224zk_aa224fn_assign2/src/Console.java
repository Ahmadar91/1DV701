/**
 * The type Console.
 */
public class Console implements Logger
{
    @Override
    public void print(String message)
    {
        System.out.println(message);
    }
}
