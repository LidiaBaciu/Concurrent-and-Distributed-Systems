package basicAndExtra1;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;
public class Factory{

	private int factoryID;
	public int dimensionOfFactory;
	public int[][] matrix;
	public ArrayList<Elf> elves;
	public ArrayList<String>[] giftsToReindeers;
	private ArrayList<String> factoryList;
	public int reindeersCount = 0; 							
	private Semaphore semElf = new Semaphore(1);  
	private Semaphore listSemaphore = new Semaphore(1);     
	private Semaphore semReindeer = new Semaphore(1);
	
	
	/**
	 * @param workshop the workshop
	 * @param factoryID which is the id of the factory
	 * @param dimensionOfFactory representing its dimension
	 * @param factoryList the arraylist with the gifts of each factory
	 */
	@SuppressWarnings("unchecked")
	public Factory( Workshop workshop, int factoryID, int dimensionOfFactory, ArrayList<String> factoryList) {
		this.factoryList = factoryList;
		giftsToReindeers = new ArrayList[workshop.numberOfReindeers];
		for(int i = 0 ; i< workshop.numberOfReindeers; i++) {
			giftsToReindeers[i] = new ArrayList<String>();
		}
		this.factoryID = factoryID;
		this.dimensionOfFactory = dimensionOfFactory;
		matrix = new int[dimensionOfFactory][dimensionOfFactory];
		elves = new ArrayList<Elf>();
		
	}
	

	/**
	 * @param id of the reindeer
	 */
	public void acquireReindeersLock(int id) {
		try
		{
			semReindeer.acquire();
			reindeersCount++;
			// if I am the first reindeer tell all others that the book is being read
			if (reindeersCount == 1){
				try{
					listSemaphore.acquire();
					if(factoryList.size() != 0) {
						giftsToReindeers[id].add(factoryList.get(0));
						factoryList.remove(0);
					}
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			semReindeer.release();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param id of the reindeer
	 */
	public void releaseReindeersLock(int id) {
		try{
			semReindeer.acquire();
			reindeersCount--;
			// if I am the last reader tell all others that the book is no longer being read
			if (reindeersCount == 0){
				listSemaphore.release();
			}
			semReindeer.release();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * @param elfID represents the elf's id
	 * @param gift which gift was created
	 */
	public void acquireElvesLock(int elfID, String gift) {
		try{
			listSemaphore.acquire();
			factoryList.add(gift);
			getUpdates();
		}
		catch (InterruptedException e) {}
	}

	/**
	 * release the locks of the list and of the elf
	 */
	public void releaseElvesLock() {
		listSemaphore.release();
		semElf.release();
	}
	
	/**
	 * @param elf which needs to be registered
	 */
	public void registerElf(Elf elf) {
		try {
			semElf.acquire();
			elves.add(elf);
			System.out.println("factory no."+factoryID +"has elf"+elves.toString());
			semElf.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	/**
	 * asks the positions of each elf registered
	 */
	public void getUpdates() 
	{
		try {
			semElf.acquire();
			for (Elf elf : elves) {
				matrix[elf.getPositions().get(0)][elf.getPositions().get(1)] = 1;  //it's now occupied...
				matrix[elf.getPositions().get(2)][elf.getPositions().get(3)] = 0;  //it's now free...
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			releaseElvesLock();
		}
		//TODO: send the locations to Santa
	}

	/**
	 * @param elf which needs to be retired
	 * its run method will be stopped 
	 */
	public void retire(Elf elf) {
		if(semElf.tryAcquire()) {
			int id = elf.elfID;
			elf.retireElf();
			elves.remove(elf);
			System.out.println("the elf no."+id+" created " + elf.numberOfGifts + "gifts before it was retired.");
			System.out.println("factory no."+factoryID +"has retired the elf no."+id+"\nnow it has:"+elves.toString());
			semElf.release();
		}
	}

	/**
	 * prints the number of the gifts
	 * @throws InterruptedException
	 */
	public void printNumberOfGifts() throws InterruptedException {
		semElf.acquire();
		if(elves.size() == 0) {
			System.out.println("all elves from the factory no."+factoryID+" have retired. sorry");
		}else {
			for (Elf elf : elves) {
				System.out.println("the elf no."+elf.elfID + "has created " + elf.numberOfGifts + " gifts");
			}
		}
		semElf.release();
	}
}
