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
 * Child processes write messages with timeStamp-stamps to their pipe.
 *
 */
public class ChildProcess implements Runnable
{
	static final long END_TIME = 30000; //stop after 30 seconds
	long startTime;

	Pipe pipe;
	ByteBuffer buf;

	int childName;

	/**
	 * Each child process has a pipe which it uses to send messages to the parent process.
	 * Behavior of message depends on the child process (child 5 waits for user input)
	 * @param name the number of the process
	 * @param startTime the start timeStamp
	 * @param pipe the pipe corresponding to this child process
	 */
	public ChildProcess(int name, long startTime, Pipe pipe)
	{
		this.startTime = startTime;
		this.pipe = pipe;
		buf = ByteBuffer.allocate(128);
		childName = name;
	}

	@Override
	public void run()
	{
		String message;
		int messageNumber = 0;
		double curTime;
		String timeStamp;

		while(System.currentTimeMillis() - startTime < END_TIME) //stop all processes after 30 secs
		{
			if(childName == 4) //5th child waits for input from user
			{
				System.out.print("Enter a message: ");
				Scanner in = new Scanner(System.in);
				// format message with timestamp
				curTime = (double) ((System.currentTimeMillis() - startTime) / 1000.0);
				timeStamp = String.format("%06.3f", curTime);
				messageNumber++;
				message =  "0:" + timeStamp + ": Child " + (childName + 1) +
						" " + in.nextLine() + "\n";
			}
			else // other children generate automated message
			{
				//sleep for random time between 0 - 2 seconds
				Random rando = new Random();
				long restTime = rando.nextInt(3) * 1000;
				try {
					Thread.sleep(restTime);
				} catch (InterruptedException e2) {
					e2.printStackTrace();
				}
				// format message with timestamp
				curTime = (double) ((System.currentTimeMillis() - startTime) / 1000.0);
				timeStamp = String.format("%06.3f", curTime);
				messageNumber++;
				message = "0:" + timeStamp + ": Child " + (childName + 1) +
						" message " + messageNumber + "\n";

			}
			try { // write message to pipe
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
