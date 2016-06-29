import java.io.*;
import java.net.*;
import java.security.cert.X509Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateException;
import java.security.PublicKey;

/*
 * A chat server that delivers public and private messages.
 */
public class MultiServer {

  // The server socket.
  private static ServerSocket serverSocket = null;
  // The client socket.
  private static Socket clientSocket = null;

  // This chat server can accept up to maxClientsCount clients' connections.
  private static final int maxClientsCount = 10;
  private static final clientThread[] threads = new clientThread[maxClientsCount];

  public static void main(String args[]) {

    // The default port number.
    int portNumber = 0;
    if (args.length < 1) {
      System.out.println("Wrong command: java MultiServer <portNumber>\n");
      return;
    } else {
      portNumber = Integer.parseInt(args[0]);
      System.out.println("Usage: java MultiServer <host> <portNumber>\n"
          + "Now using port number=" + portNumber);
    }

    // /*
    //  * Send the server's certificate.
    //  */
    // try {
    //   FileInputStream fin = new FileInputStream("cert.pem");
    //   CertificateFactory f = CertificateFactory.getInstance("X.509");
    //   X509Certificate certificate = (X509Certificate)f.generateCertificate(fin);
    //   PublicKey pk = certificate.getPublicKey();

    // } catch (FileNotFoundException ex) {

    // } catch (CertificateException ex) {

    // }

    /*
     * Open a server socket on the portNumber. Note that we can
     * not choose a port less than 1023 if we are not privileged users (root).
     */
    try {
      serverSocket = new ServerSocket(portNumber);
    } catch (IOException e) {
      System.out.println(e);
    }

    /*
     * Create a client socket for each connection and pass it to a new client
     * thread.
     */
    while (true) {
      try {
        clientSocket = serverSocket.accept();
        int i = 0;
        for (i = 0; i < maxClientsCount; i++) {
          if (threads[i] == null) {
            (threads[i] = new clientThread(clientSocket, threads)).start();
            break;
          }
        }
        if (i == maxClientsCount) {
          PrintStream os = new PrintStream(clientSocket.getOutputStream());
          os.println("Server too busy. Try later.");
          os.close();
          clientSocket.close();
        }
      } catch (IOException e) {
        System.out.println(e);
      }
    }
  }
}


class clientThread extends Thread {

  private String clientName = null;
  private DataInputStream is = null;
  private PrintStream os = null;
  private Socket clientSocket = null;
  private final clientThread[] threads;
  private int maxClientsCount;

  public clientThread(Socket clientSocket, clientThread[] threads) {
    this.clientSocket = clientSocket;
    this.threads = threads;
    maxClientsCount = threads.length;
  }

  public void run() {
    int maxClientsCount = this.maxClientsCount;
    clientThread[] threads = this.threads;

    try {
      /*
       * Create input and output streams for this client.
       */
      is = new DataInputStream(clientSocket.getInputStream());
      os = new PrintStream(clientSocket.getOutputStream());
      String name = "name";
      String password = "";
      /* Welcome the new the client. */
      os.println("Welcome "
          + " to our download server.\nTo leave enter QUIT in a new line.");

      /* Register required */
      String line = is.readLine();
      while (!line.startsWith("REGISTER")){
        os.println("ERROR: You need to register. Type REGISTER.");
        line = is.readLine();
      }
      os.println("Enter a login.");
      name = is.readLine().trim();
      os.println("Enter a password.");
      password = is.readLine().trim();
      os.println("READY");

      /* Send certificate */
      try {
        //ServerSocket servsock = new ServerSocket(8080);
        File myFile = new File("f.txt");
        //Socket sock = servsock.accept();
        //while(true) {
          byte[] mybytearray = new byte[(int) myFile.length()];
          BufferedInputStream bis = new BufferedInputStream(new FileInputStream(myFile));
          bis.read(mybytearray, 0, mybytearray.length);
          OutputStream out = clientSocket.getOutputStream();
          out.write(mybytearray, 0, mybytearray.length);
          out.flush();
          //sock.close();
          //break;
        //}
      } catch (IOException ex) {
          System.out.println("Can't get socket input stream. ");
      }


      /* Start the conversation. */
      while (true) {
        line = is.readLine();
        if (line.startsWith("QUIT")) {
          break;
        }
        /* If the message is private sent it to the given client. */
        if (line.startsWith("REGISTER")) {
          os.println("You're registed.");
        } else {
        }
      }
      
      os.println("*** Bye " + name + " ***");

      /*
       * Clean up. Set the current thread variable to null so that a new client
       * could be accepted by the server.
       */
      synchronized (this) {
        for (int i = 0; i < maxClientsCount; i++) {
          if (threads[i] == this) {
            threads[i] = null;
          }
        }
      }
      /*
       * Close the output stream, close the input stream, close the socket.
       */
      is.close();
      os.close();
      clientSocket.close();
    } catch (IOException e) {
    }
  }
}
