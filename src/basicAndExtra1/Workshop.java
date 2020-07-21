package basicAndExtra1;

import java.util.ArrayList;
import java.util.Random;

public class Workshop {

	private int numberOfFactories;
	private Reindeer[] reindeers;
	private Random rand;
	private int maxElves;
	private Factory[] factories;
	public int numberOfReindeers;
	private ArrayList<String>[] factoryList;
	private LockBasedQueue<String> santaReindeersList;
	private int howManyElvesToRetire;

	/**
	 * @param santaReindeersList2 the list connecting the reindeers and santa 
	 * @throws InterruptedException
	 */
	@SuppressWarnings("unchecked")
	public Workshop(LockBasedQueue<String> santaReindeersList2) throws InterruptedException {

		this.santaReindeersList = santaReindeersList2;
		rand = new Random();
		//Reindeers are known from the beginning (there are more than 8).
		numberOfReindeers = 9;
		reindeers = new Reindeer[numberOfReindeers];

		//minimum 2 factories, maximum 5
		numberOfFactories = rand.nextInt(4) + 2;
		System.out.println(numberOfFactories + " factories");

		factories = new Factory[numberOfFactories];
		factoryList = new ArrayList[numberOfFactories];

		createFactories();

		ElfSpawner spawnElf = new ElfSpawner(numberOfFactories, factories, maxElves);
		spawnElf.start();
		spawnElf.join();
		//the spawning thread is to be awaited.
		
		howManyElvesToRetire = rand.nextInt(maxElves);
		ElfRetirer elfRetirer = new ElfRetirer(numberOfFactories, factories, howManyElvesToRetire);
		elfRetirer.start();
	
		createReindeers();
	}

	
	/**
	 * creates the factories
	 */
	private void createFactories() {
		int sumOfDimensions = 0;
		for(int i = 0 ; i < numberOfFactories; i++) { 
			//All factories are in matrix like form (random NxN, where N is between 100-500)
			int dimensionOfFactory = rand.nextInt(401)+100;
			factoryList[i] = new ArrayList<String>();
			factories[i] = new Factory(this, i, dimensionOfFactory, factoryList[i]);

			sumOfDimensions += dimensionOfFactory;
			System.out.println("Factory " + i + " has the dim " + dimensionOfFactory);
		}
		maxElves = sumOfDimensions/2;
	}

	/**
	 * create the reindeers 
	 */
	public void createReindeers() {
		//Reindeers are available in the workshop
		for(int i = 0 ; i < numberOfReindeers; i++) {
			reindeers[i] = new Reindeer(i, numberOfFactories, factories ,santaReindeersList);
			reindeers[i].start();
		}
		
	}
}



