import java.io.*;
import java.net.*;

public class MultiClient implements Runnable {

  // The client socket
  private static Socket clientSocket = null;
  // The output stream
  private static PrintStream os = null;
  // The input stream
  private static DataInputStream is = null;

  private static BufferedReader inputLine = null;
  private static boolean registered = false;
  private static boolean closed = false;
  
  public static void main(String[] args) {

    // The default port.
    int portNumber = 1234;
    // The default host.
    String host = "localhost";

    if (args.length < 2) {
      System.out
          .println("Usage: java MultiClient <host> <portNumber>\n"
              + "Now using host=" + host + ", portNumber=" + portNumber);
    } else {
      host = args[0];
      portNumber = Integer.valueOf(args[1]).intValue();
    }

    /*
     * Open a socket on a given host and port. Open input and output streams.
     */
    try {
      clientSocket = new Socket(host, portNumber);
      inputLine = new BufferedReader(new InputStreamReader(System.in));
      os = new PrintStream(clientSocket.getOutputStream());
      is = new DataInputStream(clientSocket.getInputStream());
    } catch (UnknownHostException e) {
      System.err.println("Don't know about host " + host);
    } catch (IOException e) {
      System.err.println("Couldn't get I/O for the connection to the host "
          + host);
    }

    /*
     * If everything has been initialized then we want to write some data to the
     * socket we have opened a connection to on the port portNumber.
     */
    if (clientSocket != null && os != null && is != null) {
      try {

        /* Create a thread to read from the server. */
        new Thread(new MultiClient()).start();
        /* Register */
        while (!registered) {
          os.println(inputLine.readLine().trim());
        }

        /* Send Certificate */

        while (!closed) {
          os.println(inputLine.readLine().trim());
        }
        /*
         * Close the output stream, close the input stream, close the socket.
         */
        os.close();
        is.close();
        clientSocket.close();
      } catch (IOException e) {
        System.err.println("IOException:  " + e);
      }
    }
  }

  /*
   * Create a thread to read from the server.
   * 
   * @see java.lang.Runnable#run()
   */
  public void run() {
    /*
     * Keep on reading from the socket till we receive "Bye" from the
     * server. Once we received that then we want to break.
     */
    String responseLine;
    try {

      /* Register */
      responseLine = is.readLine();
      System.out.println(responseLine);
      responseLine = is.readLine();
      System.out.println(responseLine);
      while ((responseLine = is.readLine()) != null) {
        if (responseLine.equals("READY")) {
          registered = true;
          break; 
        }
        System.out.println(responseLine);
      }

      /* Send certificate */
      //Socket sock = new Socket("127.0.0.1", 8080);
      byte[] mybytearray = new byte[1024];
      InputStream in = clientSocket.getInputStream();
      FileOutputStream fos = new FileOutputStream("new.txt");
      BufferedOutputStream bos = new BufferedOutputStream(fos);
      int bytesRead = in.read(mybytearray, 0, mybytearray.length);
      bos.write(mybytearray, 0, bytesRead);
      bos.close();
      //sock.close();

      /* Start conversation */
      while ((responseLine = is.readLine()) != null) {
        System.out.println(responseLine);
        if (responseLine.indexOf("*** Bye") != -1)
          break;
        if (responseLine.equals("Registering...")) {
        }

      }
      closed = true;
    } catch (IOException e) {
      System.err.println("IOException:  " + e);
    }
  }
}