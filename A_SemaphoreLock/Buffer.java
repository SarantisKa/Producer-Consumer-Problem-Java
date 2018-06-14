import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer
{
	private Semaphore bufferEmpty;
	private Semaphore bufferFull;
	private ReentrantLock lock = new ReentrantLock();
	private ReentrantLock lock1 = new ReentrantLock();
	private int[] contents;
	//private boolean bufferEmpty = true;
	//private boolean bufferFull = false;
	private final int size;
	private int front, back, counter = 0;
	// Constructor
	public Buffer(int s) {
		this.size = s;
		contents = new int[size];
		for (int i=0; i<size; i++)
		contents[i] = 0;
		this.front = 0;
		this.back = size - 1;
		this.bufferEmpty = new Semaphore(s);
		this.bufferFull = new Semaphore(0);
	}
	public void put(int data) {
		try {
		bufferEmpty.acquire();
		}
		catch (InterruptedException e) {}
		lock.lock();
//		while (bufferFull) {
//			try {
//			wait();
//			} catch (InterruptedException e) {}
//		}
		back = (back + 1) % size;
		contents[back] = data;
		counter++;
		System.out.println("Item " + data + " added in loc " + back + ". Count = " + counter);
//		bufferEmpty = false;
		if (counter==size){
		//bufferFull = true; 
		System.out.println("The buffer is full");
		}
		lock.unlock();
		bufferFull.release();
//		notifyAll();
	}
	public int get() {
		int data;
		try {
		bufferFull.acquire();
		}
		catch (InterruptedException e) {}
		lock1.lock();
		//bufferFull.acquire();
//		while (bufferEmpty) {
//			try {
//			wait();
//			}
//			catch (InterruptedException e) {}
//		}
		data = contents[front];
		System.out.println("Item " + data + " removed from loc " + front + ". Count = " + (counter-1));
		front = (front + 1) % size;
		counter--;
		//bufferFull = false;
		if (counter==0){
//		bufferEmpty = true; 
		System.out.println("The buffer is empty");
		}
		bufferEmpty.release();
		lock1.unlock();
//		notifyAll();
		return data;
	}
}
