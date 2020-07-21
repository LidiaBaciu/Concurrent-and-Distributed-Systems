package extra4;

import java.util.Random;

public class ElfSpawner extends Thread {

	private Elf elf ;
	private Factory factory;
	private Random rand = new Random();
	private int xPosition;
	private int yPosition;
	private int numberOfFactories;
	private Factory[] factories;
	private MyOwnCyclicBarrier newBarrier;
	private int maxElves;


	/**
	 * @param numberOfFactories represents the number of factories
	 * @param factories the array of factories
	 * @param maxElves  the maximum number of elves that can be created
	 * @param newBarrier2 my own cyclic barrier
	 */
	public ElfSpawner(int numberOfFactories, Factory[] factories, MyOwnCyclicBarrier newBarrier2, int maxElves) {
		this.numberOfFactories = numberOfFactories;
		this.factories = factories;
		this.newBarrier = newBarrier2;
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
	 * @param factory the randomy assigned factory
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
