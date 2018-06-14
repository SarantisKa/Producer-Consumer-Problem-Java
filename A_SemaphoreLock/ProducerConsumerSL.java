import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumerSL
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
		private int[] contents = new int[bufferSize];
		private int front = 0, back = bufferSize - 1, counter = 0;
		private Semaphore bufferEmpty = new Semaphore(bufferSize);
		private Semaphore bufferFull = new Semaphore(0);;
		private ReentrantLock lock = new ReentrantLock();
		private ReentrantLock lock1 = new ReentrantLock();
		//private int[] contents;
		//private boolean bufferEmpty = true;
		//private boolean bufferFull = false;
		//private final int size;
		//private int front, back, counter = 0;
		// Constructor
		
		public void put(int data) {
			try {
			bufferEmpty.acquire();
			}
			catch (InterruptedException e) {}
			lock.lock();
//			while (bufferFull) {
//				try {
//				wait();
//				} catch (InterruptedException e) {}
//			}
			back = (back + 1) % bufferSize;
			contents[back] = data;
//			counter++;
			System.out.println("Item " + data + " added in loc " + back);
//			bufferEmpty = false;
			if (counter==bufferSize){
			//bufferFull = true; 
			System.out.println("The buffer is full");
			}
			lock.unlock();
			bufferFull.release();
//			notifyAll();
		}
		public int get() {
			int data;
			try {
			bufferFull.acquire();
			}
			catch (InterruptedException e) {}
			lock1.lock();
			//bufferFull.acquire();
//			while (bufferEmpty) {
//				try {
//				wait();
//				}
//				catch (InterruptedException e) {}
//			}
			data = contents[front];
			System.out.println("Item " + data + " removed from loc " + front);
			front = (front + 1) % bufferSize;
//			counter--;
			//bufferFull = false;
			if (counter==0){
//			bufferEmpty = true; 
//			System.out.println("The buffer is empty");
			}
			bufferEmpty.release();
			lock1.unlock();
//			notifyAll();
			return data;
		}
	}
}