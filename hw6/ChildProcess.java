import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Child processes is connected to parent process via a single pipe.
 * Child processes write messages with time-stamps to their pipe.
 * 
 */
public class ChildProcess implements Runnable
{
	long startTime, currentTime;
	final long END_TIME = 30000; //stop after 30 seconds

	Pipe pipe; 

	ByteBuffer buf;
	int childName;
	int messageNumber;
	/**
	 * Each child process has a pipe which it uses to send messages to the parent process.
	 * Behavior of message depends on the child process (child 5 waits for user input)
	 * @param name the number of the process
	 * @param startTime the start time
	 * @param pipe the pipe corresponding to this child process
	 */
	public ChildProcess(int name, long startTime, Pipe pipe)
	{ 
		this.startTime = startTime;
		this.pipe = pipe;
		messageNumber = 0;
		childName = name;
		buf = ByteBuffer.allocate(128);
	}

	@Override
	public void run() 
	{	
		String message;
		while(System.currentTimeMillis() - startTime < END_TIME) //stop all processes after 30 secs
		{
			double t = (double) ((System.currentTimeMillis() - startTime) / 1000.0);
			String time = String.format("%06.3f", t); //the elapsed time
			if(childName == 4) //5th child waits for input from user
			{
				System.out.println("Enter a message: ");
				Scanner in = new Scanner(System.in);
				messageNumber++;
				message = "User message: ";
				message += in.nextLine();
				t = (double) ((System.currentTimeMillis() - startTime) / 1000.0);
				time = String.format("%06.3f", t); //child time stamp
				message += " @ 0:" + time + ": Child " + (childName + 1) + 
						": UserMessage " + messageNumber + "\n";
			}
			else
			{
				Random rando = new Random();
				long restTime = rando.nextInt(3) * 1000; 
				try {
					Thread.sleep(restTime); //sleep for random time between 0 - 2 seconds
				} catch (InterruptedException e2) {
					e2.printStackTrace();
				}

				messageNumber++;
				message = "0:" + time + ": Child " + (childName + 1) +
						" : Message " + messageNumber + "\n";

			}	
			try {
				buf.clear();
				buf.put(message.getBytes()); //put message in buffer
				buf.flip();
				while(buf.hasRemaining()) {
					pipe.sink().write(buf); //write to pipe
				}
				buf.clear(); //clear buffer
			} catch (IOException ex) {
				Logger.getLogger(ChildProcess.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}

