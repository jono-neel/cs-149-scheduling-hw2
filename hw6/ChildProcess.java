import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ChildProcess implements Runnable
{
	long startTime, currentTime;
	final long END_TIME = 3000;

	Pipe pipe; 

	ByteBuffer buf;
	int childName;
	int messageNumber;

	public ChildProcess(int name, long startTime, Pipe pipe)
	{ 
		this.startTime = startTime;
                this.pipe = pipe;
		messageNumber = 0;
		childName = name;
		buf = ByteBuffer.allocate(48);
	}

	@Override
	public void run() 
	{	
		String message;
		while(System.currentTimeMillis() - startTime < END_TIME)
		{
                    System.out.println("..." +  this.childName);

                    long restTime = (long) ((Math.random() % 3)*1000);
                    try {
                            Thread.sleep(500);
                    } catch (InterruptedException e2) {
                            e2.printStackTrace();
                    }

                    double t = (double) ((System.currentTimeMillis() - startTime) / 1000.0);
                    String time = String.format("%06.3f", t);
                    if(childName == 4)
			{
				messageNumber++;
				message = "0:" + time + ": Child " + (childName + 1) + 
						" : UserMessage ";
				message += in.nextLine();
				//System.out.println(message);
			}
			else
			{
				messageNumber++;
				message = "0:" + time + ": Child " + (childName + 1) +
						" : Message " + messageNumber;
				//System.out.println(message);
			}
			try {
				int nBytes = pipe.sink().write(ByteBuffer.wrap(message.getBytes()));
				System.out.println("Write: " + nBytes + " to pipe: " + message);
				buf.clear();
			} catch (IOException ex) {
				Logger.getLogger(ChildProcess.class.getName()).log(Level.SEVERE, null, ex);
			}	
		}
	}
}
