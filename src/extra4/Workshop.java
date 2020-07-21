package extra4;

import java.util.ArrayList;
import java.util.Random;

public class Workshop {

	private int numberOfFactories;
	private Random rand;
	private static int maxElves;
	private Factory[] factories;
	public int numberOfReindeers;
	private ArrayList<String>[] factoryList;


	@SuppressWarnings("unchecked")
	public Workshop() throws InterruptedException {

		rand = new Random();
		
		//minimum 2 factories, maximum 5
		numberOfFactories = rand.nextInt(4) + 2;
//		numberOfFactories = 1;
		System.out.println(numberOfFactories + " factories");

		factories = new Factory[numberOfFactories];
		factoryList = new ArrayList[numberOfFactories];

		createFactories();

		MyOwnCyclicBarrier newBarrier = new MyOwnCyclicBarrier(maxElves);
		
		ElfSpawner spawnElf = new ElfSpawner(numberOfFactories, factories, newBarrier, maxElves);
		spawnElf.start();
		
		for (Factory factory : factories) {
			factory.printNumberOfGifts();
		}

	}


	private void createFactories() {
		int sumOfDimensions = 0;
		for(int i = 0 ; i < numberOfFactories; i++) { 
			//rand.nextInt((max - min) + 1) + min;
			int dimensionOfFactory = rand.nextInt(401)+100;
//			int dimensionOfFactory = 10;
			factoryList[i] = new ArrayList<String>();
			factories[i] = new Factory(this, i, dimensionOfFactory, factoryList[i]);

			sumOfDimensions += dimensionOfFactory;
			System.out.println("Factory " + i + " has the dim " + dimensionOfFactory);
		}
		maxElves = sumOfDimensions/2;
	}

}



