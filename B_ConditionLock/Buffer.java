import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer
{
	private int[] contents;
	private final int size;
	private int front, back, counter = 0;
    private ReentrantLock lock = new ReentrantLock();
    private Condition bufferEmpty = lock.newCondition();
    private Condition bufferFull = lock.newCondition();
	// Constructor
        public Buffer(int s) {
		this.size = s;
                contents = new int[size];
		for (int i=0; i<size; i++)
			contents[i] = 0;
                this.front = 0;
                this.back = size-1;
     	}

	// Put an item into buffer
	public void put(int data){
		lock.lock();
		  while (counter == size) {
			      try {
			    	bufferFull.await();  
			      }catch (InterruptedException e) {}
		  }
			back = (back + 1) % size;
			contents[back] = data;
            counter++;
			System.out.println("Item " + data + " added in loc " + back + ". Count = " + counter);
			bufferEmpty.signal(); // or signal_all(bufferEmpty);
			lock.unlock();
        	if (counter==size)
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
            front = (front + 1) % size;
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

	
			
	
