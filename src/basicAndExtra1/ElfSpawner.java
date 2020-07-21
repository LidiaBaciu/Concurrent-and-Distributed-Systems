package basicAndExtra1;

import java.util.Random;

public class ElfSpawner extends Thread {

	private Elf elf ;
	private Factory factory;
	private Random rand = new Random();
	private int xPosition;
	private int yPosition;
	private int numberOfFactories;
	private Factory[] factories;
	private  int maxElves;


	/**
	 * @param numberOfFactories represents the number of factories
	 * @param factories the array of factories
	 * @param maxElves  the maximum number of elves
	 */
	public ElfSpawner(int numberOfFactories, Factory[] factories, int maxElves) {
		this.numberOfFactories = numberOfFactories;
		this.factories = factories;
		this.maxElves = maxElves;
	}


	public void run() {
		for(int i = 0 ; i < maxElves; i++) { 
			//I am creating the maximum number possible of elves
			int randomFactory = rand.nextInt(numberOfFactories);
			//it's assigned a random number which represents the id of the factory
			while(factories[randomFactory].elves.size() > factories[randomFactory].dimensionOfFactory/2) {
				randomFactory = rand.nextInt(numberOfFactories);
			}
			//if that factory already has the maximum number of elves, then the number changes 
			factory = factories[randomFactory];
			
			//try to find a free position in the factory
			assignElf(factory);
			
			elf = new Elf(factory, xPosition, yPosition, i);
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
	 * @param factory with the random id number
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
