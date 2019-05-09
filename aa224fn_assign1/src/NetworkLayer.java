import java.util.StringTokenizer;


public abstract class NetworkLayer {


    private String ip;

    private int port = 4950;

    private int bufSize = 1024;

    private int transferRate = 0;


    private String message;
    private byte[] buffer;


    NetworkLayer() {

    }


    public String getIpAddr() {
        return ip;
    }


    public void setIpAddr(String ip) {
        if (!checkIp(ip)) {
            System.err.println("Wrong IP address, the format must be in [0-255].[0-255].[0-255].[0-255]");
            System.exit(1);

        }
        this.ip = ip;
    }


    public int getPort() {
        return port;
    }


    public void setPort(String port) {

        int check = 0;
        try {
            check = Integer.parseInt(port);
        } catch (NumberFormatException e) {
            System.err.println("Error! Port is not numerical "+ e.getMessage());
            System.exit(1);
        }
        if (!checkPortInRange(check)) {
            System.err.println("Wrong port range, port number must be between 0 and 65535");
            System.exit(1);
        }
        this.port = check;
    }


    public int getBufferSize() {
        return bufSize;
    }


    public void setBufferSize(String bufferSize) {

        int check = 0;
        try {
            check = Integer.parseInt(bufferSize);
        } catch (NumberFormatException e) {
            System.err.println("Error! Buffer size is not numerical "+ e.getMessage());
            System.exit(1);

        }

        if (!checkBufferSize(check)) {
            System.err.println("buffer size range inserted is under or equal to 0 ");
            System.exit(1);
        }
        this.bufSize = check;

    }


    public int getMessagesTransferRate() {
        return transferRate;
    }


    public void setMessagesTransferRate(String messageRate) {

        int check = 0;
        try {
            check = Integer.parseInt(messageRate);
        } catch (NumberFormatException e) {
            System.err.println("Error! Message rate is not numerical "+ e.getMessage());
            System.exit(1);
        }

        if (!checkMessageRate(check)) {
            System.err.println("Wrong message rate, must be large than zero");
            System.exit(1);
        }
        this.transferRate = check;
    }

    public void setMessageForUDP(String message) {

        if (CheckMessageIsEmpty(message)) {
            System.err.println("Invalid input, message is empty ");
            System.exit(1);


        } else if (!checkMessageForUDP(message)) {
            System.err.println("Invalid input, message is bigger than the accepted UDP packet size which is  65,535 ");
            System.exit(1);
        }
        this.message = message;
    }


    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {

        if (CheckMessageIsEmpty(message)) {
            System.err.println("Invalid input, message is empty ");

            System.exit(1);
        }
        this.message = message;
    }


    private boolean CheckMessageIsEmpty(String message) {
        return message.isEmpty();

    }

    private boolean checkMessageForUDP(String message) {
        return (message.length() < 65507);

    }


    private boolean checkIp(String ip) {

        //  Check if the string is valid ip string

        if (ip == null || ip.length() < 7 || ip.length() > 15)
            return false;

        //  Check the ip address string if its in the format of [x].[x].[x].[x]

        StringTokenizer stringTokenizer = new StringTokenizer(ip, ".");
        if (stringTokenizer.countTokens() != 4)
            return false;

        while (stringTokenizer.hasMoreTokens()) {

            //  Get the current token and convert it to a integer and check if its between 0-255

            String Number = stringTokenizer.nextToken();

            try {
                int ipNumberValue = Integer.valueOf(Number).intValue();
                if (ipNumberValue < 0 || ipNumberValue > 255)
                    return false;
            } catch (NumberFormatException ex) {
                return false;
            }
        }
        return true;
    }


    private boolean checkPortInRange(int port) {
        return (port > 0 && port < 65507);

    }


    private boolean checkBufferSize(int bufferSize) {
        return (bufferSize > 0);

    }


    private boolean checkMessageRate(int messageRate) {
        return (messageRate > 0);


    }

    public boolean sleepUntilOneSecond(int count, long time) {

        if (getMessagesTransferRate() <= count) {
            try {
                Thread.sleep(System.currentTimeMillis() - time);
                System.out.println("\nMessage transfer rate reached max!");
            } catch (InterruptedException e) {
                try{
                    Thread.currentThread().interrupt();
                }catch (RuntimeException e1){
                    System.err.println("Thread interrupted " + e.getMessage());
                    System.exit(1);
                }

            }
            return true;
        }
        return false;
    }


    public byte[] getByteArr() {

        try {
            buffer = new byte[getBufferSize()];
        } catch (OutOfMemoryError e) {
            System.err.println("Buffer Size is too large " + e.getMessage());
            System.exit(1);
        }
        return buffer;
    }


}