public class BufferMonitors {
     private int[] contents;
     private boolean bufferEmpty = true;
     private boolean bufferFull = false;
     private final int size;
     private int front, back, counter = 0;
// Constructor
  public BufferMonitors(int s) {
        this.size = s;
        contents = new int[size];
    for (int i=0; i<size; i++)
       contents[i] = 0;
       this.front = 0;
       this.back = size - 1;
    }

public synchronized void put(int data) {
     while (bufferFull) {
      try {
       wait();
      } catch (InterruptedException e) {}
      } back =(back +1) %size;
        contents[back] = data;
        counter++;
        System.out.println("Item " + data + " added in loc " + back + ". Count = " + counter);
        bufferEmpty = false;
      if (counter==size) bufferFull = true;
        notifyAll();
     }

public synchronized int get() {
  int data;
   while (bufferEmpty) {
   try {
     wait();
   } catch (
     InterruptedException e) {}
   }
        data = contents[front];
        System.out.println("Item " + data + " removed from loc " + front + ". Count = " + (counter-1));
        front = (front + 1) % size;
        counter--;
        bufferFull = false;
    if (counter==0) bufferEmpty = true;
   notifyAll();
     return data;
  }
}
	
