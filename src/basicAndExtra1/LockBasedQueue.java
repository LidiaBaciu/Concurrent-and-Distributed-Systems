package basicAndExtra1;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockBasedQueue <T> {

	private Lock lock = new ReentrantLock();
	private Condition condition = lock.newCondition();

	volatile int head;
	volatile int tail;
	private T[] listOfElements;

	/**
	 * @param maxLenght represents the capacity of the list
	 */
	@SuppressWarnings("unchecked")
	public LockBasedQueue(int maxLenght) {
		head = 0;
		tail = 0;
		listOfElements = (T[]) new Object [maxLenght];
	}

	/**
	 * @return a String from the queue
	 * @throws InterruptedException 
	 */
	public T deq() throws InterruptedException {
		T x = null;
		lock.lock();
		try {
			if( tail == head) {
				condition.await(); 
			}
			x = listOfElements[head % listOfElements.length];
			head++;
//			System.out.println("Santa: " + head);
			condition.signal();
			return x;
		}finally {
			lock.unlock();
		}
	}

	/**
	 * @param x string to be added in the queue
	 * @throws InterruptedException 
	 */
	public void enq(T x) throws InterruptedException {
		lock.lock();
		try {
			if(tail - head == listOfElements.length) {
				condition.await();
			}
			listOfElements[head % listOfElements.length] = x;
			tail++;
//			System.out.println("Reindeer: " + tail);
			condition.signal();
		}finally {
			lock.unlock();
		}
	}
}
