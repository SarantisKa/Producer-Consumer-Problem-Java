import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumerLC
{
	static int bufferSize = 10;
	static int noIterations = 20;
	static int producerDelay = 100;
	static int consumerDelay = 100;
        static int noProds = 1;
        static int noComs = 1;
        static Producer prod[] = new Producer[noProds];
	static Consumer cons[] = new Consumer[noProds];
	static Buffer buff = new Buffer();
		
	public static void main(String[] args)
	{
		// Producer threads
		for (int i=0; i<noProds; i++) {
			prod[i] = new Producer();
			prod[i].start();
		}

		// Consumer threads
                for (int j=0; j<noProds; j++) {
		        cons[j] = new Consumer();
			cons[j].start();
                }
	}
	
	static class Producer extends Thread 
	{
		// Producer runs for reps times with random(scale) intervals
		public void run() {
			for(int i = 0; i < noIterations; i++) {
				buff.put(i);
                        	try {
              				sleep((int)(Math.random()*producerDelay));
           			} catch (InterruptedException e) { }
                	}
		}
        }

	static class Consumer extends Thread 
	{
		// Consumer runs for ever ..
		public void run() {
			int value;
			while (true) {
				try {
              				sleep((int)(Math.random()*consumerDelay));
           			} catch (InterruptedException e) { }
				value = buff.get();
			}
		}
        }

	// Bounded Buffer
	static class Buffer
	{
		//private int[] contents;
		//private final int size;
		//private int front, back, counter = 0;
	    private ReentrantLock lock = new ReentrantLock();
	    private Condition bufferEmpty = lock.newCondition();
	    private Condition bufferFull = lock.newCondition();
		private int[] contents = new int[bufferSize];
		//private boolean bufferEmpty = true;
		//private boolean bufferFull = false;
		private int front = 0, back = bufferSize - 1, counter = 0;
		// Constructor

		// Put an item into buffer
		public void put(int data){
			lock.lock();
			  while (counter == bufferSize) {
				      try {
				    	bufferFull.await();  
				      }catch (InterruptedException e) {}
			  }
				back = (back + 1) % bufferSize;
				contents[back] = data;
	            counter++;
				System.out.println("Item " + data + " added in loc " + back + ". Count = " + counter);
				bufferEmpty.signal(); // or signal_all(bufferEmpty);
				lock.unlock();
	        	if (counter==bufferSize)
				{
					System.out.println("The buffer is full");

				}
			}

		// Get an item from bufffer
		public int get()
		{
			int data;
			lock.lock();
			while (counter == 0) {
			      try {
			    	bufferEmpty.await();  
			      }catch (InterruptedException e) {}
		  }
				data = contents[front];
				System.out.println("Item " + data + " removed from loc " + front + ". Count = " + (counter-1));
	            front = (front + 1) % bufferSize;
				counter--;
				bufferFull.signal(); // or signal_all(bufferEmpty)
				lock.unlock();
				if (counter==0) 
				{
					System.out.println("The buffer is empty");
				}
	        	return data;
		}
	}
}
