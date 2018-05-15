import java.net.*;
import java.io.*;
import java.util.*;

public class ChatServer
{
  /**Main method listens for a client connection on port
  * 3456. Runs continuously.
  **/
  public static void main(String[] args) throws Exception
  {
    ServerSocket server = new ServerSocket(3456);    
    TreeMap<String, Socket> clientList = new TreeMap<String, Socket>();
    ChatServer web = new ChatServer();
    
    while(true)
    {
      Socket client = server.accept();       
      web.new ClientThreadIN(client,clientList);        
              
      System.out.println((client.getLocalPort())+" is connected");
    }
    
  }

/** The ClientThreadIN thread takes the client socket object
    and the TreeMap of user to socket. Each thread sends and
    reads input from its client.
*/
  class ClientThreadIN extends Thread
  {
    Socket clientIN;
    ChatServer web;
    TreeMap<String,Socket> clientList;
    Scanner fromClient;
    PrintWriter toClient;
    
    /*Constructor-- Sets up the input and output streams for the
      client socket that this thread handles.
      @param c The client Socket object.
      @param t The TreeMap of Users and their sockets.
    */
    public ClientThreadIN(Socket c,TreeMap<String,Socket> t) throws Exception
    {
      clientIN = c;    
      clientList = t;      
      fromClient = new Scanner(clientIN.getInputStream());
      toClient   = new PrintWriter(clientIN.getOutputStream(), true);
      start();
    }
    
    /*Run method executes while thread is alive. The method
      first prompts the user for a name to use for this chat
      session. Then the user name and socket is put into the
      map. Next, The server then sends a list of the 
      connected users as well as some instructions
      concerning its protocol to the client. After that initial
      setup, the thread listens for input from the client and 
      directs the input to the correct client output stream by 
      retrieving the appropriate socket from the map.
    */
    public void run()
    {
      String s = "";
      String s2 = "";
          
      toClient.println("Enter user name: ");
      s = fromClient.nextLine();
      clientList.put(s,clientIN);
      toClient.println("Welcome ");
      toClient.println(s);
      toClient.println("Connected users:");
      Set users = clientList.keySet();
      for(Iterator i = users.iterator(); i.hasNext(); )
         toClient.println(i.next());
      toClient.println("Type: User_Name/Message to send message.");
      toClient.println("Type: "+s+"/DONE  to exit.");
      toClient.println("$");
      
      PrintWriter toPeer = null;
      
      while(true)
      {
        if(fromClient.hasNextLine())
        {
          s = fromClient.nextLine();
          StringTokenizer message = new StringTokenizer(s,"/");
          String to = message.nextToken();
          Socket destination = clientList.get(to);
          try{
            toPeer = new PrintWriter(destination.getOutputStream(), true);
          } catch(Exception e){toClient.println(to+" not available.");} 
        
          String text = message.nextToken();
          String from = message.nextToken();
        
          if(text.equals("DONE"))
          {
            clientList.remove(to);
            toPeer.println("Goodbye "+from);
            toPeer.println(from+":DONE");
            fromClient.close();
            toClient.close();
            try{
              clientIN.close();
            } catch (Exception e) {}
          }
          else
          {             
            toPeer.println(from+":"+text);
          }          
        }
      }
    }
  }
}
