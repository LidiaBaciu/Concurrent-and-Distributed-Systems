package extra4;

public class MyOwnCyclicBarrier {

	private int parties;
	private int partiesAwaiting;
	private CyclicBarrierEvent cyclicBarrierEvent;

	/**
	 * @param parties how many threads should reach the barrier
	 */
	public MyOwnCyclicBarrier(int parties) {
		this.parties = parties;
		partiesAwaiting = parties;
		cyclicBarrierEvent = new CyclicBarrierEvent();
	}

	/**
	 * @throws InterruptedException
	 */
	public synchronized void await() throws InterruptedException {

		partiesAwaiting--;

		if(partiesAwaiting>0){
			this.wait();
		}
		else{
			partiesAwaiting=parties;
			//we start again by resetting the number of parties awaiting
			notifyAll(); 
			//notify all the waiting threads
			cyclicBarrierEvent.run(); 
			//print my message
		}
	}
}
