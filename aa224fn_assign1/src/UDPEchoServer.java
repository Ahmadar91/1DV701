
/*
  UDPEchoServer.java
  A simple echo server with no error handling
*/

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;



public class UDPEchoServer
{



	private static final int BUFSIZE = 1024;


	private static final int MYPORT = 4950;


	public static void main(String[] args)
	{


		byte[] buf = new byte[BUFSIZE];

		/* Create socket */
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket(null);

		} catch (SocketException e) {
			System.err.println("Error! could not  close client socket "+ e.getMessage());
		}
		try
		{
			/* Create local bind point */
			SocketAddress localBindPoint = new InetSocketAddress(MYPORT);
			if (socket != null) {
				socket.bind(localBindPoint);
			}
			System.out.println("Server Started with buffer size: " + BUFSIZE + " Port: " + socket.getLocalPort());
		} catch (SocketException e)
		{
			System.err.println("Error! could not  connect to socket "+ e.getMessage());
			return;
		}
		catch (NullPointerException e1)
		{
			System.err.println("Socket port is empty (null)"+ e1.getMessage());
			return;
		}

		while (true)
		{
			DatagramPacket receivePacket;
			try
			{
				/* Create datagram packet for receiving message */
				receivePacket = new DatagramPacket(buf, buf.length);
				/* Receiving message */
				socket.receive(receivePacket);

			} catch (IOException e)
			{
				System.err.println("Error! could not receive message "+e.getMessage());
				return;
			}
			try
			{
				/* Create datagram packet for sending message */
				DatagramPacket sendPacket = new DatagramPacket(receivePacket.getData(), receivePacket.getLength(),
						receivePacket.getAddress(), receivePacket.getPort());

				/* Send message */
				socket.send(sendPacket);
				System.out.printf("UDP echo request from %s", receivePacket.getAddress().getHostAddress());
				System.out.printf(" using port %d\n", receivePacket.getPort());
			} catch (IOException e)
			{
				System.err.println("Error! could not send message "+ e.getMessage());
				return;
			}
		}

	}
}