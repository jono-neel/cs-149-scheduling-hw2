

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

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		ChildProcess[] c = new ChildProcess[5];
		Thread[] t = new Thread[5];
		for(int i = 0; i < 5; i++)
		{
			c[i] = new ChildProcess(i,startTime);
			t[i] = new Thread(c[i]);
		}
		
		for(int i = 0; i < 5; i++)
		{
			t[i].start();
		}
		// TODO Auto-generated method stub

	}

}
