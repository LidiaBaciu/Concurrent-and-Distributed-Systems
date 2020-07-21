package basicAndExtra1;

import java.util.Random;

public class ElfRetirer extends Thread {

	private Elf elf;
	private Factory factory;
	private Random rand = new Random();
	private int numberOfFactories;
	private Factory[] factories;
	private int howManyElvesToRetire;

	/**
	 * @param numberOfFactories represents the random generated number of factories
	 * @param factories the actual list of factories
	 * @param howManyElvesToRetire a random generated number which represents how many elves should be retired
	 */
	public ElfRetirer(int numberOfFactories, Factory[] factories, int howManyElvesToRetire) {
		this.numberOfFactories = numberOfFactories;
		this.factories = factories;
		this.howManyElvesToRetire = howManyElvesToRetire;
	}

	@SuppressWarnings("static-access")
	public void run() {
		for(int i = 0; i < howManyElvesToRetire; i++) {
			int randomFactory = rand.nextInt(numberOfFactories);
			if(factories[randomFactory].elves.size() > 0) {
				factory = factories[randomFactory];
				int randomElfid = rand.nextInt(factory.elves.size()) ;
				elf = factory.elves.get(randomElfid);
				System.out.println("elf no."+elf.elfID + " has retired");
				factory.retire(elf);
				try {
					this.sleep(5000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				};

			}
		}
	}
}