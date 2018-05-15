
import static java.lang.Thread.yield;

class OutputThread extends Thread
{
    ClientSide chat;
    String key;
    
    public OutputThread(ClientSide c, String key) 
    {
      chat = c;
      this.key = key;
    }
    
    public void run()
    { 
        key = chat.readKeyboard();
        if(key != null)
        {
          chat.sendServer(key+"/"+chat.getName());
        }
        else
        {
          yield();
        }  
    }
}