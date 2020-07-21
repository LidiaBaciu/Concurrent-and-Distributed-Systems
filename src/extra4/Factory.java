package extra4;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Factory
{
	private int factoryID;
	public int dimensionOfFactory;
	public int[][] matrix;
	public ArrayList<Elf> elves;
	public ArrayList<String>[] giftsToReindeers;
	private ArrayList<String> factoryList;				
	
	static Semaphore semElf = new Semaphore(1);  
	private Semaphore listSemaphore = new Semaphore(1);     
	static Semaphore semReindeer = new Semaphore(1);
	
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
	
	public void acquireElvesLock(int writerID, String gift) {
		try{
			semElf.acquire();
			listSemaphore.acquire();
			factoryList.add(gift);
			getUpdates();
		}
		catch (InterruptedException e) {}
	}

	public void releaseElvesLock() {
		listSemaphore.release();
		semElf.release();
	}
	
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


	public void getUpdates() 
	{
		//TODO: send the locations to Santa
		for (Elf elf : elves) {
			synchronized (matrix) {
				matrix[elf.getPositions().get(0)][elf.getPositions().get(1)] = 1;  //it's now occupied...
				matrix[elf.getPositions().get(2)][elf.getPositions().get(3)] = 0;  //it's now free...
			}
		}
		releaseElvesLock();
	}

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
