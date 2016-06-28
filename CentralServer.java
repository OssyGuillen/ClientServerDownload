import java.net.*;
import java.io.*;
import java.util.Scanner;

public class CentralServer extends Thread
{
   private ServerSocket socket;
    DataInputStream in;
    DataOutputStream out;
    InputStream inStream;
    OutputStream outStream;
    String message="";
    String received="";
    Socket server;
   
   public CentralServer(int port) throws IOException
   {
      socket = new ServerSocket(port);
      socket.setSoTimeout(500000);

   }

   public void run()
   {
      while(true)
      {
         try
         {
            System.out.println("Waiting for client on port " +
            socket.getLocalPort() + "...");
            server = socket.accept();
            System.out.println("Just connected to "
                  + server.getRemoteSocketAddress());

            //getIOStreams();
            processConnection();
            // in =
            //       new DataInputStream(server.getInputStream());
            // System.out.println(in.readUTF());
            // out =
            //      new DataOutputStream(server.getOutputStream());
            // out.writeUTF("Thank you for connecting to "
            //   + server.getLocalSocketAddress() + "\nGoodbye!");
            in.close();
            out.close();
            server.close();
         }catch(SocketTimeoutException s)
         {
            System.out.println("Socket timed out!");
            break;
         }catch(IOException e)
         {
            e.printStackTrace();
            break;
         }
      }
   }

    public void processConnection() {
        //sendData("Connection established with the server");
        do {

            try {
                inStream = server.getInputStream ();
                in = new DataInputStream ( inStream );
                message = in.readUTF();   
                System.out.println("Client sent: "+message);
                DataInputStream dis = new DataInputStream(System.in);
                message = dis.readLine();
                outStream = server.getOutputStream();
                out = new DataOutputStream (outStream); 
                System.out.println("Enter your message here: ");
                out.writeUTF(message);  
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (!message.equals("EXIT"));
    }

   public static void main(String [] args)
   {
      int port = Integer.parseInt(args[0]);
      try
      {
         Thread t = new CentralServer(port);
         t.start();
      }catch(IOException e)
      {
         e.printStackTrace();
      }
   }
}