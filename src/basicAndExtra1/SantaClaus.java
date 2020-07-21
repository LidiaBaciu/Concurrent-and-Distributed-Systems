package basicAndExtra1;

import java.util.LinkedList;

public class SantaClaus extends Thread{

	private LockBasedQueue<String> santaReindeersList;
	private LinkedList<String> santasList;
	
	/**
	 * @param santaReindeersList the list connecting the reindeers and santa 
	 */
	public SantaClaus(LockBasedQueue<String> santaReindeersList) {
		this.santaReindeersList = santaReindeersList;
		santasList = new LinkedList<String>();
	}
	
	public void run() {
		//this i helps me to print the message
		int i = 0;
		while(true) {
			i++;
			try {
				santasList.add(santaReindeersList.deq());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(i == 10000) {
				System.out.println("Santa has received: " + santasList.size());
				i = 0;
			}
		}
	}
	
}
