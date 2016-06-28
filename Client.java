import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client
{
    static DataOutputStream out;
    static DataInputStream in;
    static InputStream inStream;
    static OutputStream outStream;
    static Socket client;
    static String message = "";

    public static void processConnection() {
        //sendData("Connection established with the client");
        do{
            try {
              System.out.println("Enter your message here: ");
              DataInputStream dis = new DataInputStream(System.in);
              message = dis.readLine();
              outStream = client.getOutputStream();
              out = new DataOutputStream (outStream);       
              out.writeUTF(message);    
              inStream = client.getInputStream ();
              in = new DataInputStream ( inStream );
              message = in.readUTF();
              System.out.println("Server Sent: "+message);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }while(!message.equals("EXIT"));
    }
   public static void main(String [] args)
   {
      String serverName = args[0];
      int port = Integer.parseInt(args[1]);


      try
      {
         System.out.println("Connecting to " + serverName +
       " on port " + port);
         client = new Socket(serverName, port);
         System.out.println("Just connected to " 
       + client.getRemoteSocketAddress());
         //getIOStreams();
         processConnection();
         // OutputStream outToServer = client.getOutputStream();
         // DataOutputStream out = new DataOutputStream(outToServer);
         // out.writeUTF("Hello from "
         //              + client.getLocalSocketAddress());
         // InputStream inFromServer = client.getInputStream();
         // DataInputStream in =
         //                new DataInputStream(inFromServer);
         // System.out.println("Server says " + in.readUTF());
         in.close();
         out.close();
         client.close();
      }catch(IOException e)
      {
         e.printStackTrace();
      }
   }
}