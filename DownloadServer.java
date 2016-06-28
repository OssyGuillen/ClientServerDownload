import java.net.*;
import java.io.*;

public class DownloadServer extends Thread
{
   private ServerSocket socket;
   
   public DownloadServer(int port) throws IOException
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
            Socket server = socket.accept();
            System.out.println("Just connected to "
                  + server.getRemoteSocketAddress());
            DataInputStream in =
                  new DataInputStream(server.getInputStream());
            System.out.println(in.readUTF());
            DataOutputStream out =
                 new DataOutputStream(server.getOutputStream());
            out.writeUTF("Thank you for connecting to "
              + server.getLocalSocketAddress() + "\nGoodbye!");
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