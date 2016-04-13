
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;



/**
 * TODO: create parent process to read from source channels
 * TODO: write to output file
 * TODO: make child 5 read from user input
 * TODO: not 100% sure blocking for read/write via select is working
 * TODO: make parent append time to message
 * TODO: terminate parent process/program after all child process have finished
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
            Pipe[] pipes = new Pipe[5];
            Selector selector;
            //SelectionKey[] key = new SelectionKey[5];
            int readyChannels;

            ByteBuffer buf = ByteBuffer.allocate(48);
            selector = Selector.open();
            
            for(int i = 0; i < 5; i++)
            {
                pipes[i] = Pipe.open();
                pipes[i].sink().configureBlocking(false);
                //key[i] = 
                pipes[i].sink().register(selector, SelectionKey.OP_WRITE);
                
                c[i] = new ChildProcess(i,startTime, pipes[i]);
                t[i] = new Thread(c[i]);
            }
            
            for(int i = 0; i < 5; i++)
            {
                    t[i].start();
            }
		// TODO Auto-generated method stub
                
            Random rand =  new Random();
            boolean stopRunning = true;
            while(stopRunning)
            {
                for(int k = 0; k < 5; k++)
                {
                    if(!t[k].isAlive())
                    {
                        stopRunning = !stopRunning;
                        break;
                    }
                }
                int nBytes;
                
                readyChannels = selector.select();

                if(readyChannels == 0)
                {
                    System.out.println("No channels are ready");
                    continue;   
                }

                Set<SelectionKey> selectedKeys = selector.selectedKeys();

                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                while(keyIterator.hasNext()) 
                {
                    
                    SelectionKey keyy = keyIterator.next();
                    if(keyy.isAcceptable()) {
                        // a connection was accepted by a ServerSocketChannel.

                    } else if (keyy.isConnectable()) {
                        // a connection was established with a remote server.

                    } else if (keyy.isReadable()) {
                        // a channel is ready for reading

                    } else if (keyy.isWritable()) 
                    {
                        String message ="Parent Message: ";
                        int rNum = rand.nextInt() % 10;
                        Math.abs(rNum);
                        for(;rNum-- > 0;)
                        {
                            message += ".";
                        }
                        //buf.put(message.getBytes());
                        nBytes = ((Pipe.SinkChannel)keyy.channel()).write(ByteBuffer.wrap(message.getBytes()));

                        System.out.println("Write: " + nBytes + " to pipe: " + message);
                        Thread.sleep(500);
                    }

                    buf.clear();

                  keyIterator.remove();
            }
                
            
	}

    }
}
