package basicAndExtra1;

public class Main {
	
	public static void main(String[] args) throws InterruptedException {
		
		int capacity = 10000;
		
		LockBasedQueue<String> santaReindeersList = new LockBasedQueue<String>(capacity);
		
		new SantaClaus(santaReindeersList).start();
		
		new Workshop(santaReindeersList);
		
	}
	
}
