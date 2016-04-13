import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;


public class ChildProcess implements Runnable
{
	long startTime, currentTime;
	final long END_TIME = 30;

	Pipe pipe; 
	Pipe.SinkChannel sinkChannel;
	Selector selector;
	SelectionKey key;
	int readyChannels;

	ByteBuffer buf;
	int childName;
	int messageNumber;

	public ChildProcess(int name, long startTime)
	{ 
		this.startTime = startTime;
		messageNumber = 0;
		childName = name;
		buf = ByteBuffer.allocate(48);
		try 
		{
			pipe = Pipe.open();
			sinkChannel = pipe.sink();
			selector = Selector.open();
			sinkChannel.configureBlocking(false);
			key = sinkChannel.register(selector, SelectionKey.OP_WRITE);
			

		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	@Override
	public void run() 
	{	

		String message;
		while(true)
		{
			long restTime = (long) ((Math.random() % 3)*1000);
			try {
				Thread.sleep(restTime);
			} catch (InterruptedException e2) {
				e2.printStackTrace();
			}
			try {
				readyChannels = selector.select();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}


			if(readyChannels == 0) continue;


			Set<SelectionKey> selectedKeys = selector.selectedKeys();

			Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

			while(keyIterator.hasNext()) 
			{

				SelectionKey key = keyIterator.next();

				if(key.isAcceptable()) 
				{
					// a connection was accepted by a ServerSocketChannel.
				} 
				else if (key.isConnectable()) 
				{
					// a connection was established with a remote server.
				} 
				else if (key.isReadable()) 
				{
					// a channel is ready for reading
				}
				else if (key.isWritable()) 
				{
					double t = (double) ((System.currentTimeMillis() - startTime) / 1000.0);
					String time = String.format("%06.3f", t);
					if(childName == 4)
					{
						messageNumber++;
						message = "0:" + time + ": Child " + (childName + 1) + 
								" : UserMessage " + messageNumber;
						System.out.println(message);
					}
					else
					{
						messageNumber++;
						message = "0:" + time + ": Child " + (childName + 1) + 
								" : Message " + messageNumber;
						System.out.println(message);
					}

					buf.clear();
					buf.put(message.getBytes());

					buf.flip();

					while(buf.hasRemaining()) 
					{
						try {

							sinkChannel.write(buf);					
						} catch (IOException e) 
						{
							e.printStackTrace();
						}
					}
				}
				keyIterator.remove();
			}
		}
	}
}
