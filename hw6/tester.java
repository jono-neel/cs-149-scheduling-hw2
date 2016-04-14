
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;



/**
 * Parent process that creates 5 child processes connected via pipes. Parent uses
 * select to make sure pipes are ready to be read from and then reads messages
 * from pipes byte by byte and writes the messages straight into the file "output.txt"
 * 
 * used tutorials at:
 * http://tutorials.jenkov.com/java-nio/selectors.html#selectedkeys
 * http://tutorials.jenkov.com/java-nio/selectors.html
 * http://tutorials.jenkov.com/java-nio/pipe.html
 * http://tutorials.jenkov.com/java-nio/buffers.html
 */

public class tester {

	public static void main(String[] args) throws IOException, InterruptedException {
		long startTime = System.currentTimeMillis();
		ChildProcess[] c = new ChildProcess[5];
		Thread[] t = new Thread[5];                
		Pipe[] pipes = new Pipe[5]; //one pipe per child
		Selector selector;
		int readyChannels;
		ByteBuffer buf = ByteBuffer.allocate(128);
		selector = Selector.open();
		PrintWriter writer = new PrintWriter("output.txt", "UTF-8");


		for(int i = 0; i < 5; i++)
		{
			pipes[i] = Pipe.open();
			pipes[i].source().configureBlocking(false);
			pipes[i].source().register(selector, SelectionKey.OP_READ);
			c[i] = new ChildProcess(i,startTime, pipes[i]);
			t[i] = new Thread(c[i]);
		}

		for(int i = 0; i < 5; i++)
		{
			t[i].start(); //start the child processes
		}

		Random rand =  new Random();
		boolean stopRunning = true;
		while(stopRunning) //stop the parent process if any of the children have finished
		{
			for(int k = 0; k < 5; k++)
			{
				if(!t[k].isAlive())
				{
					stopRunning = !stopRunning;
					break;
				}
			}

			readyChannels = selector.select();
			if(readyChannels == 0)
			{
				System.out.println("No channels are ready");
				continue;   
			}
			Set<SelectionKey> selectedKeys = selector.selectedKeys();
			Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
			while(keyIterator.hasNext()) //while pipes are ready to be read from
			{

				SelectionKey keyy = keyIterator.next();
				if(keyy.isAcceptable()) {
					// a connection was accepted by a ServerSocketChannel.

				} else if (keyy.isConnectable()) {
					// a connection was established with a remote server.

				} else if (keyy.isReadable()) 
				{
					double time = (double) ((System.currentTimeMillis() - startTime) / 1000.0);
					String timestamp = String.format("%06.3f", time);
					writer.println("Parent timestamp: " + timestamp); //write parent time stamp
					int bytesRead = ((Pipe.SourceChannel)keyy.channel()).read(buf); //read into buffer.
					buf.flip();  //make buffer ready for read

					while(buf.hasRemaining()){
						writer.print((char) buf.get()); // read 1 byte at a time and print it to the file
					}
					buf.clear(); //make buffer ready for writing
					bytesRead = ((Pipe.SourceChannel)keyy.channel()).read(buf);

				} 
				else if (keyy.isWritable()) 
				{
				}
				buf.clear(); //clear the buffer
				keyIterator.remove(); 
			} 
		}
		writer.close();
	}
