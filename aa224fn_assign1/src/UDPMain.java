public class UDPMain {


    public static void main(String[] args) {


        // program arguments in order: IP - Port - BufferSize - Transfer rate
        try {
            if (args.length != 4) {
                System.err.printf("usage: %s server_name IP\n", args[0]);
                System.err.printf("usage: %s server_name port\n", args[1]);
                System.err.printf("usage: %s server_name Buffer Size\n", args[2]);
                System.err.printf("usage: %s server_name Message Transfer Rate\n", args[3]);
                System.exit(1);
            }
        } catch (Exception e) {
            System.err.printf("Missing arguments please insert 4 arguments: IP - Port - Buffer size - Transfer Tate, %s\n", e.getMessage());
        }

        UDPEchoClient Client;

        try {
            Client = new UDPEchoClient(args[0], args[1], args[2], args[3], "An Echo Message");

            Client.run();



        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.printf("Arguments are empty, %s\n", e.getMessage());
            System.exit(1);
        }
    }
}
