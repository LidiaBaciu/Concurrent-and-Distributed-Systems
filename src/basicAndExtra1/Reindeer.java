package basicAndExtra1;

import java.util.LinkedList;
import java.util.Random;

public class Reindeer extends Thread{

	private Factory factory;
	private LinkedList<String> giftsReceived;
	private int id;
	private LockBasedQueue<String> santaReindeersList;
	private int numberOfFactories;
	private Factory[] factories;
	private Random rand;

	/**
	 * @param id of the reindeer
	 * @param numberOfFactories represents the number of the factories
	 * @param factories represents the array of factories created
	 * @param santaReindeersList the list connecting the reindeers and santa 
	 */
	public Reindeer(int id, int numberOfFactories, Factory[] factories, LockBasedQueue<String> santaReindeersList) {
		this.id = id;
		giftsReceived = new LinkedList<String>();
		this.santaReindeersList = santaReindeersList;
		this.numberOfFactories = numberOfFactories;
		this.factories = factories;
		rand = new Random();
	}

	public void run() {
		//this i helps me to print the message
		int i = 0;
		while(true)
		{	
			i++;
			int randomFactory = rand.nextInt(numberOfFactories);
			while(factories[randomFactory].reindeersCount > 9) {
				randomFactory = rand.nextInt(numberOfFactories);
			}
			factory = factories[randomFactory];
			factory.acquireReindeersLock(id);
			try {
				if(factory.giftsToReindeers[id].size() != 0) {
					giftsReceived.addAll(factory.giftsToReindeers[id]);
					santaReindeersList.enq(giftsReceived.getFirst());
				}
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			finally {
				factory.releaseReindeersLock(id);
			}
			if(i == 10000) {
				System.out.println("the reindeer no. "+ id +" has received: " + giftsReceived.size());
				i = 0;
			}
		}
	}

	@Override
	public String toString() {
		return " id: " + id;
	}

}
