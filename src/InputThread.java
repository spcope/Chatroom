/**
 * @author Sean P. Cope
 */
public class InputThread extends Thread
{
    ClientSide chat;
    String input;
    
    public InputThread(ClientSide c, String input)
    {     
      chat = c;
      this.input = input;
    }

    public void run()
    {
        input = chat.readServer();
        if(input != null)
        {
          if(input.equals(chat.getName()+":DONE")) 
          {
            chat.done();
            return;
          }
          else
            System.out.println(input);
        }
        else
        {
          yield();
        }
    }
}