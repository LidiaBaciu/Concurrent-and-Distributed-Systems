package extra3;

import java.util.Random;
import java.util.concurrent.CyclicBarrier;

public class ElfSpawner extends Thread {

	private Elf elf ;
	private Factory factory;
	private Random rand = new Random();
	private int xPosition;
	private int yPosition;
	private int numberOfFactories;
	private Factory[] factories;
	private CyclicBarrier newBarrier;
	private int maxElves;

	/**
	 * @param numberOfFactories how many factories were already created
	 * @param factories the actual list of factories created
	 * @param newBarrier the cyclic barrier
	 * @param maxElves how many elves should be created
	 */
	public ElfSpawner(int numberOfFactories, Factory[] factories, CyclicBarrier newBarrier, int maxElves) {
		this.numberOfFactories = numberOfFactories;
		this.factories = factories;
		this.newBarrier = newBarrier;
		this.maxElves = maxElves;
	}


	public void run() {
		for(int i = 0 ; i < maxElves; i++) { 
			
			int randomFactory = rand.nextInt(numberOfFactories);
			
			while(factories[randomFactory].elves.size() > factories[randomFactory].dimensionOfFactory/2) {
				randomFactory = rand.nextInt(numberOfFactories);
			}
			factory = factories[randomFactory];
			
			assignElf(factory);
			
			elf = new Elf(factory, xPosition, yPosition, i, newBarrier);
			System.out.println("elf no."+ i +" coordinates: "+xPosition +","+yPosition);

			elf.start();
			
			try {
				ElfSpawner.sleep(rand.nextInt((500) + 1) + 500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * find out which coordinates are free and put the elf there
	 * @param factory the randomly assigned factory
	 */
	public void assignElf(Factory factory) {
		
		xPosition = rand.nextInt(factory.dimensionOfFactory-1) + 1;
		yPosition = rand.nextInt(factory.dimensionOfFactory-1) + 1;
		
		//if it's zero it means it's free
		while(factory.matrix[xPosition][yPosition] != 0) {
			xPosition = rand.nextInt(factory.dimensionOfFactory-1) + 1;
			yPosition = rand.nextInt(factory.dimensionOfFactory-1) + 1;
		}
		//when i'm here, it means i have found one spot free
	}
	

}
