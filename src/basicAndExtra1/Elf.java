package basicAndExtra1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class Elf extends Thread {

	public int elfID;
	private Factory factory;
	private int xCurrentPosition;
	private int yCurrentPosition;
	private int xOldPosition;
	private int yOldPosition;
	private AtomicBoolean elfStop;
	private Random rand;
	public int numberOfGifts;
	private boolean ranIntoElf;
	private ArrayList<String> whichGifts;
	private int numberSurroundingElves;


	/**
	 * @param factory the factory where the elf belongs
	 * @param xPosition its starting x-coordinate 
	 * @param yPosition its starting y-coordinate
	 * @param elfID elf's id
	 */
	public Elf(Factory factory, int xPosition, int yPosition, int elfID) {
		this.factory = factory;
		this.elfID = elfID;
		xCurrentPosition = xPosition;
		yCurrentPosition = yPosition;
		numberOfGifts = 0;
		elfStop = new AtomicBoolean(false);
		ranIntoElf = false;
		whichGifts = new ArrayList<String>( Arrays.asList("car", "doll","happiness","books")); 
		rand = new Random();
		numberSurroundingElves = 0;
	}


	/**
	 * @return an ArrayList containing both current and old positions
	 */
	public ArrayList<Integer> getPositions(){
		ArrayList<Integer> positions = new ArrayList<Integer>();
		positions.add(0, xCurrentPosition);
		positions.add(1, yCurrentPosition);
		positions.add(2, xOldPosition);
		positions.add(3, yOldPosition);
		return positions;
	}

	/**
	 * register the elf. it is added to the ArrayList of elves from its factory
	 */
	public void register() {
		factory.registerElf(this);
	}

	/**
	 * retire the elf. it is removed from the ArrayList of elves AND its run method stops
	 */
	public void retireElf() {
		elfStop.set(true);
	}

	/**
	 * the elf tries to move until it succeeds 
	 * when it moves, it also creates a gift and sends it to the factory
	 * @throws InterruptedException
	 */
	@SuppressWarnings("static-access")
	public void move() throws InterruptedException {
		ArrayList<String> optionsToMove = new ArrayList<String>( Arrays.asList("Up", "Down","Left","Right")); 

		boolean moved = false;

		int whereToMove = rand.nextInt(4);
		String next = optionsToMove.get(whereToMove);
		if(next.equalsIgnoreCase("Left")) {
			moved = moveLeft();
		}else if(next.equalsIgnoreCase("Right")) {
			moved = moveRight();
		}else if(next.equalsIgnoreCase("Down")) {
			moved = moveDown();
		}else if(next.equalsIgnoreCase("Up")) {
			moved = moveUp();
		}

		if(moved == false){
			if(ranIntoElf == true) {
				numberSurroundingElves++;
				if(numberSurroundingElves == 4) {
					this.sleep(rand.nextInt(41) + 10);
				}
			}else {
				while(moved == false) {
					moved = doNotHitWall(whereToMove);
				}
			}
		}
		if(moved == true) {
			numberSurroundingElves = 0;
			String whichGift = whichGifts.get(rand.nextInt(whichGifts.size()-1) + 1);
			//here i have to check again if the position is still free...
			factory.acquireElvesLock(elfID, whichGift);
			numberOfGifts++;
		}

	}

	/**
	 * @param wallDirection represents where the wall is. the new direction cannot be equal to the wall's
	 * @return true if the move succeded, false otherwise
	 * @throws InterruptedException
	 */
	private boolean doNotHitWall(int wallDirection) throws InterruptedException {

		boolean moved = false;
		int whereToMove = rand.nextInt(4) + 1;
		while(whereToMove == wallDirection) {
			whereToMove = rand.nextInt(4) + 1;
		}
		if(whereToMove == 1) {
			moved = moveLeft();
		}else if(whereToMove == 2) {
			moved = moveRight();
		}else if(whereToMove == 3) {
			moved = moveDown();
		}else if(whereToMove == 4) {
			moved = moveUp();
		}
		return moved;

	}

	/**
	 * @return true if the move succeded
	 * @throws InterruptedException
	 */
	@SuppressWarnings("static-access")
	private boolean moveLeft() throws InterruptedException {
		if(xCurrentPosition - 1 >= 0 && factory.matrix[xCurrentPosition-1][yCurrentPosition] == 0){
			xOldPosition = xCurrentPosition;
			yOldPosition = yCurrentPosition;
			xCurrentPosition--;
			this.sleep(30);
			return true;
		}else if(xCurrentPosition - 1 >= 0 && factory.matrix[xCurrentPosition-1][yCurrentPosition] == 1) {
			ranIntoElf = true;
		}
		return false;
	}

	/**
	 * @return true if the move succeded
	 * @throws InterruptedException
	 */
	@SuppressWarnings("static-access")
	private boolean moveRight() throws InterruptedException {
		if(xCurrentPosition + 1 < factory.dimensionOfFactory && factory.matrix[xCurrentPosition+1][yCurrentPosition] == 0) {
			xOldPosition = xCurrentPosition;
			yOldPosition = yCurrentPosition;
			xCurrentPosition++;
			this.sleep(30);
			return true;
		}else if(xCurrentPosition + 1 < factory.dimensionOfFactory && factory.matrix[xCurrentPosition+1][yCurrentPosition] == 1) {
			ranIntoElf = true;
		}
		return false;
	}

	/**
	 * @return true if the move succeded
	 * @throws InterruptedException
	 */
	@SuppressWarnings("static-access")
	private boolean moveUp() throws InterruptedException {
		if(yCurrentPosition + 1 < factory.dimensionOfFactory && factory.matrix[xCurrentPosition][yCurrentPosition+1] == 0) {
			yOldPosition = yCurrentPosition;
			xOldPosition = xCurrentPosition;
			yCurrentPosition++;
			this.sleep(30);
			return true;
		}else if(yCurrentPosition + 1 < factory.dimensionOfFactory && factory.matrix[xCurrentPosition][yCurrentPosition+1] == 1) {
			ranIntoElf = true;
		}
		return false;
	}

	/**
	 * @return true if the move succeded
	 * @throws InterruptedException
	 */
	@SuppressWarnings("static-access")
	private boolean moveDown() throws InterruptedException {
		if(yCurrentPosition - 1 >= 0 && factory.matrix[xCurrentPosition][yCurrentPosition-1] == 0){
			yOldPosition = yCurrentPosition;
			xOldPosition = xCurrentPosition;
			yCurrentPosition--;
			this.sleep(30);
			return true;
		}else if(yCurrentPosition - 1 >= 0 && factory.matrix[xCurrentPosition][yCurrentPosition-1] == 1) {
			ranIntoElf = true;
		}
		return false;
	}

	public void run() {
		try {
			register();
			while(elfStop.get() == false) 
//			for(int i = 0 ; i < 1500; i++)
			{
				move();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("the elf no. " + elfID + " created " + numberOfGifts + " gifts.");
	}

	@Override
	public String toString() {
		return " id: " + elfID;
	}

}
